package com.nirmiteepublic.clink.ui.activity.pages;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.databinding.ActivityTaskDescryptionBinding;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;
import com.nirmiteepublic.clink.functions.utils.UserUtils;
import com.nirmiteepublic.clink.functions.viewmanagers.PegaAppCompatActivity;
import com.nirmiteepublic.clink.models.Task;
import com.nirmiteepublic.clink.models.TaskModelResponse;
import com.nirmiteepublic.clink.models.UserDataPrimary;
import com.nirmiteepublic.clink.models.UserModelPrimary;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class TaskDescryptionActivity extends PegaAppCompatActivity {

    private static final String TAG = "TaskDescryptionActivity";
    private static final int EDIT_TASK_REQUEST_CODE = 1002;

    ActivityTaskDescryptionBinding binding;
    String taskID;
    private AlertDialog progressDialog;
    private boolean isDataLoaded = false;
    private boolean isImageLoaded = false;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTaskDescryptionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        UserUtils.init(this);

        setupViews();
        handleIntent();
        loadUserImage();
    }

    private void setupViews() {
        setWindowThemeSecond();

        if (UserUtils.isSuperAdmin()) {
            binding.edit.setVisibility(View.VISIBLE);
            binding.viewUsers.setVisibility(View.VISIBLE);
        }

        binding.edit.setOnClickListener(v -> {
            Intent sendIntent = new Intent(TaskDescryptionActivity.this, CreateTaskActivity.class);
            sendIntent.putExtra("EDITTASK", true);
            sendIntent.putExtra("TaskId", taskID);
            startActivityForResult(sendIntent, EDIT_TASK_REQUEST_CODE);
        });

        binding.viewUsers.setOnClickListener(v -> {
            Intent intent1 = new Intent(TaskDescryptionActivity.this, UserCompletedTaskActivity.class);
            intent1.putExtra("taskID", taskID);
            startActivity(intent1);
        });

        binding.comment.setOnClickListener(view ->
                startActivity(new Intent(TaskDescryptionActivity.this, TaskCommentActivity.class))
        );

        binding.btncomplete.setOnClickListener(view ->
                markTaskAsCompleted(taskID, UserUtils.getUserId())
        );
    }

    private void handleIntent() {
         intent = getIntent();
        taskID = intent.getStringExtra("taskID");
        String relatedId = intent.getStringExtra("relatedId");

        showProgressBar();

        if (relatedId != null) {
            fetchTaskDetails(relatedId);
        } else if (taskID != null) {
            fetchTaskDetails(taskID);
        } else {
            Toast.makeText(this, "Task Expired or not Available", Toast.LENGTH_SHORT).show();
            hideProgressBar();
        }

        if (intent.hasExtra("imageID") && intent.hasExtra("taskID")) {
            String receivedImageID = intent.getStringExtra("imageID");
            loadImage(binding.bannerimage, receivedImageID);
        }
    }

    private void loadUserImage() {
        loadImage(binding.imgProfile, UserUtils.getUserDp());
    }

    private void loadImage(ImageView imageView, String imageUrl) {
        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        isImageLoaded = true;
                        checkAllLoadingComplete();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        isImageLoaded = true;
                        checkAllLoadingComplete();
                        return false;
                    }
                })
                .into(imageView);
    }

    private void showProgressBar() {
        if (progressDialog == null || !progressDialog.isShowing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            View dialogView = getLayoutInflater().inflate(R.layout.progress_dialog, null);
            builder.setView(dialogView);
            progressDialog = builder.create();
            if (progressDialog.getWindow() != null) {
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
            progressDialog.show();
        }
    }

    private void hideProgressBar() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void markTaskAsCompleted(String taskId, String userId) {
        showProgressBar();
        String apiUrl = RetrofitClient.TASK_BASE_URL + "tasks/" + taskId + "/complete";

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("userId", userId);
        } catch (JSONException e) {
            e.printStackTrace();
            hideProgressBar();
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                apiUrl,
                requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideProgressBar();
                        try {
                            if (response.has("status") && response.getString("status").equals("success")) {
                                Toast.makeText(TaskDescryptionActivity.this, "Task Completed", Toast.LENGTH_SHORT).show();

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        binding.btncomplete.setText("You Have Already Completed Task");
                                        binding.btncomplete.setBackgroundResource(R.drawable.sign_disabled);
                                        binding.btncomplete.setEnabled(false);
                                    }
                                });
                            } else {
                                Toast.makeText(TaskDescryptionActivity.this, "Task Not Completed", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(TaskDescryptionActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideProgressBar();
                        error.printStackTrace();
                    }
                }
        );

        Volley.newRequestQueue(this).add(request);
    }

    private void fetchTaskDetails(String taskID) {
        RetrofitClient.getInstance(this).getApiInterfaces().getTaskById(taskID).enqueue(new Callback<TaskModelResponse>() {
            @Override
            public void onResponse(Call<TaskModelResponse> call, retrofit2.Response<TaskModelResponse> response) {
                if (response.isSuccessful()) {
                    Task taskModel = response.body().getTask();
                    if (taskModel != null) {
                        updateTaskUI(taskModel);
                        fetchUser(taskModel.getCreatedBy());
                    }
                }
                isDataLoaded = true;
                checkAllLoadingComplete();
            }

            @Override
            public void onFailure(Call<TaskModelResponse> call, Throwable t) {
                hideProgressBar();
                Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTaskUI(Task taskModel) {
        loadImage(binding.bannerimage, RetrofitClient.TASKIMAGE_BASE_URL + taskModel.getImageID());
        binding.date.setText(taskModel.getTime());
        binding.title.setText(taskModel.getTaskName());
        binding.descryption.setText(taskModel.getTaskDescription());
        updateTaskUrl(taskModel.getTaskUrl());
        updateCompletionStatus(taskModel.getCompletedUsers());

        taskID = intent.getStringExtra("taskID");
    }

    private void updateTaskUrl(String taskUrl) {
        if (taskUrl != null && !taskUrl.isEmpty()) {
            binding.taskUrl.setText(taskUrl);
            binding.taskUrl.setAutoLinkMask(Linkify.WEB_URLS);
            binding.taskUrl.setLinksClickable(true);
            binding.taskUrl.setMovementMethod(LinkMovementMethod.getInstance());
            binding.taskUrl.setLinkTextColor(ContextCompat.getColor(this, R.color.cyan_process));
            binding.taskUrl.setOnClickListener(v -> {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(taskUrl)));
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(this, "No application can handle this request. Please install a web browser", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void updateCompletionStatus(List<Object> completedUsers) {
        if (completedUsers != null && !completedUsers.isEmpty()) {
            for (Object userObject : completedUsers) {
                if (userObject instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> completedUser = (Map<String, Object>) userObject;
                    String completedUserId = (String) completedUser.get("_id");
                    if (completedUserId.equals(UserUtils.getUserId())) {
                        binding.btncomplete.setText("You Have Already Completed Task");
                        binding.btncomplete.setBackgroundResource(R.drawable.sign_disabled);
                        binding.btncomplete.setEnabled(false);
                        break;
                    }
                }
            }
        }
    }

    private void fetchUser(String id) {
        RetrofitClient.getInstance(this).getApiInterfaces().getPrimaryUser(id).enqueue(new Callback<UserModelPrimary>() {
            @Override
            public void onResponse(Call<UserModelPrimary> call, retrofit2.Response<UserModelPrimary> response) {
                if (response.isSuccessful()) {
                    UserDataPrimary userModelPrimary = response.body().getUserDataPrimary();
                    fetchUserDetails(userModelPrimary.getNum());
                }
            }

            @Override
            public void onFailure(Call<UserModelPrimary> call, Throwable t) {
                Toast.makeText(TaskDescryptionActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchUserDetails(String num) {
        RetrofitClient.getInstance(this).getApiInterfaces().getUserData(num).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    String res = response.body().string();
                    JSONObject jsonObject = new JSONObject(res);
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    updateUserUI(dataObject);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(TaskDescryptionActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserUI(JSONObject dataObject) {
        binding.city.setText(dataObject.optString("dist"));
        String fullname = dataObject.optString("fName") + " " + dataObject.optString("lName");
        binding.name.setText(fullname);
        loadImage(binding.profile, RetrofitClient.PROFILE_IMAGE + dataObject.optString("dp"));
    }

    private void checkAllLoadingComplete() {
        if (isDataLoaded && isImageLoaded) {
            hideProgressBar();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_TASK_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null && data.hasExtra("updatedTaskId")) {
                String updatedTaskId = data.getStringExtra("updatedTaskId");
                taskID = updatedTaskId;  // Update the taskID
                showProgressBar();
                fetchTaskDetails(updatedTaskId);
            }
        }
    }
}