package com.nirmiteepublic.clink.ui.activity.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.adapters.CompletedUserAdapter;
import com.nirmiteepublic.clink.databinding.ActivityUserCompletedTaskBinding;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;
import com.nirmiteepublic.clink.functions.viewmanagers.PegaAppCompatActivity;
import com.nirmiteepublic.clink.models.CompleteduserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserCompletedTaskActivity extends PegaAppCompatActivity {
    String  taskID;
    ActivityUserCompletedTaskBinding binding;
    ArrayList<CompleteduserModel> model = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserCompletedTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        setWindowThemeSecond();

        if (intent.hasExtra("taskID")) {
            taskID = intent.getStringExtra("taskID");
            getTaskUsers(taskID);



        }
    }

    private void getTaskUsers(String TaskId) {
        // Append the task ID to the base URL
        String url = RetrofitClient.TASK_BASE_URL + "/tasks/" + TaskId;

        // Create a new JsonObjectRequest for the GET request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject=response.getJSONObject("task");
                            JSONArray array=jsonObject.getJSONArray("completedUsers");

                            for (int i=0;i<array.length();i++){
                                JSONObject userObject=array.getJSONObject(i);
                                String fName = userObject.getString("fName");
                                String lName = userObject.getString("lName");
                                String num = userObject.getString("num");
                                String dp = userObject.optString("dp");
                                Log.d("User", "First Name: " + fName);

                                CompleteduserModel user = new CompleteduserModel(fName, lName, num, dp);
                                model.add(user);
                            }
                            CompletedUserAdapter adapter=new CompletedUserAdapter(model);
                            binding.recview.setAdapter(adapter);

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        // Handle the response
                        // Here, you can parse the response JSON or perform any other operations
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        // Here, you can handle errors such as network issues or server errors
                        Log.e("Error", error.toString());
                    }
                });

        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
}