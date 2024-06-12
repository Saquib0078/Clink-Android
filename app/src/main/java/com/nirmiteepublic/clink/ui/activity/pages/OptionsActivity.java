package com.nirmiteepublic.clink.ui.activity.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.databinding.ActivityOptionsBinding;
import com.nirmiteepublic.clink.functions.listeners.option.OptionRequestType;
import com.nirmiteepublic.clink.functions.listeners.option.OptionsRequestCallBackListener;
import com.nirmiteepublic.clink.ui.fragments.OptionsFragment;

public class OptionsActivity extends AppCompatActivity  {
ActivityOptionsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityOptionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        OptionRequestType optionRequestType = (OptionRequestType) intent.getSerializableExtra("optionRequestType");
        System.out.println("OptionRequestType: " + optionRequestType);
        String title = intent.getStringExtra("title");
        String preData = intent.getStringExtra("preData");
        System.out.println("Title: " + title);
        System.out.println("PreData: " + preData);
        loadOptionsFragment(optionRequestType);



    }




    private void loadOptionsFragment(OptionRequestType optionRequestType) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new OptionsFragment(optionRequestType, "Title", "PreData", new OptionsRequestCallBackListener() {
                    @Override
                    public void onOptionSelect(String selected) {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("selectedOption", selected);
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    }
                }))
                .commit();
    }


}