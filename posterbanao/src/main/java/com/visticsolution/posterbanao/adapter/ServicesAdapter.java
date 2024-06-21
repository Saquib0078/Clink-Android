package com.visticsolution.posterbanao.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.visticsolution.posterbanao.R;
import com.visticsolution.posterbanao.databinding.ItemServicesBinding;
import com.visticsolution.posterbanao.interfaces.AdapterClickListener;
import com.visticsolution.posterbanao.model.ServicesModel;

import java.util.List;


public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ViewHolder> {

    private final List<ServicesModel> list;
    private final AdapterClickListener listener;
    Context context;

    public ServicesAdapter(Context context, List<ServicesModel> list, AdapterClickListener listener) {
        this.list = list;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemServicesBinding binding = ItemServicesBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.setService(list.get(position));

        holder.binding.offerPrice.setText(context.getString(R.string.currency)+" "+list.get(position).getNew_price());
        holder.binding.priceTv.setText(context.getString(R.string.currency)+" "+list.get(position).getOld_price());
        holder.binding.priceTv.setPaintFlags(holder.binding.priceTv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.binding.applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(view,position,list.get(position));
            }
        });
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(view,position,list.get(position));
            }
        });
    }



    @Override
    public int getItemCount() {
        return list.size();
    }




    class ViewHolder extends RecyclerView.ViewHolder {

        ItemServicesBinding binding;

        ViewHolder(@NonNull ItemServicesBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

    }

}