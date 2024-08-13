package com.visticsolution.posterbanao.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.visticsolution.posterbanao.OpenPostActivity;
import com.visticsolution.posterbanao.databinding.ItemFestivalCategoryHomeBinding;
import com.visticsolution.posterbanao.model.CategoryModel;

import java.util.ArrayList;
import java.util.List;

public class FestivalCategoryAdapter extends RecyclerView.Adapter<FestivalCategoryAdapter.ViewHolder> {

    Context context;
    List<CategoryModel> list = new ArrayList<>();

    public FestivalCategoryAdapter(Context context, List<CategoryModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFestivalCategoryHomeBinding binding = ItemFestivalCategoryHomeBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int pos) {
        int position = pos;
        holder.binding.setCategory(list.get(position));
        try {
            holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, OpenPostActivity.class)
                            .putExtra("title",list.get(position).getName())
                            .putExtra("type","category")
                            .putExtra("item_id",list.get(position).getId()));
                }
            });
        }catch (Exception e){}
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemFestivalCategoryHomeBinding binding;

        public ViewHolder(@NonNull ItemFestivalCategoryHomeBinding itemView) {
            super(itemView.getRoot());

            binding = itemView;

        }
    }
}
