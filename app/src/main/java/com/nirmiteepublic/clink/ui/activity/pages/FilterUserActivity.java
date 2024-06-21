package com.nirmiteepublic.clink.ui.activity.pages;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.adapters.FilterAdapter;
import com.nirmiteepublic.clink.databinding.ActivityFilterUsersBinding;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;
import com.nirmiteepublic.clink.functions.viewmanagers.PegaAppCompatActivity;
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
import java.util.Arrays;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilterUserActivity extends PegaAppCompatActivity {

    private ActivityFilterUsersBinding binding;
    private AlertDialog progressDialog;
    private FilterAdapter adapter;
    private Bitmap bitmap; // For selected image from gallery
    private Bitmap meetbitmap; // For predefined image when meet is true
    private boolean meet; // Flag to indicate if meet is true
    private List<String> phoneNumbers = new ArrayList<>(); // List to hold phone numbers
      String meeting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFilterUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setWindowThemeSecond();

        meet = getIntent().getBooleanExtra("meetActivity", false);
          meeting=getIntent().getStringExtra("meeting");
        // Initialize views and setup RecyclerView
        initViews();
        loadData();

        // Button to show filter dialog
        binding.filter.setOnClickListener(view -> {
            FilterUserFragment bottomSheetFragment = new FilterUserFragment();
            bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
        });

        // Button to send notification
        binding.btn.setOnClickListener(view -> {
            showProgressDialog();
            sendNotification();
        });
    }

    // Initialize views and adapters
    private void initViews() {
        binding.checkBoxSelectAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                adapter.selectAll();
            } else {
                adapter.clearSelection();
            }
        });
    }

    // Load data from intent or network
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
                        String fullName = name + " " + lname;

                        if (!TextUtils.isEmpty(dist)) {
                            dataItem.setFName(fullName);
                            dataItem.setDist(dist);
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

    // Load data from network using Retrofit
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

    // Show progress dialog
    private void showProgressDialog() {
        if (progressDialog == null || !progressDialog.isShowing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false); // Make the dialog non-cancelable

            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.progress_dialog, null); // Custom layout for progress dialog
            builder.setView(dialogView);

            progressDialog = builder.create();
            progressDialog.show();
        }
    }

    // Hide progress dialog
    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    // Send notification method
    // Send notification method
    private void sendNotification() {
        // Set up the parent view
        LayoutInflater inflater = LayoutInflater.from(this);
        View customDialogView = inflater.inflate(R.layout.notification_alertdialog, null);

        // Access EditText views and button in the custom layout
        EditText titleInput = customDialogView.findViewById(R.id.title);
        EditText bodyInput = customDialogView.findViewById(R.id.descryption);
        Button btnUploadImage = customDialogView.findViewById(R.id.btnUploadImage);
        Button sendButton = customDialogView.findViewById(R.id.send);
        ImageView img = customDialogView.findViewById(R.id.img);

        // Pre-fill the fields if intent extras are present
        String intentTitle = getIntent().getStringExtra("title");
        String intentBody = getIntent().getStringExtra("body");
        String intentMeetingType = getIntent().getStringExtra("meetingType");
        byte[] intentImageUrl = getIntent().getByteArrayExtra("imageUrl");

        if (intentTitle != null) {
            titleInput.setText(intentTitle);
        }
        if (intentBody != null) {
            bodyInput.setText(intentBody);
        }
        if (intentImageUrl != null && meet) {
            meetbitmap = BitmapFactory.decodeByteArray(intentImageUrl, 0, intentImageUrl.length);
            Glide.with(this).load(meetbitmap).into(img);
        }

        // Set up the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(customDialogView);

        // Create the AlertDialog
        AlertDialog alertDialog = builder.create();

        // Set an OnClickListener for the Upload Image button
        btnUploadImage.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncher.launch(intent);
        });

        // Set an OnClickListener for the Send button
        sendButton.setOnClickListener(view -> {
            String title = titleInput.getText().toString().trim();
            String body = bodyInput.getText().toString().trim();

            if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(body)) {
                // Determine which bitmap to use based on the meet flag
                Bitmap selectedBitmap = meet ? meetbitmap : bitmap;

                if (selectedBitmap != null) {
                    handleSendButtonClick(title, body, intentMeetingType, selectedBitmap);
                    alertDialog.dismiss();
                } else {
                    hideProgressDialog();
                    Toast.makeText(FilterUserActivity.this, "Please select an image", Toast.LENGTH_SHORT).show();
                }
            } else {
                hideProgressDialog();
                Toast.makeText(FilterUserActivity.this, "Title and description cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        // Set negative button (Cancel)
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        // Show the AlertDialog
        alertDialog.show();
    }


    // Handle image selection from gallery
    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            if (data != null) {
                Uri uri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    });

    // Handle sending notification with selected or predefined image
    private void handleSendButtonClick(String title, String body, String meetingType, Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT);

        RequestBody meetingTypeRequestBody = RequestBody.create(MediaType.parse("text/plain"), meetingType);
        RequestBody meetNameRequestBody = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody meetDescriptionRequestBody = RequestBody.create(MediaType.parse("text/plain"), body);
        RequestBody imageRequestBody = RequestBody.create(MediaType.parse("image/jpeg"), Base64.decode(base64Image, Base64.DEFAULT));
        MultipartBody.Part imageID = MultipartBody.Part.createFormData("imageUrl", "image.jpg", imageRequestBody);
        List<MergedUsersItem> selectedUsers = adapter.getSelectedUsers();

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
                meetingTypeRequestBody
        ).enqueue(new Callback<ResponseBody>() {
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
                        Toast.makeText(FilterUserActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
    }
}
