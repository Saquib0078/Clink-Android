package com.visticsolution.posterbanao.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.visticsolution.posterbanao.PlayAnimatedVideActivity;
import com.visticsolution.posterbanao.binding.BindingAdaptet;
import com.visticsolution.posterbanao.databinding.ItemBussinessCategoryHomeBinding;
import com.visticsolution.posterbanao.model.CategoryModel;
import com.visticsolution.posterbanao.model.VideoTamplateCategory;

import java.util.ArrayList;
import java.util.List;

public class VideoTamplateCategoryAdapter extends RecyclerView.Adapter<VideoTamplateCategoryAdapter.ViewHolder> {

    Context context;
    List<VideoTamplateCategory> list = new ArrayList<>();

    public VideoTamplateCategoryAdapter(Context context, List<VideoTamplateCategory> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBussinessCategoryHomeBinding binding = ItemBussinessCategoryHomeBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int pos) {
        int position = pos;
        VideoTamplateCategory model = list.get(position);

        CategoryModel categoryModel = new CategoryModel();
        categoryModel.setName(model.getName());
        categoryModel.setImage(model.getImage());
        try {
            holder.binding.setCategory(categoryModel);
            holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PlayAnimatedVideActivity.selectedCategory = model;
                    context.startActivity(new Intent(context,PlayAnimatedVideActivity.class));
                }
            });
            BindingAdaptet.setImageUrl(holder.binding.iv,model.getImage());
        }catch (Exception e){

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemBussinessCategoryHomeBinding binding;

        public ViewHolder(@NonNull ItemBussinessCategoryHomeBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
