package com.nirmiteepublic.clink.ui.activity.pages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.adapters.OtpAdapter;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;
import com.nirmiteepublic.clink.models.OtpResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewOtp extends AppCompatActivity {
    private RecyclerView otpRecyclerView;
    private OtpAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_otp);

        otpRecyclerView = findViewById(R.id.otpRecyclerView);
        otpRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OtpAdapter(new ArrayList<>());

        fetchOtps();
    }

    private void fetchOtps() {
        RetrofitClient.getInstance(this).getApiInterfaces().getOtps().enqueue(new Callback<OtpResponse>() {
            @Override
            public void onResponse(Call<OtpResponse> call, Response<OtpResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("API_RESPONSE", response.body().toString());
                    OtpAdapter adapter=new OtpAdapter(response.body().getOtpData());
                    otpRecyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(ViewOtp.this, "Failed to fetch OTPs", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OtpResponse> call, Throwable t) {
                Toast.makeText(ViewOtp.this, "Error: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}