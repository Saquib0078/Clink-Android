package com.visticsolution.posterbanao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.visticsolution.posterbanao.classes.Functions;
import com.visticsolution.posterbanao.databinding.ItemUserListBinding;
import com.visticsolution.posterbanao.interfaces.AdapterClickListener;
import com.visticsolution.posterbanao.model.UserModel;

import java.util.List;


public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    private final List<UserModel> list;
    private final AdapterClickListener listener;
    Context context;

    public UsersAdapter(Context context, List<UserModel> list, AdapterClickListener listener) {
        this.list = list;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemUserListBinding binding = ItemUserListBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.setUsers(list.get(position));
        holder.binding.dateBtn.setText(Functions.getFormatedDate(list.get(position).getCreated_at()));
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

        ItemUserListBinding binding;

        ViewHolder(@NonNull ItemUserListBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

    }

}