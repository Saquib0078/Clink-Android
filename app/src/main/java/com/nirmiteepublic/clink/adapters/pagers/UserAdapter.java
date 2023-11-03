package com.nirmiteepublic.clink.adapters.pagers;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nirmiteepublic.clink.databinding.ItemNetworkBinding;
import com.nirmiteepublic.clink.models.UserItem;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<UserItem> userList;

    public UserAdapter(List<UserItem> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new UserViewHolder(ItemNetworkBinding.inflate(((Activity) parent.getContext()).getLayoutInflater(), parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserItem user = userList.get(position);
        Glide.with(holder.itemView.getContext())
                .load(user.getUserImage())
                .into(holder.binding.userImage);

        holder.binding.userName.setText(user.getUserName());
        holder.binding.suggestion.setText(user.getText());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        ItemNetworkBinding binding;

        public UserViewHolder(@NonNull ItemNetworkBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

