package com.visticsolution.posterbanao;

import static com.visticsolution.posterbanao.classes.Constants.SUCCESS;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.View;
import com.visticsolution.posterbanao.adapter.SubscriptionAdapter;
import com.visticsolution.posterbanao.classes.Functions;
import com.visticsolution.posterbanao.classes.Variables;
import com.visticsolution.posterbanao.databinding.ActivityPremiumBinding;

import com.visticsolution.posterbanao.dialog.CallBack;
import com.visticsolution.posterbanao.dialog.CustomeDialogFragment;
import com.visticsolution.posterbanao.dialog.DialogType;
import com.visticsolution.posterbanao.dialog.PaymentTypeDialogFragment;
import com.visticsolution.posterbanao.interfaces.AdapterClickListener;
import com.visticsolution.posterbanao.model.SubscriptionModel;
import com.visticsolution.posterbanao.payment.CCAvenueActivity;
import com.visticsolution.posterbanao.payment.InstamojoActivity;
import com.visticsolution.posterbanao.payment.OfflineActivity;
import com.visticsolution.posterbanao.payment.PaytmActivity;
import com.visticsolution.posterbanao.payment.RazorpayActivity;
import com.visticsolution.posterbanao.payment.StripeActivity;
import com.visticsolution.posterbanao.responses.HomeResponse;
import com.visticsolution.posterbanao.responses.UserResponse;
import com.visticsolution.posterbanao.viewmodel.HomeViewModel;
import com.visticsolution.posterbanao.viewmodel.UserViewModel;
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.ArrayList;
import java.util.List;

public class PremiumActivity extends AppCompatActivity {


    public PremiumActivity() {
    }

    ActivityPremiumBinding binding;
    String selected_id = "", selected_price = "", selected_name = "",promocode = "";
    Activity context;
    private InfiniteScrollAdapter<?> infiniteAdapter;

    UserViewModel userViewModel;
    HomeViewModel homeViewModel;
    SubscriptionAdapter adapter;
    List<SubscriptionModel> plan_list = new ArrayList<>();
    String type = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPremiumBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = this;

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        adapter = new SubscriptionAdapter(context, plan_list, new AdapterClickListener() {
            @Override
            public void onItemClick(View view, int pos, Object object) {
                SubscriptionModel model = (SubscriptionModel) object;
                selected_price = model.discount_price;
                selected_name = model.name;
                selected_id = model.id;
                startPayment(Integer.parseInt(selected_price));
            }
        });
        infiniteAdapter = InfiniteScrollAdapter.wrap(adapter);
        binding.recycler.setAdapter(infiniteAdapter);
        binding.recycler.setItemTransitionTimeMillis(150);
        binding.recycler.setSlideOnFling(true);
        binding.recycler.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .build());
        binding.mainLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.getSubscriptions().observe(this, new Observer<HomeResponse>() {
            @Override
            public void onChanged(HomeResponse homeResponse) {
                if (homeResponse != null){
                    if (homeResponse.subscriptions.size() > 0){
                        plan_list.addAll(homeResponse.subscriptions);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private void startPayment(int price) {
        selected_price = String.valueOf(price);
        new PaymentTypeDialogFragment(true, new CallBack() {
            @Override
            public void getResponse(String requestType,String promo, int discount) {
                if (discount > 0){
                    int dprice = (price*discount) / 100;
                    selected_price = String.valueOf(price-dprice);
                    promocode = promo;
                }
                if (selected_price.equals("0")){
                    updateSubscription("no_payment", "FREE");
                }else{
                    if (requestType.equals(Variables.PAYTM)) {
                        type = Variables.PAYTM;
                        Intent intent = new Intent(context, PaytmActivity.class);
                        intent.putExtra("price", selected_price);
                        resultCallbackForPayment.launch(intent);
                    } else if (requestType.equals(Variables.INSTAMOJO)){
                        type = Variables.INSTAMOJO;
                        Intent intent = new Intent(context, InstamojoActivity.class);
                        intent.putExtra("price", selected_price);
                        resultCallbackForPayment.launch(intent);
                    }else if (requestType.equals(Variables.RAZORPAY)){
                        type = Variables.RAZORPAY;
                        Intent intent = new Intent(context, RazorpayActivity.class);
                        intent.putExtra("price", selected_price);
                        resultCallbackForPayment.launch(intent);
                    }else if (requestType.equals(Variables.STRIPE)){
                        type = Variables.STRIPE;
                        Intent intent = new Intent(context, StripeActivity.class);
                        intent.putExtra("price", selected_price);
                        resultCallbackForPayment.launch(intent);
                    }else if (requestType.equals(Variables.CCAVENUE)){
                        type = Variables.CCAVENUE;
                        Intent intent = new Intent(context, CCAvenueActivity.class);
                        intent.putExtra("price", selected_price);
                        resultCallbackForPayment.launch(intent);
                    }else if (requestType.equals(Variables.OFFLINE)){
                        type = Variables.OFFLINE;
                        Intent intent = new Intent(context, OfflineActivity.class);
                        intent.putExtra("price", selected_price);
                        intent.putExtra("sid", selected_id);
                        intent.putExtra("promocode", promocode);
                        resultCallbackForOffline.launch(intent);
                    }
                }
            }
        }).show(getSupportFragmentManager(), "");
    }

    ActivityResultLauncher<Intent> resultCallbackForOffline = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        finish();
                    }
                }
            });

    ActivityResultLauncher<Intent> resultCallbackForPayment = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        updateSubscription(type, data.getStringExtra("transaction"));
                    }
                }
            });

    private void updateSubscription(String type, String tid) {
        Functions.showLoader(this);
        userViewModel.updateUserSubscription(Functions.getUID(context),type,selected_id,tid,promocode,selected_price).observe(this, new Observer<UserResponse>() {
            @Override
            public void onChanged(UserResponse userResponse) {
                Functions.cancelLoader();
                if (userResponse != null){
                    if (userResponse.code == SUCCESS){
                        Functions.saveUserData(userResponse.userModel,context);
                        new CustomeDialogFragment(
                                getString(R.string.sucsess),
                                userResponse.message,
                                DialogType.SUCCESS,
                                false,
                                false,
                                false,
                                new CustomeDialogFragment.DialogCallback() {
                                    @Override
                                    public void onCencel() {

                                    }
                                    @Override
                                    public void onSubmit() {

                                    }
                                    @Override
                                    public void onDismiss() {

                                    }
                                    @Override
                                    public void onComplete(Dialog dialog) {
                                        finish();
                                    }
                                }
                        ).show(getSupportFragmentManager(),"");
                    }else{
                        new CustomeDialogFragment(
                                getString(R.string.error),
                                userResponse.message,
                                DialogType.ERROR,
                                true,
                                false,
                                true,
                                new CustomeDialogFragment.DialogCallback() {
                                    @Override
                                    public void onCencel() {

                                    }
                                    @Override
                                    public void onSubmit() {
                                        updateSubscription(type,tid);
                                    }
                                    @Override
                                    public void onDismiss() {

                                    }
                                    @Override
                                    public void onComplete(Dialog dialog) {

                                    }
                                }
                        ).show(getSupportFragmentManager(),"");
                    }
                }
            }
        });
    }
}