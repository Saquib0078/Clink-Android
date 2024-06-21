package com.nirmiteepublic.clink.adapters.viewholders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.nirmiteepublic.clink.databinding.ItemBroadcastHeaderBinding;
import com.nirmiteepublic.clink.functions.utils.UserUtils;
import com.nirmiteepublic.clink.functions.viewmanagers.PegaAppCompatActivity;
import com.nirmiteepublic.clink.ui.activity.NotificationActivity;
import com.nirmiteepublic.clink.ui.activity.pages.ProfileActivity;
import com.nirmiteepublic.clink.ui.activity.pages.RequestUserActivity;
import com.nirmiteepublic.clink.ui.activity.pages.SelectMediaActivity;
import com.nirmiteepublic.clink.ui.fragments.BroadCastFragment;

public class ItemBroadcastHeaderViewHolder extends RecyclerView.ViewHolder {

    private final Context context;
    private final ItemBroadcastHeaderBinding binding;


    public ItemBroadcastHeaderViewHolder(ItemBroadcastHeaderBinding binding, Context context) {
        super(binding.getRoot());
        this.binding = binding;
        this.context = context;
    }


    @SuppressLint("SetTextI18n")
    public void bind() {
        binding.name.setText("Hey, " + UserUtils.getUserFirstName());
        binding.greeting.setText("Good " + UserUtils.getGreeting());
        SharedPreferences prefs = context.getSharedPreferences("NotificationPrefs", Context.MODE_PRIVATE);
        boolean notificationStatus = prefs.getBoolean("notificationStatus", false);
        if (notificationStatus) {
            prefs.edit().putBoolean("notificationStatus", false).apply();
            binding.notificationdot.setVisibility(View.VISIBLE);
        } else {
            binding.notificationdot.setVisibility(View.GONE);
        }
        if (UserUtils.isAdmin()) {
            binding.support.setVisibility(View.GONE);
            binding.broadcast.setVisibility(View.VISIBLE);
            binding.broadcast.setOnClickListener(v -> ((PegaAppCompatActivity) context).openResultActivityWithRightAnim(SelectMediaActivity.class, BroadCastFragment.REQUEST_CODE_BROADCAST_PUBLISHED));

        }

        if (UserUtils.isSuperAdmin()) {
            binding.notification.setVisibility(View.GONE);
            binding.request.setVisibility(View.VISIBLE);
            binding.request.setOnClickListener(v -> ((PegaAppCompatActivity) context).openActivityWithRightAnim(RequestUserActivity.class));

        }



        binding.bell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, NotificationActivity.class));
            }
        });


        binding.user.setOnClickListener(v -> ((PegaAppCompatActivity) context).openActivityWithRightAnim(ProfileActivity.class));

    }


}