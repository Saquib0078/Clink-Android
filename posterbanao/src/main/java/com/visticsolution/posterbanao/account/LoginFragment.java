//package com.visticsolution.posterbanao.account;
//
//import static android.content.Context.MODE_PRIVATE;
//
//import androidx.activity.result.ActivityResult;
//import androidx.activity.result.ActivityResultCallback;
//import androidx.activity.result.ActivityResultLauncher;
//import androidx.activity.result.contract.ActivityResultContracts;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentTransaction;
//import androidx.lifecycle.Observer;
//import androidx.lifecycle.ViewModelProvider;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.app.Dialog;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.os.CountDownTimer;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.view.animation.AnimationUtils;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import com.google.android.gms.auth.api.signin.GoogleSignIn;
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
//import com.google.android.gms.auth.api.signin.GoogleSignInClient;
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
//import com.google.android.gms.common.api.ApiException;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.FirebaseException;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.auth.PhoneAuthCredential;
//import com.google.firebase.auth.PhoneAuthProvider;
//import com.visticsolution.posterbanao.AddBussinessActivity;
//import com.visticsolution.posterbanao.AddPoliticalActivity;
//import com.visticsolution.posterbanao.HomeActivity;
//import com.visticsolution.posterbanao.classes.Constants;
//import com.visticsolution.posterbanao.databinding.FragmentLoginBinding;
//import com.visticsolution.posterbanao.dialog.CustomeDialogFragment;
//import com.visticsolution.posterbanao.dialog.DialogType;
//import com.visticsolution.posterbanao.fragments.ReferFragment;
//import com.visticsolution.posterbanao.model.UserModel;
//import com.visticsolution.posterbanao.network.ApiClient;
//import com.visticsolution.posterbanao.network.ApiService;
//import com.visticsolution.posterbanao.responses.UserResponse;
//import com.visticsolution.posterbanao.responses.WhatsappOtpResponse;
//import com.visticsolution.posterbanao.viewmodel.UserViewModel;
//import com.rilixtech.widget.countrycodepicker.Country;
//import com.rilixtech.widget.countrycodepicker.CountryCodePicker;
//import com.visticsolution.posterbanao.classes.Functions;
//import com.visticsolution.posterbanao.classes.Variables;
//import com.visticsolution.posterbanao.R;
//
//import org.json.JSONObject;
//
//import java.util.concurrent.TimeUnit;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class LoginFragment extends Fragment {
//
//    FirebaseAuth mAuth;
//    SharedPreferences sharedPreferences;
//    LoginModel loginmodel = new LoginModel();
//    Activity context;
//
//    UserViewModel userViewModel;
//    FragmentLoginBinding binding;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        binding = FragmentLoginBinding.inflate(getLayoutInflater());
//        return binding.getRoot();
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        context = getActivity();
//
//        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
//
//        mAuth = FirebaseAuth.getInstance();
//        sharedPreferences = context.getSharedPreferences(Variables.PREF_NAME, MODE_PRIVATE);
//
//
//        view.findViewById(R.id.google_btn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loginwithgoogle(v);
//            }
//        });
//
//        if (Functions.getSharedPreference(context).getString("whatsapp_otp","false").equals("true")){
//            binding.whatsappBtn.setVisibility(View.VISIBLE);
//        }else {
//            binding.whatsappBtn.setVisibility(View.GONE);
//        }
//
//        binding.phoneBtn.setOnClickListener(view1 -> {
//            PhoneLoginFragment comment_f = new PhoneLoginFragment();
//            FragmentTransaction transaction = getFragmentManager().beginTransaction();
//            transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);
//            transaction.addToBackStack(null);
//            transaction.replace(android.R.id.content, comment_f).commit();
//        });
//        binding.whatsappBtn.setOnClickListener(view1 -> {
//            WhatsappLoginFragment comment_f = new WhatsappLoginFragment();
//            FragmentTransaction transaction = getFragmentManager().beginTransaction();
//            transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);
//            transaction.addToBackStack(null);
//            transaction.replace(android.R.id.content, comment_f).commit();
//        });
//    }
//
//    GoogleSignInClient mGoogleSignInClient;
//    public void loginwithgoogle(View view) {
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .requestProfile()
//                .requestId()
//                .build();
//        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
//
//
//        try {
//            mGoogleSignInClient.signOut();
//        }catch (Exception e){}
//
//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);
//
//        if (account != null) {
//
//            String fname = "" + account.getGivenName();
//            String lname = "" + account.getFamilyName();
//
//            loginmodel = new LoginModel();
//            loginmodel.fname = Functions.removeSpecialChar(fname);
//            loginmodel.email = account.getEmail();
//            loginmodel.lname = Functions.removeSpecialChar(lname);
//            loginmodel.socailId = account.getId();
//            loginmodel.authTokon = account.getIdToken();
//            loginmodel.picture = account.getPhotoUrl().toString();
//
//            insertLoginData("" + account.getId(), "google");
//        } else {
//            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//            resultCallbackForGoogle.launch(signInIntent);
//        }
//    }
//
//    ActivityResultLauncher<Intent> resultCallbackForGoogle = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
//                @Override
//                public void onActivityResult(ActivityResult result) {
//                    if (result.getResultCode() == Activity.RESULT_OK) {
//                        Intent data = result.getData();
//                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//                        handleSignInResult(task);
//                    }
//                }
//            });
//
//    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
//        try {
//            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
//            if (account != null) {
//                String fname = "" + account.getGivenName();
//                String lname = "" + account.getFamilyName();
//
//                loginmodel = new LoginModel();
//                loginmodel.fname = Functions.removeSpecialChar(fname);
//                loginmodel.email = account.getEmail();
//                loginmodel.lname = Functions.removeSpecialChar(lname);
//                loginmodel.socailId = account.getId();
//                loginmodel.authTokon = account.getIdToken();
//                loginmodel.picture = ""+account.getPhotoUrl();
//
//                insertLoginData(""+account.getId(),"google");
//            }
//        } catch (ApiException e) {
//            Toast.makeText(context, "E -> "+e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void insertLoginData(String socialId, String social) {
//        Functions.showLoader(context);
//        if (!loginmodel.lname.equals("")){
//            loginmodel.fname = loginmodel.fname+" "+loginmodel.lname;
//        }
//
//        JSONObject parameters = new JSONObject();
//        try {
//            parameters.put("social_id", socialId);
//            parameters.put("social", "" + social);
//            parameters.put("auth_token", "" + loginmodel.authTokon);
//            parameters.put("email", "" + loginmodel.email);
//            parameters.put("number", "" + loginmodel.phoneNo);
//            parameters.put("profile_pic", "" + loginmodel.picture);
//            parameters.put("name", "" + loginmodel.fname);
//            parameters.put("device_token", Functions.getDeviceToken(context));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        userViewModel.login(parameters).observe(this, new Observer<UserResponse>() {
//            @Override
//            public void onChanged(UserResponse userResponse) {
//                if (userResponse != null){
//                    Functions.cancelLoader();
//                    if (userResponse.code == Constants.SUCCESS){
//                        parseLoginData(userResponse.userModel);
//                    }else {
//                        Functions.showToast(context,userResponse.message);
//                    }
//                }
//            }
//        });
//    }
//
//    public void parseLoginData(UserModel userModel) {
//        Functions.saveUserData(userModel, context);
//        if (userModel.getName() == null || userModel.getName().equals("null") ||
//                userModel.getState() == null || userModel.getState().equals("") ||
//                userModel.getDistrict() == null || userModel.getDistrict().equals("")){
//
//            startActivity(new Intent(context, EditProfileActivity.class).putExtra("fromlogin",true));
//
//        }else {
//            Intent intent=new Intent(context, HomeActivity.class);
//            startActivity(intent);
//            getActivity().finish();
//        }
//    }
//
//    private void showCategoryFragment(String type) {
//        if (type.equals("business")){
//            startActivity(new Intent(context, AddBussinessActivity.class).putExtra("fromlogin",true));
//        }else if (type.equals("political")){
//            startActivity(new Intent(context, AddPoliticalActivity.class).putExtra("fromlogin",true));
//        }
//    }
//
//    private WindowManager.LayoutParams getLayoutParams(@NonNull Dialog dialog) {
//        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
//        if (dialog.getWindow() != null) {
//            layoutParams.copyFrom(dialog.getWindow().getAttributes());
//        }
//        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
//        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
//
//        return layoutParams;
//    }
//}