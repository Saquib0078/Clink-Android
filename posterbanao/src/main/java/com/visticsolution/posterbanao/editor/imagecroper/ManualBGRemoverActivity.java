package com.visticsolution.posterbanao.editor.imagecroper;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.ItemTouchHelper;

import com.visticsolution.posterbanao.R;
import com.visticsolution.posterbanao.dialog.PickedImageActionFragment;
import com.visticsolution.posterbanao.editor.imagecroper.views.BrushView;
import com.visticsolution.posterbanao.editor.imagecroper.views.TouchImageView;
import com.visticsolution.posterbanao.editor.utils.MyUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Vector;

public class ManualBGRemoverActivity extends Activity {

    public static Bitmap highResolutionOutput;
    private final ArrayList<Path> paths = new ArrayList<>();
    private final ArrayList<Path> redoPaths = new ArrayList<>();
    private final ArrayList<Vector<Point>> redoTargetPointsArray = new ArrayList<>();
    private final ArrayList<Vector<Point>> targetPointsArray = new ArrayList<>();
    float targetOffset;
    boolean isPanning = false;
    boolean softEdge = false;
    float brushsize = 70.0f;
    int drawingmode = 1;
    int initialdrawingcount;
    int initialdrawingcountlimit = 20;
    boolean ismultipletoucherasing;
    int mode = 0;
    int offset = ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION;
    int tolerance = 50;
    int undolimit = 10;
    int updatedbrushsize;
    LinearLayout bgButtonsContainer;
    Bitmap bitmapMaster;
    ImageButton blackButton;
    LinearLayout bottomBar;
    LinearLayout bottomBar1;
    BrushView brush;
    Vector<Integer> brushSizes = new Vector<>();
    Canvas canvasMaster;
    float currentx = 0.0f;
    float currenty = 0.0f;
    int density;
    TouchImageView drawingImageView;
    ImageButton eraseBtn;
    Vector<Integer> erasing = new Vector<>();
    ImageButton fitBtn;
    RelativeLayout imageViewContainer;
    int imageViewHeight;
    int imageViewWidth;
    boolean isAsyncExecuteForThresholdChange;
    boolean isBitmapUpdated;
    boolean isImageResized;
    boolean isTouchOnBitmap;
    ImageButton lassoBtn;
    int lassoStartX;
    int lassoStartY;
    Bitmap lastEiditedBitmap;
    LinearLayout mainLayout;
    Point mainViewSize;
    SeekBar offsetSeekBar = null;
    Bitmap originalBitmap;
//    String orignal;
    ImageButton reDrawBtn;
    Vector<Integer> redoBrushSizes = new Vector<>();
    ImageButton redoBtn;
    Vector<Integer> redoErasing = new Vector<>();
    ImageButton resetBtn;
    Bitmap resizedBitmap;
    TextView shareBtn;
    ImageButton softedgeButton;
    ImageButton targetAreaBtn;
    Vector<Point> targetPoints;
    int targetValueX;
    int targetValueY;
    LinearLayout thresholdContainer;
    SeekBar thresholdSeekBar = null;
    LinearLayout topBar;
    ImageButton transButton;
    ImageButton undoBtn;
    boolean wasImageSaved;
    ImageButton whitebutton;
    LinearLayout widthContainer;
    SeekBar widthSeekBar = null;
    ImageButton zoomAndPanBtn;
    private Path drawingPath;
    private ProgressBar spinner;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_manual_bg_remover);
        getWindow().setFlags(1024, 1024);
        allocatrVariable();


        ProgressBar progressBar = findViewById(R.id.progressBar);
        this.spinner = progressBar;
        progressBar.setVisibility(View.INVISIBLE);
        this.mainLayout = findViewById(R.id.mainLayOut);
        this.widthContainer = findViewById(R.id.widthcontainer);
        LinearLayout linearLayout = findViewById(R.id.thresholdcontainer);
        this.thresholdContainer = linearLayout;
        linearLayout.setVisibility(View.INVISIBLE);
        this.bgButtonsContainer = findViewById(R.id.bg_buttons);
        this.drawingImageView = findViewById(R.id.drawingImageView);
        this.brush = findViewById(R.id.brushContainingView);
        this.topBar = findViewById(R.id.topBar);
        this.bottomBar = findViewById(R.id.bottomBar);
        this.bottomBar1 = findViewById(R.id.bottomBar1);
        this.imageViewContainer = findViewById(R.id.imageViewContainer);
        this.fitBtn = findViewById(R.id.fitBtn);
        this.resetBtn = findViewById(R.id.resetBtn);
        this.shareBtn = findViewById(R.id.shareBtn);
        this.undoBtn = findViewById(R.id.undoBtn);
        this.redoBtn = findViewById(R.id.redoBtn);
        this.lassoBtn = findViewById(R.id.lassoBtn);
        this.eraseBtn = findViewById(R.id.eraseBtn);
        this.reDrawBtn = findViewById(R.id.restoreBtn);
        this.targetAreaBtn = findViewById(R.id.targetAreaBtn);
        this.softedgeButton = findViewById(R.id.softEdge);
        this.zoomAndPanBtn = findViewById(R.id.zoomBtn);
        this.transButton = findViewById(R.id.trans_button);
        this.whitebutton = findViewById(R.id.white_button);
        this.blackButton = findViewById(R.id.black_button);
        this.offsetSeekBar = findViewById(R.id.offsetSeekBar);
        this.widthSeekBar = findViewById(R.id.widthSeekBar);
        this.thresholdSeekBar = findViewById(R.id.thresholdSeekBar);


        this.transButton.setOnClickListener(view -> ManualBGRemoverActivity.this.imageViewContainer.setBackgroundResource(R.drawable.pattern));
        this.blackButton.setOnClickListener(view -> ManualBGRemoverActivity.this.imageViewContainer.setBackgroundResource(R.drawable.pattern_black));
        this.whitebutton.setOnClickListener(view -> ManualBGRemoverActivity.this.imageViewContainer.setBackgroundResource(R.drawable.pattern_white));
        setUiSize();
        setOnClickActionsMethods();

        try {
            if (PickedImageActionFragment.path == null){
                this.originalBitmap = PickedImageActionFragment.bitmap;
            }else{
                this.originalBitmap = BitmapFactory.decodeFile(PickedImageActionFragment.path);
            }

        } catch (Exception e) {
            Log.d("ERASER__",e.getMessage());
            finish();
        }

        this.wasImageSaved = true;
        setBitMap();
        updateBrush((float) (this.mainViewSize.x / 2), (float) (this.mainViewSize.y / 2));


        this.drawingImageView.setOnTouchListener(new MainTouchListner());

        this.widthSeekBar.setMax(150);
        this.widthSeekBar.setProgress((int) (this.brushsize - 20.0f));
        this.widthSeekBar.setOnSeekBarChangeListener(new BrushSizeChangeListner());
        this.offsetSeekBar.setMax(350);
        this.offsetSeekBar.setProgress(this.offset);
        this.offsetSeekBar.setOnSeekBarChangeListener(new OffsetChangeListner());
        this.thresholdSeekBar.setMax(50);
        this.thresholdSeekBar.setProgress(25);
        this.thresholdSeekBar.setOnSeekBarChangeListener(new TolerenceSeekbarListner());
    }


    private void allocatrVariable() {
        this.drawingPath = new Path();
        this.targetPoints = new Vector<>();
    }

    public void setUiSize() {
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        this.mainViewSize = point;
        defaultDisplay.getSize(point);
        int i = (int) getResources().getDisplayMetrics().density;
        this.density = i;
        this.targetOffset = (float) (i * 66);
        getWindow().getDecorView().getWindowVisibleDisplayFrame(new Rect());
        this.imageViewWidth = this.mainViewSize.x;
        this.imageViewHeight = this.mainViewSize.y;
    }

    public void setOnClickActionsMethods() {
        this.fitBtn.setOnClickListener(view -> ManualBGRemoverActivity.this.fitBtnClicked());
        this.resetBtn.setOnClickListener(view -> ManualBGRemoverActivity.this.resetBtnClicked());
        this.undoBtn.setOnClickListener(view -> ManualBGRemoverActivity.this.undoBtnClicked());
        this.redoBtn.setOnClickListener(view -> ManualBGRemoverActivity.this.redoBtnClicked());
        this.shareBtn.setOnClickListener(view -> ManualBGRemoverActivity.this.shareBtnClicked());
        this.lassoBtn.setOnClickListener(view -> ManualBGRemoverActivity.this.lassoBtnClicked());
        this.eraseBtn.setOnClickListener(view -> ManualBGRemoverActivity.this.eraseBtnClicked());
        this.reDrawBtn.setOnClickListener(view -> ManualBGRemoverActivity.this.reDrawBtnClicked());
        this.targetAreaBtn.setOnClickListener(view -> ManualBGRemoverActivity.this.targetAreaBtnClicked());
        this.softedgeButton.setOnClickListener(view -> {
            if (ManualBGRemoverActivity.this.softEdge) {
                ManualBGRemoverActivity.this.softEdge = false;
                ManualBGRemoverActivity.this.softedgeButton.setAlpha(0.55f);
                return;
            }
            ManualBGRemoverActivity.this.softEdge = true;
            ManualBGRemoverActivity.this.softedgeButton.setAlpha(1.0f);
        });
        this.zoomAndPanBtn.setOnClickListener(view -> ManualBGRemoverActivity.this.zoomAndPanBtnClicked());
    }

    public void setBitMap() {
        this.isImageResized = false;
        Bitmap bitmap = this.resizedBitmap;
        if (bitmap != null) {
            bitmap.recycle();
            this.resizedBitmap = null;
        }
        Bitmap bitmap2 = this.bitmapMaster;
        if (bitmap2 != null) {
            bitmap2.recycle();
            this.bitmapMaster = null;
        }
        this.canvasMaster = null;
        Bitmap resizeBitmapByCanvas = resizeBitmapByCanvas(true);
        this.resizedBitmap = resizeBitmapByCanvas;
        if (this.wasImageSaved) {
            Bitmap bmEditedfromInternalStorage = getBmEditedfromInternalStorage();
            if (bmEditedfromInternalStorage != null) {
                this.lastEiditedBitmap = bmEditedfromInternalStorage.copy(Bitmap.Config.ARGB_8888, true);
                bmEditedfromInternalStorage.recycle();
            } else {
                this.lastEiditedBitmap = this.resizedBitmap.copy(Bitmap.Config.ARGB_8888, true);
            }
        } else {
            this.lastEiditedBitmap = resizeBitmapByCanvas.copy(Bitmap.Config.ARGB_8888, true);
        }
        this.bitmapMaster = Bitmap.createBitmap(this.lastEiditedBitmap.getWidth(), this.lastEiditedBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(this.bitmapMaster);
        this.canvasMaster = canvas;
        canvas.drawBitmap(this.lastEiditedBitmap, 0.0f, 0.0f, null);
        this.drawingImageView.setImageBitmap(this.bitmapMaster);
        resetPathArrays();
        eraseBtnClicked();
    }

    public void updateBrush(float f, float f2) {
        this.brush.offset = (float) this.offset;
        this.brush.centerx = f;
        this.brush.centery = f2;
        this.brush.width = this.brushsize / 2.0f;
        this.brush.invalidate();
    }

    public void resetBtnClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Warning!");
        builder.setMessage("Progress will be lost. Are you sure?");
        builder.setNeutralButton("No", (dialogInterface, i) -> dialogInterface.cancel());
        builder.setNegativeButton("Yes", (dialogInterface, i) -> {
            ManualBGRemoverActivity.this.resetPathArrays();
            ManualBGRemoverActivity.this.canvasMaster.drawBitmap(ManualBGRemoverActivity.this.resizedBitmap, 0.0f, 0.0f, null);
            if (ManualBGRemoverActivity.this.lastEiditedBitmap != null) {
                ManualBGRemoverActivity.this.lastEiditedBitmap.recycle();
                ManualBGRemoverActivity.this.lastEiditedBitmap = null;
            }
            ManualBGRemoverActivity bGRemoverActivity = ManualBGRemoverActivity.this;
            bGRemoverActivity.lastEiditedBitmap = bGRemoverActivity.resizedBitmap.copy(ManualBGRemoverActivity.this.resizedBitmap.getConfig(), true);
            ManualBGRemoverActivity.this.drawingImageView.invalidate();
            ManualBGRemoverActivity.this.undoBtn.setEnabled(false);
            ManualBGRemoverActivity.this.redoBtn.setEnabled(false);
            ManualBGRemoverActivity.this.isBitmapUpdated = false;
        });
        builder.show();
    }

    public void fitBtnClicked() {
        if (this.mode == 0) {
            this.drawingImageView.resetZoom();
        }
    }

    public void undoBtnClicked() {
        if (this.mode == 0) {
            this.isBitmapUpdated = false;
            int size = this.paths.size();
            if (size != 0) {
                if (size == 1) {
                    this.undoBtn.setEnabled(false);
                }
                int i = size - 1;
                this.redoTargetPointsArray.add(this.targetPointsArray.remove(i));
                this.redoPaths.add(this.paths.remove(i));
                this.redoErasing.add(this.erasing.remove(i));
                this.redoBrushSizes.add(this.brushSizes.remove(i));
                if (!this.redoBtn.isEnabled()) {
                    this.redoBtn.setEnabled(true);
                }
                updateCanvas(false);
            }
        }
    }

    public void redoBtnClicked() {
        if (this.mode == 0) {
            this.isBitmapUpdated = false;
            int size = this.redoPaths.size();
            if (size != 0) {
                if (size == 1) {
                    this.redoBtn.setEnabled(false);
                }
                int i = size - 1;
                this.targetPointsArray.add(this.redoTargetPointsArray.remove(i));
                this.paths.add(this.redoPaths.remove(i));
                this.erasing.add(this.redoErasing.remove(i));
                this.brushSizes.add(this.redoBrushSizes.remove(i));
                if (!this.undoBtn.isEnabled()) {
                    this.undoBtn.setEnabled(true);
                }
                updateCanvas(false);
            }
        }
    }

    public void lassoBtnClicked() {
        this.widthContainer.setVisibility(View.VISIBLE);
        this.thresholdContainer.setVisibility(View.INVISIBLE);
        this.bgButtonsContainer.setVisibility(View.INVISIBLE);
        if (this.drawingmode == 2) {
            this.drawingmode = 7;
            updateCanvas(false);
        }
        this.drawingImageView.setPan(false);
        this.isPanning = false;
        this.drawingmode = 7;
        changeBackground(7);
        this.brush.setMode(3);
        this.brush.invalidate();
    }

    public void eraseBtnClicked() {
        this.widthContainer.setVisibility(View.VISIBLE);
        this.thresholdContainer.setVisibility(View.INVISIBLE);
        this.bgButtonsContainer.setVisibility(View.INVISIBLE);
        if (this.drawingmode == 2) {
            this.drawingmode = 1;
            if (this.paths.size() > 0) {
                updateCanvas(false);
            }
        }
        this.drawingImageView.setPan(false);
        this.isPanning = false;
        this.drawingmode = 1;
        changeBackground(1);
        this.brush.setMode(1);
        this.brush.invalidate();
    }

    public void reDrawBtnClicked() {
        if (!(this.drawingmode == 2 || this.resizedBitmap == null)) {
            Bitmap bitmap = this.bitmapMaster;
            Bitmap copy = bitmap.copy(bitmap.getConfig(), false);
            this.canvasMaster.drawBitmap(this.resizedBitmap, 0.0f, 0.0f, null);
            this.canvasMaster.drawColor(Color.argb(150, 0, 255, 20));
            this.canvasMaster.drawBitmap(copy, 0.0f, 0.0f, null);
        }
        this.widthContainer.setVisibility(View.VISIBLE);
        this.thresholdContainer.setVisibility(View.INVISIBLE);
        this.bgButtonsContainer.setVisibility(View.INVISIBLE);
        this.drawingImageView.setPan(false);
        this.isPanning = false;
        this.drawingmode = 2;
        changeBackground(2);
        this.brush.setMode(1);
        this.brush.invalidate();
    }

    public void targetAreaBtnClicked() {
        if (this.drawingmode != 4) {
            this.isBitmapUpdated = false;
        }
        this.widthContainer.setVisibility(View.INVISIBLE);
        this.thresholdContainer.setVisibility(View.VISIBLE);
        this.bgButtonsContainer.setVisibility(View.INVISIBLE);
        if (this.drawingmode == 2) {
            this.drawingmode = 4;
            updateCanvas(false);
        }
        this.brush.setMode(2);
        this.drawingmode = 4;
        changeBackground(4);
        this.drawingImageView.setPan(false);
        this.isPanning = false;
        this.brush.invalidate();
    }


    public void zoomAndPanBtnClicked() {
        this.drawingImageView.setPan(true);
        this.isPanning = true;
        changeBackground(5);
        this.brush.setMode(0);
        this.brush.invalidate();
    }

    public void changeBackground(int i) {
        this.lassoBtn.setBackgroundColor(Color.argb(0, 0, 0, 0));
        this.eraseBtn.setBackgroundColor(Color.argb(0, 0, 0, 0));
        this.targetAreaBtn.setBackgroundColor(Color.argb(0, 0, 0, 0));
        this.reDrawBtn.setBackgroundColor(Color.argb(0, 0, 0, 0));
        this.zoomAndPanBtn.setBackgroundColor(Color.argb(0, 0, 0, 0));
        if (i == 7) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                this.lassoBtn.setBackgroundColor(getColor(R.color.editor_controller_bg));
            }
        } else if (i == 1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                this.eraseBtn.setBackgroundColor(getColor(R.color.editor_controller_bg));
            }
        } else if (i == 4) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                this.targetAreaBtn.setBackgroundColor(getColor(R.color.editor_controller_bg));
            }
        } else if (i == 2) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                this.reDrawBtn.setBackgroundColor(getColor(R.color.editor_controller_bg));
            }
        } else if (i == 5) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                this.zoomAndPanBtn.setBackgroundColor(getColor(R.color.editor_controller_bg));
            }
        }
    }

    public void shareBtnClicked() {
        if (this.mode == 0) {
            if (this.drawingmode == 2) {
                updateCanvas(true);
                makeHighResolutionOutput();
                Bitmap bitmap = this.bitmapMaster;
                Bitmap copy = bitmap.copy(bitmap.getConfig(), false);
                this.canvasMaster.drawBitmap(this.resizedBitmap, 0.0f, 0.0f, null);
                this.canvasMaster.drawColor(Color.argb(150, 0, 255, 20));
                this.canvasMaster.drawBitmap(copy, 0.0f, 0.0f, null);
            } else {
                makeHighResolutionOutput();
            }
            ManualBGRemoverActivity.this.savePhoto(ManualBGRemoverActivity.highResolutionOutput);

        }
    }

    public void updateCanvas(boolean z) {
        this.canvasMaster.drawColor(0, PorterDuff.Mode.CLEAR);
        this.canvasMaster.drawBitmap(this.lastEiditedBitmap, 0.0f, 0.0f, null);
        for (int i = 0; i < this.paths.size(); i++) {
            int intValue = this.brushSizes.get(i).intValue();
            int intValue2 = this.erasing.get(i).intValue();
            Paint paint = new Paint();
            if (this.softEdge) {
                paint.setMaskFilter(new BlurMaskFilter(10.0f, BlurMaskFilter.Blur.NORMAL));
            } else {
                paint.setMaskFilter(new BlurMaskFilter(1.0f, BlurMaskFilter.Blur.NORMAL));
            }
            if (intValue2 == 1) {
                paint.setColor(0);
                paint.setStyle(Paint.Style.STROKE);
                paint.setAntiAlias(true);
                paint.setStrokeJoin(Paint.Join.ROUND);
                paint.setStrokeCap(Paint.Cap.ROUND);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
                paint.setStrokeWidth((float) intValue);
                this.canvasMaster.drawPath(this.paths.get(i), paint);
            } else if (intValue2 == 2) {
                paint.setStrokeWidth((float) intValue);
                paint.setStyle(Paint.Style.STROKE);
                paint.setAntiAlias(true);
                paint.setStrokeJoin(Paint.Join.ROUND);
                paint.setStrokeCap(Paint.Cap.ROUND);
                BitmapShader bitmapShader = new BitmapShader(this.resizedBitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
                paint.setColor(-1);
                paint.setShader(bitmapShader);
                this.canvasMaster.drawPath(this.paths.get(i), paint);
            } else if (intValue2 == 7) {
                Bitmap bitmap = this.bitmapMaster;
                Bitmap copy = bitmap.copy(bitmap.getConfig(), true);
                new Canvas(copy).drawBitmap(this.bitmapMaster, 0.0f, 0.0f, null);
                Canvas canvas = new Canvas(this.bitmapMaster);
                canvas.drawColor(0, PorterDuff.Mode.CLEAR);
                Paint paint2 = new Paint();
                canvas.drawPath(this.paths.get(i), paint2);
                paint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                canvas.drawBitmap(copy, 0.0f, 0.0f, paint2);
            } else if (intValue2 == 6) {
                Vector<Point> vector = this.targetPointsArray.get(i);
                for (int i2 = 0; i2 < vector.size(); i2++) {
                    Point point = vector.get(i2);
                    this.bitmapMaster.setPixel(point.x, point.y, 0);
                }
            }
        }
        if (!z) {
            if (this.drawingmode == 2) {
                Bitmap bitmap2 = this.bitmapMaster;
                Bitmap copy2 = bitmap2.copy(bitmap2.getConfig(), false);
                this.canvasMaster.drawBitmap(this.resizedBitmap, 0.0f, 0.0f, null);
                this.canvasMaster.drawColor(Color.argb(150, 0, 255, 20));
                this.canvasMaster.drawBitmap(copy2, 0.0f, 0.0f, null);
            }
            this.drawingImageView.invalidate();
        }
    }

    private void makeHighResolutionOutput() {
        if (this.isImageResized) {
            Bitmap createBitmap = Bitmap.createBitmap(this.originalBitmap.getWidth(), this.originalBitmap.getHeight(), this.originalBitmap.getConfig());
            Canvas canvas = new Canvas(createBitmap);
            Paint paint = new Paint();
            if (this.softEdge) {
                paint.setMaskFilter(new BlurMaskFilter(10.0f, BlurMaskFilter.Blur.NORMAL));
            } else {
                paint.setMaskFilter(new BlurMaskFilter(1.0f, BlurMaskFilter.Blur.NORMAL));
            }
            paint.setColor(Color.argb(255, 255, 255, 255));
            Rect rect = new Rect(0, 0, this.bitmapMaster.getWidth(), this.bitmapMaster.getHeight());
            Rect rect2 = new Rect(0, 0, this.originalBitmap.getWidth(), this.originalBitmap.getHeight());
            canvas.drawRect(rect2, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
            canvas.drawBitmap(this.bitmapMaster, rect, rect2, paint);
            highResolutionOutput = null;
            highResolutionOutput = Bitmap.createBitmap(this.originalBitmap.getWidth(), this.originalBitmap.getHeight(), this.originalBitmap.getConfig());
            Canvas canvas2 = new Canvas(highResolutionOutput);
            canvas2.drawBitmap(this.originalBitmap, 0.0f, 0.0f, null);
            Paint paint2 = new Paint();
            paint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
            canvas2.drawBitmap(createBitmap, 0.0f, 0.0f, paint2);
            return;
        }
        highResolutionOutput = null;
        Bitmap bitmap = this.bitmapMaster;
        highResolutionOutput = bitmap.copy(bitmap.getConfig(), true);
    }

    public void savePhoto(Bitmap bitmap) {


        new MyUtils.GetImageFileAsync(ManualBGRemoverActivity.this, bitmap).onBitmapSaved(file1 -> {

            Intent intent = new Intent();
            PickedImageActionFragment.path = file1.getAbsolutePath();
            setResult(RESULT_OK, intent);

            finish();

        });


    }

    private void moveTopoint(float f, float f2) {
        float imageViewZoom = getImageViewZoom();
        float f3 = f2 - ((float) this.offset);
        if (this.redoPaths.size() > 0) {
            resetRedoPathArrays();
        }
        PointF imageViewTranslation = getImageViewTranslation();
        double d = f - imageViewTranslation.x;
        double d2 = imageViewZoom;
        int i = (int) ((float) (d / d2));
        double d3 = f3 - imageViewTranslation.y;

        int i2 = (int) ((float) (d3 / d2));
        this.drawingPath.moveTo((float) i, (float) i2);
        if (this.drawingmode == 7) {
            this.lassoStartX = i;
            this.lassoStartY = i2;
        }
        this.updatedbrushsize = (int) (this.brushsize / imageViewZoom);
    }

    public PointF getImageViewTranslation() {
        return this.drawingImageView.getTransForm();
    }

    private void lineTopoint(Bitmap bitmap, float f, float f2) {
        int i = this.initialdrawingcount;
        int i2 = this.initialdrawingcountlimit;
        if (i < i2) {
            int i3 = i + 1;
            this.initialdrawingcount = i3;
            if (i3 == i2) {
                this.ismultipletoucherasing = true;
            }
        }
        float imageViewZoom = getImageViewZoom();
        float f3 = f2 - ((float) this.offset);
        PointF imageViewTranslation = getImageViewTranslation();
        double d = f - imageViewTranslation.x;
        double d2 = imageViewZoom;
        int i4 = (int) ((float) (d / d2));
        double d3 = f3 - imageViewTranslation.y;

        int i5 = (int) ((float) (d3 / d2));
        if (!this.isTouchOnBitmap && i4 > 0 && i4 < bitmap.getWidth() && i5 > 0 && i5 < bitmap.getHeight()) {
            this.isTouchOnBitmap = true;
        }
        this.drawingPath.lineTo((float) i4, (float) i5);
    }

    private void drawOnTouchMove() {
        Paint paint = new Paint();
        if (this.softEdge) {
            paint.setMaskFilter(new BlurMaskFilter(10.0f, BlurMaskFilter.Blur.NORMAL));
        } else {
            paint.setMaskFilter(new BlurMaskFilter(1.0f, BlurMaskFilter.Blur.NORMAL));
        }
        int i = this.drawingmode;
        if (i == 1) {
            paint.setStrokeWidth((float) this.updatedbrushsize);
            paint.setColor(0);
            paint.setStyle(Paint.Style.STROKE);
            paint.setAntiAlias(true);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        } else if (i == 2) {
            paint.setStrokeWidth((float) this.updatedbrushsize);
            paint.setStyle(Paint.Style.STROKE);
            paint.setAntiAlias(true);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeCap(Paint.Cap.ROUND);
            BitmapShader bitmapShader = new BitmapShader(this.resizedBitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
            paint.setColor(-1);
            paint.setShader(bitmapShader);
        }
        this.canvasMaster.drawPath(this.drawingPath, paint);
        this.drawingImageView.invalidate();
    }

    private void applyLasso() {
        Bitmap bitmap = this.bitmapMaster;
        Bitmap copy = bitmap.copy(bitmap.getConfig(), true);
        new Canvas(copy).drawBitmap(this.bitmapMaster, 0.0f, 0.0f, null);
        this.canvasMaster.drawColor(0, PorterDuff.Mode.CLEAR);
        Paint paint = new Paint();
        if (this.softEdge) {
            paint.setMaskFilter(new BlurMaskFilter(10.0f, BlurMaskFilter.Blur.NORMAL));
        } else {
            paint.setMaskFilter(new BlurMaskFilter(1.0f, BlurMaskFilter.Blur.NORMAL));
        }
        paint.setAntiAlias(true);
        this.canvasMaster.drawPath(this.drawingPath, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        this.canvasMaster.drawBitmap(copy, 0.0f, 0.0f, paint);
        this.drawingImageView.invalidate();
    }

    private void applyFloodFil(float f, float f2) {
        this.isBitmapUpdated = false;
        float imageViewZoom = getImageViewZoom();
        float f3 = f2 - this.targetOffset;
        PointF imageViewTranslation = getImageViewTranslation();
        double d = f - imageViewTranslation.x;

        int i = (int) ((float) (d / (double) imageViewZoom));
        double d3 = f3 - imageViewTranslation.y;


        int i2 = (int) ((float) (d3 / (double) imageViewZoom));
        if (i >= 0 && i <= this.bitmapMaster.getWidth() && i2 >= 0 && i2 <= this.bitmapMaster.getHeight()) {
            this.isBitmapUpdated = true;
            this.targetValueX = i;
            this.targetValueY = i2;
            this.isAsyncExecuteForThresholdChange = false;
            applyFloodFillWithProgressBar();
        }
    }

    private void applyRePlaceColor(float f, float f2) {
        this.isBitmapUpdated = false;
        float imageViewZoom = getImageViewZoom();
        float f3 = f2 - this.targetOffset;
        PointF imageViewTranslation = getImageViewTranslation();
        double d = f - imageViewTranslation.x;
        double d2 = imageViewZoom;

        int i = (int) ((float) (d / d2));
        double d3 = f3 - imageViewTranslation.y;


        int i2 = (int) ((float) (d3 / d2));
        if (i >= 0 && i <= this.bitmapMaster.getWidth() && i2 >= 0 && i2 <= this.bitmapMaster.getHeight() && this.bitmapMaster.getPixel(i, i2) != 0) {
            this.targetValueX = i;
            this.targetValueY = i2;
            Bitmap bitmap = this.bitmapMaster;
            rePlaceAcolorOfBitmap(bitmap, bitmap.getPixel(i, i2), 0);
            if (this.targetPoints.size() != 0) {
                this.isBitmapUpdated = true;
                if (this.redoPaths.size() > 0) {
                    resetRedoPathArrays();
                }
                addDrawingPathToArrayList();
            }
        }
    }

    public Bitmap resizeBitmapByCanvas(boolean z) {
        if (originalBitmap == null){
            Toast.makeText(this, "null bitmap", Toast.LENGTH_SHORT).show();
        }
        float f;
        float f2;
        float width = (float) this.originalBitmap.getWidth();
        float height = (float) this.originalBitmap.getHeight();
        if (width > height) {
            int i = this.imageViewWidth;
            f = (float) i;
            f2 = (((float) i) * height) / width;
        } else {
            int i2 = this.imageViewHeight;
            f = (((float) i2) * width) / height;
            f2 = (float) i2;
        }
        if (f > width || f2 > height) {
            return this.originalBitmap;
        }
        Bitmap createBitmap = Bitmap.createBitmap((int) f, (int) f2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        float f3 = f / width;
        Matrix matrix = new Matrix();
        matrix.postTranslate(0.0f, (f2 - (height * f3)) / 2.0f);
        matrix.preScale(f3, f3);
        Paint paint = new Paint();
        paint.setFilterBitmap(true);
        canvas.drawBitmap(this.originalBitmap, matrix, paint);
        this.isImageResized = true;
        return createBitmap;
    }

    public Bitmap getBmEditedfromInternalStorage() {
        try {
            FileInputStream openFileInput = openFileInput("BITMAP_EDITED");
            Bitmap decodeStream = BitmapFactory.decodeStream(openFileInput);
            openFileInput.close();
            return decodeStream;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public void resetPathArrays() {
        this.undoBtn.setEnabled(false);
        this.redoBtn.setEnabled(false);
        this.targetPointsArray.clear();
        this.redoTargetPointsArray.clear();
        this.paths.clear();
        this.brushSizes.clear();
        this.erasing.clear();
        this.redoPaths.clear();
        this.redoBrushSizes.clear();
        this.redoErasing.clear();
    }

    private void addDrawingPathToArrayList() {
        if (this.paths.size() >= this.undolimit) {
            updateLastEiditedBitmapForUndoLimit();
            this.targetPointsArray.remove(0);
            this.paths.remove(0);
            this.erasing.remove(0);
            this.brushSizes.remove(0);
        }
        if (this.paths.size() == 0) {
            this.undoBtn.setEnabled(true);
            this.redoBtn.setEnabled(false);
        }
        int i = this.drawingmode;
        if (i == 1) {
            this.erasing.add(1);
        } else if (i == 2) {
            this.erasing.add(2);
        } else if (i == 4 || i == 3) {
            this.erasing.add(6);
        } else if (i == 7) {
            this.erasing.add(7);
        }
        this.brushSizes.add(Integer.valueOf(this.updatedbrushsize));
        this.paths.add(this.drawingPath);
        this.drawingPath = new Path();
        this.targetPointsArray.add(this.targetPoints);
        this.targetPoints = new Vector<>();
    }

    public void updateBrushWidth() {
        this.brush.width = this.brushsize / 2.0f;
        this.brush.invalidate();
    }

    public void updateBrushOffset() {
        this.brush.centery += ((float) this.offset) - this.brush.offset;
        this.brush.offset = (float) this.offset;
        this.brush.invalidate();
    }

    public void updateLastEiditedBitmapForUndoLimit() {
        Canvas canvas = new Canvas(this.lastEiditedBitmap);
        for (int i = 0; i < 1; i++) {
            int intValue = this.brushSizes.get(i).intValue();
            int intValue2 = this.erasing.get(i).intValue();
            Paint paint = new Paint();
            if (this.softEdge) {
                paint.setMaskFilter(new BlurMaskFilter(10.0f, BlurMaskFilter.Blur.NORMAL));
            } else {
                paint.setMaskFilter(new BlurMaskFilter(1.0f, BlurMaskFilter.Blur.NORMAL));
            }
            if (intValue2 == 1) {
                paint.setColor(0);
                paint.setStyle(Paint.Style.STROKE);
                paint.setAntiAlias(true);
                paint.setStrokeJoin(Paint.Join.ROUND);
                paint.setStrokeCap(Paint.Cap.ROUND);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
                paint.setStrokeWidth((float) intValue);
                canvas.drawPath(this.paths.get(i), paint);
            } else if (intValue2 == 2) {
                paint.setStrokeWidth((float) intValue);
                paint.setStyle(Paint.Style.STROKE);
                paint.setAntiAlias(true);
                paint.setStrokeJoin(Paint.Join.ROUND);
                paint.setStrokeCap(Paint.Cap.ROUND);
                BitmapShader bitmapShader = new BitmapShader(this.resizedBitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
                paint.setColor(-1);
                paint.setShader(bitmapShader);
                canvas.drawPath(this.paths.get(i), paint);
            } else if (intValue2 == 7) {
                Bitmap bitmap = this.lastEiditedBitmap;
                Bitmap copy = bitmap.copy(bitmap.getConfig(), true);
                new Canvas(copy).drawBitmap(this.lastEiditedBitmap, 0.0f, 0.0f, null);
                Canvas canvas2 = new Canvas(this.lastEiditedBitmap);
                canvas2.drawColor(0, PorterDuff.Mode.CLEAR);
                Paint paint2 = new Paint();
                paint2.setAntiAlias(true);
                canvas2.drawPath(this.paths.get(i), paint2);
                paint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                canvas2.drawBitmap(copy, 0.0f, 0.0f, paint2);
            } else if (intValue2 == 6) {
                Vector<Point> vector = this.targetPointsArray.get(i);
                for (int i2 = 0; i2 < vector.size(); i2++) {
                    Point point = vector.get(i2);
                    this.lastEiditedBitmap.setPixel(point.x, point.y, 0);
                }
            }
        }
    }

    private void applyFloodFillWithProgressBar() {
        this.thresholdContainer.setVisibility(View.INVISIBLE);
        this.spinner.setVisibility(View.VISIBLE);
        getWindow().setFlags(16, 16);
        this.thresholdSeekBar.setEnabled(false);
        new Handler().postDelayed(new Runnable() {

            public void run() {
                if (ManualBGRemoverActivity.this.isAsyncExecuteForThresholdChange) {
                    ManualBGRemoverActivity.this.undoForThresholdChange();
                }
                ManualBGRemoverActivity bGRemoverActivity = ManualBGRemoverActivity.this;
                bGRemoverActivity.floodFill(bGRemoverActivity.bitmapMaster, new Point(ManualBGRemoverActivity.this.targetValueX, ManualBGRemoverActivity.this.targetValueY), ManualBGRemoverActivity.this.bitmapMaster.getPixel(ManualBGRemoverActivity.this.targetValueX, ManualBGRemoverActivity.this.targetValueY), 0);
                if (ManualBGRemoverActivity.this.isBitmapUpdated) {
                    ManualBGRemoverActivity.this.addDrawingPathToArrayList();
                    ManualBGRemoverActivity.this.resetRedoPathArrays();
                    ManualBGRemoverActivity.this.undoBtn.setEnabled(true);
                    ManualBGRemoverActivity.this.redoBtn.setEnabled(false);
                }
                new Handler().postDelayed(new AutoRunabble(), 100);
            }

            class AutoRunabble implements Runnable {
                AutoRunabble() {
                }

                public void run() {
                    ManualBGRemoverActivity.this.spinner.setVisibility(View.INVISIBLE);
                    if (ManualBGRemoverActivity.this.drawingmode == 4) {
                        ManualBGRemoverActivity.this.thresholdContainer.setVisibility(View.VISIBLE);
                    }
                    ManualBGRemoverActivity.this.thresholdSeekBar.setEnabled(true);
                    ManualBGRemoverActivity.this.getWindow().clearFlags(16);
                }
            }
        }, 100);
    }

    public void rePlaceAcolorOfBitmap(Bitmap bitmap, int i, int i2) {
        for (int i3 = 0; i3 < bitmap.getWidth(); i3++) {
            for (int i4 = 0; i4 < bitmap.getHeight(); i4++) {
                if (compareColor(bitmap.getPixel(i3, i4), i)) {
                    bitmap.setPixel(i3, i4, i2);
                    this.targetPoints.add(new Point(i3, i4));
                }
            }
        }
        this.drawingImageView.invalidate();
    }

    public boolean compareColor(int i, int i2) {
        if (!(i == 0 || i2 == 0)) {
            if (i == i2) {
                return true;
            }
            int abs = Math.abs(Color.red(i) - Color.red(i2));
            int abs2 = Math.abs(Color.green(i) - Color.green(i2));
            int abs3 = Math.abs(Color.blue(i) - Color.blue(i2));
            int i3 = this.tolerance;
            return abs <= i3 && abs2 <= i3 && abs3 <= i3;
        }
        return false;
    }

    public void rePlaceColor(Bitmap bitmap, int i, int i2, int i3, int i4) {
        int max = Math.max(i - i4, 0);
        int max2 = Math.max(i2 - i4, 0);
        int max3 = Math.max(i3 - i4, 0);
        int min = Math.min(i + i4, 255);
        int min2 = Math.min(i2 + i4, 255);
        int min3 = Math.min(i3 + i4, 255);
        for (int i5 = 0; i5 < bitmap.getWidth(); i5++) {
            for (int i6 = 0; i6 < bitmap.getHeight(); i6++) {
                int pixel = bitmap.getPixel(i5, i6);
                int i7 = (pixel >> 16) & 255;
                int i8 = (pixel >> 8) & 255;
                int i9 = pixel & 255;
                if (i7 >= max && i7 <= min && i8 >= max2 && i8 <= min2 && i9 >= max3 && i9 <= min3) {
                    bitmap.setPixel(i5, i6, 0);
                    this.targetPoints.add(new Point(i5, i6));
                }
            }
        }
        this.drawingImageView.invalidate();
    }

    public void undoForThresholdChange() {
        int size = this.paths.size() - 1;
        if (this.erasing.get(size).intValue() == 6) {
            Vector<Point> vector = this.targetPointsArray.get(size);
            for (int i = 0; i < vector.size(); i++) {
                Point point = vector.get(i);
                this.bitmapMaster.setPixel(point.x, point.y, this.resizedBitmap.getPixel(point.x, point.y));
            }
            this.targetPointsArray.remove(size);
            this.paths.remove(size);
            this.erasing.remove(size);
            this.brushSizes.remove(size);
        }
    }

    private void floodFill(Bitmap bitmap, Point point, int i, int i2) {
        if (i == 0) {
            this.isBitmapUpdated = false;
            return;
        }
        LinkedList linkedList = new LinkedList();
        linkedList.add(point);
        while (linkedList.size() > 0) {
            Point point2 = (Point) linkedList.poll();
            if (compareColor(bitmap.getPixel(point2.x, point2.y), i)) {
                Point point3 = new Point(point2.x + 1, point2.y);
                while (point2.x > 0 && compareColor(bitmap.getPixel(point2.x, point2.y), i)) {
                    bitmap.setPixel(point2.x, point2.y, i2);
                    this.targetPoints.add(new Point(point2.x, point2.y));
                    if (point2.y > 0 && compareColor(bitmap.getPixel(point2.x, point2.y - 1), i)) {
                        linkedList.add(new Point(point2.x, point2.y - 1));
                    }
                    if (point2.y < bitmap.getHeight() - 1 && compareColor(bitmap.getPixel(point2.x, point2.y + 1), i)) {
                        linkedList.add(new Point(point2.x, point2.y + 1));
                    }
                    point2.x--;
                }
                while (point3.x < bitmap.getWidth() - 1 && compareColor(bitmap.getPixel(point3.x, point3.y), i)) {
                    bitmap.setPixel(point3.x, point3.y, i2);
                    this.targetPoints.add(new Point(point3.x, point3.y));
                    if (point3.y > 0 && compareColor(bitmap.getPixel(point3.x, point3.y - 1), i)) {
                        linkedList.add(new Point(point3.x, point3.y - 1));
                    }
                    if (point3.y < bitmap.getHeight() - 1 && compareColor(bitmap.getPixel(point3.x, point3.y + 1), i)) {
                        linkedList.add(new Point(point3.x, point3.y + 1));
                    }
                    point3.x++;
                }
            }
        }
    }

    public void resetRedoPathArrays() {
        this.redoBtn.setEnabled(false);
        this.redoTargetPointsArray.clear();
        this.redoPaths.clear();
        this.redoBrushSizes.clear();
        this.redoErasing.clear();
    }

    public float getImageViewZoom() {
        return this.drawingImageView.getCurrentZoom();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private class MainTouchListner implements View.OnTouchListener {
        MainTouchListner() {
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            int action = motionEvent.getAction();
            if (ManualBGRemoverActivity.this.isPanning || (motionEvent.getPointerCount() != 1 && !ManualBGRemoverActivity.this.ismultipletoucherasing)) {
                if (ManualBGRemoverActivity.this.initialdrawingcount > 0) {
                    if (ManualBGRemoverActivity.this.drawingmode == 1 || ManualBGRemoverActivity.this.drawingmode == 2) {
                        ManualBGRemoverActivity.this.updateCanvas(false);
                        ManualBGRemoverActivity.this.drawingPath.reset();
                    } else if (ManualBGRemoverActivity.this.drawingmode == 7) {
                        ManualBGRemoverActivity.this.brush.lessoLineDrawingPath.reset();
                        ManualBGRemoverActivity.this.brush.invalidate();
                    }
                    ManualBGRemoverActivity.this.initialdrawingcount = 0;
                }
                ManualBGRemoverActivity.this.drawingImageView.onTouchEvent(motionEvent);
                ManualBGRemoverActivity.this.mode = 5;
            } else if (action == 0) {
                ManualBGRemoverActivity.this.isTouchOnBitmap = false;
                ManualBGRemoverActivity.this.drawingImageView.onTouchEvent(motionEvent);
                ManualBGRemoverActivity.this.mode = 1;
                ManualBGRemoverActivity.this.initialdrawingcount = 0;
                ManualBGRemoverActivity.this.ismultipletoucherasing = false;
                if (ManualBGRemoverActivity.this.drawingmode == 1 || ManualBGRemoverActivity.this.drawingmode == 2 || ManualBGRemoverActivity.this.drawingmode == 7) {
                    ManualBGRemoverActivity bGRemoverActivity = ManualBGRemoverActivity.this;
                    bGRemoverActivity.moveTopoint(motionEvent.getX(), motionEvent.getY());
                }
                if (ManualBGRemoverActivity.this.drawingmode == 7) {
                    ManualBGRemoverActivity.this.brush.resetLessoLineDrawingPath(motionEvent.getX(), motionEvent.getY());
                }
                ManualBGRemoverActivity.this.updateBrush(motionEvent.getX(), motionEvent.getY());
            } else if (action == 2) {
                if (ManualBGRemoverActivity.this.mode == 1) {
                    ManualBGRemoverActivity.this.currentx = motionEvent.getX();
                    ManualBGRemoverActivity.this.currenty = motionEvent.getY();
                    if (ManualBGRemoverActivity.this.drawingmode == 7) {
                        ManualBGRemoverActivity.this.brush.addLineToLessoLineDrawingPath(motionEvent.getX(), motionEvent.getY());
                    }
                    ManualBGRemoverActivity bGRemoverActivity2 = ManualBGRemoverActivity.this;
                    bGRemoverActivity2.updateBrush(bGRemoverActivity2.currentx, ManualBGRemoverActivity.this.currenty);
                    if (ManualBGRemoverActivity.this.drawingmode == 1 || ManualBGRemoverActivity.this.drawingmode == 2 || ManualBGRemoverActivity.this.drawingmode == 7) {
                        ManualBGRemoverActivity bGRemoverActivity3 = ManualBGRemoverActivity.this;
                        bGRemoverActivity3.lineTopoint(bGRemoverActivity3.bitmapMaster, ManualBGRemoverActivity.this.currentx, ManualBGRemoverActivity.this.currenty);
                        if (ManualBGRemoverActivity.this.drawingmode != 7) {
                            ManualBGRemoverActivity.this.drawOnTouchMove();
                        }
                    }
                }
            } else if (action == 1 || action == 6) {
                if (ManualBGRemoverActivity.this.mode == 1) {
                    if (ManualBGRemoverActivity.this.drawingmode == 4) {
                        ManualBGRemoverActivity.this.tolerance = 25;
                        ManualBGRemoverActivity.this.thresholdSeekBar.setProgress(ManualBGRemoverActivity.this.tolerance);
                        ManualBGRemoverActivity.this.applyFloodFil(motionEvent.getX(), motionEvent.getY());
                    } else if (ManualBGRemoverActivity.this.drawingmode == 3) {
                        ManualBGRemoverActivity.this.applyRePlaceColor(motionEvent.getX(), motionEvent.getY());
                    } else if ((ManualBGRemoverActivity.this.drawingmode == 1 || ManualBGRemoverActivity.this.drawingmode == 2 || ManualBGRemoverActivity.this.drawingmode == 7) && ManualBGRemoverActivity.this.initialdrawingcount > 0) {
                        if (ManualBGRemoverActivity.this.drawingmode == 7) {
                            ManualBGRemoverActivity.this.brush.lessoLineDrawingPath.reset();
                            ManualBGRemoverActivity.this.brush.invalidate();
                            if (ManualBGRemoverActivity.this.isTouchOnBitmap) {
                                ManualBGRemoverActivity.this.applyLasso();
                            }
                        }
                        if (ManualBGRemoverActivity.this.isTouchOnBitmap) {
                            ManualBGRemoverActivity.this.addDrawingPathToArrayList();
                        }
                    }
                }
                ManualBGRemoverActivity.this.ismultipletoucherasing = false;
                ManualBGRemoverActivity.this.initialdrawingcount = 0;
                ManualBGRemoverActivity.this.mode = 0;
            }
            if (action == 1 || action == 6) {
                ManualBGRemoverActivity.this.mode = 0;
            }
            return true;
        }
    }

    private class BrushSizeChangeListner implements SeekBar.OnSeekBarChangeListener {


        BrushSizeChangeListner() {
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

            //onstart
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

            //onstart
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean isChanges) {
            ManualBGRemoverActivity.this.brushsize = ((float) i) + 20.0f;
            ManualBGRemoverActivity.this.updateBrushWidth();
        }
    }

    private class OffsetChangeListner implements SeekBar.OnSeekBarChangeListener {
        OffsetChangeListner() {
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            ManualBGRemoverActivity.this.offset = i;
            ManualBGRemoverActivity.this.updateBrushOffset();
        }
    }

    private class TolerenceSeekbarListner implements SeekBar.OnSeekBarChangeListener {
        TolerenceSeekbarListner() {
        }

        public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            // on progress
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
            //onstart
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            if (ManualBGRemoverActivity.this.drawingmode == 3 || ManualBGRemoverActivity.this.drawingmode == 4) {
                ManualBGRemoverActivity.this.tolerance = seekBar.getProgress();
                if (ManualBGRemoverActivity.this.isBitmapUpdated) {
                    if (ManualBGRemoverActivity.this.drawingmode == 4) {
                        ManualBGRemoverActivity.this.isAsyncExecuteForThresholdChange = true;
                        ManualBGRemoverActivity.this.applyFloodFillWithProgressBar();
                    } else if (ManualBGRemoverActivity.this.drawingmode == 3) {
                        ManualBGRemoverActivity bGRemoverActivity = ManualBGRemoverActivity.this;
                        bGRemoverActivity.rePlaceAcolorOfBitmap(bGRemoverActivity.bitmapMaster, ManualBGRemoverActivity.this.bitmapMaster.getPixel(ManualBGRemoverActivity.this.targetValueX, ManualBGRemoverActivity.this.targetValueY), 0);
                    }
                }
            }
        }
    }
}
