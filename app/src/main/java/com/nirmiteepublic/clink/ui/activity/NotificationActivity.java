package com.nirmiteepublic.clink.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.adapters.NotificationAdapter;
import com.nirmiteepublic.clink.databinding.ActivityNotificationBinding;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;
import com.nirmiteepublic.clink.functions.viewmanagers.PegaAppCompatActivity;
import com.nirmiteepublic.clink.models.NotificationModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends PegaAppCompatActivity {
ActivityNotificationBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityNotificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getNotification();

        setWindowThemeSecond();

        binding.progressBar.setVisibility(View.VISIBLE);

    }

    private void getNotification(){
        RetrofitClient.getInstance(this).getApiInterfaces().getNotification().enqueue(new Callback<NotificationModel>() {
            @Override
            public void onResponse(Call<NotificationModel> call, Response<NotificationModel> response) {
                if (response.isSuccessful()){
                    binding.progressBar.setVisibility(View.GONE);
                    NotificationAdapter adapter=new NotificationAdapter(response.body().getNotification(),NotificationActivity.this);
                    binding.recview.setAdapter(adapter);


                }
            }

            @Override
            public void onFailure(Call<NotificationModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(NotificationActivity.this, "Failed to load"+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}