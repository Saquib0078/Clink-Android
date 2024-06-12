package com.visticsolution.posterbanao.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.visticsolution.posterbanao.OpenPostActivity;
import com.visticsolution.posterbanao.R;
import com.visticsolution.posterbanao.databinding.ItemCustomeCategoryHomeBinding;
import com.visticsolution.posterbanao.model.CategoryModel;

import java.util.ArrayList;
import java.util.List;

public class CustomeCategoryAdapter extends RecyclerView.Adapter<CustomeCategoryAdapter.ViewHolder> {

    Context context;
    List<CategoryModel> list = new ArrayList<>();
    int colorCount = 0;

    public CustomeCategoryAdapter(Context context, List<CategoryModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCustomeCategoryHomeBinding binding = ItemCustomeCategoryHomeBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        int pos = position;
        holder.binding.setCategory(list.get(pos));

        colorCount++;
        if (colorCount == 9){
            colorCount = 0;
        }

        String[] colorsTxt = context.getResources().getStringArray(R.array.cat_colors);
        holder.binding.ly.getBackground().setTint(Color.parseColor(colorsTxt[colorCount]));

        try {
            holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, OpenPostActivity.class)
                            .putExtra("title",list.get(pos).getName())
                            .putExtra("type","category")
                            .putExtra("item_id",list.get(pos).getId()));
                }
            });
        }catch (Exception e){

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemCustomeCategoryHomeBinding binding;

        public ViewHolder(@NonNull ItemCustomeCategoryHomeBinding itemView) {
            super(itemView.getRoot());

            binding = itemView;
        }
    }
}
