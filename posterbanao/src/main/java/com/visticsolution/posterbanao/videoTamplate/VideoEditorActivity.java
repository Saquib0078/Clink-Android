package com.visticsolution.posterbanao.videoTamplate;

import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore.Images.Media;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
//import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Player.EventListener;
import com.google.android.exoplayer2.SimpleExoPlayer;
//import com.google.android.exoplayer2.source.ExtractorMediaSource.Factory;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.visticsolution.posterbanao.PreviewActivity;
import com.visticsolution.posterbanao.R;
import com.visticsolution.posterbanao.classes.Functions;
import com.visticsolution.posterbanao.model.VideoTamplateModel;
import com.visticsolution.posterbanao.videoTamplate.adapter.ImageListAdapter;
import com.visticsolution.posterbanao.videoTamplate.interfaces.OnClick;
import com.yalantis.ucrop.UCrop;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class VideoEditorActivity extends AppCompatActivity {

    public static final int REQUEST_PICK = 9162;
    public static String resizedVideoPathFinal;
    JSONObject jsonObj;
    int i;

    private String url;
    private String[] cmd;
    private TextView saveVideo;
    private int totalImage;
    private TextView btnTryAgain;
    private String imageList;
    private String ratio;
    private String ffCmd;
    private PlayerView exoPlayerVideoDetail;
    private String ffCmdVideo;
    private String ffUser;
    private String videoResolution;
    private String duration;
    private String colorKayRandom;
    private String opVideo;
    private LinearLayout layoutTryAgain;
    private String picPath;

    private int o = 0;
    private ApplyFiler applyFiler;
    private ProgressBar progressBarExoplayer;
    private RecyclerView recyclerView;
    private ImageListAdapter imageListAdapter;
    private SimpleExoPlayer simpleExoPlayer;
    private String[] totalImages;
    private ImageView thumb;
    private RelativeLayout videoList;
    private String waterMarkPath;
    private ArrayList<String> finalCommand;
    private String filesPath;
    private Activity context;

    public static VideoTamplateModel staticVideoModelData ;

    public VideoEditorActivity() {
        url = "";
    }

    @SuppressLint("IntentReset")
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_video_editor);
        context = this;
        initView();

        filesPath = getIntent().getStringExtra("filepath");


        // Copy  watermark to external storage
        try {
            copyRAWtoSDCard(Functions.getAppFolder(context));
        } catch (IOException e) {
            e.printStackTrace();
        }


        //path to the watermark

        waterMarkPath = Functions.getAppFolder(context) + "/watermark.png";

        finalCommand = new ArrayList<>();
        File yourFile = new File(filesPath + "/python.json");
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(yourFile);
            String jsonStr = null;
            try {

                FileChannel fc = stream.getChannel();
                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
                jsonStr = Charset.defaultCharset().decode(bb).toString();
                jsonObj = new JSONObject(jsonStr);
                totalImage = jsonObj.getJSONArray("images").length();

                totalImages = new String[totalImage];
                for (int count = 0; count < jsonObj.getJSONArray("images").length(); count++) {

                    totalImages[count] = filesPath + "/" + jsonObj.getJSONArray("images").getJSONObject(count).getString("name");
                }
                int width;
                int height;

                height = Integer.parseInt(jsonObj.getJSONObject("video").getString("h"));
                width = Integer.parseInt(jsonObj.getJSONObject("video").getString("w"));
                duration = jsonObj.getJSONObject("video").getString("duration");
                if (height > width) {
                    ratio = "9,16";
                } else {
                    ratio = "16,9";

                }


            } catch (Exception e) {
                Toast.makeText(context, "E -> "+e.getMessage(), Toast.LENGTH_SHORT).show();

            } finally {
                stream.close();
            }
        } catch (Exception e) {
            Toast.makeText(context, "EE -> "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        recyclerView = findViewById(R.id.num_img_list);

        if (totalImages != null && totalImages.length > 0) {
            imageListAdapter = new ImageListAdapter(context, totalImage, totalImages, new ClickAdapter());
            recyclerView.setLayoutManager(new GridLayoutManager((Context) context, 1, RecyclerView.HORIZONTAL, false));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(imageListAdapter);
        }
        ((ImageView) findViewById(R.id.back)).setOnClickListener(view -> onBackPressed());

        saveVideo.setOnClickListener(view -> {
            pausePlayer();
            Functions.showLoader(this);
            Functions.dialog.findViewById(R.id.tv_lay).setVisibility(View.VISIBLE);
            loaderMessageTv = Functions.dialog.findViewById(R.id.message_tv);
            applyFiler = new ApplyFiler();
            applyFiler.execute();
        });

        btnTryAgain.setOnClickListener(view -> {
            layoutTryAgain.setVisibility(View.GONE);
            progressBarExoplayer.setVisibility(View.VISIBLE);
            initializePlayer();
        });

    }

    TextView loaderMessageTv;
    private void initView() {

        exoPlayerVideoDetail = (PlayerView) findViewById(R.id.exo_player_video_detail);
        layoutTryAgain = (LinearLayout) findViewById(R.id.layout_try_again);
        progressBarExoplayer = (ProgressBar) findViewById(R.id.progressBar_exoplayer);
        btnTryAgain = (TextView) findViewById(R.id.btn_try_again);
        saveVideo = (TextView) findViewById(R.id.save_video);


    }

    public final String replaceToOriginal(String str) {
        return str.replace("{pythoncomplex}", "filter_complex").replace("{pythonmerge}", "alphamerge").replace("{pythono}", "overlay").replace("{pythonz}", "zoom").replace("{pythonf}", "fade");
    }

    private void beginCrop(Uri uri) {
        if (uri != null) {
            try {
                String[] split = ratio.split(",");
                Uri fromFile = Uri.fromFile(new File(context.getCacheDir(), "SampleCropImage" + System.currentTimeMillis() + ".png"));
                UCrop.Options options2 = new UCrop.Options();
                options2.withAspectRatio(Integer.parseInt(split[0]),Integer.parseInt(split[1]));
                options2.setCompressionFormat(Bitmap.CompressFormat.PNG);
                options2.setToolbarColor(getResources().getColor(R.color.backgroundColor));
                options2.setFreeStyleCropEnabled(true);
                UCrop.of(uri, fromFile).withOptions(options2).start(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private int getPercentages(int curntTime) {

        float progressF;
        int progress;
        int videoDuration = Integer.parseInt(duration);

        progressF = ((float) curntTime / (float) videoDuration) * 100;

        progress = (int) (progressF / 1000);

        if (progress >= 100) {
            return 100;
        } else {
            return progress;
        }
    }

    public void execureCommand(String[] strArr) {
        FFmpeg.executeAsync(strArr, (executionId, returnCode) -> {

            if (returnCode == RETURN_CODE_SUCCESS) {

                new Handler(Looper.getMainLooper()).post(() -> {
                    Functions.cancelLoader();
                    MediaScannerConnection.scanFile(getApplicationContext(), new String[]{new File(url).getAbsolutePath()}, new String[]{"mp4"}, null);

                    Intent intent = new Intent(context, PreviewActivity.class);
                    intent.putExtra("url", url);
                    startActivity(intent);
                    finish();
                });
            } else {

                new Handler(Looper.getMainLooper()).post(() -> {
                    Functions.cancelLoader();
                    Functions.showToast(context, "Failed.."+executionId);
                });

            }

        });

        Config.enableStatisticsCallback(statistics -> {

            if (loaderMessageTv != null) {
                loaderMessageTv.setText(String.valueOf(getPercentages(statistics.getTime())));
            }

        });


    }

    public void initializePlayer() {
//        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(context, new DefaultRenderersFactory(getApplicationContext()), new DefaultTrackSelector());
//        exoPlayerVideoDetail.setPlayer(simpleExoPlayer);
//        CacheDataSourceFactory cacheDataSourceFactory = new CacheDataSourceFactory(VideoCache.getInstance(this), new DefaultDataSourceFactory((Context) context, "MyVideoMakerApplication"));
//        try {
//            if (!(staticVideoModelData == null || TextUtils.isEmpty(staticVideoModelData.getVideo_url()))) {
//                simpleExoPlayer.prepare(new Factory(cacheDataSourceFactory).createMediaSource(Uri.parse(Functions.getItemBaseUrl(staticVideoModelData.getVideo_url()))));
//                simpleExoPlayer.setPlayWhenReady(true);
//                simpleExoPlayer.setRepeatMode(Player.REPEAT_MODE_ALL);
//                exoPlayerVideoDetail.hideController();
//
//                exoPlayerVideoDetail.setOnTouchListener(new View.OnTouchListener() {
//                    private final GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
//
//                        @Override
//                        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//                            super.onFling(e1, e2, velocityX, velocityY);
//                            float deltaY = e1.getX() - e2.getX();
//                            float deltaYAbs = Math.abs(deltaY);
//                            // Only when swipe distance between minimal and maximal distance value then we treat it as effective swipe
//                            if ((deltaYAbs > 100) && (deltaYAbs < 1000)) {
//                                if (deltaY > 0) {
//                                    onBackPressed();
//                                }
//                            }
//
//                            return true;
//                        }
//                        @Override
//                        public boolean onSingleTapUp(MotionEvent e) {
//                            super.onSingleTapUp(e);
//                            if (!simpleExoPlayer.getPlayWhenReady()) {
//                                simpleExoPlayer.setPlayWhenReady(true);
//                            } else {
//                                new Handler(getMainLooper()).postDelayed(() -> simpleExoPlayer.setPlayWhenReady(false), 200);
//                            }
//
//                            return true;
//                        }
//
//                        @Override
//                        public boolean onDoubleTap(MotionEvent e) {
//
//                            if (!simpleExoPlayer.getPlayWhenReady()) {
//                                simpleExoPlayer.setPlayWhenReady(true);
//                            }
//
//                            return super.onDoubleTap(e);
//
//                        }
//                    });
//
//                    @Override
//                    public boolean onTouch(View v, MotionEvent event) {
//                        gestureDetector.onTouchEvent(event);
//                        return true;
//                    }
//                });
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        simpleExoPlayer.addListener(new EventListener() {
//
//            @Override
//            public void onPlayerError(@NotNull ExoPlaybackException exoPlaybackException) {
//                if (exoPlaybackException.getMessage() != null && exoPlaybackException.getMessage().contains("Unable to connect")) {
//                    exoPlayerVideoDetail.hideController();
//                    layoutTryAgain.setVisibility(View.VISIBLE);
//                }
//            }
//
//            @Override
//            public void onPlayerStateChanged(boolean z, int i) {
//                progressBarExoplayer.setVisibility(i == 2 ? View.VISIBLE : View.INVISIBLE);
//            }
//
//        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == REQUEST_PICK && resultCode == -1) {
            beginCrop(intent.getData());
        } else if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            if (intent != null) {
                if (resultCode == -1) {
                    Uri uri = UCrop.getOutput(intent);
                    totalImages[o] = uri.getPath();
                    imageListAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(imageListAdapter);
                }
            } else {
            }
        }
    }

    private void copyRAWtoSDCard(String path) throws IOException {
        InputStream in = getResources().openRawResource(R.raw.watermark);
        File file = new File(path);
        file.mkdirs();
        FileOutputStream out = new FileOutputStream(path + "/watermark.png");
        byte[] buff = new byte[1024];
        int read = 0;
        try {
            while ((read = in.read(buff)) > 0) {
                out.write(buff, 0, read);
            }
        } finally {
            in.close();
            out.close();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override

    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        simpleExoPlayer.release();
    }

    @Override
    public void onPause() {
        super.onPause();
        pausePlayer();
    }

    @Override
    public void onRestoreInstanceState(Bundle bundle) {
        imageList = bundle.getString("image_list");
        videoResolution = bundle.getString("video_resolution");
        colorKayRandom = bundle.getString("colorkey_rand");
        duration = bundle.getString("duration");
        ffCmd = bundle.getString("ff_cmd");
        ffCmdVideo = bundle.getString("ff_cmd_video");
        ffUser = bundle.getString("ff_cmd_user");
        picPath = bundle.getString("picturePath");
        opVideo = bundle.getString("opt_video");
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            if (resizedVideoPathFinal != null) {
                recyclerView.setVisibility(View.GONE);
                videoList.setVisibility(View.VISIBLE);
                Glide.with(context).load(resizedVideoPathFinal).into(thumb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putString("total_image", String.valueOf(totalImage));
        bundle.putString("image_list", String.valueOf(imageList));
        bundle.putString("video_resolution", String.valueOf(videoResolution));
        bundle.putString("image_ratio", String.valueOf(ratio));
        bundle.putString("colorkey_rand", String.valueOf(colorKayRandom));
        bundle.putString("duration", String.valueOf(duration));
        bundle.putString("ff_cmd", String.valueOf(ffCmd));
        bundle.putString("ff_cmd_video", String.valueOf(ffCmdVideo));
        bundle.putString("ff_cmd_user", String.valueOf(ffUser));
        bundle.putString("picturePath", String.valueOf(picPath));
        bundle.putString("opt_video", String.valueOf(opVideo));
        super.onSaveInstanceState(bundle);
    }

    @Override
    public void onStart() {
        super.onStart();
        initializePlayer();
        pausePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        simpleExoPlayer.release();
    }

    public void pausePlayer() {
        simpleExoPlayer.setPlayWhenReady(false);
        simpleExoPlayer.getPlaybackState();
    }

    private static class VideoCache {
        static SimpleCache sDownloadCache;

        static SimpleCache getInstance(Context context) {
            if (sDownloadCache == null) {
                sDownloadCache = new SimpleCache(new File(context.getCacheDir(), "exoNewCache"), new LeastRecentlyUsedCacheEvictor(1073741824));
            }
            return sDownloadCache;
        }
    }

    public class ApplyFiler extends AsyncTask<Void, Integer, Void> {

        public Void doInBackground(Void... voidArr) {
            return doinback(voidArr);
        }

        @SuppressLint({"SdCardPath", "WrongConstant"})
        public Void doinback(Void... voidArr) {
            try {
//                String format = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(Calendar.getInstance().getTime());
                String stringBuilder = Functions.getAppFolder(context)+"AnimatedVideo/"+"video.mp4";

                if (!new File(Functions.getAppFolder(context)+"AnimatedVideo/").exists()){
                    new File(Functions.getAppFolder(context)+"AnimatedVideo/").mkdir();
                    new File(Functions.getAppFolder(context)+"AnimatedVideo/").mkdirs();
                }

                if (new File(stringBuilder).exists()){
                    new File(stringBuilder).delete();
                }

                finalCommand = new ArrayList<>();

                try {

                    for (int count = 0; count < jsonObj.getJSONArray("images").length(); count++) {

                        JSONArray tempArray = jsonObj.getJSONArray("images").getJSONObject(count).getJSONArray("prefix");
                        for (int count1 = 0; count1 < tempArray.length(); count1++) {
                            finalCommand.add(tempArray.getString(count1));
                        }
                        finalCommand.add(totalImages[count]);
                    }

                    for (int count = 0; count < jsonObj.getJSONArray("static_inputs").length(); count++) {

                        JSONArray tempArray = jsonObj.getJSONArray("static_inputs").getJSONObject(count).getJSONArray("prefix");
                        for (int count1 = 0; count1 < tempArray.length(); count1++) {
                            finalCommand.add(tempArray.getString(count1));
                        }
                        finalCommand.add(filesPath + "/" + jsonObj.getJSONArray("static_inputs").getJSONObject(count).getString("name"));
                    }

                    finalCommand.add("-i");
                    finalCommand.add(waterMarkPath);

                    JSONArray jSONArray = jsonObj.getJSONArray("m");
                    if (jSONArray.length() != 0) {
                        for (int i = 0; i < jSONArray.length(); i++) {
                            finalCommand.add(replaceToOriginal(jSONArray.getString(i)));
                        }
                    }

                    jSONArray = jsonObj.getJSONArray("r");
                    if (jSONArray.length() != 0) {
                        for (int i = 0; i < jSONArray.length(); i++) {
                            finalCommand.add(replaceToOriginal(jSONArray.getString(i)));
                        }
                    }

                    jSONArray = jsonObj.getJSONArray("d");
                    if (jSONArray.length() != 0) {
                        for (int i = 0; i < jSONArray.length(); i++) {
                            finalCommand.add(replaceToOriginal(jSONArray.getString(i)));
                        }
                    }

                    finalCommand.add("-preset");
                    finalCommand.add("ultrafast");
                    finalCommand.add(stringBuilder);

                } catch (Exception e) {
                    Log.d("FFMPEGException___", String.valueOf(e));
                }

                cmd = new String[finalCommand.size()];
                cmd = finalCommand.toArray(cmd);

                url = stringBuilder;
                if (cmd.length != 0) {
                    execureCommand(cmd);
                } else {
                    Toast.makeText(getApplicationContext(), "Command Empty", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.d("FFMPEGException___", "ee -> " + e);
            }
            return null;
        }

        @Override
        public void onPreExecute() {

        }
    }

    public class ClickAdapter implements OnClick {
        public void clickEvent(int i) {
            Intent intent = new Intent(Intent.ACTION_PICK, Media.EXTERNAL_CONTENT_URI);
            try {
                o = i;
                startActivityForResult(intent, REQUEST_PICK);
            } catch (ActivityNotFoundException unused) {
                Toast.makeText(context, R.string.crop__pick_error, Toast.LENGTH_SHORT).show();
            }
        }
    }


}
