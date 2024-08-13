package com.nirmiteepublic.clink.ui.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.adapters.UsersAdapter;
import com.nirmiteepublic.clink.databinding.FragmentNetworkBinding;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;
import com.nirmiteepublic.clink.functions.retrofit.res.PegaCallback;
import com.nirmiteepublic.clink.functions.retrofit.res.PegaResponseManager;
import com.nirmiteepublic.clink.functions.utils.UserUtils;
import com.nirmiteepublic.clink.functions.viewmanagers.PegaFragment;
import com.nirmiteepublic.clink.models.UserModel;
import com.nirmiteepublic.clink.ui.activity.pages.FilterUserActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NetworkFragment extends PegaFragment {

    private FragmentNetworkBinding binding;
    private List<UserModel> userModels = new ArrayList<>();
    private UsersAdapter adapter;
    private boolean isLoading = false;
    private static final int PAGE_SIZE = 100000; // Adjust this value as needed

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNetworkBinding.inflate(inflater, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            requireActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.cyan_process));
        }

        binding.progressBar.setVisibility(View.VISIBLE);


        setupRecyclerView();
        setupSearchView();
        setupFilterButton();
        loadInitialNetwork();

        return binding.getRoot();
    }

    private void setupRecyclerView() {
        adapter = new UsersAdapter(userModels);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        initScrollListener();
    }

    private void setupSearchView() {
        binding.searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                filteredList(s.toString());
            }
        });
    }

    private void setupFilterButton() {
        if (UserUtils.isSuperAdmin()) {
            binding.filter.setOnClickListener(v -> {
                startActivity(new Intent(requireContext(), FilterUserActivity.class)
                        .putExtra("taskType", "normal"));
            });
        } else {
            binding.filter.setVisibility(View.GONE);
        }
    }

    private void initScrollListener() {
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null && !isLoading) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= PAGE_SIZE) {
                        loadMoreNetwork();
                    }
                }
            }
        });
    }

    private void loadInitialNetwork() {
        isLoading = true;
        binding.progressBar.setVisibility(View.VISIBLE);

        RetrofitClient.getInstance(requireContext()).getApiInterfaces().getNetworks(0).enqueue(
                new PegaResponseManager(new PegaCallback(requireActivity(), false) {
                    @Override
                    public void onSuccess(@Nullable JSONObject... data) {
                        if (data != null) {
                            for (JSONObject object : data) {
                                userModels.add(new Gson().fromJson(object.toString(), UserModel.class));
                            }
                            adapter.notifyItemRangeInserted(0, userModels.size());
                            setupRealtimeStatusListener();
                        }
                        isLoading = false;
                        binding.progressBar.setVisibility(View.GONE);
                    }
                })
        );
    }

    private void loadMoreNetwork() {

        binding.progressBar.setVisibility(View.VISIBLE);

        isLoading = true;
        int currentSize = userModels.size();
        RetrofitClient.getInstance(requireContext()).getApiInterfaces().getNetworks(currentSize).enqueue(
                new PegaResponseManager(new PegaCallback(requireActivity(), false) {
                    @Override
                    public void onSuccess(@Nullable JSONObject... data) {
                        if (data != null) {
                            for (JSONObject object : data) {
                                userModels.add(new Gson().fromJson(object.toString(), UserModel.class));
                            }
                            adapter.notifyItemRangeInserted(currentSize, userModels.size() - currentSize);
                        }
                        isLoading = false;
                        binding.progressBar.setVisibility(View.GONE);

                    }
                })
        );
    }

    private void setupRealtimeStatusListener() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userId = userSnapshot.getKey();
                    Boolean isOnline = userSnapshot.child("totalTimeOnline").child("online").getValue(Boolean.class);
                    updateUserStatus(userId, isOnline != null && isOnline);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error fetching user data: " + databaseError.getMessage());
            }
        });
    }

    private void updateUserStatus(String userId, boolean isOnline) {
        for (int i = 0; i < userModels.size(); i++) {
            UserModel userModel = userModels.get(i);
            if (userModel.get_id() != null && userModel.get_id().equals(userId)) {
                userModel.setOnline(isOnline);
                adapter.notifyItemChanged(i);
                break;
            }
        }
    }

    private void filteredList(String text) {
        String searchText = text.toLowerCase().trim();
        List<UserModel> filteredList = userModels.stream()
                .filter(userModel -> userModel.getUsername().toLowerCase().contains(searchText))
                .collect(Collectors.toList());

        if (filteredList.isEmpty()) {
            // You might want to show a "No results" view here
        } else {
            adapter.setFilteredList(filteredList);
        }

        Log.d("FilterDebug", "Search text: '" + searchText + "', Matches found: " + filteredList.size());
    }
}