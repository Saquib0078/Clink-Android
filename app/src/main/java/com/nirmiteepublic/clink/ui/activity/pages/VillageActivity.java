package com.nirmiteepublic.clink.ui.activity.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.databinding.ActivityVillageBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class VillageActivity extends AppCompatActivity {
ActivityVillageBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityVillageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
     binding.progressBar.setVisibility(View.VISIBLE);
        String selectedTehsil = getIntent().getStringExtra("selectedTehsil");
        String selectedDistrict = getIntent().getStringExtra("selectedDistrict");
        Toast.makeText(this, "Dis"+selectedDistrict, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, selectedTehsil, Toast.LENGTH_SHORT).show();


        JSONArray villagesForTehsil = getVillagesForTehsil(selectedDistrict,selectedTehsil);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        for (int i = 0; i < villagesForTehsil.length(); i++) {
            try {
                String village = villagesForTehsil.getString(i);
                adapter.add(village);
                binding.progressBar.setVisibility(View.GONE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ListView listView = findViewById(R.id.listViewVillage);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedVillage = adapter.getItem(position);
            Intent resultIntent = new Intent();
            resultIntent.putExtra("selectedVillage", selectedVillage);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        });

    }
    private JSONArray getVillagesForTehsil(String selectedDistrict, String selectedTehsil) {
        try {
            // Load the JSON data from the assets file
            InputStream inputStream = getAssets().open("VILLAGE.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            String jsonString = new String(buffer, StandardCharsets.UTF_8);

            // Parse the JSON data
            JSONObject districtData = new JSONObject(jsonString);

            // Check if the selected district exists
            if (districtData.has(selectedDistrict)) {
                // Get the tehsil data for the selected district
                JSONObject tehsilData = districtData.getJSONObject(selectedDistrict);

                // Check if the selected tehsil exists in the tehsilData
                if (tehsilData.has(selectedTehsil)) {
                    return tehsilData.getJSONArray(selectedTehsil);
                } else {
                    Log.e("JSON Error", "Selected tehsil not found in JSON data");
                }
            } else {
                Log.e("JSON Error", "Selected district not found in JSON data");
            }

            return new JSONArray(); // Return an empty array if tehsil or district not found
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            Log.e("JSON Error", "Error loading or parsing JSON data");
            return new JSONArray();
        }
    }

}