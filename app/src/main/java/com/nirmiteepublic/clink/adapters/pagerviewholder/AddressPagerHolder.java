package com.nirmiteepublic.clink.adapters.pagerviewholder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Toast;

import com.nirmiteepublic.clink.databinding.PagerItemUserDetailsBinding;
import com.nirmiteepublic.clink.functions.listeners.option.OptionRequestType;
import com.nirmiteepublic.clink.functions.listeners.option.OptionsCallBackListener;
import com.nirmiteepublic.clink.functions.listeners.option.OptionsRequestCallBackListener;
import com.nirmiteepublic.clink.functions.utils.PegaAnimationUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class AddressPagerHolder {

    private final int position;
    private final PagerItemUserDetailsBinding binding;
    private final OptionsCallBackListener optionsCallBackListener;
    private final Context context;
    private final JSONObject data = new JSONObject();


    public AddressPagerHolder(int position, PagerItemUserDetailsBinding binding, OptionsCallBackListener optionsCallBackListener, Context context) {
        this.position = position;
        this.binding = binding;
        this.optionsCallBackListener = optionsCallBackListener;
        this.context = context;
    }


    @SuppressLint("SetTextI18n")
    public void bind() {
        binding.title.setText("Tell US About Your Address");

        binding.option1Title.setText("Select Your District");
        binding.option2Title.setText("Select Your Tehasil");
        binding.option3Title.setText("Select Your Village");


        binding.option1.setOnClickListener(v -> optionsCallBackListener.onOptionsRequest(OptionRequestType.DISTRICT, "Select Your District", null, new OptionsRequestCallBackListener() {
            @Override
            public void onOptionSelect(String selected) {
                try {
                    binding.option1Title.setTextColor(Color.BLACK);
                    binding.option1Title.setText(selected);
                    data.put("dist", selected);
                    Toast.makeText(context, selected, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }));

        binding.option2.setOnClickListener(v -> {
            if (!data.has("dist")) {
                PegaAnimationUtils.shake(binding.option1);
                Toast.makeText(context, "You Have to Select District First", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                optionsCallBackListener.onOptionsRequest(OptionRequestType.TEHASIL, "Select Your Tehasil", data.getString("dist"), new OptionsRequestCallBackListener() {
                    @Override
                    public void onOptionSelect(String selected) {
                        try {
                            binding.option2Title.setTextColor(Color.BLACK);
                            binding.option2Title.setText(selected);
                            data.put("teh", selected);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
        binding.option3.setOnClickListener(v -> {
            if (!data.has("teh")) {
                PegaAnimationUtils.shake(binding.option2);
                Toast.makeText(context, "You Have to Select Tehasil First", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                optionsCallBackListener.onOptionsRequest(OptionRequestType.VILLAGE, "Select Your Village", data.getString("dist") + ":" + data.getString("teh"), new OptionsRequestCallBackListener() {
                    @Override
                    public void onOptionSelect(String selected) {
                        try {
                            binding.option3Title.setTextColor(Color.BLACK);
                            binding.option3Title.setText(selected);
                            data.put("vill", selected);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });

    }

    public void onNext() {
        if (!data.has("dist")) {
            invalidData(binding.option1, "Please Select Your District");
            return;
        }
        if (!data.has("teh")) {
            invalidData(binding.option2, "Please Select Your Tehasil");
            return;
        }
        if (!data.has("vill")) {
            invalidData(binding.option3, "Select Your Village");
            return;
        }
        optionsCallBackListener.onContinue(position, data);
    }

    private void invalidData(View v, String msg) {
        PegaAnimationUtils.shake(v);
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

}
