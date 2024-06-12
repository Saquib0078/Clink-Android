package com.nirmiteepublic.clink.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.nirmiteepublic.clink.databinding.RowFrameViewBinding;
import com.nirmiteepublic.clink.functions.listeners.FrameListener;
import com.nirmiteepublic.clink.models.FrameModel;

import java.util.List;

public class FrameAdapter extends RecyclerView.Adapter<FrameAdapter.ViewHolder> {
    public static String selected_frame_name = "";
    Context context;
    private List<FrameModel> list;
    private FrameListener listener;

    public FrameAdapter(Context context, List<FrameModel> list, FrameListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RowFrameViewBinding binding = RowFrameViewBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final FrameModel frameModel = list.get(position);
        Glide.with(context).load(frameModel.getThumbnail()).into(holder.binding.imgFrame);

        if (selected_frame_name.equals(frameModel.getTitle())) {
            holder.binding.frameBgLay.setBackgroundTintList(null);
        } else {
            holder.binding.frameBgLay.getBackground().setTint(-3355444);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (frameModel.getType().equals("business") || frameModel.getType().equals("user")) {
                    listener.onFrameSelected(frameModel,position);
                }
                selected_frame_name = frameModel.getTitle();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RowFrameViewBinding binding;

        ViewHolder(RowFrameViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

