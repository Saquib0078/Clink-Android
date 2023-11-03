package com.nirmiteepublic.clink.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.nirmiteepublic.clink.adapters.pagers.CardViewAdapter;
import com.nirmiteepublic.clink.adapters.pagers.GraphicsImageAdapter;
import com.nirmiteepublic.clink.adapters.pagers.ImageSliderAdapter;
import com.nirmiteepublic.clink.databinding.ComponentHorizontalRecyclerBinding;
import com.nirmiteepublic.clink.databinding.ComponentTitleBinding;
import com.nirmiteepublic.clink.databinding.FragmentGraphicsBinding;
import com.nirmiteepublic.clink.functions.viewmanagers.PegaFragment;
import com.nirmiteepublic.clink.models.CardViewItem;
import com.nirmiteepublic.clink.models.ImageItem;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class GraphicsFragment extends PegaFragment {
    FragmentGraphicsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGraphicsBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        try {
JSONObject object=new JSONObject("{\n  \"status\": \"success\",\n  \"data\": [\n    {\n      \"slider\": [\"https://cdn.pixabay.com/photo/2023/10/15/13/59/walnuts-8316999_1280.jpg\", \"https://cdn.pixabay.com/photo/2023/10/15/13/59/walnuts-8316999_1280.jpg\", \"https://cdn.pixabay.com/photo/2023/10/15/13/59/walnuts-8316999_1280.jpg\"],\n      \"sections\": [\n        [\n          {\n            \"type\": \"title\",\n            \"title\": \"Todays Trending\"\n          },\n          {\n            \"type\": \"queries\",\n            \"queries\": [\"hello\", \"123\", \"789\"]\n          },\n          {\n            \"type\": \"images\",\n            \"images\": [\"https://cdn.pixabay.com/photo/2023/10/15/13/59/walnuts-8316999_1280.jpg\", \"https://cdn.pixabay.com/photo/2023/10/15/13/59/walnuts-8316999_1280.jpg\",\"https://cdn.pixabay.com/photo/2023/10/16/10/51/fox-8318961_1280.png\",\"https://cdn.pixabay.com/photo/2023/10/16/10/51/fox-8318961_1280.png\",\"https://cdn.pixabay.com/photo/2023/10/16/10/51/fox-8318961_1280.png\",\"https://cdn.pixabay.com/photo/2023/10/16/10/51/fox-8318961_1280.png\",\"https://cdn.pixabay.com/photo/2023/10/16/10/51/fox-8318961_1280.png\"]\n          }\n        ],\n        [\n            {\n              \"type\": \"title\",\n              \"title\": \"Todays Trending\"\n            },\n            \n            {\n              \"type\": \"videos\",\n              \"videos\": [\"https://cdn.pixabay.com/photo/2023/10/15/13/59/walnuts-8316999_1280.jpg\", \"https://cdn.pixabay.com/photo/2023/10/15/13/59/walnuts-8316999_1280.jpg\"]\n            }\n          ],\n          [\n            {\n              \"type\": \"title\",\n              \"title\": \"Todays Trending\"\n            },\n        \n            {\n              \"type\": \"images\",\n              \"images\": [\"https://cdn.pixabay.com/photo/2023/10/15/13/59/walnuts-8316999_1280.jpg\", \"https://cdn.pixabay.com/photo/2023/10/15/13/59/walnuts-8316999_1280.jpg\"]\n            }\n          ]\n      ]\n    }\n  ]\n}\n");
            initView(object.getJSONArray("data").getJSONObject(0));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return rootView;
    }

    private void initView(JSONObject data) throws JSONException {
        initImageSlider(data.getJSONArray("slider"));

        JSONArray sections = data.getJSONArray("sections");
        for (int i = 0; i < sections.length(); i++) {
            JSONArray section = sections.getJSONArray(i);
            for (int j = 0; j < section.length(); j++) {
                JSONObject component = section.getJSONObject(j);
                createView(component);
            }
        }

    }

    private void createView(JSONObject component) throws JSONException {
        switch (component.getString("type")) {
            case "title":
                createComponentTitle(component);
                break;
            case "queries":
                createComponentQueries(component);
                break;
            case "images":
                createComponentImages(component);
                break;
        }
    }

    private void createComponentImages(JSONObject component) throws JSONException {
        ComponentHorizontalRecyclerBinding componentQueriesBinding = ComponentHorizontalRecyclerBinding.inflate(getLayoutInflater(), binding.container, false);

        List<ImageItem> items = new ArrayList<>();
        JSONArray queries = component.getJSONArray("images");
        for (int i = 0; i < queries.length(); i++) {
            items.add(new ImageItem(queries.getString(i)));
        }
        GraphicsImageAdapter adapter = new GraphicsImageAdapter(items);
        componentQueriesBinding.recyclerview.setAdapter(adapter);
        binding.container.addView(componentQueriesBinding.getRoot());
    }

    private void createComponentQueries(JSONObject component) throws JSONException {
        ComponentHorizontalRecyclerBinding componentQueriesBinding = ComponentHorizontalRecyclerBinding.inflate(getLayoutInflater(), binding.container, false);
        List<CardViewItem> items = new ArrayList<>();
        JSONArray queries = component.getJSONArray("queries");
        for (int i = 0; i < queries.length(); i++) {
            items.add(new CardViewItem(queries.getString(i)));
        }
        CardViewAdapter adapter = new CardViewAdapter(items);
        componentQueriesBinding.recyclerview.setAdapter(adapter);
        binding.container.addView(componentQueriesBinding.getRoot());
    }

    private void createComponentTitle(JSONObject component) throws JSONException {
        ComponentTitleBinding componentTitleBinding = ComponentTitleBinding.inflate(getLayoutInflater(), binding.container, false);
        componentTitleBinding.title.setText(component.getString("title"));
        binding.container.addView(componentTitleBinding.getRoot());
    }

    private void initImageSlider(JSONArray data) throws JSONException {
        List<String> sliderImages = new ArrayList<>();

        for (int i = 0; i < data.length(); i++) {
            sliderImages.add(data.getString(i));
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

}
