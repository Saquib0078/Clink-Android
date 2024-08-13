package com.visticsolution.posterbanao.editor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.visticsolution.posterbanao.binding.BindingAdaptet;
import com.visticsolution.posterbanao.classes.Functions;
import com.visticsolution.posterbanao.databinding.ItemBackgroundBinding;
import com.visticsolution.posterbanao.interfaces.AdapterClickListener;
import com.visticsolution.posterbanao.model.BackgroundModel;

import java.util.ArrayList;
import java.util.List;

public class BackgroundAdapter extends RecyclerView.Adapter<BackgroundAdapter.ViewHolder> {

    Context context;
    List<BackgroundModel> list = new ArrayList<>();
    AdapterClickListener clickListener;

    public BackgroundAdapter(Context context, List<BackgroundModel> list, AdapterClickListener clickListener) {
        this.context = context;
        this.list = list;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBackgroundBinding binding = ItemBackgroundBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            BindingAdaptet.setImageUrl(holder.binding.imgView, Functions.getItemBaseUrl(list.get(position).item_url));
            holder.binding.imgView.setOnClickListener(view -> {
                clickListener.onItemClick(view,position,list.get(position));
            });
        }catch (Exception e){}
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemBackgroundBinding binding;

        public ViewHolder(@NonNull ItemBackgroundBinding itemView) {
            super(itemView.getRoot());

            binding = itemView;
        }
    }
}
