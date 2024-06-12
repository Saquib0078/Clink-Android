package com.visticsolution.posterbanao.payment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.visticsolution.posterbanao.R;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.visticsolution.posterbanao.classes.Functions;
import com.visticsolution.posterbanao.classes.Variables;

import org.json.JSONObject;

public class RazorpayActivity extends AppCompatActivity implements PaymentResultListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_razorpay);

        startRazorPayment(Integer.parseInt(getIntent().getStringExtra("price")));
    }

    private void startRazorPayment(int amount) {
        Checkout checkout = new Checkout();
        checkout.setKeyID(Functions.getSharedPreference(this).getString("razorpay_key",""));
        checkout.setImage(R.mipmap.ic_launcher);
        JSONObject options = null;
        try {
            options = new JSONObject();
            options.put("name", getString(R.string.app_name));
            options.put("send_sms_hash",true);
            options.put("allow_rotation", true);
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", (float) amount * 100);
            JSONObject preFill = new JSONObject();
            preFill.put("email", Functions.getSharedPreference(this).getString(Variables.U_EMAIL,""));
            preFill.put("contact", Functions.getSharedPreference(this).getString(Variables.NUMBER,""));
            options.put("prefill", preFill);

            checkout.open(this, options);
        } catch (Exception e) {
            Functions.cancelLoader();
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onPaymentSuccess(String s) {
        Intent intent = new Intent().putExtra("transaction",s);
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Log.d("onPaymentError",getIntent().getStringExtra("price"));
        setResult(RESULT_CANCELED);
        finish();
    }
}