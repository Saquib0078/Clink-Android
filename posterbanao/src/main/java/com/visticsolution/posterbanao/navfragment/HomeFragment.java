package com.visticsolution.posterbanao.navfragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.visticsolution.posterbanao.CustomTamplateActivity;
import com.visticsolution.posterbanao.OpenPostActivity;
import com.visticsolution.posterbanao.PlayAnimatedVideActivity;
import com.visticsolution.posterbanao.PremiumActivity;
import com.visticsolution.posterbanao.VideoTamplateCategoryActivity;
import com.visticsolution.posterbanao.WebviewA;
import com.visticsolution.posterbanao.account.EditProfileActivity;
import com.visticsolution.posterbanao.adapter.BussinessCategoryAdapter;
import com.visticsolution.posterbanao.adapter.CustomTamplateCategoryAdapter;
import com.visticsolution.posterbanao.adapter.FestivalCategoryAdapter;
import com.visticsolution.posterbanao.adapter.PersonalPosterVAdapter;
import com.visticsolution.posterbanao.adapter.PoliticalCategoryAdapter;
import com.visticsolution.posterbanao.adapter.PostsAdapter;
import com.visticsolution.posterbanao.adapter.HomeSectionAdapter;
import com.visticsolution.posterbanao.adapter.SliderPagerAdapter;
import com.visticsolution.posterbanao.adapter.TabRecyclerAdapter;
import com.visticsolution.posterbanao.adapter.UpcomingEventAdapter;
import com.visticsolution.posterbanao.CategoriesActivity;
import com.visticsolution.posterbanao.adapter.VideoTamplateHomeAdapter;
import com.visticsolution.posterbanao.classes.App;
import com.visticsolution.posterbanao.classes.Callback;
import com.visticsolution.posterbanao.classes.Constants;
import com.visticsolution.posterbanao.classes.Functions;
import com.visticsolution.posterbanao.classes.PermissionUtils;
import com.visticsolution.posterbanao.classes.Variables;
import com.visticsolution.posterbanao.binding.BindingAdaptet;
import com.visticsolution.posterbanao.databinding.FragmentHomeBinding;
import com.visticsolution.posterbanao.dialog.LanguageDialogFragment;
import com.visticsolution.posterbanao.fragments.ContactFragment;
import com.visticsolution.posterbanao.fragments.MyBussinessFragment;
import com.visticsolution.posterbanao.fragments.SearchFragment;
import com.visticsolution.posterbanao.interfaces.AdapterClickListener;
import com.visticsolution.posterbanao.MainActivity;
import com.visticsolution.posterbanao.interfaces.DismisListner;
import com.visticsolution.posterbanao.model.CategoryModel;
import com.visticsolution.posterbanao.model.InvitationCategoryModel;
import com.visticsolution.posterbanao.model.OfferDialogModel;
import com.visticsolution.posterbanao.model.PostsModel;
import com.visticsolution.posterbanao.model.SectionModel;
import com.visticsolution.posterbanao.R;
import com.visticsolution.posterbanao.model.SliderModel;
import com.visticsolution.posterbanao.model.VideoTamplateCategory;
import com.visticsolution.posterbanao.responses.HomeResponse;
import com.visticsolution.posterbanao.viewmodel.HomeViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    public static boolean firsttime = false;
    Context context;
    FragmentHomeBinding binding;
    View view;
    HomeViewModel homeViewModel;
    PermissionUtils takePermissionUtils;
    int pageCount = 0;
    String id = "daily";
    List<PostsModel> persoalPostList = new ArrayList<>();
    List<CategoryModel> personalCateList = new ArrayList<>();
    View currentView;
    PersonalPosterVAdapter personalAdapter;
    String cotegoryID;
    List<SliderModel> slider1List = new ArrayList<>(), slider2List = new ArrayList<>(), slider3List = new ArrayList<>();
    ViewPager.PageTransformer compositePageTransformer = new ViewPager.PageTransformer() {
        @Override
        public void transformPage(@NonNull View page, float position) {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.85f + r * 0.15f);
            page.setScaleX(0.85f + r * 0.15f);
        }
    };
    private final ActivityResultLauncher<String[]> mPermissionResult = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onActivityResult(Map<String, Boolean> result) {

                }
            });

    public HomeFragment(boolean firsttime) {
        HomeFragment.firsttime = firsttime;
    }

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Functions.setLocale(Functions.getSharedPreference(getContext()).getString(Variables.APP_LANGUAGE_CODE, Variables.DEFAULT_LANGUAGE_CODE), getActivity(), MainActivity.class, false);
        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        Functions.fadeIn(binding.getRoot(), getContext());

        Window window = getActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#08BBCC"));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getContext();
        this.view = view;

        takePermissionUtils = new PermissionUtils(getActivity(), mPermissionResult);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding.searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);
                transaction.addToBackStack(null);
                transaction.replace(android.R.id.content, new SearchFragment()).commit();
            }
        });
        binding.contactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showContactFragment();
            }
        });
        binding.languageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLanguageDialog();
            }
        });
        binding.backImg.setOnClickListener(view12 -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });
        binding.businessRefreshLay.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getHomeData();
            }
        });

        binding.personalRefreshLay.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageCount = 0;
                getPersonalData();
            }
        });

        binding.politicalViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, CategoriesActivity.class).putExtra("type", "political"));
            }
        });

        binding.upcomingViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, CategoriesActivity.class).putExtra("type", "festival"));
            }
        });

        binding.viewAllFestivalCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, CategoriesActivity.class).putExtra("type", "custom"));
            }
        });

        binding.viewAllBusinessCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, CategoriesActivity.class).putExtra("type", "business"));
            }
        });

        binding.videoTamplateViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, VideoTamplateCategoryActivity.class));
            }
        });

        binding.foryouViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, OpenPostActivity.class)
                        .putExtra("title", Functions.getSharedPreference(context).getString(Variables.ACTIVE_PROFILE_CATEGORY_NAME, ""))
                        .putExtra("type", "category")
                        .putExtra("item_id", Functions.getSharedPreference(context).getString(Variables.ACTIVE_PROFILE_CATEGORY_ID, "")));
            }
        });

        binding.activeBusinessName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyBussinessFragment fragment = new MyBussinessFragment(new DismisListner() {
                    @Override
                    public void onDismis() {
                        getHomeData();
                        binding.forYouTv.setText(getString(R.string.for_you) + " (" + Functions.getSharedPreference(context).getString(Variables.ACTIVE_PROFILE_CATEGORY_NAME, "") + ")");
                        binding.activeBusinessName.setText(Functions.getSharedPreference(context).getString(Variables.ACTIVE_PROFILE_NAME, getString(R.string.add_bussiness)));
                    }
                });
                fragment.show(getChildFragmentManager(), "");
            }
        });

        binding.businessBtn.setOnClickListener(view1 -> {
            binding.personalBtn.getBackground().setTint(context.getColor(R.color.black_50));
            binding.businessBtn.getBackground().setTint(context.getColor(R.color.app_color));
            binding.businessRefreshLay.setVisibility(View.VISIBLE);
            binding.personalRefreshLay.setVisibility(View.GONE);
            binding.helpLayout.setVisibility(View.VISIBLE);
            getHomeData();
        });
        binding.personalBtn.setOnClickListener(view1 -> {
            binding.personalBtn.getBackground().setTint(context.getColor(R.color.app_color));
            binding.businessBtn.getBackground().setTint(context.getColor(R.color.black_50));
            binding.businessRefreshLay.setVisibility(View.GONE);
            binding.personalRefreshLay.setVisibility(View.VISIBLE);
            binding.helpLayout.setVisibility(View.GONE);
            getPersonalData();
        });
        binding.personalBtn.setVisibility(View.GONE);
        binding.businessBtn.setVisibility(View.GONE);

        getHomeData();

        binding.personalRecy.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == (persoalPostList.size() - 1)) {
                    pageCount++;
                    getPersonalData();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        if (Functions.getSharedPreference(context).getString("whatsapp_contact", "").equals("true")) {
            AnimatorSet mAnimatorSet = new AnimatorSet();
            mAnimatorSet.playTogether(
                    animate("scaleX", 0.65f, 1),
                    animate("scaleY", 0.65f, 1),
                    animate("alpha", 0, 1)
            );
            mAnimatorSet.start();

            binding.hideHelpBtn.setOnClickListener(view1 -> {
                mAnimatorSet.pause();
                binding.helpLayout.setVisibility(View.GONE);
            });

            binding.helpLayout.setOnClickListener(view1 -> {
                String url = "https://api.whatsapp.com/send?phone=" + Functions.getSharedPreference(context).getString("whatsapp_number", "");
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            });
        } else {
            binding.helpLayout.setVisibility(View.GONE);
        }


    }

    private ObjectAnimator animate(String style, float... values) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(binding.helpLayout, style, values).setDuration(1200);
        objectAnimator.setInterpolator(new BounceInterpolator());
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animation.setStartDelay(1000);
                animation.start();
            }
        });
        return objectAnimator;
    }

    private void getPersonalData() {
        if (pageCount == 0) {
            persoalPostList.clear();
            if (personalAdapter != null) {
                personalAdapter.notifyDataSetChanged();
            }
        }
        binding.shimmerLay.setVisibility(View.VISIBLE);
        binding.shimmerLay.startShimmer();
        homeViewModel.getDailyPosts(id, Functions.getSharedPreference(context).getString(Variables.SELCT_LANGUAGE, ""), "", pageCount).observe(getViewLifecycleOwner(), new Observer<HomeResponse>() {
            @Override
            public void onChanged(HomeResponse homeResponse) {
                binding.personalRefreshLay.setRefreshing(false);
                binding.shimmerLay.setVisibility(View.GONE);
                binding.shimmerLay.stopShimmer();
                Functions.cancelLoader();
                if (homeResponse != null) {
                    if (homeResponse.daily_post != null) {
                        persoalPostList.addAll(homeResponse.daily_post);
                        if (homeResponse.daily_post.size() > 0) {
                            if (personalAdapter != null) {
                                personalAdapter.notifyDataSetChanged();
                            } else {
                                setPersonalAdapter();
                            }
                        }
                    }

                    if (homeResponse.custom_category != null && homeResponse.custom_category.size() > 0) {
                        if (!(personalCateList.size() > 0)) {
                            personalCateList.addAll(homeResponse.custom_category);
                            List<String> titleList = new ArrayList<>();
                            titleList.add("Today");
                            for (int i = 0; i < personalCateList.size(); i++) {
                                titleList.add(personalCateList.get(i).getName());
                            }
                            binding.personalCatRecy.setAdapter(new TabRecyclerAdapter(context, titleList, new AdapterClickListener() {
                                @Override
                                public void onItemClick(View view, int pos, Object object) {
                                    pageCount = 0;
                                    if (pos == 0) {
                                        id = "daily";
                                        getPersonalData();
                                    } else {
                                        id = personalCateList.get(pos - 1).getId();
                                        getPersonalData();
                                    }
                                }
                            }));
                        }
                    }
                } else {
                    Toast.makeText(context, "null", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setPersonalAdapter() {
        personalAdapter = new PersonalPosterVAdapter(context, persoalPostList, new PersonalPosterVAdapter.OnClickEvent() {
            @Override
            public void onClick(View view, View posterew, PostsModel postItem) {
                currentView = posterew;
                int viewId = view.getId();
                if (viewId == R.id.removeWatermark) {
                    showPremiumFragment();
                } else if (viewId == R.id.downloadBtn) {
                    if (takePermissionUtils.isStorageCameraPermissionGranted()) {
                        currentView.findViewById(R.id.colorsLay).setVisibility(View.GONE);
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
                } else if (viewId == R.id.share_Btn) {
                    if (takePermissionUtils.isStorageCameraPermissionGranted()) {
                        currentView.findViewById(R.id.colorsLay).setVisibility(View.GONE);
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
                } else if (viewId == R.id.edit_Btn) {
                    startActivity(new Intent(context, EditProfileActivity.class));
                } else if (viewId == R.id.next_Btn) {
                    try {
                        binding.personalRecy.setCurrentItem(binding.personalRecy.getCurrentItem() + 1);
                    } catch (Exception e) {
                    }
                }
            }
        });
        binding.personalRecy.setAdapter(personalAdapter);
    }

    private void saveImage(Bitmap bitmap, String type) {
        Functions.showLoader(context);
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
                    Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(),
                            bitmap.getHeight(), bitmap.getConfig());
                    Canvas canvas = new Canvas(createBitmap);
                    canvas.drawColor(-1);
                    canvas.drawBitmap(bitmap, 0.0f, 0.0f, null);
                    createBitmap.compress(Bitmap.CompressFormat.PNG,
                            100, fileOutputStream);
                    createBitmap.recycle();
                    fileOutputStream.flush();
                    fileOutputStream.close();

                    MediaScannerConnection.scanFile(context, new String[]{file2.getAbsolutePath()},
                            null, new MediaScannerConnection.OnScanCompletedListener() {
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
                    Functions.showToast(getActivity(), getString(R.string.image_saved));
                } else {
                    shareFileImageUri(getImageContentUri(new File(filePath)), "", type);
                }
            } else {
                Functions.showToast(getActivity(), getString(R.string.error));
            }
            Functions.cancelLoader();
        }
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

    public Uri getImageContentUri(File imageFile) {
        return Uri.parse(imageFile.getAbsolutePath());
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.forYouTv.setText(getString(R.string.for_you) + " (" + Functions.getSharedPreference(context).getString(Variables.ACTIVE_PROFILE_CATEGORY_NAME, "") + ")");
        binding.activeBusinessName.setText(Functions.getSharedPreference(context).getString(Variables.ACTIVE_PROFILE_NAME, getString(R.string.add_bussiness)));
    }

    private void showLanguageDialog() {
        new LanguageDialogFragment(new Callback() {
            @Override
            public void Responce(String resp) {
                getHomeData();
            }
        }).show(getFragmentManager(), "");
    }

    private void showContactFragment() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);
        transaction.addToBackStack(null);
        transaction.replace(android.R.id.content, new ContactFragment()).commit();
    }

    private void showPremiumFragment() {
        startActivity(new Intent(getContext(), PremiumActivity.class));
    }

    private void getHomeData() {
        cotegoryID = Functions.getSharedPreference(context).getString(Variables.ACTIVE_PROFILE_CATEGORY_ID, "");
        binding.businessRefreshLay.setVisibility(View.GONE);
        binding.shimmerLay.setVisibility(View.VISIBLE);
        binding.shimmerLay.startShimmer();
        homeViewModel.getData(Functions.getUID(context), Functions.getSharedPreference(context).getString(Variables.SELCT_LANGUAGE, ""), cotegoryID).observe(getViewLifecycleOwner(), new Observer<HomeResponse>() {
            @Override
            public void onChanged(HomeResponse homeResponse) {
                binding.businessRefreshLay.setRefreshing(false);
                if (homeResponse != null) {
                    if (homeResponse.sliderdata.size() > 0) {
                        setSliderAdapter(homeResponse.sliderdata);
                    }
                    if (homeResponse.upcoming_event.size() > 0) {
                        setUpcomingCategoryAdapter(homeResponse.upcoming_event);
                    } else {
                        binding.upcomingLay.setVisibility(View.GONE);
                    }
                    if (homeResponse.video_tamplate_category.size() > 0) {
                        setVideoTemplateAdapter(homeResponse.video_tamplate_category);
                    } else {
                        binding.videoTamplateLay.setVisibility(View.GONE);
                    }
                    if (homeResponse.section.size() > 0) {
                        setSectionAdapter(homeResponse.section);
                    }
                    if (homeResponse.custom_category.size() > 0) {
                        setFestivalCategoryAdapter(homeResponse.custom_category);
                    }
                    if (homeResponse.business_category.size() > 0) {
                        setBussinessCategoryAdapter(homeResponse.business_category);
                    }
                    if (homeResponse.political_category.size() > 0) {
                        setPoliticalCategoryAdapter(homeResponse.political_category);
                    }
                    if (homeResponse.recent.size() > 0) {
                        setPostsAdapter(homeResponse.recent);
                    }

                    if (homeResponse.customTamplateCategory.size() > 0) {
                        setCusTamCateAdapter(homeResponse.customTamplateCategory);
                    }
                    if (homeResponse.offerdialog != null) {
                        if (firsttime) {
                            showOfferDialog(homeResponse.offerdialog);
                        }
                    }

                    if (homeResponse.foryou != null && homeResponse.foryou.size() > 0) {
                        setForYouAdapter(homeResponse.foryou);
                    } else {
                        binding.forYouLay.setVisibility(View.GONE);
                    }
                    binding.shimmerLay.stopShimmer();
                    binding.businessRefreshLay.setVisibility(View.VISIBLE);
                    binding.shimmerLay.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setCusTamCateAdapter(List<InvitationCategoryModel> list) {
        binding.tamplateCateRrecylerview.setAdapter(new CustomTamplateCategoryAdapter(context, list, new AdapterClickListener() {
            @Override
            public void onItemClick(View view, int pos, Object object) {
                InvitationCategoryModel model = (InvitationCategoryModel) object;
                startActivity(new Intent(context, CustomTamplateActivity.class)
                        .putExtra("category_id", model.getId())
                        .putExtra("name", model.getName()));
            }
        }));
    }

    private void showOfferDialog(OfferDialogModel offerdialog) {
        firsttime = false;
        Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setAttributes(getLayoutParams(dialog));
        dialog.setContentView(R.layout.offerdialog);
        ImageView imageView = dialog.findViewById(R.id.dialog_image);
        ImageView close_btn = dialog.findViewById(R.id.close_btn);
        BindingAdaptet.setImageUrl(imageView, offerdialog.item_url);
        dialog.setCancelable(false);
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (offerdialog.action.equals(Constants.URL)) {
                    Intent intent = new Intent(context, WebviewA.class);
                    intent.putExtra("url", offerdialog.action_item);
                    intent.putExtra("title", getString(R.string.app_name));
                    startActivity(intent);
                } else if (offerdialog.action.equals(Constants.SUBSCRIPTION)) {
                    showPremiumFragment();
                } else if (offerdialog.action.equals(Constants.CATEGORY)) {
                    context.startActivity(new Intent(context, OpenPostActivity.class).putExtra("title", getString(R.string.app_name)).putExtra("type", "category").putExtra("item_id", offerdialog.action_item));
                }
            }
        });
        dialog.show();
    }

    public void setSliderAdapter(List<SliderModel> sliderdata) {

        for (int i = 0; i < sliderdata.size(); i++) {
            if (sliderdata.get(i).slider.equals("1")) {
                slider1List.add(sliderdata.get(i));
            }
            if (sliderdata.get(i).slider.equals("2")) {
                slider2List.add(sliderdata.get(i));
            }
            if (sliderdata.get(i).slider.equals("3")) {
                slider3List.add(sliderdata.get(i));
            }
        }


        if (slider1List.size() > 0) {
            setSlider1();
            binding.topLayout.setVisibility(View.VISIBLE);
        } else {
            binding.topLayout.setVisibility(View.GONE);
        }
        if (slider2List.size() > 0) {
            setSlider2();
            binding.slider2Lay.setVisibility(View.VISIBLE);
        } else {
            binding.slider2Lay.setVisibility(View.GONE);
        }
        if (slider3List.size() > 0) {
            setSlider3();
            binding.slider3Lay.setVisibility(View.VISIBLE);
        } else {
            binding.slider3Lay.setVisibility(View.GONE);
        }

    }

    private void setSlider1() {
        SliderPagerAdapter sliderAdapter1Recycler = new SliderPagerAdapter(getActivity(), slider1List, new AdapterClickListener() {
            @Override
            public void onItemClick(View view, int pos, Object object) {
                SliderModel sliderdata = (SliderModel) object;
                if (sliderdata.action.equals(Constants.CATEGORY)) {
                    context.startActivity(new Intent(context, OpenPostActivity.class).putExtra("title", getString(R.string.app_name)).putExtra("type", "category").putExtra("item_id", sliderdata.action_item));
                } else if (sliderdata.action.equals(Constants.SUBSCRIPTION)) {
                    showPremiumFragment();
                } else if (sliderdata.action.equals(Constants.URL)) {
                    Intent intent = new Intent(context, WebviewA.class);
                    intent.putExtra("url", sliderdata.action_item);
                    intent.putExtra("title", getString(R.string.app_name));
                    startActivity(intent);
                }
            }
        });

        binding.slider1Pager.setPageTransformer(false, compositePageTransformer);
        int itemWidth = App.getScreenWidth();
        ViewGroup.LayoutParams params = binding.slider1Pager.getLayoutParams();
        if (params instanceof LinearLayout.LayoutParams) {
            // Handle LinearLayout-specific parameters
            LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) params;
            // ...
        } else if (params instanceof RelativeLayout.LayoutParams) {
            // Handle RelativeLayout-specific parameters
            RelativeLayout.LayoutParams relativeParams = (RelativeLayout.LayoutParams) params;
            // ...
        }
//        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) binding.slider1Pager.getLayoutParams();
        params.width = itemWidth;
        params.height = (int) (itemWidth * 0.45f);
        binding.slider1Pager.setLayoutParams(params);
        binding.slider1Pager.setAdapter(sliderAdapter1Recycler);
    }

    private void setSlider2() {
        SliderPagerAdapter sliderAdapter2Recycler = new SliderPagerAdapter(getActivity(), slider2List, new AdapterClickListener() {
            @Override
            public void onItemClick(View view, int pos, Object object) {
                SliderModel sliderdata = (SliderModel) object;
                if (sliderdata.action.equals(Constants.CATEGORY)) {
                    context.startActivity(new Intent(context, OpenPostActivity.class).putExtra("title", getString(R.string.app_name)).putExtra("type", "category").putExtra("item_id", sliderdata.action_item));
                } else if (sliderdata.action.equals(Constants.SUBSCRIPTION)) {
                    showPremiumFragment();
                } else if (sliderdata.action.equals(Constants.URL)) {
                    Intent intent = new Intent(context, WebviewA.class);
                    intent.putExtra("url", sliderdata.action_item);
                    intent.putExtra("title", getString(R.string.app_name));
                    startActivity(intent);
                }
            }
        });
        binding.slider2Pager.setPageTransformer(false, compositePageTransformer);
        int itemWidth = App.getScreenWidth();
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) binding.slider2Pager.getLayoutParams();
        params.width = itemWidth;
        params.height = (int) (itemWidth * 0.45f);
        binding.slider2Pager.setLayoutParams(params);
        binding.slider2Pager.setAdapter(sliderAdapter2Recycler);
    }

    private void setSlider3() {
        SliderPagerAdapter sliderAdapter3Recycler = new SliderPagerAdapter(getActivity(), slider3List, new AdapterClickListener() {
            @Override
            public void onItemClick(View view, int pos, Object object) {
                SliderModel sliderdata = (SliderModel) object;
                if (sliderdata.action.equals(Constants.CATEGORY)) {
                    context.startActivity(new Intent(context, OpenPostActivity.class).putExtra("title", getString(R.string.app_name)).putExtra("type", "category").putExtra("item_id", sliderdata.action_item));
                } else if (sliderdata.action.equals(Constants.SUBSCRIPTION)) {
                    showPremiumFragment();
                } else if (sliderdata.action.equals(Constants.URL)) {
                    Intent intent = new Intent(context, WebviewA.class);
                    intent.putExtra("url", sliderdata.action_item);
                    intent.putExtra("title", getString(R.string.app_name));
                    startActivity(intent);
                }
            }
        });
        binding.slider3Pager.setPageTransformer(false, compositePageTransformer);
        int itemWidth = App.getScreenWidth();
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) binding.slider3Pager.getLayoutParams();
        params.width = itemWidth;
        params.height = (int) (itemWidth * 0.45f);
        binding.slider3Pager.setLayoutParams(params);
        binding.slider3Pager.setAdapter(sliderAdapter3Recycler);
    }

    private void setForYouAdapter(List<PostsModel> foryou) {
        binding.forYouRecylerview.setAdapter(new HomeSectionAdapter.HorizontalAdapter(context, 0, foryou, new HomeSectionAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, List<PostsModel> postsModels, int main_position, int child_position) {
                        startActivity(new Intent(context, OpenPostActivity.class)
                                .putExtra("title", "For You")
                                .putExtra("type", "category")
                                .putExtra("item_id", postsModels.get(child_position).category_id)
                                .putExtra("model", postsModels.get(child_position)));
                    }
                })
        );
    }

    private void setVideoTemplateAdapter(List<VideoTamplateCategory> posts) {
        binding.videoTamplateRecylerview.setAdapter(new VideoTamplateHomeAdapter(context, posts, new AdapterClickListener() {
            @Override
            public void onItemClick(View view, int pos, Object object) {
                VideoTamplateCategory model = (VideoTamplateCategory) object;
                PlayAnimatedVideActivity.selectedCategory = model;
                startActivity(new Intent(context, PlayAnimatedVideActivity.class));
            }
        }));
    }

    private void setFestivalCategoryAdapter(List<CategoryModel> festival_category) {
        binding.rvCategory.setAdapter(new FestivalCategoryAdapter(context, festival_category));
    }

    private void setBussinessCategoryAdapter(List<CategoryModel> business_category) {
        binding.rvBussinessCategory.setAdapter(new BussinessCategoryAdapter(context, business_category));
    }

    private void setPoliticalCategoryAdapter(List<CategoryModel> political_category) {
        binding.politicalRecylerview.setAdapter(new PoliticalCategoryAdapter(context, political_category));
    }

    private void setUpcomingCategoryAdapter(List<CategoryModel> upcoming_event) {
        binding.rvUpcomingFestival.setAdapter(new UpcomingEventAdapter(context, upcoming_event));
    }

    private void setSectionAdapter(List<SectionModel> section) {
        binding.rvSection.setAdapter(new HomeSectionAdapter(context, section, new HomeSectionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, List<PostsModel> list, int main_position, int child_position) {

                startActivity(new Intent(context, OpenPostActivity.class)
                        .putExtra("title", section.get(main_position).getName())
                        .putExtra("type", "section")
                        .putExtra("item_id", list.get(child_position).section_id)
                        .putExtra("model", list.get(child_position)));

            }
        }));
    }

    private void setPostsAdapter(List<PostsModel> recent) {
        binding.rvNewest.setAdapter(new PostsAdapter(context, recent, new PostsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, PostsModel postsModels, int main_position) {
                startActivity(new Intent(context, OpenPostActivity.class)
                        .putExtra("title", "Recent")
                        .putExtra("type", "images")
                        .putExtra("item_id", postsModels.category_id)
                        .putExtra("model", postsModels));
            }
        }, 3, getResources().getDimension(R.dimen._2sdp)));
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
}