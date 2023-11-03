package com.nirmiteepublic.clink.adapters.pagers;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.databinding.ItemCardviewBinding;
import com.nirmiteepublic.clink.models.CardViewItem;

import java.util.List;

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.CardViewHolder> {
    Context context;
    private List<CardViewItem> items;
    private int selectedItemIndex = -1; // Index of the selected item

    public CardViewAdapter(List<CardViewItem> items) {
        this.items = items;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new CardViewHolder(ItemCardviewBinding.inflate(((Activity) parent.getContext()).getLayoutInflater(), parent, false));
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        CardViewItem item = items.get(position);
        holder.binding.textview.setText(item.getText());
        if (position == selectedItemIndex) {
            holder.itemView.setBackgroundResource(R.drawable.item_cardview_border);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.item_cardview_border_white);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, item.getText(), Toast.LENGTH_SHORT).show();
                int previousSelectedItem = selectedItemIndex;
                selectedItemIndex = position;

                // Deselect the previously selected item
                if (previousSelectedItem != -1) {
                    notifyItemChanged(previousSelectedItem);
                }

                // Notify that the item has changed, which will update its background
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {
        ItemCardviewBinding binding;

        public CardViewHolder(ItemCardviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}

