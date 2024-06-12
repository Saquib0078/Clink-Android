package com.visticsolution.posterbanao.fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.visticsolution.posterbanao.R;
import com.visticsolution.posterbanao.adapter.WithdrawAdapter;
import com.visticsolution.posterbanao.classes.Functions;
import com.visticsolution.posterbanao.databinding.FragmentWithdrawBinding;
import com.visticsolution.posterbanao.interfaces.AdapterClickListener;
import com.visticsolution.posterbanao.responses.UserResponse;
import com.visticsolution.posterbanao.viewmodel.UserViewModel;


public class WithdrawFragment extends Fragment {

    public WithdrawFragment() {}

    FragmentWithdrawBinding binding;
    Activity context;
    UserViewModel viewModel;
    int balance = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentWithdrawBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();

        viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        getData();

        binding.withdrawBtn.setOnClickListener(v -> {
            if (binding.upiIdEt.getText().toString().equals("") || !binding.upiIdEt.getText().toString().contains("@")){
                Functions.showToast(context,getString(R.string.enter_currect_upi_id));
            }else if (balance > Integer.parseInt(Functions.getSharedPreference(context).getString("min_withdraw","20"))){
                Functions.showLoader(context);
                viewModel.withdrawRequest(Functions.getUID(context),balance,binding.upiIdEt.getText().toString()).observe(getViewLifecycleOwner(), new Observer<UserResponse>() {
                    @Override
                    public void onChanged(UserResponse userResponse) {
                        Functions.cancelLoader();
                        getData();
                        Functions.showToast(context,userResponse.getMessage());
                    }
                });
            }else{
                Toast.makeText(context, getString(R.string.min_withdraw_is)+Functions.getSharedPreference(context).getString("min_withdraw","20"), Toast.LENGTH_SHORT).show();
            }
        });

        binding.backBtn.setOnClickListener(view1 -> {
            getActivity().onBackPressed();
        });
    }

    private void getData() {
        viewModel.getWithdrawRequest(Functions.getUID(context)).observe(getViewLifecycleOwner(), new Observer<UserResponse>() {
            @Override
            public void onChanged(UserResponse userResponse) {
                if (userResponse != null){
                    balance = userResponse.getBalance();
                    binding.balanceTv.setText(context.getString(R.string.currency)+""+userResponse.getBalance());
                    binding.totalWithdraw.setText(context.getString(R.string.currency)+""+userResponse.getTotal_withdraw());
                    binding.totalEarning.setText(context.getString(R.string.currency)+""+(userResponse.getBalance()+userResponse.getTotal_withdraw()));
                    if (userResponse.getWithdrawlist().size() > 0){
                        binding.userList.setAdapter(new WithdrawAdapter(context, userResponse.getWithdrawlist(), new AdapterClickListener() {
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