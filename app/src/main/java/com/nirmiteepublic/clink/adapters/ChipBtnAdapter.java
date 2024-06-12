package com.nirmiteepublic.clink.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nirmiteepublic.clink.adapters.viewholders.ItemChipBtnViewHolder;
import com.nirmiteepublic.clink.databinding.ItemChipBtnBinding;
import com.nirmiteepublic.clink.functions.listeners.ChipBtnListener;
import com.nirmiteepublic.clink.models.ChipBtnsItem;

import java.util.ArrayList;
import java.util.List;

public class ChipBtnAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final List<ChipBtnListener> chipBtnListenerList = new ArrayList<>();
    private final List<String> chipButtonList;


    public int selected;
    Context context;

    public ChipBtnAdapter(List<String> chipButtonList) {
        this.chipButtonList = chipButtonList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context= parent.getContext();
        return new ItemChipBtnViewHolder(ItemChipBtnBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), parent.getContext(), chipButtonList.get(viewType));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ((ItemChipBtnViewHolder) holder).bind(this, position);

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return chipButtonList.size();
    }

}
