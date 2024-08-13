package com.visticsolution.posterbanao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.visticsolution.posterbanao.databinding.ItemCustomeTamplateCategoryBinding;
import com.visticsolution.posterbanao.interfaces.AdapterClickListener;
import com.visticsolution.posterbanao.model.InvitationCategoryModel;

import java.util.List;

public class CustomTamplateCategoryAdapter extends RecyclerView.Adapter<CustomTamplateCategoryAdapter.ViewHolder> {

    Context context;
    List<InvitationCategoryModel> list;
    AdapterClickListener adapterClickListener;

    public CustomTamplateCategoryAdapter(Context context, List<InvitationCategoryModel> list, AdapterClickListener adapterClickListener) {
        this.context = context;
        this.list = list;
        this.adapterClickListener = adapterClickListener;
    }

    @NonNull
    @Override
    public CustomTamplateCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCustomeTamplateCategoryBinding binding = ItemCustomeTamplateCategoryBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomTamplateCategoryAdapter.ViewHolder holder, int position) {
        holder.binding.setPosts(list.get(position));
        holder.binding.getRoot().setOnClickListener(view -> {
            adapterClickListener.onItemClick(view,position,list.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemCustomeTamplateCategoryBinding binding;

        public ViewHolder(@NonNull ItemCustomeTamplateCategoryBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
