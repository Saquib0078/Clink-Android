package com.nirmiteepublic.clink.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.adapters.viewholders.ItemProgressViewHolder;
import com.nirmiteepublic.clink.adapters.viewholders.ItemUserViewHolder;
import com.nirmiteepublic.clink.databinding.ItemProgressBinding;
import com.nirmiteepublic.clink.databinding.ItemUserBinding;
import com.nirmiteepublic.clink.functions.utils.UserUtils;
import com.nirmiteepublic.clink.models.UserModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UsersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<UserModel> userModelList;
    List<UserModel> filteredUserModels;
    Context context;
    boolean matched;
    private final String searchString = "";
    private int lastPosition = -1;


    public UsersAdapter(List<UserModel> userModelList) {
        this.userModelList = userModelList;

    }
    public void setFilteredList(List<UserModel>filteredList){
        this.userModelList=filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();


        if (userModelList.get(viewType).getUsername() == null) {

            return new ItemProgressViewHolder(ItemProgressBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }

        return new ItemUserViewHolder(ItemUserBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), parent.getContext(), userModelList.get(viewType));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        UserModel model = userModelList.get(position);

        if (holder instanceof ItemProgressViewHolder) {
            ((ItemProgressViewHolder) holder).bind();
            return;
        }

        ((ItemUserViewHolder) holder).bind(position, model);

        setAnimation(holder.itemView,position);
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return searchString.isEmpty() ? userModelList.size() : filteredUserModels.size();
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_right);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}

