package com.visticsolution.posterbanao;

import static com.visticsolution.posterbanao.classes.Constants.SUCCESS;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.visticsolution.posterbanao.account.EditProfileActivity;
import com.visticsolution.posterbanao.adapter.LogosPagerAdapter;
import com.visticsolution.posterbanao.adapter.SelectCategoryAdapter;
import com.visticsolution.posterbanao.binding.BindingAdaptet;
import com.visticsolution.posterbanao.databinding.ActivityAddBussinessBinding;
import com.visticsolution.posterbanao.dialog.CustomeDialogFragment;
import com.visticsolution.posterbanao.dialog.DialogType;
import com.visticsolution.posterbanao.dialog.PickFromFragment;
import com.visticsolution.posterbanao.dialog.PickedImageActionFragment;
import com.visticsolution.posterbanao.dialog.SelectCategoryFragment;
import com.visticsolution.posterbanao.editor.utils.MyUtils;
import com.visticsolution.posterbanao.fragments.LogosFragment;
import com.visticsolution.posterbanao.model.CategoryModel;
import com.visticsolution.posterbanao.responses.UserResponse;
import com.visticsolution.posterbanao.viewmodel.UserViewModel;
import com.visticsolution.posterbanao.classes.Functions;
import com.visticsolution.posterbanao.classes.PermissionUtils;
import com.visticsolution.posterbanao.classes.Variables;
import com.visticsolution.posterbanao.model.BussinessModel;
import com.yalantis.ucrop.UCrop;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class AddBussinessActivity extends AppCompatActivity {

    String bussiness_id,categoryID = "";
    PermissionUtils takePermissionUtils;
    SharedPreferences sharedPreferences;
    ActivityAddBussinessBinding binding;
    UserViewModel userViewModel;
    Activity context;
    boolean fromLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBussinessBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = this;
        fromLogin = getIntent().getBooleanExtra("fromlogin",false);

        bussiness_id = getIntent().getStringExtra("business_id");
        takePermissionUtils=new PermissionUtils(this,mPermissionResult);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        sharedPreferences = getSharedPreferences(Variables.PREF_NAME, MODE_PRIVATE);
        if (bussiness_id != null && !Objects.equals(bussiness_id, "")){
            binding.titleTv.setText(getString(R.string.edit_bussiness));
            getBussinessDetails();
        }else {
            binding.titleTv.setText(getString(R.string.add_bussiness));
        }

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        binding.uploadPicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (takePermissionUtils.isStorageCameraPermissionGranted()) {
                    selectImage();
                } else {
                    takePermissionUtils.takeStorageCameraPermission();
                }
            }
        });
        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitClick();
            }
        });

        binding.categoryPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCategoryFragment("business");
            }
        });
        binding.createLogoBtn.setOnClickListener(view -> {
            if (binding.companyEdit.getText().toString().equals("")){
                Functions.showToast(context,getString(R.string.enter_bussiness_name));
                binding.companyEdit.setError(getString(R.string.enter_bussiness_name));
            }else{
                new LogosFragment(binding.companyEdit.getText().toString(), new LogosPagerAdapter.OnLogoSelect() {
                    @Override
                    public void sticker(String pth) {
                        path = pth;
                        Glide.with(context).load(path).error(R.drawable.placeholder).placeholder(R.drawable.placeholder).into(binding.bussinessPic);
                    }
                }).show(getSupportFragmentManager(),"");
            }
        });
    }

    private void showCategoryFragment(String type) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);
        transaction.addToBackStack(null);
        transaction.replace(android.R.id.content, new SelectCategoryFragment(type, new SelectCategoryAdapter.OnCategorySelect() {
            @Override
            public void onSelect(CategoryModel model) {
                categoryID = model.getId();
                binding.categoryName.setText(model.getName());
                BindingAdaptet.setImageUrl(binding.categoryPic,Functions.getItemBaseUrl(model.getImage()));
            }
        })).commit();
    }
    private void getBussinessDetails() {
        Functions.showLoader(context);
        userViewModel.getBusinessDetail(Functions.getUID(context),bussiness_id).observe(this, new Observer<UserResponse>() {
            @Override
            public void onChanged(UserResponse userResponse) {
                Functions.cancelLoader();
                if (userResponse != null){
                    if (userResponse.code == SUCCESS){
                        setData(userResponse.getBusiness());
                    }else{
                        new CustomeDialogFragment(
                                getString(R.string.alert),
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
                                        getBussinessDetails();
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

    private void setData(BussinessModel business) {
        try {
            BindingAdaptet.setImageUrl(binding.bussinessPic,business.getImage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        binding.companyEdit.setText(business.getCompany());
        binding.ownerEdit.setText(business.getName());
        binding.numberEdit.setText(business.getNumber());
        binding.designationEdit.setText(business.getDesignation());
        binding.emailEdit.setText(business.getEmail());
        binding.addressEdit.setText(business.getAddress());
        binding.websiteName.setText(business.getWebsite());
        binding.youtubeEdit.setText(business.getAddress());
        binding.youtubeEdit.setText(business.getYoutube());
        binding.instaEdit.setText(business.getInstagram());
        binding.aboutEdit.setText(business.getAbout());
        binding.whatsappEdit.setText(business.getWhatsapp());
        binding.twitterEdit.setText(business.getTwitter());
        binding.facebookEdit.setText(business.getFacebook());
        categoryID = business.getCategory_id();

        userViewModel.getUserCategory(business.getCategory_id()).observe(this,new Observer<UserResponse>() {
            @Override
            public void onChanged(UserResponse userResponse) {
                if (userResponse.usercategory != null){
                    binding.categoryName.setText(userResponse.usercategory.getName());
                    BindingAdaptet.setImageUrl(binding.categoryPic,Functions.getItemBaseUrl(userResponse.usercategory.getImage()));
                }
            }
        });
    }

    private void selectImage() {
        new PickFromFragment(getString(R.string.add_photo_), new PickFromFragment.DialogCallback() {
            @Override
            public void onCencel() {
            }
            @Override
            public void onGallery() {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                resultCallbackForGallery.launch(intent);
            }
            @Override
            public void onCamera() {
                openCameraIntent();
            }
        }).show(getSupportFragmentManager(),"");
    }

    String imageFilePath;
    private void openCameraIntent() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(context.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = Functions.createImageFile(context);
                imageFilePath = photoFile.getAbsolutePath();
            } catch (Exception ex) {
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                resultCallbackForCamera.launch(pictureIntent);
            }
        }
    }

    ActivityResultLauncher<Intent> resultCallbackForGallery = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        Uri selectedImage = data.getData();
                        beginCrop(selectedImage);

                    }
                }
            });

    ActivityResultLauncher<Intent> resultCallbackForCamera = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Uri selectedImage = (Uri.fromFile(new File(imageFilePath)));
                        beginCrop(selectedImage);
                    }
                }
            });

    private void beginCrop(Uri source) {
        Uri fromFile = Uri.fromFile(new File(getCacheDir(), "SampleCropImage" + System.currentTimeMillis() + ".png"));
        UCrop.Options options2 = new UCrop.Options();
        options2.setCompressionFormat(Bitmap.CompressFormat.PNG);
        options2.setToolbarColor(getResources().getColor(R.color.backgroundColor));
        options2.setFreeStyleCropEnabled(true);
        UCrop.of(source, fromFile).withOptions(options2).start(this);
    }

    String path = "";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            new PickedImageActionFragment(0, MyUtils.getPathFromURI(context, UCrop.getOutput(data)), new PickedImageActionFragment.OnBitmapSelect() {
                @Override
                public void output(int id, String out) {
                    Glide.with(context).load(out).error(R.drawable.placeholder).placeholder(R.drawable.placeholder).into(binding.bussinessPic);
                    path = out;
                }
            }).show(getSupportFragmentManager(),"");
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }

    private final ActivityResultLauncher<String[]> mPermissionResult = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onActivityResult(Map<String, Boolean> result) {

                    boolean allPermissionClear=true;
                    List<String> blockPermissionCheck=new ArrayList<>();
                    for (String key : result.keySet())
                    {
                        if (!(result.get(key)))
                        {
                            allPermissionClear=false;
                            blockPermissionCheck.add(Functions.getPermissionStatus(AddBussinessActivity.this,key));
                        }
                    }
                    if (blockPermissionCheck.contains("blocked"))
                    {
                        Functions.showPermissionSetting(AddBussinessActivity.this,getString(R.string.we_need_storage_and_camera_permission_for_upload_profile_pic));
                    }
                    else
                    if (allPermissionClear)
                    {
                        selectImage();
                    }

                }
            });


    public void submitClick() {
        if (cheakValidation()){
            Functions.showLoader(context);
            JSONObject parameters = new JSONObject();
            try {
                parameters.put("user_id", Functions.getSharedPreference(context).getString(Variables.U_ID, ""));
                parameters.put("id", bussiness_id);
                parameters.put("image", path);
                parameters.put("company", binding.companyEdit.getText().toString());
                parameters.put("name", binding.ownerEdit.getText().toString());
                parameters.put("number", binding.numberEdit.getText().toString());
                parameters.put("designation", binding.designationEdit.getText().toString());
                parameters.put("category_id", categoryID);
                parameters.put("email", binding.emailEdit.getText().toString());
                parameters.put("address", binding.addressEdit.getText().toString());
                parameters.put("website", binding.websiteName.getText().toString());
                parameters.put("youtube", binding.youtubeEdit.getText().toString());
                parameters.put("instagram", binding.instaEdit.getText().toString());
                parameters.put("about", binding.aboutEdit.getText().toString());
                parameters.put("facebook", binding.facebookEdit.getText().toString());
                parameters.put("twitter", binding.twitterEdit.getText().toString());
                parameters.put("whatsapp", binding.whatsappEdit.getText().toString());
                parameters.put("type", "business");
            } catch (Exception e) {
                e.printStackTrace();
            }
            userViewModel.addUserBussiness(parameters).observe(this, new Observer<UserResponse>() {
                @Override
                public void onChanged(UserResponse userResponse) {
                    Functions.cancelLoader();
                    if (userResponse != null){
                        if (userResponse.code == SUCCESS){
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
                                            getBussinessDetails();
                                        }
                                        @Override
                                        public void onDismiss() {
                                        }
                                        @Override
                                        public void onComplete(Dialog dialog) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Functions.saveBussinessData(userResponse.business,context);
                                                    if (fromLogin){
                                                        startActivity(new Intent(context, EditProfileActivity.class).putExtra("fromlogin",true));
                                                        finish();
                                                    }else{
                                                        onBackPressed();
                                                    }
                                                }
                                            });
                                        }
                                    }
                            ).show(getSupportFragmentManager(),"");
                        }else{
                            new CustomeDialogFragment(
                                    getString(R.string.alert),
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
                                            getBussinessDetails();
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

    private boolean cheakValidation() {
        if (categoryID.equals("")){
            Functions.showToast(context,getString(R.string.please_select_bussiness_category));
            return false;
        }
        if (bussiness_id == null && TextUtils.isEmpty(path)){
            Functions.showToast(context,getString(R.string.please_select_bussiness_logo));
            return false;
        }
        if (TextUtils.isEmpty(binding.companyEdit.getText().toString())){
            binding.companyEdit.setError(getString(R.string.enter_bussiness_name));
            return false;
        }
        if (TextUtils.isEmpty(binding.ownerEdit.getText().toString())){
            binding.companyEdit.setError(getString(R.string.enter_owner_name));
            return false;
        }
        if (TextUtils.isEmpty(binding.emailEdit.getText().toString())){
            binding.emailEdit.setError(getString(R.string.enter_bussiness_email));
            return false;
        }

        return true;
    }

}