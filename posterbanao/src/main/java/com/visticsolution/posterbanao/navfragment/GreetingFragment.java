package com.visticsolution.posterbanao.navfragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.visticsolution.posterbanao.OpenPostActivity;
import com.visticsolution.posterbanao.adapter.GreeingSectionAdapter;
import com.visticsolution.posterbanao.classes.Functions;
import com.visticsolution.posterbanao.classes.Variables;
import com.visticsolution.posterbanao.databinding.FragmentGreetingBinding;
import com.visticsolution.posterbanao.model.PostsModel;
import com.visticsolution.posterbanao.model.SectionModel;
import com.visticsolution.posterbanao.responses.HomeResponse;
import com.visticsolution.posterbanao.viewmodel.HomeViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GreetingFragment extends Fragment {

    public GreetingFragment() {}
    Context context;
    View view;
    HomeViewModel homeViewModel;
    FragmentGreetingBinding binding;
    GreeingSectionAdapter adapter;
    List<SectionModel> list = new ArrayList<>();
    int pageCount = 0;
    boolean isLoading = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGreetingBinding.inflate(getLayoutInflater());
        Functions.fadeIn(binding.getRoot(), getContext());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getContext();
        this.view  = view;

        list.clear();
        adapter = new GreeingSectionAdapter(context, list, new GreeingSectionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, List<PostsModel> posts, int main_position, int child_position) {
                startActivity(new Intent(context, OpenPostActivity.class)
                        .putExtra("title", list.get(main_position).getName())
                        .putExtra("type", "greeting")
                        .putExtra("item_id", posts.get(child_position).section_id)
                        .putExtra("model", (Serializable) posts.get(child_position)));

            }
        });
        binding.recyclerview.setAdapter(adapter);

        binding.refereshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.shimmerLay.startShimmer();
                binding.shimmerLay.setVisibility(View.VISIBLE);
                list.clear();
                getData("");
                pageCount = 0;
            }
        });
        binding.nestedScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // on scroll change we are checking when users scroll as bottom.
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    if (!isLoading){
                        isLoading = true;
                        pageCount++;
                        getData("");
                    }
                }
            }
        });

        binding.searchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    Functions.showLoader(context);
                    pageCount = 0;
                    list.clear();
                    getData(textView.getText().toString());
                    return true;
                }
                return false;
            }
        });

        binding.searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Functions.showLoader(context);
                pageCount = 0;
                list.clear();
                getData(binding.searchEt.getText().toString());
            }
        });
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        getData("");
    }

    private void getData(String search) {
        homeViewModel.getGreetingData(Functions.getSharedPreference(context).getString(Variables.SELCT_LANGUAGE,""),search,pageCount).observe(getViewLifecycleOwner(), new Observer<HomeResponse>() {
            @Override
            public void onChanged(HomeResponse homeResponse) {
                Functions.cancelLoader();
                isLoading = false;
                binding.refereshLayout.setRefreshing(false);
                binding.shimmerLay.stopShimmer();
                binding.shimmerLay.setVisibility(View.GONE);
                if (homeResponse != null){
                    if (homeResponse.greeting_section.size() > 0){
                        list.addAll(homeResponse.greeting_section);
                        adapter.notifyDataSetChanged();
                    }else {
                        binding.noDataLayout.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

}