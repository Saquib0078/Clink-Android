package com.nirmiteepublic.clink.ui.fragments;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.nirmiteepublic.clink.adapters.ChipBtnAdapter;
import com.nirmiteepublic.clink.adapters.GraphicsAdapter;
import com.nirmiteepublic.clink.adapters.GraphicsUniversalAdapter;
import com.nirmiteepublic.clink.adapters.ImageSliderAdapter;
import com.nirmiteepublic.clink.databinding.FragmentGraphicsBinding;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;
import com.nirmiteepublic.clink.functions.viewmanagers.PegaFragment;
import com.nirmiteepublic.clink.models.ChipBtnResponse;
import com.nirmiteepublic.clink.models.GraphicModel;
import com.nirmiteepublic.clink.models.SliderImageResponse;
import com.nirmiteepublic.clink.models.SlidersItem;
import com.nirmiteepublic.clink.models.UniversalModel;
import com.nirmiteepublic.clink.ui.activity.pages.GraphicsFilterActivity;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class GraphicsFragment extends PegaFragment implements GraphicsAdapter.GraphicItemClickListener {

    FragmentGraphicsBinding binding;
    List<String> chipButtonList;
    String sliderID;
    List<GraphicModel> graphicModelList;
    private ChipBtnAdapter adapter;
    String type;
    String GraphicID;
//    String title;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentGraphicsBinding.inflate(inflater, container, false);

//        String title = getArguments().getString("title");

       binding.searchBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String text=binding.searchView.getText().toString().trim();
               Intent intent=new Intent(requireContext(), GraphicsFilterActivity.class);
               intent.putExtra("text",text);
               startActivity(intent);
           }
       });
        fetchDataFromApi();


        RetrofitClient.getInstance(requireContext()).getApiInterfaces().getChipBtns().enqueue(new Callback<ChipBtnResponse>() {
            @Override
            public void onResponse(Call<ChipBtnResponse> call, retrofit2.Response<ChipBtnResponse> response) {
                if (response.isSuccessful()) {
                    chipButtonList = new ArrayList<>();
                    Gson gson = new Gson();
                    String jsonResponse = gson.toJson(response.body().getChipBtns());

                    try {
                        JSONArray jsonArray = new JSONArray(jsonResponse);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String chipButtonListValue = jsonObject.getString("chipButtonList");
                            chipButtonList.add(chipButtonListValue);
                            ChipBtnAdapter chipBtnAdapter = new ChipBtnAdapter(chipButtonList);
                            binding.recview.setAdapter(chipBtnAdapter);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ChipBtnResponse> call, Throwable t) {
                Toast.makeText(requireContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RetrofitClient.getInstance(requireContext()).getApiInterfaces().getSlider().enqueue(new Callback<SliderImageResponse>() {
            @Override
            public void onResponse(Call<SliderImageResponse> call, retrofit2.Response<SliderImageResponse> response) {
                List<String> sliderUrls = new ArrayList<>();

                SliderImageResponse sliderResponse = response.body();
                Gson gson = new Gson();

                String jsonResponse = gson.toJson(sliderResponse);
                System.out.println(jsonResponse);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(jsonResponse);
                } catch (JSONException e) {
                    Toast.makeText(requireContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
                try {
                    JSONArray slidersArray = jsonObject.getJSONArray("sliders");
                    for (int i = 0; i < slidersArray.length(); i++) {
                        JSONObject sliderObj = slidersArray.getJSONObject(i);
                        sliderID = sliderObj.getString("_id");
                        String image = sliderObj.getString("slider");
                        String imageUrl = RetrofitClient.BASE_URL + "graphics/getgraphics/" + image;
                        sliderUrls.add(imageUrl);
                    }
                } catch (JSONException e) {
                    Toast.makeText(requireContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
                try {
                    initImageSlider(new JSONArray(sliderUrls));
                } catch (JSONException e) {
                    Toast.makeText(requireContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<SliderImageResponse> call, Throwable t) {
                Toast.makeText(requireContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });




        /* Detect if Image is Visible to user
         * if false then change the status bar color to white
         * if true the set the default */
        binding.getRoot().getViewTreeObserver().addOnScrollChangedListener(() -> {
            Rect scrollBounds = new Rect();
            binding.getRoot().getHitRect(scrollBounds);
            if (binding.headerContainer.getLocalVisibleRect(scrollBounds)) {
                setWindowCyan();
            } else {
                setWindowWhite();
            }
        });
        return binding.getRoot();
    }


    private void initImageSlider(JSONArray data) throws JSONException {
        List<SlidersItem> sliderImages = new ArrayList<>();

        for (int i = 0; i < data.length(); i++) {
            sliderImages.add(new SlidersItem(data.getString(i)));
        }

        ImageSliderAdapter adapter = new ImageSliderAdapter(sliderImages);


        binding.imageSlider.setSliderAdapter(adapter);

        /* Set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!! */
        binding.imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM);
        binding.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        binding.imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        binding.imageSlider.setIndicatorSelectedColor(Color.WHITE);
        binding.imageSlider.setIndicatorUnselectedColor(Color.GRAY);
        binding.imageSlider.setScrollTimeInSec(4);
        binding.imageSlider.startAutoCycle();
    }


    public void fetchDataFromApi() {   //+ "?title=" + title
        String url = RetrofitClient.BASE_URL + "graphics/getGraphicsMedia";
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");

                            if (status.equals("success")) {
                                JSONArray graphicDataArray = response.getJSONArray("graphics");
                                System.out.println(graphicDataArray);
                                // Initialize list to store UniversalModels
                                List<UniversalModel> universalModels = new ArrayList<>();

                                for (int i = 0; i < graphicDataArray.length(); i++) {
                                    // Extract each object from graphicData array
                                    JSONObject graphicData = graphicDataArray.getJSONObject(i);
                                    System.out.println(graphicData);
                                    // Extract title and type
                                    String title = graphicData.getString("title");
                                    String type = graphicData.getString("type");
                                    GraphicID = graphicData.getString("_id");
                                    String createdat=graphicData.getString("createdAt");
                                    // Extract slider array
                                    JSONArray graphicModelListArray = graphicData.getJSONArray("graphicModelList");
                                    graphicModelList = new ArrayList<>();
                                    for (int j = 0; j < graphicModelListArray.length(); j++) {
                                        JSONObject graphicModelObj = graphicModelListArray.getJSONObject(j);
                                        String imageId = graphicModelObj.getString("_id");
                                        String imageCode = graphicModelObj.getString("filename");
                                        String imageUrl = RetrofitClient.BASE_URL + "graphics/getgraphics/" + imageCode;
                                        graphicModelList.add(new GraphicModel(type, imageUrl, imageId));

                                    }


                                    UniversalModel universalModel = new UniversalModel(title, type,createdat, GraphicID);
//                                    universalModel.setChipButtonList(chipButtonList);
                                    universalModel.setGraphicModelList(graphicModelList);

                                    universalModels.add(new UniversalModel(title, "image-graphic-row-2",createdat, GraphicID).setChipButtonList(chipButtonList).setGraphicModelList(graphicModelList));

                                }

//                                universalModels.add(new UniversalModel(title, "image-graphic-row-2").setChipButtonList(chipButtonList).setGraphicModelList(graphicModelList));

                                GraphicsUniversalAdapter graphicsUniversalAdapter = new GraphicsUniversalAdapter(universalModels);
                                binding.recyclerView.setAdapter(graphicsUniversalAdapter);
                            } else {
                                // Handle the case where the API call was not successful
                                Toast.makeText(requireContext(), "API call was not successful", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(requireContext(), "Error parsing JSON", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle errors
                Toast.makeText(requireContext(), "Error fetching data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the request queue
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onGraphicItemClicked(String graphicId, String id) {

//     DeleteGraphicsImage(GraphicID,graphicId);
//        Toast.makeText(requireContext(), ".."+id, Toast.LENGTH_SHORT).show();
        Toast.makeText(requireContext(), graphicId, Toast.LENGTH_SHORT).show();
    }


    public void DeleteGraphicsImage(String id, String GraphicsId) {
        RetrofitClient.getInstance(requireContext()).getApiInterfaces().deleteGraphicObject(GraphicsId, id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "Image Deleted Successfully", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(requireContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}