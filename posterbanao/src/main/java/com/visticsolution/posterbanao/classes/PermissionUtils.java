package com.visticsolution.posterbanao.classes;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.activity.result.ActivityResultLauncher;
import androidx.core.content.ContextCompat;

import com.visticsolution.posterbanao.R;

public class PermissionUtils {
    Activity activity;
    ActivityResultLauncher<String[]> mPermissionResult;

    public PermissionUtils(Activity activity, ActivityResultLauncher<String[]> mPermissionResult) {
        this.activity = activity;
        this.mPermissionResult=mPermissionResult;
    }

    public void takeStorageCameraPermission()
    {
        String[] permissions;
        if (android.os.Build.VERSION.SDK_INT > 31) {
            permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO};
        } else {
            permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        }

        mPermissionResult.launch(permissions);
    }

    public boolean isStorageCameraPermissionGranted()
    {
        if (android.os.Build.VERSION.SDK_INT > 31) {
            int readPhotoStoragePermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_MEDIA_IMAGES);
            int readVideoStoragePermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_MEDIA_VIDEO);
            int cameraPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
            return (readPhotoStoragePermission== PackageManager.PERMISSION_GRANTED && readVideoStoragePermission== PackageManager.PERMISSION_GRANTED && cameraPermission== PackageManager.PERMISSION_GRANTED);
        } else {
            int readExternalStoragePermission= ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
            int cameraPermission= ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
            return (readExternalStoragePermission== PackageManager.PERMISSION_GRANTED && cameraPermission== PackageManager.PERMISSION_GRANTED);
        }
    }

}
