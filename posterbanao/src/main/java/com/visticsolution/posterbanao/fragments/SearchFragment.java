package com.visticsolution.posterbanao.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.visticsolution.posterbanao.OpenPostActivity;
import com.visticsolution.posterbanao.adapter.PostsAdapter;
import com.visticsolution.posterbanao.classes.Constants;
import com.visticsolution.posterbanao.classes.Functions;
import com.visticsolution.posterbanao.model.PostsModel;
import com.visticsolution.posterbanao.R;
import com.visticsolution.posterbanao.network.ApiClient;
import com.visticsolution.posterbanao.network.ApiService;
import com.visticsolution.posterbanao.responses.HomeResponse;
import com.visticsolution.posterbanao.viewmodel.HomeViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchFragment extends Fragment {

    public SearchFragment() {
    }

    Context context;
    RecyclerView recycler;
    EditText search_edit;
    View view;
    HomeViewModel homeViewModel;

    ListView sugestionRec;
    List<String> suggestionList = new ArrayList<>();
    ArrayAdapter<String> arr;
    boolean isSearch = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getContext();
        this.view = view;
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        sugestionRec = view.findViewById(R.id.suggestionView);
        search_edit = view.findViewById(R.id.search_edit);
        recycler = view.findViewById(R.id.recycler);
        setPostsAdapter();

        view.findViewById(R.id.backBtn).setOnClickListener(view1 -> {
            getActivity().onBackPressed();
        });

        sugestionRec.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                isSearch = true;
                search_edit.setText(suggestionList.get(i));
                callSearchPost(suggestionList.get(i));
            }
        });

        search_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    callSearchPost(search_edit.getText().toString());
                    return true;
                }
                return false;
            }
        });

        search_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().equals("") && !isSearch){
                    ApiClient.getRetrofit().create(ApiService.class).getSearchSuggestion(Constants.API_KEY, charSequence.toString()).enqueue(new Callback<List<String>>() {
                        @Override
                        public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                            try {
                                suggestionList = response.body();
                                arr = new ArrayAdapter<String>(getContext(),
                                        androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                                        suggestionList);
                                sugestionRec.setAdapter(arr);
                                sugestionRec.setVisibility(View.VISIBLE);
                            } catch (Exception e) {
                                Log.d("Exception___",e.getMessage());
                            }
                        }
                        @Override
                        public void onFailure(Call<List<String>> call, Throwable t) {

                        }
                    });
                }else{
                    try {
                        sugestionRec.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    List<PostsModel> posts_list = new ArrayList<>();

    private void callSearchPost(String s) {
        sugestionRec.setVisibility(View.GONE);
        Functions.showLoader(context);
        homeViewModel.getPostByPage("", "", "", "","", s,"", 0).observe(this, new Observer<HomeResponse>() {
            @Override
            public void onChanged(HomeResponse homeResponse) {
                Functions.cancelLoader();
                isSearch = false;
                posts_list.clear();
                if (homeResponse != null) {
                    if (homeResponse.posts.size() > 0) {
                        posts_list.addAll(homeResponse.posts);
                        view.findViewById(R.id.no_data_layout).setVisibility(View.GONE);
                    } else {
                        view.findViewById(R.id.no_data_layout).setVisibility(View.VISIBLE);
                    }
                    searchAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    PostsAdapter searchAdapter;

    private void setPostsAdapter() {
        searchAdapter = new PostsAdapter(context, posts_list, new PostsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, PostsModel postsModels, int main_position) {
                startActivity(new Intent(context, OpenPostActivity.class).
                        putExtra("title", getString(R.string.app_name)).
                        putExtra("type", "category").
                        putExtra("item_id", postsModels.category_id));
            }
        }, 3, getResources().getDimension(R.dimen._2sdp));
        recycler.setAdapter(searchAdapter);
    }
}