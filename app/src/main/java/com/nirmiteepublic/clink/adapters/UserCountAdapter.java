package com.nirmiteepublic.clink.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.models.ResultItem;
import com.nirmiteepublic.clink.models.UserCount;

import java.util.List;

public class UserCountAdapter extends RecyclerView.Adapter<UserCountAdapter.UserCountViewHolder> {

    private Context mContext;
    private final List<ResultItem> mUserCounts;

    public UserCountAdapter( List<ResultItem> userCounts) {
        mUserCounts = userCounts;
    }

    @NonNull
    @Override
    public UserCountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext= parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_user_count, parent, false);
        return new UserCountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserCountViewHolder holder, int position) {
        ResultItem userCount = mUserCounts.get(position);

        holder.textDistrict.setText(userCount.getDistrict());
        holder.textTehsil.setText(userCount.getTehsil());
        holder.textVillage.setText(userCount.getVillage());
        holder.textCount.setText(String.valueOf(userCount.getCount()));
    }

    @Override
    public int getItemCount() {
        return mUserCounts.size();
    }

    public static class UserCountViewHolder extends RecyclerView.ViewHolder {
        TextView textDistrict, textTehsil, textVillage, textCount;

        public UserCountViewHolder(@NonNull View itemView) {
            super(itemView);
            textDistrict = itemView.findViewById(R.id.text_district);
            textTehsil = itemView.findViewById(R.id.text_tehsil);
            textVillage = itemView.findViewById(R.id.text_village);
            textCount = itemView.findViewById(R.id.text_count);
        }
    }
}
