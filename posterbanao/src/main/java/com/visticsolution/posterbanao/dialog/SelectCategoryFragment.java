package com.visticsolution.posterbanao.dialog;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.visticsolution.posterbanao.R;
import com.visticsolution.posterbanao.adapter.SelectCategoryAdapter;
import com.visticsolution.posterbanao.classes.Functions;
import com.visticsolution.posterbanao.databinding.FragmentSelectCategoryBinding;
import com.visticsolution.posterbanao.model.CategoryModel;
import com.visticsolution.posterbanao.responses.HomeResponse;
import com.visticsolution.posterbanao.viewmodel.HomeViewModel;

public class SelectCategoryFragment extends Fragment {

    FragmentSelectCategoryBinding binding;
    HomeViewModel homeViewModel;
    String type;
    SelectCategoryAdapter.OnCategorySelect listner;
    CategoryModel model;

    public SelectCategoryFragment(String type,SelectCategoryAdapter.OnCategorySelect listner) {
        this.listner = listner;
        this.type = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSelectCategoryBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        getData("");
        binding.searchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    getData(binding.searchEt.getText().toString());
                    return true;
                }
                return false;
            }
        });

        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (model != null){
                    listner.onSelect(model);
                    getActivity().onBackPressed();
                }else {
                    Toast.makeText(getContext(), getString(R.string.please_select_category), Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
//
//        binding.cencelBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            }
//        });
    }

    private void getData(String s) {
        Functions.showLoader(getContext());
        homeViewModel.getBusinessPoliticalCategories(s,type).observe(getViewLifecycleOwner(), new Observer<HomeResponse>() {
            @Override
            public void onChanged(HomeResponse homeResponse) {
                Functions.cancelLoader();
                if (homeResponse.business_political_category != null){
                    binding.recycler.setAdapter(new SelectCategoryAdapter(getContext(), homeResponse.business_political_category, new SelectCategoryAdapter.OnCategorySelect() {
                        @Override
                        public void onSelect(CategoryModel mode) {
                            model = mode;
                            listner.onSelect(model);
                            getActivity().onBackPressed();
                        }
                    }));
                }
            }
        });
    }

}