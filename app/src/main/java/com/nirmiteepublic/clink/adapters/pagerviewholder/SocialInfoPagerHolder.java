package com.nirmiteepublic.clink.adapters.pagerviewholder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.text.InputType;
import android.view.View;
import android.widget.Toast;

import com.nirmiteepublic.clink.databinding.PagerItemUserDetailsInputBinding;
import com.nirmiteepublic.clink.functions.listeners.option.OptionsCallBackListener;
import com.nirmiteepublic.clink.functions.utils.PegaAnimationUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class SocialInfoPagerHolder {

    private final int position;
    private final PagerItemUserDetailsInputBinding binding;
    private final OptionsCallBackListener optionsCallBackListener;
    private final Context context;
    private final JSONObject data = new JSONObject();

    public SocialInfoPagerHolder(int position, PagerItemUserDetailsInputBinding binding, OptionsCallBackListener optionsCallBackListener, Context context) {
        this.position = position;
        this.binding = binding;
        this.optionsCallBackListener = optionsCallBackListener;
        this.context = context;
    }


    @SuppressLint("SetTextI18n")
    public void bind() {
        binding.title.setText("Tell US About Your Social Profile");
        binding.option3.setVisibility(View.VISIBLE);

        binding.option1Input.setHint("Enter Your WhatsApp Number");
        binding.option1Input.setInputType(InputType.TYPE_CLASS_NUMBER);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.option1Input.setAutofillHints(View.AUTOFILL_HINT_PHONE);
        }

        binding.option2Input.setHint("Enter Your Instagram URL (Optional)");
        binding.option3Input.setHint("Enter Your Facebook (Optional)");

    }

    public void onNext() {
        String whatsAppNumber = binding.option1Input.getText().toString().trim().replace("+91", "");
        String insUrl = binding.option2Input.getText().toString().trim();
        String fbUrl = binding.option3Input.getText().toString().trim();

        if (whatsAppNumber.length() != 10) {
            PegaAnimationUtils.shake(binding.option1);
            Toast.makeText(context, "Please Enter a Valid Whatsapp Number", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            if (!insUrl.isEmpty()) {
                if (insUrl.length() > 70) {
                    PegaAnimationUtils.shake(binding.option2);
                    Toast.makeText(context, "Instagram URL Can Contain Maximum 70 Characters", Toast.LENGTH_SHORT).show();
                    return;
                }
                data.put("insta", insUrl);
            }
            if (!fbUrl.isEmpty()) {
                if (fbUrl.length() > 70) {
                    PegaAnimationUtils.shake(binding.option3);
                    Toast.makeText(context, "Facebook URL Can Contain Maximum 70 Characters", Toast.LENGTH_SHORT).show();
                    return;
                }
                data.put("fb", fbUrl);
            }
            data.put("wpn", whatsAppNumber);
            optionsCallBackListener.onContinue(position, data);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

}
