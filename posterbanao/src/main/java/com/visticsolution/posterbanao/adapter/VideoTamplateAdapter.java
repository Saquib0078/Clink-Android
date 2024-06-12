package com.visticsolution.posterbanao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.visticsolution.posterbanao.databinding.ItemTamplateVideoBinding;
import com.visticsolution.posterbanao.interfaces.AdapterClickListener;
import com.visticsolution.posterbanao.model.VideoTamplateModel;

import java.util.ArrayList;
import java.util.List;

public class VideoTamplateAdapter extends RecyclerView.Adapter<VideoTamplateAdapter.ViewModel> {

    Context context;
    List<VideoTamplateModel> list = new ArrayList<>();
    AdapterClickListener listener;
    int selectedPosition = 0;

    public VideoTamplateAdapter(Context context, List<VideoTamplateModel> list, AdapterClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VideoTamplateAdapter.ViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTamplateVideoBinding videoBinding = ItemTamplateVideoBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ViewModel(videoBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoTamplateAdapter.ViewModel holder, int position) {
        VideoTamplateModel model = list.get(position);
        try {
            if (this.selectedPosition == position) {
                holder.binding.viewImage.setVisibility(View.VISIBLE);
            } else {
                holder.binding.viewImage.setVisibility(View.INVISIBLE);
            }
            holder.binding.setPosts(model);
            holder.binding.getRoot().setOnClickListener(view -> {
                VideoTamplateAdapter recyclerOverLayAdapter = VideoTamplateAdapter.this;
                recyclerOverLayAdapter.notifyItemChanged(recyclerOverLayAdapter.selectedPosition);
                VideoTamplateAdapter recyclerOverLayAdapter2 = VideoTamplateAdapter.this;
                recyclerOverLayAdapter2.selectedPosition = position;
                recyclerOverLayAdapter2.notifyItemChanged(recyclerOverLayAdapter2.selectedPosition);
                listener.onItemClick(view,position,model);
            });
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
