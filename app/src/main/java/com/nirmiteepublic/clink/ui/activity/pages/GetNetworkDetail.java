package com.nirmiteepublic.clink.ui.activity.pages;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.databinding.ActivityGetNetworkDetailBinding;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;
import com.nirmiteepublic.clink.functions.utils.UserUtils;
import com.nirmiteepublic.clink.models.RoleResponse;
import com.nirmiteepublic.clink.models.RoleResponseItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetNetworkDetail extends AppCompatActivity {
    ActivityGetNetworkDetailBinding binding;
    String Role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGetNetworkDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.progressBar.setVisibility(View.VISIBLE);

        binding.Role.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showCustomAlertDialog();
                return true;
            }
        });

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.Role.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchAndDisplayRoles();
            }
        });

        Intent intent = getIntent();
        String id = intent.getStringExtra("userId");
        getUsersById(id);
    }


    private void getUsersById(String id) {
        RetrofitClient.getInstance(this).getApiInterfaces().getNetworkDetail(id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        binding.progressBar.setVisibility(View.GONE);

                        String responseBody = null;
                        if (response != null) {
                            responseBody = response.body().string();
                            Gson gson = new Gson();
                            JsonObject jsonObject = gson.fromJson(responseBody, JsonObject.class);

                            if (jsonObject.has("findNum") && jsonObject.has("user")) {
                                JsonObject findNumObject = jsonObject.getAsJsonObject("findNum");
                                JsonObject findNumObject1 = jsonObject.getAsJsonObject("user");

                                // Extract values with null checks
                                String fname = findNumObject1.has("fName") ? findNumObject1.getAsJsonPrimitive("fName").getAsString() : "Not Available";
                                String lname = findNumObject1.has("lName") ? findNumObject1.getAsJsonPrimitive("lName").getAsString() : "Not Available";
                                String lang = findNumObject.has("lang") ? findNumObject.getAsJsonPrimitive("lang").getAsString() : "Not Available";
                                String role = findNumObject.has("role") ? findNumObject.getAsJsonPrimitive("role").getAsString() : "";
                                String edu = findNumObject.has("edu") ? findNumObject.getAsJsonPrimitive("edu").getAsString() : "Not Available";
                                String intr = findNumObject.has("intr") ? findNumObject.getAsJsonPrimitive("intr").getAsString() : "Not Available";
                                String num = findNumObject.has("num") ? findNumObject.getAsJsonPrimitive("num").getAsString() : "Not Available";
                                String dist = findNumObject.has("dist") ? findNumObject.getAsJsonPrimitive("dist").getAsString() : "Not Available";
                                String teh = findNumObject.has("teh") ? findNumObject.getAsJsonPrimitive("teh").getAsString() : "Not Available";
                                String vill = findNumObject.has("vill") ? findNumObject.getAsJsonPrimitive("vill").getAsString() : "Not Available";
                                String lMark = findNumObject.has("lMark") ? findNumObject.getAsJsonPrimitive("lMark").getAsString() : "Not Available";
                                String wpn = findNumObject.has("wpn") ? findNumObject.getAsJsonPrimitive("wpn").getAsString() : "";
                                String fb = findNumObject.has("fb") ? findNumObject.getAsJsonPrimitive("fb").getAsString() : "";
                                String insta = findNumObject.has("insta") ? findNumObject.getAsJsonPrimitive("insta").getAsString() : "";
                                String ward = findNumObject.has("ward") ? findNumObject.getAsJsonPrimitive("ward").getAsString() : "Not Available";
                                String id = findNumObject.has("_id") ? findNumObject.getAsJsonPrimitive("_id").getAsString() : "Not Available";
                                String dp = findNumObject1.has("dp") ? findNumObject1.getAsJsonPrimitive("dp").getAsString() : "Not Available";
                                String dob = findNumObject.has("dob") ? findNumObject.getAsJsonPrimitive("dob").getAsString() : "Not Available";
                                String bio = findNumObject.has("bio") ? findNumObject.getAsJsonPrimitive("bio").getAsString() : "";
//                                Toast.makeText(GetNetworkDetail.this, dp, Toast.LENGTH_SHORT).show();

                                if (bio != null) {
                                    binding.bio.setText(bio);
                                    binding.Bio.setVisibility(View.VISIBLE);

                                } else {
                                    binding.Bio.setVisibility(View.GONE);

                                }
                                UserUtils.setSecondaryUserid(id);
//                                binding.fname.setText(fname + " " + lname +" "+"("+role+")");
                                String fullName = fname + " " + lname;
                                if (role != null && !role.isEmpty()) {
                                    fullName += " (" + role + ")";

                                }
                                binding.fname.setText(fullName);

                                binding.dob.setText(dob);
                                binding.role.setText(role);
                                binding.district.setText(dist);
                                binding.tehsil.setText(teh);
                                binding.village.setText(vill);
//                                binding.whatsapp.setText(wpn);
                                binding.area.setText(lMark);
                                binding.education.setText(edu);
                                binding.intrest.setText(intr);
                                binding.mobile.setText(num);
                                binding.language.setText(lang);
//                                binding.facebook.setText(fb);
//                                binding.insta.setText(insta);
                                binding.ward.setText(ward);

                                binding.whatsappBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (wpn != null && !wpn.isEmpty()) {
                                            openWhatsApp(wpn);
                                        } else {
                                            Toast.makeText(GetNetworkDetail.this, "Please Update your WhatsApp Number", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                binding.phone.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (num != null && !num.isEmpty()) {
                                            openDialer(num);
                                        }
                                    }
                                });

                                binding.mobile.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (num != null && !num.isEmpty()) {
                                            openDialer(num);
                                        }
                                    }
                                });
                                binding.facebookBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (fb != null && !fb.isEmpty()) {
                                            openFacebook(fb);
                                        } else {
                                            Toast.makeText(GetNetworkDetail.this, "Please Update your Facebook Url", Toast.LENGTH_SHORT).show();
                                        }
                                    }


                                });
                                binding.instagramBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (insta != null && !insta.isEmpty()) {
                                            openInstagram(insta);
                                        } else {
                                            Toast.makeText(GetNetworkDetail.this, "Please Update your Instagram Url", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                                Glide.with(GetNetworkDetail.this)
                                        .load(RetrofitClient.VIDEO_BASE_URL + dp)
                                        .placeholder(R.drawable.default_image)
                                        .into(binding.profileImage);
                            } else {
                                // Handle the case when "findNum" or "data" is not present in the JSON
                                // You can set default values or show an error message as needed
                            }
                        } else {
                            binding.progressBar.setVisibility(View.GONE);

                            Toast.makeText(GetNetworkDetail.this, "No user Found", Toast.LENGTH_SHORT).show();

                        }
                    } catch (IOException e) {
                        binding.progressBar.setVisibility(View.GONE);

                        Toast.makeText(GetNetworkDetail.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(GetNetworkDetail.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                binding.progressBar.setVisibility(View.GONE);
            }
        });


    }

    // Function to load JSON from asset
    private void openWhatsApp(String phoneNumber) {
        try {
            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.setData(Uri.parse("https://wa.me/" + phoneNumber));
            startActivity(sendIntent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "WhatsApp not installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void openFacebook(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        intent.setPackage("com.facebook.katana");
        try {
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            // If the Facebook app is not installed, open the URL in the browser
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        }
    }

    private void openInstagram(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        intent.setPackage("com.instagram.android");
        try {
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            // If the Instagram app is not installed, open the URL in the browser
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        }
    }

    private void openDialer(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }


    private void fetchAndDisplayRoles() {
        RetrofitClient.getInstance(GetNetworkDetail.this).getApiInterfaces().getAllRoles().enqueue(new Callback<RoleResponse>() {
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
                                Toast.makeText(GetNetworkDetail.this, "No roles found", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(GetNetworkDetail.this, "Error parsing JSON", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(GetNetworkDetail.this, "Response body is null", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(GetNetworkDetail.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RoleResponse> call, Throwable t) {
                Toast.makeText(GetNetworkDetail.this, "Error: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showRolesInListView(List<RoleResponseItem> roles) {
        Context context = GetNetworkDetail.this;

        // Create and set up the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Roles");
        ListView listView = new ListView(context);
        ArrayAdapter<RoleResponseItem> adapter = new ArrayAdapter<RoleResponseItem>(context, android.R.layout.simple_list_item_1, roles) {
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

        // Handle item click
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RoleResponseItem selectedRole = roles.get(position);
                String role = selectedRole.getName();
                binding.role.setText(role);
                UpdateUser();
                dialog.dismiss(); // Dismiss the dialog after selecting a role
            }
        });

        // Handle item long click for deletion
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                RoleResponseItem selectedRole = roles.get(position);

                // Show confirmation dialog
                new AlertDialog.Builder(context)
                        .setTitle("Delete Role")
                        .setMessage("Are you sure you want to delete this role?")
                        .setPositiveButton("Yes", (dialogInterface, i) -> {
                            // Call delete API
                            RetrofitClient.getInstance(context).getApiInterfaces()
                                    .deleteRole(selectedRole.getId())
                                    .enqueue(new Callback<Void>() {
                                        @Override
                                        public void onResponse(Call<Void> call, Response<Void> response) {
                                            if (response.isSuccessful()) {
                                                Toast.makeText(context, "Role Deleted Successfully", Toast.LENGTH_SHORT).show();
                                                roles.remove(position);
                                                adapter.notifyDataSetChanged(); // Refresh the list
                                            } else {
                                                Toast.makeText(context, "Failed to delete role", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Void> call, Throwable t) {
                                            Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        })
                        .setNegativeButton("No", null)
                        .show();
                return true; // Indicates that the long click was handled
            }
        });

        dialog.show(); // Display the dialog
    }
    private void UpdateUser() {
        Map<String, Object> updatedFields = new HashMap<>();
        updatedFields.put("role", binding.role.getText().toString());


        RetrofitClient.getInstance(this).getApiInterfaces().updateUserById(UserUtils.getSecondaryUserid(), updatedFields).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(GetNetworkDetail.this, "Role Updated Successfully", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(GetNetworkDetail.this, "Failed to update Role", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(GetNetworkDetail.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }


        });
    }

    private void showCustomAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(GetNetworkDetail.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_slider_alertdialog, null);
        builder.setView(dialogView);

        EditText editText = dialogView.findViewById(R.id.role);
        Button button = dialogView.findViewById(R.id.updaterole);

        final AlertDialog alertDialog = builder.create();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputText = editText.getText().toString().trim();
                if (!inputText.isEmpty()) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("name", inputText);
                    String jsonString = new Gson().toJson(jsonObject);
                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonString);
                    updateRole(requestBody);
                    alertDialog.dismiss();
                } else {
                    Toast.makeText(GetNetworkDetail.this, "Please enter a role", Toast.LENGTH_SHORT).show();
                }
            }
        });

        alertDialog.show();
    }

    private void updateRole(RequestBody requestBody) {
        RetrofitClient.getInstance(this).getApiInterfaces().createRole(requestBody)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(GetNetworkDetail.this, "Role updated successfully", Toast.LENGTH_SHORT).show();
                            // Update UI or do other necessary operations
                        } else {
                            Toast.makeText(GetNetworkDetail.this, "Failed to update role: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(GetNetworkDetail.this, "Failed to update role: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}