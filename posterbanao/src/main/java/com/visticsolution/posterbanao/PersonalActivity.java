package com.visticsolution.posterbanao;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.visticsolution.posterbanao.account.EditProfileActivity;
import com.visticsolution.posterbanao.adapter.PersonalPosterVAdapter;
import com.visticsolution.posterbanao.classes.Functions;
import com.visticsolution.posterbanao.classes.PermissionUtils;
import com.visticsolution.posterbanao.classes.Variables;
import com.visticsolution.posterbanao.databinding.ActivityPersonalBinding;
import com.visticsolution.posterbanao.model.PostsModel;
import com.visticsolution.posterbanao.responses.HomeResponse;
import com.visticsolution.posterbanao.viewmodel.HomeViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PersonalActivity extends AppCompatActivity {

    HomeViewModel homeViewModel;
    ActivityPersonalBinding binding;
    Context context;

    int pageCount = 0;
    LinearLayoutManager layoutManager;
    boolean loading = false;
    PersonalPosterVAdapter adapter;
    List<PostsModel> daily_post = new ArrayList<>();
    View currentView;
    PermissionUtils takePermissionUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPersonalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = this;
        layoutManager = new LinearLayoutManager(this);
        binding.personalRecycler.setLayoutManager(layoutManager);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        takePermissionUtils=new PermissionUtils(this,mPermissionResult);

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.personalRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int pastVisiblesItems, visibleItemCount, totalItemCount;
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItems = 0;
                    firstVisibleItems = layoutManager.findFirstVisibleItemPosition();
                    if(firstVisibleItems > 0) {
                        pastVisiblesItems = firstVisibleItems;
                    }

                    if (!loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = true;
                            pageCount++;
                            getPosts("");
                        }
                    }
                }
            }
        });
        binding.searchBtn.setOnClickListener(view -> {
            pageCount = 0;
            daily_post.clear();
            adapter.notifyDataSetChanged();
            getPosts(binding.searchEt.getText().toString());
            Functions.showLoader(context);
        });
        binding.searchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    pageCount = 0;
                    daily_post.clear();
                    adapter.notifyDataSetChanged();
                    getPosts(binding.searchEt.getText().toString());
                    Functions.showLoader(context);
                    return true;
                }
                return false;
            }
        });

        adapter = new PersonalPosterVAdapter(context, daily_post, new PersonalPosterVAdapter.OnClickEvent() {
            @Override
            public void onClick(View view,View posterew, PostsModel postItem) {
                currentView = posterew;
                int id = view.getId();
                if (id == R.id.removeWatermark) {
                    showPremiumFragment();
                } else if (id == R.id.downloadBtn) {
                    if (takePermissionUtils.isStorageCameraPermissionGranted()) {
                        RelativeLayout premium_tag = currentView.findViewById(R.id.premium_tag);
                        ImageView rewateBtn = currentView.findViewById(R.id.removeWatermark);
                        rewateBtn.setVisibility(View.GONE);
                        premium_tag.setVisibility(View.GONE);

                        if (postItem.getPremium().equals("1") && !Functions.IsPremiumEnable(context)) {
                            showPremiumFragment();
                        } else {
                            saveImage(Functions.viewToBitmap(currentView), "download");
                        }

                    } else {
                        takePermissionUtils.takeStorageCameraPermission();
                    }
                } else if (id == R.id.share_Btn) {
                    if (takePermissionUtils.isStorageCameraPermissionGranted()) {
                        RelativeLayout premium_tag = currentView.findViewById(R.id.premium_tag);
                        ImageView rewateBtn = currentView.findViewById(R.id.removeWatermark);
                        rewateBtn.setVisibility(View.GONE);
                        premium_tag.setVisibility(View.GONE);

                        if (postItem.getPremium().equals("1") && !Functions.IsPremiumEnable(context)) {
                            showPremiumFragment();
                        } else {
                            saveImage(Functions.viewToBitmap(currentView), "Share");
                        }

                    } else {
                        takePermissionUtils.takeStorageCameraPermission();
                    }
                } else if (id == R.id.edit_Btn) {
                    startActivity(new Intent(context, EditProfileActivity.class));
                }
            }
        });
        binding.personalRecycler.setAdapter(adapter);

        Functions.showLoader(context);
        getPosts("");
    }

    private void saveImage(Bitmap bitmap,String type) {
        Functions.showLoader(this);
        String fileName = System.currentTimeMillis() + ".jpeg";
        String filePath = Environment.getExternalStorageDirectory() + File.separator
                + Environment.DIRECTORY_PICTURES + File.separator + getResources().getString(R.string.app_name)
                + File.separator + fileName;

        boolean success = false;

        if (!new File(filePath).exists()) {
            try {
                File file = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES
                ), "/" + getResources().getString(R.string.app_name));
                if (!file.exists()) {
                    if (!file.mkdirs()) {
                        Toast.makeText(this,
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
                    Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(),
                            bitmap.getHeight(), bitmap.getConfig());
                    Canvas canvas = new Canvas(createBitmap);
                    canvas.drawColor(-1);
                    canvas.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
                    createBitmap.compress(Bitmap.CompressFormat.PNG,
                            100, fileOutputStream);
                    createBitmap.recycle();
                    fileOutputStream.flush();
                    fileOutputStream.close();

                    MediaScannerConnection.scanFile(this, new String[]{file2.getAbsolutePath()},
                            (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                                public void onScanCompleted(String str, Uri uri) {
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("-> uri=");
                                    sb.append(uri);
                                    sb.append("-> FILE=");
                                    sb.append(file2.getAbsolutePath());
                                    Uri muri = Uri.fromFile(file2);
                                }
                            });
                    success = true;
                } catch (Exception e) {
                    e.printStackTrace();
                    success = false;
                }
                Functions.cancelLoader();
            } catch (Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            if (success) {
                if (type.equals("download")) {
                    Functions.showToast(this,getString(R.string.image_saved));
                } else {
                    shareFileImageUri(getImageContentUri(new File(filePath)), "", type);
                }
            } else {
                Functions.showToast(this,getString(R.string.error));
            }
            Functions.cancelLoader();
        }
    }

    public Uri getImageContentUri(File imageFile) {
        return Uri.parse(imageFile.getAbsolutePath());
    }

    public void shareFileImageUri(Uri path, String name, String shareTo) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);

        switch (shareTo) {
            case "whtsapp":
                shareIntent.setPackage("com.whatsapp");
                break;
            case "fb":
                shareIntent.setPackage("com.facebook.katana");
                break;
            case "insta":
                shareIntent.setPackage("com.instagram.android");
                break;
            case "twter":
                shareIntent.setPackage("com.twitter.android");
                break;
        }
        shareIntent.setDataAndType(path, "image/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, path);
        if (!name.equals("")) {
            shareIntent.putExtra(Intent.EXTRA_TEXT, name);
        }
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_via)));
    }

    private void getPosts(String search) {
        loading = true;
        homeViewModel.getDailyPosts(search, Functions.getSharedPreference(context).getString(Variables.SELCT_LANGUAGE,""),"",pageCount).observe(this, new Observer<HomeResponse>() {
            @Override
            public void onChanged(HomeResponse homeResponse) {
                loading = false;
                Functions.cancelLoader();
                if (homeResponse != null){
                    if (homeResponse.daily_post.size() > 0){
                        daily_post.addAll(homeResponse.daily_post);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private void showPremiumFragment() {
        startActivity(new Intent(this,PremiumActivity.class));
    }

    private final ActivityResultLauncher<String[]> mPermissionResult = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onActivityResult(Map<String, Boolean> result) {

                }
            });

}