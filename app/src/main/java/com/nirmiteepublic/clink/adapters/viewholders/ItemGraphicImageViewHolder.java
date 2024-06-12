package com.nirmiteepublic.clink.adapters.viewholders;

import android.content.Context;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.databinding.ItemGraphicsImageBinding;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;
import com.nirmiteepublic.clink.functions.utils.Utils;
import com.nirmiteepublic.clink.models.GraphicModel;

public class ItemGraphicImageViewHolder extends RecyclerView.ViewHolder {

    private final Context context;
    private final GraphicModel graphicModel;
    ItemGraphicsImageBinding binding;

    public ItemGraphicImageViewHolder(ItemGraphicsImageBinding binding, Context context, GraphicModel graphicModel) {
        super(binding.getRoot());
        this.binding = binding;
        this.context = context;
        this.graphicModel = graphicModel;
    }

    public void bind() {

        Glide.with(itemView)
                .load(graphicModel.getGraphicID())
                .fitCenter().placeholder(ContextCompat.getDrawable(context, R.drawable.round_image_placeholder))
                .into(binding.image);
    }
}