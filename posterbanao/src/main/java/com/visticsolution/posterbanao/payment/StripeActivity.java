package com.visticsolution.posterbanao.payment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.visticsolution.posterbanao.R;
import com.visticsolution.posterbanao.classes.Constants;
import com.visticsolution.posterbanao.classes.Functions;
import com.visticsolution.posterbanao.network.ApiClient;
import com.visticsolution.posterbanao.network.ApiService;
import com.visticsolution.posterbanao.responses.StripeResponse;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.Stripe;

import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import retrofit2.Call;

public class StripeActivity extends AppCompatActivity {

    private Stripe stripe;
    private String amount;
    private String orderID;
    private String paymentIntentClientSecret, stripePublishableKey;
    PaymentSheet.CustomerConfiguration customerConfig;
    PaymentSheet paymentSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stripe);

        amount = getIntent().getStringExtra("price");
        orderID = "strp_" + System.currentTimeMillis();

        paymentSheet = new PaymentSheet(this, this::onPaymentSheetResult);
        Functions.showLoader(this);
        getClientScre();
    }


    private void getClientScre() {
        ApiClient.getRetrofit().create(ApiService.class).createStripePayment(
                Constants.API_KEY,
                Functions.getUID(StripeActivity.this),
                orderID,
                amount
        ).enqueue(new retrofit2.Callback<StripeResponse>() {
            @Override
            public void onResponse(Call<StripeResponse> call, retrofit2.Response<StripeResponse> response) {
                Functions.cancelLoader();
                if (response.body() !=null){
                    if (response.body().code == 200) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                stripePublishableKey = response.body().publishableKey;
                                paymentIntentClientSecret = response.body().clientSecret;


                                customerConfig = new PaymentSheet.CustomerConfiguration(
                                        response.body().customer,
                                        response.body().ephemeralKey
                                );

                                PaymentConfiguration.init(StripeActivity.this, stripePublishableKey);

                                final PaymentSheet.Configuration configuration = new PaymentSheet.Configuration.Builder(getString(R.string.app_name))
                                        .customer(customerConfig)
                                        .allowsDelayedPaymentMethods(true)
                                        .build();
                                paymentSheet.presentWithPaymentIntent(
                                        paymentIntentClientSecret,
                                        configuration
                                );
                            }
                        });

                    } else {
                        Functions.showToast(StripeActivity.this, response.body().message);
                    }
                }else {
                    Functions.showToast(StripeActivity.this,"Server Error");
                }
            }

            @Override
            public void onFailure(Call<StripeResponse> call, Throwable t) {
                Toast.makeText(StripeActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                Functions.cancelLoader();
            }
        });
    }

    void onPaymentSheetResult(final PaymentSheetResult paymentSheetResult) {
        Functions.cancelLoader();
        if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            setResult(RESULT_CANCELED);
            finish();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            setResult(RESULT_CANCELED);
            finish();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            AddTransaction("SUCCESS", orderID);
        }
    }

    public void AddTransaction(String status, String id) {
        if (status.equals("SUCCESS")) {
            Intent intent = new Intent().putExtra("transaction", id);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(this, "Transaction failed", Toast.LENGTH_SHORT).show();
        }
    }
}