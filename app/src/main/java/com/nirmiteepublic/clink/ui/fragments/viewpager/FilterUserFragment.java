package com.nirmiteepublic.clink.ui.fragments.viewpager;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.adapters.FilterAdapter;
import com.nirmiteepublic.clink.databinding.FragmentFilterUserBinding;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;
import com.nirmiteepublic.clink.models.MergedUsersItem;
import com.nirmiteepublic.clink.models.RoleResponse;
import com.nirmiteepublic.clink.models.RoleResponseItem;
import com.nirmiteepublic.clink.ui.activity.pages.BoothActivity;
import com.nirmiteepublic.clink.ui.activity.pages.DistrictActivity;
import com.nirmiteepublic.clink.ui.activity.pages.FilterUserActivity;
import com.nirmiteepublic.clink.ui.activity.pages.RolesActivity;
import com.nirmiteepublic.clink.ui.activity.pages.TehsilActivity;
import com.nirmiteepublic.clink.ui.activity.pages.VillageActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    String Role;

    public FilterAdapter adapter;


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

        binding.Role.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchAndDisplayRoles();
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
//                    Toast.makeText(requireContext(), "Nothing Selected", Toast.LENGTH_SHORT).show();
                    }

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

        RetrofitClient.getInstance(requireContext()).getApiInterfaces().queryUsers(selectedDistrict, selectedTehsil, selectedVillage, selectBooth,minAge,maxAge,gender,date,Role).enqueue(new Callback<JsonObject>() {
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

    private void fetchAndDisplayRoles() {
        RetrofitClient.getInstance(  requireContext()).getApiInterfaces().getAllRoles().enqueue(new Callback<RoleResponse>() {
            @Override
            public void onResponse(Call<RoleResponse> call, Response<RoleResponse> response) {
                if (response.isSuccessful()) {
                    RoleResponse roleResponse = response.body();
                    if (roleResponse != null) {
                        Gson gson = new Gson();
                        String jsonResponse = gson.toJson(roleResponse);
                        System.out.println("JSON Response: " + jsonResponse);

                        try {
                            JSONObject jsonObject = new JSONObject(jsonResponse);
                            JSONArray rolesArray = jsonObject.getJSONArray("role");
                            List<RoleResponseItem> roles = new ArrayList<>();

                            for (int i = 0; i < rolesArray.length(); i++) {
                                JSONObject roleObject = rolesArray.getJSONObject(i);
                                RoleResponseItem role = new RoleResponseItem();
                                role.setId(roleObject.getString("_id"));
                                role.setName(roleObject.getString("name"));
                                role.setV(roleObject.getInt("__v"));
                                roles.add(role);
                            }

                            if (!roles.isEmpty()) {
                                showRolesInListView(roles);
                            } else {
                                Toast.makeText(  requireContext(), "No roles found", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(  requireContext(), "Error parsing JSON", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(  requireContext(), "Response body is null", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(  requireContext(), "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RoleResponse> call, Throwable t) {
                Toast.makeText(  requireContext(), "Error: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showRolesInListView(List<RoleResponseItem> roles) {
        AlertDialog.Builder builder = new AlertDialog.Builder(  requireContext());
        builder.setTitle("Roles");

        ListView listView = new ListView(  requireContext());
        ArrayAdapter<RoleResponseItem> adapter = new ArrayAdapter<RoleResponseItem>(  requireContext(),
                android.R.layout.simple_list_item_1, roles) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setText(getItem(position).getName());
                return view;
            }
        };
        listView.setAdapter(adapter);

        builder.setView(listView);

        final AlertDialog dialog = builder.create();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RoleResponseItem selectedRole = roles.get(position);
                Role=selectedRole.getName();
                binding.role.setText(Role);
//                Toast.makeText( requireContext(), "Selected: " + selectedRole.getName(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                // You can do something with the selected role here
            }

        });

        dialog.show();
    }
}