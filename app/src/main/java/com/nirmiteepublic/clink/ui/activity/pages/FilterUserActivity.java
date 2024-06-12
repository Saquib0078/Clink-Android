package com.nirmiteepublic.clink.ui.activity.pages;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.adapters.FilterAdapter;
import com.nirmiteepublic.clink.databinding.ActivityFilterUsersBinding;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;
import com.nirmiteepublic.clink.functions.viewmanagers.PegaAppCompatActivity;
import com.nirmiteepublic.clink.models.DataItem;
import com.nirmiteepublic.clink.models.MergedUserResponse;
import com.nirmiteepublic.clink.models.MergedUsersItem;
import com.nirmiteepublic.clink.ui.fragments.viewpager.FilterUserFragment;
import com.pegalite.popups.DialogData;
import com.pegalite.popups.PegaProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilterUserActivity extends PegaAppCompatActivity {
    List<DataItem> dataItems = new ArrayList<>();
    FilterAdapter adapter;
    Bitmap bitmap;
    ImageView img;
    ActivityFilterUsersBinding binding;
    private FilterUserFragment filterUserFragment;
    String radioButtonValue;
    private PegaProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFilterUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        progressDialog = new PegaProgressDialog(this);
        setWindowThemeSecond();
        String meet = getIntent().getStringExtra("meetActivity");

        String task = getIntent().getStringExtra("taskActivity");
        if ("meeting".equals(meet)) {

            Toast.makeText(this, "" + meet, Toast.LENGTH_SHORT).show();
        }

        if ("task".equals(task)) {

            Toast.makeText(this, "" + task, Toast.LENGTH_SHORT).show();
        }

//        Intent intent = getIntent();
//        if (intent != null) {
//            String receivedData = intent.getStringExtra("json");
//        }
        binding.checkBoxSelectAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                adapter.selectAll();
            } else {
                adapter.clearSelection();
            }
        });

        binding.filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilterUserFragment bottomSheetFragment = new FilterUserFragment();
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
            }
        });

//        adapter = new FilterUserAdapter(DataItems);
        loadData();
        binding.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressDialog();
                sendNotification();
            }
        });


    }


    private void loadNetwork() {
        RetrofitClient.getInstance(this).getApiInterfaces().getUSER().enqueue(new Callback<MergedUserResponse>() {
            @Override
            public void onResponse(Call<MergedUserResponse> call, Response<MergedUserResponse> response) {
                if (response.isSuccessful()) {
                    hideProgressDialog();
                    adapter = new FilterAdapter(response.body().getMergedUsers(), FilterUserActivity.this);

                    binding.recyclerView.setAdapter(adapter);


                } else {
                    hideProgressDialog();
                    Toast.makeText(FilterUserActivity.this, "No data", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<MergedUserResponse> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(FilterUserActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void sendNotification() {

        // Set up the parent view
        LayoutInflater inflater = LayoutInflater.from(this);
        View customDialogView = inflater.inflate(R.layout.notification_alertdialog, null);

        // Access EditText views and button in the custom layout
        EditText titleInput = customDialogView.findViewById(R.id.title);
        EditText bodyInput = customDialogView.findViewById(R.id.descryption);
        Button btnUploadImage = customDialogView.findViewById(R.id.btnUploadImage);
        Button sendButton = customDialogView.findViewById(R.id.send);
//        RadioGroup radioGroup = customDialogView.findViewById(R.id.radiogrp);
//        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
//            if (checkedId == R.id.limited) {
//                radioButtonValue = "meeting";
//
//            } else if (checkedId == R.id.openall) {
//                radioButtonValue = "task";
//
//            } else {
//                radioButtonValue = "meeting";
//            }
//        });

        img = customDialogView.findViewById(R.id.img);


        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(intent);
            }
        });
        // Set up the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(customDialogView);

        // Create the AlertDialog
        AlertDialog alertDialog = builder.create();

        // Set an OnClickListener for the Send button
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle Send button click
                String title = titleInput.getText().toString();
                String body = bodyInput.getText().toString();

                // Validate if title and body are not empty
                if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(body)) {
                    List<MergedUsersItem> selectedUsers = adapter.getSelectedUsers();

                    JsonArray jsonArray = new JsonArray();

                    List<String> phoneNumbers = new ArrayList<>();
//                    for (MergedUsersItem user : selectedUsers) {
//                        String number=user.getNum();
//                        String converted=number;
//                        Log.d("number",converted);
//                        phoneNumbers.add(converted);
//                    }
                    for (MergedUsersItem user : selectedUsers) {
                        String number = user.getNum();
                        phoneNumbers.add(number);
                        System.out.println(number);
                    }

                    //                    requestBody.put("title", title);
//                    requestBody.put("body", body);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    if (bitmap != null) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                        byte[] byteArray = byteArrayOutputStream.toByteArray();
                        String base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT);


                        // Create RequestBody for text data
                        RequestBody meetNameRequestBody = RequestBody.create(MediaType.parse("text/plain"), title);
                        RequestBody meetDescriptionRequestBody = RequestBody.create(MediaType.parse("text/plain"), body);
//                        RequestBody radio = RequestBody.create(MediaType.parse("text/plain"), radioButtonValue);


                        RequestBody imageRequestBody = RequestBody.create(MediaType.parse("image/jpeg"), Base64.decode(base64Image, Base64.DEFAULT));

                        // Create MultipartBody.Part for the image
                        MultipartBody.Part imageID = MultipartBody.Part.createFormData("imageUrl", "image.jpg", imageRequestBody);


                        // Send the notification
                        RetrofitClient.getInstance(FilterUserActivity.this).getApiInterfaces().sendNotification(
                                        meetNameRequestBody
                                        , meetDescriptionRequestBody
                                        , phoneNumbers,
                                        imageID)
                                .enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if (response.isSuccessful()) {

                                            Toast.makeText(FilterUserActivity.this, "Send Successfully", Toast.LENGTH_SHORT).show();
                                            hideProgressDialog();
                                        } else {
                                            try {
                                                String res = response.errorBody().string();
                                                hideProgressDialog();
                                                Log.e("ERROR", res);

                                            } catch (IOException e) {
                                                Toast.makeText(FilterUserActivity.this, "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                            }

                                            Toast.makeText(FilterUserActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        hideProgressDialog();
                                        Toast.makeText(FilterUserActivity.this, "Failed to send notification: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                        // Dismiss the dialog after sending notification
                        alertDialog.dismiss();
                    }
                } else {
                    hideProgressDialog();
                    // Show a toast if title or body is empty
                    Toast.makeText(FilterUserActivity.this, "Title and description cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set negative button (Cancel)
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Show the AlertDialog
        alertDialog.show();
    }


    private void loadData() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("json")) {
            String receivedData = intent.getStringExtra("json");
            if (receivedData != null) {
                try {
                    JSONArray jsonArray = new JSONArray(receivedData);
                    List<MergedUsersItem> dataList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        MergedUsersItem dataItem = new MergedUsersItem();
                        Gson gson = new Gson();
                        String json = gson.toJson(jsonObject);
                        String dist = jsonObject.optString("dist");
                        String name = jsonObject.optString("fName");
                        String lname = jsonObject.optString("lName");
                        System.out.println(dataItem);
                        String fullName = name + " " + lname;

                        if (!TextUtils.isEmpty(dist)) {
                            dataItem.setFName(fullName);
                            dataItem.setDist(dist);

                        }
                        dataList.add(dataItem);
                        Log.e("item", json + "");

                    }
                    // Update the adapter with the parsed data
                    adapter = new FilterAdapter(dataList, this);
                    binding.recyclerView.setAdapter(adapter);
                    return;
                } catch (JSONException e) {
                    Toast.makeText(this, "Error parsing JSON from intent", Toast.LENGTH_SHORT).show();
                }
            }
        }

        loadNetwork();
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                Uri uri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    img.setImageBitmap(bitmap);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }

        }
    });

    private void showProgressDialog() {
        if (!progressDialog.isShowing()) {
            progressDialog.ShowProgress(DialogData.UN_CANCELABLE); // Show the progress dialog
        }
    }

    private void hideProgressDialog() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss(); // Dismiss the progress dialog
        }
    }


}
