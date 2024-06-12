package com.nirmiteepublic.clink.adapters.viewholders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.nirmiteepublic.clink.databinding.ItemMediaImageBinding;
import com.nirmiteepublic.clink.models.MediaModel;
import com.nirmiteepublic.clink.ui.activity.pages.SelectMediaActivity;
import com.nirmiteepublic.clink.ui.activity.pages.broadcast.PublishBroadcastActivity;

import java.util.Locale;

public class ItemMediaVideoViewHolder extends RecyclerView.ViewHolder {

    private final Context context;
    private final MediaModel model;
    ItemMediaImageBinding binding;

    public ItemMediaVideoViewHolder(ItemMediaImageBinding binding, Context context, MediaModel model) {
        super(binding.getRoot());
        this.binding = binding;
        this.context = context;
        this.model = model;
    }

    public void bind() {
        Thread thread = new Thread(this::setVideoThumbnail);
        thread.start();

        binding.duration.setVisibility(View.VISIBLE);
        binding.duration.setText(formatDuration(model.getDuration()));

        binding.getRoot().setOnClickListener(v -> {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, binding.image, "media_trans_video");
            ((Activity) context).startActivityForResult(new Intent(context, PublishBroadcastActivity.class).putExtra("uri", model.getUri().toString()).putExtra("filePath", model.getFilePath()).putExtra("type", model.getMimeType()), SelectMediaActivity.REQUEST_CODE_PUBLISH_BROADCAST, options.toBundle());
        });
    }


    private void setVideoThumbnail() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            try (MediaMetadataRetriever retriever = new MediaMetadataRetriever()) {
                retriever.setDataSource(context, model.getUri());
                Bitmap thumbnail = retriever.getFrameAtTime();
                new Handler(Looper.getMainLooper()).post(() -> binding.image.setImageBitmap(thumbnail));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String formatDuration(long durationMillis) {
        long durationSeconds = durationMillis / 1000;
        long minutes = durationSeconds / 60;
        long seconds = durationSeconds % 60;
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }
}
