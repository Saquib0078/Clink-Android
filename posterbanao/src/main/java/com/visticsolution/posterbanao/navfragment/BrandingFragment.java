package com.visticsolution.posterbanao.navfragment;

import static com.visticsolution.posterbanao.classes.Functions.getSharedPreference;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.visticsolution.posterbanao.MainActivity;
import com.visticsolution.posterbanao.PremiumActivity;
import com.visticsolution.posterbanao.R;
import com.visticsolution.posterbanao.adapter.BusinessCardDigitalAdapter;
import com.visticsolution.posterbanao.adapter.BusinessCardTamplateAdapter;
import com.visticsolution.posterbanao.adapter.ServicesAdapter;
import com.visticsolution.posterbanao.ads.RewardedAdapter;
import com.visticsolution.posterbanao.classes.Constants;
import com.visticsolution.posterbanao.classes.Functions;
import com.visticsolution.posterbanao.classes.PermissionUtils;
import com.visticsolution.posterbanao.classes.Variables;
import com.visticsolution.posterbanao.databinding.FragmentBrandingBinding;
import com.visticsolution.posterbanao.dialog.CallBack;
import com.visticsolution.posterbanao.dialog.InquiryFragment;
import com.visticsolution.posterbanao.dialog.PaymentTypeDialogFragment;
import com.visticsolution.posterbanao.dialog.RemoveWatermarkDialog;
import com.visticsolution.posterbanao.fragments.MyBussinessFragment;
import com.visticsolution.posterbanao.ServiceDetailActivity;
import com.visticsolution.posterbanao.interfaces.AdapterClickListener;
import com.visticsolution.posterbanao.model.BusinessCardModel;
import com.visticsolution.posterbanao.model.ServicesModel;
import com.visticsolution.posterbanao.network.ApiClient;
import com.visticsolution.posterbanao.network.ApiService;
import com.visticsolution.posterbanao.payment.InstamojoActivity;
import com.visticsolution.posterbanao.payment.PaytmActivity;
import com.visticsolution.posterbanao.payment.RazorpayActivity;
import com.visticsolution.posterbanao.payment.StripeActivity;
import com.visticsolution.posterbanao.payment.CCAvenueActivity;
import com.visticsolution.posterbanao.responses.HomeResponse;
import com.visticsolution.posterbanao.responses.SimpleResponse;
import com.visticsolution.posterbanao.viewmodel.HomeViewModel;

import java.io.File;
import java.util.Map;

import retrofit2.Call;

public class BrandingFragment extends Fragment {

    public BrandingFragment() {
        // Required empty public constructor
    }

    FragmentBrandingBinding binding;
    HomeViewModel homeViewModel;
    Activity context;
    PermissionUtils takePermissionUtils;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBrandingBinding.inflate(getLayoutInflater());
        Functions.fadeIn(binding.getRoot(), getContext());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        takePermissionUtils = new PermissionUtils(getActivity(), mPermissionResult);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.getBusinessCards().observe(getActivity(), new Observer<HomeResponse>() {
            @Override
            public void onChanged(HomeResponse homeResponse) {
                if (homeResponse != null && homeResponse.getBusinesscardtamplate() != null){
                    binding.recycler.setAdapter(new BusinessCardTamplateAdapter(getContext(),homeResponse.getBusinesscardtamplate()));
                }
                if (homeResponse != null && homeResponse.getBusinesscarddigital() != null){
                    binding.digitalRecycler.setAdapter(new BusinessCardDigitalAdapter(getContext(), homeResponse.getBusinesscarddigital(), new AdapterClickListener() {
                        @Override
                        public void onItemClick(View view, int pos, Object object) {
                            BusinessCardModel model = (BusinessCardModel) object;
                            if (model.getPremium().equals("1") && !Functions.IsPremiumEnable(context)){
                                showPremiumFragment(model);
                            }else{
                                if (!getSharedPreference(context).getString(Variables.BUSSINESS_NAME,"").equals("")){
                                    createBusinessCard(model);
                                }else{
                                    Functions.showToast(context,getString(R.string.please_select_bussiness));
                                }
                            }
                        }
                    }));
                }
            }
        });

        binding.activeBusinessName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MyBussinessFragment().show(getChildFragmentManager(),"");
            }
        });

        homeViewModel.getServices().observe(getActivity(), new Observer<HomeResponse>() {
            @Override
            public void onChanged(HomeResponse homeResponse) {
                binding.shimmerLay.setVisibility(View.GONE);
                binding.scrollView.setVisibility(View.VISIBLE);
                if (homeResponse != null && homeResponse.getServices().size() > 0){
                    binding.servicesRecycler.setAdapter(new ServicesAdapter(context, homeResponse.getServices(), new AdapterClickListener() {
                        @Override
                        public void onItemClick(View view, int pos, Object object) {
                            ServicesModel model = (ServicesModel) object;
                            if (view.getId() == R.id.apply_btn) {
                                InquiryFragment.model = model;
                                new InquiryFragment().show(getChildFragmentManager(), "");
                            } else {
                                showDetailFragment(model);
                            }
                        }
                    }));
                }
            }
        });
    }

    private final ActivityResultLauncher<String[]> mPermissionResult = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onActivityResult(Map<String, Boolean> result) {
                }
            });

    private void showDetailFragment(ServicesModel model) {
        ServiceDetailActivity.model = model;
        startActivity(new Intent(context,ServiceDetailActivity.class));
    }

    private void createBusinessCard(BusinessCardModel model) {
        Functions.showLoader(context);
        ApiClient.getRetrofit().create(ApiService.class).createBusinessCard(Constants.API_KEY,model.getId(),Functions.getSharedPreference(context).getString(Variables.BUSSINESS_ID,"")).enqueue(new retrofit2.Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, retrofit2.Response<SimpleResponse> response) {
                Functions.cancelLoader();
                if (response.body().code == 200){
                    if (takePermissionUtils.isStorageCameraPermissionGranted()) {
                        DownloadPDF(response.body().message);
                    } else {
                        takePermissionUtils.takeStorageCameraPermission();
                    }
                }else {
                    Functions.showToast(context,response.body().message);
                }
            }
            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {

            }
        });
    }

    private void DownloadPDF(String message) {
        Functions.showLoader(context);
        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
        String filename = getSharedPreference(context).getString(Variables.BUSSINESS_NAME,"b_")+System.currentTimeMillis()+".pdf";
        AndroidNetworking.download(message, directory.getAbsolutePath(), filename).build().startDownload(new DownloadListener() {
            public void onDownloadComplete() {
                Functions.showToast(context,getString(R.string.download_complete));
                Functions.cancelLoader();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(message)));
//                startActivity(new Intent(context, WebviewA.class).putExtra("url","https://docs.google.com/viewer?embedded=true&url="+message).putExtra("title","Business Card"));
            }

            public void onError(ANError aNError) {
                Functions.cancelLoader();
                Functions.showToast(context,aNError.getErrorDetail());
            }
        });
    }

    BusinessCardModel selectedModel;
    private void showPremiumFragment(BusinessCardModel model) {
        selectedModel = model;
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
                            Intent intent = new Intent(context, PaytmActivity.class);
                            intent.putExtra("price", Functions.getSharedPreference(context).getString("single_post_subsciption_amount","10"));
                            resultCallbackForPayment.launch(intent);
                        } else if (requestType.equals(Variables.RAZORPAY)){
                            Intent intent = new Intent(context, RazorpayActivity.class);
                            intent.putExtra("price", Functions.getSharedPreference(context).getString("single_post_subsciption_amount","10"));
                            resultCallbackForPayment.launch(intent);
                        }else if (requestType.equals(Variables.INSTAMOJO)){
                            Intent intent = new Intent(context, InstamojoActivity.class);
                            intent.putExtra("price", Functions.getSharedPreference(context).getString("single_post_subsciption_amount","10"));
                            resultCallbackForPayment.launch(intent);
                        }else if (requestType.equals(Variables.CCAVENUE)){
                            Intent intent = new Intent(context, CCAvenueActivity.class);
                            intent.putExtra("price", Functions.getSharedPreference(context).getString("single_post_subsciption_amount","10"));
                            resultCallbackForPayment.launch(intent);
                        }else if (requestType.equals(Variables.STRIPE)){
                            Intent intent = new Intent(context, StripeActivity.class);
                            intent.putExtra("price", Functions.getSharedPreference(context).getString("single_post_subsciption_amount","10"));
                            resultCallbackForPayment.launch(intent);
                        }
                    }
                }).show(getChildFragmentManager(),"tttt");
            }

            @Override
            public void goPremium() {
                startActivity(new Intent(context, PremiumActivity.class));
            }
        }).show(getChildFragmentManager(),"");
    }


    boolean rewardGranted = false;

    private void loadVideoAd() {
        Functions.showLoader(context);
        new RewardedAdapter(context, new RewardedAdapter.Listener() {
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
                    createBusinessCard(selectedModel);
                }
            }
        });
        RewardedAdapter.LoadAds();
    }

    ActivityResultLauncher<Intent> resultCallbackForPayment = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (Functions.isLogin(context)){
                            createBusinessCard(selectedModel);
                        }else {
                            startActivity(new Intent(context, MainActivity.class));
                        }
                    }
                }
            });

    @Override
    public void onResume() {
        super.onResume();
        binding.activeBusinessName.setText(Functions.getSharedPreference(context).getString(Variables.BUSSINESS_NAME,getString(R.string.add_bussiness)));
    }
}