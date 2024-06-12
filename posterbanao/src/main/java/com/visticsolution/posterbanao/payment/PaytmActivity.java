package com.visticsolution.posterbanao.payment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import com.paytm.pgsdk.TransactionManager;
import com.visticsolution.posterbanao.R;
import com.visticsolution.posterbanao.classes.Constants;
import com.visticsolution.posterbanao.classes.Functions;
import com.visticsolution.posterbanao.network.ApiClient;
import com.visticsolution.posterbanao.network.ApiService;
import com.visticsolution.posterbanao.responses.PaytmResponse;
import com.visticsolution.posterbanao.responses.SimpleResponse;

import retrofit2.Call;


public class PaytmActivity extends AppCompatActivity {

    Activity context;
    String orderID,custID,amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paytm);

        context = this;

        amount = getIntent().getStringExtra("price");
        orderID = Functions.randomAlphaNumeric(20);
        custID = Functions.randomAlphaNumeric(10);


        Functions.showLoader(context);
        ApiClient.getRetrofit().create(ApiService.class).createPaytmPayment(
                Constants.API_KEY,
                orderID,
                custID,
                amount
        ).enqueue(new retrofit2.Callback<PaytmResponse>() {
            @Override
            public void onResponse(Call<PaytmResponse> call, retrofit2.Response<PaytmResponse> response) {
                Functions.cancelLoader();
                if (response.body().code == 200){
                    startPayment(response.body().getSignature(),response.body().callback_url);
                }else {
                    Functions.showToast(context,response.body().message);
                }
            }
            @Override
            public void onFailure(Call<PaytmResponse> call, Throwable t) {
                Toast.makeText(context, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                Functions.cancelLoader();
            }
        });

    }

    private void startPayment(String signature, String callback_url)  {

        PaytmOrder paytmOrder = new PaytmOrder(orderID, Functions.getSharedPreference(context).getString("paytm_merchant_id",""),
                signature, String.valueOf(amount), callback_url);
        TransactionManager transactionManager = new TransactionManager(paytmOrder, new PaytmPaymentTransactionCallback() {
            @Override
            public void onTransactionResponse(@Nullable Bundle bundle) {
                if (bundle.getString("STATUS").equals("TXN_FAILURE")) {
                    showError(bundle.getString("RESPMSG"));
                } else if (bundle.getString("STATUS").equals("TXN_SUCCESS")) {
                    verifyTransaction(orderID);
                }
            }

            @Override
            public void networkNotAvailable() {
                showError("Internet is not available");
            }

            @Override
            public void onErrorProceed(String s) {
                showError(s);
            }

            @Override
            public void clientAuthenticationFailed(String s) {
                showError("Transaction Failed");
            }

            @Override
            public void someUIErrorOccurred(String s) {
                showError("Transaction Error");
            }

            @Override
            public void onErrorLoadingWebPage(int i, String s, String s1) {
                showError("Transaction Error");
            }

            @Override
            public void onBackPressedCancelTransaction() {
                showError("Transaction cancel");
            }

            @Override
            public void onTransactionCancel(String s, Bundle bundle) {
                showError("Transaction cancel");
            }
        });
        transactionManager.setAppInvokeEnabled(false);
//        transactionManager.setShowPaymentUrl("https://securegw-stage.paytm.in/theia/api/v1/showPaymentPage");
        transactionManager.startTransaction(context, 50000);
    }

    private void showError(String respmsg) {
        Toast.makeText(context, ""+respmsg, Toast.LENGTH_SHORT).show();
    }


    private void verifyTransaction(String orderId) {
        Functions.showLoader(context);
        ApiClient.getRetrofit().create(ApiService.class).verifyPaytmPayment(
                Constants.API_KEY,
                orderID
        ).enqueue(new retrofit2.Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, retrofit2.Response<SimpleResponse> response) {
                Functions.cancelLoader();
                if (response.body().code == 200){
                    if (response.body().response.equalsIgnoreCase("TXN_SUCCESS")) {
                        Intent intent = new Intent().putExtra("transaction",orderId);
                        setResult(RESULT_OK,intent);
                    }else{
                        setResult(RESULT_CANCELED);
                    }
                    finish();
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
}