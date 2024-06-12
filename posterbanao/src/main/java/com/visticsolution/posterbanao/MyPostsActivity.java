package com.visticsolution.posterbanao;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.visticsolution.posterbanao.adapter.UserPostsAdapter;
import com.visticsolution.posterbanao.classes.Functions;
import com.visticsolution.posterbanao.classes.Variables;
import com.visticsolution.posterbanao.model.UserPostModel;
import com.visticsolution.posterbanao.responses.UserResponse;
import com.visticsolution.posterbanao.viewmodel.UserViewModel;

import java.util.List;

public class MyPostsActivity extends AppCompatActivity {

    RecyclerView recycler;
    UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Functions.setLocale(Functions.getSharedPreference(this).getString(Variables.APP_LANGUAGE_CODE,Variables.DEFAULT_LANGUAGE_CODE), this, MyPostsActivity.class,false);
        setContentView(R.layout.activity_my_posts);

        recycler = findViewById(R.id.recycler);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getUserPosts(Functions.getUID(this)).observe(this, new Observer<UserResponse>() {
            @Override
            public void onChanged(UserResponse userResponse) {
                findViewById(R.id.shimmer_lay).setVisibility(View.GONE);
                if (userResponse != null){
                    if (userResponse.getUserposts().size() > 0){
                        setPostsAdapter(userResponse.getUserposts());
                    }else {
                        findViewById(R.id.no_data_layout).setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    private void setPostsAdapter(List<UserPostModel> userposts) {
        recycler.setAdapter(new UserPostsAdapter(this,userposts));
    }

    public void finish(View view) {
        finish();
    }
}