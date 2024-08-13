package com.visticsolution.posterbanao.payment;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.instamojo.android.Instamojo;
import com.instamojo.android.activities.PaymentDetailsActivity;
import com.instamojo.android.helpers.Constants;
import com.visticsolution.posterbanao.classes.Functions;
import com.visticsolution.posterbanao.classes.Variables;
import com.visticsolution.posterbanao.databinding.ActivityInstamojoBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class InstamojoActivity extends AppCompatActivity implements Instamojo.InstamojoPaymentCallback {

    ActivityInstamojoBinding binding;
    private Context context;
    private ProgressDialog dialog;
    private static final String TAG = PaymentDetailsActivity.class.getSimpleName();
    private final String currentEnv = null;
    private String accessToken = null;
    String price = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInstamojoBinding.inflate(getLayoutInflater());

        Instamojo.getInstance().initialize(this, Instamojo.Environment.PRODUCTION);
        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setMessage("please wait...");
        dialog.setCancelable(false);

        price = getIntent().getStringExtra("price");
        if (!(Integer.parseInt(price) > 9)){
            price = price+".00";
        }
        context = this;
        binding.getRoot();
        tokenCreate();
    }


    private void tokenCreate() {
        dialog.show();
        StringRequest str = new StringRequest(com.android.volley.Request.Method.POST, "https://api.instamojo.com/oauth2/token/", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("LOG_VOLLEY", response);
                try {
                    JSONObject responseObject = new JSONObject(response);
                    accessToken = responseObject.getString("access_token");
                    orderPayment(responseObject.getString("access_token"));
                    //Payment(accessToken);
                } catch (JSONException e) {
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                setResult(RESULT_CANCELED);
                finish();
                // Toast.makeText(context, "Failed to Send Message", Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("grant_type", "client_credentials");
                map.put("client_id", Functions.getSharedPreference(context).getString("client_id",""));
                map.put("client_secret", Functions.getSharedPreference(context).getString("client_secret",""));
                System.out.println(map);
                return map;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(60000, 2, 0));
        Volley.newRequestQueue(context).add(str);
    }

    private void orderPayment(String accessToken) {
        StringRequest str = new StringRequest(com.android.volley.Request.Method.POST, "https://api.instamojo.com/v2/payment_requests/", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("LOG_VOLLEY  mm", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    CreateOrderId(jsonObject.optString("id"));
                } catch (Exception e) {
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(" mm", error.toString());
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                setResult(RESULT_CANCELED);
                finish();
                // Toast.makeText(context, "Failed to Send Message", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + accessToken);
                System.out.println("From Header:----" + params);
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> map = new HashMap<>();
                map.put("purpose", "subscription");
                map.put("amount",price);
                map.put("buyer_name", Functions.getSharedPreference(context).getString(Variables.NAME,""));
                map.put("email", Functions.getSharedPreference(context).getString(Variables.U_EMAIL,""));
                map.put("phone", Functions.getSharedPreference(context).getString(Variables.NUMBER,""));
                map.put("redirect_url", "https://test.instamojo.com/integrations/android/redirect/");
                map.put("webhook", "");
                map.put("allow_repeated_payments", "False");
                System.out.println(map);
                return map;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(60000, 2, 0));
        Volley.newRequestQueue(context).add(str);
    }

    private void CreateOrderId(String id) {
        StringRequest str = new StringRequest(com.android.volley.Request.Method.POST, "https://api.instamojo.com/v2/gateway/orders/payment-request/", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("LOG_VOLLEY  mm", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    initiateSDKPayment(jsonObject.optString("order_id"));
                } catch (Exception e) {
                }


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Log.e(" mm", error.toString());
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                setResult(RESULT_CANCELED);
                finish();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + accessToken);
                System.out.println("From Header:----" + params);
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> map = new HashMap<>();
                map.put("id", id);
                System.out.println(map);
                return map;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(60000, 2, 0));
        Volley.newRequestQueue(context).add(str);
    }


    private void initiateSDKPayment(String orderID) {
        Log.e("RRRR", orderID);
        dialog.dismiss();
        Instamojo.getInstance().initiatePayment(this, orderID, this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE && data != null) {
            String orderID = data.getStringExtra(Constants.ORDER_ID);
            String transactionID = data.getStringExtra(Constants.TRANSACTION_ID);
            String paymentID = data.getStringExtra(Constants.PAYMENT_ID);

            // Check transactionID, orderID, and orderID for null before using them to check the Payment status.
            if (transactionID != null || paymentID != null) {
                //mk checkPaymentStatus(transactionID, orderID);
            } else {
                showToast("Oops!! Payment was cancelled");
            }
        }
    }

    private void showToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onInstamojoPaymentComplete(String s, String s1, String s2, String s3) {
        Log.e("RESPONSE", s + s1 + s2 + s3);
        Log.d(TAG, "Payment complete. Order ID: " + s + ", Transaction ID: " + s1
                + ", Payment ID:" + s2 + ", Status: " + s3);
        Intent intent = new Intent().putExtra("transaction",s);
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public void onPaymentCancelled() {
        showToast("Payment cancelled");
        setResult(RESULT_CANCELED);
        finish();
        Log.d(TAG, "Payment cancelled");
        Log.e("MISBA", "Payment cancelled");
    }

    @Override
    public void onInitiatePaymentFailure(String s) {
        Log.d(TAG, "Initiate payment failed");
        Log.e("LAST", "Initiate payment failed" + s);
        showToast("Initiating payment failed. Error: " + s);
        setResult(RESULT_CANCELED);
        finish();
    }
}