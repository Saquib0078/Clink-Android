package com.visticsolution.posterbanao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.visticsolution.posterbanao.databinding.ItemCategoryListBinding;
import com.visticsolution.posterbanao.model.CategoryModel;

import java.util.List;


public class SelectCategoryAdapter extends RecyclerView.Adapter<SelectCategoryAdapter.ViewHolder> {

    public interface OnCategorySelect{
        void onSelect(CategoryModel model);
    }

    private final List<CategoryModel> list;
    private final OnCategorySelect listener;
    Context context;
    int selectedPosition = 500;

    public SelectCategoryAdapter(Context context, List<CategoryModel> list, OnCategorySelect listener) {
        this.list = list;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCategoryListBinding binding = ItemCategoryListBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoryModel model = list.get(position);
        holder.binding.setCategory(model);
        if (this.selectedPosition == position) {
            holder.binding.radioBtn.setVisibility(View.VISIBLE);
        } else {
            holder.binding.radioBtn.setVisibility(View.GONE);
        }
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectCategoryAdapter recyclerOverLayAdapter = SelectCategoryAdapter.this;
                recyclerOverLayAdapter.notifyItemChanged(recyclerOverLayAdapter.selectedPosition);
                SelectCategoryAdapter recyclerOverLayAdapter2 = SelectCategoryAdapter.this;
                recyclerOverLayAdapter2.selectedPosition = position;
                recyclerOverLayAdapter2.notifyItemChanged(recyclerOverLayAdapter2.selectedPosition);
                listener.onSelect(model);
            }
        });

    }



    @Override
    public int getItemCount() {
        return list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        ItemCategoryListBinding binding;

        ViewHolder(@NonNull ItemCategoryListBinding itemView) {
            super(itemView.getRoot());

            binding = itemView;

        }
    }

}