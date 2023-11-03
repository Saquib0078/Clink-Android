package com.nirmiteepublic.clink.adapters.pagers;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.nirmiteepublic.clink.databinding.ImageSliderBinding;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class ImageSliderAdapter extends
        SliderViewAdapter<ImageSliderAdapter.SliderAdapterViewHolder> {

    private final List<String> imageUrls;

    Context context;

    public ImageSliderAdapter(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    @Override
    public SliderAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        context = parent.getContext();
        return new SliderAdapterViewHolder(ImageSliderBinding.inflate(((Activity) parent.getContext()).getLayoutInflater()));
    }

    @Override
    public void onBindViewHolder(SliderAdapterViewHolder holder, final int position) {

        Glide.with(holder.itemView)
                .load(imageUrls.get(position))
                .fitCenter()
                .into(holder.binding.image);

    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    static class SliderAdapterViewHolder extends ViewHolder {

        ImageSliderBinding binding;

        public SliderAdapterViewHolder(ImageSliderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}