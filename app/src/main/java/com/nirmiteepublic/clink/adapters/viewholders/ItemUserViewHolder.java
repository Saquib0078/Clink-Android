package com.nirmiteepublic.clink.adapters.viewholders;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.databinding.ItemUserBinding;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;
import com.nirmiteepublic.clink.functions.utils.UserUtils;
import com.nirmiteepublic.clink.models.UserModel;
import com.nirmiteepublic.clink.ui.activity.pages.GetNetworkDetail;

public class ItemUserViewHolder extends RecyclerView.ViewHolder {

    private final Context context;
    private final UserModel userModel;
    ItemUserBinding binding;

    private DatabaseReference userOnlineRef;


    public ItemUserViewHolder(ItemUserBinding binding, Context context, UserModel userModel) {
        super(binding.getRoot());
        this.binding = binding;
        this.context = context;
        this.userModel = userModel;
    }

    public void bind(int position, UserModel model) {
//        binding.online.setVisibility(View.VISIBLE);


//            binding.view.setVisibility(View.VISIBLE);
        if (model.isOnline())
            binding.online.setVisibility(View.VISIBLE);

        binding.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                    Toast.makeText(context, "Button Clicked"+position+1, Toast.LENGTH_SHORT).show();
//
//                    getUsers(position,UserUtils.getUserId());
                Intent intent = new Intent(context, GetNetworkDetail.class);
                intent.putExtra("userId", model.get_id());
                context.startActivity(intent);
//                    Toast.makeText(context, userModel.get_id(), Toast.LENGTH_SHORT).show();

            }
        });
        if (UserUtils.isAdmin() || UserUtils.isSuperAdmin()) {
            binding.view.setVisibility(View.VISIBLE);
        } else {
            binding.view.setVisibility(View.GONE);

        }


        binding.userName.setText(model.getUsername());
        String dp = RetrofitClient.PROFILE_IMAGE + model.getDp();
        Glide.with(context)
                .load(dp)
                .fitCenter()
                .placeholder(R.drawable.default_image) // Set placeholder image resource
                .error(ContextCompat.getDrawable(context, R.drawable.default_image)) // Error image if loading fails
                .into(binding.userImage);
    }


}