package com.nirmiteepublic.clink.ui.fragments;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.adapters.OptionsAdapter;
import com.nirmiteepublic.clink.functions.listeners.option.OptionRequestType;
import com.nirmiteepublic.clink.functions.listeners.option.OptionsCallback;
import com.nirmiteepublic.clink.functions.listeners.option.OptionsRequestCallBackListener;
import com.nirmiteepublic.clink.ui.activity.pages.EditProfileActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class OptionsFragment extends Fragment {

    private final OptionRequestType optionRequestType;
    private final String title, preData;
    OptionsCallback optionsCallback;
    private final OptionsRequestCallBackListener optionsRequestCallBackListener;
    private final List<String> list = new ArrayList<>();
    com.nirmiteepublic.clink.databinding.FragmentOptionsBinding binding;

    public OptionsFragment(OptionRequestType optionRequestType, String title, String preData, OptionsRequestCallBackListener optionsRequestCallBackListener) {
        this.optionRequestType = optionRequestType;
        this.title = title;
        this.preData = preData;
        this.optionsRequestCallBackListener = optionsRequestCallBackListener;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = com.nirmiteepublic.clink.databinding.FragmentOptionsBinding.inflate(inflater, container, false);

        binding.back.setOnClickListener(v -> goBack());
        binding.title.setText(title);


        Thread thread = new Thread(() -> {

            loadJsonData();
            OptionsAdapter adapter = new OptionsAdapter(list, new OptionsRequestCallBackListener() {
                @Override
                public void onOptionSelect(String selected) {
                    optionsRequestCallBackListener.onOptionSelect(selected);
                    goBack();
                }
            });




            if (list.size() <= 15) {
                requireActivity().runOnUiThread(() -> {
                    binding.progress.setVisibility(View.GONE);
                    binding.recyclerView.setAdapter(adapter);
                });
                return;
            }

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                binding.progress.setVisibility(View.GONE);
                binding.recyclerView.setAdapter(adapter);
            }, 1000);

        });
        thread.start();

        return binding.getRoot();
    }

    private void goBack() {
        requireActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right).remove(this).commit();

   }

    private void loadJsonData() {
        AssetManager assetManager = requireActivity().getAssets();
        try {
            InputStream inputStream = assetManager.open(optionRequestType.toString() + ".json");

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                String json = stringBuilder.toString();
                JSONArray jsonArray;
                if (optionRequestType == OptionRequestType.TEHASIL) {
                    jsonArray = new JSONObject(json).getJSONArray(preData);
                } else if (optionRequestType == OptionRequestType.VILLAGE) {
                    String[] parts = preData.split(":");
                    jsonArray = new JSONObject(json).getJSONObject(parts[0]).getJSONArray(parts[1]);
                } else {
                    jsonArray = new JSONArray(json);
                }

                for (int i = 0; i < jsonArray.length(); i++) {
                    list.add(jsonArray.getString(i));
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}