package com.visticsolution.posterbanao;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.visticsolution.posterbanao.adapter.BussinessCategoryAdapter;
import com.visticsolution.posterbanao.classes.Functions;
import com.visticsolution.posterbanao.ads.BannerAdapter;
import com.visticsolution.posterbanao.model.CategoryModel;
import com.visticsolution.posterbanao.responses.HomeResponse;
import com.visticsolution.posterbanao.viewmodel.HomeViewModel;

import java.util.ArrayList;
import java.util.List;

public class CategoriesActivity extends AppCompatActivity {

    List<CategoryModel> list = new ArrayList<>();
    BussinessCategoryAdapter adapter;
    RecyclerView recyclerView;
    GridLayoutManager layoutManager;
    int pageCount = 0;
    Context context;
    TextView title_tv;
    boolean loading = true;
    String type = "festival";
    EditText search_edit;

    HomeViewModel homeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        context = this;
        type = getIntent().getStringExtra("type");


        recyclerView = findViewById(R.id.recycler);
        layoutManager = new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new BussinessCategoryAdapter(this, list);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int pastVisiblesItems, visibleItemCount, totalItemCount;
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();

                    int firstVisibleItems = 0;
                    firstVisibleItems = layoutManager.findFirstVisibleItemPosition();
                    if(firstVisibleItems > 0) {
                        pastVisiblesItems = firstVisibleItems;
                    }

                    if (!loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = true;
                            pageCount++;
                            getCategories("");
                        }
                    }
                }
            }
        });
        search_edit = findViewById(R.id.search_edit);
        search_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    pageCount = 0;
                    list.clear();
                    getCategories(search_edit.getText().toString());
                    return true;
                }
                return false;
            }
        });

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        getCategories("");
        BannerAdapter.showBannerAds(this, findViewById(R.id.ad_container));
    }

    private void getCategories(String s) {
        if (!s.equals("")){
            Functions.showLoader(context);
        }
        homeViewModel.getCategoriesByPage(type,search_edit.getText().toString(),pageCount).observe(this, new Observer<HomeResponse>() {
            @Override
            public void onChanged(HomeResponse homeResponse) {
                loading = false;
                Functions.cancelLoader();
                findViewById(R.id.shimmer_lay).setVisibility(View.GONE);
                if (homeResponse != null){
                    if (homeResponse.categories.size() > 0){
                        list.addAll(homeResponse.categories);
                        adapter.notifyDataSetChanged();
                        findViewById(R.id.no_data_layout).setVisibility(View.GONE);
                    }else {
                        if (pageCount == 0){
                            findViewById(R.id.no_data_layout).setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });
    }


    public void finish(View view) {
        finish();
    }
}