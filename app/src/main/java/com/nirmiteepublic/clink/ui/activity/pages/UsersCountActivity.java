package com.nirmiteepublic.clink.ui.activity.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.adapters.UserCountAdapter;
import com.nirmiteepublic.clink.databinding.ActivityUsersCountBinding;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;
import com.nirmiteepublic.clink.functions.viewmanagers.PegaAppCompatActivity;
import com.nirmiteepublic.clink.models.UserCount;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsersCountActivity extends PegaAppCompatActivity {
ActivityUsersCountBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUsersCountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        setWindowThemeSecond();

        RetrofitClient.getInstance(this).getApiInterfaces().getUserLocation().enqueue(new Callback<UserCount>() {
            @Override
            public void onResponse(Call<UserCount> call, Response<UserCount> response) {
                if (response.isSuccessful()){
                    UserCountAdapter adapter=new UserCountAdapter(response.body().getResult());
                    binding.recview.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<UserCount> call, Throwable t) {
                Toast.makeText(UsersCountActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}