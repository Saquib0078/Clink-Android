package com.nirmiteepublic.clink.ui.activity.pages;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;

import com.nirmiteepublic.clink.adapters.MediaAdapter;
import com.nirmiteepublic.clink.databinding.ActivitySelectMediaBinding;
import com.nirmiteepublic.clink.functions.helpers.GridSpacingItemDecoration;
import com.nirmiteepublic.clink.functions.viewmanagers.PegaAppCompatActivity;
import com.nirmiteepublic.clink.models.MediaModel;
import com.nirmiteepublic.clink.ui.activity.pages.broadcast.PublishBroadcastActivity;

import java.util.ArrayList;
import java.util.List;

public class SelectMediaActivity extends PegaAppCompatActivity {

    public static final int REQUEST_CODE_PUBLISH_BROADCAST = 103;
    private static final int REQUEST_CODE_READ_IMAGES = 100;
    private static final int REQUEST_CODE_READ_VIDEOS = 101;
    private static final int REQUEST_CODE_READ_STORAGE = 102;
    private static final int MAX_VIDEO_LENGTH = 45000;
    ActivitySelectMediaBinding binding;
    MediaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectMediaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setBackWithRightAnim();
        setWindowThemeThird();

        binding.back.setOnClickListener(v -> onBackPressed());
        binding.proceed.setOnClickListener(view -> proceed());
        Thread thread = new Thread(this::loadMedia);
        thread.start();
    }

    private void proceed() {
//        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)
//                this, binding.image, "media_trans_image");
        List<MediaModel> selectedMediaModels = adapter.getSelectedMediaModels();
        if (!selectedMediaModels.isEmpty()) {
            Intent intent = new Intent(this, PublishBroadcastActivity.class);
            ArrayList<String> uris = new ArrayList<>();
            for (MediaModel mediaModel : selectedMediaModels) {
                uris.add(mediaModel.getUri().toString());
            }
            intent.putStringArrayListExtra("uri", uris);
            startActivityForResult(intent, SelectMediaActivity
                    .REQUEST_CODE_PUBLISH_BROADCAST);
        } else {
            Toast.makeText(this, "No images selected", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkMediaPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                String[] permissions = {android.Manifest.permission.READ_MEDIA_IMAGES, android.Manifest.permission.READ_MEDIA_VIDEO};
                ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_READ_IMAGES);
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_READ_STORAGE);
            }
        }
        return false;
    }

    private void loadMedia() {
        if (checkMediaPermission()) {
            ArrayList<MediaModel> mediaItems = getAllMedia();
            runOnUiThread(() -> {
                binding.progressBar.setVisibility(View.GONE);
                adapter = new MediaAdapter(mediaItems);
                binding.recyclerView.addItemDecoration(new GridSpacingItemDecoration(4, getResources().getDimensionPixelOffset(com.intuit.sdp.R.dimen._1sdp), false));
                binding.recyclerView.setAdapter(adapter);
            });

        }
    }

    private ArrayList<MediaModel> getAllMedia() {
        ArrayList<MediaModel> mediaItems = new ArrayList<>();

        ContentResolver contentResolver = getContentResolver();

        Uri queryUri = MediaStore.Files.getContentUri("external");

        String[] projection = {MediaStore.Files.FileColumns._ID, MediaStore.Files.FileColumns.DATA, MediaStore.Files.FileColumns.DATE_ADDED, MediaStore.Files.FileColumns.MEDIA_TYPE, MediaStore.Files.FileColumns.MIME_TYPE, MediaStore.Video.VideoColumns.DURATION};

        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "=" + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE + " OR " + MediaStore.Files.FileColumns.MEDIA_TYPE + "=" + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

        try (Cursor cursor = contentResolver.query(queryUri, projection, selection, null, MediaStore.Files.FileColumns.DATE_ADDED + " DESC")) {
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    int mediaType = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE));
                    long duration = (mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) ? cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DURATION)) : 0;
                    if (duration > MAX_VIDEO_LENGTH) { // 45 Seconds in MS
                        continue;
                    }

                    long fileId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID));
                    String filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA));
                    long dateAdded = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_ADDED));
                    String mimeType = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE));

                    Uri mediaUri = Uri.withAppendedPath(MediaStore.Files.getContentUri("external"), String.valueOf(fileId));

                    mediaItems.add(new MediaModel(mediaUri, dateAdded, duration, mediaType, mimeType, filePath));
                }
            }
        }

        mediaItems.sort((o1, o2) -> Long.compare(o2.getDateAdded(), o1.getDateAdded()));

        return mediaItems;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PUBLISH_BROADCAST && data != null) {
            setResult(RESULT_OK, new Intent().putExtra("result", true));
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_READ_STORAGE) {
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission denied. Cannot load media.", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                loadMedia();
                return;
            }
        } else if (requestCode == REQUEST_CODE_READ_IMAGES || requestCode == REQUEST_CODE_READ_VIDEOS) {
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission denied. Cannot load media.", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
            }

            loadMedia();
        }
    }

}