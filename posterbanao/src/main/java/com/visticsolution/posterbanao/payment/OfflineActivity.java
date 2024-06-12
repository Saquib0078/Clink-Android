package com.visticsolution.posterbanao.payment;

import static com.visticsolution.posterbanao.classes.Constants.SUCCESS;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.visticsolution.posterbanao.R;
import com.visticsolution.posterbanao.binding.BindingAdaptet;
import com.visticsolution.posterbanao.classes.Functions;
import com.visticsolution.posterbanao.databinding.ActivityOfflineBinding;
import com.visticsolution.posterbanao.dialog.CustomeDialogFragment;
import com.visticsolution.posterbanao.dialog.DialogType;
import com.visticsolution.posterbanao.responses.UserResponse;
import com.visticsolution.posterbanao.viewmodel.UserViewModel;
import com.yalantis.ucrop.util.FileUtils;

public class OfflineActivity extends AppCompatActivity {

    ActivityOfflineBinding binding;
    Context context;
    String imagePath = "",sid,promocode,price;
    UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOfflineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = this;
        binding.backBtn.setOnClickListener(view -> {
            setResult(RESULT_CANCELED);
            finish();
        });

        sid = getIntent().getStringExtra("sid");
        promocode = getIntent().getStringExtra("promocode");
        price = getIntent().getStringExtra("price");

        binding.amountTv.setText("Pay " + getString(R.string.currency) + getIntent().getStringExtra("price"));
        binding.paymentDetailsTv.setText(Functions.getSharedPreference(context).getString("offline_details", "No details found"));

        BindingAdaptet.setImageUrl(binding.qrImage,Functions.getSharedPreference(context).getString("qr_image_url", "https://www.qrcodepress.com/wp-content/uploads/2014/09/QR-code-detective-when-not-to-use.jpg"));

        binding.selectImage.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            resultCallbackForGallery.launch(intent);
        });

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        binding.submitBtn.setOnClickListener(view -> {
            if (!imagePath.equals("")){
                Functions.showLoader(this);
                userViewModel.offlineSubscription(Functions.getUID(context),"offline",sid,imagePath,promocode,price).observe(this, new Observer<UserResponse>() {
                    @Override
                    public void onChanged(UserResponse userResponse) {
                        Functions.cancelLoader();
                        if (userResponse != null){
                            if (userResponse.code == SUCCESS){
                                Functions.saveUserData(userResponse.getUserModel(),context);
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
                                                dialog.dismiss();
                                                setResult(RESULT_OK);
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
                                                binding.submitBtn.performClick();
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
            }else{
                Functions.showToast(this,getString(R.string.please_select_scrshot));
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    ActivityResultLauncher<Intent> resultCallbackForGallery = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        Uri selectedImage = data.getData();
                        binding.imgView.setImageURI(selectedImage);
                        imagePath = FileUtils.getPath(context,selectedImage);
                    }
                }
            });
}