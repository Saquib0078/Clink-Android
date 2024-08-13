package com.visticsolution.posterbanao.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.visticsolution.posterbanao.adapter.PostsAdapter;
import com.visticsolution.posterbanao.model.PostsModel;
import com.visticsolution.posterbanao.R;
import com.visticsolution.posterbanao.responses.HomeResponse;
import com.visticsolution.posterbanao.viewmodel.HomeViewModel;

import java.util.ArrayList;
import java.util.List;


public class PagerPosterByLanguageFragment extends Fragment {

    public interface PostCallBack{
        void onSelect(PostsModel postsModel,String type);
    }

    PostCallBack postCallBack;
    public PagerPosterByLanguageFragment(PostCallBack postCallBack) {
        this.postCallBack = postCallBack;
    }

    List<PostsModel> list = new ArrayList<>();
    PostsAdapter postsAdapter;
    RecyclerView recyclerView;
    StaggeredGridLayoutManager layoutManager;
    int pageCount = 0;
    String language_code,category_id,type,Objecttype;
    Context context;
    View view;
    boolean isVideo = false;
    boolean loading = true;

    HomeViewModel homeViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pager_poster_by_language, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        context = getContext();

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        language_code = getArguments().getString("l_code");
        type = getArguments().getString("type");
        category_id = getArguments().getString("category_id");
        isVideo = getArguments().getBoolean("itemtype");

        recyclerView = view.findViewById(R.id.recycler);
        layoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        postsAdapter = new PostsAdapter(context, list, new PostsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, PostsModel postsModels, int main_position) {
                postCallBack.onSelect(postsModels,type);
            }
        },3,getResources().getDimension(R.dimen._2sdp));
        recyclerView.setAdapter(postsAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int pastVisiblesItems, visibleItemCount, totalItemCount;
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();

                    int[] firstVisibleItems = null;
                    firstVisibleItems = layoutManager.findFirstVisibleItemPositions(firstVisibleItems);
                    if(firstVisibleItems != null && firstVisibleItems.length > 0) {
                        pastVisiblesItems = firstVisibleItems[0];
                    }

                    if (!loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = true;
                            pageCount++;
                            getPosts();
                        }
                    }
                }
            }
        });
        getPosts();
    }

    String cate_sec = "category";
    private void getPosts() {
        if (isVideo){
            Objecttype = "video";
        }else {
            if (type.equals("Posts")){
                Objecttype = "posts";
            }else if (type.equals("GreetingPosts")){
                Objecttype = "greeting";
                cate_sec = "section";
            }
        }

        homeViewModel.getPostByPage(cate_sec,language_code,Objecttype,category_id,"","","",pageCount).observe(getViewLifecycleOwner(), new Observer<HomeResponse>() {
            @Override
            public void onChanged(HomeResponse homeResponse) {
                view.findViewById(R.id.shimmer_lay).setVisibility(View.GONE);
                loading = false;
                if (homeResponse != null){
                    if (homeResponse.posts.size() > 0){
                        list.addAll(homeResponse.posts);
                        postsAdapter.notifyDataSetChanged();
                    }else{
                        if (!(list.size() > 0)){
                            view.findViewById(R.id.no_data_layout).setVisibility(View.VISIBLE);
                        }else{
                            view.findViewById(R.id.no_data_layout).setVisibility(View.GONE);
                        }
                    }
                }
            }
        });
    }
}