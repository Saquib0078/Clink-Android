package com.visticsolution.posterbanao.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.visticsolution.posterbanao.classes.Functions;
import com.visticsolution.posterbanao.PreviewActivity;
import com.visticsolution.posterbanao.databinding.ItemUserPosterBinding;
import com.visticsolution.posterbanao.model.UserPostModel;

import java.util.ArrayList;
import java.util.List;

public class UserPostsAdapter extends RecyclerView.Adapter<UserPostsAdapter.ViewHolder> {

    Context context;
    List<UserPostModel> list = new ArrayList<>();

    public UserPostsAdapter(Context context, List<UserPostModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        ItemUserPosterBinding binding = ItemUserPosterBinding.inflate(LayoutInflater.from(context),viewGroup,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int pos) {
        int position = pos;
        holder.binding.setPosts(list.get(pos));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, PreviewActivity.class).putExtra("url", Functions.getItemBaseUrl(list.get(position).post_url)));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemUserPosterBinding binding;

        public ViewHolder(@NonNull ItemUserPosterBinding itemView) {
            super(itemView.getRoot());

            binding = itemView;
        }
    }

}
