package com.nirmiteepublic.clink.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.databinding.ItemFilteruserBinding;
import com.nirmiteepublic.clink.databinding.ItemNotificationBinding;
import com.nirmiteepublic.clink.databinding.ItemUserBinding;
import com.nirmiteepublic.clink.functions.utils.UserUtils;
import com.nirmiteepublic.clink.models.NotificationItem;
import com.nirmiteepublic.clink.models.NotificationModel;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    List<NotificationItem>models;
    Context context;

    public NotificationAdapter(List<NotificationItem> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context= parent.getContext();
        return new NotificationAdapter.ViewHolder(ItemNotificationBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {
         NotificationItem notificationModel=models.get(position);
         holder.binding.userName.setText(notificationModel.getTitle());
         holder.binding.suggestion.setText(notificationModel.getBody());
         holder.binding.time.setText(notificationModel.getRegTime());
        Glide.with(context)
                .load(UserUtils.getUserDp())
                .placeholder(R.drawable.default_image) // Replace R.drawable.default_image with your default image resource
                .error(R.drawable.default_image)
                .into(holder.binding.userImage);
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemNotificationBinding binding;
        public ViewHolder(@NonNull ItemNotificationBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
