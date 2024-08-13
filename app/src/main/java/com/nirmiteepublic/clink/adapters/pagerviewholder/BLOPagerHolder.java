package com.nirmiteepublic.clink.adapters.pagerviewholder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.Toast;

import com.nirmiteepublic.clink.databinding.PagerItemUserDetailsInputBinding;
import com.nirmiteepublic.clink.functions.listeners.option.OptionRequestType;
import com.nirmiteepublic.clink.functions.listeners.option.OptionsCallBackListener;
import com.nirmiteepublic.clink.functions.listeners.option.OptionsRequestCallBackListener;
import com.nirmiteepublic.clink.functions.utils.PegaAnimationUtils;
import com.nirmiteepublic.clink.ui.activity.pages.BoothActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class BLOPagerHolder {

    private final int position;
    private final PagerItemUserDetailsInputBinding binding;
    private final OptionsCallBackListener optionsCallBackListener;
    private String selectBooth;
    private final Context context;
    private final JSONObject data = new JSONObject();
    private static final int REQUEST_CODE_BOOTH = 12;

    public BLOPagerHolder(int position, PagerItemUserDetailsInputBinding binding, OptionsCallBackListener optionsCallBackListener, Context context) {
        this.position = position;
        this.binding = binding;
        this.optionsCallBackListener = optionsCallBackListener;
        this.context = context;
    }

    @SuppressLint("SetTextI18n")
    public void bind() {
        binding.title.setText("Tell Us Something More");

        binding.option1Input.setHint("Enter Landmark");
        binding.option2Input.setHint("Enter Ward (Optional)");
        binding.option4Input.setHint("Enter Booth");
        binding.option3.setVisibility(View.GONE);
      binding.option4.setVisibility(View.VISIBLE);
        binding.option4.setOnClickListener(v -> optionsCallBackListener.onOptionsRequest(OptionRequestType.Booth, "Select Your Booth", null, new OptionsRequestCallBackListener() {
            @Override
            public void onOptionSelect(String selected) {
                try {
                    binding.option4Input.setTextColor(Color.BLACK);
                    binding.option4Input.setText(selected);
                    data.put("booth", selected);
                    Toast.makeText(context, selected, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }));
//        binding.option3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, "Click"+selectBooth, Toast.LENGTH_SHORT).show();
//
//                Intent intent = new Intent(context, BoothActivity.class);
//                ((Activity) context).startActivityForResult(intent, REQUEST_CODE_BOOTH);
//            }
//        });
    }

    public void onNext() {
        String landmark = binding.option1Input.getText().toString().trim();
        String ward = binding.option2Input.getText().toString().trim();
        String booth = binding.option4Input.getText().toString().trim();

        if (landmark.isEmpty()) {
            PegaAnimationUtils.shake(binding.option1);
            Toast.makeText(context, "Please Enter Landmark", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            if (!ward.isEmpty()) {
                if (ward.length() > 30) {
                    PegaAnimationUtils.shake(binding.option2);
                    Toast.makeText(context, "Ward Name Can Contain Maximum 30 Characters", Toast.LENGTH_SHORT).show();
                    return;
                }
                data.put("ward", ward);
            }

            if (booth == null || booth.isEmpty()) {
                invalidData(binding.option4, "Select Your Booth");
                return;
            }

            data.put("booth", booth);
            data.put("lMark", landmark);

            optionsCallBackListener.onContinue(position, data);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_CODE_BOOTH) {
//            if (resultCode == Activity.RESULT_OK) {
//                selectBooth = data.getStringExtra("selectBooth");
//                Toast.makeText(context, "Selected"+selectBooth, Toast.LENGTH_SHORT).show();
//                binding.option3Input.setText(selectBooth);
//            }
//        }
//    }

    private void invalidData(View v, String msg) {
        PegaAnimationUtils.shake(v);
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
