package com.nirmiteepublic.clink.adapters.pagers;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BroadcastMultiViewAdapter extends RecyclerView.Adapter<BroadcastMultiViewAdapter.BroadCastViewHolder> {
    @NonNull
    @Override
    public BroadcastMultiViewAdapter.BroadCastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BroadcastMultiViewAdapter.BroadCastViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class BroadCastViewHolder extends RecyclerView.ViewHolder{
        public BroadCastViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
}
