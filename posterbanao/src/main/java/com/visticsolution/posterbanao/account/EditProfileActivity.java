package com.visticsolution.posterbanao.account;

import static com.visticsolution.posterbanao.classes.Constants.SUCCESS;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.visticsolution.posterbanao.adapter.SelectCategoryAdapter;
import com.visticsolution.posterbanao.databinding.ActivityEditProfileBinding;
import com.visticsolution.posterbanao.dialog.PickedImageActionFragment;
import com.visticsolution.posterbanao.dialog.SelectCategoryFragment;
import com.visticsolution.posterbanao.editor.StateDistArray;
import com.visticsolution.posterbanao.editor.utils.MyUtils;
import com.visticsolution.posterbanao.model.CategoryModel;
import com.visticsolution.posterbanao.model.UserModel;
import com.visticsolution.posterbanao.binding.BindingAdaptet;
import com.visticsolution.posterbanao.dialog.CustomeDialogFragment;
import com.visticsolution.posterbanao.dialog.DialogType;
import com.visticsolution.posterbanao.dialog.PickFromFragment;
import com.visticsolution.posterbanao.responses.UserResponse;
import com.visticsolution.posterbanao.viewmodel.UserViewModel;

import com.visticsolution.posterbanao.classes.Functions;
import com.visticsolution.posterbanao.classes.PermissionUtils;
import com.visticsolution.posterbanao.classes.Variables;
import com.visticsolution.posterbanao.IntroActivity;
import com.visticsolution.posterbanao.R;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {


    ActivityEditProfileBinding binding;
    PermissionUtils takePermissionUtils;
    UserViewModel userViewModel;
    Activity context;

    String state = "";
    String district = "";
    String referCode = "";
    String categoryID = "";
    boolean fromLogin = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fromLogin = getIntent().getBooleanExtra("fromlogin",false);
        context = this;
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        binding.usernameEdit.setText(Functions.getSharedPreference(context).getString(Variables.NAME, ""));
        binding.emailEdit.setText(Functions.getSharedPreference(context).getString(Variables.U_EMAIL, ""));
        binding.designationEdit.setText(Functions.getSharedPreference(context).getString(Variables.U_DESIGNATION, ""));

        categoryID = Functions.getSharedPreference(context).getString(Variables.CATEGORY_ID, "");
        state = Functions.getSharedPreference(context).getString(Variables.STATE, "");
        district = Functions.getSharedPreference(context).getString(Variables.DISTRICT, "");

        if (fromLogin){
            binding.titleTv.setText(getString(R.string.update_personal_profile));
        }

        if (!Functions.getSharedPreference(context).getString(Variables.NUMBER, "").equals("null")) {
            binding.numberEdit.setText(Functions.getSharedPreference(context).getString(Variables.NUMBER, ""));
        }

//        if (Functions.getSharedPreference(context).getString(Variables.SOCIAL, "").equals("phone")) {
//            binding.numberEdit.setEnabled(false);
//        }
//
//        if (Functions.getSharedPreference(context).getString(Variables.SOCIAL, "").equals("whatsapp")) {
//            binding.numberEdit.setEnabled(false);
//        }
//
//        if (Functions.getSharedPreference(context).getString(Variables.SOCIAL, "").equals("google")) {
//            binding.emailEdit.setEnabled(false);
//        }

        if (Functions.getSharedPreference(context).getString("refer_earn","false").equals("false")){
            binding.referCodeLay.setVisibility(View.GONE);
        }

        if (!Functions.getSharedPreference(context).getString(Variables.REFERD, "").equals("")) {
            binding.referCodeLay.setVisibility(View.GONE);
        }

        if (!Functions.getSharedPreference(context).getString(Variables.CATEGORY_ID, "").equals("0")){
            userViewModel.getUserCategory(Functions.getSharedPreference(context).getString(Variables.CATEGORY_ID, "")).observe(this,new Observer<UserResponse>() {
                @Override
                public void onChanged(UserResponse userResponse) {
                    if (userResponse.usercategory != null){
                        binding.categoryNameTv.setText(userResponse.usercategory.getName());
                        BindingAdaptet.setImageUrl(binding.categoryPic,Functions.getItemBaseUrl(userResponse.usercategory.getImage()));
                    }
                }
            });
        }

        if(Functions.getSharedPreference(context).getString("refer_earn","").equals("false")){
            binding.referCodeLay.setVisibility(View.GONE);
        }

        binding.referBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                referCode = binding.referCodeEdit.getText().toString();
                if (referCode.length() > 6){
                    Functions.showLoader(context);
                    userViewModel.cheakReferCode(referCode).observe(EditProfileActivity.this, new Observer<UserResponse>() {
                        @Override
                        public void onChanged(UserResponse userResponse) {
                            Functions.cancelLoader();
                            if (userResponse.userModel != null){
                                if (!Functions.getUID(context).equals(userResponse.userModel.getId())){
                                    binding.referedUserName.setVisibility(View.VISIBLE);
                                    binding.referedUserName.setText(getString(R.string.refered_by)+" "+userResponse.userModel.getName());
                                }else {
                                    Functions.showToast(context,getString(R.string.please_enter_another_code));
                                }
                            }else {
                                binding.referedUserName.setVisibility(View.GONE);
                                binding.referedUserName.setText("");
                                Functions.showToast(context,getString(R.string.invalid_code));
                            }
                        }
                    });
                }else {
                    Functions.showToast(context,getString(R.string.invalid_code));
                }
            }
        });

        try {
            BindingAdaptet.setImageUrl(binding.userPic, Functions.getItemBaseUrl(Functions.getSharedPreference(context).getString(Variables.P_PIC, "")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Functions.getSharedPreference(context).getString(Variables.NAME, "").equals("")) {
            binding.backBtn.setVisibility(View.GONE);
        }

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        takePermissionUtils = new PermissionUtils(this, mPermissionResult);
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

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, StateDistArray.states);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.stateSpinner.setAdapter(spinnerArrayAdapter);
        binding.stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0){
                    state = adapterView.getSelectedItem().toString();
                    onStateSelect(adapterView.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.categorySelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCategoryFragment("");
            }
        });

        for (int position = 0; position < spinnerArrayAdapter.getCount(); position++) {
            if(spinnerArrayAdapter.getItem(position).equals(state)) {
                binding.stateSpinner.setSelection(position);
                return;
            }
        }

    }

    private void showCategoryFragment(String type) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);
        transaction.addToBackStack(null);
        transaction.replace(android.R.id.content, new SelectCategoryFragment(type, new SelectCategoryAdapter.OnCategorySelect() {
            @Override
            public void onSelect(CategoryModel model) {
                categoryID = model.getId();
                binding.categoryNameTv.setText(model.getName());
                BindingAdaptet.setImageUrl(binding.categoryPic,Functions.getItemBaseUrl(model.getImage()));
            }
        })).commit();
    }

    private void selectImage() {
        new PickFromFragment(getString(R.string.select_image), new PickFromFragment.DialogCallback() {
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
        }).show(getSupportFragmentManager(), "");
    }

    String imageFilePath;

    private void openCameraIntent() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = Functions.createImageFile(context);
                imageFilePath = photoFile.getAbsolutePath();
            } catch (Exception ex) {
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile( this, getPackageName() + ".fileprovider", photoFile);
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
        Uri fromFile = Uri.fromFile(new File(context.getCacheDir(), "SampleCropImage" + System.currentTimeMillis() + ".png"));
        UCrop.Options options2 = new UCrop.Options();
        options2.setCompressionFormat(Bitmap.CompressFormat.PNG);
        options2.setToolbarColor(getResources().getColor(R.color.backgroundColor));
        options2.setFreeStyleCropEnabled(true);
        UCrop.of(source, fromFile).withOptions(options2).start(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            new PickedImageActionFragment(0, MyUtils.getPathFromURI(context, UCrop.getOutput(data)), new PickedImageActionFragment.OnBitmapSelect() {
                @Override
                public void output(int id, String out) {
                    handleCrop(out);
                }
            }).show(getSupportFragmentManager(),"");
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }

    private void handleCrop(String imageFilePath) {
        File newfile = new File(Functions.getAppFolder(context)+"profile");
        if (!newfile.exists()){
            newfile.mkdir();
            newfile.mkdirs();
        }
        new File(Functions.getSharedPreference(this).getString("P_PATH","")).delete();

        Functions.showLoader(context);
        userViewModel.updateProfilePic(Functions.getUID(context), imageFilePath).observe(this, new Observer<UserResponse>() {
            @Override
            public void onChanged(UserResponse userResponse) {
                Functions.cancelLoader();
                try {
                    moveFile(new File(imageFilePath),newfile);
                } catch (IOException e) {
                    Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                Functions.getSharedPreference(context).edit().putString("P_PATH",newfile.getAbsolutePath()+"/"+new File(imageFilePath).getName()).apply();

                if (userResponse != null) {
                    if (userResponse.code == SUCCESS) {
                        BindingAdaptet.setImageUrl(binding.userPic, Functions.getItemBaseUrl(userResponse.getUserModel().getProfile_pic()));
                    } else {
                        Functions.showToast(context, userResponse.message);
                    }
                }
            }
        });
    }


    private void moveFile(File file, File dir) throws IOException {
        File newFile = new File(dir, file.getName());
        FileChannel outputChannel = null;
        FileChannel inputChannel = null;
        try {
            outputChannel = new FileOutputStream(newFile).getChannel();
            inputChannel = new FileInputStream(file).getChannel();
            inputChannel.transferTo(0, inputChannel.size(), outputChannel);
            inputChannel.close();
            file.delete();

            MediaScannerConnection.scanFile(this, new String[]{newFile.getAbsolutePath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String path, Uri uri) {

                        }
                    });
        }catch (Exception e){
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }finally {
            if (inputChannel != null) inputChannel.close();
            if (outputChannel != null) outputChannel.close();
        }

    }

    private ActivityResultLauncher<String[]> mPermissionResult = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onActivityResult(Map<String, Boolean> result) {
                    boolean allPermissionClear = true;
                    List<String> blockPermissionCheck = new ArrayList<>();
                    for (String key : result.keySet()) {
                        if (!(result.get(key))) {
                            allPermissionClear = false;
                            blockPermissionCheck.add(Functions.getPermissionStatus( context,key));
                        }
                    }
                    if (blockPermissionCheck.contains("blocked")) {
                        Functions.showPermissionSetting(context, getString(R.string.we_need_storage_and_camera_permission_for_upload_profile_pic));
                    } else if (allPermissionClear) {
                        selectImage();
                    }
                }
            });


    public void submitClick() {
        if (cheakValidation()) {
            Functions.showLoader(context);
            userViewModel.updateProfile(
                    Functions.getUID(context),
                    binding.usernameEdit.getText().toString(),
                    binding.emailEdit.getText().toString(),
                    binding.numberEdit.getText().toString(),
                    binding.designationEdit.getText().toString(),
                    referCode,
                    state,
                    district,
                    "0").observe(this, new Observer<UserResponse>() {
                @Override
                public void onChanged(UserResponse userResponse) {
                    Functions.cancelLoader();
                    if (userResponse != null) {
                        if (userResponse.code == SUCCESS) {
                            new CustomeDialogFragment(
                                    getString(R.string.sucsess),
                                    getString(R.string.profile_update_successfully),
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
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    saveData(userResponse.getUserModel());
                                                    finish();
                                                }
                                            });
                                        }
                                    }
                            ).show(getSupportFragmentManager(), "");
                        }
                    }
                }
            });
        }
    }

    private void saveData(UserModel userModel) {
        Functions.getSharedPreference(context).edit().putString(Variables.CATEGORY_NAME,binding.categoryNameTv.getText().toString()).apply();
        if (fromLogin) {
            Functions.saveUserData(userModel, context);
            Intent intent = new Intent(context, IntroActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            Functions.saveUserData(userModel, context);
            onBackPressed();
        }
    }

    private boolean cheakValidation() {
        if (TextUtils.isEmpty(binding.usernameEdit.getText().toString())) {
            binding.usernameEdit.setError(getString(R.string.enter_name));
            return false;
        }
        if (TextUtils.isEmpty(binding.emailEdit.getText().toString())) {
            binding.emailEdit.setError(getString(R.string.enter_email));
            return false;
        }
        if (TextUtils.isEmpty(binding.numberEdit.getText().toString())) {
            binding.numberEdit.setError(getString(R.string.enter_number));
            return false;
        }

        if (TextUtils.isEmpty(state)) {
            Functions.showToast(context,getString(R.string.please_select_state));
            return false;
        }

        if (TextUtils.isEmpty(district)) {
            Functions.showToast(context,getString(R.string.please_select_dist));
            return false;
        }

        return true;
    }

    String[] districtsList = null;

    private void onStateSelect(String state) {
        switch (state) {
            case "Andhra Pradesh":
                districtsList = StateDistArray.AndraPradesh;
                break;
            case "Arunachal Pradesh":
                districtsList = StateDistArray.ArunachalPradesh;
                break;
            case "Assam":
                districtsList = StateDistArray.Assam;
                break;
            case "Bihar":
                districtsList = StateDistArray.Bihar;
                break;
            case "Chhattisgarh":
                districtsList = StateDistArray.Chhattisgarh;
                break;
            case "Goa":
                districtsList = StateDistArray.Goa;
                break;
            case "Gujarat":
                districtsList = StateDistArray.Gujarat;
                break;
            case "Haryana":
                districtsList = StateDistArray.Haryana;
                break;
            case "Himachal Pradesh":
                districtsList = StateDistArray.HimachalPradesh;
                break;
            case "Jammu and Kashmir":
                districtsList = StateDistArray.JammuKashmir;
                break;
            case "Jharkhand":
                districtsList = StateDistArray.Jharkhand;
                break;
            case "Karnataka":
                districtsList = StateDistArray.Karnataka;
                break;
            case "Kerala":
                districtsList = StateDistArray.Kerala;
                break;
            case "Madhya Pradesh":
                districtsList = StateDistArray.MadhyaPradesh;
                break;
            case "Maharashtra":
                districtsList = StateDistArray.Maharashtra;
                break;
            case "Manipur":
                districtsList = StateDistArray.Manipur;
                break;
            case "Meghalaya":
                districtsList = StateDistArray.Meghalaya;
                break;
            case "Mizoram":
                districtsList = StateDistArray.Mizoram;
                break;
            case "Nagaland":
                districtsList = StateDistArray.Nagaland;
                break;
            case "Odisha":
                districtsList = StateDistArray.Odisha;
                break;
            case "Punjab":
                districtsList = StateDistArray.Punjab;
                break;
            case "Rajasthan":
                districtsList = StateDistArray.Rajasthan;
                break;
            case "Tamil Nadu":
                districtsList = StateDistArray.TamilNadu;
                break;
            case "Telangana":
                districtsList = StateDistArray.Telangana;
                break;
            case "Tripura":
                districtsList = StateDistArray.Tripura;
                break;
            case "Uttarakhand":
                districtsList = StateDistArray.Uttarakhand;
                break;
            case "Uttar Pradesh":
                districtsList = StateDistArray.UttarPradesh;
                break;
            case "West Bengal":
                districtsList = StateDistArray.WestBengal;
                break;
            case "Andaman and Nicobar Islands":
                districtsList = StateDistArray.AndamanNicobar;
                break;
            case "Chandigarh":
                districtsList = StateDistArray.AndamanNicobar;
                break;
            case "Dadra and Nagar Haveli":
                districtsList = StateDistArray.DadraHaveli;
                break;
            case "Daman and Diu":
                districtsList = StateDistArray.DamanDiu;
                break;
            case "Delhi":
                districtsList = StateDistArray.Delhi;
                break;
            case "Lakshadweep":
                districtsList = StateDistArray.Lakshadweep;
                break;
            case "Puducherry":
                districtsList = StateDistArray.Puducherry;
                break;
        }
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, districtsList);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.districtSpinner.setAdapter(spinnerArrayAdapter);
        binding.districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0){
                    district = adapterView.getSelectedItem().toString();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        for (int position = 0; position < spinnerArrayAdapter.getCount(); position++) {
            if(spinnerArrayAdapter.getItem(position).equals(district)) {
                binding.districtSpinner.setSelection(position);
                return;
            }
        }
    }

}