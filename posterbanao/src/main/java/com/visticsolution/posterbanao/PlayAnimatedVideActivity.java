package com.visticsolution.posterbanao;

import static com.google.android.exoplayer2.ui.PlayerView.SHOW_BUFFERING_WHEN_PLAYING;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
//import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Renderer;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.visticsolution.posterbanao.adapter.VideoTamplateAdapter;
import com.visticsolution.posterbanao.classes.Functions;
import com.visticsolution.posterbanao.databinding.ActivityPlayAnimatedVideBinding;
import com.visticsolution.posterbanao.dialog.UpgradeDialogFragment;
import com.visticsolution.posterbanao.interfaces.AdapterClickListener;
import com.visticsolution.posterbanao.model.VideoTamplateCategory;
import com.visticsolution.posterbanao.model.VideoTamplateModel;
import com.visticsolution.posterbanao.videoTamplate.VideoEditorActivity;
import com.visticsolution.posterbanao.viewmodel.HomeViewModel;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class PlayAnimatedVideActivity extends AppCompatActivity {

    public static VideoTamplateCategory selectedCategory;
    public VideoTamplateModel selectedVideo;
    ActivityPlayAnimatedVideBinding binding;
    Context context;
    SimpleExoPlayer exoplayer;
    HomeViewModel homeViewModel;
    List<VideoTamplateModel> list = new ArrayList<>();
    TextView dialogMessageTv;
    String zipfoldername = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayAnimatedVideBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = this;
        binding.titleTv.setText(selectedCategory.getName());
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        initializePlayer();
        if (!selectedCategory.getVideos().isEmpty()) {
            selectedVideo = selectedCategory.getVideos().get(0);
            playVideo(selectedVideo.getVideo_url());
            list.add(selectedCategory.getVideos().get(0));

        }
        homeViewModel.getVideoTamplates(selectedCategory.getId()).observe(this, new Observer<List<VideoTamplateModel>>() {
            @Override
            public void onChanged(List<VideoTamplateModel> videoTamplateModels) {
                if (videoTamplateModels != null) {
                    try {
                        videoTamplateModels.remove(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    list.addAll(videoTamplateModels);
                    binding.recyclerView.setAdapter(new VideoTamplateAdapter(context, list, new AdapterClickListener() {
                        @Override
                        public void onItemClick(View view, int pos, Object object) {
                            VideoTamplateModel model = (VideoTamplateModel) object;
                            selectedVideo = model;
                            playVideo(model.getVideo_url());
                        }
                    }));
                }
            }
        });
        binding.nextBtn.setOnClickListener(view -> {
            if (selectedVideo.getPremium().equals("1")) {
                if (Functions.IsPremiumEnable(context)) {
                    downloadZip();
                } else {
                    new UpgradeDialogFragment(getString(R.string.create_animated_video_alert)).show(getSupportFragmentManager(), "");
                }
            } else {
                downloadZip();
            }
        });
    }

    private void playVideo(String url) {
        int appNameStringRes = R.string.app_name;
        String userAgent = com.google.android.exoplayer2.util.Util.getUserAgent(this, this.getString(appNameStringRes));
        DefaultDataSourceFactory defdataSourceFactory = new DefaultDataSourceFactory(this, userAgent);
        Uri uriOfContentUrl = Uri.parse(Functions.getItemBaseUrl(url));
        MediaSource mediaSource = new ProgressiveMediaSource.Factory(defdataSourceFactory).createMediaSource(uriOfContentUrl);  // creating a media source

        exoplayer.prepare(mediaSource);
        exoplayer.setPlayWhenReady(true); // start loading video and play it at the moment a chunk of it is available offline
        exoplayer.addListener(new Player.EventListener() {

            @Override
            public void onLoadingChanged(boolean isLoading) {
                Player.EventListener.super.onLoadingChanged(isLoading);
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                Player.EventListener.super.onPlayerStateChanged(playWhenReady, playbackState);
                if (playbackState == ExoPlayer.STATE_ENDED) {//                            ivPlayVideo.setVisibility(View.VISIBLE);
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
        binding.playerview.setUseController(true);
        binding.playerview.setControllerHideOnTouch(true);
        binding.playerview.setShowBuffering(SHOW_BUFFERING_WHEN_PLAYING);
        TrackSelector trackSelectorDef = new DefaultTrackSelector();
//        exoplayer = ExoPlayerFactory.newSimpleInstance(this, trackSelectorDef);
//        exoplayer = new ExoPlayer.Builder(this).build();
        exoplayer = new SimpleExoPlayer.Builder(this)
                .setTrackSelector(trackSelectorDef)
                .build();
        binding.playerview.setPlayer(exoplayer); // attach surface to the view
        binding.playerview.setShowBuffering(SHOW_BUFFERING_WHEN_PLAYING);
    }

    private void downloadZip() {
        Functions.showLoader(PlayAnimatedVideActivity.this);
        Functions.dialog.findViewById(R.id.tv_lay).setVisibility(View.VISIBLE);
        dialogMessageTv = Functions.dialog.findViewById(R.id.message_tv);
        dialogMessageTv.setText("Downloading...");

        File appfolder = new File(Functions.getAppFolder(this) + "VideoTemplates");
        try {
            URL url = new URL(Functions.getItemBaseUrl(selectedVideo.getZip_url()));
            String name = System.currentTimeMillis() + ".zip";
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                name = Paths.get(url.getPath()).getFileName().toString();
            }
            if (new File(appfolder.getAbsolutePath(), name).exists()) {

            } else {
                String finalName = name;
                AndroidNetworking.download(Functions.getItemBaseUrl(selectedVideo.getZip_url()), appfolder.getAbsolutePath(), name).build().startDownload(new DownloadListener() {
                    public void onDownloadComplete() {
                        try {
                            dialogMessageTv.setText("Unziping...");
                            unzip(new File(appfolder.getAbsolutePath(), finalName), new File(appfolder.getAbsolutePath()));
                        } catch (IOException ex) {
                            Functions.cancelLoader();
                        }
                    }

                    public void onError(ANError aNError) {
                        Functions.cancelLoader();
                        Toast.makeText(context, aNError.getErrorDetail(), Toast.LENGTH_SHORT).show();
                    }

                });
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    public void unzip(File zipFile, File targetDirectory) throws IOException {
        ZipInputStream zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile)));
        try {
            ZipEntry ze;
            int count;
            byte[] buffer = new byte[8192];
            while ((ze = zis.getNextEntry()) != null) {
                if (zipfoldername.equals("")) {
                    zipfoldername = ze.getName();
                }
                File file = new File(targetDirectory, ze.getName());
                File dir = ze.isDirectory() ? file : file.getParentFile();
                if (!dir.isDirectory() && !dir.mkdirs())
                    throw new FileNotFoundException("Failed to ensure directory: " +
                            dir.getAbsolutePath());
                if (ze.isDirectory())
                    continue;
                FileOutputStream fout = new FileOutputStream(file);
                try {
                    while ((count = zis.read(buffer)) != -1)
                        fout.write(buffer, 0, count);
                } finally {
                    fout.close();
                }
            }

            Functions.cancelLoader();
            zipFile.delete();

            VideoEditorActivity.staticVideoModelData = selectedVideo;
            Intent intent = new Intent(context, VideoEditorActivity.class);
            intent.putExtra("filepath", targetDirectory.getAbsolutePath() + "/" + zipfoldername);
            startActivity(intent);
            finish();
        } finally {
            zis.close();
        }
    }

    public void finish(View view) {
        finish();
    }
}