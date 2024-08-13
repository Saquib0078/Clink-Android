package com.nirmiteepublic.clink.ui.activity.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.nirmiteepublic.clink.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class TehsilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tehsil);
        String selectedDistrict = getIntent().getStringExtra("selectedDistrict");

// Assuming you have a method to get tehsils based on the district
        JSONArray tehsilsForDistrict = getTehsilsForDistrict(selectedDistrict);

// Display a list of tehsils
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        for (int i = 0; i < tehsilsForDistrict.length(); i++) {
            try {
                String tehsil = tehsilsForDistrict.getString(i);
                adapter.add(tehsil);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ListView listView = findViewById(R.id.listViewTehsil);
        listView.setAdapter(adapter);

// Handle item click to send back the selected tehsil
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedTehsil = adapter.getItem(position);
            Intent resultIntent = new Intent();
            resultIntent.putExtra("selectedTehsil", selectedTehsil);
            Toast.makeText(this, selectedTehsil, Toast.LENGTH_SHORT).show();
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        });


    }

    private JSONArray getTehsilsForDistrict(String selectedDistrict) {
        try {
            // Load the JSON data from the assets file
            InputStream inputStream = getAssets().open("VILLAGE.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            String jsonString = new String(buffer, StandardCharsets.UTF_8);

            // Parse the JSON data
            JSONObject districtsData = new JSONObject(jsonString);
            JSONObject districtObject = districtsData.getJSONObject(selectedDistrict);

            // Get the tehsils array for the selected district
            return districtObject.names(); // Replace with the actual array key for tehsils
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

}