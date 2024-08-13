package com.nirmiteepublic.clink.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.databinding.ItemUserBinding;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;
import com.nirmiteepublic.clink.models.CompleteduserModel;

import java.util.List;

public class CompletedUserAdapter extends RecyclerView.Adapter<CompletedUserAdapter.ViewHolder> {

    private final List<CompleteduserModel> userList;
      Context context;

    public CompletedUserAdapter( List<CompleteduserModel> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context= parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemUserBinding binding = ItemUserBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CompleteduserModel user = userList.get(position);
          holder.binding.userName.setText(user.getfName()+" "+user.getlName());
        Glide.with(context)
                .load( RetrofitClient.PROFILE_IMAGE+user.getDp())
                .placeholder(R.drawable.default_image)
                .centerCrop()
                .into(holder.binding.userImage);

        holder.binding.online.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemUserBinding binding;

        public ViewHolder(ItemUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }
}
