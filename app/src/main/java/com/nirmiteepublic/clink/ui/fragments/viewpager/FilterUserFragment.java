package com.nirmiteepublic.clink.ui.fragments.viewpager;

import static android.app.Activity.RESULT_OK;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.adapters.FilterAdapter;
import com.nirmiteepublic.clink.databinding.FragmentFilterUserBinding;
import com.nirmiteepublic.clink.functions.retrofit.req.FilterListner;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;
import com.nirmiteepublic.clink.models.DataItem;
import com.nirmiteepublic.clink.models.FilterCriteria;
import com.nirmiteepublic.clink.models.MergedUsersItem;
import com.nirmiteepublic.clink.models.UserModel;
import com.nirmiteepublic.clink.models.UserResponse;
import com.nirmiteepublic.clink.ui.activity.pages.BoothActivity;
import com.nirmiteepublic.clink.ui.activity.pages.DistrictActivity;
import com.nirmiteepublic.clink.ui.activity.pages.FilterUserActivity;
import com.nirmiteepublic.clink.ui.activity.pages.TehsilActivity;
import com.nirmiteepublic.clink.ui.activity.pages.VillageActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Nullable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FilterUserFragment extends BottomSheetDialogFragment {

    FragmentFilterUserBinding binding;

    String gender;

    String maxAge;
    String minAge;
    String selectedDistrict;
    String date;
    String selectedTehsil;
    String selectedVillage;
    String selectBooth;
    public FilterAdapter adapter;
    public interface BottomSheetListener {
        void onDataReceived(List<DataItem> userList);
    }
    public BottomSheetListener listener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentFilterUserBinding.inflate(inflater, container, false);



        adapter = new FilterAdapter(new ArrayList<>(), requireContext());
        binding.filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callFilterApi();

            }
        });
        binding.btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAllData();
            }
        });

        binding.birthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });
        binding.village.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), VillageActivity.class);
                intent.putExtra("selectedDistrict", selectedDistrict);
                intent.putExtra("selectedTehsil", selectedTehsil);
                startActivityForResult(intent, 3);
            }

        });

        binding.tehsil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(requireContext(), TehsilActivity.class);

                intent.putExtra("selectedDistrict", selectedDistrict);
                startActivityForResult(intent, 2);
            }

        });

        binding.District.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), DistrictActivity.class);

                startActivityForResult(intent, 1);

            }

        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.gender_array,
                android.R.layout.simple_spinner_item
        );

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        binding.gender.setAdapter(adapter);

        // Handle item selection
        binding.gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Exclude the hint from processing
                if (position != 0) {
                    gender = parentView.getItemAtPosition(position).toString();
                    // You can perform actions based on the selected gender
//                    Toast.makeText(requireContext(), "Selected Gender: " + gender, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });


        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.age_range,
                android.R.layout.simple_spinner_item
        );

        // Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        binding.age.setAdapter(adapter1);

        // Handle item selection
        binding.age.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Exclude the hint from processing
                if (position != 0) {
                    String selectedAgeRange = parentView.getItemAtPosition(position).toString();
                    String[] ageRangeParts = selectedAgeRange.split("-");
                     minAge = ageRangeParts[0];
                     maxAge = ageRangeParts[1];

                } else {
                    Toast.makeText(requireContext(), "Nothing Selected", Toast.LENGTH_SHORT).show();                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });


        binding.booth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(requireContext(), BoothActivity.class);

                startActivityForResult(intent, 200);
            }
        });


        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1: // Edit District
                    handleEditDistrictResult(data);
                    break;
                case 2: // Edit Tehsil
                    handleEditTehsilResult(data);
                    break;
                case 3: // Edit Village
                    handleEditVillageResult(data);
                    break;
                case 200:
                    handleBoothSelect(data);
                    break;

                // Add more cases if you have additional actions
            }
        }
    }

    private void handleBoothSelect(Intent data) {
        if (data != null && data.hasExtra("selectBooth")) {
            selectBooth = data.getStringExtra("selectBooth");
            binding.booth.setText(selectBooth);
        }
    }


    private void handleEditDistrictResult(Intent data) {
        // Handle the result for editing district
        if (data != null && data.hasExtra("selectedDistrict")) {
            selectedDistrict = data.getStringExtra("selectedDistrict");
            binding.District.setText(selectedDistrict);
            binding.Tehsil.setVisibility(View.VISIBLE);
        }
    }

    private void handleEditTehsilResult(Intent data) {
        // Handle the result for editing tehsil
        if (data != null && data.hasExtra("selectedTehsil")) {
            selectedTehsil = data.getStringExtra("selectedTehsil");
            binding.tehsil.setText(selectedTehsil);
            binding.Village.setVisibility(View.VISIBLE);
        }
    }

    private void handleEditVillageResult(Intent data) {
        if (data != null && data.hasExtra("selectedVillage")) {
            selectedVillage = data.getStringExtra("selectedVillage");
            binding.village.setText(selectedVillage);
        }
    }

    private void showDatePicker() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog and show it
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // Handle the selected date
                // You can update a TextView or do any other action here
                date = dayOfMonth + "/" + (monthOfYear + 1);
                binding.birthdate.setText(date);
//                Intent intent=new Intent(requireContext(),FilterUserActivity.class);
//                intent.putExtra("date",SelectedDate);
//                startActivity(intent);
                // Update your UI or perform an action with the selected date
            }
        }, year, month, dayOfMonth);

        datePickerDialog.show();
    }


    private void callFilterApi() {

        RetrofitClient.getInstance(requireContext()).getApiInterfaces().queryUsers(selectedDistrict, selectedTehsil, selectedVillage, selectBooth,minAge,maxAge,gender,date).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    // Handle successful response
                    JsonObject jsonObject = response.body();

                    if (jsonObject != null && jsonObject.has("mergedUsers")) {
                        JsonArray jsonArray = jsonObject.getAsJsonArray("mergedUsers");
                        List<MergedUsersItem> userList = new ArrayList<>();
                        Gson gson = new Gson();
                        for (JsonElement element : jsonArray) {

                            MergedUsersItem user = gson.fromJson(element, MergedUsersItem.class);
                            userList.add(user);
                            Log.d("", String.valueOf(jsonArray));
                        }
                        Toast.makeText(requireContext(), date, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(requireContext(), FilterUserActivity.class);
                        intent.putExtra("json", String.valueOf(jsonArray));
                        startActivity(intent);
                    } else {
                        Log.e("API Error", "No 'mergedUsers' array found in the response.");
                    }
                } else {
                    String errorBodyString = null;
                    try {
                        errorBodyString = response.errorBody().string();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    JsonObject errorJson = new Gson().fromJson(errorBodyString, JsonObject.class);
                    // Handle error response
                    Log.e("API Error", "Failed to query users: " + errorJson);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("API Error", "Failed to query users: " + t.getLocalizedMessage());

            }
        });
    }
    private void clearAllData() {
        // Clear all fields and variables holding the selected values
        selectedDistrict = null;
        selectedTehsil = null;
        selectedVillage = null;
        selectBooth = null;
        minAge = null;
        maxAge = null;
        gender = null;
        date = null;

        // Clear text and selections from UI elements
        binding.District.setText("");
        binding.tehsil.setText("");
        binding.village.setText("");
        binding.booth.setText("");
        binding.age.setSelection(0); // Set the spinner to the first item (hint)
        binding.gender.setSelection(0); // Set the spinner to the first item (hint)
        binding.birthdate.setText(""); // Clear the date text
    }

}