package com.nirmiteepublic.clink.ui.activity.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.databinding.ActivityTaskDescryptionBinding;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;
import com.nirmiteepublic.clink.functions.utils.UserUtils;
import com.nirmiteepublic.clink.functions.viewmanagers.PegaAppCompatActivity;
import com.nirmiteepublic.clink.models.Task;
import com.nirmiteepublic.clink.models.TaskModelResponse;
import com.nirmiteepublic.clink.models.UserDataPrimary;
import com.nirmiteepublic.clink.models.UserModelPrimary;
import com.pegalite.popups.DialogData;
import com.pegalite.popups.PegaProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class TaskDescryptionActivity extends PegaAppCompatActivity {

    ActivityTaskDescryptionBinding binding;
    private PegaProgressDialog progressDialog;

    String taskID;
    boolean userCompletedTask = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTaskDescryptionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.progressBar.setVisibility(View.VISIBLE);


        setWindowThemeSecond();

        Glide.with(this)
                .load(UserUtils.getUserDp())
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .into(binding.imgProfile);

        Intent intent = getIntent();
        if (intent.hasExtra("imageID") && intent.hasExtra("taskID")) {
            String receivedImageID = intent.getStringExtra("imageID");
            taskID = intent.getStringExtra("taskID");

            Glide.with(this)
                    .load(receivedImageID)
                    .into(binding.bannerimage);
        }

        if (UserUtils.isSuperAdmin()) {
            binding.viewUsers.setVisibility(View.VISIBLE);
        }

        binding.viewUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(TaskDescryptionActivity.this, UserCompletedTaskActivity.class);
                intent1.putExtra("taskID", taskID);
                startActivity(intent1);
            }
        });

        fetchTaskDetails(taskID);

        binding.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TaskDescryptionActivity.this, TaskCommentActivity.class));
            }
        });

        binding.btncomplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userId = UserUtils.getUserId();
                markTaskAsCompleted(taskID, userId);
            }
        });
    }

    private void showProgressBar() {
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        binding.progressBar.setVisibility(View.GONE);
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
                        Toast.makeText(TaskDescryptionActivity.this, "Error in network request: " + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Add the request to the Volley request queue
        Volley.newRequestQueue(this).add(request);
    }

    private void fetchTaskDetails(String taskID) {
        showProgressBar();
        RetrofitClient.getInstance(this).getApiInterfaces().getTaskById(taskID).enqueue(new Callback<TaskModelResponse>() {
            @Override
            public void onResponse(Call<TaskModelResponse> call, retrofit2.Response<TaskModelResponse> response) {
                hideProgressBar();
                if (response.isSuccessful()) {
                    Task taskModel = response.body().getTask();
                    if (taskModel != null) {
                        Gson gson = new Gson();
                        String json = gson.toJson(taskModel);
                        String createdBy = taskModel.getCreatedBy();
                        String date = taskModel.getTime();
                        String description = taskModel.getTaskDescription();
                        String image = taskModel.getDp();
                        String taskname = taskModel.getTaskName();

                        List<Object> completedUsers = taskModel.getCompletedUsers();
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
                                    }
                                }
                            }
                        }

                        binding.date.setText(date);
                        binding.title.setText(taskname);
                        binding.descryption.setText(description);

                        fetchUser(createdBy);
                    }
                }
            }

            @Override
            public void onFailure(Call<TaskModelResponse> call, Throwable t) {
                hideProgressBar();
                Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchUser(String id) {
        showProgressBar();
        RetrofitClient.getInstance(this).getApiInterfaces().getPrimaryUser(id).enqueue(new Callback<UserModelPrimary>() {
            @Override
            public void onResponse(Call<UserModelPrimary> call, retrofit2.Response<UserModelPrimary> response) {
                hideProgressBar();
                if (response.isSuccessful()) {
                    UserDataPrimary userModelPrimary = response.body().getUserDataPrimary();
                    String num = userModelPrimary.getNum();
                    fetchUserDetails(num);
                }
            }

            @Override
            public void onFailure(Call<UserModelPrimary> call, Throwable t) {
                hideProgressBar();
                Toast.makeText(TaskDescryptionActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchUserDetails(String num) {
        showProgressBar();
        RetrofitClient.getInstance(this).getApiInterfaces().getUserData(num).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                hideProgressBar();
                try {
                    String res = response.body().string();
                    JSONObject jsonObject = new JSONObject(res);
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    String dist = dataObject.optString("dist");
                    binding.city.setText(dist);
                    String name = dataObject.optString("fName");
                    String lname = dataObject.optString("lName");
                    String fullname = name + " " + lname;
                    String image = dataObject.optString("dp");
                    binding.name.setText(fullname);

                    String url = RetrofitClient.PROFILE_IMAGE + image;
                    Glide.with(TaskDescryptionActivity.this)
                            .load(url)
                            .placeholder(R.drawable.default_image)
                            .into(binding.profile);

                } catch (IOException | JSONException e) {
                    hideProgressBar();
                    Toast.makeText(TaskDescryptionActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideProgressBar();
                Toast.makeText(TaskDescryptionActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
