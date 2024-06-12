package com.visticsolution.posterbanao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.visticsolution.posterbanao.databinding.ItemTamplateVideoBinding;
import com.visticsolution.posterbanao.interfaces.AdapterClickListener;
import com.visticsolution.posterbanao.model.VideoTamplateCategory;

import java.util.ArrayList;
import java.util.List;

public class VideoTamplateHomeAdapter extends RecyclerView.Adapter<VideoTamplateHomeAdapter.ViewModel> {

    Context context;
    List<VideoTamplateCategory> list = new ArrayList<>();
    AdapterClickListener listener;

    public VideoTamplateHomeAdapter(Context context, List<VideoTamplateCategory> list, AdapterClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VideoTamplateHomeAdapter.ViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTamplateVideoBinding videoBinding = ItemTamplateVideoBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ViewModel(videoBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoTamplateHomeAdapter.ViewModel holder, int position) {
        VideoTamplateCategory model = list.get(position);
        holder.binding.title.setVisibility(View.VISIBLE);
        holder.binding.title.setText(model.getName());

        try {
            if (model.getVideos().size() > 0){
                holder.binding.setPosts(model.getVideos().get(0));

                holder.binding.getRoot().setOnClickListener(view -> {
                    listener.onItemClick(view,position,model);
                });
            }
        }catch (Exception e){
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewModel extends RecyclerView.ViewHolder {

        ItemTamplateVideoBinding binding;

        public ViewModel(ItemTamplateVideoBinding posts) {
            super(posts.getRoot());

            binding = posts;
        }
    }
}
