package com.visticsolution.posterbanao;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.visticsolution.posterbanao.adapter.TransactionAdapter;
import com.visticsolution.posterbanao.classes.Functions;
import com.visticsolution.posterbanao.databinding.ActivityTransactionBinding;
import com.visticsolution.posterbanao.interfaces.AdapterClickListener;
import com.visticsolution.posterbanao.responses.UserResponse;
import com.visticsolution.posterbanao.viewmodel.UserViewModel;

public class TransactionActivity extends AppCompatActivity {


    ActivityTransactionBinding binding;
    Activity context;
    UserViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTransactionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context = this;

        binding.backBtn.setOnClickListener(view -> {
            finish();
        });
        viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        getData();
    }

    private void getData() {
        viewModel.getTransactionRequest(Functions.getUID(context)).observe(this, new Observer<UserResponse>() {
            @Override
            public void onChanged(UserResponse userResponse) {
                if (userResponse != null){
                    if (userResponse.getTransactionlist().size() > 0){
                        binding.userList.setAdapter(new TransactionAdapter(context, userResponse.getTransactionlist(), new AdapterClickListener() {
                            @Override
                            public void onItemClick(View view, int pos, Object object) {

                            }
                        }));
                    }else{
                        binding.noDataLayout.setVisibility(View.VISIBLE);
                    }
                }else {
                    binding.noDataLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}