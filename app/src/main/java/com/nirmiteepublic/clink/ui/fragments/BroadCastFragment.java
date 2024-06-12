package com.nirmiteepublic.clink.ui.fragments;

import android.content.Intent;
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

import com.google.android.exoplayer2.util.Log;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.nirmiteepublic.clink.adapters.BroadcastUniversalAdapter;
import com.nirmiteepublic.clink.databinding.FragmentBroadCastBinding;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;
import com.nirmiteepublic.clink.functions.retrofit.res.PegaCallback;
import com.nirmiteepublic.clink.functions.retrofit.res.PegaResponseManager;
import com.nirmiteepublic.clink.functions.utils.UserUtils;
import com.nirmiteepublic.clink.functions.viewmanagers.PegaFragment;
import com.nirmiteepublic.clink.models.BroadcastModel;
import com.nirmiteepublic.clink.ui.activity.pages.AdminGraphActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BroadCastFragment extends PegaFragment {

    public static final int REQUEST_CODE_BROADCAST_PUBLISHED = 100;
    private final List<BroadcastModel> broadcastModels = new ArrayList<>();
    private FragmentBroadCastBinding binding;
    private boolean isLoading;



    private BroadcastUniversalAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBroadCastBinding.inflate(inflater, container, false);

        loadData();

        getuser();







        if (UserUtils.isSuperAdmin()) {
            binding.admin.setVisibility(View.VISIBLE);
            binding.admin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(requireContext(), AdminGraphActivity.class));
                }
            });
        }



//        mDatabase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                int onlineUsersCount = 0;
//                // Iterate through the users
//                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
//                    // Get the online status of each user
//                    Boolean isOnline = userSnapshot.child("online").getValue(Boolean.class);
//                    // Check if the user is online (status is true)
//                    if (isOnline != null && isOnline) {
//                        onlineUsersCount++;
//                    }
//                }
//                Toast.makeText(requireContext(), ""+onlineUsersCount, Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(requireContext(), "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//                // Handle errors
//            }
//        });
        return binding.getRoot();
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

    private void loadData() {
        broadcastModels.add(new BroadcastModel("header")); // For Broadcast header
        adapter = new BroadcastUniversalAdapter(broadcastModels);
        binding.recyclerView.setAdapter(adapter);

        RetrofitClient.getInstance(requireContext()).getApiInterfaces().getBroadcasts(0).enqueue(new PegaResponseManager(new PegaCallback(requireActivity(), false) {
            @Override
            public void onSuccess(@Nullable JSONObject... data) {
                binding.progressBar.setVisibility(View.GONE);
                if (data != null) {
                    for (JSONObject object : data) {
                        broadcastModels.add(new Gson().fromJson(object.toString(), BroadcastModel.class));
                    }
                    isLoading = false;
                    adapter.notifyItemRangeInserted(1, broadcastModels.size());
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
                        JSONObject dataObject = jsonObject.getJSONObject("user"); // Access the "data" object

                        String fName = dataObject.optString("fName");
                        String dp = dataObject.optString("fName");

                        String Image = dataObject.optString("Image");
                        String FrameName = dataObject.optString("FrameName");
                        String FrameAdd = dataObject.optString("FrameAdd");
                        String ProfileImage = dataObject.optString("dp");

                        UserUtils.setPROFILEIMAGE(RetrofitClient.PROFILE_IMAGE + ProfileImage);
                        UserUtils.setFRAMEADD(FrameAdd);
                        UserUtils.setFRAMENAME(FrameName);
                        UserUtils.setUserDp(RetrofitClient.USER_BASE_URL + "getUsermedia/" + Image);


                    } catch (IOException e) {
                        Toast.makeText(requireContext(), "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        Toast.makeText(requireContext(), "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(requireContext(), "" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private void setUserOnlineStatus(boolean isOnline) {
//        DatabaseReference userOnlineRef = FirebaseDatabase.getInstance().getReference("appUsage")
//                .child(UserUtils.getUserId())
//                .child("online");
//
//
//
//        userOnlineRef.setValue(isOnline);
//    }
//    @Override
//    public void onPause() {
//        super.onPause();
//        setUserOnlineStatus(false);
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        setUserOnlineStatus(false);
//    }


}