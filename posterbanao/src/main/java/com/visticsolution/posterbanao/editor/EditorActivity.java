package com.visticsolution.posterbanao.editor;

import static android.text.style.DynamicDrawableSpan.ALIGN_BOTTOM;
import static android.view.View.DRAWING_CACHE_QUALITY_HIGH;
import static android.widget.RelativeLayout.ALIGN_TOP;
import static android.widget.RelativeLayout.CENTER_IN_PARENT;
import static android.widget.RelativeLayout.TRUE;
import static com.google.android.exoplayer2.ui.PlayerView.SHOW_BUFFERING_WHEN_PLAYING;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.transition.Explode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;

import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;
import com.androidnetworking.AndroidNetworking;
import com.arthenica.mobileffmpeg.ExecuteCallback;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.daasuu.imagetovideo.EncodeListener;
import com.daasuu.imagetovideo.ImageToVideoConverter;
import com.google.android.exoplayer2.ExoPlayer;
//import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.material.tabs.TabLayout;
import com.hw.photomovie.render.GLTextureView;
import com.visticsolution.posterbanao.AddBussinessActivity;
import com.visticsolution.posterbanao.AddPoliticalActivity;
import com.visticsolution.posterbanao.PreviewActivity;
import com.visticsolution.posterbanao.R;
//TODO ZoomLayout
//import com.visticsolution.posterbanao.customview.ZoomLayout;
import com.visticsolution.posterbanao.dialog.CallBack;
import com.visticsolution.posterbanao.dialog.PaymentTypeDialogFragment;
import com.visticsolution.posterbanao.editor.adapter.BackgroundAdapter;
import com.visticsolution.posterbanao.editor.adapter.RecyclerFrameAdapter;
import com.visticsolution.posterbanao.editor.adapter.StickersAdapter;
import com.visticsolution.posterbanao.fragments.ContactFragment;
import com.visticsolution.posterbanao.interfaces.AdapterClickListener;
import com.visticsolution.posterbanao.model.BackgroundModel;
import com.visticsolution.posterbanao.payment.InstamojoActivity;
import com.visticsolution.posterbanao.payment.PaytmActivity;
import com.visticsolution.posterbanao.payment.RazorpayActivity;
import com.visticsolution.posterbanao.ads.BannerAdapter;
import com.visticsolution.posterbanao.ads.RewardedAdapter;
import com.visticsolution.posterbanao.classes.Callback;
import com.visticsolution.posterbanao.classes.Functions;
import com.visticsolution.posterbanao.classes.PermissionUtils;
import com.visticsolution.posterbanao.classes.Variables;
import com.visticsolution.posterbanao.dialog.CustomeDialogFragment;
import com.visticsolution.posterbanao.dialog.DialogType;
import com.visticsolution.posterbanao.dialog.PickFromFragment;
import com.visticsolution.posterbanao.dialog.PickedImageActionFragment;
import com.visticsolution.posterbanao.dialog.RemoveWatermarkDialog;
import com.visticsolution.posterbanao.editor.Fragment.ListFragment;
import com.visticsolution.posterbanao.editor.View.AutoFitEditText;
import com.visticsolution.posterbanao.editor.View.StickerView;
import com.visticsolution.posterbanao.editor.View.ViewIdGenerator;
import com.visticsolution.posterbanao.editor.View.text.AutofitTextRel;
import com.visticsolution.posterbanao.editor.View.text.TextInfo;
import com.visticsolution.posterbanao.editor.adapter.FontAdapter;
import com.visticsolution.posterbanao.editor.adapter.FramePagerAdapter;
import com.visticsolution.posterbanao.editor.adapter.MusicPagerAdapter;
import com.visticsolution.posterbanao.editor.adapter.RecyclerBorderAdapter;
import com.visticsolution.posterbanao.editor.adapter.RecyclerItemClickListener;
import com.visticsolution.posterbanao.editor.adapter.RecyclerOverLayAdapter;
import com.visticsolution.posterbanao.editor.adapter.RecyclerTextBgAdapter;
import com.visticsolution.posterbanao.editor.adapter.RecyclerVideoAnimationAdapter;
import com.visticsolution.posterbanao.editor.adapter.RecyclerVideoFilterAdapter;
import com.visticsolution.posterbanao.editor.adapter.StickerPagerAdapter;
import com.visticsolution.posterbanao.editor.colorpicker.LineColorPicker;
import com.visticsolution.posterbanao.editor.listener.GetColorListener;
import com.visticsolution.posterbanao.editor.listener.OnClickCallback;
import com.visticsolution.posterbanao.editor.listener.RepeatListener;
import com.visticsolution.posterbanao.editor.model.ElementInfo;
import com.visticsolution.posterbanao.editor.model.Sticker_info;
import com.visticsolution.posterbanao.editor.model.TemplateInfo;
import com.visticsolution.posterbanao.editor.model.textInfo;
import com.visticsolution.posterbanao.editor.movie.DemoPresenter;
import com.visticsolution.posterbanao.editor.movie.IDemoView;
import com.visticsolution.posterbanao.editor.moviewidget.FilterItem;
import com.visticsolution.posterbanao.editor.moviewidget.SaveVideoResponse;
import com.visticsolution.posterbanao.editor.moviewidget.TransferItem;
import com.visticsolution.posterbanao.editor.utility.ImageUtils;
import com.visticsolution.posterbanao.editor.utils.FrameUtils;
import com.visticsolution.posterbanao.editor.utils.MyUtils;
import com.visticsolution.posterbanao.editor.utils.StorageUtils;
import com.visticsolution.posterbanao.fragments.MyBussinessFragment;
import com.visticsolution.posterbanao.model.FrameCategoryModel;
import com.visticsolution.posterbanao.model.FrameModel;
import com.visticsolution.posterbanao.model.PostsModel;
import com.visticsolution.posterbanao.PremiumActivity;
import com.visticsolution.posterbanao.payment.StripeActivity;
import com.visticsolution.posterbanao.payment.CCAvenueActivity;
import com.visticsolution.posterbanao.responses.FrameResponse;
import com.visticsolution.posterbanao.viewmodel.FrameViewModel;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageGaussianBlurFilter;
import yuku.ambilwarna.AmbilWarnaDialog;

public class EditorActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, GetColorListener, StickerView.TouchEventListener, AutofitTextRel.TouchEventListener, RecyclerOverLayAdapter.OnOverlaySelected, RecyclerBorderAdapter.OnBorderSelected, IDemoView {

    public static final String TEMP_PATH = "Temp_Path";

    private static final int SELECT_PICTURE_FROM_GALLERY = 907;
    private static final int SELECT_PICTURE_FROM_GALLERY_FOR_STICKER_CHANGE = 1907;
    private static final int SELECT_PICTURE_FROM_GALLERY_BACKGROUND = 909;

    private static final String TAG = "EditorActivity";
    public static Bitmap btmSticker = null;
    public static ImageView btnLayControls = null;
    public static Bitmap imgBtmap = null;
    public static FrameLayout layContainer;
    public static int mRadius;
    public static RelativeLayout txtStkrRel;
    public static RelativeLayout movie_lay;
    public static Bitmap withoutWatermark;
    public static PostsModel postsModel;
    private final int bColor = Color.parseColor("#4149b6");
    private final List<WeakReference<Fragment>> mFragments = new ArrayList();
    public StickerView currentStickerView = null;
    public ImageView backgroundImg = null;
    PlayerView playerView;
    ExoPlayer exoplayer;
    String postPath;
    String musicPath = "";
    Activity context;
    Animation animSlideUp;
    Bitmap bit;
    Bitmap bitmap;
    ImageButton btnBck1;
    RelativeLayout centerRel;
    boolean checkTouchContinue = false;
    boolean editMode = false;
    String filename;
    View focusedView;
    float hr = 1.0f;
    LinearLayout layFontsSpacing;
    RelativeLayout layoutShadow1;
    RelativeLayout layoutShadow2;
    RelativeLayout mainRel;
    int outerColor = 0;
    int outerSize = 0;
    SeekBar seek;
    SeekBar borderSeek;
    SeekBar seekBar3;
    SeekBar seekbarShadow;
    float wr = 1.0f;
    int seekValue = 90;
    int shadowFlag = 0;
    boolean OneShow = true;
    FontAdapter adapter;
    RecyclerOverLayAdapter adaptorOverlay;
    RecyclerBorderAdapter adaptorBorder;
    RecyclerVideoFilterAdapter adaptorVideoFilter;
    RecyclerVideoAnimationAdapter adaptorVideoAnimation;
    RecyclerTextBgAdapter adaptorTxtBg;
    int alpha = 80;
    int bgAlpha = 0;
    int bgColor = ViewCompat.MEASURED_STATE_MASK;
    String bgDrawable = "0";
    LinearLayout bgShow;
    LinearLayout btnColorBackgroundPic;
    ImageView btnEditControlBg;
    ImageView btnEditControlColor;
    ImageView btnEditControlOutlineColor;
    ImageView btnEditControlShadowColor;
    LinearLayout btnImgBackground;
    ImageView btnRedo;
    RelativeLayout bckprass;
    RelativeLayout bckprassSticker;
    ImageView btnShadowBottom;
    ImageView btnShadowLeft;
    ImageView btnShadowRight;
    ImageView btnShadowTop;
    ImageView btnUndo;
    TextView btnErase;
    ImageButton btnUpDown;
    ImageButton btnUpDown1;
    boolean checkMemory;
    LinearLayout colorShow;
    String colorType;
    LinearLayout controlsShow;
    LinearLayout controlsShowStkr;
    boolean dialogShow = true;
    ArrayList<ElementInfo> elementInfos = new ArrayList<>();
    String fontName = "";
    LinearLayout fontsCurve;
    LinearLayout fontsShow;
    LinearLayout fontsSpacing;
    String backgroundPosterPath = "";
    String hex;
    TextView imgOK;
    //TODO ZoomLayout
    //    ZoomLayout greeting_zoom_lay;
    RelativeLayout layStkrMain;
    ImageView frameImage;
    RelativeLayout layTextMain;
    LinearLayout layBackground;
    RelativeLayout shapeRel;
    ImageView borderImg;
    RelativeLayout layColor;
    RelativeLayout layControlStkr;
    TextView layDupliStkr;
    TextView layDupliText;
    TextView layEdit;
    RelativeLayout layFilter;
    RelativeLayout layHue;
    ScrollView layScroll;
    LinearLayout laySticker;
    LinearLayout layMusic;
    int leftRightShadow = 0;
    ListFragment listFragment;
    FrameLayout mViewAllFrame;
    int isTamplate;
    BitmapFactory.Options options = new BitmapFactory.Options();
    LinearLayout outlineShow;
    String overlayName = "";
    int overlayBlur;
    int overlayOpacty;
    String[] pallete = {"#ffffff", "#cccccc", "#999999", "#666666", "#333333", "#000000", "#ffee90", "#ffd700", "#daa520", "#b8860b", "#ccff66", "#adff2f", "#00fa9a", "#00ff7f", "#00ff00", "#32cd32", "#3cb371", "#99cccc", "#66cccc", "#339999", "#669999", "#006666", "#336666", "#ffcccc", "#ff9999", "#ff6666", "#ff3333", "#ff0033", "#cc0033"};
    ProgressBar progressBarUndo;
    String ratio;
    String type;
    boolean isBuyed = false;
    RelativeLayout rellative;
    float rotation = 0.0f;
    LinearLayout sadowShow;
    float screenHeight;
    float screenWidth;
    int shadowColor = ViewCompat.MEASURED_STATE_MASK;
    int shadowProg = 0;
    GLTextureView glTextureView;
    DemoPresenter demoPresenter;
    int sizeFull = 0;
    ArrayList<Sticker_info> stickerInfoArrayList = new ArrayList<>();
    int stkrColorSet = Color.parseColor("#ffffff");
    int tAlpha = 100;
    int tColor = -1;
    int tempID = 2001;
    String tempPath = "";
    ArrayList<TemplateInfo> templateListRU = new ArrayList<>();
    ArrayList<TemplateInfo> templateListUR = new ArrayList<>();
    int textColorSet = Color.parseColor("#ffffff");
    ArrayList<textInfo> textInfoArrayList = new ArrayList<>();
    ArrayList<TextInfo> textInfosUR = new ArrayList<>();
    int topBottomShadow = 0;
    ImageView transImg;
    TextView txtBG;
    TextView txtEffect;
    TextView txtImage;
    HashMap<Integer, Object> txtShapeList;
    TextView txtSticker;
    TextView txtText;
    SeekBar verticalSeekBar = null;
    PermissionUtils takePermissionUtils;
    RecyclerView backgroudRecycler;
    boolean rewardGranted = false;
    RewardedAdapter rewardedAdapter;
    ActivityResultLauncher<Intent> resultCallbackForPayment = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        isBuyed = true;
                        findViewById(R.id.watermark).setVisibility(View.GONE);
                    }
                }
            });
    EditText stickerSearchEt;
    TabLayout stickerTabLayout;
    ViewPager stickerViewPager;
    RecyclerView stickerRecycler;
    FrameViewModel frameViewModel;
    TabLayout musicTabLayout;
    ViewPager musicViewPager;
    RecyclerView frameRecycler;
    TabLayout frameTabLayout;
    ViewPager frameViewPager;
    LottieAnimationView disk_lottie;
    ImageView play_pause_btn;
    SimpleExoPlayer musicPlayer;
    TextView loaderMessageTv;
    private boolean isChanageSticker;
    private SeekBar alphaSeekbar;
    private Animation animSlideDown;
    private String file;
    ActivityResultLauncher<Intent> resultCallbackForCamera = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        try {
                            Uri fromFile2 = Uri.fromFile(new File(getCacheDir(), "SampleCropImage" + System.currentTimeMillis() + ".png"));
                            UCrop.Options options3 = new UCrop.Options();
                            options3.setCompressionFormat(Bitmap.CompressFormat.PNG);
                            options3.setToolbarColor(getResources().getColor(R.color.backgroundColor));
                            options3.setFreeStyleCropEnabled(true);
                            UCrop.of(Uri.fromFile(new File(EditorActivity.this.file)), fromFile2).withOptions(options3).start(EditorActivity.this);
                        } catch (Exception e2) {
                            Toast.makeText(context, e2.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
    private View focusedCopy = null;
    private LineColorPicker horizontalPicker;
    private LineColorPicker horizontalPickerColor;
    private SeekBar hueSeekbar;
    private boolean isBackground;
    private boolean isGreeting;
    private boolean isMovie;
    private LinearLayout layEffects;
    private LinearLayout layBorder;
    private LinearLayout layFrame;
    private LinearLayout layVideoFilter;
    private LinearLayout layVideoAnimation;
    private RelativeLayout layRemove;
    private LinearLayout layTextedit;
    private float letterSpacing = 0.0f;
    private float lineSpacing = 0.0f;
    private LineColorPicker pickerBg;
    private LineColorPicker pickerOutline;
    private int processs;
    private SeekBar seekLetterSpacing;
    private SeekBar seekLineSpacing;
    private SeekBar seekOutlineSize;
    private SeekBar seekShadowBlur;
    private LinearLayout seekbarContainer;
    private LineColorPicker shadowPickerColor;
    private TextView ratio_tv;
    private TextView txtBgControl;
    private TextView txtColorsControl;
    private TextView txtFontsControl;
    private TextView txtShadowControl;
    private TextView txtTextControls;
    private TextView txtFontsSpacing;
    private TextView txtFontsStyle;
    private TextView txtFontsCurve;
    private TextView txtOutlineControl;
    private RelativeLayout userImage;
    private TextView sticker_gallery_change;
    private final ActivityResultLauncher<String[]> mPermissionResult = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onActivityResult(Map<String, Boolean> result) {
                }
            });

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

    private float getnewHeight(int i, int i2, float f, float f2) {
        return (((float) i2) * f) / ((float) i);
    }

    private float getnewWidth(int i, int i2, float f, float f2) {
        return (((float) i) * f2) / ((float) i2);
    }

    public float getXpos(float f) {
        return (((float) this.mainRel.getWidth()) * f) / 100.0f;
    }

    public float getYpos(float f) {
        return (((float) this.mainRel.getHeight()) * f) / 100.0f;
    }

    public int getNewWidht(float f, float f2) {
        return (int) ((((float) this.mainRel.getWidth()) * (f2 - f)) / 100.0f);
    }

    public int getNewHeight(float f, float f2) {
        return (int) ((((float) this.mainRel.getHeight()) * (f2 - f)) / 100.0f);
    }

    public int getNewHeightText(float f, float f2) {
        float height = (((float) this.mainRel.getHeight()) * (f2 - f)) / 100.0f;
        return (int) (((float) ((int) height)) + (height / 2.0f));
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setFlags(1024, 1024);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_editor);

        context = this;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        this.screenWidth = (float) displayMetrics.widthPixels;
        this.screenHeight = (float) (displayMetrics.heightPixels - ImageUtils.dpToPx(this, 105.0f));

        AndroidNetworking.initialize(getApplicationContext());
        takePermissionUtils = new PermissionUtils(this, mPermissionResult);

        Explode explode = new Explode();
        explode.setDuration(400);
        getWindow().setEnterTransition(explode);
        getWindow().setExitTransition(explode);

        findView();
        intilization();

        Functions.showLoader(context);
        startLoadingTimer();

        this.options.inScaled = false;
        this.isTamplate = getIntent().getIntExtra("isTamplate", 0);

        if (this.isTamplate != 0) {

            this.textInfoArrayList = getIntent().getParcelableArrayListExtra("text");
            this.stickerInfoArrayList = getIntent().getParcelableArrayListExtra("sticker");
            this.tempPath = getIntent().getStringExtra("backgroundImage");
            this.backgroundPosterPath = getIntent().getStringExtra("backgroundImage");
            this.ratio = getIntent().getStringExtra("ration");
            drawBackgroundImage(this.ratio);

        } else {

            this.ratio = getIntent().getStringExtra("ratio");
            this.type = getIntent().getStringExtra("type");
            this.isBuyed = getIntent().getBooleanExtra("isBayed", false);

            if (type != null && type.equals("greeting")) {
                findViewById(R.id.greeting_btn).setVisibility(View.VISIBLE);
                findViewById(R.id.greeting_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isGreeting = true;
                        layVideoAnimation.setVisibility(View.GONE);
                        layVideoFilter.setVisibility(View.GONE);
                        layFrame.setVisibility(View.GONE);
                        layBorder.setVisibility(View.GONE);
                        layEffects.setVisibility(View.GONE);
                        layStkrMain.setVisibility(View.GONE);
                        layBackground.setVisibility(View.GONE);
                        layTextMain.setVisibility(View.GONE);
                        laySticker.setVisibility(View.GONE);
                        showPicImageDialog();
                    }
                });
            } else if (type != null && type.equals("Movie")) {
                isMovie = true;
                backgroundImg.setVisibility(View.INVISIBLE);

                findViewById(R.id.video_animation).setVisibility(View.VISIBLE);
                findViewById(R.id.bv1).setVisibility(View.VISIBLE);
                findViewById(R.id.video_filter).setVisibility(View.VISIBLE);
                findViewById(R.id.bv2).setVisibility(View.VISIBLE);

                glTextureView = new GLTextureView(this);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                );
                params.addRule(CENTER_IN_PARENT, TRUE);
                params.addRule(ALIGN_TOP, backgroundImg.getId());
                params.addRule(ALIGN_BOTTOM, backgroundImg.getId());
                movie_lay.addView(glTextureView, params);
                demoPresenter = new DemoPresenter();
                demoPresenter.attachView(this);
                demoPresenter.onPhotoPick(Constants.movieImageList);
                initVideoAnimationRecycler();
                initVideoFilterRecycler();
            }
            this.postPath = getIntent().getStringExtra("backgroundImage");
            this.backgroundPosterPath = getIntent().getStringExtra("backgroundImage");
            this.hex = getIntent().getStringExtra("hex");

            drawBackgroundImage(this.ratio);
            if (postPath.endsWith(".mp4")) {
                initializePlayer();
                backgroundImg.setVisibility(View.INVISIBLE);
            } else {
                findViewById(R.id.playerview).setVisibility(View.GONE);
            }
        }

        if (!Functions.IsPremiumEnable(context) && postsModel != null && postsModel.premium.equals("1") && !isBuyed) {
            findViewById(R.id.watermark).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.watermark).setVisibility(View.GONE);
        }

        int[] iArr = new int[this.pallete.length];
        for (int i = 0; i < iArr.length; i++) {
            iArr[i] = Color.parseColor(this.pallete[i]);
        }
        this.horizontalPicker.setColors(iArr);
        this.horizontalPickerColor.setColors(iArr);
        this.shadowPickerColor.setColors(iArr);
        this.pickerOutline.setColors(iArr);
        this.pickerBg.setColors(iArr);
        this.horizontalPicker.setSelectedColor(this.textColorSet);
        this.horizontalPickerColor.setSelectedColor(this.stkrColorSet);
        this.shadowPickerColor.setSelectedColor(iArr[5]);
        this.pickerOutline.setSelectedColor(iArr[5]);
        this.pickerBg.setSelectedColor(iArr[5]);
        int color = this.horizontalPicker.getColor();
        int color2 = this.horizontalPickerColor.getColor();
        int color3 = this.shadowPickerColor.getColor();
        int color4 = this.pickerBg.getColor();
        int color5 = this.pickerOutline.getColor();
        updateColor(color);
        updateColor(color2);
        updateShadow(color3);
        updateOutline(color5);
        updateBgColor(color4);
        this.horizontalPickerColor.setOnColorChangedListener(EditorActivity.this::updateColor);
        this.horizontalPicker.setOnColorChangedListener(EditorActivity.this::updateColor);
        this.shadowPickerColor.setOnColorChangedListener(EditorActivity.this::updateShadow);
        this.pickerOutline.setOnColorChangedListener(EditorActivity.this::updateOutline);
        this.pickerBg.setOnColorChangedListener(EditorActivity.this::updateBgColor);
        this.mViewAllFrame = findViewById(R.id.viewall_layout);
        this.rellative = findViewById(R.id.rellative);
        this.layScroll = findViewById(R.id.lay_scroll);

        this.layScroll.setOnTouchListener((view, motionEvent) -> {
            onTouchApply();
            return true;
        });

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
        layoutParams.addRule(13);
//        this.layScroll.setLayoutParams(layoutParams);
//        this.layScroll.postInvalidate();
//        this.layScroll.requestLayout();


        findViewById(R.id.btnLeft).setOnTouchListener(new RepeatListener(200, 100, view -> updatePositionSticker("decX")));
        findViewById(R.id.btnUp).setOnTouchListener(new RepeatListener(200, 100, view -> updatePositionSticker("incrX")));
        findViewById(R.id.btnRight).setOnTouchListener(new RepeatListener(200, 100, view -> updatePositionSticker("decY")));
        findViewById(R.id.btnDown).setOnTouchListener(new RepeatListener(200, 100, view -> updatePositionSticker("incrY")));
        findViewById(R.id.btnLeftS).setOnTouchListener(new RepeatListener(200, 100, view -> updatePositionSticker("decX")));
        findViewById(R.id.btnRightS).setOnTouchListener(new RepeatListener(200, 100, view -> updatePositionSticker("incrX")));
        findViewById(R.id.btnUpS).setOnTouchListener(new RepeatListener(200, 100, view -> updatePositionSticker("decY")));
        findViewById(R.id.btnDownS).setOnTouchListener(new RepeatListener(200, 100, view -> updatePositionSticker("incrY")));

        findViewById(R.id.remove_watermark_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RemoveWatermarkDialog(new RemoveWatermarkDialog.OnClickItem() {
                    @Override
                    public void watchVideAd() {
                        loadVideoAd();
                    }

                    @Override
                    public void buySinglePost() {
                        new PaymentTypeDialogFragment(false, new CallBack() {
                            @Override
                            public void getResponse(String requestType, String promocode, int discount) {
                                if (requestType.equals(Variables.PAYTM)) {
                                    Intent intent = new Intent(EditorActivity.this, PaytmActivity.class);
                                    intent.putExtra("price", Functions.getSharedPreference(EditorActivity.this).getString("single_post_subsciption_amount", "10"));
                                    resultCallbackForPayment.launch(intent);
                                } else if (requestType.equals(Variables.RAZORPAY)) {
                                    Intent intent = new Intent(EditorActivity.this, RazorpayActivity.class);
                                    intent.putExtra("price", Functions.getSharedPreference(EditorActivity.this).getString("single_post_subsciption_amount", "10"));
                                    resultCallbackForPayment.launch(intent);
                                } else if (requestType.equals(Variables.INSTAMOJO)) {
                                    Intent intent = new Intent(EditorActivity.this, InstamojoActivity.class);
                                    intent.putExtra("price", Functions.getSharedPreference(EditorActivity.this).getString("single_post_subsciption_amount", "10"));
                                    resultCallbackForPayment.launch(intent);
                                } else if (requestType.equals(Variables.CCAVENUE)) {
                                    Intent intent = new Intent(EditorActivity.this, CCAvenueActivity.class);
                                    intent.putExtra("price", Functions.getSharedPreference(EditorActivity.this).getString("single_post_subsciption_amount", "10"));
                                    resultCallbackForPayment.launch(intent);
                                } else if (requestType.equals(Variables.STRIPE)) {
                                    Intent intent = new Intent(EditorActivity.this, StripeActivity.class);
                                    intent.putExtra("price", Functions.getSharedPreference(EditorActivity.this).getString("single_post_subsciption_amount", "10"));
                                    resultCallbackForPayment.launch(intent);
                                }
                            }
                        }).show(getSupportFragmentManager(), "tttt");
                    }

                    @Override
                    public void goPremium() {
                        showPremiumFragment();
                    }
                }).show(getSupportFragmentManager(), "");
            }
        });

    }

    private void startLoadingTimer() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Functions.loaderError(new Callback() {
                            @Override
                            public void Responce(String resp) {
                                drawBackgroundImage(ratio);
                            }
                        });
                    }
                });
            }
        }, 12000);
    }

    private void GETBackground() {
        backgroudRecycler = findViewById(R.id.backgroundRecycler);
        frameViewModel.getBackgrounds().observe(this, new Observer<FrameResponse>() {
            @Override
            public void onChanged(FrameResponse frameResponse) {
                if (frameResponse != null && frameResponse.getBackgrounds() != null) {
                    backgroudRecycler.setAdapter(new BackgroundAdapter(EditorActivity.this, frameResponse.backgrounds, new AdapterClickListener() {
                        @Override
                        public void onItemClick(View view, int pos, Object object) {
                            BackgroundModel model = (BackgroundModel) object;
                            Functions.showLoader(EditorActivity.this);
                            try {
                                Glide.with(EditorActivity.this)
                                        .asBitmap()
                                        .load(Functions.getItemBaseUrl(model.item_url))
                                        .into(new CustomTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                Functions.cancelLoader();
                                                layBackground.setVisibility(View.GONE);
                                                imgOK.setVisibility(View.VISIBLE);
                                                if (layBackground.getVisibility() == View.VISIBLE) {
                                                    layBackground.startAnimation(animSlideDown);
                                                    layBackground.setVisibility(View.GONE);
                                                }
                                                screenWidth = (float) backgroundImg.getWidth();
                                                screenHeight = (float) backgroundImg.getHeight();


                                                Constants.bitmap = ImageUtils.scaleCenterCrop(resource, (int) screenHeight, (int) screenWidth);
                                                hex = "";
                                                setImageBitmapAndResizeLayout(Constants.bitmap);
                                            }

                                            @Override
                                            public void onLoadCleared(@Nullable Drawable placeholder) {

                                            }
                                        });
                            } catch (Exception e3) {
                                e3.printStackTrace();
                            }
                        }
                    }));
                }
            }
        });
    }

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
                if (rewardGranted) {
                    isBuyed = true;
                    findViewById(R.id.watermark).setVisibility(View.GONE);
                }
            }
        });
        RewardedAdapter.LoadAds();
    }

    private void showPremiumFragment() {
        startActivity(new Intent(this, PremiumActivity.class));
    }

    @Override
    public GLTextureView getGLView() {
        return glTextureView;
    }

    @Override
    public Context getActivity() {
        return EditorActivity.this;
    }

    private void initializePlayer() {
        playerView = findViewById(R.id.playerview);
        playerView.setControllerHideOnTouch(true);
        playerView.setShowBuffering(SHOW_BUFFERING_WHEN_PLAYING);
        TrackSelector trackSelectorDef = new DefaultTrackSelector(this);
//        exoplayer = ExoPlayerFactory.newSimpleInstance(this, trackSelectorDef);
//        exoplayer = new ExoPlayer.Builder(this).build();
        exoplayer = new SimpleExoPlayer.Builder(this)
                .setTrackSelector(trackSelectorDef)
                .build();

        int appNameStringRes = R.string.app_name;
        String userAgent = com.google.android.exoplayer2.util.Util.getUserAgent(this, this.getString(appNameStringRes));
        DefaultDataSourceFactory defdataSourceFactory = new DefaultDataSourceFactory(this, userAgent);
        Uri uriOfContentUrl = Uri.parse(postPath);
        MediaSource mediaSource = new ProgressiveMediaSource.Factory(defdataSourceFactory).createMediaSource(MediaItem.fromUri(uriOfContentUrl));  // creating a media source

        exoplayer.prepare(mediaSource);
        exoplayer.setPlayWhenReady(true); // start loading video and play it at the moment a chunk of it is available offline
        playerView.setPlayer(exoplayer); // attach surface to the view
        playerView.setShowBuffering(SHOW_BUFFERING_WHEN_PLAYING);
//        exoplayer.addListener(new Player.EventListener() {
//
//            @Override
//            public void onIsLoadingChanged(boolean isLoading) {
//                Player.EventListener.super.onLoadingChanged(isLoading);
//            }
//            @Override
//            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
//                Player.EventListener.super.onPlayerStateChanged(playWhenReady, playbackState);
//            }
//        });
    }

    private void findView() {
        this.ratio_tv = findViewById(R.id.ratio_tv);
        this.progressBarUndo = findViewById(R.id.progress_undo);
        this.btnUndo = findViewById(R.id.btn_undo);
        this.btnRedo = findViewById(R.id.btn_redo);
        this.bckprass = findViewById(R.id.bckprass);
        this.bckprassSticker = findViewById(R.id.bckprass_sticker);
        this.btnUndo.setOnClickListener(this);
        this.btnRedo.setOnClickListener(this);
        this.btnBck1 = findViewById(R.id.btn_bck1);
        this.btnBck1.setOnClickListener(this);
        this.txtTextControls = findViewById(R.id.txt_text_controls);
        this.txtFontsControl = findViewById(R.id.txt_fonts_control);
        this.txtFontsStyle = findViewById(R.id.txt_fonts_Style);
        this.layFontsSpacing = findViewById(R.id.lay_fonts_Spacing);
        this.txtFontsSpacing = findViewById(R.id.txt_fonts_Spacing);
        this.txtFontsCurve = findViewById(R.id.txt_fonts_curve);
        this.txtColorsControl = findViewById(R.id.txt_colors_control);
        this.txtShadowControl = findViewById(R.id.txt_shadow_control);
        this.txtOutlineControl = findViewById(R.id.txt_outline_control);
        this.txtBgControl = findViewById(R.id.txt_bg_control);
        findViewById(R.id.bckprass_music).setOnClickListener(this);

        this.btnEditControlColor = findViewById(R.id.btnEditControlColor);
        this.btnEditControlShadowColor = findViewById(R.id.btnEditControlShadowColor);
        this.btnEditControlOutlineColor = findViewById(R.id.btnEditControlOutlineColor);
        this.btnEditControlBg = findViewById(R.id.btnEditControlBg);
        this.btnShadowLeft = findViewById(R.id.btnShadowLeft);
        this.btnShadowRight = findViewById(R.id.btnShadowRight);
        this.btnShadowTop = findViewById(R.id.btnShadowTop);
        this.btnShadowBottom = findViewById(R.id.btnShadowBottom);
        this.btnErase = findViewById(R.id.btn_erase);

        ImageView btnShadowTabChange = findViewById(R.id.btnShadowTabChange);
        this.layoutShadow1 = findViewById(R.id.layoutShadow1);
        this.layoutShadow2 = findViewById(R.id.layoutShadow2);
        this.txtText = findViewById(R.id.bt_text);
        this.txtSticker = findViewById(R.id.bt_sticker);
        this.txtImage = findViewById(R.id.bt_image);
        this.txtEffect = findViewById(R.id.bt_effect);
        this.txtBG = findViewById(R.id.bt_bg);
        this.btnErase.setOnClickListener(this);
        btnShadowTabChange.setOnClickListener(view -> {
            if (shadowFlag == 0) {
                shadowFlag = 1;
                layoutShadow2.setVisibility(View.VISIBLE);
                layoutShadow1.setVisibility(View.GONE);
            } else if (shadowFlag == 1) {
                shadowFlag = 0;
                layoutShadow1.setVisibility(View.VISIBLE);
                layoutShadow2.setVisibility(View.GONE);
            }
        });
    }

    public void drawBackgroundImage(final String str) {
        this.laySticker.setVisibility(View.GONE);
        RequestBuilder<Bitmap> asBitmap = Glide.with(getApplicationContext()).asBitmap();
        RequestOptions requestOptions = new RequestOptions().skipMemoryCache(true);
        float f = this.screenWidth;
        float f2 = this.screenHeight;
        if (f <= f2) {
            f = f2;
        }
        asBitmap.apply(requestOptions.override((int) f)).load(backgroundPosterPath).into(new SimpleTarget<Bitmap>() {
            public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                try {
                    bitmapRatio(str, bitmap);
                } catch (Exception e) {
                    finish();
                }
            }
        });
    }

    public void bitmapRatio(String str, Bitmap bitmap2) {
        Log.d("editorActivity___", "1 -> " + str);
        if (str != null) {
            String[] split = str.split(":");
            int gcd = gcd(Integer.parseInt(split[0]), Integer.parseInt(split[1]));

            Integer.parseInt(split[0]);
            Integer.parseInt(split[1]);

            String str4 = (Integer.parseInt(split[0]) / gcd) + ":" + (Integer.parseInt(split[1]) / gcd);
            Log.d("editorActivity___", "2 -> " + str4);
            if (!str4.equals("")) {
                if (str4.equals("1:1")) {
                    bitmap2 = cropInRatio(bitmap2, 1, 1);
                } else if (str4.equals("16:9")) {
                    bitmap2 = cropInRatio(bitmap2, 16, 9);
                } else if (str4.equals("9:16")) {
                    bitmap2 = cropInRatio(bitmap2, 9, 16);
                } else if (str4.equals("4:3")) {
                    bitmap2 = cropInRatio(bitmap2, 4, 3);
                } else if (str4.equals("3:4")) {
                    bitmap2 = cropInRatio(bitmap2, 3, 4);
                } else {
                    bitmap2 = cropInRatio(bitmap2, Integer.parseInt(split[0]), Integer.parseInt(split[1]));
                }
            }
            Bitmap resizeBitmap = Constants.resizeBitmap(bitmap2, (int) this.screenWidth, (int) this.screenHeight);
            setImageBitmapAndResizeLayout(resizeBitmap);
            ratio_tv.setText(str4);

//            FrameCategoryVertical(str4);

            if (Functions.getSharedPreference(context).getString(Variables.ACTIVE_PROFILE_TYPE, "").equals("")) {
                getAllFrames("personal", str4);
                findViewById(R.id.addBusinessAlertLay).setVisibility(View.VISIBLE);
                findViewById(R.id.addBusinessBtn).setOnClickListener(view -> {
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setContentView(R.layout.dialog_user_type);
                    dialog.setCancelable(true);
                    dialog.getWindow().setAttributes(getLayoutParams(dialog));
                    TextView businessBtn = dialog.findViewById(R.id.businessBtn);
                    TextView politicalBtn = dialog.findViewById(R.id.politicalBtn);
                    TextView skipBtn = dialog.findViewById(R.id.skip_btn);
                    skipBtn.setVisibility(View.GONE);
                    businessBtn.setOnClickListener(view2 -> {
                        dialog.dismiss();
                        addNew("business");
                    });
                    politicalBtn.setOnClickListener(view2 -> {
                        dialog.dismiss();
                        addNew("political");
                    });
                    dialog.show();
                });
            } else if (Functions.getSharedPreference(context).getString(Variables.ACTIVE_PROFILE_TYPE, "").equals("Business")) {
                getAllFrames("business", str4);
            } else if (Functions.getSharedPreference(context).getString(Variables.ACTIVE_PROFILE_TYPE, "").equals("Political")) {
                getAllFrames("political", str4);
            }

            if (str4.equals("1:1")) {
                BannerAdapter.showBannerAds(this, findViewById(R.id.ad_container));
            } else {
                findViewById(R.id.ad_container).setVisibility(View.GONE);
            }

            findViewById(R.id.get_frame).setOnClickListener(view -> {
                showContactFragment();
            });
        } else {
            Toast.makeText(context, "Layout Ratio Error", Toast.LENGTH_SHORT).show();
        }

    }

    public void addNew(String type) {
        if (type.equals("political")) {
            startActivity(new Intent(context, AddPoliticalActivity.class));
        } else {
            startActivity(new Intent(context, AddBussinessActivity.class));
        }
    }

    public Bitmap cropInRatio(Bitmap bitmap2, int i, int i2) {
        float width = (float) bitmap2.getWidth();
        float height = (float) bitmap2.getHeight();
        float f = getnewHeight(i, i2, width, height);
        float f2 = getnewWidth(i, i2, width, height);
        return (f2 == width && f == height) ? bitmap2 : (f > height || f >= height) ? (f2 > width || f2 >= width) ? null : Bitmap.createBitmap(bitmap2, (int) ((width - f2) / 2.0f), 0, (int) f2, (int) height) : Bitmap.createBitmap(bitmap2, 0, (int) ((height - f) / 2.0f), (int) width, (int) f);
    }

    private void setImageBitmapAndResizeLayout(Bitmap bitmap2) {
        this.mainRel.getLayoutParams().width = bitmap2.getWidth();
        this.mainRel.getLayoutParams().height = bitmap2.getHeight();
        this.mainRel.postInvalidate();
        this.mainRel.requestLayout();
        backgroundImg.setImageBitmap(bitmap2);
        imgBtmap = bitmap2;
        this.bit = bitmap2;
        this.mainRel.post(() -> {
            try {
                bit = ImageUtils.resizeBitmap(bit, centerRel.getWidth(), centerRel.getHeight());
                float height = (float) bit.getHeight();
                wr = ((float) bit.getWidth()) / ((float) bit.getWidth());
                hr = height / ((float) bit.getHeight());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        new BlurOperationTwoAsync(this, this.bit, this.backgroundImg, isTamplate).execute("");

    }

    private void intilization() {

        layContainer = findViewById(R.id.lay_container);
        this.centerRel = findViewById(R.id.center_rel);
        this.btnImgBackground = findViewById(R.id.btnImgBackground);
        this.btnColorBackgroundPic = findViewById(R.id.btnColorBackgroundPic);
        this.sticker_gallery_change = findViewById(R.id.sticker_gallery_change);
        this.sticker_gallery_change.setOnClickListener(this);
        this.shapeRel = findViewById(R.id.shape_rel);
        this.layRemove = findViewById(R.id.lay_remove);
        this.layTextMain = findViewById(R.id.lay_TextMain);
        this.layStkrMain = findViewById(R.id.lay_StkrMain);
        this.frameImage = findViewById(R.id.frameImage);
//        this.greeting_zoom_lay = findViewById(R.id.greeting_zoom_lay); //TODO ZoomLayout
        this.btnUpDown = findViewById(R.id.btn_up_down);
        this.btnUpDown1 = findViewById(R.id.btn_up_down1);
        this.mainRel = findViewById(R.id.main_rel);
        backgroundImg = findViewById(R.id.background_img);
        txtStkrRel = findViewById(R.id.txt_stkr_rel);
        movie_lay = findViewById(R.id.movie_lay);
        this.userImage = findViewById(R.id.select_artwork);
        findViewById(R.id.select_music).setOnClickListener(this);
        RelativeLayout select_backgnd = findViewById(R.id.select_backgnd);
        RelativeLayout select_effect = findViewById(R.id.select_effect);
        RelativeLayout select_border = findViewById(R.id.select_border);
        RelativeLayout video_filter = findViewById(R.id.video_filter);
        RelativeLayout select_frame = findViewById(R.id.select_frame);
        RelativeLayout video_animation = findViewById(R.id.video_animation);
        RelativeLayout add_sticker = findViewById(R.id.add_sticker);
        RelativeLayout add_text = findViewById(R.id.add_text);
        this.layEffects = findViewById(R.id.lay_effects);
        this.layBorder = findViewById(R.id.lay_borders);
        this.layFrame = findViewById(R.id.lay_frames);
        this.layVideoFilter = findViewById(R.id.lay_video_filters);
        this.layVideoAnimation = findViewById(R.id.lay_video_animations);
        this.laySticker = findViewById(R.id.lay_sticker);
        this.layMusic = findViewById(R.id.lay_music);
        this.layBackground = findViewById(R.id.lay_background);
        this.borderImg = findViewById(R.id.border_img);
        this.seekbarContainer = findViewById(R.id.seekbar_container);

        this.alphaSeekbar = findViewById(R.id.alpha_seekBar);
        this.seekBar3 = findViewById(R.id.seekBar3);
        this.seekbarShadow = findViewById(R.id.seekBar_shadow);
        SeekBar seekTextCurve = findViewById(R.id.seekTextCurve);
        this.hueSeekbar = findViewById(R.id.hue_seekBar);
        this.seekShadowBlur = findViewById(R.id.seekShadowBlur);
        this.seekOutlineSize = findViewById(R.id.seekOutlineSize);
        this.transImg = findViewById(R.id.trans_img);
        this.alphaSeekbar.setOnSeekBarChangeListener(this);
        this.seekBar3.setOnSeekBarChangeListener(this);
        this.seekbarShadow.setOnSeekBarChangeListener(this);
        this.hueSeekbar.setOnSeekBarChangeListener(this);
        this.seek = findViewById(R.id.seek);
        this.borderSeek = findViewById(R.id.seek_border);
        this.layFilter = findViewById(R.id.lay_filter);
        this.layDupliText = findViewById(R.id.lay_dupliText);
        this.layDupliStkr = findViewById(R.id.lay_dupliStkr);
        this.layEdit = findViewById(R.id.lay_edit);
        this.layDupliText.setOnClickListener(this);
        this.layDupliStkr.setOnClickListener(this);
        this.layEdit.setOnClickListener(this);

        this.imgOK = findViewById(R.id.btn_done);
        btnLayControls = findViewById(R.id.btn_layControls);
        this.layTextedit = findViewById(R.id.lay_textEdit);
        this.verticalSeekBar = findViewById(R.id.seekBar2);
        this.horizontalPicker = findViewById(R.id.picker);
        this.horizontalPickerColor = findViewById(R.id.picker1);
        this.shadowPickerColor = findViewById(R.id.pickerShadow);
        this.pickerOutline = findViewById(R.id.pickerOutline);
        this.pickerBg = findViewById(R.id.pickerBg);
        this.layColor = findViewById(R.id.lay_color);
        this.layHue = findViewById(R.id.lay_hue);

        this.seekLetterSpacing = findViewById(R.id.seekLetterSpacing);
        this.seekLineSpacing = findViewById(R.id.seekLineSpacing);
        this.hueSeekbar.setProgress(1);
        this.seek.setMax(255);
        this.borderSeek.setMax(30);
        this.seek.setProgress(80);
        this.seekbarShadow.setProgress(5);
        this.seekBar3.setProgress(255);
        this.transImg.setImageAlpha(this.alpha);
        this.seek.setOnSeekBarChangeListener(this);
        this.borderSeek.setOnSeekBarChangeListener(this);
        this.imgOK.setOnClickListener(this);
        btnLayControls.setOnClickListener(this);
        this.userImage.setOnClickListener(this);
        select_backgnd.setOnClickListener(this);
        select_effect.setOnClickListener(this);
        select_border.setOnClickListener(this);
        video_animation.setOnClickListener(this);
        video_filter.setOnClickListener(this);
        select_frame.setOnClickListener(this);
        add_sticker.setOnClickListener(this);
        add_text.setOnClickListener(this);
        this.layRemove.setOnClickListener(this);
        this.centerRel.setOnClickListener(this);
        this.animSlideUp = Constants.getAnimUp(this);
        this.animSlideDown = Constants.getAnimDown(this);
        this.verticalSeekBar.setOnSeekBarChangeListener(this);
        this.btnImgBackground.setOnClickListener(this);
        this.btnColorBackgroundPic.setOnClickListener(this);

        this.laySticker.setOnClickListener(this);
        this.layMusic.setOnClickListener(this);

        initOverlayRecycler();
        initBorderRecycler();
        initStickerCategory();
        MusicCategoryVertical();
        GETBackground();
        fackClick();
        this.seekLetterSpacing.setOnSeekBarChangeListener(this);
        this.seekLineSpacing.setOnSeekBarChangeListener(this);
        seekTextCurve.setOnSeekBarChangeListener(this);
        this.seekShadowBlur.setOnSeekBarChangeListener(this);
        this.seekOutlineSize.setOnSeekBarChangeListener(this);
        this.fontsShow = findViewById(R.id.fontsShow);
        this.fontsSpacing = findViewById(R.id.fontsSpacing);
        this.fontsCurve = findViewById(R.id.fontsCurve);
        this.colorShow = findViewById(R.id.colorShow);
        this.sadowShow = findViewById(R.id.sadowShow);
        this.outlineShow = findViewById(R.id.outlineShow);
        this.bgShow = findViewById(R.id.bgShow);
        this.controlsShow = findViewById(R.id.controlsShow);
        this.adapter = new FontAdapter(this, getResources().getStringArray(R.array.fonts_array));
        this.adapter.setSelected(0);
        ((GridView) findViewById(R.id.font_gridview)).setAdapter(this.adapter);


        this.adapter.setItemClickCallback((OnClickCallback<ArrayList<String>, Integer, String, Activity>) (arrayList, num, str, activity) -> {
            setTextFonts(str);
            adapter.setSelected(num.intValue());
        });
        this.adaptorTxtBg = new RecyclerTextBgAdapter(this, Constants.imageId);
        RecyclerView recyclerView = findViewById(R.id.txtBg_recylr);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(this.adaptorTxtBg);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, (view, i) -> {
            EditorActivity EditorActivity = EditorActivity.this;
            EditorActivity.setTextBgTexture("btxt" + i);
        }));
        this.layControlStkr = findViewById(R.id.lay_controlStkr);
        this.controlsShowStkr = findViewById(R.id.controlsShowStkr);
        this.layControlStkr.setOnClickListener(this);
        showLayoutFragment();

    }

    private void initStickerCategory() {
        stickerSearchEt = findViewById(R.id.stickerSearchEt);
        stickerTabLayout = findViewById(R.id.stickerTabLayout);
        stickerViewPager = findViewById(R.id.stickerViewpager);
        stickerRecycler = findViewById(R.id.stickerRecycler);
        frameViewModel = new ViewModelProvider(this).get(FrameViewModel.class);
        getStickers("");

        stickerSearchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (v.getText().toString().length() > 0) {
                        findViewById(R.id.no_sticker_data_layout).setVisibility(View.GONE);
                        stickerTabLayout.setVisibility(View.GONE);
                        stickerViewPager.setVisibility(View.GONE);
                        Functions.showLoader(context);
                        getStickers(v.getText().toString());
                    } else {
                        stickerTabLayout.setVisibility(View.VISIBLE);
                        stickerViewPager.setVisibility(View.VISIBLE);
                        stickerRecycler.setVisibility(View.GONE);
                        findViewById(R.id.no_sticker_data_layout).setVisibility(View.GONE);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void getStickers(String search) {
        frameViewModel.getStickers(search).observe(this, new Observer<FrameResponse>() {
            @Override
            public void onChanged(FrameResponse frameResponse) {
                Functions.cancelLoader();
                if (frameResponse != null) {
                    if (search != "") {
                        stickerRecycler.setAdapter(new StickersAdapter(context, frameResponse.stickers, new StickerPagerAdapter.OnStickerSelect() {
                            @Override
                            public void sticker(String path) {
                                loadSticker(path);
                            }
                        }));
                        if (frameResponse.stickers.size() > 0) {
                            findViewById(R.id.no_sticker_data_layout).setVisibility(View.GONE);
                        } else {
                            findViewById(R.id.no_sticker_data_layout).setVisibility(View.VISIBLE);
                        }
                        stickerRecycler.setVisibility(View.VISIBLE);
                    } else {
                        stickerViewPager.setAdapter(new StickerPagerAdapter(getSupportFragmentManager(), frameResponse.stickercategory, new StickerPagerAdapter.OnStickerSelect() {
                            @Override
                            public void sticker(String path) {
                                loadSticker(path);
                            }
                        }));
                        stickerTabLayout.setupWithViewPager(stickerViewPager);
                    }
                }

            }

            private void loadSticker(String path) {
                Functions.showLoader(EditorActivity.this);
                Glide.with(context)
                        .asBitmap()
                        .load(Functions.getItemBaseUrl(path))
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                Functions.cancelLoader();
                                colorType = "";
                                addSticker("", "", resource);
                                laySticker.startAnimation(animSlideDown);
                                laySticker.setVisibility(View.GONE);
                                imgOK.setVisibility(View.VISIBLE);
                                btnUndo.setVisibility(View.VISIBLE);
                                btnRedo.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                                Functions.cancelLoader();
                            }
                        });
            }
        });
    }

    private void MusicCategoryVertical() {
        musicTabLayout = findViewById(R.id.musicTabLayout);
        musicViewPager = findViewById(R.id.musicViewpager);
        frameViewModel.getMusic().observe(this, new Observer<FrameResponse>() {
            @Override
            public void onChanged(FrameResponse frameResponse) {
                if (frameResponse != null && frameResponse.musiccategories != null) {
                    musicViewPager.setAdapter(new MusicPagerAdapter(getSupportFragmentManager(), frameResponse.musiccategories, new MusicPagerAdapter.OnMusicSelect() {
                        @Override
                        public void onSelect(String path) {
                            if (exoplayer != null) {
                                exoplayer.getAudioComponent().setVolume(0f);
                            }
                            layMusic.startAnimation(animSlideDown);
                            layMusic.setVisibility(View.GONE);
                            musicPath = Functions.getItemBaseUrl(path);
                            playOnDisk();
                            playMusic(path);

                            imgOK.setVisibility(View.VISIBLE);
                            btnUndo.setVisibility(View.VISIBLE);
                            btnRedo.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onPlay(String path) {
                            playMusic(path);
                        }

                        @Override
                        public void onStop() {
                            stopMusic();
                        }
                    }));
                    musicTabLayout.setupWithViewPager(musicViewPager);
                }
            }
        });
    }

    private void getAllFrames(String type, String ratio) {
        if (frameRecycler != null) {
            return;
        }
        frameRecycler = findViewById(R.id.frameRecycler);
        frameViewModel.getFramesByType(type, ratio, false, Functions.getUID(context)).observe(this, new Observer<FrameResponse>() {
            @Override
            public void onChanged(FrameResponse frameResponse) {
                if (frameResponse != null) {

                    List<FrameModel> frameModelslist = new ArrayList<>();

                    if (frameResponse.userframes != null) {
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

                    if (frameResponse.frames != null) {
                        frameModelslist.addAll(frameResponse.frames);
                    }

                    if (frameModelslist.size() > 0) {
                        findViewById(R.id.frameLay).setVisibility(View.VISIBLE);
                        if (frameModelslist.get(0).getJson() == null) {
                            onPersonalFrameCLick(frameModelslist.get(0));
                        } else {
                            onFrameClick(frameModelslist.get(0));
                        }
                    } else {
                        findViewById(R.id.frameLay).setVisibility(View.GONE);
                    }

                    frameRecycler.setAdapter(new RecyclerFrameAdapter(context, frameModelslist, new FramePagerAdapter.OnFrameSelect() {
                        @Override
                        public void onSelect(FrameModel frameModel) {
                            if (frameModel.getJson() == null) {
                                onPersonalFrameCLick(frameModel);
                            } else {
                                onFrameClick(frameModel);
                            }
                        }

                        @Override
                        public void onPersonalSelect(String framepath) {

                        }
                    }));
                }
            }
        });
    }

    private void showContactFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);
        transaction.addToBackStack(null);
        transaction.replace(android.R.id.content, new ContactFragment()).commit();
    }

    private void FrameCategoryVertical(String ratio) {
        frameTabLayout = findViewById(R.id.frameTabLayout);
        frameViewPager = findViewById(R.id.framePager);
        frameViewModel.getFrames(ratio).observe(this, new Observer<FrameResponse>() {
            @Override
            public void onChanged(FrameResponse frameResponse) {
                if (frameResponse != null && frameResponse.framecategories != null) {
                    frameResponse.framecategories.add(0, new FrameCategoryModel("id", "personal"));
                    frameViewPager.setAdapter(new FramePagerAdapter(getSupportFragmentManager(), frameResponse.framecategories, new FramePagerAdapter.OnFrameSelect() {
                        @Override
                        public void onSelect(FrameModel frameModel) {
                            frameImage.setVisibility(View.GONE);
                            if (frameModel.getType().equals("political") && Functions.getSharedPreference(context).getString(Variables.POLITICAL_ID, "").equals("")) {
                                Toast.makeText(context, getString(R.string.please_add_political_profile), Toast.LENGTH_SHORT).show();
                                new MyBussinessFragment().show(getSupportFragmentManager(), "");
                                return;
                            } else if (frameModel.getType().equals("business") && Functions.getSharedPreference(context).getString(Variables.BUSSINESS_ID, "").equals("")) {
                                Toast.makeText(context, getString(R.string.please_select_bussiness), Toast.LENGTH_SHORT).show();
                                new MyBussinessFragment().show(getSupportFragmentManager(), "");
                                return;
                            }
                            onFrameClick(frameModel);
                        }

                        @Override
                        public void onPersonalSelect(String framepath) {
                            EditorActivity.txtStkrRel.removeAllViews();
                            frameImage.setVisibility(View.VISIBLE);
                            Glide.with(context)
                                    .asBitmap()
                                    .load(Functions.getItemBaseUrl(framepath))
                                    .into(new CustomTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                            BitmapDrawable background = new BitmapDrawable(resource);
                                            frameImage.setBackgroundDrawable(background);
                                        }

                                        @Override
                                        public void onLoadCleared(@Nullable Drawable placeholder) {

                                        }
                                    });
                        }
                    }));
                    frameTabLayout.setupWithViewPager(frameViewPager);

                    if (frameResponse.framecategories.size() > 1 && frameResponse.framecategories.get(1).getFrames().size() > 0) {
                        if (frameResponse.framecategories.get(1).getFrames().get(0).getType().equals("personal")) {
                            onFrameClick(frameResponse.framecategories.get(1).getFrames().get(0));
                        } else if (frameResponse.framecategories.get(1).getFrames().get(0).getType().equals("business") && !Functions.getSharedPreference(context).getString(Variables.BUSSINESS_ID, "").equals("")) {
                            onFrameClick(frameResponse.framecategories.get(1).getFrames().get(0));
                        }
                    }

                }
            }
        });
    }

    private void playOnDisk() {
        disk_lottie = findViewById(R.id.disk_lottie);
        play_pause_btn = findViewById(R.id.play_pause_btn);
        disk_lottie.playAnimation();
        findViewById(R.id.disk_lay).setVisibility(View.VISIBLE);
        findViewById(R.id.remove_music_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                musicPath = "";
                stopMusic();
                findViewById(R.id.disk_lay).setVisibility(View.GONE);
                if (exoplayer != null) {
                    exoplayer.getAudioComponent().setVolume(1f);
                }
            }
        });
        play_pause_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (musicPlayer != null && musicPlayer.isPlaying()) {
                    stopMusic();
                } else {
                    if (musicPlayer != null) {
                        musicPlayer.setPlayWhenReady(true);
                    } else {
                        playMusic(musicPath);
                    }

                }
            }
        });
    }

    private void playMusic(String path) {
        if (musicPlayer != null && musicPlayer.isPlaying()) {
            musicPlayer.setPlayWhenReady(false);
            musicPlayer.release();
        }
        TrackSelector trackSelectorDef = new DefaultTrackSelector();
//        musicPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelectorDef);
        musicPlayer = new SimpleExoPlayer.Builder(this)
                .setTrackSelector(trackSelectorDef)
                .build();
        int appNameStringRes = R.string.app_name;
        String userAgent = com.google.android.exoplayer2.util.Util.getUserAgent(this, this.getString(appNameStringRes));
        DefaultDataSourceFactory defdataSourceFactory = new DefaultDataSourceFactory(this, userAgent);
        Uri uriOfContentUrl = Uri.parse(Functions.getItemBaseUrl(path));
        MediaSource mediaSource = new ProgressiveMediaSource.Factory(defdataSourceFactory).createMediaSource(uriOfContentUrl);  // creating a media source
        musicPlayer.prepare(mediaSource);
        musicPlayer.setPlayWhenReady(true);
        musicPlayer.setRepeatMode(Player.REPEAT_MODE_ALL);
        musicPlayer.addListener(new Player.EventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playWhenReady) {
                    if (disk_lottie != null) {
                        play_pause_btn.setImageDrawable(getDrawable(com.google.android.exoplayer2.R.drawable.exo_controls_pause));
                        disk_lottie.playAnimation();
                    }
                } else {
                    if (disk_lottie != null) {
                        play_pause_btn.setImageDrawable(getDrawable(com.google.android.exoplayer2.R.drawable.exo_controls_play));
                        disk_lottie.pauseAnimation();
                    }
                }
            }
        });
    }

    private void stopMusic() {
        if (musicPlayer != null && musicPlayer.isPlaying()) {
            musicPlayer.setPlayWhenReady(false);
        }
    }

    private void initOverlayRecycler() {
        this.adaptorOverlay = new RecyclerOverLayAdapter(this, Constants.overlayArr, this);
        RecyclerView recyclerView = findViewById(R.id.overlay_recylr);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(this.adaptorOverlay);

    }

    private void initBorderRecycler() {
        this.adaptorBorder = new RecyclerBorderAdapter(this, Constants.borderArr, this);
        RecyclerView recyclerView = findViewById(R.id.border_recylr);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(this.adaptorBorder);
    }

    private void initVideoFilterRecycler() {
        this.adaptorVideoFilter = new RecyclerVideoFilterAdapter(this, new RecyclerVideoFilterAdapter.FilterCallback() {
            @Override
            public void onFilterSelect(FilterItem item) {
                demoPresenter.onFilterSelect(item);
            }
        });
        RecyclerView recyclerView = findViewById(R.id.videofilter_recylr);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(this.adaptorVideoFilter);
    }

    private void initVideoAnimationRecycler() {
        this.adaptorVideoAnimation = new RecyclerVideoAnimationAdapter(this, new RecyclerVideoAnimationAdapter.AnimatinCallback() {
            @Override
            public void onAnimationSelect(TransferItem item) {
                demoPresenter.onTransferSelect(item);
            }
        });
        RecyclerView recyclerView = findViewById(R.id.videoanim_recylr);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(this.adaptorVideoAnimation);
    }

    private void showLayoutFragment() {

        this.listFragment = new ListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.lay_container, this.listFragment, "fragment").commit();
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.lay_music) {
        } else if (id == R.id.bckprass_music) {
            this.layMusic.startAnimation(this.animSlideDown);
            layMusic.setVisibility(View.GONE);
            stopMusic();
            this.imgOK.setVisibility(View.VISIBLE);
            this.btnUndo.setVisibility(View.VISIBLE);
            this.btnRedo.setVisibility(View.VISIBLE);
        } else if (id == R.id.add_sticker) {
            removeImageViewControll();
            hideSlideBar();
            if (this.seekbarContainer.getVisibility() == View.VISIBLE) {
                this.seekbarContainer.startAnimation(this.animSlideDown);
                this.laySticker.startAnimation(this.animSlideDown);
                this.seekbarContainer.setVisibility(View.GONE);
                this.laySticker.setVisibility(View.GONE);
            }
            if (this.laySticker.getVisibility() != View.VISIBLE) {
                this.laySticker.setVisibility(View.VISIBLE);
                this.laySticker.startAnimation(this.animSlideUp);
                this.imgOK.setVisibility(View.GONE);
                this.btnUndo.setVisibility(View.GONE);
                this.btnRedo.setVisibility(View.GONE);
            } else {
                this.laySticker.startAnimation(this.animSlideDown);
                this.laySticker.setVisibility(View.GONE);
                this.imgOK.setVisibility(View.VISIBLE);
                this.btnUndo.setVisibility(View.VISIBLE);
                this.btnRedo.setVisibility(View.VISIBLE);
            }
            this.layVideoAnimation.setVisibility(View.GONE);
            this.layVideoFilter.setVisibility(View.GONE);
            this.layBorder.setVisibility(View.GONE);
            this.layFrame.setVisibility(View.GONE);
            this.layEffects.setVisibility(View.GONE);
            this.layStkrMain.setVisibility(View.GONE);
            this.layBackground.setVisibility(View.GONE);
            this.layTextMain.setVisibility(View.GONE);
        } else if (id == R.id.select_music) {
            removeImageViewControll();
            hideSlideBar();
            if (this.layMusic.getVisibility() != View.VISIBLE) {
                this.layMusic.setVisibility(View.VISIBLE);
                this.layMusic.startAnimation(this.animSlideUp);
                this.imgOK.setVisibility(View.GONE);
                this.btnUndo.setVisibility(View.GONE);
                this.btnRedo.setVisibility(View.GONE);
            } else {
                this.layMusic.startAnimation(this.animSlideDown);
                this.layMusic.setVisibility(View.GONE);
                this.imgOK.setVisibility(View.VISIBLE);
                this.btnUndo.setVisibility(View.VISIBLE);
                this.btnRedo.setVisibility(View.VISIBLE);
            }
            this.layFrame.setVisibility(View.GONE);
            this.layVideoAnimation.setVisibility(View.GONE);
            this.layVideoFilter.setVisibility(View.GONE);
            this.layBorder.setVisibility(View.GONE);
            this.layEffects.setVisibility(View.GONE);
            this.layStkrMain.setVisibility(View.GONE);
            this.layBackground.setVisibility(View.GONE);
            this.layTextMain.setVisibility(View.GONE);
        } else if (id == R.id.add_text) {
            removeImageViewControll();
            hideSlideBar();
            if (this.seekbarContainer.getVisibility() == View.VISIBLE) {
                this.seekbarContainer.startAnimation(this.animSlideDown);
                this.seekbarContainer.setVisibility(View.GONE);
            }
            this.layFrame.setVisibility(View.GONE);
            this.layVideoAnimation.setVisibility(View.GONE);
            this.layVideoFilter.setVisibility(View.GONE);
            this.layBorder.setVisibility(View.GONE);
            this.layEffects.setVisibility(View.GONE);
            this.layStkrMain.setVisibility(View.GONE);
            this.layBackground.setVisibility(View.GONE);
            this.layTextMain.setVisibility(View.GONE);
            this.laySticker.setVisibility(View.GONE);

            this.imgOK.setVisibility(View.VISIBLE);
            this.btnUndo.setVisibility(View.VISIBLE);
            this.btnRedo.setVisibility(View.VISIBLE);
            addTextDialog(null);
        } else if (id == R.id.btnAlignMentFont) {
            setLeftAlignMent();
        } else if (id == R.id.btnBoldFont) {
            setBoldFonts();
        } else if (id == R.id.btnCapitalFont) {
            setCapitalFont();
        } else if (id == R.id.btnCenterFont) {
            setCenterAlignMent();
        } else if (id == R.id.btnColorBackgroundPic) {
            colorPickerDialog(false);
        } else if (id == R.id.btnEditControlBg) {
            mainControlBgPickerDialog(false);
        } else if (id == R.id.btnEditControlColor) {
            mainControlcolorPickerDialog(false);
        } else if (id == R.id.btnEditControlOutlineColor) {
            mainControlOutlinePickerDialog(false);
        } else if (id == R.id.btnEditControlShadowColor) {
            mainControlShadowPickerDialog(false);
        } else if (id == R.id.btnImgBackground) {
            onGalleryBackground();
        } else if (id == R.id.sticker_gallery_change) {
            onGalleryButtonClick_forChangeSticker();
        } else if (id == R.id.btnItalicFont) {
            setItalicFont();
        } else if (id == R.id.btnRightFont) {
            setRightAlignMent();
        } else if (id == R.id.btnShadowBottom) {
            setBottomShadow();
        } else if (id == R.id.btnShadowLeft) {
            setLeftShadow();
        } else if (id == R.id.btnShadowRight) {
            setRightShadow();
        } else if (id == R.id.btnShadowTop) {
            setTopShadow();
        } else if (id == R.id.btnUnderlineFont) {
            setUnderLineFont();
        } else if (id == R.id.btn_bckprass) {
            onBackPressed();
        } else if (id == R.id.btn_done) {
            hideSlideBar();
            findViewById(R.id.remove_watermark_tv).setVisibility(View.GONE);
            this.layVideoAnimation.setVisibility(View.GONE);
            this.layVideoFilter.setVisibility(View.GONE);
            this.layBorder.setVisibility(View.GONE);
            this.layFrame.setVisibility(View.GONE);
            this.layEffects.setVisibility(View.GONE);
            this.layStkrMain.setVisibility(View.GONE);
            this.layBackground.setVisibility(View.GONE);
            this.layTextMain.setVisibility(View.GONE);
            this.laySticker.setVisibility(View.GONE);
            removeImageViewControll();
            if (this.seekbarContainer.getVisibility() == View.VISIBLE) {
                this.seekbarContainer.startAnimation(this.animSlideDown);
                this.seekbarContainer.setVisibility(View.GONE);
            }
            if (this.layTextMain.getVisibility() == View.VISIBLE) {
                this.layTextMain.startAnimation(this.animSlideDown);
                this.layTextMain.setVisibility(View.GONE);
            }
            if (this.layStkrMain.getVisibility() == View.VISIBLE) {
                this.layStkrMain.startAnimation(this.animSlideDown);
                this.layStkrMain.setVisibility(View.GONE);
            }
            exportBtnDone();

        } else if (id == R.id.btn_erase) {
            int childCount = txtStkrRel.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = txtStkrRel.getChildAt(i);
                if (childAt instanceof StickerView) {
                    StickerView stickerView = (StickerView) childAt;
                    if (stickerView.getBorderVisbilty()) {
                        if (!stickerView.getComponentInfo().getSTKR_PATH().equals("")) {
                            Constants.uri = stickerView.getComponentInfo().getSTKR_PATH();

                            try {
                                new PickedImageActionFragment(childAt.getId(), stickerView.getComponentInfo().getSTKR_PATH(), new PickedImageActionFragment.OnBitmapSelect() {
                                    @Override
                                    public void output(int id, String out) {
                                        int childCount = txtStkrRel.getChildCount();
                                        int i5 = id;
                                        for (int i6 = 0; i6 < childCount; i6++) {
                                            View childAt = txtStkrRel.getChildAt(i6);
                                            if ((childAt instanceof StickerView) && childAt.getId() == i5) {
                                                ((StickerView) childAt).setStrPath(out);
                                                saveBitmapUndu();
                                            }
                                        }
                                    }
                                }).show(getSupportFragmentManager(), "");

                                return;
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                                return;
                            }
                        } else if (!stickerView.getComponentInfo().getRES_ID().equals("")) {
                            Constants.rewid = stickerView.getComponentInfo().getRES_ID();

                            try {
                                new PickedImageActionFragment(childAt.getId(), stickerView.getComponentInfo().getRES_ID(), new PickedImageActionFragment.OnBitmapSelect() {
                                    @Override
                                    public void output(int id, String out) {
                                        int childCount = txtStkrRel.getChildCount();
                                        int i5 = id;
                                        for (int i6 = 0; i6 < childCount; i6++) {
                                            View childAt = txtStkrRel.getChildAt(i6);
                                            if ((childAt instanceof StickerView) && childAt.getId() == i5) {
                                                ((StickerView) childAt).setStrPath(out);
                                                saveBitmapUndu();
                                            }
                                        }
                                    }
                                }).show(getSupportFragmentManager(), "");

                                return;
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                                return;
                            }
                        } else if (stickerView.getMainImageBitmap() != null) {
                            Constants.bitmapSticker = stickerView.getMainImageBitmap();

                            try {
                                new PickedImageActionFragment(childAt.getId(), stickerView.getMainImageBitmap(), new PickedImageActionFragment.OnBitmapSelect() {
                                    @Override
                                    public void output(int id, String out) {
                                        int childCount = txtStkrRel.getChildCount();
                                        int i5 = id;
                                        for (int i6 = 0; i6 < childCount; i6++) {
                                            View childAt = txtStkrRel.getChildAt(i6);
                                            if ((childAt instanceof StickerView) && childAt.getId() == i5) {
                                                ((StickerView) childAt).setStrPath(out);
                                                saveBitmapUndu();
                                            }
                                        }
                                    }
                                }).show(getSupportFragmentManager(), "");

                                return;
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                                return;
                            }
                        }

                    }
                }
            }
            Toast.makeText(this, "Select sticker to perform erase operation..", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.btn_layControls) {
            removeImageViewControll();
            if (this.layTextMain.getVisibility() == View.VISIBLE) {
                this.layTextMain.startAnimation(this.animSlideDown);
                this.layTextMain.setVisibility(View.GONE);
            }
            if (this.layStkrMain.getVisibility() == View.VISIBLE) {
                this.layStkrMain.startAnimation(this.animSlideDown);
                this.layStkrMain.setVisibility(View.GONE);
            }
            if (layContainer.getVisibility() == View.GONE) {
                this.listFragment.getLayoutChild();
                layContainer.setVisibility(View.VISIBLE);
                layContainer.animate().translationX((float) layContainer.getLeft()).setDuration(200).setInterpolator(new DecelerateInterpolator()).start();
                return;
            }
            layContainer.setVisibility(View.VISIBLE);
            layContainer.animate().translationX((float) (-layContainer.getRight())).setDuration(200).setInterpolator(new AccelerateInterpolator()).start();
            new Handler().postDelayed(() -> {
                EditorActivity.layContainer.setVisibility(View.GONE);
            }, 200);

        } else if (id == R.id.btn_redo) {
            redo();
        } else if (id == R.id.btn_undo) {
            undo();
        } else if (id == R.id.btn_up_down) {
            this.focusedCopy = this.focusedView;
            this.layStkrMain.requestLayout();
            this.layStkrMain.postInvalidate();
            if (this.seekbarContainer.getVisibility() == View.VISIBLE) {
                hideResContainer();
            } else {
                showResContainer();
            }
        } else if (id == R.id.btn_up_down1) {
            this.focusedCopy = this.focusedView;
            this.layTextMain.requestLayout();
            this.layTextMain.postInvalidate();
            if (this.layTextedit.getVisibility() == View.VISIBLE) {
                hideTextResContainer();
            } else {
                showTextResContainer();
            }
        } else if (id == R.id.center_rel || id == R.id.lay_remove) {
            this.layVideoAnimation.setVisibility(View.GONE);
            this.layVideoFilter.setVisibility(View.GONE);
            this.layBorder.setVisibility(View.GONE);
            this.layFrame.setVisibility(View.GONE);
            this.layEffects.setVisibility(View.GONE);
            this.layStkrMain.setVisibility(View.GONE);
            this.laySticker.setVisibility(View.GONE);
            this.layBackground.setVisibility(View.GONE);
            onTouchApply();
        } else if (id == R.id.lay_backgnd_control) {
            this.fontsShow.setVisibility(View.GONE);
            this.fontsSpacing.setVisibility(View.GONE);
            this.fontsCurve.setVisibility(View.GONE);
            this.colorShow.setVisibility(View.GONE);
            this.sadowShow.setVisibility(View.GONE);
            this.outlineShow.setVisibility(View.GONE);
            this.bgShow.setVisibility(View.VISIBLE);
            this.controlsShow.setVisibility(View.GONE);
            selectControl8();
        } else if (id == R.id.lay_colors_control) {
            this.fontsShow.setVisibility(View.GONE);
            this.fontsSpacing.setVisibility(View.GONE);
            this.fontsCurve.setVisibility(View.GONE);
            this.colorShow.setVisibility(View.VISIBLE);
            this.sadowShow.setVisibility(View.GONE);
            this.outlineShow.setVisibility(View.GONE);
            this.bgShow.setVisibility(View.GONE);
            this.controlsShow.setVisibility(View.GONE);
            selectControl6();
        } else if (id == R.id.lay_controls_control) {
            this.fontsShow.setVisibility(View.GONE);
            this.fontsSpacing.setVisibility(View.GONE);
            this.fontsCurve.setVisibility(View.GONE);
            this.colorShow.setVisibility(View.GONE);
            this.sadowShow.setVisibility(View.GONE);
            this.outlineShow.setVisibility(View.GONE);
            this.bgShow.setVisibility(View.GONE);
            this.controlsShow.setVisibility(View.VISIBLE);
            selectControl1();
        } else if (id == R.id.lay_dupliStkr) {
            int childCount2 = txtStkrRel.getChildCount();
            for (int i2 = 0; i2 < childCount2; i2++) {
                View childAt2 = txtStkrRel.getChildAt(i2);
                if (childAt2 instanceof StickerView) {
                    StickerView stickerView2 = (StickerView) childAt2;
                    if (stickerView2.getBorderVisbilty()) {
                        StickerView stickerView3 = new StickerView(this);
                        stickerView3.setComponentInfo(stickerView2.getComponentInfo());
                        stickerView3.setId(ViewIdGenerator.generateViewId());
                        stickerView3.setViewWH((float) this.mainRel.getWidth(), (float) this.mainRel.getHeight());
                        txtStkrRel.addView(stickerView3);
                        removeImageViewControll();
                        stickerView3.setOnTouchCallbackListener(this);
                        stickerView3.setBorderVisibility(true);
                    }
                }
            }
        } else if (id == R.id.lay_dupliText) {
            int childCount3 = txtStkrRel.getChildCount();
            for (int i3 = 0; i3 < childCount3; i3++) {
                View childAt3 = txtStkrRel.getChildAt(i3);
                if (childAt3 instanceof AutofitTextRel) {
                    AutofitTextRel autofitTextRel = (AutofitTextRel) childAt3;
                    if (autofitTextRel.getBorderVisibility()) {
                        AutofitTextRel autofitTextRel2 = new AutofitTextRel(this);
                        txtStkrRel.addView(autofitTextRel2);
                        removeImageViewControll();
                        autofitTextRel2.setTextInfo(autofitTextRel.getTextInfo(), false);
                        autofitTextRel2.setId(ViewIdGenerator.generateViewId());
                        autofitTextRel2.setOnTouchCallbackListener(this);
                        autofitTextRel2.setBorderVisibility(true);
                    }
                }
            }
        } else if (id == R.id.lay_edit) {
            doubleTabPrass();
        } else if (id == R.id.lay_fonts_Curve) {
            this.fontsSpacing.setVisibility(View.GONE);
            this.fontsCurve.setVisibility(View.VISIBLE);
            this.fontsShow.setVisibility(View.GONE);
            this.colorShow.setVisibility(View.GONE);
            this.sadowShow.setVisibility(View.GONE);
            this.outlineShow.setVisibility(View.GONE);
            this.bgShow.setVisibility(View.GONE);
            this.controlsShow.setVisibility(View.GONE);
            selectControl5();
        } else if (id == R.id.lay_fonts_Spacing) {
            this.fontsSpacing.setVisibility(View.VISIBLE);
            this.fontsCurve.setVisibility(View.GONE);
            this.fontsShow.setVisibility(View.GONE);
            this.colorShow.setVisibility(View.GONE);
            this.sadowShow.setVisibility(View.GONE);
            this.outlineShow.setVisibility(View.GONE);
            this.bgShow.setVisibility(View.GONE);
            this.controlsShow.setVisibility(View.GONE);
            selectControl4();
        } else if (id == R.id.lay_fonts_control) {
            this.fontsShow.setVisibility(View.VISIBLE);
            this.fontsSpacing.setVisibility(View.GONE);
            this.fontsCurve.setVisibility(View.GONE);
            this.colorShow.setVisibility(View.GONE);
            this.sadowShow.setVisibility(View.GONE);
            this.outlineShow.setVisibility(View.GONE);
            this.bgShow.setVisibility(View.GONE);
            this.controlsShow.setVisibility(View.GONE);
            selectControl2();
        } else if (id == R.id.lay_fonts_style) {
            this.fontsSpacing.setVisibility(View.GONE);
            this.fontsCurve.setVisibility(View.GONE);
            this.fontsShow.setVisibility(View.GONE);
            this.colorShow.setVisibility(View.GONE);
            this.sadowShow.setVisibility(View.GONE);
            this.outlineShow.setVisibility(View.GONE);
            this.bgShow.setVisibility(View.GONE);
            this.controlsShow.setVisibility(View.GONE);
            selectControl3();
        } else if (id == R.id.lay_outline_control) {
            this.fontsShow.setVisibility(View.GONE);
            this.fontsSpacing.setVisibility(View.GONE);
            this.fontsCurve.setVisibility(View.GONE);
            this.colorShow.setVisibility(View.GONE);
            this.sadowShow.setVisibility(View.GONE);
            this.outlineShow.setVisibility(View.VISIBLE);
            this.bgShow.setVisibility(View.GONE);
            this.controlsShow.setVisibility(View.GONE);
            selectControl9();
        } else if (id == R.id.lay_shadow_control) {
            this.fontsShow.setVisibility(View.GONE);
            this.fontsSpacing.setVisibility(View.GONE);
            this.fontsCurve.setVisibility(View.GONE);
            this.colorShow.setVisibility(View.GONE);
            this.sadowShow.setVisibility(View.VISIBLE);
            this.outlineShow.setVisibility(View.GONE);
            this.bgShow.setVisibility(View.GONE);
            this.controlsShow.setVisibility(View.GONE);
            selectControl7();
        } else if (id == R.id.select_artwork) {
            removeImageViewControll();
            hideSlideBar();
            this.layVideoAnimation.setVisibility(View.GONE);
            this.layVideoFilter.setVisibility(View.GONE);
            this.layBorder.setVisibility(View.GONE);
            this.layEffects.setVisibility(View.GONE);
            this.layStkrMain.setVisibility(View.GONE);
            this.layBackground.setVisibility(View.GONE);
            this.layTextMain.setVisibility(View.GONE);
            this.laySticker.setVisibility(View.GONE);
            showPicImageDialog();

        } else if (id == R.id.select_backgnd) {
            hideSlideBar();
            if (this.layBackground.getVisibility() != View.VISIBLE) {
                this.bckprass.setOnClickListener(v -> layBackground.setVisibility(View.GONE));
                this.layBackground.setVisibility(View.VISIBLE);
                this.layBackground.startAnimation(this.animSlideUp);
            } else {
                this.layBackground.startAnimation(this.animSlideDown);
                this.imgOK.setVisibility(View.VISIBLE);
                this.btnUndo.setVisibility(View.VISIBLE);
                this.btnRedo.setVisibility(View.VISIBLE);
                this.layBackground.setVisibility(View.GONE);
            }
            this.layVideoAnimation.setVisibility(View.GONE);
            this.layVideoFilter.setVisibility(View.GONE);
            this.layFrame.setVisibility(View.GONE);
            this.layBorder.setVisibility(View.GONE);
            this.layEffects.setVisibility(View.GONE);
            this.layStkrMain.setVisibility(View.GONE);
            this.layTextMain.setVisibility(View.GONE);
            this.laySticker.setVisibility(View.GONE);

            this.imgOK.setVisibility(View.VISIBLE);
            this.btnUndo.setVisibility(View.VISIBLE);
            this.btnRedo.setVisibility(View.VISIBLE);
        } else if (id == R.id.select_effect) {
            removeImageViewControll();
            hideSlideBar();
            if (this.layEffects.getVisibility() != View.VISIBLE) {
                this.layEffects.setVisibility(View.VISIBLE);
                this.layEffects.startAnimation(this.animSlideUp);
            } else {
                this.layEffects.setVisibility(View.GONE);
                this.layEffects.startAnimation(this.animSlideDown);
            }
            this.layVideoAnimation.setVisibility(View.GONE);
            this.layVideoFilter.setVisibility(View.GONE);
            this.layFrame.setVisibility(View.GONE);
            this.layBorder.setVisibility(View.GONE);
            this.layStkrMain.setVisibility(View.GONE);
            this.layBackground.setVisibility(View.GONE);
            this.layTextMain.setVisibility(View.GONE);
            this.laySticker.setVisibility(View.GONE);

            this.imgOK.setVisibility(View.VISIBLE);
            this.btnUndo.setVisibility(View.VISIBLE);
            this.btnRedo.setVisibility(View.VISIBLE);
        } else if (id == R.id.select_frame) {
            removeImageViewControll();
            hideSlideBar();
            if (this.layFrame.getVisibility() != View.VISIBLE) {
                this.layFrame.setVisibility(View.VISIBLE);
                this.layFrame.startAnimation(this.animSlideUp);
            } else {
                this.layFrame.setVisibility(View.GONE);
                this.layFrame.startAnimation(this.animSlideDown);
            }
            this.layVideoAnimation.setVisibility(View.GONE);
            this.layVideoFilter.setVisibility(View.GONE);
            this.layEffects.setVisibility(View.GONE);
            this.layBorder.setVisibility(View.GONE);
            this.layStkrMain.setVisibility(View.GONE);
            this.layBackground.setVisibility(View.GONE);
            this.layTextMain.setVisibility(View.GONE);
            this.laySticker.setVisibility(View.GONE);

            this.imgOK.setVisibility(View.VISIBLE);
            this.btnUndo.setVisibility(View.VISIBLE);
            this.btnRedo.setVisibility(View.VISIBLE);
        } else if (id == R.id.select_border) {
            removeImageViewControll();
            hideSlideBar();
            if (this.layBorder.getVisibility() != View.VISIBLE) {
                this.layBorder.setVisibility(View.VISIBLE);
                this.layBorder.startAnimation(this.animSlideUp);
            } else {
                this.layBorder.setVisibility(View.GONE);
                this.layBorder.startAnimation(this.animSlideDown);
            }
            this.layVideoAnimation.setVisibility(View.GONE);
            this.layVideoFilter.setVisibility(View.GONE);
            this.layEffects.setVisibility(View.GONE);
            this.layFrame.setVisibility(View.GONE);
            this.layStkrMain.setVisibility(View.GONE);
            this.layBackground.setVisibility(View.GONE);
            this.layTextMain.setVisibility(View.GONE);
            this.laySticker.setVisibility(View.GONE);

            this.imgOK.setVisibility(View.VISIBLE);
            this.btnUndo.setVisibility(View.VISIBLE);
            this.btnRedo.setVisibility(View.VISIBLE);
        } else if (id == R.id.video_filter) {
            removeImageViewControll();
            hideSlideBar();
            if (this.layVideoFilter.getVisibility() != View.VISIBLE) {
                this.layVideoFilter.setVisibility(View.VISIBLE);
                this.layVideoFilter.startAnimation(this.animSlideUp);
            } else {
                this.layVideoFilter.setVisibility(View.GONE);
                this.layVideoFilter.startAnimation(this.animSlideDown);
            }
            this.layVideoAnimation.setVisibility(View.GONE);
            this.layBorder.setVisibility(View.GONE);
            this.layEffects.setVisibility(View.GONE);
            this.layFrame.setVisibility(View.GONE);
            this.layStkrMain.setVisibility(View.GONE);
            this.layBackground.setVisibility(View.GONE);
            this.layTextMain.setVisibility(View.GONE);
            this.laySticker.setVisibility(View.GONE);

            this.imgOK.setVisibility(View.VISIBLE);
            this.btnUndo.setVisibility(View.VISIBLE);
            this.btnRedo.setVisibility(View.VISIBLE);
        } else if (id == R.id.video_animation) {
            removeImageViewControll();
            hideSlideBar();
            if (this.layVideoAnimation.getVisibility() != View.VISIBLE) {
                this.layVideoAnimation.setVisibility(View.VISIBLE);
                this.layVideoAnimation.startAnimation(this.animSlideUp);
            } else {
                this.layVideoAnimation.setVisibility(View.GONE);
                this.layVideoAnimation.startAnimation(this.animSlideDown);
            }
            this.layVideoFilter.setVisibility(View.GONE);
            this.layBorder.setVisibility(View.GONE);
            this.layFrame.setVisibility(View.GONE);
            this.layEffects.setVisibility(View.GONE);
            this.layStkrMain.setVisibility(View.GONE);
            this.layBackground.setVisibility(View.GONE);
            this.layTextMain.setVisibility(View.GONE);
            this.laySticker.setVisibility(View.GONE);

            this.imgOK.setVisibility(View.VISIBLE);
            this.btnUndo.setVisibility(View.VISIBLE);
            this.btnRedo.setVisibility(View.VISIBLE);
        }
    }

    public void onGalleryButtonClick_forChangeSticker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.select_image)), SELECT_PICTURE_FROM_GALLERY_FOR_STICKER_CHANGE);
    }

    private void exportBtnDone() {
        if (playerView != null) {
            playerView.hideController();
        }
        if (postsModel != null) {
            if (postsModel.getPremium().equals("0")) {
                if (!Functions.IsPremiumEnable(context)) {
                    findViewById(R.id.freeWatermarkLayout).setVisibility(View.VISIBLE);
                }
            }
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                bitmap = viewToBitmap(mainRel);
                String[] split = ratio.split(":");
                int parseInt = Integer.parseInt(split[0]);
                int parseInt2 = Integer.parseInt(split[1]);
                if (ratio.equals("16:9") || ratio.equals("1:1") || ratio.equals("9:16") || ratio.equals("4:3") || ratio.equals("3:4") || ratio.equals("2:3")) {
                    Bitmap bitmap2 = bitmap;
                    bitmap = Bitmap.createScaledBitmap(bitmap2, bitmap2.getWidth() * 2, bitmap.getHeight() * 2, true);
                } else {
                    bitmap = Bitmap.createScaledBitmap(bitmap, parseInt, parseInt2, true);
                }
                saveImage(bitmap, true);
            }
        }, 200);
//        saveImage(this.bitmap,true);
        findViewById(R.id.freeWatermarkLayout).setVisibility(View.GONE);
    }

    private void selectControl9() {
        this.txtTextControls.setTextColor(getResources().getColor(R.color.textColor));
        this.txtFontsControl.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtFontsStyle.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtColorsControl.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtFontsCurve.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtFontsSpacing.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtShadowControl.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtBgControl.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtOutlineControl.setTextColor(getResources().getColor(R.color.textColor));
    }

    private void setRightShadow() {
        this.leftRightShadow += 4;
        int childCount = txtStkrRel.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = txtStkrRel.getChildAt(i);
            if (childAt instanceof AutofitTextRel) {
                AutofitTextRel autofitTextRel = (AutofitTextRel) childAt;
                if (autofitTextRel.getBorderVisibility()) {
                    autofitTextRel.setLeftRightShadow((float) this.leftRightShadow);
                }
            }
        }
    }

    private void setLeftShadow() {
        this.leftRightShadow -= 4;
        int childCount = txtStkrRel.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = txtStkrRel.getChildAt(i);
            if (childAt instanceof AutofitTextRel) {
                AutofitTextRel autofitTextRel = (AutofitTextRel) childAt;
                if (autofitTextRel.getBorderVisibility()) {
                    autofitTextRel.setLeftRightShadow((float) this.leftRightShadow);
                }
            }
        }
    }

    private void setBottomShadow() {
        this.topBottomShadow += 4;
        int childCount = txtStkrRel.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = txtStkrRel.getChildAt(i);
            if (childAt instanceof AutofitTextRel) {
                AutofitTextRel autofitTextRel = (AutofitTextRel) childAt;
                if (autofitTextRel.getBorderVisibility()) {
                    autofitTextRel.setTopBottomShadow((float) this.topBottomShadow);
                }
            }
        }
    }

    private void setTopShadow() {
        this.topBottomShadow -= 4;
        int childCount = txtStkrRel.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = txtStkrRel.getChildAt(i);
            if (childAt instanceof AutofitTextRel) {
                AutofitTextRel autofitTextRel = (AutofitTextRel) childAt;
                if (autofitTextRel.getBorderVisibility()) {
                    autofitTextRel.setTopBottomShadow((float) this.topBottomShadow);
                }
            }
        }
    }

    private void mainControlcolorPickerDialog(boolean z) {
        new AmbilWarnaDialog(this, this.bColor, z, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            public void onOk(AmbilWarnaDialog ambilWarnaDialog, int i) {
                updateColor(i);
            }

            public void onCancel(AmbilWarnaDialog ambilWarnaDialog) {
                Log.e(EditorActivity.TAG, "onCancel: ");
            }
        }).show();
    }

    private void mainControlShadowPickerDialog(boolean z) {
        new AmbilWarnaDialog(this, this.bColor, z, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            public void onOk(AmbilWarnaDialog ambilWarnaDialog, int i) {
                updateShadow(i);
            }

            public void onCancel(AmbilWarnaDialog ambilWarnaDialog) {
                Log.e(EditorActivity.TAG, "onCancel: ");
            }
        }).show();
    }

    private void mainControlOutlinePickerDialog(boolean z) {
        new AmbilWarnaDialog(this, this.bColor, z, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            public void onOk(AmbilWarnaDialog ambilWarnaDialog, int i) {
                updateOutline(i);
            }

            public void onCancel(AmbilWarnaDialog ambilWarnaDialog) {
                Log.e(EditorActivity.TAG, "onCancel: ");
            }
        }).show();
    }

    private void mainControlBgPickerDialog(boolean z) {
        new AmbilWarnaDialog(this, this.bColor, z, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            public void onOk(AmbilWarnaDialog ambilWarnaDialog, int i) {
                updateBgColor(i);
            }

            public void onCancel(AmbilWarnaDialog ambilWarnaDialog) {
                Log.e(EditorActivity.TAG, "onCancel: ");
            }
        }).show();
    }

    private void showResContainer() {
        this.btnUpDown.animate().setDuration(500).start();
        this.btnUpDown.setImageDrawable(getDrawable(R.drawable.control_hide_arrow));
        this.seekbarContainer.setVisibility(View.VISIBLE);
        this.layStkrMain.startAnimation(this.animSlideUp);
        this.layStkrMain.requestLayout();
        this.layStkrMain.postInvalidate();
        this.layStkrMain.post(() -> {
            EditorActivity EditorActivity = EditorActivity.this;
        });
    }

    private void hideResContainer() {
        this.btnUpDown.animate().setDuration(500).start();
        this.btnUpDown.setImageDrawable(getDrawable(R.drawable.control_show_arrow));
        this.seekbarContainer.setVisibility(View.GONE);
        this.layStkrMain.startAnimation(this.animSlideDown);
        this.layStkrMain.requestLayout();
        this.layStkrMain.postInvalidate();
        this.layStkrMain.post(() -> {
            EditorActivity EditorActivity = EditorActivity.this;
        });
    }

    private void showTextResContainer() {
        this.btnUpDown1.animate().setDuration(500).start();
        this.btnUpDown1.setImageDrawable(getDrawable(R.drawable.control_hide_arrow));
        this.layTextedit.setVisibility(View.VISIBLE);
        this.layTextMain.startAnimation(this.animSlideUp);
        this.layTextMain.requestLayout();
        this.layTextMain.postInvalidate();
        this.layTextMain.post(() -> {
            EditorActivity EditorActivity = EditorActivity.this;
        });
    }

    private void hideTextResContainer() {
        this.btnUpDown1.animate().setDuration(500).start();
        this.btnUpDown1.setImageDrawable(getDrawable(R.drawable.control_show_arrow));
        this.layTextMain.startAnimation(this.animSlideDown);
        this.layTextedit.setVisibility(View.GONE);
        this.layTextMain.requestLayout();
        this.layTextMain.postInvalidate();
        this.layTextMain.post(() -> {
            EditorActivity EditorActivity = EditorActivity.this;
        });
    }

    public void setTextBgTexture(String str) {
        getResources().getIdentifier(str, "drawable", getPackageName());
        int childCount = txtStkrRel.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = txtStkrRel.getChildAt(i);
            if (childAt instanceof AutofitTextRel) {
                AutofitTextRel autofitTextRel = (AutofitTextRel) childAt;
                if (autofitTextRel.getBorderVisibility()) {
                    autofitTextRel.setBgDrawable(str);
                    autofitTextRel.setBgAlpha(this.seekBar3.getProgress());
                    this.bgColor = 0;
                    ((AutofitTextRel) txtStkrRel.getChildAt(i)).getTextInfo().setBG_DRAWABLE(str);
                    this.bgDrawable = autofitTextRel.getBgDrawable();
                    this.bgAlpha = this.seekBar3.getProgress();
                }
            }
        }
    }

    public void setTextFonts(String str) {
        this.fontName = str;
        int childCount = txtStkrRel.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = txtStkrRel.getChildAt(i);
            if (childAt instanceof AutofitTextRel) {
                AutofitTextRel autofitTextRel = (AutofitTextRel) childAt;
                if (autofitTextRel.getBorderVisibility()) {

                    autofitTextRel.setTextFont(str);
                    saveBitmapUndu();

                }
            }
        }
    }

    private void setLetterApacing() {
        int childCount = txtStkrRel.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = txtStkrRel.getChildAt(i);
            if (childAt instanceof AutofitTextRel) {
                AutofitTextRel autofitTextRel = (AutofitTextRel) childAt;
                if (autofitTextRel.getBorderVisibility()) {
                    autofitTextRel.applyLetterSpacing(this.letterSpacing);
                }
            }
        }
    }

    private void setLineApacing() {
        int childCount = txtStkrRel.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = txtStkrRel.getChildAt(i);
            if (childAt instanceof AutofitTextRel) {
                AutofitTextRel autofitTextRel = (AutofitTextRel) childAt;
                if (autofitTextRel.getBorderVisibility()) {
                    autofitTextRel.applyLineSpacing(this.lineSpacing);
                }
            }
        }
    }

    private void setBoldFonts() {
        int childCount = txtStkrRel.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = txtStkrRel.getChildAt(i);
            if (childAt instanceof AutofitTextRel) {
                AutofitTextRel autofitTextRel = (AutofitTextRel) childAt;
                if (autofitTextRel.getBorderVisibility()) {
                    autofitTextRel.setBoldFont();
                }
            }
        }
    }

    private void setCapitalFont() {
        int childCount = txtStkrRel.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = txtStkrRel.getChildAt(i);
            if (childAt instanceof AutofitTextRel) {
                AutofitTextRel autofitTextRel = (AutofitTextRel) childAt;
                if (autofitTextRel.getBorderVisibility()) {
                    autofitTextRel.setCapitalFont();
                }
            }
        }
    }

    private void setUnderLineFont() {
        int childCount = txtStkrRel.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = txtStkrRel.getChildAt(i);
            if (childAt instanceof AutofitTextRel) {
                AutofitTextRel autofitTextRel = (AutofitTextRel) childAt;
                if (autofitTextRel.getBorderVisibility()) {
                    autofitTextRel.setUnderLineFont();
                }
            }
        }
    }

    private void setItalicFont() {
        int childCount = txtStkrRel.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = txtStkrRel.getChildAt(i);
            if (childAt instanceof AutofitTextRel) {
                AutofitTextRel autofitTextRel = (AutofitTextRel) childAt;
                if (autofitTextRel.getBorderVisibility()) {
                    autofitTextRel.setItalicFont();
                }
            }
        }
    }

    private void setLeftAlignMent() {
        int childCount = txtStkrRel.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = txtStkrRel.getChildAt(i);
            if (childAt instanceof AutofitTextRel) {
                AutofitTextRel autofitTextRel = (AutofitTextRel) childAt;
                if (autofitTextRel.getBorderVisibility()) {
                    autofitTextRel.setLeftAlignMent();
                }
            }
        }
    }

    private void setCenterAlignMent() {
        int childCount = txtStkrRel.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = txtStkrRel.getChildAt(i);
            if (childAt instanceof AutofitTextRel) {
                AutofitTextRel autofitTextRel = (AutofitTextRel) childAt;
                if (autofitTextRel.getBorderVisibility()) {
                    autofitTextRel.setCenterAlignMent();
                }
            }
        }
    }

    private void setRightAlignMent() {
        int childCount = txtStkrRel.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = txtStkrRel.getChildAt(i);
            if (childAt instanceof AutofitTextRel) {
                AutofitTextRel autofitTextRel = (AutofitTextRel) childAt;
                if (autofitTextRel.getBorderVisibility()) {
                    autofitTextRel.setRightAlignMent();
                }
            }
        }
    }

    public void setBitmapOverlay(int i) {
        this.layFilter.setVisibility(View.VISIBLE);
        this.transImg.setVisibility(View.VISIBLE);
        try {
            this.transImg.setImageBitmap(BitmapFactory.decodeResource(getResources(), i));
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            BitmapFactory.Options options2 = new BitmapFactory.Options();
            options2.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(getResources(), i, options2);
            BitmapFactory.Options options3 = new BitmapFactory.Options();
            options3.inSampleSize = ImageUtils.getClosestResampleSize(options2.outWidth, options2.outHeight, this.mainRel.getWidth() < this.mainRel.getHeight() ? this.mainRel.getWidth() : this.mainRel.getHeight());
            options2.inJustDecodeBounds = false;
            this.transImg.setImageBitmap(BitmapFactory.decodeResource(getResources(), i, options3));
        }
    }

    public void updateColor(int i) {
        int childCount = txtStkrRel.getChildCount();
        for (int i2 = 0; i2 < childCount; i2++) {
            View childAt = txtStkrRel.getChildAt(i2);
            if (childAt instanceof AutofitTextRel) {
                AutofitTextRel autofitTextRel = (AutofitTextRel) childAt;
                if (autofitTextRel.getBorderVisibility()) {
                    autofitTextRel.setTextColor(i);
                    this.tColor = i;
                    this.textColorSet = i;
                    this.horizontalPicker.setSelectedColor(i);
                    saveBitmapUndu();
                }
            }
            if (childAt instanceof StickerView) {
                StickerView stickerView = (StickerView) childAt;
                if (stickerView.getBorderVisbilty()) {
                    stickerView.setColor(i);
                    this.stkrColorSet = i;
                    this.horizontalPickerColor.setSelectedColor(i);
                    saveBitmapUndu();
                }
            }
        }
    }

    public void updateShadow(int i) {
        int childCount = txtStkrRel.getChildCount();
        for (int i2 = 0; i2 < childCount; i2++) {
            View childAt = txtStkrRel.getChildAt(i2);
            if (childAt instanceof AutofitTextRel) {
                AutofitTextRel autofitTextRel = (AutofitTextRel) childAt;
                if (autofitTextRel.getBorderVisibility()) {
                    autofitTextRel.setTextShadowColor(i);
                    this.shadowColor = i;
                    saveBitmapUndu();
                }
            }
        }
    }

    public void updateOutline(int i) {
        int childCount = txtStkrRel.getChildCount();
        for (int i2 = 0; i2 < childCount; i2++) {
            View childAt = txtStkrRel.getChildAt(i2);
            if (childAt instanceof AutofitTextRel) {
                AutofitTextRel autofitTextRel = (AutofitTextRel) childAt;
                if (autofitTextRel.getBorderVisibility()) {
                    autofitTextRel.setTextOutlineColor(i);
                    this.shadowColor = i;
                    saveBitmapUndu();
                }
            }
        }
    }

    public void updateBgColor(int i) {
        int childCount = txtStkrRel.getChildCount();
        for (int i2 = 0; i2 < childCount; i2++) {
            View childAt = txtStkrRel.getChildAt(i2);
            if (childAt instanceof AutofitTextRel) {
                AutofitTextRel autofitTextRel = (AutofitTextRel) childAt;
                if (autofitTextRel.getBorderVisibility()) {
                    autofitTextRel.setBgAlpha(this.seekBar3.getProgress());
                    autofitTextRel.setBgColor(i);
                    this.bgColor = i;
                    this.bgDrawable = "0";
                    saveBitmapUndu();
                }
            }
        }
    }

    public void updatePositionSticker(String str) {
        int childCount = txtStkrRel.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = txtStkrRel.getChildAt(i);
            if (childAt instanceof AutofitTextRel) {
                AutofitTextRel autofitTextRel = (AutofitTextRel) childAt;
                if (autofitTextRel.getBorderVisibility()) {
                    if (str.equals("incrX")) {
                        autofitTextRel.incrX();
                    }
                    if (str.equals("decX")) {
                        autofitTextRel.decX();
                    }
                    if (str.equals("incrY")) {
                        autofitTextRel.incrY();
                    }
                    if (str.equals("decY")) {
                        autofitTextRel.decY();
                    }
                }
            }
            if (childAt instanceof StickerView) {
                StickerView stickerView = (StickerView) childAt;
                if (stickerView.getBorderVisbilty()) {
                    if (str.equals("incrX")) {
                        stickerView.incrX();
                    }
                    if (str.equals("decX")) {
                        stickerView.decX();
                    }
                    if (str.equals("incrY")) {
                        stickerView.incrY();
                    }
                    if (str.equals("decY")) {
                        stickerView.decY();
                    }
                }
            }
        }
    }

    private boolean closeViewAll() {
        this.mViewAllFrame.removeAllViews();
        this.mViewAllFrame.setVisibility(View.GONE);
        return false;
    }

    public void addTextDialog(final TextInfo originTextInfo) {
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.add_text_dialog);
        dialog.setCancelable(false);
        dialog.getWindow().setAttributes(getLayoutParams(dialog));
        final AutoFitEditText autoFitEditText = (AutoFitEditText) dialog.findViewById(R.id.auto_fit_edit_text);
        TextView button = (TextView) dialog.findViewById(R.id.btnCancelDialog);
        TextView button2 = (TextView) dialog.findViewById(R.id.btnAddTextSDialog);
        if (originTextInfo != null) {
            autoFitEditText.setText(originTextInfo.getTEXT());
        } else {
            autoFitEditText.setText("");
        }
        button.setOnClickListener(view -> dialog.dismiss());
        button2.setOnClickListener(view -> {
            if (autoFitEditText.getText().toString().trim().length() > 0) {
                String replace = autoFitEditText.getText().toString().replace("\n", " ");
                TextInfo textInfo = new TextInfo();
                if (editMode) {
                    textInfo.setTEXT(replace);
                    try {
                        if (originTextInfo != null) {
                            textInfo.setFONT_NAME(originTextInfo.getFONT_NAME());
                            textInfo.setTEXT_COLOR(originTextInfo.getTEXT_COLOR());
                            textInfo.setTEXT_ALPHA(originTextInfo.getTEXT_ALPHA());
                            textInfo.setSHADOW_COLOR(originTextInfo.getSHADOW_COLOR());
                            textInfo.setSHADOW_PROG(originTextInfo.getSHADOW_PROG());
                            textInfo.setBG_COLOR(originTextInfo.getBG_COLOR());
                            textInfo.setBG_DRAWABLE(originTextInfo.getBG_DRAWABLE());
                            textInfo.setBG_ALPHA(originTextInfo.getBG_ALPHA());
                            textInfo.setROTATION(originTextInfo.getROTATION());
                            textInfo.setFIELD_TWO("");
                            textInfo.setPOS_X(originTextInfo.getPOS_X());
                            textInfo.setPOS_Y(originTextInfo.getPOS_Y());
                            textInfo.setWIDTH(originTextInfo.getWIDTH());
                            textInfo.setHEIGHT(originTextInfo.getHEIGHT());
                        } else {
                            textInfo.setFONT_NAME(fontName);
                            textInfo.setTEXT_COLOR(ViewCompat.MEASURED_STATE_MASK);
                            textInfo.setTEXT_ALPHA(100);
                            textInfo.setSHADOW_COLOR(ViewCompat.MEASURED_STATE_MASK);
                            textInfo.setSHADOW_PROG(0);
                            textInfo.setBG_COLOR(ViewCompat.MEASURED_STATE_MASK);
                            textInfo.setBG_DRAWABLE("0");
                            textInfo.setBG_ALPHA(0);
                            textInfo.setROTATION(0.0f);
                            textInfo.setFIELD_TWO("");
                            textInfo.setPOS_X((float) ((EditorActivity.txtStkrRel.getWidth() / 2) - ImageUtils.dpToPx(EditorActivity.this, 100.0f)));
                            textInfo.setPOS_Y((float) ((EditorActivity.txtStkrRel.getHeight() / 2) - ImageUtils.dpToPx(EditorActivity.this, 100.0f)));
                            textInfo.setWIDTH(ImageUtils.dpToPx(EditorActivity.this, 200.0f));
                            textInfo.setHEIGHT(ImageUtils.dpToPx(EditorActivity.this, 200.0f));
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        textInfo.setFONT_NAME(fontName);
                        textInfo.setTEXT_COLOR(ViewCompat.MEASURED_STATE_MASK);
                        textInfo.setTEXT_ALPHA(100);
                        textInfo.setSHADOW_COLOR(ViewCompat.MEASURED_STATE_MASK);
                        textInfo.setSHADOW_PROG(0);
                        textInfo.setBG_COLOR(ViewCompat.MEASURED_STATE_MASK);
                        textInfo.setBG_DRAWABLE("0");
                        textInfo.setBG_ALPHA(0);
                        textInfo.setROTATION(0.0f);
                        textInfo.setFIELD_TWO("");
                        textInfo.setPOS_X((float) ((EditorActivity.txtStkrRel.getWidth() / 2) - ImageUtils.dpToPx(EditorActivity.this, 100.0f)));
                        textInfo.setPOS_Y((float) ((EditorActivity.txtStkrRel.getHeight() / 2) - ImageUtils.dpToPx(EditorActivity.this, 100.0f)));
                        textInfo.setWIDTH(ImageUtils.dpToPx(EditorActivity.this, 200.0f));
                        textInfo.setHEIGHT(ImageUtils.dpToPx(EditorActivity.this, 200.0f));
                    }
                    int childCount = EditorActivity.txtStkrRel.getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        View childAt = EditorActivity.txtStkrRel.getChildAt(i);
                        if (childAt instanceof AutofitTextRel) {
                            AutofitTextRel autofitTextRel = (AutofitTextRel) childAt;
                            if (autofitTextRel.getBorderVisibility()) {
                                autofitTextRel.setTextInfo(textInfo, false);
                                autofitTextRel.setBorderVisibility(true);
                                boolean unused = editMode = false;
                            }
                        }
                    }
                } else {
                    textInfo.setTEXT(replace);
                    textInfo.setFONT_NAME(fontName);
                    textInfo.setTEXT_COLOR(ViewCompat.MEASURED_STATE_MASK);
                    textInfo.setTEXT_ALPHA(100);
                    textInfo.setSHADOW_COLOR(ViewCompat.MEASURED_STATE_MASK);
                    textInfo.setSHADOW_PROG(0);
                    textInfo.setBG_COLOR(ViewCompat.MEASURED_STATE_MASK);
                    textInfo.setBG_DRAWABLE("0");
                    textInfo.setBG_ALPHA(0);
                    textInfo.setROTATION(0.0f);
                    textInfo.setFIELD_TWO("");
                    textInfo.setPOS_X((float) ((EditorActivity.txtStkrRel.getWidth() / 2) - ImageUtils.dpToPx(EditorActivity.this, 100.0f)));
                    textInfo.setPOS_Y((float) ((EditorActivity.txtStkrRel.getHeight() / 2) - ImageUtils.dpToPx(EditorActivity.this, 100.0f)));
                    textInfo.setWIDTH(ImageUtils.dpToPx(EditorActivity.this, 200.0f));
                    textInfo.setHEIGHT(ImageUtils.dpToPx(EditorActivity.this, 200.0f));
                    try {
                        verticalSeekBar.setProgress(100);
                        seekbarShadow.setProgress(0);
                        seekBar3.setProgress(255);
                        AutofitTextRel autofitTextRel2 = new AutofitTextRel(EditorActivity.this);
                        EditorActivity.txtStkrRel.addView(autofitTextRel2);
                        autofitTextRel2.setTextInfo(textInfo, false);
                        autofitTextRel2.setId(ViewIdGenerator.generateViewId());
                        autofitTextRel2.setOnTouchCallbackListener(EditorActivity.this);
                        autofitTextRel2.setBorderVisibility(true);
                    } catch (ArrayIndexOutOfBoundsException e2) {
                        e2.printStackTrace();
                    }
                }
                if (layTextMain.getVisibility() == View.GONE) {
                    layTextMain.setVisibility(View.VISIBLE);
                    layTextMain.startAnimation(animSlideUp);
                }
                saveBitmapUndu();
                dialog.dismiss();
                return;
            }
            Toast.makeText(EditorActivity.this, "Please enter text here.", Toast.LENGTH_SHORT).show();
        });
        dialog.show();
    }

    private WindowManager.LayoutParams getLayoutParams(@NonNull Dialog dialog) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        if (dialog.getWindow() != null) {
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
        }
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        return layoutParams;
    }

    public void onTouchApply() {
        if (this.layStkrMain.getVisibility() == View.VISIBLE) {
            this.layStkrMain.startAnimation(this.animSlideDown);
            this.layStkrMain.setVisibility(View.GONE);
        }
        if (this.layTextMain.getVisibility() == View.VISIBLE) {
            this.layTextMain.startAnimation(this.animSlideDown);
            this.layTextMain.setVisibility(View.GONE);
        }
        if (this.seekbarContainer.getVisibility() == View.GONE) {
            this.seekbarContainer.clearAnimation();
            this.layTextMain.clearAnimation();
            this.seekbarContainer.setVisibility(View.VISIBLE);
            this.seekbarContainer.startAnimation(this.animSlideUp);
        }
        this.layStkrMain.clearAnimation();
        this.layTextMain.clearAnimation();
        removeImageViewControll();
        hideSlideBar();
    }

    private void addSticker(String str, String str2, Bitmap bitmap2) {
        if (this.layStkrMain.getVisibility() == View.GONE) {
            this.layStkrMain.setVisibility(View.VISIBLE);
            this.layStkrMain.startAnimation(this.animSlideUp);
        }
        if (this.colorType.equals("white")) {
            this.layColor.setVisibility(View.VISIBLE);
            this.layHue.setVisibility(View.GONE);
        } else {
            this.layColor.setVisibility(View.GONE);
            this.layHue.setVisibility(View.VISIBLE);
        }
        this.hueSeekbar.setProgress(1);
        removeImageViewControll();
        ElementInfo elementInfo = new ElementInfo();
        elementInfo.setPOS_X((float) ((this.mainRel.getWidth() / 2) - ImageUtils.dpToPx(this, 70.0f)));
        elementInfo.setPOS_Y((float) ((this.mainRel.getHeight() / 2) - ImageUtils.dpToPx(this, 70.0f)));
        elementInfo.setWIDTH(ImageUtils.dpToPx(this, 140.0f));
        elementInfo.setHEIGHT(ImageUtils.dpToPx(this, 140.0f));
        elementInfo.setROTATION(0.0f);
        elementInfo.setRES_ID(str);
        elementInfo.setBITMAP(bitmap2);
        elementInfo.setCOLORTYPE(this.colorType);
        elementInfo.setTYPE("STICKER");
        elementInfo.setSTC_OPACITY(255);
        elementInfo.setSTC_COLOR(0);
        elementInfo.setSTKR_PATH(str2);
        elementInfo.setSTC_HUE(this.hueSeekbar.getProgress());
        elementInfo.setFIELD_TWO("0,0");
        StickerView stickerView = new StickerView(this);
        stickerView.optimizeScreen(this.screenWidth, this.screenHeight);
        stickerView.setDefaultTouchListener(true);
        stickerView.isMultiTouchEnabled = true;
        stickerView.setViewWH((float) this.mainRel.getWidth(), (float) this.mainRel.getHeight());
        stickerView.setComponentInfo(elementInfo);
        stickerView.setId(ViewIdGenerator.generateViewId());
        txtStkrRel.addView(stickerView);


        stickerView.setOnTouchCallbackListener(this);
        stickerView.setBorderVisibility(true);
        currentStickerView = stickerView;
        if (this.seekbarContainer.getVisibility() == View.GONE) {
            this.seekbarContainer.setVisibility(View.VISIBLE);
            this.seekbarContainer.startAnimation(this.animSlideUp);
        }
    }

    private void saveImage(Bitmap bitmap, boolean z) {
        Functions.showLoader(this);
        Functions.dialog.findViewById(R.id.tv_lay).setVisibility(View.VISIBLE);
        loaderMessageTv = Functions.dialog.findViewById(R.id.message_tv);

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
        filename = file.getPath() + File.separator + str2;

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
                startSavingAction(filename);
            } else {
                Functions.cancelLoader();
                Functions.showToast(this, getString(R.string.error));
            }

        }
    }

    public void saveBitmap(final boolean z) {
        Functions.showLoader(this);
        Functions.dialog.findViewById(R.id.tv_lay).setVisibility(View.VISIBLE);
        loaderMessageTv = Functions.dialog.findViewById(R.id.message_tv);
        new Thread(() -> {
            String str;
            try {
//                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "/Thumbnail Design");
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
                    str = str2 + ".png";
                } else {
                    str = str2 + ".jpg";
                }
                filename = file.getPath() + File.separator + str;
                File file2 = new File(filename);
                try {
                    if (!file2.exists()) {
                        file2.createNewFile();
                    }
                    FileOutputStream fileOutputStream = new FileOutputStream(file2);
                    this.checkMemory = this.bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);

                    fileOutputStream.flush();
                    fileOutputStream.close();
                    MediaScannerConnection.scanFile(EditorActivity.this, new String[]{file2.getAbsolutePath()}, null, (str1, uri) -> {
                        Log.i("ExternalStorage", "Scanned " + str1 + ":");
                        String sb = "-> uri=" +
                                uri;
                        Log.i("ExternalStorage", sb);
                    });

                    if (checkMemory) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startSavingAction(filename);
                            }
                        });
                    } else {
                        Functions.cancelLoader();
                        Log.d("finalProcess__", musicPath);
                        new AlertDialog.Builder(EditorActivity.this, 16974126).setMessage(Constants.getSpannableString(EditorActivity.this, R.string.memoryerror)).setPositiveButton(Constants.getSpannableString(EditorActivity.this, R.string.ok), (dialogInterface1, i) -> dialogInterface1.dismiss()).create().show();
                    }
                } catch (Exception e) {
                    Functions.cancelLoader();
                    Log.d("finalProcess__", e.getMessage());
                }
                Thread.sleep(1000);
            } catch (Exception e2) {
                Functions.cancelLoader();
                Log.d("finalProcess__", musicPath);
            }
        }).start();
    }

    private void startSavingAction(String framePath) {
        Log.d("finalProcess__", musicPath);
        if (!musicPath.isEmpty()) {
            if (musicPath.startsWith("http")) {
                loaderMessageTv.setText("Downloading music..");
                Functions.downloadMusicFile(this, musicPath, new Callback() {
                    @Override
                    public void Responce(String resp) {
                        if (resp != null) {
                            musicPath = resp;
                            if (isMovie) {
                                demoPresenter.setMusic(musicPath);
                            }
                            movieAction(framePath);
                        } else {
                            Log.d("finalProcess__", "Downloading Error..");
                            loaderMessageTv.setText("Downloading Error..");
                            Functions.cancelLoader();
                        }
                    }
                });
            } else {
                movieAction(framePath);
            }
        } else {
            movieAction(framePath);
        }
    }

    private void movieAction(String framePath) {
        if (isMovie) {
            loaderMessageTv.setText(R.string.creating_movie);
            demoPresenter.saveVideo(new SaveVideoResponse() {
                @Override
                public void onVideoSAve(String s) {
                    backgroundPosterPath = s;
                    filename = s;
                    applyFrameOnVideo(framePath);
                }
            });
        } else {
            if (backgroundPosterPath.endsWith(".mp4")) {
                loaderMessageTv.setText(R.string.downloading_video);
                if (backgroundPosterPath.startsWith("http")) {
                    Functions.downloadVideoFile(this, backgroundPosterPath, new Callback() {
                        @Override
                        public void Responce(String resp) {
                            if (resp != null) {
                                backgroundPosterPath = resp;
                                if (!musicPath.isEmpty()) {
                                    applyMp3OnVideo(framePath, false);
                                } else {
                                    applyFrameOnVideo(framePath);
                                }
                            } else {
                                Log.d("finalProcess__", "Downloading Error..");
                                loaderMessageTv.setText("Downloading Error..");
                                Functions.cancelLoader();
                            }
                        }
                    });
                } else {
                    if (!musicPath.isEmpty()) {
                        applyMp3OnVideo(framePath, false);
                    } else {
                        applyFrameOnVideo(framePath);
                    }
                }
            } else {
                if (!musicPath.isEmpty()) {
                    imageToVideo(framePath);
                } else {
                    filename = framePath;
                    imageSavedSuccess();
                }
            }
        }

    }

    private void imageToVideo(String framePath) {
        String outputDir = Functions.getAppFolder(context) + "/image_to_video" + System.currentTimeMillis() + ".mp4";
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loaderMessageTv.setText(R.string.applying_image_to_video);

                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(musicPath);
                int duration = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));

                int height = backgroundImg.getHeight();
                int width = backgroundImg.getWidth();

                Log.e("finalProcess__", "Duration -> " + duration);
                Log.e("finalProcess__", "Width -> " + backgroundImg.getWidth() + " Height -> " + backgroundImg.getHeight());

                try {
                    new ImageToVideoConverter(
                            outputDir,
                            framePath,
                            new EncodeListener() {
                                @Override
                                public void onProgress(float v) {

                                }

                                @Override
                                public void onCompleted() {
                                    backgroundPosterPath = outputDir;
                                    applyMp3OnVideo(framePath, true);
                                }

                                @Override
                                public void onFailed(@NonNull Exception e) {
                                    Functions.cancelLoader();
                                    Log.e("finalProcess__", "onFailed " + e.getMessage());
                                }
                            },
                            new Size(width, height),
                            TimeUnit.SECONDS.toMicros(duration / 1000)).start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void applyMp3OnVideo(String framePath, boolean isFrameapplied) {
        Log.e("finalProcess__", "applyMp3OnVideo " + backgroundPosterPath);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                ffmpeg -i video.mp4 -i audio.wav -map 0:v -map 1:a -c:v copy -shortest output.mp4
//                ffmpeg -i v.mp4 -i a.wav -c:v copy -map 0:v:0 -map 1:a:0 new.mp4
//                ffmpeg -i source_video.mp4 -i source_audio.wav -acodec copy -vcodec copy -map 0:v:0 -map 1:a:0 final_video.mov
//                ffmpeg -i input_filename.avi -acodec mp3 -vcodec copy output_filename.avi

                String outputDir = Functions.getAppFolder(context) + "/apply_mp3_" + System.currentTimeMillis() + ".mp4";
                loaderMessageTv.setText(R.string.applying_music);

                FFmpeg.executeAsync(new String[]{"-i", backgroundPosterPath, "-i", musicPath,
                        "-acodec", "copy",
                        "-vcodec", "copy", "-map",
                        "0:v:0", "-map", "1:a:0", outputDir}, new ExecuteCallback() {
                    @Override
                    public void apply(long executionId, int returnCode) {
                        if (returnCode == 1) {
                            FFmpeg.cancel(executionId);
                            Functions.cancelLoader();
                            Functions.showToast(EditorActivity.this, "Try Again!!");
                        }
                        if (returnCode == 0) {
                            if (isFrameapplied) {
                                Functions.cancelLoader();
                                filename = outputDir;
                                imageSavedSuccess();
                            } else {
                                applyFrameOnVideo(framePath);
                            }
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

    private void applyFrameOnVideo(String framePath) {
        Log.e("finalProcess__", backgroundPosterPath);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String outputDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/apply_frame_" + System.currentTimeMillis() + ".mp4";
                loaderMessageTv.setText(R.string.applying_frame);

                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(backgroundPosterPath);
                int width = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
                int height = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
                try {
                    retriever.release();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Bitmap b = BitmapFactory.decodeFile(framePath);
                Bitmap out = Bitmap.createScaledBitmap(b, width, height, false);

                File file = new File(Functions.getAppFolder(context), "resizeframe.png");
                FileOutputStream fOut = null;
                try {
                    fOut = new FileOutputStream(file);
                    out.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                    fOut.flush();
                    fOut.close();
                    b.recycle();
                    out.recycle();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                FFmpeg.executeAsync(new String[]{"-i", backgroundPosterPath, "-i", file.getAbsolutePath(),
                        "-filter_complex",
                        "overlay", "-r", "150",
                        "-vb", "20M",
                        "-y", outputDir}, new ExecuteCallback() {
                    @Override
                    public void apply(long executionId, int returnCode) {
                        if (returnCode == 1) {
                            FFmpeg.cancel(executionId);
                            Functions.cancelLoader();
                            Functions.showToast(EditorActivity.this, "Try Again!!");
                        }
                        if (returnCode == 0) {
                            FFmpeg.cancel(executionId);
                            Functions.cancelLoader();
                            filename = outputDir;
                            imageSavedSuccess();
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

    public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
        int id = seekBar.getId();
        int i2 = 0;
        if (id == R.id.alpha_seekBar) {
            int childCount = txtStkrRel.getChildCount();
            while (i2 < childCount) {
                View childAt = txtStkrRel.getChildAt(i2);
                if (childAt instanceof StickerView) {
                    StickerView stickerView = (StickerView) childAt;
                    if (stickerView.getBorderVisbilty()) {
                        stickerView.setAlphaProg(i);
                    }
                }
                i2++;
            }
        } else if (id != R.id.hue_seekBar) {
            if (id == R.id.seek) {
                this.alpha = i;
                if (Build.VERSION.SDK_INT >= 16) {
                    this.transImg.setImageAlpha(this.alpha);
                } else {
                    this.transImg.setAlpha(this.alpha);
                }
            } else if (id == R.id.seek_border) {
                this.alpha = i;
                this.shapeRel.setPadding(i, i, i, i);
            } else if (id == R.id.seekBar2) {
                this.processs = i;
                int childCount2 = txtStkrRel.getChildCount();
                while (i2 < childCount2) {
                    View childAt2 = txtStkrRel.getChildAt(i2);
                    if (childAt2 instanceof AutofitTextRel) {
                        AutofitTextRel autofitTextRel = (AutofitTextRel) childAt2;
                        if (autofitTextRel.getBorderVisibility()) {
                            autofitTextRel.setTextAlpha(i);
                        }
                    }
                    i2++;
                }
            } else if (id == R.id.seekBar3) {
                int childCount3 = txtStkrRel.getChildCount();
                while (i2 < childCount3) {
                    View childAt3 = txtStkrRel.getChildAt(i2);
                    if (childAt3 instanceof AutofitTextRel) {
                        AutofitTextRel autofitTextRel2 = (AutofitTextRel) childAt3;
                        if (autofitTextRel2.getBorderVisibility()) {
                            autofitTextRel2.setBgAlpha(i);
                            this.bgAlpha = i;
                        }
                    }
                    i2++;
                }
            } else if (id == R.id.seekBar_shadow) {
                int childCount4 = txtStkrRel.getChildCount();
                while (i2 < childCount4) {
                    View childAt4 = txtStkrRel.getChildAt(i2);
                    if (childAt4 instanceof AutofitTextRel) {
                        AutofitTextRel autofitTextRel3 = (AutofitTextRel) childAt4;
                        if (autofitTextRel3.getBorderVisibility()) {
                            autofitTextRel3.setTextShadowProg(i);
                            this.shadowProg = i;
                        }
                    }
                    i2++;
                }
            } else if (id == R.id.seekLetterSpacing) {
                this.letterSpacing = (float) (i / 3);
                setLetterApacing();
            } else if (id == R.id.seekLineSpacing) {
                this.lineSpacing = (float) (i / 2);
                setLineApacing();
            } else if (id == R.id.seekOutlineSize) {
                int childCount5 = txtStkrRel.getChildCount();
                while (i2 < childCount5) {
                    View childAt5 = txtStkrRel.getChildAt(i2);
                    if (childAt5 instanceof AutofitTextRel) {
                        AutofitTextRel autofitTextRel4 = (AutofitTextRel) childAt5;
                        if (autofitTextRel4.getBorderVisibility()) {
                            autofitTextRel4.setTextOutlLine(i);
                        }
                    }
                    i2++;
                }
            } else if (id == R.id.seekShadowBlur) {
                int childCount6 = txtStkrRel.getChildCount();
                while (i2 < childCount6) {
                    View childAt6 = txtStkrRel.getChildAt(i2);
                    if (childAt6 instanceof AutofitTextRel) {
                        AutofitTextRel autofitTextRel5 = (AutofitTextRel) childAt6;
                        if (autofitTextRel5.getBorderVisibility()) {
                            autofitTextRel5.setTextShadowOpacity(i);
                        }
                    }
                    i2++;
                }
            } else if (id == R.id.seekTextCurve) {
                mRadius = seekBar.getProgress() - 360;
                int i3 = mRadius;
                if (i3 <= 0 && i3 >= -8) {
                    mRadius = -8;
                }
                int childCount7 = txtStkrRel.getChildCount();
                while (i2 < childCount7) {
                    View childAt7 = txtStkrRel.getChildAt(i2);
                    if (childAt7 instanceof AutofitTextRel) {
                        AutofitTextRel autofitTextRel6 = (AutofitTextRel) childAt7;
                        if (autofitTextRel6.getBorderVisibility()) {
                            autofitTextRel6.setDrawParams();
                        }
                    }
                    i2++;
                }
            }
        } else {
            int childCount8 = txtStkrRel.getChildCount();
            while (i2 < childCount8) {
                View childAt8 = txtStkrRel.getChildAt(i2);
                if (childAt8 instanceof StickerView) {
                    StickerView stickerView2 = (StickerView) childAt8;
                    if (stickerView2.getBorderVisbilty()) {
                        stickerView2.setHueProg(i);
                    }
                }
                i2++;
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

        //do something

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (exoplayer != null) {
            exoplayer.setPlayWhenReady(false);
        }
        if (demoPresenter != null) {
            demoPresenter.onPause();
        }
        stopMusic();
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
        int id = seekBar.getId();
        if (id == R.id.alpha_seekBar || id == R.id.hue_seekBar || id == R.id.seekBar2) {
            saveBitmapUndu();
        }
    }

    private void touchDown(View view, String str) {
        this.leftRightShadow = 0;
        this.topBottomShadow = 0;
        this.focusedView = view;
        if (str.equals("hideboder")) {
            removeImageViewControll();
        }
        hideSlideBar();
        if (view instanceof StickerView) {
            this.layBorder.setVisibility(View.GONE);
            this.layEffects.setVisibility(View.GONE);
            this.layTextMain.setVisibility(View.GONE);
            this.layFrame.setVisibility(View.GONE);
            this.layStkrMain.setVisibility(View.GONE);
            this.layVideoAnimation.setVisibility(View.GONE);
            this.layVideoFilter.setVisibility(View.GONE);
            StickerView stickerView = (StickerView) view;
            this.stkrColorSet = stickerView.getColor();
            this.alphaSeekbar.setProgress(stickerView.getAlphaProg());
            this.hueSeekbar.setProgress(stickerView.getHueProg());
        }
        if (view instanceof AutofitTextRel) {
            this.layVideoAnimation.setVisibility(View.GONE);
            this.layVideoFilter.setVisibility(View.GONE);
            this.layBorder.setVisibility(View.GONE);
            this.layFrame.setVisibility(View.GONE);
            this.layEffects.setVisibility(View.GONE);
            this.layStkrMain.setVisibility(View.GONE);
            this.layTextMain.setVisibility(View.GONE);
        }

    }

    private void hideSlideBar() {
        if (layContainer.getVisibility() == View.VISIBLE) {
            layContainer.animate().translationX((float) (-layContainer.getRight())).setDuration(200).setInterpolator(new AccelerateInterpolator()).start();
            new Handler().postDelayed(() -> {
                EditorActivity.layContainer.setVisibility(View.GONE);
                EditorActivity.btnLayControls.setVisibility(View.VISIBLE);
            }, 200);
        }
    }

    private void touchMove(View view) {
        boolean z = view instanceof StickerView;
        if (z) {
            StickerView stickerView = (StickerView) view;
            this.alphaSeekbar.setProgress(stickerView.getAlphaProg());
            this.hueSeekbar.setProgress(stickerView.getHueProg());
        } else {
            this.layTextMain.setVisibility(View.GONE);
        }
        if (z) {
            this.layVideoAnimation.setVisibility(View.GONE);
            this.layVideoFilter.setVisibility(View.GONE);
            this.layBorder.setVisibility(View.GONE);
            this.layEffects.setVisibility(View.GONE);
            this.layFrame.setVisibility(View.GONE);
            this.layTextMain.setVisibility(View.GONE);
            this.layStkrMain.setVisibility(View.GONE);
        }
        if (view instanceof AutofitTextRel) {
            this.layVideoAnimation.setVisibility(View.GONE);
            this.layVideoFilter.setVisibility(View.GONE);
            this.layBorder.setVisibility(View.GONE);
            this.layFrame.setVisibility(View.GONE);
            this.layEffects.setVisibility(View.GONE);
            this.layTextMain.setVisibility(View.GONE);
            this.layStkrMain.setVisibility(View.GONE);
        }
    }

    private void touchUp(final View view) {
        if (this.focusedCopy != this.focusedView) {
            this.seekbarContainer.setVisibility(View.VISIBLE);
            this.layTextedit.setVisibility(View.VISIBLE);
        }
        if (view instanceof AutofitTextRel) {
            this.rotation = view.getRotation();


            if (this.layTextMain.getVisibility() == View.GONE) {
                this.layTextMain.setVisibility(View.VISIBLE);
                this.layTextMain.startAnimation(this.animSlideUp);
            }
            int i = this.processs;
            if (i != 0) {
                this.verticalSeekBar.setProgress(i);
            }
        }
        if ((view instanceof StickerView) && this.layStkrMain.getVisibility() == View.GONE) {
            if ((((StickerView) view).getColorType()).equals("white")) {
                this.layColor.setVisibility(View.VISIBLE);
                this.layHue.setVisibility(View.GONE);
            } else {
                this.layColor.setVisibility(View.GONE);
                this.layHue.setVisibility(View.VISIBLE);
            }
            this.layStkrMain.setVisibility(View.VISIBLE);
            currentStickerView = (StickerView) view;
            this.layStkrMain.startAnimation(this.animSlideUp);
        }
        if (this.seekbarContainer.getVisibility() == View.GONE) {
            this.seekbarContainer.startAnimation(this.animSlideDown);
            this.seekbarContainer.setVisibility(View.GONE);
        }
    }

    public void onDelete() {
        if (this.layStkrMain.getVisibility() == View.VISIBLE) {
            this.layStkrMain.startAnimation(this.animSlideDown);
            this.layStkrMain.setVisibility(View.GONE);
        }
        if (this.layTextMain.getVisibility() == View.GONE) {
            this.layTextMain.startAnimation(this.animSlideDown);
            this.layTextMain.setVisibility(View.GONE);
        }
        saveBitmapUndu();
    }

    @Override
    public void onEdit(View view, Uri uri) {

    }

    @Override
    public void onMidX(View view) {

    }

    @Override
    public void onMidXY(View view) {

    }

    @Override
    public void onMidY(View view) {

    }

    public void onRotateDown(View view) {
        touchDown(view, "viewboder");
    }

    public void onRotateMove(View view) {
        touchMove(view);
    }

    public void onRotateUp(View view) {
        touchUp(view);
    }

    public void onScaleDown(View view) {
        touchDown(view, "viewboder");
    }

    public void onScaleMove(View view) {
        touchMove(view);
    }

    public void onScaleUp(View view) {
        touchUp(view);
    }

    public void onTouchDown(View view) {
        touchDown(view, "hideboder");
        if (this.checkTouchContinue) {
            this.layStkrMain.post(() -> {
                checkTouchContinue = true;
            });
        }
    }

    public void onTouchMove(View view) {
        touchMove(view);
    }

    public void onTouchUp(View view) {
        this.checkTouchContinue = false;
        touchUp(view);
    }

    @Override
    public void onXY(View view) {

    }

    public void onTouchMoveUpClick(View view) {
        saveBitmapUndu();
    }

    public void onDoubleTap() {
        doubleTabPrass();
    }

    private void doubleTabPrass() {
        this.editMode = true;
        try {

            int childCount3 = txtStkrRel.getChildCount();
            for (int i3 = 0; i3 < childCount3; i3++) {
                View childAt3 = txtStkrRel.getChildAt(i3);
                if (childAt3 instanceof AutofitTextRel) {
                    AutofitTextRel autofitTextRel = (AutofitTextRel) childAt3;
                    if (autofitTextRel.getBorderVisibility()) {
                        TextInfo textInfo = autofitTextRel.getTextInfo();
                        this.layVideoAnimation.setVisibility(View.GONE);
                        this.layVideoFilter.setVisibility(View.GONE);
                        this.layBorder.setVisibility(View.GONE);
                        this.layFrame.setVisibility(View.GONE);
                        this.layEffects.setVisibility(View.GONE);
                        this.layStkrMain.setVisibility(View.GONE);
                        this.layTextMain.setVisibility(View.GONE);

                        addTextDialog(textInfo);
                    }
                }
            }

        } catch (NullPointerException e) {

            e.printStackTrace();

        }
    }

    public void removeImageViewControll() {
        RelativeLayout relativeLayout = txtStkrRel;
        if (relativeLayout != null) {
            int childCount = relativeLayout.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = txtStkrRel.getChildAt(i);
                if (childAt instanceof AutofitTextRel) {
                    ((AutofitTextRel) childAt).setBorderVisibility(false);
                }

                if (childAt instanceof StickerView) {
                    Log.e("remove", "==");


                    ((StickerView) childAt).setBorderVisibility(false);
                }
            }
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        int i3 = i;
        int i4 = i2;
        Intent intent2 = intent;
        super.onActivityResult(i, i2, intent);
        if (i4 == -1) {
            if (intent2 != null) {
                if (i3 == SELECT_PICTURE_FROM_GALLERY) {
                    try {
                        isChanageSticker = false;
                        Uri fromFile = Uri.fromFile(new File(getCacheDir(), "SampleCropImage" + System.currentTimeMillis() + ".png"));
                        UCrop.Options options2 = new UCrop.Options();
                        options2.setCompressionFormat(Bitmap.CompressFormat.PNG);
                        options2.setToolbarColor(getResources().getColor(R.color.backgroundColor));
                        options2.setFreeStyleCropEnabled(true);

                        UCrop.of(intent.getData(), fromFile).withOptions(options2).start(this);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (i3 == SELECT_PICTURE_FROM_GALLERY_FOR_STICKER_CHANGE) {
                    try {
                        isChanageSticker = true;
                        Uri fromFile = Uri.fromFile(new File(getCacheDir(), "SampleCropImage" + System.currentTimeMillis() + ".png"));
                        UCrop.Options options2 = new UCrop.Options();
                        options2.setCompressionFormat(Bitmap.CompressFormat.PNG);
                        options2.setToolbarColor(getResources().getColor(R.color.backgroundColor));
                        options2.setFreeStyleCropEnabled(true);
                        UCrop.of(intent.getData(), fromFile).withOptions(options2).start(this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (i3 == SELECT_PICTURE_FROM_GALLERY_BACKGROUND) {
                    try {
                        isBackground = true;
                        this.layBackground.setVisibility(View.GONE);
                        this.imgOK.setVisibility(View.VISIBLE);
                        if (this.layBackground.getVisibility() == View.VISIBLE) {
                            this.layBackground.startAnimation(this.animSlideDown);
                            this.layBackground.setVisibility(View.GONE);
                        }
                        this.screenWidth = (float) backgroundImg.getWidth();
                        this.screenHeight = (float) backgroundImg.getHeight();
                        Constants.bitmap = ImageUtils.scaleCenterCrop(Constants.getBitmapFromUri(this, intent.getData(), this.screenWidth, this.screenHeight), (int) this.screenHeight, (int) this.screenWidth);
                        this.hex = "";
                        setImageBitmapAndResizeLayout(Constants.bitmap);
                    } catch (Exception e3) {
                        e3.printStackTrace();
                    }
                }
                if (i4 == -1 && i3 == 69) {
                    if (isGreeting) {
                        colorType = "";
                        ImageView imageView = new ImageView(context);
                        Glide.with(context).load(MyUtils.getPathFromURI(context, UCrop.getOutput(intent))).into(imageView);
//TODO ZoomLayout

//                        greeting_zoom_lay.addView(imageView);
//                        greeting_zoom_lay.setVisibility(View.VISIBLE);
                        findViewById(R.id.greeting_btn).setVisibility(View.GONE);

                        isGreeting = false;

                    } else {
                        new PickedImageActionFragment(currentStickerView != null ? currentStickerView.getId() : 0, MyUtils.getPathFromURI(context, UCrop.getOutput(intent)), new PickedImageActionFragment.OnBitmapSelect() {
                            @Override
                            public void output(int id, String out) {
                                if (isChanageSticker) {
                                    int childCount = txtStkrRel.getChildCount();
                                    int i5 = id;
                                    for (int i6 = 0; i6 < childCount; i6++) {
                                        View childAt = txtStkrRel.getChildAt(i6);
                                        if ((childAt instanceof StickerView) && childAt.getId() == i5) {
                                            ((StickerView) childAt).setStrPath(out);
                                            saveBitmapUndu();
                                        }
                                    }
                                } else {
                                    EditorActivity.this.colorType = "colored";
                                    addSticker("", out, null);
                                }
                            }
                        }).show(getSupportFragmentManager(), "");
                    }
                } else if (i4 == 96) {
                    UCrop.getError(intent);
                }
                if (i3 == 4) {
                    Toast.makeText(context, "Custom", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void handleCropResult(String intent) {
        this.layBackground.setVisibility(View.GONE);
        this.imgOK.setVisibility(View.VISIBLE);
        if (getSupportFragmentManager().getBackStackEntryCount() >= 1) {
            getSupportFragmentManager().popBackStack();
        }


        if (this.isBackground) {
            this.hex = "";
            try {
                if (this.seekbarContainer.getVisibility() == View.GONE) {
                    this.seekbarContainer.setVisibility(View.VISIBLE);
                    this.seekbarContainer.startAnimation(this.animSlideUp);
                }
                bitmapRatio(this.ratio, ImageUtils.getResampleImageBitmap(Uri.parse(intent), this, (int) (this.screenWidth > this.screenHeight ? this.screenWidth : this.screenHeight)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            this.colorType = "colored";
        }

        if (isChanageSticker) {
            txtStkrRel.removeView(currentStickerView);
            if (this.layStkrMain.getVisibility() == View.GONE) {
                this.layStkrMain.setVisibility(View.VISIBLE);
                this.layStkrMain.startAnimation(this.animSlideUp);
            }
            try {
                if (this.colorType.equals("white")) {
                    this.layColor.setVisibility(View.VISIBLE);
                    this.layHue.setVisibility(View.GONE);
                } else {
                    this.layColor.setVisibility(View.GONE);
                    this.layHue.setVisibility(View.VISIBLE);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }


            this.hueSeekbar.setProgress(1);
            removeImageViewControll();
            ElementInfo elementInfo = new ElementInfo();
            elementInfo.setPOS_X(currentStickerView.getComponentInfo().getPOS_X());
            elementInfo.setPOS_Y(currentStickerView.getComponentInfo().getPOS_Y());
            elementInfo.setWIDTH(currentStickerView.getComponentInfo().getWIDTH());
            elementInfo.setHEIGHT(currentStickerView.getComponentInfo().getHEIGHT());
            elementInfo.setROTATION(currentStickerView.getComponentInfo().getROTATION());
            elementInfo.setRES_ID("");
            elementInfo.setBITMAP(bitmap);
            elementInfo.setCOLORTYPE(this.colorType);
            elementInfo.setTYPE("STICKER");
            elementInfo.setSTC_OPACITY(currentStickerView.getComponentInfo().getSTC_OPACITY());
            elementInfo.setSTC_COLOR(0);
            elementInfo.setSTKR_PATH(intent);
            elementInfo.setSTC_HUE(currentStickerView.getComponentInfo().getSTC_HUE());
            elementInfo.setFIELD_TWO("0,0");
            StickerView stickerView = new StickerView(this);
            stickerView.optimizeScreen(this.screenWidth, this.screenHeight);
            stickerView.setViewWH((float) this.mainRel.getWidth(), (float) this.mainRel.getHeight());
            stickerView.setComponentInfo(elementInfo);
            stickerView.setId(ViewIdGenerator.generateViewId());
            txtStkrRel.addView(stickerView);
            stickerView.setOnTouchCallbackListener(this);
            stickerView.setBorderVisibility(true);
            currentStickerView = stickerView;
            if (this.seekbarContainer.getVisibility() == View.GONE) {
                this.seekbarContainer.setVisibility(View.VISIBLE);
                this.seekbarContainer.startAnimation(this.animSlideUp);
            }
//
        } else {
            this.colorType = "colored";
            addSticker("", intent, null);
        }

        this.isBackground = false;
    }

//    public void openCustomActivity(Bundle bundle, Intent intent) {
//        Bundle extras = intent.getExtras();
//        this.profile = "no";
//        if (this.profile.equals("no")) {
//            this.profile = TEMP_PATH;
//            this.hex = "";
//            setImageBitmapAndResizeLayout(ImageUtils.resizeBitmap(Constants.bitmap, (int) this.screenWidth, (int) this.screenHeight));
//            return;
//        }
//        this.hex = extras.getString("color");
//        drawBackgroundImageFromDp(this.ratio, this.profile);
//    }

    private int gcd(int i, int i2) {
        return i2 == 0 ? i : gcd(i2, i % i2);
    }

    public void onGalleryButtonClick() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction("android.intent.action.PICK");
        startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.select_image)), SELECT_PICTURE_FROM_GALLERY);
    }

    public void onCameraButtonClick() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = Functions.createImageFile(context);
                this.file = photoFile.getAbsolutePath();
            } catch (Exception ex) {
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, getPackageName() + ".provider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                resultCallbackForCamera.launch(pictureIntent);
            }
        }
    }

    public void onGalleryBackground() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_image)), SELECT_PICTURE_FROM_GALLERY_BACKGROUND);
    }

    private void colorPickerDialog(boolean z) {
        new AmbilWarnaDialog(this, this.bColor, z, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            public void onCancel(AmbilWarnaDialog ambilWarnaDialog) {
            }

            public void onOk(AmbilWarnaDialog ambilWarnaDialog, int i) {
                updateBackgroundColor(i);
            }
        }).show();
    }


    public void updateBackgroundColor(int i) {
        this.layBackground.setVisibility(View.GONE);
        this.imgOK.setVisibility(View.VISIBLE);
        if (this.layBackground.getVisibility() == View.VISIBLE) {
            this.layBackground.startAnimation(this.animSlideDown);
            this.layBackground.setVisibility(View.GONE);
        }
        Bitmap createBitmap = Bitmap.createBitmap(720, 1280, Bitmap.Config.ARGB_8888);
        createBitmap.eraseColor(i);
        Log.e(TAG, "updateColor: ");
        try {
            this.screenWidth = (float) backgroundImg.getWidth();
            this.screenHeight = (float) backgroundImg.getHeight();
            Constants.bitmap = ImageUtils.scaleCenterCrop(createBitmap, (int) this.screenHeight, (int) this.screenWidth);
            this.hex = "";
            setImageBitmapAndResizeLayout(Constants.bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() >= 1) {
            getSupportFragmentManager().popBackStack();
        }
        if (this.mViewAllFrame.getVisibility() == View.VISIBLE) {
            closeViewAll();
        } else if (this.layTextMain.getVisibility() == View.VISIBLE) {
            if (this.layTextedit.getVisibility() == View.VISIBLE) {
                hideTextResContainer();
                return;
            }
            showBackDialog();
        } else if (this.layStkrMain.getVisibility() == View.VISIBLE) {
            if (this.seekbarContainer.getVisibility() == View.VISIBLE) {
                hideResContainer();
                return;
            }
            showBackDialog();
        } else if (this.laySticker.getVisibility() == View.VISIBLE) {
            laySticker.startAnimation(animSlideDown);
            laySticker.setVisibility(View.GONE);
            imgOK.setVisibility(View.VISIBLE);
            btnUndo.setVisibility(View.VISIBLE);
            btnRedo.setVisibility(View.VISIBLE);
            this.bckprassSticker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    layEffects.setVisibility(View.GONE);
                    laySticker.startAnimation(animSlideDown);
                    laySticker.setVisibility(View.GONE);
                    imgOK.setVisibility(View.VISIBLE);
                    btnUndo.setVisibility(View.VISIBLE);
                    btnRedo.setVisibility(View.VISIBLE);
                }
            });
            btnLayControls.setVisibility(View.VISIBLE);
        } else if (this.seekbarContainer.getVisibility() == View.VISIBLE) {
            this.seekbarContainer.startAnimation(this.animSlideDown);
            this.seekbarContainer.setVisibility(View.GONE);
        } else if (this.layEffects.getVisibility() == View.VISIBLE) {
            this.layEffects.startAnimation(this.animSlideDown);
            this.layEffects.setVisibility(View.GONE);
        } else if (this.layBorder.getVisibility() == View.VISIBLE) {
            this.layBorder.startAnimation(this.animSlideDown);
            this.layBorder.setVisibility(View.GONE);
        } else if (this.layFrame.getVisibility() == View.VISIBLE) {
            this.layFrame.startAnimation(this.animSlideDown);
            this.layFrame.setVisibility(View.GONE);
        } else if (this.layVideoFilter.getVisibility() == View.VISIBLE) {
            this.layVideoFilter.startAnimation(this.animSlideDown);
            this.layVideoFilter.setVisibility(View.GONE);
        } else if (this.layVideoAnimation.getVisibility() == View.VISIBLE) {
            this.layVideoAnimation.startAnimation(this.animSlideDown);
            this.layVideoAnimation.setVisibility(View.GONE);
        } else if (this.layMusic.getVisibility() == View.VISIBLE) {
            this.layMusic.startAnimation(this.animSlideDown);
            this.layMusic.setVisibility(View.GONE);
            this.imgOK.setVisibility(View.VISIBLE);
            this.btnUndo.setVisibility(View.VISIBLE);
            this.btnRedo.setVisibility(View.VISIBLE);
        } else if (this.layBackground.getVisibility() == View.VISIBLE) {
            this.layBackground.startAnimation(this.animSlideDown);
            this.imgOK.setVisibility(View.VISIBLE);
            this.btnUndo.setVisibility(View.VISIBLE);
            this.btnRedo.setVisibility(View.VISIBLE);
            this.layBackground.setVisibility(View.GONE);
        } else if (layContainer.getVisibility() == View.VISIBLE) {
            layContainer.animate().translationX((float) (-layContainer.getRight())).setDuration(200).setInterpolator(new AccelerateInterpolator()).start();
            new Handler().postDelayed(() -> {
                EditorActivity.layContainer.setVisibility(View.GONE);
                EditorActivity.btnLayControls.setVisibility(View.VISIBLE);
            }, 200);
        } else {
            showBackDialog();
        }
    }

    private void showBackDialog() {
        new CustomeDialogFragment(getString(R.string.alert),
                getString(R.string.editor_exit_dialog),
                DialogType.WARNING,
                true,
                true,
                true,
                new CustomeDialogFragment.DialogCallback() {
                    @Override
                    public void onCencel() {
                    }

                    @Override
                    public void onSubmit() {
                        finish();
                    }

                    @Override
                    public void onDismiss() {
                    }

                    @Override
                    public void onComplete(Dialog dialog) {
                    }
                }).show(getSupportFragmentManager(), "");
    }


    public void imageSavedSuccess() {
        Intent intent = new Intent(EditorActivity.this, PreviewActivity.class);
        intent.putExtra("url", filename);
        startActivity(intent);
        finish();
    }

    private void showPicImageDialog() {
        new PickFromFragment(getString(R.string.select_image), new PickFromFragment.DialogCallback() {
            @Override
            public void onCencel() {

            }

            @Override
            public void onGallery() {
                if (takePermissionUtils.isStorageCameraPermissionGranted()) {
                    onGalleryButtonClick();
                } else {
                    takePermissionUtils.takeStorageCameraPermission();
                }
            }

            @Override
            public void onCamera() {
                if (takePermissionUtils.isStorageCameraPermissionGranted()) {
                    onCameraButtonClick();
                } else {
                    takePermissionUtils.takeStorageCameraPermission();
                }
            }
        }).show(getSupportFragmentManager(), "");

    }

    public void onColor(int i, String str, int i2) {
        if (i != 0) {
            int childCount = txtStkrRel.getChildCount();
            int i3 = 0;
            if (str.equals("txtShadow")) {
                while (i3 < childCount) {
                    View childAt = txtStkrRel.getChildAt(i3);
                    if (childAt instanceof AutofitTextRel) {
                        ((AutofitTextRel) txtStkrRel.getChildAt(i2)).setBorderVisibility(true);
                        AutofitTextRel autofitTextRel = (AutofitTextRel) childAt;
                        if (autofitTextRel.getBorderVisibility()) {
                            this.shadowColor = i;
                            autofitTextRel.setTextShadowColor(i);
                        }
                    }
                    i3++;
                }
            } else if (str.equals("txtBg")) {
                while (i3 < childCount) {
                    View childAt2 = txtStkrRel.getChildAt(i3);
                    if (childAt2 instanceof AutofitTextRel) {
                        ((AutofitTextRel) txtStkrRel.getChildAt(i2)).setBorderVisibility(true);
                        AutofitTextRel autofitTextRel2 = (AutofitTextRel) childAt2;
                        if (autofitTextRel2.getBorderVisibility()) {
                            this.bgColor = i;
                            this.bgDrawable = "0";
                            autofitTextRel2.setBgColor(i);
                            autofitTextRel2.setBgAlpha(this.seekBar3.getProgress());
                        }
                    }
                    i3++;
                }
            } else {
                View childAt3 = txtStkrRel.getChildAt(i2);
                if (childAt3 instanceof AutofitTextRel) {
                    ((AutofitTextRel) txtStkrRel.getChildAt(i2)).setBorderVisibility(true);
                    AutofitTextRel autofitTextRel3 = (AutofitTextRel) childAt3;
                    if (autofitTextRel3.getBorderVisibility()) {
                        this.tColor = i;
                        this.textColorSet = i;
                        autofitTextRel3.setTextColor(i);
                    }
                }
                if (childAt3 instanceof StickerView) {
                    ((StickerView) txtStkrRel.getChildAt(i2)).setBorderVisibility(true);
                    StickerView stickerView = (StickerView) childAt3;
                    if (stickerView.getBorderVisbilty()) {
                        this.stkrColorSet = i;
                        stickerView.setColor(i);
                    }
                }
            }
        } else {
            if (this.layTextMain.getVisibility() == View.VISIBLE) {
                this.layTextMain.startAnimation(this.animSlideDown);
                this.layTextMain.setVisibility(View.GONE);
            }
            if (this.layStkrMain.getVisibility() == View.VISIBLE) {
                this.layStkrMain.startAnimation(this.animSlideDown);
                this.layStkrMain.setVisibility(View.GONE);
            }
        }
    }


    public void errorDialogTempInfo() {
        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
    }


    public void onDestroy() {
        super.onDestroy();
        freeMemory();
    }

    public void freeMemory() {
        Bitmap bitmap2 = this.bitmap;
        if (bitmap2 != null) {
            bitmap2.recycle();
            this.bitmap = null;
        }
        Bitmap bitmap3 = imgBtmap;
        if (bitmap3 != null) {
            bitmap3.recycle();
            imgBtmap = null;
        }
        Bitmap bitmap4 = withoutWatermark;
        if (bitmap4 != null) {
            bitmap4.recycle();
            withoutWatermark = null;
        }
        Bitmap bitmap5 = btmSticker;
        if (bitmap5 != null) {
            bitmap5.recycle();
            btmSticker = null;
        }
        try {
            new Thread(() -> {
                try {
                    Glide.get(EditorActivity.this).clearDiskCache();
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
            Glide.get(this).clearMemory();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }


    public void onResume() {
        super.onResume();
        if (exoplayer != null) {
            exoplayer.setPlayWhenReady(true);
        }
        if (demoPresenter != null) {
            demoPresenter.onResume();
        }
        if (!musicPath.isEmpty()) {
            playMusic(musicPath);
        }
    }

    private void fackClick() {
        this.layEffects.setOnClickListener(view -> {
        });
        this.layTextedit.setOnClickListener(view -> {
        });
        this.seekbarContainer.setOnClickListener(view -> {
        });

        this.seekLetterSpacing.setOnClickListener(view -> {
        });
        this.seekLineSpacing.setOnClickListener(view -> {
        });
        this.verticalSeekBar.setOnClickListener(view -> {
        });
        this.seekbarShadow.setOnClickListener(view -> {
        });
        this.seekShadowBlur.setOnClickListener(view -> {
        });
        this.seekOutlineSize.setOnClickListener(view -> {
        });
        this.seekBar3.setOnClickListener(view -> {
        });
        this.seek.setOnClickListener(view -> {
        });
        this.borderSeek.setOnClickListener(view -> {
        });
    }

    public void selectControl1() {
        this.txtTextControls.setTextColor(getResources().getColor(R.color.textColor));
        this.txtFontsControl.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtFontsStyle.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtFontsCurve.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtColorsControl.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtShadowControl.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtBgControl.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtOutlineControl.setTextColor(getResources().getColor(R.color.hintColor));
    }

    public void selectControl2() {
        this.txtTextControls.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtFontsControl.setTextColor(getResources().getColor(R.color.textColor));
        this.txtFontsStyle.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtFontsSpacing.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtFontsCurve.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtColorsControl.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtShadowControl.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtBgControl.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtOutlineControl.setTextColor(getResources().getColor(R.color.hintColor));
    }

    public void selectControl3() {
        this.txtTextControls.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtFontsControl.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtFontsStyle.setTextColor(getResources().getColor(R.color.textColor));
        this.txtFontsSpacing.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtFontsCurve.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtColorsControl.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtShadowControl.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtBgControl.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtOutlineControl.setTextColor(getResources().getColor(R.color.hintColor));
    }

    public void selectControl4() {
        this.txtTextControls.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtFontsControl.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtFontsStyle.setTextColor(getResources().getColor(R.color.textColor));
        this.txtFontsSpacing.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtFontsCurve.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtColorsControl.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtShadowControl.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtBgControl.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtOutlineControl.setTextColor(getResources().getColor(R.color.hintColor));
    }

    public void selectControl5() {
        this.txtTextControls.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtFontsControl.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtFontsStyle.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtFontsSpacing.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtFontsCurve.setTextColor(getResources().getColor(R.color.textColor));
        this.txtColorsControl.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtShadowControl.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtBgControl.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtOutlineControl.setTextColor(getResources().getColor(R.color.hintColor));
    }

    public void selectControl6() {
        this.txtTextControls.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtFontsControl.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtFontsStyle.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtFontsSpacing.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtFontsCurve.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtColorsControl.setTextColor(getResources().getColor(R.color.textColor));
        this.txtShadowControl.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtBgControl.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtOutlineControl.setTextColor(getResources().getColor(R.color.hintColor));
    }

    public void selectControl7() {
        this.txtTextControls.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtFontsControl.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtFontsStyle.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtColorsControl.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtFontsCurve.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtFontsSpacing.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtShadowControl.setTextColor(getResources().getColor(R.color.textColor));
        this.txtBgControl.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtOutlineControl.setTextColor(getResources().getColor(R.color.hintColor));
    }

    public void selectControl8() {
        this.txtTextControls.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtFontsControl.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtFontsStyle.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtColorsControl.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtFontsCurve.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtFontsSpacing.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtShadowControl.setTextColor(getResources().getColor(R.color.hintColor));
        this.txtBgControl.setTextColor(getResources().getColor(R.color.textColor));
        this.txtOutlineControl.setTextColor(getResources().getColor(R.color.hintColor));
    }

    @Override
    public void selectedImage(int drawable) {
        setBitmapOverlay(drawable);
    }

    @Override
    public void selectedBorder(int drawable) {
        borderImg.setImageDrawable(getResources().getDrawable(drawable));
    }

    public void saveBitmapUndu() {
        try {
            this.tempID++;
            TemplateInfo templateInfo = new TemplateInfo();
            templateInfo.setTHUMB_URI("");
            templateInfo.setTEMPLATE_ID(this.tempID);
            templateInfo.setFRAME_NAME(this.backgroundPosterPath);
            templateInfo.setRATIO(this.ratio);
            templateInfo.setSEEK_VALUE(String.valueOf(this.seekValue));
            templateInfo.setTYPE("USER");
            templateInfo.setTEMP_PATH(this.tempPath);
            templateInfo.setTEMPCOLOR(this.hex);
            templateInfo.setOVERLAY_NAME(this.overlayName);
            templateInfo.setOVERLAY_OPACITY(this.seek.getProgress());
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            int childCount = txtStkrRel.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = txtStkrRel.getChildAt(i);
                if (childAt instanceof AutofitTextRel) {
                    TextInfo textInfo = ((AutofitTextRel) childAt).getTextInfo();
                    textInfo.setORDER(i);
                    textInfo.setTYPE("TEXT");
                    arrayList.add(textInfo);
                } else {
                    ElementInfo componentInfo = ((StickerView) txtStkrRel.getChildAt(i)).getComponentInfo();
                    componentInfo.setTYPE("STICKER");
                    componentInfo.setORDER(i);
                    arrayList2.add(componentInfo);
                }
            }
            templateInfo.setTextInfoArrayList(arrayList);
            templateInfo.setElementInfoArrayList(arrayList2);
            this.templateListUR.add(templateInfo);
            iconVisibility();
        } catch (Exception e) {
            Log.i("testing", "Exception " + e.getMessage());
            e.printStackTrace();
        } catch (Throwable th) {
        }
    }

    public void loadSaveUnduRedo(TemplateInfo templateInfo) {
        this.backgroundPosterPath = templateInfo.getFRAME_NAME();
        this.tempPath = templateInfo.getTEMP_PATH();
        this.ratio = templateInfo.getRATIO();
        this.tempID = templateInfo.getTEMPLATE_ID();
        String seek_value = templateInfo.getSEEK_VALUE();
        this.hex = templateInfo.getTEMPCOLOR();
        this.overlayName = templateInfo.getOVERLAY_NAME();
        this.overlayOpacty = templateInfo.getOVERLAY_OPACITY();
        this.overlayBlur = templateInfo.getOVERLAY_BLUR();
        this.seekValue = Integer.parseInt(seek_value);
        this.textInfosUR = templateInfo.getTextInfoArrayList();
        this.elementInfos = templateInfo.getElementInfoArrayList();
        this.progressBarUndo.setVisibility(View.VISIBLE);
        this.btnRedo.setVisibility(View.GONE);
        this.btnUndo.setVisibility(View.GONE);
        LoadStickersAsyncUR loadStickersAsyncUR = new LoadStickersAsyncUR();
        loadStickersAsyncUR.execute(String.valueOf(this.tempID));
    }

    public void undo() {
        if (this.layTextMain.getVisibility() == View.VISIBLE) {
            this.layTextMain.setVisibility(View.GONE);
        }
        if (this.layStkrMain.getVisibility() == View.VISIBLE) {
            this.layStkrMain.setVisibility(View.GONE);
        }
        if (this.templateListUR.size() > 2) {
            this.btnUndo.setImageResource(R.drawable.ic_undo_disa);
        } else {
            this.btnUndo.setImageResource(R.drawable.ic_undo);
        }
        if (this.templateListUR.size() > 1) {
            ArrayList<TemplateInfo> arrayList = this.templateListUR;
            loadSaveUnduRedo(arrayList.get(arrayList.size() - 2));
            ArrayList<TemplateInfo> arrayList2 = this.templateListRU;
            ArrayList<TemplateInfo> arrayList3 = this.templateListUR;
            arrayList2.add(arrayList3.get(arrayList3.size() - 1));
            ArrayList<TemplateInfo> arrayList4 = this.templateListUR;
            arrayList4.remove(arrayList4.get(arrayList4.size() - 1));
        }
        iconVisibility();
    }

    public void redo() {
        if (this.layTextMain.getVisibility() == View.VISIBLE) {
            this.layTextMain.setVisibility(View.GONE);
        }
        if (this.layStkrMain.getVisibility() == View.VISIBLE) {
            this.layStkrMain.setVisibility(View.GONE);
        }
        if (this.templateListRU.size() > 1) {
            this.btnRedo.setImageResource(R.drawable.ic_redo_disa);
        } else {
            this.btnRedo.setImageResource(R.drawable.ic_redo);
        }
        if (this.templateListRU.size() > 0) {
            ArrayList<TemplateInfo> arrayList = this.templateListRU;
            loadSaveUnduRedo(arrayList.get(arrayList.size() - 1));
            ArrayList<TemplateInfo> arrayList2 = this.templateListUR;
            ArrayList<TemplateInfo> arrayList3 = this.templateListRU;
            arrayList2.add(arrayList3.get(arrayList3.size() - 1));
            ArrayList<TemplateInfo> arrayList4 = this.templateListRU;
            arrayList4.remove(arrayList4.get(arrayList4.size() - 1));
        }
        iconVisibility();
    }

    public void iconVisibility() {
        if (this.templateListUR.size() > 1) {
            this.btnUndo.setImageResource(R.drawable.ic_undo);
        } else {
            this.btnUndo.setImageResource(R.drawable.ic_undo_disa);
        }
        if (this.templateListRU.size() > 0) {
            this.btnRedo.setImageResource(R.drawable.ic_redo);
        } else {
            this.btnRedo.setImageResource(R.drawable.ic_redo_disa);
        }
    }

    private void onPersonalFrameCLick(FrameModel model) {
        EditorActivity.txtStkrRel.removeAllViews();
        frameImage.setVisibility(View.VISIBLE);
        Glide.with(context)
                .asBitmap()
                .load(Functions.getItemBaseUrl(model.getThumbnail()))
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        BitmapDrawable background = new BitmapDrawable(resource);
                        frameImage.setBackgroundDrawable(background);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    private void onFrameClick(FrameModel model) {
        frameImage.setVisibility(View.GONE);
        FrameUtils frameUtils = new FrameUtils(context);
        frameUtils.proccessFrame(context, model, new FrameUtils.OnFrameStatus() {
            @Override
            public void onFrameLoaded(ArrayList<Sticker_info> stickerInfos, ArrayList<textInfo> textInfos) {
                Functions.showLoader(context);
                stickerInfoArrayList = stickerInfos;
                textInfoArrayList = textInfos;
//
//                int childCount = txtStkrRel.getChildCount();
//                for (int i = 0; i < childCount; i++) {
//                    View childAt = txtStkrRel.getChildAt(i);
//                    if ((childAt instanceof StickerView)) {
//                        if (((StickerView) childAt).isFrameItem()) {
//                            ((StickerView) childAt).deleteView();
//                        }
//                    }
//                    if ((childAt instanceof AutofitTextRel)) {
//                        if (((AutofitTextRel) childAt).isFrameItem()) {
//                            ((AutofitTextRel) childAt).deleteView();
//                        }
//                    }
//                }

                LoadFrameAsync loadFrameAsync = new LoadFrameAsync();
                loadFrameAsync.execute();

                if (!Functions.IsPremiumEnable(context) && postsModel != null && !postsModel.premium.equals("1")) {
                    if (model.getPremium().equals("1")) {
                        findViewById(R.id.watermark).setVisibility(View.VISIBLE);
                    } else {
                        findViewById(R.id.watermark).setVisibility(View.GONE);
                    }
                } else if (postsModel == null) {
                    if (model.getPremium().equals("1")) {
                        findViewById(R.id.watermark).setVisibility(View.VISIBLE);
                    } else {
                        findViewById(R.id.watermark).setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    public Bitmap gaussinBlur(Activity activity2, Bitmap bitmap2) {
        try {
            GPUImage gPUImage = new GPUImage(activity2);
            GPUImageGaussianBlurFilter gPUImageGaussianBlurFilter = new GPUImageGaussianBlurFilter();
            gPUImage.setFilter(gPUImageGaussianBlurFilter);
//            new FilterAdjuster(gPUImageGaussianBlurFilter).adjust(150);
            gPUImage.requestRender();
            return gPUImage.getBitmapWithFilterApplied(bitmap2);
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    private class LoadFrameAsync extends AsyncTask<String, String, Boolean> {

        private LoadFrameAsync() {
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
        }


        public Boolean doInBackground(String... strArr) {

            ArrayList<ElementInfo> stickerArrayList;
            ArrayList<TextInfo> textInfoArrayList;
            String str;

            textInfoArrayList = new ArrayList<>();
            stickerArrayList = new ArrayList<>();

            for (int i = 0; i < stickerInfoArrayList.size(); i++) {
                EditorActivity EditorActivity2 = EditorActivity.this;
                int newWidht = EditorActivity2.getNewWidht(Float.valueOf(EditorActivity2.stickerInfoArrayList.get(i).getSt_x_pos()).floatValue(), Float.valueOf(EditorActivity.this.stickerInfoArrayList.get(i).getSt_width()).floatValue());
                int newHeight = EditorActivity2.getNewHeight(Float.valueOf(EditorActivity2.stickerInfoArrayList.get(i).getSt_y_pos()).floatValue(), Float.valueOf(EditorActivity.this.stickerInfoArrayList.get(i).getSt_height()).floatValue());
                int i2 = newWidht;//< 10 ? 20 : (newWidht <= 10 || newWidht > 20) ? newWidht : 35;
                int i3 = newHeight;//< 10 ? 20 : (newHeight <= 10 || newHeight > 20) ? newHeight : 35;
                if (EditorActivity.this.stickerInfoArrayList.get(i).getSt_field2() != null) {
                    str = EditorActivity.this.stickerInfoArrayList.get(i).getSt_field2();
                } else {
                    str = "";
                }

                float parseInt = (stickerInfoArrayList.get(i).getSt_rotation() == null ||
                        stickerInfoArrayList.get(i).getSt_rotation().equals("")) ? 0.0f :
                        (float) Integer.parseInt(stickerInfoArrayList.get(i).getSt_rotation());

                float xpos = getXpos(Float.valueOf(stickerInfoArrayList.get(i).getSt_x_pos()).floatValue());
                float ypos = getYpos(Float.valueOf(stickerInfoArrayList.get(i).getSt_y_pos()).floatValue());

                stickerArrayList.add(new ElementInfo(stickerInfoArrayList.get(i).getName(), xpos, ypos, i2, i3, parseInt, 0.0f, "", "STICKER", Integer.parseInt(stickerInfoArrayList.get(i).getSt_order()), 0, 255, 0, 0, 0, 0, stickerInfoArrayList.get(i).getSt_image(), "colored", 1, 0, str, "", "", null, null));
            }
            for (int i5 = 0; i5 < EditorActivity.this.textInfoArrayList.size(); i5++) {

                String text = EditorActivity.this.textInfoArrayList.get(i5).getText();
                String font_family = EditorActivity.this.textInfoArrayList.get(i5).getFont_family();
                String justification = EditorActivity.this.textInfoArrayList.get(i5).getTxt_justification();
                String weight = EditorActivity.this.textInfoArrayList.get(i5).getTxt_weight();

                int parseColor = Color.parseColor(EditorActivity.this.textInfoArrayList.get(i5).getTxt_color());
                EditorActivity EditorActivity5 = EditorActivity.this;
                float xpos2 = EditorActivity5.getXpos(Float.valueOf(EditorActivity5.textInfoArrayList.get(i5).getTxt_x_pos()).floatValue());
                EditorActivity EditorActivity6 = EditorActivity.this;
                float ypos = EditorActivity6.getYpos(Float.valueOf(EditorActivity6.textInfoArrayList.get(i5).getTxt_y_pos()).floatValue());
                EditorActivity EditorActivity7 = EditorActivity.this;
                int newWidht2 = EditorActivity7.getNewWidht(Float.valueOf(EditorActivity7.textInfoArrayList.get(i5).getTxt_x_pos()).floatValue(), Float.valueOf(EditorActivity.this.textInfoArrayList.get(i5).getTxt_width()).floatValue());
                int newHeit2 = EditorActivity7.getNewHeightText(Float.valueOf(EditorActivity7.textInfoArrayList.get(i5).getTxt_y_pos()).floatValue(),
                        Float.valueOf(EditorActivity7.textInfoArrayList.get(i5).getTxt_height()).floatValue());
                textInfoArrayList.add(new TextInfo(text, font_family, parseColor, 100, ViewCompat.MEASURED_STATE_MASK, 0, "0", ViewCompat.MEASURED_STATE_MASK, 0, xpos2, ypos, newWidht2, newHeit2, Float.parseFloat(EditorActivity.this.textInfoArrayList.get(i5).getTxt_rotation()), "TEXT", Integer.parseInt(EditorActivity.this.textInfoArrayList.get(i5).getTxt_order()), 0, 0, 0, 0, 0, "", "", "", 0.0f, 0.0f, 0, 0, weight, justification));
            }

            txtShapeList = new HashMap<>();
            Iterator<TextInfo> it = textInfoArrayList.iterator();
            while (it.hasNext()) {
                TextInfo next = it.next();
                txtShapeList.put(Integer.valueOf(next.getORDER()), next);
            }
            Iterator<ElementInfo> it2 = stickerArrayList.iterator();
            while (it2.hasNext()) {
                ElementInfo next2 = it2.next();
                txtShapeList.put(Integer.valueOf(next2.getORDER()), next2);
            }
            return true;
        }

        @Override
        public void onPostExecute(Boolean bool) {
            super.onPostExecute(bool);
            Functions.cancelLoader();
            EditorActivity.txtStkrRel.removeAllViews();
            ArrayList arrayList = new ArrayList(txtShapeList.keySet());
            Collections.sort(arrayList);
            int size = arrayList.size();

            for (int i = 0; i < size; i++) {
                Object obj = txtShapeList.get(arrayList.get(i));
                if (obj instanceof ElementInfo) {
                    ElementInfo elementInfo = (ElementInfo) obj;

                    StickerView stickerView = new StickerView(EditorActivity.this);
                    EditorActivity.txtStkrRel.addView(stickerView);
                    stickerView.optimizeScreen(screenWidth, screenHeight);
                    stickerView.setViewWH((float) mainRel.getWidth(), (float) mainRel.getHeight());
                    stickerView.setComponentInfo(elementInfo);
                    stickerView.setFrameItem(true);
                    stickerView.setId(ViewIdGenerator.generateViewId());
                    stickerView.optimize(wr, hr);
                    if (elementInfo.getNAME() != null && elementInfo.getNAME().equals("logo")) {
                        stickerView.setDefaultTouchListener(true);
                    }
                    stickerView.setOnTouchCallbackListener(EditorActivity.this);
                    stickerView.setBorderVisibility(false);
                } else {
                    AutofitTextRel autofitTextRel = new AutofitTextRel(EditorActivity.this);
                    EditorActivity.txtStkrRel.addView(autofitTextRel);
                    TextInfo textInfo = (TextInfo) obj;

                    autofitTextRel.setTextInfo(textInfo, false);
                    autofitTextRel.setFrameItem(true);
                    autofitTextRel.setId(ViewIdGenerator.generateViewId());
                    autofitTextRel.optimize(wr, hr);
                    autofitTextRel.setOnTouchCallbackListener(EditorActivity.this);
                    autofitTextRel.setBorderVisibility(false);

                    try {
//                        if (textInfo.getFONT_WEIGHT().equals("bold")){
//                            autofitTextRel.setBoldFont();
//                        }
                        if (textInfo.getFONT_JUSTIFY().equals("left")) {
                            autofitTextRel.setLeftAlignMent();
                        }
                        if (textInfo.getFONT_JUSTIFY().equals("center")) {
                            autofitTextRel.setCenterAlignMent();
                        }
                        if (textInfo.getFONT_JUSTIFY().equals("right")) {
                            autofitTextRel.setRightAlignMent();
                        }
//                        Log.d("onFrameLoaded__","EDITOR _> "+textInfo.getFONT_WEIGHT());
//                        Log.d("onFrameLoaded__","EDITOR _> "+textInfo.getFONT_JUSTIFY());
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("onFrameLoaded__", "EDITOR _> " + e.getMessage());
                    }

                    if (textInfo.getFONT_NAME() != null) {
                        setTextFonts(textInfo.getFONT_NAME());
                    }

                    fontName = textInfo.getFONT_NAME();
                    tColor = textInfo.getTEXT_COLOR();
                    shadowColor = textInfo.getSHADOW_COLOR();
                    shadowProg = textInfo.getSHADOW_PROG();
                    tAlpha = textInfo.getTEXT_ALPHA();
                    bgDrawable = textInfo.getBG_DRAWABLE();
                    bgAlpha = textInfo.getBG_ALPHA();
                    rotation = textInfo.getROTATION();
                    bgColor = textInfo.getBG_COLOR();
                    outerColor = textInfo.getOutLineColor();
                    outerSize = textInfo.getOutLineSize();
                    leftRightShadow = (int) textInfo.getLeftRighShadow();
                    topBottomShadow = (int) textInfo.getTopBottomShadow();
                    topBottomShadow = (int) textInfo.getTopBottomShadow();
                }
                sizeFull++;
            }

            if (!overlayName.equals("")) {
                EditorActivity EditorActivity6 = EditorActivity.this;
                EditorActivity6.setBitmapOverlay(getResources().getIdentifier(overlayName, "drawable", getPackageName()));
            }
            saveBitmapUndu();
        }
    }

    public class BlurOperationTwoAsync extends AsyncTask<String, Void, String> {
        ImageView background_blur;
        Bitmap btmp;
        Activity context;
        int isTamplate = 0;

        public BlurOperationTwoAsync(EditorActivity thumbnailActivity, Bitmap bitmap, ImageView imageView, int isTamplate) {
            this.context = thumbnailActivity;
            this.btmp = bitmap;
            this.background_blur = imageView;
            this.isTamplate = isTamplate;
        }

        public String doInBackground(String... strArr) {
            this.btmp = gaussinBlur(this.context, this.btmp);
            return "yes";
        }

        @Override
        public void onPostExecute(String str) {
            if (!(txtStkrRel.getChildCount() > 0)) {
                if (this.isTamplate != 0) {
                    txtStkrRel.removeAllViews();
                    LoadStickersAsync loadStickersAsync2 = new LoadStickersAsync();
                    loadStickersAsync2.execute();
                } else {
                    Functions.cancelLoader();
                }
            } else {
                Functions.cancelLoader();
            }

        }
    }

    private class LoadStickersAsync extends AsyncTask<String, String, Boolean> {

        private LoadStickersAsync() {
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
        }


        public Boolean doInBackground(String... strArr) {
            ArrayList<ElementInfo> stickerArrayList;
            ArrayList<TextInfo> textInfoArrayList;
            String str;

            textInfoArrayList = new ArrayList<>();
            stickerArrayList = new ArrayList<>();

            for (int i = 0; i < EditorActivity.this.stickerInfoArrayList.size(); i++) {
                EditorActivity EditorActivity = EditorActivity.this;

                Log.d("loadStickersBG2___", "x -> " + Float.valueOf(EditorActivity.stickerInfoArrayList.get(i).getSt_x_pos()).floatValue());
                Log.d("loadStickersBG2___", "y -> " + Float.valueOf(EditorActivity.stickerInfoArrayList.get(i).getSt_y_pos()).floatValue());

                int newWidht = EditorActivity.getNewWidht(Float.valueOf(EditorActivity.stickerInfoArrayList.get(i).getSt_x_pos()).floatValue(), Float.valueOf(EditorActivity.this.stickerInfoArrayList.get(i).getSt_width()).floatValue());
                EditorActivity EditorActivity2 = EditorActivity.this;
                int newHeight = EditorActivity2.getNewHeight(Float.valueOf(EditorActivity2.stickerInfoArrayList.get(i).getSt_y_pos()).floatValue(), Float.valueOf(EditorActivity.this.stickerInfoArrayList.get(i).getSt_height()).floatValue());
                int i2 = newWidht;//< 10 ? 20 : (newWidht <= 10 || newWidht > 20) ? newWidht : 35;
                int i3 = newHeight;//< 10 ? 20 : (newHeight <= 10 || newHeight > 20) ? newHeight : 35;
                if (EditorActivity.this.stickerInfoArrayList.get(i).getSt_field2() != null) {
                    str = EditorActivity.this.stickerInfoArrayList.get(i).getSt_field2();
                } else {
                    str = "";
                }

                float parseInt = (stickerInfoArrayList.get(i).getSt_rotation() == null ||
                        stickerInfoArrayList.get(i).getSt_rotation().equals("")) ? 0.0f :
                        (float) Integer.parseInt(stickerInfoArrayList.get(i).getSt_rotation());

                float xpos = getXpos(Float.valueOf(stickerInfoArrayList.get(i).getSt_x_pos()).floatValue());
                float ypos = getYpos(Float.valueOf(stickerInfoArrayList.get(i).getSt_y_pos()).floatValue());

                stickerArrayList.add(new ElementInfo(stickerInfoArrayList.get(i).getName(), xpos, ypos, i2, i3, parseInt, 0.0f, "", "STICKER", Integer.parseInt(stickerInfoArrayList.get(i).getSt_order()), 0, 255, 0, 0, 0, 0, stickerInfoArrayList.get(i).getSt_image(), "colored", 1, 0, str, "", "", null, null));
            }
            for (int i5 = 0; i5 < EditorActivity.this.textInfoArrayList.size(); i5++) {

                String text = EditorActivity.this.textInfoArrayList.get(i5).getText();
                String font_family = EditorActivity.this.textInfoArrayList.get(i5).getFont_family();
                String justification = EditorActivity.this.textInfoArrayList.get(i5).getTxt_justification();
                String weight = EditorActivity.this.textInfoArrayList.get(i5).getTxt_weight();

                int parseColor = Color.parseColor(EditorActivity.this.textInfoArrayList.get(i5).getTxt_color());
                EditorActivity EditorActivity5 = EditorActivity.this;
                float xpos2 = EditorActivity5.getXpos(Float.valueOf(EditorActivity5.textInfoArrayList.get(i5).getTxt_x_pos()).floatValue());
                EditorActivity EditorActivity6 = EditorActivity.this;
                float ypos = EditorActivity6.getYpos(Float.valueOf(EditorActivity6.textInfoArrayList.get(i5).getTxt_y_pos()).floatValue());
                EditorActivity EditorActivity7 = EditorActivity.this;
                int newWidht2 = EditorActivity7.getNewWidht(Float.valueOf(EditorActivity7.textInfoArrayList.get(i5).getTxt_x_pos()).floatValue(), Float.valueOf(EditorActivity.this.textInfoArrayList.get(i5).getTxt_width()).floatValue());
                int newHeit2 = EditorActivity7.getNewHeight(Float.valueOf(EditorActivity7.textInfoArrayList.get(i5).getTxt_y_pos()).floatValue(),
                        Float.valueOf(EditorActivity7.textInfoArrayList.get(i5).getTxt_height()).floatValue());
                textInfoArrayList.add(new TextInfo(text, font_family, parseColor, 100, ViewCompat.MEASURED_STATE_MASK, 0, "0", ViewCompat.MEASURED_STATE_MASK, 0, xpos2, ypos, newWidht2, newHeit2, Float.parseFloat(EditorActivity.this.textInfoArrayList.get(i5).getTxt_rotation()), "TEXT", Integer.parseInt(EditorActivity.this.textInfoArrayList.get(i5).getTxt_order()), 0, 0, 0, 0, 0, "", "", "", 0.0f, 0.0f, 0, 0, weight, justification));
            }


            EditorActivity.this.txtShapeList = new HashMap<>();
            Iterator<TextInfo> it = textInfoArrayList.iterator();
            while (it.hasNext()) {
                TextInfo next = it.next();
                EditorActivity.this.txtShapeList.put(Integer.valueOf(next.getORDER()), next);
            }
            Iterator<ElementInfo> it2 = stickerArrayList.iterator();
            while (it2.hasNext()) {
                ElementInfo next2 = it2.next();
                EditorActivity.this.txtShapeList.put(Integer.valueOf(next2.getORDER()), next2);
            }
            return true;
        }

        @Override
        public void onPostExecute(Boolean bool) {
            super.onPostExecute(bool);
            Functions.cancelLoader();
            ArrayList arrayList = new ArrayList(EditorActivity.this.txtShapeList.keySet());
            Collections.sort(arrayList);
            int size = arrayList.size();


            for (int i = 0; i < size; i++) {
                Object obj = EditorActivity.this.txtShapeList.get(arrayList.get(i));
                if (obj instanceof ElementInfo) {
                    ElementInfo elementInfo = (ElementInfo) obj;

                    StickerView stickerView = new StickerView(EditorActivity.this);
                    EditorActivity.txtStkrRel.addView(stickerView);
                    stickerView.optimizeScreen(EditorActivity.this.screenWidth, EditorActivity.this.screenHeight);
                    stickerView.setViewWH((float) EditorActivity.this.mainRel.getWidth(), (float) EditorActivity.this.mainRel.getHeight());
                    stickerView.setComponentInfo(elementInfo);
                    stickerView.setId(ViewIdGenerator.generateViewId());
                    stickerView.optimize(EditorActivity.this.wr, EditorActivity.this.hr);
                    stickerView.setOnTouchCallbackListener(EditorActivity.this);
                    stickerView.setBorderVisibility(false);
                    EditorActivity.this.sizeFull++;
                } else {


                    AutofitTextRel autofitTextRel = new AutofitTextRel(EditorActivity.this);
                    txtStkrRel.addView(autofitTextRel);
                    TextInfo textInfo = (TextInfo) obj;


                    autofitTextRel.setTextInfo(textInfo, false);
                    autofitTextRel.setId(ViewIdGenerator.generateViewId());
                    autofitTextRel.optimize(EditorActivity.this.wr, EditorActivity.this.hr);
                    autofitTextRel.setOnTouchCallbackListener(EditorActivity.this);
                    autofitTextRel.setBorderVisibility(false);
                    try {
//                        if (textInfo.getFONT_WEIGHT().equals("bold")){
//                            autofitTextRel.setBoldFont();
//                        }
                        if (textInfo.getFONT_JUSTIFY().equals("left")) {
                            autofitTextRel.setLeftAlignMent();
                        }
                        if (textInfo.getFONT_JUSTIFY().equals("center")) {
                            autofitTextRel.setCenterAlignMent();
                        }
                        if (textInfo.getFONT_JUSTIFY().equals("right")) {
                            autofitTextRel.setRightAlignMent();
                        }
                        Log.d("onFrameLoaded__", "EDITOR _> " + textInfo.getFONT_WEIGHT());
                        Log.d("onFrameLoaded__", "EDITOR _> " + textInfo.getFONT_JUSTIFY());
                    } catch (Exception e) {
                        Log.d("onFrameLoaded__", "EDITOR _> " + e.getMessage());
                    }

                    if (textInfo.getFONT_NAME() != null) {
                        setTextFonts(textInfo.getFONT_NAME());
                    }

                    EditorActivity.this.fontName = textInfo.getFONT_NAME();
                    EditorActivity.this.tColor = textInfo.getTEXT_COLOR();
                    EditorActivity.this.shadowColor = textInfo.getSHADOW_COLOR();
                    EditorActivity.this.shadowProg = textInfo.getSHADOW_PROG();
                    EditorActivity.this.tAlpha = textInfo.getTEXT_ALPHA();
                    EditorActivity.this.bgDrawable = textInfo.getBG_DRAWABLE();
                    EditorActivity.this.bgAlpha = textInfo.getBG_ALPHA();
                    EditorActivity.this.rotation = textInfo.getROTATION();
                    EditorActivity.this.bgColor = textInfo.getBG_COLOR();
                    EditorActivity.this.outerColor = textInfo.getOutLineColor();
                    EditorActivity.this.outerSize = textInfo.getOutLineSize();
                    EditorActivity.this.leftRightShadow = (int) textInfo.getLeftRighShadow();
                    EditorActivity.this.topBottomShadow = (int) textInfo.getTopBottomShadow();
                    EditorActivity.this.topBottomShadow = (int) textInfo.getTopBottomShadow();
                    EditorActivity.this.sizeFull++;
                }
            }
//            if (EditorActivity.this.txtShapeList.size() == EditorActivity.this.sizeFull && EditorActivity.this.dialogShow) {
//                try {
//                    EditorActivity.this.dialogIs.dismiss();
//                } catch (IllegalArgumentException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (!EditorActivity.this.overlayName.equals("")) {
//                EditorActivity EditorActivity6 = EditorActivity.this;
//                EditorActivity6.setBitmapOverlay(getResources().getIdentifier(EditorActivity.this.overlayName, "drawable", EditorActivity.this.getPackageName()));
//            }
            EditorActivity.this.saveBitmapUndu();
        }
    }

    private class LoadStickersAsyncUR extends AsyncTask<String, String, Boolean> {

        public Boolean doInBackground(String... strArr) {
            txtShapeList = new HashMap<>();
            Iterator<TextInfo> it = textInfosUR.iterator();
            while (it.hasNext()) {
                TextInfo next = it.next();
                txtShapeList.put(Integer.valueOf(next.getORDER()), next);
            }
            Iterator<ElementInfo> it2 = elementInfos.iterator();
            while (it2.hasNext()) {
                ElementInfo next2 = it2.next();
                txtShapeList.put(Integer.valueOf(next2.getORDER()), next2);
            }
            return true;
        }

        @Override
        public void onPostExecute(Boolean bool) {
            super.onPostExecute(bool);
            EditorActivity.txtStkrRel.removeAllViews();
            Functions.cancelLoader();
            ArrayList arrayList = new ArrayList(txtShapeList.keySet());
            Collections.sort(arrayList);
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                Object obj = txtShapeList.get(arrayList.get(i));
                if (obj instanceof ElementInfo) {
                    ElementInfo elementInfo = (ElementInfo) obj;
                    String stkrPath = elementInfo.getSTKR_PATH();
                    if (stkrPath.equals("")) {
                        StickerView stickerView = new StickerView(EditorActivity.this, true);
                        EditorActivity.txtStkrRel.addView(stickerView);
                        stickerView.optimizeScreen(screenWidth, screenHeight);
                        stickerView.setViewWH((float) mainRel.getWidth(), (float) mainRel.getHeight());
                        stickerView.setComponentInfo(elementInfo);
                        stickerView.setId(ViewIdGenerator.generateViewId());
                        stickerView.optimize(wr, hr);
                        stickerView.setOnTouchCallbackListener(EditorActivity.this);
                        stickerView.setBorderVisibility(false);
                        sizeFull++;
                    } else {
                        File file = new StorageUtils(context).getPackageStorageDir(getString(R.string.invitaition_directory));

                        if (!file.exists() && !file.mkdirs()) {
                            Log.d("", "Can't create directory to save image.");
                            EditorActivity EditorActivity = EditorActivity.this;
                            Toast.makeText(EditorActivity, EditorActivity.getResources().getString(R.string.create_dir_err), Toast.LENGTH_SHORT).show();
                            return;
                        } else if (new StorageUtils(context).getPackageStorageDir(getString(R.string.invitaition_directory)).exists()) {
                            File file2 = new File(stkrPath);
                            if (file2.exists()) {
                                StickerView stickerView2 = new StickerView(EditorActivity.this, true);
                                EditorActivity.txtStkrRel.addView(stickerView2);
                                stickerView2.optimizeScreen(screenWidth, screenHeight);
                                stickerView2.setViewWH((float) mainRel.getWidth(), (float) mainRel.getHeight());
                                stickerView2.setComponentInfo(elementInfo);
                                stickerView2.setId(ViewIdGenerator.generateViewId());
                                stickerView2.optimize(wr, hr);
                                stickerView2.setOnTouchCallbackListener(EditorActivity.this);
                                stickerView2.setBorderVisibility(false);
                                sizeFull++;
                            } else if (file2.getName().replace(".png", "").length() < 7) {
                                dialogShow = false;
                            } else {
                                if (OneShow) {
                                    EditorActivity EditorActivity2 = EditorActivity.this;
                                    EditorActivity2.dialogShow = true;
                                    EditorActivity2.errorDialogTempInfo();
                                    OneShow = false;
                                }
                                sizeFull++;
                            }
                        } else {
                            File file3 = new File(stkrPath);
                            if (file3.exists()) {
                                StickerView stickerView3 = new StickerView(EditorActivity.this, true);
                                EditorActivity.txtStkrRel.addView(stickerView3);
                                stickerView3.optimizeScreen(screenWidth, screenHeight);
                                stickerView3.setViewWH((float) mainRel.getWidth(), (float) mainRel.getHeight());
                                stickerView3.setComponentInfo(elementInfo);
                                stickerView3.setId(ViewIdGenerator.generateViewId());
                                stickerView3.optimize(wr, hr);
                                stickerView3.setOnTouchCallbackListener(EditorActivity.this);
                                stickerView3.setBorderVisibility(false);
                                sizeFull++;
                            } else if (file3.getName().replace(".png", "").length() < 7) {
                                dialogShow = false;
                            } else {
                                if (OneShow) {
                                    EditorActivity EditorActivity3 = EditorActivity.this;
                                    EditorActivity3.dialogShow = true;
                                    EditorActivity3.errorDialogTempInfo();
                                    OneShow = false;
                                }
                                sizeFull++;
                            }
                        }
                    }
                } else {

                    AutofitTextRel autofitTextRel = new AutofitTextRel(EditorActivity.this, true);
                    EditorActivity.txtStkrRel.addView(autofitTextRel);
                    TextInfo textInfo = (TextInfo) obj;
                    autofitTextRel.setTextInfo(textInfo, false);
                    autofitTextRel.setId(ViewIdGenerator.generateViewId());
                    autofitTextRel.optimize(wr, hr);
                    autofitTextRel.setOnTouchCallbackListener(EditorActivity.this);
                    autofitTextRel.setBorderVisibility(false);
                    fontName = textInfo.getFONT_NAME();
                    tColor = textInfo.getTEXT_COLOR();
                    shadowColor = textInfo.getSHADOW_COLOR();
                    shadowProg = textInfo.getSHADOW_PROG();
                    tAlpha = textInfo.getTEXT_ALPHA();
                    bgDrawable = textInfo.getBG_DRAWABLE();
                    bgAlpha = textInfo.getBG_ALPHA();
                    rotation = textInfo.getROTATION();
                    bgColor = textInfo.getBG_COLOR();
                    sizeFull++;
                }
            }
            progressBarUndo.setVisibility(View.GONE);
            btnRedo.setVisibility(View.VISIBLE);
            btnUndo.setVisibility(View.VISIBLE);
        }

    }
}
