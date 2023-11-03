package com.nirmiteepublic.clink.adapters.pagers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nirmiteepublic.clink.databinding.ItemGraphicsImageBinding;
import com.nirmiteepublic.clink.models.ImageItem;
import com.nirmiteepublic.clink.ui.activity.pages.GraphicsDetails;

import java.util.List;

public class GraphicsImageAdapter extends RecyclerView.Adapter<GraphicsImageAdapter.ImageViewHolder> {
    private List<ImageItem> imageItems;
    private Context context;

    public GraphicsImageAdapter(List<ImageItem> imageItems) {
        this.imageItems = imageItems;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        return new GraphicsImageAdapter.ImageViewHolder(ItemGraphicsImageBinding.inflate(((Activity) parent.getContext()).getLayoutInflater(), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {

        ImageItem item = imageItems.get(position);

        // Load and display the image using Glide
        Glide.with(context)
                .load(item.getImageUrl())
                .into(holder.binding.imagegraphics);
        Glide.with(context)
                .load(item.getImageUrl())
                .into(holder.binding.imagegraphics1);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), GraphicsDetails.class);
                intent.putExtra("image_url", item.getImageUrl());
                v.getContext().startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return imageItems.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ItemGraphicsImageBinding binding;

        public ImageViewHolder(ItemGraphicsImageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
