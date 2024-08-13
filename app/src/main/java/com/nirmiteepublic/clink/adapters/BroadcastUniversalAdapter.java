package com.nirmiteepublic.clink.adapters;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
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

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        if (holder instanceof ItemBroadcastMediaViewHolder) {
            ((ItemBroadcastMediaViewHolder) holder).stopVideo();
        }
    }
    public void setRecyclerView(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // Loop through all visible child views in the RecyclerView
                for (int i = 0; i < recyclerView.getChildCount(); i++) {
                    View child = recyclerView.getChildAt(i);
                    RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(child);

                    // Check if the view holder is an instance of ItemBroadcastMediaViewHolder
                    if (viewHolder instanceof ItemBroadcastMediaViewHolder) {
                        ItemBroadcastMediaViewHolder mediaViewHolder = (ItemBroadcastMediaViewHolder) viewHolder;

                        // Check if the view is not visible
                        if (!isViewVisible(recyclerView, child)) {
                            // Stop and mute the video
                            mediaViewHolder.stopVideo();
                            mediaViewHolder.muteVideo();
                        } else {
                            // Unmute the video if it's visible again (optional)
                            mediaViewHolder.unmuteVideo();
                        }
                    }
                }
            }
        });
    }

//    public void releaseAllPlayers() {
//        for (int i = 0; i < getItemCount(); i++) {
//            RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(i);
//            if (holder instanceof ItemBroadcastMediaViewHolder) {
//                ((ItemBroadcastMediaViewHolder) holder).releasePlayer();
//            }
//        }
//    }

    private boolean isViewVisible(RecyclerView recyclerView, View view) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager == null) return false;

        Rect bounds = new Rect();
        view.getHitRect(bounds);
        return layoutManager.isViewPartiallyVisible(view, true, true);
    }
}
