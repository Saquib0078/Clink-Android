package com.nirmiteepublic.clink.adapters.viewholders;

import androidx.recyclerview.widget.RecyclerView;

import com.nirmiteepublic.clink.databinding.ItemOptionBinding;
import com.nirmiteepublic.clink.functions.listeners.option.OptionsRequestCallBackListener;

public class ItemOptionViewHolder extends RecyclerView.ViewHolder {

    private final String option;
    ItemOptionBinding binding;

    public ItemOptionViewHolder(ItemOptionBinding binding, String option) {
        super(binding.getRoot());
        this.binding = binding;
        this.option = option;
    }

    public void bind(OptionsRequestCallBackListener callBackListener) {
        binding.title.setText(option);
        binding.getRoot().setOnClickListener(v -> callBackListener.onOptionSelect(option));
    }


}