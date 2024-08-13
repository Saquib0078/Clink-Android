package com.visticsolution.posterbanao;

import static com.google.android.exoplayer2.ui.PlayerView.SHOW_BUFFERING_WHEN_PLAYING;
import static com.visticsolution.posterbanao.classes.Constants.SUCCESS;

import android.app.Dialog;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlayer;
//import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.visticsolution.posterbanao.classes.Functions;
import com.visticsolution.posterbanao.classes.PermissionUtils;
import com.visticsolution.posterbanao.classes.Variables;
import com.visticsolution.posterbanao.ads.RewardedAdapter;
import com.visticsolution.posterbanao.dialog.CustomeDialogFragment;
import com.visticsolution.posterbanao.dialog.DialogType;
import com.visticsolution.posterbanao.responses.UserResponse;
import com.visticsolution.posterbanao.viewmodel.UserViewModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PreviewActivity extends AppCompatActivity implements RewardedAdapter.Listener {

    private final ActivityResultLauncher<String[]> mPermissionResult = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onActivityResult(Map<String, Boolean> result) {
                    boolean allPermissionClear = true;
                    List<String> blockPermissionCheck = new ArrayList<>();
                    for (String key : result.keySet()) {
                        if (!(result.get(key))) {
                            allPermissionClear = false;
                            blockPermissionCheck.add(Functions.getPermissionStatus(PreviewActivity.this, key));
                        }
                    }
                    if (blockPermissionCheck.contains("blocked")) {
                        Functions.showPermissionSetting(PreviewActivity.this, getString(R.string.we_need_storage_and_camera_permission_for_upload_profile_pic));
                    }
                }
            });
    ImageView poster_iv;
    String path = "", file_name;
    PlayerView playerview;
    ExoPlayer exoplayer;
    RewardedAdapter rewardedAdapter;
    PermissionUtils takePermissionUtils;
    boolean isUploading = true;
    UserViewModel userViewModel;
    boolean isDownload = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        path = getIntent().getStringExtra("url");
        takePermissionUtils = new PermissionUtils(this, mPermissionResult);

        poster_iv = findViewById(R.id.poster_iv);
        playerview = findViewById(R.id.playerview);

        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.app_color));

        if (path.endsWith(".mp4")) {
            file_name = System.currentTimeMillis() + ".mp4";
            initializePlayer();
            playerview.setVisibility(View.VISIBLE);
            poster_iv.setVisibility(View.GONE);

            int appNameStringRes = R.string.app_name;
            String userAgent = com.google.android.exoplayer2.util.Util.getUserAgent(this, this.getString(appNameStringRes));
            DefaultDataSourceFactory defdataSourceFactory = new DefaultDataSourceFactory(this, userAgent);
            Uri uriOfContentUrl = Uri.parse(path);
            MediaItem mediaItem = MediaItem.fromUri(uriOfContentUrl);
            MediaSource mediaSource = new ProgressiveMediaSource.Factory(defdataSourceFactory).createMediaSource(mediaItem);  // creating a media source
            exoplayer.setMediaSource(mediaSource);
            exoplayer.prepare();
            exoplayer.setPlayWhenReady(true); // start loading video and play it at the moment a chunk of it is available offline
            playerview.setPlayer(exoplayer); // attach surface to the view
            playerview.setShowBuffering(SHOW_BUFFERING_WHEN_PLAYING);
//            exoplayer.addListener(new Player.EventListener() {
//
//                @Override
//                public void onLoadingChanged(boolean isLoading) {
//                    Player.EventListener.super.onLoadingChanged(isLoading);
//                }
//
//                @Override
//                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
//                    Player.EventListener.super.onPlayerStateChanged(playWhenReady, playbackState);
//                    if (playbackState == ExoPlayer.STATE_ENDED) {//                            ivPlayVideo.setVisibility(View.VISIBLE);
//                    }
//                }
//            });
        } else {
            file_name = System.currentTimeMillis() + ".jpg";
            playerview.setVisibility(View.GONE);
            poster_iv.setVisibility(View.VISIBLE);
            try {
                Glide.with(this).load(path).placeholder(R.drawable.placeholder).into(poster_iv);
            } catch (Exception e) {
            }
        }

        findViewById(R.id.whatsapp_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RewardedAdapter.isLoaded()) {
                    RewardedAdapter.showAds();
                } else {
                    download("whatsapp");
                }
            }
        });

        findViewById(R.id.instagram_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RewardedAdapter.isLoaded()) {
                    RewardedAdapter.showAds();
                } else {
                    download("instagram");
                }
            }
        });
        findViewById(R.id.facebook_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RewardedAdapter.isLoaded()) {
                    RewardedAdapter.showAds();
                } else {
                    download("facebook");
                }
            }
        });
        findViewById(R.id.more_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RewardedAdapter.isLoaded()) {
                    RewardedAdapter.showAds();
                } else {
                    download("more");
                }
            }
        });
        findViewById(R.id.save_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RewardedAdapter.isLoaded()) {
                    RewardedAdapter.showAds();
                } else {
                    download("save");
                }
            }
        });
        findViewById(R.id.progress_lay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        if (!path.startsWith("http")) {
            uploadPost();
        } else {
            findViewById(R.id.progress_lay).setVisibility(View.GONE);
            isUploading = false;
        }

        rewardedAdapter = new RewardedAdapter(this, this);
        RewardedAdapter.LoadAds();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void uploadPost() {
        findViewById(R.id.progress_lay).setVisibility(View.VISIBLE);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.uploadUserPost(Functions.getUID(this), path).observe(this, new Observer<UserResponse>() {
            @Override
            public void onChanged(UserResponse userResponse) {
                isUploading = false;
                findViewById(R.id.progress_lay).setVisibility(View.GONE);
                Functions.cancelLoader();
                if (userResponse != null) {
                    if (userResponse.code != SUCCESS) {
                        Toast.makeText(PreviewActivity.this, "Post not backup !2", Toast.LENGTH_SHORT).show();
                    }
                    try {
                        Functions.saveUserData(userResponse.userModel, PreviewActivity.this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(PreviewActivity.this, "Post not backup", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (exoplayer != null) {
            exoplayer.setPlayWhenReady(false);
        }
    }

    private void initializePlayer() {
        playerview.setUseController(true);
        playerview.setControllerHideOnTouch(true);
        playerview.setShowBuffering(SHOW_BUFFERING_WHEN_PLAYING);
//        playerview.setShowBuffering(true);
//        TrackSelector trackSelectorDef = new DefaultTrackSelector();
//        exoplayer = ExoPlayerFactory.newSimpleInstance(this, trackSelectorDef);
//        exoplayer = new ExoPlayer.Builder(this).build();

        TrackSelector trackSelectorDef = new DefaultTrackSelector(this);
        exoplayer = new SimpleExoPlayer.Builder(this)
                .setTrackSelector(trackSelectorDef)
                .build();
        playerview.setPlayer(exoplayer); // attach surface to the view


    }

    @Override
    public void onBackPressed() {
        if (isUploading) {
            Functions.showToast(this, getString(R.string.please_wait_post_is_uploading));
        } else {
            super.onBackPressed();
        }
    }

    public void finish(View view) {
        finish();
    }

    private void download(String name) {
        if (takePermissionUtils.isStorageCameraPermissionGranted()) {
            if (path.startsWith("http")) {
                downloadFromServer(name);
            } else {
                File file = new File(path);
                if (file.exists()) {
                    if (name.equals("save")) {
                        String filePath = Environment.getExternalStorageDirectory() + File.separator
                                + Environment.DIRECTORY_PICTURES + File.separator + getResources().getString(R.string.app_name);
                        File outfile = new File(filePath);
                        if (!outfile.exists()) {
                            outfile.mkdir();
                            outfile.mkdirs();
                        }
                        try {
                            moveFile(file, outfile);
                        } catch (IOException e) {
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        if (path.endsWith(".mp4")) {
                            intent.setType("video/*");
                        } else {
                            intent.setType("image/*");
                        }

                        Uri photoURI = FileProvider.getUriForFile(PreviewActivity.this, getPackageName() + ".provider", file);
                        intent.putExtra(Intent.EXTRA_STREAM, photoURI);

                        if (name.equals("whatsapp")) {
                            intent.setPackage("com.whatsapp");
                        } else if (name.equals("facebook")) {
                            intent.setPackage("com.facebook.katana");
                        } else if (name.equals("instagram")) {
                            intent.setPackage("com.instagram.android");
                        }
                        try {
                            startActivity(Intent.createChooser(intent, "Share Image Via"));
                        } catch (Exception e) {
                            Toast.makeText(PreviewActivity.this, name + " Not Installed ", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    savedSuccessfull();
                }
            }
        } else {
            takePermissionUtils.takeStorageCameraPermission();
        }
    }

    private void downloadFromServer(String name) {
        File appfolder = null;
        File file = null;
        if (name.equals("save")) {
            appfolder = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES
            ), "/" + getResources().getString(R.string.app_name));
        } else {
            appfolder = new File(Functions.getAppFolder(this) + Variables.APP_HIDED_FOLDER);
        }


        if (!appfolder.exists()) {
            if (!appfolder.mkdirs()) {
                Toast.makeText(this,
                        getResources().getString(R.string.create_dir_err),
                        Toast.LENGTH_LONG).show();
                return;
            }
        }

        file = new File(appfolder.getAbsolutePath() + "/" + file_name);
        if (file.exists()) {
            if (!name.equals("save")) {
                shareFile(file, name);
            } else {
                savedSuccessfull();
            }
        } else {
            Functions.showLoader(PreviewActivity.this);
            File finalFile = file;
            AndroidNetworking.download(Functions.getItemBaseUrl(getIntent().getStringExtra("url")), appfolder.getAbsolutePath(), file_name).build().startDownload(new DownloadListener() {
                public void onDownloadComplete() {
                    Functions.cancelLoader();
                    if (!name.equals("save")) {
                        shareFile(finalFile, name);
                    } else {
                        savedSuccessfull();
                    }
                }

                public void onError(ANError aNError) {
                    Functions.cancelLoader();
                    Toast.makeText(PreviewActivity.this, aNError.getErrorDetail(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void savedSuccessfull() {
        new CustomeDialogFragment(
                getString(R.string.sucsess),
                getString(R.string.image_saved),
                DialogType.SUCCESS,
                false,
                false,
                false,
                new CustomeDialogFragment.DialogCallback() {
                    @Override
                    public void onCencel() {
                    }

                    @Override
                    public void onSubmit() {
                    }

                    @Override
                    public void onDismiss() {

                    }

                    @Override
                    public void onComplete(Dialog dialog) {
                        dialog.dismiss();
                    }
                }
        ).show(getSupportFragmentManager(), "");
    }

    private void shareFile(File file, String name) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        if (path.endsWith(".mp4")) {
            intent.setType("video/*");
        } else {
            intent.setType("image/*");
        }
        Uri photoURI = FileProvider.getUriForFile(PreviewActivity.this, getPackageName() + ".provider", file);
        intent.putExtra(Intent.EXTRA_STREAM, photoURI);
        String shareText = Functions.getSharedPreference(this).getString("share_text", "").replace("REFER_CODE", Functions.getSharedPreference(this).getString(Variables.REFER_ID, ""));
        intent.putExtra(Intent.EXTRA_TEXT, shareText);
        if (name.equals("whatsapp")) {
            intent.setPackage("com.whatsapp");
        } else if (name.equals("facebook")) {
            intent.setPackage("com.facebook.katana");
        }
        try {
            startActivity(Intent.createChooser(intent, "Share Image Via"));
        } catch (Exception e) {
            Toast.makeText(this, name + " Not Installed ", Toast.LENGTH_SHORT).show();
        }
    }

    private void moveFile(File file, File dir) throws IOException {
        if (isDownload) {
            savedSuccessfull();
            return;
        }
        File newFile = new File(dir, file.getName());
        path = newFile.getAbsolutePath();
        FileChannel outputChannel = null;
        FileChannel inputChannel = null;
        try {
            outputChannel = new FileOutputStream(newFile).getChannel();
            inputChannel = new FileInputStream(file).getChannel();
            inputChannel.transferTo(0, inputChannel.size(), outputChannel);
            inputChannel.close();
            file.delete();
            isDownload = true;

            MediaScannerConnection.scanFile(this, new String[]{newFile.getAbsolutePath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String path, Uri uri) {

                        }
                    });
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            savedSuccessfull();
            if (inputChannel != null) inputChannel.close();
            if (outputChannel != null) outputChannel.close();
        }

    }

    @Override
    public void onAdLoaded() {

    }

    @Override
    public void onAdReward() {

    }

    @Override
    public void onAdFailedToLoad() {

    }

    @Override
    public void onAdDismissed() {

    }
}