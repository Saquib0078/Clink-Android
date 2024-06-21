package com.nirmiteepublic.clink.ui.activity.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.databinding.ActivityDistrictBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class DistrictActivity extends AppCompatActivity {
ActivityDistrictBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDistrictBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_district);
        String json = loadJSONFromAsset("VILLAGE.json");
        System.out.println(json);



        try {
            JSONObject districts = new JSONObject(json);
            JSONArray districtNames = districts.names();

            // Display a list of districts
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
            for (int i = 0; i < districtNames.length(); i++) {
                adapter.add(districtNames.getString(i));
            }

            ListView listView = findViewById(R.id.listViewDistrict);
            listView.setAdapter(adapter);

            // Handle item click to launch TehsilActivity
            listView.setOnItemClickListener((parent, view, position, id) -> {
                String selectedDistrict = adapter.getItem(position);

                // Set the selected district to the previous activity's TextView
                Intent resultIntent = new Intent();
                resultIntent.putExtra("selectedDistrict", selectedDistrict);
                Toast.makeText(this, selectedDistrict, Toast.LENGTH_SHORT).show();
                setResult(Activity.RESULT_OK, resultIntent);

                finish();
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
    private String loadJSONFromAsset(String filename) {
        try {
            InputStream inputStream = getAssets().open(filename);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            return new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}