package com.nirmiteepublic.clink.ui.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.adapters.TasksAdapter;
import com.nirmiteepublic.clink.databinding.FragmentTaskBinding;
import com.nirmiteepublic.clink.functions.helpers.Prefs;
import com.nirmiteepublic.clink.functions.utils.ApiManager;
import com.nirmiteepublic.clink.functions.utils.UserUtils;
import com.nirmiteepublic.clink.functions.utils.Utils;
import com.nirmiteepublic.clink.functions.viewmanagers.PegaFragment;
import com.nirmiteepublic.clink.models.MeetModel;
import com.nirmiteepublic.clink.models.TaskModel;
import com.nirmiteepublic.clink.ui.activity.pages.CreateTaskActivity;
import com.nirmiteepublic.clink.ui.activity.pages.TaskDescryptionActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TaskFragment extends PegaFragment {

    FragmentTaskBinding binding;
    Prefs prefs;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentTaskBinding.inflate(inflater, container, false);
        binding.progressBar.setVisibility(View.VISIBLE);
        List<TaskModel> taskModelList = new ArrayList<>();
        TasksAdapter adapter = new TasksAdapter(taskModelList);
        binding.recyclerView.setAdapter(adapter);
         prefs=new Prefs(requireContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = requireActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(requireContext(), R.color.cyan_process));
        }
        String userRole = String.valueOf(UserUtils.getUserRole());
        if (userRole == "CO_ADMIN" || userRole=="SUPER_CO_ADMIN") {
            binding.fab.setVisibility(View.VISIBLE);
        } else {
            binding.fab.setVisibility(View.GONE);

        }

       binding.fab.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               startActivity(new Intent(requireContext(), CreateTaskActivity.class));
           }
       });
        ApiManager.getTask(requireContext(), new ApiManager.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                // Handle the successful API response
                try {
                    if (response.has("task")) {
                        JSONArray meetingsArray = response.getJSONArray("task");
                        binding.progressBar.setVisibility(View.GONE);
                        // Process each meeting in the array
                        for (int i = 0; i < meetingsArray.length(); i++) {
                            JSONObject meetingObject = meetingsArray.getJSONObject(i);
                            // Retrieve details from the meeting object

                            // Check if the key exists before retrieving its value
                            String taskName = meetingObject.getString("taskName");
                            String taskDescription = meetingObject.getString("taskDescription");
                            String date = meetingObject.getString("date");
                            String time = meetingObject.getString("time");
                            String imageID = meetingObject.getString("imageID");
                            String id=meetingObject.getString("_id");

//
                            String image= Utils.loadTaskImg(imageID);
                            System.out.println(meetingObject);
//



                            taskModelList.add(new TaskModel(taskName, taskDescription, date, time, image, id));
                        }

                        adapter.notifyDataSetChanged();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String errorMessage) {
                // Handle errors
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(requireContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
        return binding.getRoot();
    }


}