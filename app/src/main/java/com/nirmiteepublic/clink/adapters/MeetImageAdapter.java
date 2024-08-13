package com.nirmiteepublic.clink.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nirmiteepublic.clink.adapters.viewholders.ItemMediaImageViewHolder;
import com.nirmiteepublic.clink.adapters.viewholders.ItemMediaVideoViewHolder;
import com.nirmiteepublic.clink.adapters.viewholders.ItemMediaViewholderMeet;
import com.nirmiteepublic.clink.databinding.ItemMediaImageBinding;
import com.nirmiteepublic.clink.models.MediaModel;

import java.util.List;

public class MeetImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<MediaModel> mediaModelList;

    public MeetImageAdapter(List<MediaModel> mediaModelList) {
        this.mediaModelList = mediaModelList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemMediaViewholderMeet(ItemMediaImageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), parent.getContext(), mediaModelList.get(viewType));

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemMediaImageViewHolder) {
            ((ItemMediaImageViewHolder) holder).bind();
            return;
        }
        ((ItemMediaVideoViewHolder) holder).bind();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mediaModelList.size();
    }

}
