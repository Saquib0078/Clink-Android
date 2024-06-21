package com.nirmiteepublic.clink.ui.fragments.viewpager;


import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nirmiteepublic.clink.adapters.MeetAdapter;
import com.nirmiteepublic.clink.databinding.CreateMeetingLayoutBinding;
import com.nirmiteepublic.clink.databinding.FragmentMeetingBinding;
import com.nirmiteepublic.clink.functions.utils.ApiManager;
import com.nirmiteepublic.clink.functions.utils.Meetingutils;
import com.nirmiteepublic.clink.functions.utils.UserUtils;
import com.nirmiteepublic.clink.functions.utils.Utils;
import com.nirmiteepublic.clink.functions.viewmanagers.PegaFragment;
import com.nirmiteepublic.clink.models.MeetModel;
import com.nirmiteepublic.clink.ui.activity.pages.CreateMeetActivity;
import com.nirmiteepublic.clink.ui.activity.pages.SelectMediaActivity;
import com.pegalite.popups.DialogData;
import com.pegalite.popups.PegaProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MeetingFragment extends PegaFragment {
    private PegaProgressDialog progressDialog;

    FragmentMeetingBinding binding;

    RequestQueue queue;
    Bitmap bitmap;

    Meetingutils meetingutils;
    SelectMediaActivity selectMediaActivity;
    private CreateMeetingLayoutBinding layoutBinding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentMeetingBinding.inflate(inflater, container, false);
        List<MeetModel> taskModelList = new ArrayList<>();
        queue = Volley.newRequestQueue(requireContext());
        meetingutils = new Meetingutils();
        MeetAdapter adapter = new MeetAdapter(taskModelList);
        selectMediaActivity = new SelectMediaActivity();
        binding.recyclerView.setAdapter(adapter);
        progressDialog = new PegaProgressDialog(requireActivity());

        binding.progressBar.setVisibility(View.VISIBLE);
        progressDialog.setMessage("Loading...");
        showProgressDialog();
        ApiManager.getLiveMeetings(requireContext(), new ApiManager.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                hideProgressDialog();
                // Handle the successful API response
                try {
                    // Assuming the API returns a JSON array with the key "meeting"
                    if (response.has("meeting")) {
                        binding.progressBar.setVisibility(View.GONE);

                        JSONArray meetingsArray = response.getJSONArray("meeting");

                        // Process each meeting in the array
                        for (int i = 0; i < meetingsArray.length(); i++) {
                            JSONObject meetingObject = meetingsArray.getJSONObject(i);

                            String meetName = meetingObject.getString("meetName");
                            String meetDescription = meetingObject.getString("meetDescription");
                            String date = meetingObject.getString("date");
                            String time = meetingObject.getString("time");
                            String imageID = meetingObject.getString("imageID");
                            boolean live = meetingObject.getBoolean("live");
                            String id = meetingObject.getString("_id");
                            String image = Utils.loadMeetingImage(imageID);
                            //Limited Users
                            String radio = meetingObject.getString("radioButtonValue");


                            UserUtils.setRADIO(radio);


                            taskModelList.add(new MeetModel(meetName, meetDescription, date, time, image, id, live));
                        }
adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    hideProgressDialog();
                    Toast.makeText(requireContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String errorMessage) {
                // Handle errors
                hideProgressDialog();

                Toast.makeText(requireContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
        String userRole = String.valueOf(UserUtils.getUserRole());
        if (userRole == "CO_ADMIN" || userRole == "SUPER_CO_ADMIN") {
            binding.createmeet.setVisibility(View.VISIBLE);
        } else {
            binding.createmeet.setVisibility(View.GONE);

        }

        binding.createmeet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(requireContext(), CreateMeetActivity.class));
            }
        });


        return binding.getRoot();
    }



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
