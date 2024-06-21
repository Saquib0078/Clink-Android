package com.nirmiteepublic.clink.ui.activity.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.databinding.ActivityEducationBinding;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class EducationActivity extends AppCompatActivity {
ActivityEducationBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityEducationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ArrayList<String> itemList = readAndParseJsonFile("EDUCATION.json");

        // Set up ArrayAdapter for the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                itemList
        );

        binding.listViewDistrict.setOnItemClickListener((parent, view, position, id) -> {
            String selectedEducation = adapter.getItem(position);

            // Set the selected district to the previous activity's TextView
            Intent resultIntent = new Intent();
            resultIntent.putExtra("selectedEducation", selectedEducation);
            Toast.makeText(this, selectedEducation, Toast.LENGTH_SHORT).show();
            setResult(Activity.RESULT_OK, resultIntent);

            finish();
        });
        // Set the adapter to the ListView
        binding.listViewDistrict.setAdapter(adapter);
    }

    private ArrayList<String> readAndParseJsonFile(String fileName) {
        ArrayList<String> itemList = new ArrayList<>();

        try {
            // Open the asset file
            InputStream inputStream = getAssets().open(fileName);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            // Convert the byte array to a string
            String jsonString = new String(buffer, StandardCharsets.UTF_8);

            // Parse JSON array
            JSONArray jsonArray = new JSONArray(jsonString);

            // Extract strings from JSON array
            for (int i = 0; i < jsonArray.length(); i++) {
                itemList.add(jsonArray.getString(i));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return itemList;
    }
}