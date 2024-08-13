package com.visticsolution.posterbanao.fragments;

import static com.visticsolution.posterbanao.classes.Constants.SUCCESS;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.visticsolution.posterbanao.AddBussinessActivity;
import com.visticsolution.posterbanao.AddPoliticalActivity;
import com.visticsolution.posterbanao.R;
import com.visticsolution.posterbanao.classes.Constants;
import com.visticsolution.posterbanao.adapter.BussinessAdapter;
import com.visticsolution.posterbanao.classes.Functions;
import com.visticsolution.posterbanao.classes.Variables;
import com.visticsolution.posterbanao.databinding.FragmentMyBussinessBinding;
import com.visticsolution.posterbanao.dialog.CustomeDialogFragment;
import com.visticsolution.posterbanao.dialog.DialogType;
import com.visticsolution.posterbanao.dialog.UpgradeDialogFragment;
import com.visticsolution.posterbanao.interfaces.AdapterClickListener;
import com.visticsolution.posterbanao.interfaces.DismisListner;
import com.visticsolution.posterbanao.model.BussinessModel;
import com.visticsolution.posterbanao.network.ApiClient;
import com.visticsolution.posterbanao.network.ApiService;
import com.visticsolution.posterbanao.responses.SimpleResponse;
import com.visticsolution.posterbanao.responses.UserResponse;
import com.visticsolution.posterbanao.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class MyBussinessFragment extends BottomSheetDialogFragment {


    List<BussinessModel> list = new ArrayList<>();
    FragmentMyBussinessBinding binding;
    UserViewModel userViewModel;
    Activity context;
    DismisListner dismisListner;

    public MyBussinessFragment() {
    }

    public MyBussinessFragment(DismisListner dismisListner) {
        this.dismisListner = dismisListner;
    }

    long political_count = 1000000000;
    long business_count = 1000000000;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMyBussinessBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        binding.closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        binding.addBusinessProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.dialog_user_type);
                dialog.setCancelable(true);
                dialog.getWindow().setAttributes(getLayoutParams(dialog));
                TextView businessBtn = dialog.findViewById(R.id.businessBtn);
                TextView politicalBtn = dialog.findViewById(R.id.politicalBtn);
                TextView skipBtn = dialog.findViewById(R.id.skip_btn);
                skipBtn.setVisibility(View.GONE);
                businessBtn.setOnClickListener(view -> {
                    dialog.dismiss();
                    addNew("business");
                });
                politicalBtn.setOnClickListener(view -> {
                    dialog.dismiss();
                    addNew("political");
                });
                dialog.show();
            }
        });


//        binding.addBusiness.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                addNew("business");
//            }
//        });
//
//        binding.addPoliticalProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                addNew("political");
//            }
//        });

        binding.bussinessBtn.setActivated(true);
        binding.bussinessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.bussinessBtn.setActivated(true);
                binding.politicalBtn.setActivated(false);
                getMyBussiness("business");
            }
        });

        binding.politicalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.bussinessBtn.setActivated(false);
                binding.politicalBtn.setActivated(true);
                getMyBussiness("political");
            }
        });

        setAdapter();

        if (Functions.getSharedPreference(context).getString(Variables.ACTIVE_PROFILE_TYPE,"").equals("Political")){
            getMyBussiness("political");
            binding.bussinessBtn.setActivated(false);
            binding.politicalBtn.setActivated(true);
        }else{
            getMyBussiness("business");
            binding.bussinessBtn.setActivated(true);
            binding.politicalBtn.setActivated(false);
        }

        userViewModel.getUserBusiness(Functions.getUID(context), "political").observe(this, new Observer<UserResponse>() {
            @Override
            public void onChanged(UserResponse userResponse) {
                if (userResponse != null) {
                    if (userResponse.code == SUCCESS) {
                        if (userResponse.getBusinesses().size() > 0) {
                            political_count = userResponse.getBusinesses().size();
                        }
                    }
                }
            }
        });
    }

    boolean isLoading = false;
    @Override
    public void onResume() {
        super.onResume();
        if (!isLoading){
            if (Functions.getSharedPreference(context).getString(Variables.ACTIVE_PROFILE_TYPE,"").equals("Business")){
                getMyBussiness("business");
                binding.bussinessBtn.setActivated(true);
                binding.politicalBtn.setActivated(false);
            }else{
                getMyBussiness("political");
                binding.bussinessBtn.setActivated(false);
                binding.politicalBtn.setActivated(true);
            }
        }
    }

    private void getMyBussiness(String type) {
        isLoading = true;
        binding.politicalBtn.setEnabled(false);
        binding.bussinessBtn.setEnabled(false);
        binding.shimmerLay.startShimmer();
        binding.shimmerLay.setVisibility(View.VISIBLE);
        binding.noDataLayout.setVisibility(View.GONE);
        list.clear();
        adapter.notifyDataSetChanged();
        userViewModel.getUserBusiness(Functions.getUID(context), type).observe(this, new Observer<UserResponse>() {
            @Override
            public void onChanged(UserResponse userResponse) {
                isLoading = false;
                binding.shimmerLay.stopShimmer();
                binding.shimmerLay.setVisibility(View.GONE);
                binding.politicalBtn.setEnabled(true);
                binding.bussinessBtn.setEnabled(true);
                if (userResponse != null) {
                    if (userResponse.code == SUCCESS) {
                        if (userResponse.getBusinesses().size() > 0) {
                            if (type.equals("business")) {
                                business_count = userResponse.getBusinesses().size();
                            }
                            list.addAll(userResponse.getBusinesses());
                            adapter.notifyDataSetChanged();
                        }else {
                            binding.noDataLayout.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Functions.showToast(context, userResponse.message);
                    }
                }else{
                    binding.noDataLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    BussinessAdapter adapter;

    private void setAdapter() {
        adapter = new BussinessAdapter(context, list, new AdapterClickListener() {
            @Override
            public void onItemClick(View view, int pos, Object object) {
                BussinessModel model = (BussinessModel) object;
                int id = view.getId();
                if (id == R.id.edit_btn) {
                    editBussiness(model.getId(), model.getType());
                } else if (id == R.id.delete_btn) {
                    deleteBussiness(model.getId(), pos);
                } else {
                    if (model.getType().equals("business")) {
                        Functions.saveBussinessData(model, context);
                    } else {
                        Functions.savePoliticalData(model, context);
                    }
                    adapter.notifyDataSetChanged();
                    if (dismisListner != null) {
                        dismisListner.onDismis();
                    }
                }
            }
        });
        binding.recyclerView.setAdapter(adapter);
    }

    private void deleteBussiness(String id, int pos) {
        new CustomeDialogFragment(getString(R.string.alert), getString(R.string.do_you_realy_want_to_delete_this), DialogType.WARNING, true, true, true, new CustomeDialogFragment.DialogCallback() {
            @Override
            public void onCencel() {

            }

            @Override
            public void onSubmit() {
                ApiClient.getRetrofit().create(ApiService.class).deleteUserBusiness(Constants.API_KEY, id).enqueue(new retrofit2.Callback<SimpleResponse>() {
                    @Override
                    public void onResponse(Call<SimpleResponse> call, retrofit2.Response<SimpleResponse> response) {
                        if (response.body().code == 200) {
                            Functions.showToast(context, getString(R.string.delete_sucsess));
                            list.remove(pos);
                            adapter.notifyItemRemoved(pos);
                        }
                    }

                    @Override
                    public void onFailure(Call<SimpleResponse> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onDismiss() {

            }

            @Override
            public void onComplete(Dialog dialog) {

            }
        }).show(getChildFragmentManager(), "");
    }

    private void editBussiness(String id, String type) {
        if (type.equals("political")) {
            startActivity(new Intent(context, AddPoliticalActivity.class).putExtra("business_id", id));
        } else {
            startActivity(new Intent(context, AddBussinessActivity.class).putExtra("business_id", id));
        }
    }

    public void addNew(String type) {
        if (type.equals("political")) {
//            if (Functions.getSharedPreference(context).getInt(Variables.POLITICAL_LIMIT, 0) > political_count) {
                startActivity(new Intent(context, AddPoliticalActivity.class));
//            } else {
//                new UpgradeDialogFragment(getString(R.string.political_limit_reaced)).show(getChildFragmentManager(), "");
//            }
        } else {
//            if (Functions.getSharedPreference(context).getInt(Variables.BUSINESS_LIMIT, 0) > business_count) {
                startActivity(new Intent(context, AddBussinessActivity.class));
//            } else {
//                new UpgradeDialogFragment(getString(R.string.business_limit_reaced)).show(getChildFragmentManager(), "");
//            }
        }
    }
    private WindowManager.LayoutParams getLayoutParams(@NonNull Dialog dialog) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        if (dialog.getWindow() != null) {
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
        }
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        return layoutParams;
    }
}