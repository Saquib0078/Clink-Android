package com.nirmiteepublic.clink.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nirmiteepublic.clink.adapters.viewholders.ItemOptionViewHolder;
import com.nirmiteepublic.clink.databinding.ItemOptionBinding;
import com.nirmiteepublic.clink.functions.listeners.option.OptionsRequestCallBackListener;

import java.util.List;

public class OptionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<String> optionsList;
    private final OptionsRequestCallBackListener callBackListener;

    public OptionsAdapter(List<String> optionsList, OptionsRequestCallBackListener callBackListener) {
        this.optionsList = optionsList;
        this.callBackListener = callBackListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemOptionViewHolder(ItemOptionBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), optionsList.get(viewType));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ItemOptionViewHolder) holder).bind(callBackListener);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return optionsList.size();
    }

}
