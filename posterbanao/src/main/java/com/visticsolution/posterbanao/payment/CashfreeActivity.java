package com.visticsolution.posterbanao.payment;



import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.visticsolution.posterbanao.R;
import com.visticsolution.posterbanao.classes.Constants;
import com.visticsolution.posterbanao.classes.Functions;
import com.visticsolution.posterbanao.network.ApiClient;
import com.visticsolution.posterbanao.network.ApiService;
import com.visticsolution.posterbanao.responses.SimpleResponse;

import retrofit2.Call;

public class CashfreeActivity extends AppCompatActivity {

    Activity context;
    String orderID,amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashfree);

        context = this;

        amount = getIntent().getStringExtra("price");
        orderID = Functions.randomAlphaNumeric(20);

        Functions.showLoader(context);
        ApiClient.getRetrofit().create(ApiService.class).createCashfreePayment(
                Constants.API_KEY,
                orderID,
                amount
        ).enqueue(new retrofit2.Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, retrofit2.Response<SimpleResponse> response) {
                Functions.cancelLoader();
                if (response.body().code == 200){
                    startPayment(orderID,response.body().response);
                }else {
                    Functions.showToast(context,response.body().message);
                }
            }
            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                Toast.makeText(context, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                Functions.cancelLoader();
            }
        });
    }

    public void startPayment(String orderId, String token) {

    }
}