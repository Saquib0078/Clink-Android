package com.visticsolution.posterbanao.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.visticsolution.posterbanao.PremiumActivity;
import com.visticsolution.posterbanao.R;
import com.visticsolution.posterbanao.adapter.LogosPagerAdapter;
import com.visticsolution.posterbanao.classes.Functions;
import com.visticsolution.posterbanao.classes.PermissionUtils;
import com.visticsolution.posterbanao.databinding.FragmentLogosListBinding;
import com.visticsolution.posterbanao.editor.adapter.LogosAdapter;
import com.visticsolution.posterbanao.model.LogosModel;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class LogosListFragment extends Fragment {

    private List<LogosModel> logoList = new ArrayList<>();
    LogosPagerAdapter.OnLogoSelect logoListener;

    FragmentLogosListBinding binding;
    String name;
    PermissionUtils takePermissionUtils;
    Context context;

    public LogosListFragment(String name, List<LogosModel> logos, LogosPagerAdapter.OnLogoSelect onStickerSelect) {
        this.name = name;
        this.logoList = logos;
        this.logoListener = onStickerSelect;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLogosListBinding.inflate(getLayoutInflater());
        context = getContext();
        takePermissionUtils = new PermissionUtils(getActivity(), mPermissionResult);
        binding.recyclerView.setAdapter(new LogosAdapter(context, name, logoList, new LogosAdapter.OnClickEvent() {
            @Override
            public void onClick(View view, View logoview, LogosModel postItem) {
                if (takePermissionUtils.isStorageCameraPermissionGranted()) {

                    if (postItem.getPremium().equals("1") && !Functions.IsPremiumEnable(context)) {
                        showPremiumFragment();
                    } else {
                        RelativeLayout premium_tag = logoview.findViewById(R.id.premium_tag);
                        WebView webView = logoview.findViewById(R.id.logoWebView);
                        premium_tag.setVisibility(View.GONE);
                        saveImage(Functions.viewToBitmap(webView));
                    }

                } else {
                    takePermissionUtils.takeStorageCameraPermission();
                }
            }
        }));

        return binding.getRoot();
    }

    private void saveImage(Bitmap bitmap) {
        Functions.showLoader(context);
        String fileName = System.currentTimeMillis() + ".png";
        String filePath = Environment.getExternalStorageDirectory() + File.separator
                + Environment.DIRECTORY_PICTURES + File.separator + getResources().getString(R.string.app_name)
                + File.separator + fileName;

        boolean success = false;

        if (!new File(filePath).exists()) {
            try {
                File file = new File(Functions.getAppFolder(context));
                if (!file.exists()) {
                    if (!file.mkdirs()) {
                        Toast.makeText(context,
                                getResources().getString(R.string.create_dir_err),
                                Toast.LENGTH_LONG).show();
                        success = false;
                    }
                }
                File file2 = new File(file.getAbsolutePath() + "/" + fileName);
                if (file2.exists()) {
                    file2.delete();
                }
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(file2);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                    bitmap.recycle();
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    logoListener.sticker(file2.getAbsolutePath());

                } catch (Exception e) {
                    e.printStackTrace();
                }
                Functions.cancelLoader();
            } catch (Exception e) {
                Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            Functions.cancelLoader();
        }
    }

    private void showPremiumFragment() {
        startActivity(new Intent(context, PremiumActivity.class));
    }

    private ActivityResultLauncher<String[]> mPermissionResult = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onActivityResult(Map<String, Boolean> result) {

                }
            });
}