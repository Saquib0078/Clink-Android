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
import com.nirmiteepublic.clink.ui.activity.pages.CreateMeetActivity;
import com.nirmiteepublic.clink.ui.activity.pages.SelectMediaMeetActivity;

public class ItemMediaViewholderMeet extends RecyclerView.ViewHolder {

    private final Context context;
    private final MediaModel model;
    ItemMediaImageBinding binding;

    public ItemMediaViewholderMeet(ItemMediaImageBinding binding, Context context, MediaModel model) {
        super(binding.getRoot());
        this.binding = binding;
        this.context = context;
        this.model = model;
    }
    public void bind() {
        Glide.with(context).load(model.getUri()).placeholder(R.drawable.round_image_placeholder).into(binding.image);
        binding.getRoot().setOnClickListener(v -> {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)
                    context, binding.image, "media_trans_image");
            ((Activity) context).startActivityForResult(new Intent(context, CreateMeetActivity.class)
                    .putExtra("uri", model.getUri().toString()).putExtra("filePath", model.getFilePath())
                    .putExtra("type", model.getMimeType()), SelectMediaMeetActivity
                    .REQUEST_CODE_PUBLISH_MEET, options.toBundle());
        });
    }
}
