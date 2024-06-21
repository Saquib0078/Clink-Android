package com.visticsolution.posterbanao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.visticsolution.posterbanao.R;
import com.visticsolution.posterbanao.classes.Functions;
import com.visticsolution.posterbanao.databinding.ItemTransactionHistriBinding;
import com.visticsolution.posterbanao.interfaces.AdapterClickListener;
import com.visticsolution.posterbanao.model.TransactionModel;

import java.util.List;


public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private final List<TransactionModel> list;
    private final AdapterClickListener listener;
    Context context;

    public TransactionAdapter(Context context, List<TransactionModel> list, AdapterClickListener listener) {
        this.list = list;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTransactionHistriBinding binding = ItemTransactionHistriBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TransactionModel model = list.get(position);
        holder.binding.idTv.setText(String.valueOf(position)+1);
        holder.binding.titleTv.setText(model.getTitle());
        holder.binding.amountTv.setText(context.getString(R.string.currency)+" "+model.getAmount());
        holder.binding.dateTv.setText(Functions.getFormatedDate(model.getCreated_at()));
        if (model.getType().equals("credit")){
            holder.binding.typeTv.setText(context.getString(R.string.credit));
            holder.binding.typeTv.setTextColor(context.getColor(R.color.success));
        }else if (model.type.equals("debit")){
            holder.binding.typeTv.setText(context.getString(R.string.debit));
            holder.binding.typeTv.setTextColor(context.getColor(R.color.error));
        }

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

        ItemTransactionHistriBinding binding;

        ViewHolder(@NonNull ItemTransactionHistriBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

    }

}