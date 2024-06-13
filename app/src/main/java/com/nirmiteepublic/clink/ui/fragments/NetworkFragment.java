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
import android.widget.SearchView;
import android.widget.Toast;

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

public class NetworkFragment extends PegaFragment {

    FragmentNetworkBinding binding;
    List<UserModel> userModels = new ArrayList<>();
    UsersAdapter adapter;
    boolean isLoading;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNetworkBinding.inflate(inflater, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            requireActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.cyan_process)); // Use your color resource
        }
        adapter = new UsersAdapter(userModels);
        binding.recyclerView.setAdapter(adapter);

        binding.filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireContext(), FilterUserActivity.class));
            }
        });

//        binding.searchView.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                // Not needed
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                // Filter the list based on the search query
//                adapter.filterUsers(s.toString());
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                // Not needed
//            }
//        });

        // In your activity or fragment

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Do nothing
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filteredList(newText);
                return true;
            }
        });



        loadNetwork();
//        userStatus();
//        binding.searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                adapter.filter(query.toString());
//
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                adapter.filter(newText.toString());
//
//                return true;
//            }
//        });

        initScrollListener();

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
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == userModels.size() - 1) {
                        isLoading = true;
                        loadMoreNetwork();
                    }
                }
            }
        });
    }

    private void loadMoreNetwork() {
        int size = userModels.size();
        userModels.add(new UserModel());
        adapter.notifyItemInserted(size);
        binding.recyclerView.scrollToPosition(size);

        RetrofitClient.getInstance(requireContext()).getApiInterfaces().getNetworks(size).enqueue(new PegaResponseManager(new PegaCallback(requireActivity(), false) {
            @Override
            public void onSuccess(@Nullable JSONObject... data) {
                userModels.remove(size);
                adapter.notifyItemRemoved(size);
                if (data != null) {
                    for (JSONObject object : data) {
                        userModels.add(new Gson().fromJson(object.toString(), UserModel.class));
                    }
                    isLoading = false;
                }
            }
        }));


    }

    private void loadNetwork() {
        RetrofitClient.getInstance(requireContext()).getApiInterfaces().getNetworks(0).enqueue(new PegaResponseManager(new PegaCallback(requireActivity(), false) {
            @Override
            public void onSuccess(@Nullable JSONObject... data) {
                binding.progressBar.setVisibility(View.GONE);
                if (data != null) {
                    for (JSONObject object : data) {

                        userModels.add(new Gson().fromJson(object.toString(), UserModel.class));
                    }
                    setupRealtimeStatusListener();
                    isLoading = false;
                }
                adapter.notifyItemRangeInserted(0, userModels.size());
            }
        }));

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
        for (UserModel userModel : userModels) {
            if (userModel.get_id() != null && userModel.get_id().equals(userId)) {
                userModel.setOnline(isOnline);
                int index = userModels.indexOf(userModel);
                adapter.notifyItemChanged(index);
                break;
            }
        }
    }

    private void filteredList(String text) {
        List<UserModel> userModelList = new ArrayList<>();
        for (UserModel userModel : userModels) {
            String username = userModel.getUsername();
            if (username != null && username.toLowerCase().contains(text.toLowerCase())) {
                userModelList.add(userModel);
            }
        }

        if (userModelList.isEmpty()) {
            Toast.makeText(requireContext(), "No Data Found", Toast.LENGTH_SHORT).show();
        } else {
            adapter.setFilteredList(userModelList);
        }
    }


}