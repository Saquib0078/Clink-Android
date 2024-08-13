package com.visticsolution.posterbanao.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.visticsolution.posterbanao.R;
import com.visticsolution.posterbanao.databinding.ItemSubscriptionBinding;
import com.visticsolution.posterbanao.interfaces.AdapterClickListener;
import com.visticsolution.posterbanao.model.SubscriptionModel;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionAdapter.ViewHolder> {

    Context context ;
    List<SubscriptionModel> wallet_modelArrayList = new ArrayList<>();
    AdapterClickListener adapter_click_listener;

    public SubscriptionAdapter(Context context, List<SubscriptionModel> wallet_modelArrayList, AdapterClickListener adapter_click_listener) {
        this.context = context;
        this.wallet_modelArrayList = wallet_modelArrayList;
        this.adapter_click_listener=adapter_click_listener;
    }

    @NonNull
    @Override
    public SubscriptionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        ItemSubscriptionBinding binding = ItemSubscriptionBinding.inflate(LayoutInflater.from(context), viewGroup, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SubscriptionAdapter.ViewHolder holder, int position) {
        SubscriptionModel model = wallet_modelArrayList.get(position);
        holder.binding.setSubscription(model);
        holder.bind(position,wallet_modelArrayList.get(position),adapter_click_listener);

        holder.binding.priceTv.setText(context.getString(R.string.currency)+" "+model.getPrice());
        holder.binding.priceTv.setPaintFlags(holder.binding.priceTv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        try {
            holder.binding.detailLay.removeAllViews();
            JSONArray array = new JSONArray(model.details);
            for (int i = 0; i < array.length(); i++) {
                String detail = array.getString(i);
                View view = LayoutInflater.from(context).inflate(R.layout.item_subscription_detail,null);
                TextView textView = view.findViewById(R.id.title);
                textView.setText(detail);
                holder.binding.detailLay.addView(view);
            }
        }catch (Exception e){

        }
    }

    @Override
    public int getItemCount() {
        return wallet_modelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemSubscriptionBinding binding;

        public ViewHolder(@NonNull ItemSubscriptionBinding itemView) {
            super(itemView.getRoot());

            binding = itemView;
        }

        public void bind(final int postion, final SubscriptionModel item, final AdapterClickListener listener) {

            binding.chooseBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v,postion,item);
                }
            });

        }
    }
}
