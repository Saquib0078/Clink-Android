package com.nirmiteepublic.clink.ui.activity.pages;

import androidx.appcompat.app.AppCompatActivity;

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
import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.adapters.GraphicsAdapter;
import com.nirmiteepublic.clink.adapters.GraphicsUniversalAdapter;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;
import com.nirmiteepublic.clink.functions.viewmanagers.PegaAppCompatActivity;
import com.nirmiteepublic.clink.models.GraphicModel;
import com.nirmiteepublic.clink.models.UniversalModel;
import com.nirmiteepublic.clink.ui.activity.ViewAllActivity;
import com.pegalite.popups.DialogData;
import com.pegalite.popups.PegaProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GraphicsFilterActivity extends PegaAppCompatActivity {
com.nirmiteepublic.clink.databinding.ActivityGraphicsFilterBinding binding;
    private PegaProgressDialog progressDialog;

    List<GraphicModel> graphicModelList;
    String GraphicID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= com.nirmiteepublic.clink.databinding.ActivityGraphicsFilterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setWindowThemeSecond();
        progressDialog = new PegaProgressDialog(this);

        Intent intent = getIntent();
        String yourData = intent.getStringExtra("chip");
        String text = intent.getStringExtra("text");
//        Toast.makeText(this, ""+text, Toast.LENGTH_SHORT).show();
//        fetchDataFromApi(yourData);
//        fetchDataFromApi(text);

        showProgressDialog();

        if (yourData != null) {
            fetchDataFromApi(yourData);
            binding.tvheading.setText(yourData);
        } else if (text != null) {
            fetchDataFromApi(text);
            binding.tvheading.setText(text);
        } else {
            Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();        }

//        binding.tvheading.setText(yourData);


    }


    public void fetchDataFromApi(String title) {
        String url = RetrofitClient.BASE_URL + "graphics/getGraphicsMedia" + "?title=" + title;

        RequestQueue requestQueue = Volley.newRequestQueue(GraphicsFilterActivity.this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            hideProgressDialog();
                            JSONArray graphicsArray = response.getJSONArray("graphics");
                            JSONObject graphicsObject = graphicsArray.getJSONObject(0);
                            String title = graphicsObject.getString("title");
                            String type = graphicsObject.getString("type");
                            JSONArray graphicModelListArray = graphicsObject.getJSONArray("graphicModelList");

                            ArrayList<GraphicModel> graphicModels = new ArrayList<>();
                            for (int i = 0; i < graphicModelListArray.length(); i++) {
                                JSONObject graphicModelObj = graphicModelListArray.getJSONObject(i);
                                String imageUrl = RetrofitClient.BASE_URL + "graphics/getgraphics/" + graphicModelObj.getString("filename");
                                GraphicModel graphicModel = new GraphicModel(type, imageUrl);
                                graphicModels.add(graphicModel);
                            }

                            // Set up RecyclerView
                            GraphicsAdapter adapter = new GraphicsAdapter(graphicModels, null);
                            binding.recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            hideProgressDialog();
                            Toast.makeText(GraphicsFilterActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                Toast.makeText(GraphicsFilterActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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