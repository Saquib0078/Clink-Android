package com.nirmiteepublic.clink.adapters.pagerviewholder;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.nirmiteepublic.clink.databinding.PagerItemUserDetailsBinding;
import com.nirmiteepublic.clink.functions.listeners.option.OptionRequestType;
import com.nirmiteepublic.clink.functions.listeners.option.OptionsCallBackListener;
import com.nirmiteepublic.clink.functions.listeners.option.OptionsRequestCallBackListener;
import com.nirmiteepublic.clink.functions.utils.PegaAnimationUtils;
import com.nirmiteepublic.clink.ui.activity.pages.CreateTaskActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class PersonalInfoPagerHolder {
    private final int position;
    String SelectedDate;
    private final PagerItemUserDetailsBinding binding;
    private final OptionsCallBackListener optionsCallBackListener;
    private final Context context;
    private final JSONObject data = new JSONObject();

    public PersonalInfoPagerHolder(int position, PagerItemUserDetailsBinding binding, OptionsCallBackListener optionsCallBackListener, Context context) {
        this.position = position;
        this.binding = binding;
        this.optionsCallBackListener = optionsCallBackListener;
        this.context = context;
    }

    @SuppressLint("SetTextI18n")
    public void bind() {
        binding.title.setText("Tell Us About Yourself");

        binding.option1Title.setText("Select Your Language");
        binding.option2Title.setText("Select Your Education");
        binding.option3Title.setText("What are your interests? (Optional)");
        binding.option4.setVisibility(View.VISIBLE);
        binding.option4Title.setText("Tell us About Your Date of Birth");

        binding.option1.setOnClickListener(v -> optionsCallBackListener.onOptionsRequest(OptionRequestType.LANGUAGE, "Select Your Language", null, new OptionsRequestCallBackListener() {
            @Override
            public void onOptionSelect(String selected) {
                try {
                    binding.option1Title.setTextColor(Color.BLACK);
                    binding.option1Title.setText(selected);
                    data.put("lang", selected);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }));
        binding.option4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.option4Title.setTextColor(Color.BLACK);

                showDatePicker();
            }
        });

        binding.option2.setOnClickListener(v -> optionsCallBackListener.onOptionsRequest(OptionRequestType.EDUCATION, "Select Your Education", null, new OptionsRequestCallBackListener() {
            @Override
            public void onOptionSelect(String selected) {
                try {
                    binding.option2Title.setTextColor(Color.BLACK);
                    binding.option2Title.setText(selected);
                    data.put("edu", selected);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }));
        binding.option3.setOnClickListener(v -> optionsCallBackListener.onOptionsRequest(OptionRequestType.INTERESTS, "What are your interests?", null, new OptionsRequestCallBackListener() {
            @Override
            public void onOptionSelect(String selected) {
                try {
                    binding.option3Title.setTextColor(Color.BLACK);
                    binding.option3Title.setText(selected);
                    data.put("intr", selected);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }));

    }

    public void onNext() {
        if (!data.has("lang")) {
            invalidData(binding.option1, "Please Select Your Language");
            return;
        }
        if (!data.has("edu")) {
            invalidData(binding.option2, "Please Select Your Education");
            return;
        }
        if (!data.has("intr")) {
            invalidData(binding.option3, "Please Tell Us About Your Interests");
            return;
        }
        if (!data.has("dob")) {
            invalidData(binding.option4, "Please Tell Us About Your DOB");
            return;
        }
        optionsCallBackListener.onContinue(position, data);
    }

    private void invalidData(View v, String msg) {
        PegaAnimationUtils.shake(v);
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
    private void showDatePicker() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog and show it
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                SelectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                try {
                    data.put("dob",SelectedDate);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                binding.option4Title.setText(SelectedDate);
            }
        }, year, month, dayOfMonth);

        datePickerDialog.show();
    }

}
