package com.nirmiteepublic.clink.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nirmiteepublic.clink.adapters.viewholders.ItemBroadcastHeaderViewHolder;
import com.nirmiteepublic.clink.adapters.viewholders.ItemBroadcastMediaViewHolder;
import com.nirmiteepublic.clink.adapters.viewholders.ItemProgressViewHolder;
import com.nirmiteepublic.clink.databinding.ItemBroadcastHeaderBinding;
import com.nirmiteepublic.clink.databinding.ItemBroadcastMediaBinding;
import com.nirmiteepublic.clink.databinding.ItemProgressBinding;
import com.nirmiteepublic.clink.functions.listeners.BroadcastCallbackListener;
import com.nirmiteepublic.clink.models.BroadcastModel;

import java.util.List;

public class BroadcastUniversalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<BroadcastModel> broadcastModelList;
    public BroadcastUniversalAdapter(List<BroadcastModel> broadcastModelList) {
        this.broadcastModelList = broadcastModelList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (broadcastModelList.get(viewType) == null) {
            return new ItemProgressViewHolder(ItemProgressBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }
        if ("header".equals(broadcastModelList.get(viewType).getType())) {
            return new ItemBroadcastHeaderViewHolder(ItemBroadcastHeaderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), parent.getContext());
        }
        return new ItemBroadcastMediaViewHolder(ItemBroadcastMediaBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), parent.getContext(), broadcastModelList.get(viewType));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemBroadcastHeaderViewHolder) {
            ((ItemBroadcastHeaderViewHolder) holder).bind();
        } else if (holder instanceof ItemBroadcastMediaViewHolder) {
            ((ItemBroadcastMediaViewHolder) holder).bind();
        } else if (holder instanceof ItemProgressViewHolder) {
            ((ItemProgressViewHolder) holder).bind();
        }


    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return broadcastModelList.size();
    }

}
