package com.visticsolution.posterbanao;

import static android.view.View.DRAWING_CACHE_QUALITY_HIGH;
import static com.google.android.exoplayer2.ui.PlayerView.SHOW_BUFFERING_WHEN_PLAYING;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.MediaMetadataRetriever;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.arthenica.mobileffmpeg.ExecuteCallback;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
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
import com.visticsolution.posterbanao.adapter.PagerFrameAdapter;
import com.visticsolution.posterbanao.adapter.PostsSelectedAdapter;
import com.visticsolution.posterbanao.adapter.TabRecyclerAdapter;
import com.visticsolution.posterbanao.ads.BannerAdapter;
import com.visticsolution.posterbanao.ads.RewardedAdapter;
import com.visticsolution.posterbanao.binding.BindingAdaptet;
import com.visticsolution.posterbanao.classes.Callback;
import com.visticsolution.posterbanao.classes.Functions;
import com.visticsolution.posterbanao.classes.Variables;
import com.visticsolution.posterbanao.databinding.ActivityOpenPostBinding;
import com.visticsolution.posterbanao.dialog.CallBack;
import com.visticsolution.posterbanao.dialog.LanguageDialogFragment;
import com.visticsolution.posterbanao.dialog.PaymentTypeDialogFragment;
import com.visticsolution.posterbanao.dialog.RemoveWatermarkDialog;
import com.visticsolution.posterbanao.dialog.UpgradeDialogFragment;
import com.visticsolution.posterbanao.editor.EditorActivity;
import com.visticsolution.posterbanao.editor.utility.ImageUtils;
import com.visticsolution.posterbanao.editor.utils.TamplateUtils;
import com.visticsolution.posterbanao.interfaces.AdapterClickListener;
import com.visticsolution.posterbanao.model.CategoryModel;
import com.visticsolution.posterbanao.model.FrameModel;
import com.visticsolution.posterbanao.model.PostsModel;
import com.visticsolution.posterbanao.payment.CCAvenueActivity;
import com.visticsolution.posterbanao.payment.InstamojoActivity;
import com.visticsolution.posterbanao.payment.PaytmActivity;
import com.visticsolution.posterbanao.payment.RazorpayActivity;
import com.visticsolution.posterbanao.payment.StripeActivity;
import com.visticsolution.posterbanao.responses.FrameResponse;
import com.visticsolution.posterbanao.responses.HomeResponse;
import com.visticsolution.posterbanao.viewmodel.FrameViewModel;
import com.visticsolution.posterbanao.viewmodel.HomeViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OpenPostActivity extends AppCompatActivity {

    ActivityOpenPostBinding binding;
    String type = ""; // Section || Category || Greeting
    String subcategory = ""; // Section || Category || Greeting
    String postType = "images"; // Images || Video || Greeting
    String item_id = "";
    List<PostsModel> list = new ArrayList<>();
    int pageCount = 0;
    boolean loading = false,firstTime = true;
    HomeViewModel homeViewModel;
    SimpleExoPlayer exoplayer;
    int width = 0;
    int height = 0;
    PostsModel selectedPostsModel;
    PostsSelectedAdapter postsAdapter;
    StaggeredGridLayoutManager layoutManager;
    FrameViewModel frameViewModel;

    String ratio = "";
    String newRatio = "";
    float screenHeight;
    float screenWidth;
    Bitmap bit;
    float wr = 1.0f;
    float hr = 1.0f;
    Context context;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        Functions.setLocale(Functions.getSharedPreference(this).getString(Variables.APP_LANGUAGE_CODE,Variables.DEFAULT_LANGUAGE_CODE), this, OpenPostActivity.class,false);
        binding = ActivityOpenPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.app_color));
        context = this;
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        frameViewModel = new ViewModelProvider(this).get(FrameViewModel.class);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        this.screenWidth = (float) displayMetrics.widthPixels;
        this.screenHeight = (float) (displayMetrics.heightPixels - ImageUtils.dpToPx(this, 105.0f));

        binding.titleTv.setText(getIntent().getStringExtra("title"));
        type = getIntent().getStringExtra("type");
        item_id = getIntent().getStringExtra("item_id");
        selectedPostsModel = (PostsModel) getIntent().getSerializableExtra("model");


        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.imageBtnIndicator.setActivated(true);
        binding.imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.clear();
                binding.imageBtnIndicator.setActivated(true);
                binding.videoBtnIndicator.setActivated(false);
                postType = "images";
                pageCount = 0;
                getPosts();
            }
        });
        binding.videoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.clear();
                binding.videoBtnIndicator.setActivated(true);
                binding.imageBtnIndicator.setActivated(false);
                postType = "video";
                pageCount = 0;
                getPosts();
            }
        });

        postsAdapter = new PostsSelectedAdapter(this, list, new PostsSelectedAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, PostsModel postsModels, int main_position) {
                selectedPostsModel = postsModels;
                setData();
            }
        },3,getResources().getDimension(R.dimen._2ssp));

        binding.recyclerView.setAdapter(postsAdapter);
        layoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int pastVisiblesItems, visibleItemCount, totalItemCount;
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    int[] firstVisibleItems = null;
                    firstVisibleItems = layoutManager.findFirstVisibleItemPositions(firstVisibleItems);
                    if(firstVisibleItems != null && firstVisibleItems.length > 0) {
                        pastVisiblesItems = firstVisibleItems[0];
                    }

                    if (!loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = true;
                            pageCount++;
                            getPosts();
                        }
                    }
                }
            }
        });

        if (type.equals("greeting")){
            postType = "greeting";
            type = "section";
            binding.imageBtn.setVisibility(View.GONE);
            binding.videoBtn.setVisibility(View.GONE);
        }

        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedPostsModel.type.equals("festival")){
//                    if (Functions.getSharedPreference(context).getInt(Variables.FESTIVAL,1) == 0){
                        secondCondition();
//                    }else{
//                        new UpgradeDialogFragment(getString(R.string.alert_festival)).show(getSupportFragmentManager(), "");
//                    }
                }else if (selectedPostsModel.type.equals("business") && Functions.getSharedPreference(context).getInt(Variables.BUSINESS,1) == 0){
//                    if (Functions.getSharedPreference(context).getInt(Variables.BUSINESS,1) == 0){
                        secondCondition();
//                    }else{
//                        new UpgradeDialogFragment(getString(R.string.alert_business)).show(getSupportFragmentManager(), "");
//                    }
                }else if (selectedPostsModel.type.equals("political") && Functions.getSharedPreference(context).getInt(Variables.POLITICAL,1) == 0){
//                    if (Functions.getSharedPreference(context).getInt(Variables.POLITICAL,1) == 0){
                        secondCondition();
//                    }else{
//                        new UpgradeDialogFragment(getString(R.string.alert_poliotical)).show(getSupportFragmentManager(), "");
//                    }
                }else{
                    secondCondition();
                }
            }

            private void secondCondition() {
                if (selectedPostsModel.getItem_url().endsWith(".mp4") && Functions.getSharedPreference(context).getInt(Variables.VIDEO,1) != 0){
                    new UpgradeDialogFragment(getString(R.string.alert_video)).show(getSupportFragmentManager(), "");
                    return;
                }
                if (Functions.getSharedPreference(OpenPostActivity.this).getString("posts_limit_status","false").equals("true")){
                    if (Functions.getSharedPreference(OpenPostActivity.this).getInt(Variables.POSTS_LIMIT,0) > 0){
                        if (Functions.IsPremiumEnable(context)){
                            if (Functions.getSharedPreference(OpenPostActivity.this).getInt(Variables.DAILY_LIMIT,777) == 777 ){
                                openEditorActivity();
                            }else {
                                if (Functions.getSharedPreference(OpenPostActivity.this).getInt(Variables.DAILY_LIMIT, 0) > 0) {
                                    openEditorActivity();
                                }else{
                                    new UpgradeDialogFragment(getString(R.string.daily_limit_reaced)).show(getSupportFragmentManager(), "");
                                }
                            }
                        }else{
                            openEditorActivity();
                        }
                    }else{
                        new UpgradeDialogFragment(getString(R.string.posts_limit_reaced)).show(getSupportFragmentManager(), "");
                    }
                }else{
                    if (Functions.IsPremiumEnable(context)){
                        if (Functions.getSharedPreference(OpenPostActivity.this).getString(Variables.DAILY_LIMIT,"0777").equals("0777") ){
                            openEditorActivity();
                        }else {
                            if (Functions.getSharedPreference(OpenPostActivity.this).getInt(Variables.DAILY_LIMIT, 0) > 0) {
                                openEditorActivity();
                            }else{
                                new UpgradeDialogFragment(getString(R.string.daily_limit_reaced)).show(getSupportFragmentManager(), "");
                            }
                        }
                    }else{
                        openEditorActivity();
                    }
                }
            }

            private void openEditorActivity() {
                EditorActivity.postsModel = selectedPostsModel;
                if (selectedPostsModel.getJson() == null || selectedPostsModel.getJson().equals("")){
                    Intent intent = new Intent(OpenPostActivity.this, EditorActivity.class);
                    intent.putExtra("backgroundImage", Functions.getItemBaseUrl(selectedPostsModel.item_url));
                    intent.putExtra("type", postType);
                    intent.putExtra("ratio", width+":"+height);
                    startActivity(intent);
                }else{
                    Functions.showLoader(OpenPostActivity.this);
                    StartTamplateProcess process = new StartTamplateProcess();
                    process.execute();
                }
            }
        });

        getPosts();
        initializePlayer();
        if (selectedPostsModel != null){
            setData();
        }
        binding.laguageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LanguageDialogFragment(new Callback() {
                    @Override
                    public void Responce(String resp) {
                        list.clear();
                        binding.imageBtn.setActivated(true);
                        binding.videoBtn.setActivated(false);
                        postType = "images";
                        pageCount = 0;
                        getPosts();
                    }
                }).show(getSupportFragmentManager(),"");
            }
        });

        binding.premiumLockLay.removeWatermarkTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RemoveWatermarkDialog(new RemoveWatermarkDialog.OnClickItem() {
                    @Override
                    public void watchVideAd() {
                        loadVideoAd();
                    }

                    @Override
                    public void buySinglePost() {
                        new PaymentTypeDialogFragment(false,new CallBack() {
                            @Override
                            public void getResponse(String requestType, String promocode, int discount) {
                                if (requestType.equals(Variables.PAYTM)) {
                                    Intent intent = new Intent(OpenPostActivity.this, PaytmActivity.class);
                                    intent.putExtra("price", Functions.getSharedPreference(OpenPostActivity.this).getString("single_post_subsciption_amount","10"));
                                    resultCallbackForPayment.launch(intent);
                                } else if (requestType.equals(Variables.RAZORPAY)){
                                    Intent intent = new Intent(OpenPostActivity.this, RazorpayActivity.class);
                                    intent.putExtra("price", Functions.getSharedPreference(OpenPostActivity.this).getString("single_post_subsciption_amount","10"));
                                    resultCallbackForPayment.launch(intent);
                                }else if (requestType.equals(Variables.STRIPE)){
                                    type = Variables.STRIPE;
                                    Intent intent = new Intent(OpenPostActivity.this, StripeActivity.class);
                                    intent.putExtra("price", Functions.getSharedPreference(OpenPostActivity.this).getString("single_post_subsciption_amount","10"));
                                    resultCallbackForPayment.launch(intent);
                                }else if (requestType.equals(Variables.INSTAMOJO)){
                                    type = Variables.INSTAMOJO;
                                    Intent intent = new Intent(OpenPostActivity.this, InstamojoActivity.class);
                                    intent.putExtra("price", Functions.getSharedPreference(OpenPostActivity.this).getString("single_post_subsciption_amount","10"));
                                    resultCallbackForPayment.launch(intent);
                                }else if (requestType.equals(Variables.CCAVENUE)){
                                    type = Variables.CCAVENUE;
                                    Intent intent = new Intent(OpenPostActivity.this, CCAvenueActivity.class);
                                    intent.putExtra("price", Functions.getSharedPreference(OpenPostActivity.this).getString("single_post_subsciption_amount","10"));
                                    resultCallbackForPayment.launch(intent);
                                }
                            }
                        }).show(getSupportFragmentManager(),"tttt");
                    }

                    @Override
                    public void goPremium() {
                        showPremiumFragment();
                    }
                }).show(getSupportFragmentManager(),"");
            }
        });

        binding.saveBtn.setVisibility(View.GONE);
        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedPostsModel.type.equals("festival")){
                    if (Functions.getSharedPreference(context).getInt(Variables.FESTIVAL,1) == 0){
                        secondCondition();
                    }else{
                        new UpgradeDialogFragment(getString(R.string.alert_festival)).show(getSupportFragmentManager(), "");
                    }
                }else if (selectedPostsModel.type.equals("business") && Functions.getSharedPreference(context).getInt(Variables.BUSINESS,1) == 0){
                    if (Functions.getSharedPreference(context).getInt(Variables.BUSINESS,1) == 0){
                        secondCondition();
                    }else{
                        new UpgradeDialogFragment(getString(R.string.alert_business)).show(getSupportFragmentManager(), "");
                    }
                }else if (selectedPostsModel.type.equals("political") && Functions.getSharedPreference(context).getInt(Variables.POLITICAL,1) == 0){
                    if (Functions.getSharedPreference(context).getInt(Variables.POLITICAL,1) == 0){
                        secondCondition();
                    }else{
                        new UpgradeDialogFragment(getString(R.string.alert_poliotical)).show(getSupportFragmentManager(), "");
                    }
                }else{
                    secondCondition();
                }


            }

            private void secondCondition() {
                if (selectedPostsModel.getItem_url().endsWith(".mp4") && Functions.getSharedPreference(context).getInt(Variables.VIDEO,1) != 0){
                    new UpgradeDialogFragment(getString(R.string.alert_video)).show(getSupportFragmentManager(), "");
                    return;
                }
                if (Functions.getSharedPreference(OpenPostActivity.this).getString("posts_limit_status","false").equals("true")){
                    if (Functions.getSharedPreference(OpenPostActivity.this).getInt(Variables.POSTS_LIMIT,0) > 0){
                        if (Functions.IsPremiumEnable(context)){
                            if (Functions.getSharedPreference(OpenPostActivity.this).getInt(Variables.DAILY_LIMIT,777) == 777 ){
                                savePost();
                            }else {
                                if (Functions.getSharedPreference(OpenPostActivity.this).getInt(Variables.DAILY_LIMIT, 0) > 0) {
                                    savePost();
                                }else{
                                    new UpgradeDialogFragment(getString(R.string.daily_limit_reaced)).show(getSupportFragmentManager(), "");
                                }
                            }
                        }else{
                            savePost();
                        }
                    }else{
                        new UpgradeDialogFragment(getString(R.string.posts_limit_reaced)).show(getSupportFragmentManager(), "");
                    }
                }else{
                    if (Functions.IsPremiumEnable(context)){
                        if (Functions.getSharedPreference(OpenPostActivity.this).getString(Variables.DAILY_LIMIT,"0777").equals("0777") ){
                            savePost();
                        }else {
                            if (Functions.getSharedPreference(OpenPostActivity.this).getInt(Variables.DAILY_LIMIT, 0) > 0) {
                                savePost();
                            }else{
                                new UpgradeDialogFragment(getString(R.string.daily_limit_reaced)).show(getSupportFragmentManager(), "");
                            }
                        }
                    }else{
                        savePost();
                    }
                }
            }

            private void savePost() {
                Functions.showLoader(context);
                Functions.dialog.findViewById(R.id.tv_lay).setVisibility(View.VISIBLE);
                loaderMessageTv = Functions.dialog.findViewById(R.id.message_tv);
                loaderMessageTv.setVisibility(View.VISIBLE);

                if (binding.playerview != null) {
                    binding.playerview.hideController();
                }

                findViewById(R.id.remove_watermark_tv).setVisibility(View.GONE);
                binding.premiumTag.setVisibility(View.GONE);

                if (selectedPostsModel != null){
                    if (selectedPostsModel.getPremium().equals("0")){
                        if (!Functions.IsPremiumEnable(context)){
                            binding.freeWatermarkLayout.setVisibility(View.VISIBLE);
                        }
                    }
                }

                Handler handler = new Handler();
                handler.postDelayed(new Runnable(){
                    @Override
                    public void run() {
                        bitmap = viewToBitmap(binding.containerLay);
                        if (selectedPostsModel.item_url.endsWith(".mp4")){
                            Functions.downloadVideoFile(context, Functions.getItemBaseUrl(selectedPostsModel.getItem_url()), new Callback() {
                                @Override
                                public void Responce(String videopath) {
                                    if (videopath != null) {

                                        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                                        retriever.setDataSource(videopath);
                                        int width = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
                                        int height = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
                                        try {
                                            retriever.release();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
                                        saveImage(bitmap,true,videopath);

                                    } else {
                                        Log.d("finalProcess__", "Downloading Error..");
                                        loaderMessageTv.setText("Downloading Error..");
                                        Functions.cancelLoader();
                                    }
                                }
                            });
                        }else{
                            saveImage(bitmap,false,"");
                        }

                        if (selectedPostsModel.premium.equals("1")){
                            binding.premiumTag.setVisibility(View.VISIBLE);
                        }
                        findViewById(R.id.remove_watermark_tv).setVisibility(View.VISIBLE);
                        binding.freeWatermarkLayout.setVisibility(View.GONE);
                    }
                },200);
            }
        });

        BannerAdapter.showBannerAds(this, findViewById(R.id.ad_container));
    }

    TextView loaderMessageTv;
    private void saveImage(Bitmap bitmap, boolean z, String videopath) {
        File file = new File(Functions.getAppFolder(context));
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Log.d("", "Can't create directory to save image.");
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.create_dir_err), Toast.LENGTH_LONG).show();
                return;
            }
        }
        String str2 = "Photo_" + System.currentTimeMillis();
        if (z) {
            str2 = str2 + ".png";
        } else {
            str2 = str2 + ".jpg";
        }
        String filename = file.getPath() + File.separator + str2;

        boolean success = false;
        if (!new File(filename).exists()) {
            try {
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(filename);
                    Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
                    Canvas canvas = new Canvas(createBitmap);
                    canvas.drawColor(-1);
                    canvas.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                    bitmap.recycle();
                    fileOutputStream.flush();
                    fileOutputStream.close();

                    MediaScannerConnection.scanFile(this, new String[]{filename},
                            (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                                public void onScanCompleted(String str, Uri uri) {
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("-> uri=");
                                    sb.append(uri);
                                    sb.append("-> FILE=");
                                    sb.append(filename);
                                    Uri muri = Uri.parse(filename);
                                }
                            });
                    success = true;
                } catch (Exception e) {
                    e.printStackTrace();
                    success = false;

                }

            } catch (Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            if (success) {
                startSavingAction(filename,videopath);
            } else {
                Functions.cancelLoader();
                Functions.showToast(this,getString(R.string.error));
            }

        }
    }

    private void startSavingAction(String filename,String videopath) {
        if (!videopath.equals("")) {
            applyFrameOnVideo(filename,videopath);
        } else {
            imageSavedSuccess(filename);
        }
    }

    private void imageSavedSuccess(String filename) {
        Intent intent = new Intent(context, PreviewActivity.class);
        intent.putExtra("url", filename);
        startActivity(intent);
        finish();
    }

    private void applyFrameOnVideo(String framePath, String videoPath) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String outputDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/apply_frame_" + System.currentTimeMillis() + ".mp4";
                loaderMessageTv.setText(R.string.applying_frame);

                Log.d("applyFrameOnVideo___",width + " " + height);

                FFmpeg.executeAsync(new String[]{"-i", videoPath, "-i", framePath,
                        "-filter_complex",
                        "overlay", "-r", "150",
                        "-vb", "20M",
                        "-y", outputDir}, new ExecuteCallback() {
                    @Override
                    public void apply(long executionId, int returnCode) {
                        if (returnCode == 1) {
                            FFmpeg.cancel(executionId);
                            Functions.cancelLoader();
                            Functions.showToast(OpenPostActivity.this, "Try Again!!");
                        }
                        if (returnCode == 0) {
                            FFmpeg.cancel(executionId);
                            Functions.cancelLoader();
                            imageSavedSuccess(outputDir);
                        } else if (returnCode == 255) {
                            Log.e("finalProcess__", "Command execution cancelled by user.");
                        } else {
                            String str = String.format("Command execution failed with rc=%d and the output below.",
                                    Arrays.copyOf(new Object[]{Integer.valueOf(returnCode)}, 1));
                            Log.i("finalProcess__", str);
                        }
                    }
                });
            }
        });
    }

    public static Bitmap viewToBitmap(View view) {
        Bitmap createBitmap = null;
        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(DRAWING_CACHE_QUALITY_HIGH);
        try {
            createBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
            view.draw(new Canvas(createBitmap));
            return createBitmap;
        } catch (Exception e) {
            return createBitmap;
        } finally {
            view.destroyDrawingCache();
        }
    }

    private final class StartTamplateProcess extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Functions.showLoader(OpenPostActivity.this);
        }

        @Override
        protected String doInBackground(Void... params) {
            TamplateUtils tamplateUtils = new TamplateUtils(OpenPostActivity.this);
            tamplateUtils.openEditorActivity(selectedPostsModel.getJson());
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            Functions.cancelLoader();
        }
    }

    boolean rewardGranted = false;
    RewardedAdapter rewardedAdapter;
    private void loadVideoAd() {
        Functions.showLoader(this);
        rewardedAdapter = new RewardedAdapter(this, new RewardedAdapter.Listener() {
            @Override
            public void onAdLoaded() {
                Functions.cancelLoader();
                RewardedAdapter.showAds();
            }

            @Override
            public void onAdReward() {
                rewardGranted = true;
            }

            @Override
            public void onAdFailedToLoad() {
                Functions.cancelLoader();
            }

            @Override
            public void onAdDismissed() {
                if (rewardGranted){
                    EditorActivity.postsModel = selectedPostsModel;
                    Intent intent = new Intent(OpenPostActivity.this, EditorActivity.class);
                    intent.putExtra("backgroundImage", Functions.getItemBaseUrl(selectedPostsModel.item_url));
                    intent.putExtra("type", postType);
                    intent.putExtra("isBayed",true);
                    intent.putExtra("ratio", width+":"+height);
                    startActivity(intent);
                }
            }
        });
        RewardedAdapter.LoadAds();
    }

    private void showPremiumFragment() {
        startActivity(new Intent(this,PremiumActivity.class));
    }

    ActivityResultLauncher<Intent> resultCallbackForPayment = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (Functions.isLogin(OpenPostActivity.this)){
                            EditorActivity.postsModel = selectedPostsModel;
                            Intent intent = new Intent(OpenPostActivity.this, EditorActivity.class);
                            intent.putExtra("backgroundImage", Functions.getItemBaseUrl(selectedPostsModel.item_url));
                            intent.putExtra("type", postType);
                            intent.putExtra("isBayed",true);
                            intent.putExtra("ratio", width+":"+height);
                            startActivity(intent);
                        }else {
                            startActivity(new Intent(OpenPostActivity.this, MainActivity.class));
                        }
                    }
                }
            });

    private void getPosts() {
        if (pageCount == 0){
            list.clear();
            findViewById(R.id.shimmer_lay).setVisibility(View.VISIBLE);
        }
        getPostsFromServer();
    }

    private void getPostsFromServer() {
        Log.d("getPostsFromServer","Type -> "+type);
        Log.d("getPostsFromServer","postType -> "+postType);
        Log.d("getPostsFromServer","item_id -> "+item_id);
        Log.d("getPostsFromServer","pageCount -> "+pageCount);
        Log.d("getPostsFromServer","pageCount -> ");

        String selectePostID = "";
        if(selectedPostsModel != null){
            selectePostID = selectedPostsModel.getId();
        }
        homeViewModel.getPostByPage(type,Functions.getSharedPreference(this).getString(Variables.SELCT_LANGUAGE,""),postType,item_id,subcategory,"",selectePostID,pageCount).observe(this, new Observer<HomeResponse>() {
            @Override
            public void onChanged(HomeResponse homeResponse) {
                loading = false;
                if (homeResponse != null && homeResponse.getSubcategories() != null){
                    setSubCategories(homeResponse.getSubcategories());
                }
                if (homeResponse != null && homeResponse.getPosts() != null){
                    list.addAll(homeResponse.posts);
                    findViewById(R.id.shimmer_lay).setVisibility(View.GONE);
                    if (list.size() > 0){
                        if (selectedPostsModel == null){
                            selectedPostsModel = list.get(0);
                            setData();
                        }
                        binding.noDataLayout.setVisibility(View.GONE);
                    }else{
                        if (firstTime && selectedPostsModel == null){
                            binding.nextBtn.setVisibility(View.GONE);
                            binding.containerLay.setVisibility(View.GONE);
                            binding.relatedContent.setVisibility(View.GONE);
                            binding.noAnyDataLayout.setVisibility(View.VISIBLE);
                        }
                        binding.noDataLayout.setVisibility(View.VISIBLE);
                    }
                    firstTime = false;
                    postsAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(context, "null", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    TabRecyclerAdapter recyclerAdapter;
    private void setSubCategories(List<CategoryModel> subcategories) {
        if (recyclerAdapter == null){
            List<String> subCT = new ArrayList<>();
            subCT.add("All");
            for (int i = 0; i < subcategories.size() ; i++){
                subCT.add(subcategories.get(i).getName());
            }
            recyclerAdapter = new TabRecyclerAdapter(OpenPostActivity.this,subCT, new AdapterClickListener() {
                @Override
                public void onItemClick(View view, int pos, Object object) {
                    if (pos == 0){
                        subcategory = "";
                    }else {
                        subcategory = subcategories.get(pos-1).getId();
                    }
                    pageCount = 0;
                    getPosts();
                }
            });
            binding.subcategoryRecycler.setVisibility(View.VISIBLE);
            binding.subcategoryRecycler.setAdapter(recyclerAdapter);
        }
    }

    private void initializePlayer() {
        try {
            binding.playerview.setUseController(true);
            binding.playerview.setControllerHideOnTouch(true);
            binding.playerview.setShowBuffering(SHOW_BUFFERING_WHEN_PLAYING);
            TrackSelector trackSelectorDef = new DefaultTrackSelector(this);
//            exoplayer = ExoPlayerFactory.newSimpleInstance(this, trackSelectorDef);
            exoplayer = new SimpleExoPlayer.Builder(this)
                    .setTrackSelector(trackSelectorDef)
                    .build();
            binding.playerview.setPlayer(exoplayer); // attach surface to the view

        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void setData() {
        if (exoplayer != null){
            exoplayer.setPlayWhenReady(false);
        }
        if (selectedPostsModel.premium.equals("1")){
            binding.premiumTag.setVisibility(View.VISIBLE);
            if (Functions.IsPremiumEnable(OpenPostActivity.this)){
                findViewById(R.id.premium_lock_lay).setVisibility(View.GONE);
            }else {
                findViewById(R.id.premium_lock_lay).setVisibility(View.VISIBLE);
            }
        }else {
            findViewById(R.id.premium_lock_lay).setVisibility(View.GONE);
            binding.premiumTag.setVisibility(View.GONE);
        }

        if (selectedPostsModel.item_url.contains(".mp4")){
            binding.playerview.setVisibility(View.VISIBLE);

            int appNameStringRes = R.string.app_name;
            String userAgent = com.google.android.exoplayer2.util.Util.getUserAgent(this, this.getString(appNameStringRes));
            DefaultDataSourceFactory defdataSourceFactory = new DefaultDataSourceFactory(this, userAgent);
            Uri uriOfContentUrl = Uri.parse(Functions.getItemBaseUrl(selectedPostsModel.item_url));
            MediaSource mediaSource = new ProgressiveMediaSource.Factory(defdataSourceFactory).createMediaSource(uriOfContentUrl);  // creating a media source

            exoplayer.setRepeatMode(Player.REPEAT_MODE_ALL);
            exoplayer.prepare(mediaSource);
            exoplayer.setPlayWhenReady(true); // start loading video and play it at the moment a chunk of it is available offline
            binding.playerview.setPlayer(exoplayer); // attach surface to the view
            binding.playerview.setShowBuffering(SHOW_BUFFERING_WHEN_PLAYING);
            exoplayer.addListener(new Player.EventListener() {

                @Override
                public void onLoadingChanged(boolean isLoading) {
                    Player.EventListener.super.onLoadingChanged(isLoading);
                }

            });

        }else {
            try {
                binding.posterIv.setVisibility(View.VISIBLE);
                binding.playerview.setVisibility(View.GONE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        BindingAdaptet.setImageUrl(binding.posterIv,Functions.getItemBaseUrl(selectedPostsModel.item_url));
        Glide.with(this)
                .asBitmap()      //get hieght and width
                .load(Functions.getItemBaseUrl(selectedPostsModel.item_url))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        height = resource.getHeight();
                        width = resource.getWidth();
                        ratio = width+":"+height;

                        Log.d("OpenPostActivityLog__",ratio);
                        bitmapRatio(ratio);
                    }
                });
    }

    public void bitmapRatio(String str) {
        Log.d("editorActivity___", "1 -> " + str);
        if (str != null) {
            String[] split = str.split(":");
            int gcd = gcd(Integer.parseInt(split[0]), Integer.parseInt(split[1]));

            Integer.parseInt(split[0]);
            Integer.parseInt(split[1]);

            String str4 = (Integer.parseInt(split[0]) / gcd) + ":" + (Integer.parseInt(split[1]) / gcd);
            Log.d("editorActivity___", "2 -> " + str4);

            if (Functions.getSharedPreference(context).getString(Variables.ACTIVE_PROFILE_TYPE,"").equals("")){
                getAllFrames("personal",str4);
            }else if (Functions.getSharedPreference(context).getString(Variables.ACTIVE_PROFILE_TYPE,"").equals("Business")){
                getAllFrames("business",str4);
            }else if (Functions.getSharedPreference(context).getString(Variables.ACTIVE_PROFILE_TYPE,"").equals("Political")){
                getAllFrames("political",str4);
            }

        } else {
            Toast.makeText(context, "Layout Ratio Error", Toast.LENGTH_SHORT).show();
        }

    }

    private int gcd(int i, int i2) {
        return i2 == 0 ? i : gcd(i2, i % i2);
    }

    private void getAllFrames(String type, String ratio) {
        if (newRatio.equals(ratio)){
            return;
        }
        newRatio = ratio ;
        frameViewModel.getFramesByType(type,ratio,true, Functions.getUID(this)).observe(this, new Observer<FrameResponse>() {
            @Override
            public void onChanged(FrameResponse frameResponse) {
                if (frameResponse != null) {
                    binding.pageIndicatorView.setVisibility(View.VISIBLE);
                    List<FrameModel> frameModelslist = new ArrayList<>();

                    if (frameResponse.userframes != null){
                        if (frameResponse.getUserframes().size() > 0) {
                            for (int i = 0; i < frameResponse.getUserframes().size(); i++) {
                                FrameModel frameModel = new FrameModel();
                                frameModel.setId(frameResponse.userframes.get(i).getId());
                                frameModel.setThumbnail(frameResponse.userframes.get(i).getItem_url());
                                frameModel.setCreated_at(frameResponse.userframes.get(i).getCreated_at());
                                frameModel.setJson(null);
                                frameModel.setPremium("0");
                                frameModelslist.add(frameModel);
                            }
                        }
                    }

                    if (frameResponse.frames != null){
                        frameModelslist.addAll(frameResponse.frames);
                    }
                    binding.framePager.setAdapter(new PagerFrameAdapter(getSupportFragmentManager(), frameModelslist,wr,hr));
                }
            }
        });
        binding.framePager.setOffscreenPageLimit(20);
        binding.pageIndicatorView.setViewPager(binding.framePager);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (exoplayer != null){
            exoplayer.setPlayWhenReady(false);
        }
    }
}