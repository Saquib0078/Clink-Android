package com.nirmiteepublic.clink.adapters.viewholders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.databinding.ItemMediaImageBinding;
import com.nirmiteepublic.clink.models.MediaModel;
import com.nirmiteepublic.clink.ui.activity.pages.SelectMediaActivity;
import com.nirmiteepublic.clink.ui.activity.pages.broadcast.PublishBroadcastActivity;

import java.util.ArrayList;
import java.util.List;

public class ItemMediaImageViewHolder extends RecyclerView.ViewHolder {

    private final Context context;
    private final MediaModel model;
    ItemMediaImageBinding binding;
    private final List<MediaModel> selectedMediaModels;

    public ItemMediaImageViewHolder(ItemMediaImageBinding binding, Context context, MediaModel model,List<MediaModel> selectedMediaModels) {
        super(binding.getRoot());
        this.binding = binding;
        this.context = context;
        this.model = model;
        this.selectedMediaModels=selectedMediaModels;
    }

    public void bind() {
        Glide.with(context).load(model.getUri()).placeholder(R.drawable.round_image_placeholder).into(binding.image);
        binding.getRoot().setOnClickListener(v -> {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)
                    context, binding.image, "media_trans_image");

                if (selectedMediaModels.contains(model)) {
                    selectedMediaModels.remove(model);
                    binding.image.setAlpha(1.0f);  // Unselected state
                } else {
                    selectedMediaModels.add(model);
                    binding.image.setAlpha(0.5f);  // Selected state
                }
//            ((Activity) context).startActivityForResult(new Intent(context, PublishBroadcastActivity.class)
//                    .putExtra("uri", model.getUri().toString()).putExtra("filePath", model.getFilePath())
//                    .putExtra("type", model.getMimeType()), SelectMediaActivity
//                    .REQUEST_CODE_PUBLISH_BROADCAST, options.toBundle());
        });
    }
}
