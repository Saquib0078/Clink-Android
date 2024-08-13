package com.nirmiteepublic.clink.ui.fragments;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.nirmiteepublic.clink.adapters.BroadcastUniversalAdapter;
import com.nirmiteepublic.clink.databinding.FragmentBroadCastBinding;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;
import com.nirmiteepublic.clink.functions.retrofit.res.PegaCallback;
import com.nirmiteepublic.clink.functions.retrofit.res.PegaResponseManager;
import com.nirmiteepublic.clink.functions.utils.UserUtils;
import com.nirmiteepublic.clink.functions.viewmanagers.PegaFragment;
import com.nirmiteepublic.clink.models.BroadcastModel;
import com.nirmiteepublic.clink.ui.activity.MainActivity;
import com.nirmiteepublic.clink.ui.activity.pages.AdminGraphActivity;
import com.nirmiteepublic.clink.ui.activity.pages.MeetdescryptionActivity;
import com.nirmiteepublic.clink.ui.activity.pages.TaskDescryptionActivity;
import com.visticsolution.posterbanao.dialog.CustomeDialogFragment;
import com.visticsolution.posterbanao.dialog.DialogType;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BroadCastFragment extends PegaFragment {

    public static final int REQUEST_CODE_BROADCAST_PUBLISHED = 1000001;

    private final List<BroadcastModel> broadcastModels = new ArrayList<>();
    String Image;
    String ProfileImage;
    SharedPreferences prefs;
    Context context;
    private FragmentBroadCastBinding binding;
    private boolean isLoading;
    private BroadcastUniversalAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBroadCastBinding.inflate(inflater, container, false);
        context = container.getContext();
        loadData();
        getuser();

        prefs = context.getSharedPreferences("NotificationPrefs", Context.MODE_PRIVATE);
        boolean notificationStatus = prefs.getBoolean("notificationStatus", false);

        if (notificationStatus) {
            prefs.edit().putBoolean("notificationStatus", false).apply();
            showNotificationAlert();
        }
        if (UserUtils.isSuperAdmin()) {
            binding.admin.setVisibility(View.VISIBLE);
            binding.admin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(requireContext(), AdminGraphActivity.class));
                }
            });
        }


        return binding.getRoot();
    }

    private void showNotificationAlert() {
        String lastNotificationType = prefs.getString("last_notification_type", "");
        String lastNotificationID = prefs.getString("last_notification_id", "");
        String lastNotificationDescription = prefs.getString("last_notification_description", "");
        String lastNotificationTitle = prefs.getString("last_notification_title", "");
        String lastNotificationImage = prefs.getString("last_notification_image", "");

//        Toast.makeText(context, "Image: " + lastNotificationImage, Toast.LENGTH_SHORT).show();

        String dialogTitle = lastNotificationTitle;
        String dialogMessage =lastNotificationDescription;
//        boolean showCancel = lastNotificationType.equals("normal");
        boolean showCancelButton = true;
        boolean showOkButton = !lastNotificationType.equals("normal");;

        new CustomeDialogFragment(
                dialogTitle,
                dialogMessage,
                DialogType.INFO,
                showCancelButton,
                true,
                showOkButton,
                lastNotificationImage,
                new CustomeDialogFragment.DialogCallback() {
                    @Override
                    public void onCencel() {
                        // Handle cancel action if needed
                    }

                    @Override
                    public void onSubmit() {
                        openLastNotificationActivity(lastNotificationType, lastNotificationID);
                    }

                    @Override
                    public void onDismiss() {
                        // Handle dismiss action if needed
                    }

                    @Override
                    public void onComplete(Dialog dialog) {
                        // Handle completion action if needed
                        // dialog.cancel();
                    }
                }
        ).show(getChildFragmentManager(), "notification_dialog");
    }
    private void openLastNotificationActivity(String lastNotificationType, String lastNotificationID) {

        Intent intent = null;

        if (lastNotificationType.equals("meeting")) {
            if (lastNotificationID.isEmpty()) {
                ((MainActivity) getActivity()).selectPage(3);
            } else {
                intent = new Intent(getContext(), MeetdescryptionActivity.class);
                intent.putExtra("meetid", lastNotificationID);
            }
        } else if (lastNotificationType.equals("task")) {
            if (lastNotificationID.isEmpty()) {
                ((MainActivity) getActivity()).selectPage(4);
            } else {
                intent = new Intent(getContext(), TaskDescryptionActivity.class);
                intent.putExtra("taskID", lastNotificationID);
            }
        }

        if (intent != null) {
            startActivity(intent);
        }
    }



    private void initScrollListener() {
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == broadcastModels.size() - 1) {
                        isLoading = true;
                        loadMoreData();
                    }
                }
//                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
//                int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
//
//                for (int i = firstVisibleItemPosition; i <= lastVisibleItemPosition; i++) {
//                    View itemView = linearLayoutManager.findViewByPosition(i);
//                    RecyclerView.ViewHolder mHolder = recyclerView.findContainingViewHolder(itemView);
//
//                    if (mHolder instanceof ItemBroadcastMediaViewHolder) {
//
//                        ItemBroadcastMediaViewHolder holder = (ItemBroadcastMediaViewHolder) mHolder;
//
//                        if (holder.binding != null) {
//                            if (!isViewVisible(holder.binding.mediaContainer)) {
//                                Toast.makeText(requireContext(), "Pause", Toast.LENGTH_SHORT).show();
//                                holder.onPause();
//                            }
//                        }
//                    }
//                }
            }
        });
    }

    // Helper method to check if a view is visible on the screen
    private boolean isViewVisible(View view) {
        Rect scrollBounds = new Rect();
        binding.recyclerView.getHitRect(scrollBounds);
        return view.getLocalVisibleRect(scrollBounds);
    }

    private void loadMoreData() {
        int size = broadcastModels.size();
        broadcastModels.add(null);
        adapter.notifyItemInserted(size);
        binding.recyclerView.scrollToPosition(size);

        RetrofitClient.getInstance(requireContext()).getApiInterfaces().getBroadcasts(size - 1).enqueue(new PegaResponseManager(new PegaCallback(requireActivity(), false) {
            @Override
            public void onSuccess(@Nullable JSONObject... data) {
                broadcastModels.remove(size);
                adapter.notifyItemRemoved(size);
                if (data != null) {
                    for (JSONObject object : data) {
                        broadcastModels.add(new Gson().fromJson(object.toString(), BroadcastModel.class));
                    }
                    isLoading = false;
                }
            }
        }));


    }

    // In BroadCastFragment.java
    private void loadData() {
        broadcastModels.clear();
        broadcastModels.add(new BroadcastModel("header")); // For Broadcast header
        adapter = new BroadcastUniversalAdapter(broadcastModels);
        binding.recyclerView.setAdapter(adapter);

        RetrofitClient.getInstance(requireContext()).getApiInterfaces().getBroadcasts(0).enqueue(new PegaResponseManager(new PegaCallback(requireActivity(), false) {
            @Override
            public void onSuccess(@Nullable JSONObject... data) throws JSONException {
                binding.progressBar.setVisibility(View.GONE);
                if (data != null) {
                    for (JSONObject object : data) {
                        broadcastModels.add(new Gson().fromJson(object.toString(), BroadcastModel.class));
                    }
                    isLoading = false;
                    adapter.notifyDataSetChanged();
                    initScrollListener();
                }
            }
        }));
    }
    private void getuser() {
        RetrofitClient.getInstance(requireContext()).getApiInterfaces().getUser(UserUtils.getUserNumber()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String res = response.body().string();
                        JSONObject jsonObject = new JSONObject(res);
                        System.out.println("USer" + jsonObject);
                        JSONObject dataObject = jsonObject.getJSONObject("data"); // Access the "data" object

                        String fName = dataObject.optString("fName");
                        String dp = dataObject.optString("fName");

                        Image = dataObject.optString("Image");
                        String FrameName = dataObject.optString("FrameName");
                        String FrameAdd = dataObject.optString("FrameAdd");
                        ProfileImage = dataObject.optString("dp");

                        UserUtils.setUserDp(RetrofitClient.PROFILE_IMAGE + ProfileImage);
                        UserUtils.setFRAMEADD(FrameAdd);
                        UserUtils.setFRAMENAME(FrameName);

//                        UserUtils.setUserDp(RetrofitClient.USER_BASE_URL + "getUsermedia/" + Image);


                    } catch (IOException e) {
                        Toast.makeText(requireContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        Toast.makeText(requireContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(requireContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // In BroadCastFragment.java
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_BROADCAST_PUBLISHED && resultCode == Activity.RESULT_OK) {
            if (data != null && data.hasExtra("newBroadcast")) {
                String newBroadcastJson = data.getStringExtra("newBroadcast");
                try {
                    JSONObject newBroadcastObject = new JSONObject(newBroadcastJson);
                    BroadcastModel newBroadcast = new BroadcastModel();
                    newBroadcast.setDescription(newBroadcastObject.optString("description", ""));
                    newBroadcast.setBroadcastUrl(newBroadcastObject.optString("broadcastUrl", ""));
                    // Set other fields as needed, possibly with default values

                    // Add the new broadcast to the top of the list (after the header)
                    broadcastModels.add(1, newBroadcast);
                    adapter.notifyItemInserted(1);
                    binding.recyclerView.scrollToPosition(1);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Error processing new broadcast data", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }



}