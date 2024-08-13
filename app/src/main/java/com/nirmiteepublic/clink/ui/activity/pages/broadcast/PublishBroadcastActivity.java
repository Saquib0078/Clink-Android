package com.nirmiteepublic.clink.ui.activity.pages.broadcast;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.loader.content.CursorLoader;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.util.IOUtils;
import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.adapters.SliderAdapter;
import com.nirmiteepublic.clink.databinding.ActivityPublishBroadcastBinding;
import com.nirmiteepublic.clink.functions.helpers.PegaProgress;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;
import com.nirmiteepublic.clink.functions.retrofit.res.PegaCallback;
import com.nirmiteepublic.clink.functions.retrofit.res.PegaResponseManager;
import com.nirmiteepublic.clink.functions.viewmanagers.PegaAppCompatActivity;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class PublishBroadcastActivity extends PegaAppCompatActivity {

    private static final int REQUEST_CODE_SELECT_MEDIA = 104;
    private final ArrayList<Uri> selectedMediaUris = new ArrayList<>();
    private Uri selectedVideoUri;
    ActivityPublishBroadcastBinding binding;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPublishBroadcastBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setWindowThemeThird();
        setBackWithRightAnim();

        binding.back.setOnClickListener(v -> onBackPressed());
        binding.btnUploadImage.setOnClickListener(this::selectMedia);
        binding.publish.setOnClickListener(v -> validateAndPublish());
    }

    private void validateAndPublish() {
        String description = binding.description.getText().toString().trim();
        String broadcastUrl = binding.broadcastUrl.getText().toString().trim();

        if (!broadcastUrl.isEmpty() && !PegaProgress.isValidUrl(broadcastUrl)) {
            binding.broadcastUrl.setError("Please enter a valid URL");
            return;
        }

        boolean hasMedia = !selectedMediaUris.isEmpty() || selectedVideoUri != null;

        if (description.isEmpty() && broadcastUrl.isEmpty() && !hasMedia) {
            Toast.makeText(this, "Please provide at least one of: description, URL, or media", Toast.LENGTH_SHORT).show();
            return;
        }

//   /
//            if (selectedVideoUri != null) {
//                type = "mp4";
//            } else {
//                type = "jpg";
//            }
//        } else {
//            type = "general";
//        }

        showProgressDialog();
        publishBroadcast(description, broadcastUrl);
        //  }
    }


    private void selectMedia(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        String[] mimeTypes = {"image/*", "video/*"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Select Media"), REQUEST_CODE_SELECT_MEDIA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_MEDIA && resultCode == RESULT_OK && data != null) {
            handleMediaSelection(data);
            showMediaPreviews();
        }
    }

    private void handleMediaSelection(Intent data) {
        selectedMediaUris.clear();
        selectedVideoUri = null;

        if (data.getClipData() != null) {
            int count = data.getClipData().getItemCount();
            for (int i = 0; i < count; i++) {
                Uri uri = data.getClipData().getItemAt(i).getUri();
                processUri(uri);
            }
        } else if (data.getData() != null) {
            processUri(data.getData());
        }
    }

    private void processUri(Uri uri) {
        if (isVideoFile(uri)) {
            selectedVideoUri = uri;
            selectedMediaUris.clear();
        } else if (isImageFile(uri) && selectedVideoUri == null) {
            selectedMediaUris.add(uri);
        }
    }

    private boolean isImageFile(Uri uri) {
        String mimeType = getContentResolver().getType(uri);
        return mimeType != null && mimeType.startsWith("image");
    }

    private boolean isVideoFile(Uri uri) {
        String mimeType = getContentResolver().getType(uri);
        return mimeType != null && mimeType.startsWith("video");
    }

    private void showMediaPreviews() {
        if (selectedVideoUri != null) {
            showVideoPreview();
        } else if (selectedMediaUris.isEmpty()) {
            hideAllPreviews();
        } else if (selectedMediaUris.size() == 1) {
            showSingleImagePreview();
        } else {
            showMultipleImagePreviews();
        }
    }

    private void showVideoPreview() {
        binding.image.setVisibility(View.GONE);
        binding.slider.setVisibility(View.GONE);
        binding.video.setVisibility(View.VISIBLE);
        binding.video.setVideoURI(selectedVideoUri);
        binding.video.setOnPreparedListener(mp -> {
            mp.setLooping(true);
            binding.video.start();
        });
    }

    private void hideAllPreviews() {
        binding.image.setVisibility(View.GONE);
        binding.slider.setVisibility(View.GONE);
        binding.video.setVisibility(View.GONE);
    }

    private void showSingleImagePreview() {
        binding.image.setVisibility(View.VISIBLE);
        binding.slider.setVisibility(View.GONE);
        binding.video.setVisibility(View.GONE);
        Glide.with(this).load(selectedMediaUris.get(0)).placeholder(R.drawable.round_image_placeholder).into(binding.image);
    }

    private void showMultipleImagePreviews() {
        binding.image.setVisibility(View.GONE);
        binding.slider.setVisibility(View.VISIBLE);
        binding.video.setVisibility(View.GONE);
        SliderAdapter adapter = new SliderAdapter(null, "image", selectedMediaUris,this, view -> {
        });
        binding.slider.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        binding.slider.setSliderAdapter(adapter);
        binding.slider.setAutoCycle(false);
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Publishing Broadcast...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void publishBroadcast(String description, String broadcastUrl) {
        List<MultipartBody.Part> mediaParts = new ArrayList<>();

        if (selectedVideoUri != null) {
            MultipartBody.Part videoPart = prepareMediaPart(selectedVideoUri, "video");
            if (videoPart != null) {
                mediaParts.add(videoPart);
            }
        } else if (!selectedMediaUris.isEmpty()) {
            for (Uri uri : selectedMediaUris) {
                MultipartBody.Part imagePart = prepareMediaPart(uri, "image");
                if (imagePart != null) {
                    mediaParts.add(imagePart);
                }
            }
        }

        RequestBody descriptionPart = TextUtils.isEmpty(description) ? null :
                RequestBody.create(MediaType.parse("multipart/form-data"), description);
        RequestBody broadcastUrlPart = TextUtils.isEmpty(broadcastUrl) ? null :
                RequestBody.create(MediaType.parse("multipart/form-data"), broadcastUrl);
//        RequestBody typePart = RequestBody.create(MediaType.parse("multipart/form-data"), type);

        RetrofitClient.getInstance(this).getApiInterfaces()
                .publishBroadcast("broadcasts", broadcastUrlPart, mediaParts.isEmpty() ? null : mediaParts, descriptionPart)
                .enqueue(new PegaResponseManager(new PegaCallback(this) {
                    @Override
                    public void onSuccess(@Nullable JSONObject data) {
                        dismissProgressDialog();
                        Toast.makeText(PublishBroadcastActivity.this, "Broadcast Published Successfully", Toast.LENGTH_SHORT).show();
//                        setResult(Activity.RESULT_OK, new Intent().putExtra("status", true));
                        Intent resultIntent = new Intent();
                        if (data != null) {
                            resultIntent.putExtra("newBroadcast", data.toString());
                        } else {
                            // If data is null, create a minimal JSONObject with only the description and broadcastUrl
                            try {
                                JSONObject minimalData = new JSONObject();
                                minimalData.put("description", description);
                                minimalData.put("broadcastUrl", broadcastUrl);
                                resultIntent.putExtra("newBroadcast", minimalData.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        setResult(Activity.RESULT_OK, resultIntent);

                        finish();
                    }
                }));
    }

    private MultipartBody.Part prepareMediaPart(Uri uri, String type) {
        try {
            String filePath = getRealPathFromURI(uri);
            if (filePath == null) {
                // If we couldn't get the file path, try to use the URI directly
                return prepareMediaPartFromUri(uri, type);
            }

            File file = new File(filePath);
            if (!file.exists()) {
                // If the file doesn't exist, fall back to using the URI
                return prepareMediaPartFromUri(uri, type);
            }

            RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(uri)), file);
            return MultipartBody.Part.createFormData("media", file.getName(), requestFile);
        } catch (Exception e) {
            e.printStackTrace();
            // If any exception occurs, try to use the URI directly
            return prepareMediaPartFromUri(uri, type);
        }
    }

    private MultipartBody.Part prepareMediaPartFromUri(Uri uri, String type) {
        try {
            String fileName = getFileNameFromUri(uri);
            InputStream inputStream = getContentResolver().openInputStream(uri);
            if (inputStream == null) {
                Toast.makeText(this, "Unable to process this media file", Toast.LENGTH_SHORT).show();
                return null;
            }
            byte[] mediaData = IOUtils.toByteArray(inputStream);
            RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(uri)), mediaData);
            return MultipartBody.Part.createFormData("media", fileName, requestFile);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error processing media file", Toast.LENGTH_SHORT).show();
            return null;
        }
    }




    private String getFileNameFromUri(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (index != -1) {
                        result = cursor.getString(index);
                    }
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private String getRealPathFromURI(Uri contentUri) {
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
            Cursor cursor = loader.loadInBackground();
            if (cursor == null) return null;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String result = cursor.getString(column_index);
            cursor.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}




//public class PublishBroadcastActivity extends PegaAppCompatActivity {
//
//    private static final int REQUEST_CODE_SELECT_MEDIA = 104;
//    private final ArrayList<Uri> selectedMediaUris = new ArrayList<>();
//    private Uri selectedVideoUri;
//    ActivityPublishBroadcastBinding binding;
//    private ProgressDialog progressDialog;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityPublishBroadcastBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//        setWindowThemeThird();
//        setBackWithRightAnim();
//
//        binding.back.setOnClickListener(v -> onBackPressed());
//        binding.btnUploadImage.setOnClickListener(this::selectMedia);
//        binding.publish.setOnClickListener(v -> validateAndPublish());
//    }
//
//    private void validateAndPublish() {
//        String description = binding.description.getText().toString().trim();
//        String broadcastUrl = binding.broadcastUrl.getText().toString().trim();
//
//        if (!broadcastUrl.isEmpty() && !PegaProgress.isValidUrl(broadcastUrl)) {
//            binding.broadcastUrl.setError("Please enter a valid URL");
//            return;
//        }
//
//        boolean hasMedia = !selectedMediaUris.isEmpty() || selectedVideoUri != null;
//
//        if (description.isEmpty() && broadcastUrl.isEmpty() && !hasMedia) {
//            Toast.makeText(this, "Please provide at least one of: description, URL, or media", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        showProgressDialog();
//        publishBroadcast(description, broadcastUrl);
//    }
//
//    private void selectMedia(View view) {
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("*/*");
//        String[] mimeTypes = {"image/*", "video/*"};
//        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//        startActivityForResult(Intent.createChooser(intent, "Select Media"), REQUEST_CODE_SELECT_MEDIA);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_CODE_SELECT_MEDIA && resultCode == RESULT_OK && data != null) {
//            handleMediaSelection(data);
//            showMediaPreviews();
//        }
//    }
//
//    private void handleMediaSelection(Intent data) {
//        selectedMediaUris.clear();
//        selectedVideoUri = null;
//
//        if (data.getClipData() != null) {
//            int count = data.getClipData().getItemCount();
//            for (int i = 0; i < count; i++) {
//                Uri uri = data.getClipData().getItemAt(i).getUri();
//                processUri(uri);
//            }
//        } else if (data.getData() != null) {
//            processUri(data.getData());
//        }
//    }
//
//    private void processUri(Uri uri) {
//        if (isVideoFile(uri)) {
//            selectedVideoUri = uri;
//            selectedMediaUris.clear();
//        } else if (isImageFile(uri) && selectedVideoUri == null) {
//            selectedMediaUris.add(uri);
//        }
//    }
//
//    private boolean isImageFile(Uri uri) {
//        String mimeType = getContentResolver().getType(uri);
//        return mimeType != null && mimeType.startsWith("image");
//    }
//
//    private boolean isVideoFile(Uri uri) {
//        String mimeType = getContentResolver().getType(uri);
//        return mimeType != null && mimeType.startsWith("video");
//    }
//
//    private void showMediaPreviews() {
//        if (selectedVideoUri != null) {
//            showVideoPreview();
//        } else if (selectedMediaUris.isEmpty()) {
//            hideAllPreviews();
//        } else if (selectedMediaUris.size() == 1) {
//            showSingleImagePreview();
//        } else {
//            showMultipleImagePreviews();
//        }
//    }
//
//    private void showVideoPreview() {
//        binding.image.setVisibility(View.GONE);
//        binding.slider.setVisibility(View.GONE);
//        binding.video.setVisibility(View.VISIBLE);
//        binding.video.setVideoURI(selectedVideoUri);
//        binding.video.setOnPreparedListener(mp -> {
//            mp.setLooping(true);
//            binding.video.start();
//        });
//    }
//
//    private void hideAllPreviews() {
//        binding.image.setVisibility(View.GONE);
//        binding.slider.setVisibility(View.GONE);
//        binding.video.setVisibility(View.GONE);
//    }
//
//    private void showSingleImagePreview() {
//        binding.image.setVisibility(View.VISIBLE);
//        binding.slider.setVisibility(View.GONE);
//        binding.video.setVisibility(View.GONE);
//        Glide.with(this).load(selectedMediaUris.get(0)).placeholder(R.drawable.round_image_placeholder).into(binding.image);
//    }
//
//    private void showMultipleImagePreviews() {
//        binding.image.setVisibility(View.GONE);
//        binding.slider.setVisibility(View.VISIBLE);
//        binding.video.setVisibility(View.GONE);
//        SliderAdapter adapter = new SliderAdapter(this, null, selectedMediaUris, view -> {});
//        binding.slider.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
//        binding.slider.setSliderAdapter(adapter);
//        binding.slider.setAutoCycle(false);
//    }
//
//    private void showProgressDialog() {
//        progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Publishing Broadcast...");
//        progressDialog.setCancelable(false);
//        progressDialog.show();
//    }
//
//    private void dismissProgressDialog() {
//        if (progressDialog != null && progressDialog.isShowing()) {
//            progressDialog.dismiss();
//        }
//    }
//
//    private void publishBroadcast(String description, String broadcastUrl) {
//        List<MultipartBody.Part> mediaParts = new ArrayList<>();
//
//        if (selectedVideoUri != null) {
//            MultipartBody.Part videoPart = prepareMediaPart(selectedVideoUri, "video");
//            if (videoPart != null) {
//                mediaParts.add(videoPart);
//            }
//        } else if (!selectedMediaUris.isEmpty()) {
//            for (Uri uri : selectedMediaUris) {
//                MultipartBody.Part imagePart = prepareMediaPart(uri, "image");
//                if (imagePart != null) {
//                    mediaParts.add(imagePart);
//                }
//            }
//        }
//
//        RequestBody descriptionPart = TextUtils.isEmpty(description) ? null :
//                RequestBody.create(MediaType.parse("multipart/form-data"), description);
//        RequestBody broadcastUrlPart = TextUtils.isEmpty(broadcastUrl) ? null :
//                RequestBody.create(MediaType.parse("multipart/form-data"), broadcastUrl);
//
//        RetrofitClient.getInstance(this).getApiInterfaces()
//                .publishBroadcast("broadcasts", broadcastUrlPart, mediaParts.isEmpty() ? null : mediaParts, descriptionPart)
//                .enqueue(new PegaResponseManager(new PegaCallback(this) {
//                    @Override
//                    public void onSuccess(@Nullable JSONObject data) {
//                        dismissProgressDialog();
//                        Toast.makeText(PublishBroadcastActivity.this, "Broadcast Published Successfully", Toast.LENGTH_SHORT).show();
//                        setResult(Activity.RESULT_OK, new Intent().putExtra("status", true));
//                        finish();
//                    }
//                }));
//    }
//
//    private MultipartBody.Part prepareMediaPart(Uri uri, String type) {
//        try {
//            String filePath = getRealPathFromURI(uri);
//            if (filePath == null) {
//                // If we couldn't get the file path, try to use the URI directly
//                return prepareMediaPartFromUri(uri, type);
//            }
//
//            File file = new File(filePath);
//            if (!file.exists()) {
//                // If the file doesn't exist, fall back to using the URI
//                return prepareMediaPartFromUri(uri, type);
//            }
//
//            RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(uri)), file);
//            return MultipartBody.Part.createFormData("media", file.getName(), requestFile);
//        } catch (Exception e) {
//            e.printStackTrace();
//            // If any exception occurs, try to use the URI directly
//            return prepareMediaPartFromUri(uri, type);
//        }
//    }
//
//    private MultipartBody.Part prepareMediaPartFromUri(Uri uri, String type) {
//        try {
//            String fileName = getFileNameFromUri(uri);
//            InputStream inputStream = getContentResolver().openInputStream(uri);
//            if (inputStream == null) {
//                Toast.makeText(this, "Unable to process this media file", Toast.LENGTH_SHORT).show();
//                return null;
//            }
//            byte[] mediaData = IOUtils.toByteArray(inputStream);
//            RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(uri)), mediaData);
//            return MultipartBody.Part.createFormData("media", fileName, requestFile);
//        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(this, "Error processing media file", Toast.LENGTH_SHORT).show();
//            return null;
//        }
//    }
//
//    private String getFileNameFromUri(Uri uri) {
//        String result = null;
//        if (uri.getScheme().equals("content")) {
//            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
//                if (cursor != null && cursor.moveToFirst()) {
//                    int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
//                    if (index != -1) {
//                        result = cursor.getString(index);
//                    }
//                }
//            }
//        }
//        if (result == null) {
//            result = uri.getPath();
//            int cut = result.lastIndexOf('/');
//            if (cut != -1) {
//                result = result.substring(cut + 1);
//            }
//        }
//        return result;
//    }
//
//    private String getRealPathFromURI(Uri contentUri) {
//        try {
//            String[] proj = {MediaStore.Images.Media.DATA};
//            CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
//            Cursor cursor = loader.loadInBackground();
//            if (cursor == null) return null;
//            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            cursor.moveToFirst();
//            String result = cursor.getString(column_index);
//            cursor.close();
//            return result;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//}