package com.nirmiteepublic.clink.ui.activity.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.databinding.ActivityRegistrationBinding;
import com.nirmiteepublic.clink.functions.helpers.CredentialsValidator;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;
import com.nirmiteepublic.clink.functions.retrofit.res.PegaCallback;
import com.nirmiteepublic.clink.functions.retrofit.res.PegaResponseManager;
import com.nirmiteepublic.clink.functions.utils.PegaAnimationUtils;
import com.nirmiteepublic.clink.functions.viewmanagers.PegaAppCompatActivity;
import com.nirmiteepublic.clink.ui.activity.actions.OtpActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class RegistrationActivity extends PegaAppCompatActivity {

    ActivityRegistrationBinding binding;

    private CredentialsValidator credentialsValidator;
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (validateData(false)) {
                binding.register.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.sign_enabled));
            } else {
                binding.register.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.sign_disabled));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setWindowThemeThird();

        credentialsValidator = new CredentialsValidator(this);

        binding.register.setOnClickListener(v -> {
            String fName = binding.firstName.getText().toString().trim();
            String lName = binding.lastName.getText().toString().trim();

            String number = binding.number.getText().toString().trim().replace("+91", "");

            JSONObject data = new JSONObject();

            try {
                data.put("fName", fName);
                data.put("lName", lName);
                data.put("num", number);
                data.put("action", "registration");

                if (validateData(true)) {
                    sendOTP(data);
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        });

        binding.firstName.addTextChangedListener(textWatcher);
        binding.lastName.addTextChangedListener(textWatcher);
        binding.number.addTextChangedListener(textWatcher);

        binding.openLogin.setOnClickListener(v -> {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, binding.numberContainer, Objects.requireNonNull(ViewCompat.getTransitionName(binding.numberContainer)));
            startActivity(new Intent(this, LoginActivity.class), options.toBundle());
        });

    }

    private void sendOTP(JSONObject mainData) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), mainData.toString());
        RetrofitClient.getInstance(this).getApiInterfaces().checkRegister(requestBody).enqueue(new PegaResponseManager(new PegaCallback(this) {
            @Override
            public void onNumberExists() {
                super.onNumberExists();
                PegaAnimationUtils.shake(binding.numberContainer);
            }

            @Override
            public void onInvalidNumber() {
                super.onInvalidNumber();
                PegaAnimationUtils.shake(binding.numberContainer);
            }

            @Override
            public void onSuccess(@Nullable JSONObject data) {
                openActivityWithRightAnim(new Intent(getActivity(), OtpActivity.class).putExtra("data", mainData.toString()));
            }
        }));
    }

    /**
     * Validate all user input data
     */
    private boolean validateData(boolean showToast) {
        String fName = binding.firstName.getText().toString().trim();
        String lName = binding.lastName.getText().toString().trim();
        String number = binding.number.getText().toString().trim();

        if (showToast) {
            if (fName.isEmpty() && lName.isEmpty()) {
                Toast.makeText(this, "Please Enter Your First Name and Last Name", Toast.LENGTH_SHORT).show();
                PegaAnimationUtils.shake(binding.nameContainer);
                return false;
            }
            if (fName.isEmpty()) {
                Toast.makeText(this, "Please Enter First Name", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (lName.isEmpty()) {
                Toast.makeText(this, "Please Enter Last Name", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (!credentialsValidator.validateFirstName(fName, true)) {
                PegaAnimationUtils.shake(binding.nameContainer);
                return false;
            }

            if (!credentialsValidator.validateLastName(lName, true)) {
                PegaAnimationUtils.shake(binding.nameContainer);
                return false;
            }

            if (!credentialsValidator.validateNumber(number, true)) {
                PegaAnimationUtils.shake(binding.numberContainer);
                return false;
            }

            return true;
        } else {
            return credentialsValidator.validateFirstName(fName, false) && credentialsValidator.validateLastName(lName, false) && credentialsValidator.validateNumber(number, false);
        }

    }

}