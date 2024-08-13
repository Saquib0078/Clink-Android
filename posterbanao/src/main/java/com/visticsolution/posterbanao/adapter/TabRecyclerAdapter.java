package com.visticsolution.posterbanao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.visticsolution.posterbanao.databinding.RecyclerTabItemBinding;
import com.visticsolution.posterbanao.interfaces.AdapterClickListener;

import java.util.List;


public class TabRecyclerAdapter extends RecyclerView.Adapter<TabRecyclerAdapter.ViewHolder> {

    private final List<String> titles;
    private final AdapterClickListener listener;
    Context context;
    int selectedPosition = 0;

    public TabRecyclerAdapter(Context context, List<String> list, AdapterClickListener listener) {
        this.titles = list;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerTabItemBinding binding = RecyclerTabItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.binding.titleTv.setActivated(this.selectedPosition == position);
        viewHolder.binding.titleTv.setText(titles.get(position));
        viewHolder.binding.getRoot().setOnClickListener(view -> {
            TabRecyclerAdapter recyclerOverLayAdapter = TabRecyclerAdapter.this;
            recyclerOverLayAdapter.notifyItemChanged(recyclerOverLayAdapter.selectedPosition);
            TabRecyclerAdapter recyclerOverLayAdapter2 = TabRecyclerAdapter.this;
            recyclerOverLayAdapter2.selectedPosition = position;
            recyclerOverLayAdapter2.notifyItemChanged(recyclerOverLayAdapter2.selectedPosition);
            listener.onItemClick(view, position, null);
        });
    }


    @Override
    public int getItemCount() {
        return titles.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        RecyclerTabItemBinding binding;

        ViewHolder(@NonNull RecyclerTabItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

    }

}