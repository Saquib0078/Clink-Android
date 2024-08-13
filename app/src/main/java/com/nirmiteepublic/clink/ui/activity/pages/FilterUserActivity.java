package com.nirmiteepublic.clink.ui.activity.pages;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.adapters.FilterAdapter;
import com.nirmiteepublic.clink.databinding.ActivityFilterUsersBinding;
import com.nirmiteepublic.clink.functions.helpers.PegaProgress;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;
import com.nirmiteepublic.clink.functions.utils.SharedPreferenceUtils;
import com.nirmiteepublic.clink.functions.utils.UserUtils;
import com.nirmiteepublic.clink.functions.viewmanagers.PegaAppCompatActivity;
import com.nirmiteepublic.clink.models.MergedUserResponse;
import com.nirmiteepublic.clink.models.MergedUsersItem;
import com.nirmiteepublic.clink.ui.fragments.viewpager.FilterUserFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilterUserActivity extends PegaAppCompatActivity {

    private final List<String> phoneNumbers = new ArrayList<>();
    String meeting;
    String meetingType, meetingDate;
    ImageView img;
    String retlated_ID;
    private ActivityFilterUsersBinding binding;
    private FilterAdapter adapter;
    SharedPreferenceUtils sharedPreferenceUtils;

    private Bitmap bitmap;
    private Bitmap meetbitmap;
    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            if (data != null) {
                Uri uri = data.getData();
                try {
                    meetbitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    if (meetbitmap != null && img != null) {
                        Glide.with(this).load(meetbitmap).into(img);
                    } else {
                        Toast.makeText(this, "Failed to load meeting image", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    });
    private boolean meet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFilterUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sharedPreferenceUtils = SharedPreferenceUtils.getInstance(this);
        if(UserUtils.isSuperAdmin()){
            binding.btn.setVisibility(View.VISIBLE);
        }else{
            binding.btn.setVisibility(View.GONE);

        }

        setWindowThemeSecond();

        meet = getIntent().getBooleanExtra("meetActivity", false);
        meeting = getIntent().getStringExtra("meeting");
        meetingType = getIntent().getStringExtra("taskType");
        if (meetingType == null) {
            meetingType = "";
        }
        if (meetingType.equals("meeting")) {
            meetingDate = getIntent().getStringExtra("date");
        }
        retlated_ID = getIntent().getStringExtra("related_ID");

        initViews();
        loadData();

        binding.filter.setOnClickListener(view -> {
            FilterUserFragment bottomSheetFragment = new FilterUserFragment();
            bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
        });

        binding.btn.setOnClickListener(view -> {
            sendNotification();
        });
    }

    private void initViews() {
        binding.checkBoxSelectAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                adapter.selectAll();
            } else {
                adapter.clearSelection();
            }
        });
    }

    private void loadData() {
        Intent intent = getIntent();

        if (intent != null && intent.hasExtra("json")) {
            String receivedData = intent.getStringExtra("json");
            if (receivedData == null) {
                Toast.makeText(this, "No Matches found", Toast.LENGTH_SHORT).show();
            }
            if (receivedData != null) {
                try {
                    JSONArray jsonArray = new JSONArray(receivedData);
                    List<MergedUsersItem> dataList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        MergedUsersItem dataItem = new MergedUsersItem();
                        Gson gson = new Gson();
                        System.out.println(jsonObject);
                        String json = gson.toJson(jsonObject);
                        String dist = jsonObject.optString("dist");
                        String name = jsonObject.optString("fName");
                        String lname = jsonObject.optString("lName");
                        String dp = jsonObject.optString("dp");


                        String fullName = name + " " + lname;

                        if (!TextUtils.isEmpty(dist)) {
                            dataItem.setFName(fullName);
                            dataItem.setDist(dist);
                            dataItem.setDp(dp);
                        }
                        dataList.add(dataItem);
                    }
                    adapter = new FilterAdapter(dataList, this);
                    binding.recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    Toast.makeText(this, "Error parsing JSON from intent", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        } else {
            loadNetwork();
        }
    }

    private void loadNetwork() {
        showProgressDialog();
        RetrofitClient.getInstance(this).getApiInterfaces().getUSER().enqueue(new Callback<MergedUserResponse>() {
            @Override
            public void onResponse(Call<MergedUserResponse> call, Response<MergedUserResponse> response) {
                hideProgressDialog();
                if (response.isSuccessful()) {
                    adapter = new FilterAdapter(response.body().getMergedUsers(), FilterUserActivity.this);
                    binding.recyclerView.setAdapter(adapter);
                } else {
                    Snackbar.make(findViewById(android.R.id.content), "No Data Found", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MergedUserResponse> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(FilterUserActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showProgressDialog() {
        PegaProgress.showProgressBar(this);
    }

    private void hideProgressDialog() {
        PegaProgress.hideProgressBar();
    }

    private void sendNotification() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View customDialogView = inflater.inflate(R.layout.notification_alertdialog, null);

        EditText titleInput = customDialogView.findViewById(R.id.title);
        EditText bodyInput = customDialogView.findViewById(R.id.descryption);
        Button btnUploadImage = customDialogView.findViewById(R.id.btnUploadImage);
        Button sendButton = customDialogView.findViewById(R.id.send);
        img = customDialogView.findViewById(R.id.img);

        String intentTitle = getIntent().getStringExtra("title");
        String intentBody = getIntent().getStringExtra("body");
        String intentImageUrlStr = getIntent().getStringExtra("imageUrl");

        if (intentImageUrlStr != null) {
            Bitmap bitmap1 = BitmapFactory.decodeFile(intentImageUrlStr);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap1.compress(Bitmap.CompressFormat.PNG, 70, stream);
            byte[] intentImageUrl = stream.toByteArray();

            if (intentTitle != null) {
                titleInput.setText(intentTitle);
            }
            if (intentBody != null) {
                bodyInput.setText(intentBody);
            }

            meetbitmap = BitmapFactory.decodeByteArray(intentImageUrl, 0, intentImageUrl.length);
            if (meetbitmap != null) {
                Glide.with(this).load(meetbitmap).into(img);
            } else {
                Toast.makeText(this, "Failed to load meeting image", Toast.LENGTH_SHORT).show();
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(customDialogView);

        AlertDialog alertDialog = builder.create();

        btnUploadImage.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncher.launch(intent);
        });

        sendButton.setOnClickListener(view -> {
            String title = titleInput.getText().toString().trim();
            String body = bodyInput.getText().toString().trim();

            if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(body)) {
                showProgressDialog();  // Show loading indicator here

                Bitmap selectedBitmap = meetbitmap;
                if (selectedBitmap != null) {
                    handleSendButtonClick(title, body, meetingType, selectedBitmap);
                    alertDialog.dismiss();
                } else {
                    hideProgressDialog();
                    if (meet) {
                        Snackbar.make(customDialogView, "Meeting image is not available", Snackbar.LENGTH_SHORT).show();
                    } else {
                        Snackbar.make(customDialogView, "Please Select an Image", Snackbar.LENGTH_SHORT).show();
                    }
                }
            } else {
                Snackbar.make(customDialogView, "Title and description cannot be empty", Snackbar.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        alertDialog.setCancelable(true);

        alertDialog.show();
    }

    private void handleSendButtonClick(String title, String body, String meetingType, Bitmap bitmap) {
        if (bitmap == null) {
            Toast.makeText(FilterUserActivity.this, "Image is not available", Toast.LENGTH_SHORT).show();
            hideProgressDialog();
            return;
        }

        if (adapter == null) {
            Toast.makeText(FilterUserActivity.this, "No users selected", Toast.LENGTH_SHORT).show();
            hideProgressDialog();
            return;
        }
        if (title == null || body == null) {
            Toast.makeText(this, "All Fields Are Required", Toast.LENGTH_SHORT).show();
            return;
        }

        List<MergedUsersItem> selectedUsers = adapter.getSelectedUsers();
        if (selectedUsers.isEmpty()) {
            Snackbar.make(findViewById(android.R.id.content), "Please Select At least One User", Snackbar.LENGTH_SHORT).show();
            hideProgressDialog();
            return;
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT);
        if (meetingType == null || meetingType.isEmpty()) meetingType = "normal";
        RequestBody meetingTypeRequestBody = RequestBody.create(MediaType.parse("text/plain"), meetingType);
        RequestBody meetNameRequestBody = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody meetDescriptionRequestBody = RequestBody.create(MediaType.parse("text/plain"), body);
        RequestBody relatedIDRequestBody = RequestBody.create(MediaType.parse("text/plain"), retlated_ID != null ? retlated_ID : "");
        RequestBody meetDateRequestBody;
        if (meetingDate != null && !meetingDate.isEmpty()) {
            meetDateRequestBody = RequestBody.create(MediaType.parse("text/plain"), meetingDate);
        } else {
            String defaultDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            meetDateRequestBody = RequestBody.create(MediaType.parse("text/plain"), defaultDate);
        }
        RequestBody imageRequestBody = RequestBody.create(MediaType.parse("image/jpeg"), Base64.decode(base64Image, Base64.DEFAULT));
        MultipartBody.Part imageID = MultipartBody.Part.createFormData("imageUrl", "image.jpg", imageRequestBody);

        List<String> phoneNumbers = new ArrayList<>();

        for (MergedUsersItem user : selectedUsers) {
            String number = user.getNum();
            phoneNumbers.add(number);
            System.out.println(number);
        }

        RetrofitClient.getInstance(FilterUserActivity.this).getApiInterfaces().sendNotification(
                meetNameRequestBody,
                meetDescriptionRequestBody,
                phoneNumbers,
                imageID,
                meetingTypeRequestBody,
                relatedIDRequestBody,
                meetDateRequestBody
        ).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hideProgressDialog();
                if (response.isSuccessful()) {
                    List<String> phoneNumbers = new ArrayList<>();
                    for (MergedUsersItem user : adapter.getSelectedUsers()) {
                        phoneNumbers.add(user.getNum());
                    }
                    sharedPreferenceUtils.putString("selectedUsers", String.join(",", phoneNumbers));


                    Toast.makeText(FilterUserActivity.this, "Notification Sent Successfully", Toast.LENGTH_SHORT).show();

                    finish();
                } else {
                    try {
                        String res = response.errorBody().string();
                        Log.e("ERROR", res);
                    } catch (IOException e) {
                        Toast.makeText(FilterUserActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                    Snackbar.make(findViewById(android.R.id.content), "Failed to Send Notification", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(FilterUserActivity.this, "Failed to send notification: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}