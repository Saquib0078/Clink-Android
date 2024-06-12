package com.visticsolution.posterbanao.editor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.visticsolution.posterbanao.R;
import com.visticsolution.posterbanao.adapter.PagerFrameAdapter;
import com.visticsolution.posterbanao.editor.utility.ImageUtils;
import com.visticsolution.posterbanao.editor.utils.ExifUtils;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Constants {

    public static String sdcardPath = null;
    public static String fURL = "";
    public static Bitmap bitmap = null;
    public static int[] imageId = {R.drawable.btxt0, R.drawable.btxt1, R.drawable.btxt2, R.drawable.btxt3, R.drawable.btxt4, R.drawable.btxt5, R.drawable.btxt6, R.drawable.btxt7, R.drawable.btxt8, R.drawable.btxt9, R.drawable.btxt10};
    public static int[] overlayArr = {R.drawable.os1, R.drawable.os2, R.drawable.os3, R.drawable.os4, R.drawable.os5, R.drawable.os6, R.drawable.os7, R.drawable.os8, R.drawable.os9, R.drawable.os10};
    public static int[] borderArr = {R.drawable.border_1, R.drawable.border_2, R.drawable.border_3, R.drawable.border_4, R.drawable.border_5, R.drawable.border_6, R.drawable.border_7, R.drawable.border_8};
    public static String rewid = "";
    public static String selectedRatio = "1:1";
    public static String uri = "";
    public static Bitmap bitmapSticker = null;
    public static ArrayList<String> movieImageList = new ArrayList<>();

    public static PagerFrameAdapter framePagerAdapter;
    public static String savedRatio;

    public static Bitmap resizeBitmap(Bitmap bitmap2, int i, int i2) {
        float f;
        float f2 = (float) i;
        float f3 = (float) i2;
        float width = (float) bitmap2.getWidth();
        float height = (float) bitmap2.getHeight();
        Log.i("testings", f2 + "  " + f3 + "  and  " + width + "  " + height);
        float f4 = width / height;
        float f5 = height / width;
        if (width > f2) {
            f = f2 * f5;
            Log.i("testings", "if (wd > wr) " + f2 + "  " + f);
            if (f > f3) {
                f2 = f3 * f4;
                Log.i("testings", "  if (he > hr) " + f2 + "  " + f3);
                return Bitmap.createScaledBitmap(bitmap2, (int) f2, (int) f3, false);
            }
            Log.i("testings", " in else " + f2 + "  " + f);
        } else {
            if (height > f3) {
                float f6 = f3 * f4;
                Log.i("testings", "  if (he > hr) " + f6 + "  " + f3);
                if (f6 > f2) {
                    f3 = f2 * f5;
                } else {
                    Log.i("testings", " in else " + f6 + "  " + f3);
                    f2 = f6;
                }
            } else if (f4 > 0.75f) {
                f3 = f2 * f5;
                Log.i("testings", " if (rat1 > .75f) ");
            } else if (f5 > 1.5f) {
                f2 = f3 * f4;
                Log.i("testings", " if (rat2 > 1.5f) ");
            } else {
                f = f2 * f5;
                Log.i("testings", " in else ");
                if (f > f3) {
                    f2 = f3 * f4;
                    Log.i("testings", "  if (he > hr) " + f2 + "  " + f3);
                } else {
                    Log.i("testings", " in else " + f2 + "  " + f);
                }
            }
            return Bitmap.createScaledBitmap(bitmap2, (int) f2, (int) f3, false);
        }
        f3 = f;
        return Bitmap.createScaledBitmap(bitmap2, (int) f2, (int) f3, false);
    }

    public static Bitmap getBitmapFromUri(Context context, Uri uri2, float f, float f2) throws IOException {
        int exifRotation;
        try {
            ParcelFileDescriptor openFileDescriptor = context.getContentResolver().openFileDescriptor(uri2, "r");
            FileDescriptor fileDescriptor = openFileDescriptor.getFileDescriptor();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
            BitmapFactory.Options options2 = new BitmapFactory.Options();
            if (f <= f2) {
                f = f2;
            }
            int i = (int) f;
            options2.inSampleSize = ImageUtils.getClosestResampleSize(options.outWidth, options.outHeight, i);
            Bitmap decodeFileDescriptor = BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options2);
            Matrix matrix = new Matrix();
            if (decodeFileDescriptor.getWidth() > i || decodeFileDescriptor.getHeight() > i) {
                BitmapFactory.Options resampling = ImageUtils.getResampling(decodeFileDescriptor.getWidth(), decodeFileDescriptor.getHeight(), i);
                matrix.postScale(((float) resampling.outWidth) / ((float) decodeFileDescriptor.getWidth()), ((float) resampling.outHeight) / ((float) decodeFileDescriptor.getHeight()));
            }
            String realPathFromURI = ImageUtils.getRealPathFromURI(uri2, context);
            if (Integer.parseInt(Build.VERSION.SDK) > 4 && (exifRotation = ExifUtils.getExifRotation(realPathFromURI)) != 0) {
                matrix.postRotate((float) exifRotation);
            }
            Bitmap createBitmap = Bitmap.createBitmap(decodeFileDescriptor, 0, 0, decodeFileDescriptor.getWidth(), decodeFileDescriptor.getHeight(), matrix, true);
            openFileDescriptor.close();
            return createBitmap;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean saveBitmapObject(Activity activity, Bitmap bitmap2, String str) {
        Bitmap copy = bitmap2.copy(bitmap2.getConfig(), true);
        File file = new File(str);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            boolean compress = copy.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            copy.recycle();
            activity.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(file)));
            return compress;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("testing", "Exception" + e.getMessage());
            return false;
        }
    }

    public static Animation getAnimUp(Activity activity) {
        return AnimationUtils.loadAnimation(activity, R.anim.anim_slide_up);
    }

    public static Animation getAnimDown(Activity activity) {
        return AnimationUtils.loadAnimation(activity, R.anim.anim_slide_down);
    }

    public static CharSequence getSpannableString(Context context, int i) {
        SpannableStringBuilder append = new SpannableStringBuilder().append(new SpannableString(context.getResources().getString(i)));
        return append.subSequence(0, append.length());
    }



}
