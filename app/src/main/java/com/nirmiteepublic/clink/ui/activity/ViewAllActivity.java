package com.nirmiteepublic.clink.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.nirmiteepublic.clink.adapters.GraphicsAdapter;
import com.nirmiteepublic.clink.databinding.ActivityViewAllBinding;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;
import com.nirmiteepublic.clink.functions.viewmanagers.PegaAppCompatActivity;
import com.nirmiteepublic.clink.models.GraphicModel;
import com.pegalite.popups.DialogData;
import com.pegalite.popups.PegaProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewAllActivity extends PegaAppCompatActivity {
    ActivityViewAllBinding binding;
    private PegaProgressDialog progressDialog;

    private RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewAllBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        progressDialog = new PegaProgressDialog(this);
        setWindowThemeSecond();
        showProgressDialog();
        requestQueue = Volley.newRequestQueue(this);
        Intent intent = getIntent();
        if (intent != null) {
            String viewID = intent.getStringExtra("id");
            fetchData(viewID);

        }

    }

    private void fetchData(String id) {
        String url = RetrofitClient.BASE_URL + "graphics/getGraphicsData/" + id; // Your API endpoint URL

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println(response);
                            hideProgressDialog();
                            JSONObject graphicsObject = response.getJSONObject("graphics");
                            String title = graphicsObject.getString("title");
                            binding.tv.setText(title);
                            String type = graphicsObject.getString("type");
                            JSONArray graphicModelListArray = graphicsObject.getJSONArray("graphicModelList");

                            ArrayList<GraphicModel> graphicModels = new ArrayList<>();
                            for (int i = 0; i < graphicModelListArray.length(); i++) {

                                JSONObject graphicModelObj = graphicModelListArray.getJSONObject(i);
                                JSONObject graphicModelObj1 = graphicModelListArray.getJSONObject(0);
                                String imageUrl0 = RetrofitClient.BASE_URL + "graphics/getgraphics/" + graphicModelObj1.getString("filename");
                                Glide.with(ViewAllActivity.this)
                                        .load(imageUrl0)
                                        .into(binding.imgMain);


                                String imageUrl = RetrofitClient.BASE_URL + "graphics/getgraphics/" + graphicModelObj.getString("filename");
                                GraphicModel graphicModel = new GraphicModel(type, imageUrl);
                                graphicModels.add(graphicModel);
                            }

                            // Set up RecyclerView
                            GraphicsAdapter adapter = new GraphicsAdapter(graphicModels, null);
                            binding.recview.setAdapter(adapter);

                        } catch (JSONException e) {
                            hideProgressDialog();
                            Toast.makeText(ViewAllActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                Toast.makeText(ViewAllActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        requestQueue.add(jsonObjectRequest);

    }

    private void showProgressDialog() {
        if (!progressDialog.isShowing()) {
            progressDialog.ShowProgress(DialogData.UN_CANCELABLE); // Show the progress dialog
        }
    }

    private void hideProgressDialog() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss(); // Dismiss the progress dialog
        }
    }

}