package com.nirmiteepublic.clink.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.databinding.ItemUserRequestBinding;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;
import com.nirmiteepublic.clink.functions.utils.UserUtils;
import com.nirmiteepublic.clink.models.UserItem;
import com.nirmiteepublic.clink.ui.activity.pages.ViewRequestedUsers;
import com.pegalite.popups.PegaProgressDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRequestAdapter extends RecyclerView.Adapter<UserRequestAdapter.ViewHolder> {



    Context context;
    List<UserItem> userItems;



    public UserRequestAdapter(List<UserItem> userItems) {
        this.userItems = userItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHolder(ItemUserRequestBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserItem userItem = userItems.get(position);

        holder.binding.userName.setText(userItem.getFName());
           Glide.with(context)
                   .load(RetrofitClient.PROFILE_IMAGE+userItem.getDp())
                   .placeholder(R.drawable.default_image) // Set placeholder image resource
                   .into(holder.binding.userImage);


        if (UserUtils.isSuperAdmin()) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Options");
                    String[] options = {"Ban User"};
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                acceptUser(userItem.getId(), 5);
                                acceptUser(userItem.getId(), 5, "Banned");
                                holder.binding.requesttype.setText("Banned");
                                holder.binding.requesttype.setTextColor(Color.RED);
                                Toast.makeText(context, "User Removed From Network", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

                    builder.create().show();

                    return false;
                }
            });
        }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ViewRequestedUsers.class);
//                Toast.makeText(context, ""+userItem.getNum(), Toast.LENGTH_SHORT).show();
                intent.putExtra("id",userItem.getNum());
                context.startActivity(intent);

            }

        });




        holder.binding.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptUser(userItem.getId(), 1, "Accepted");
                holder.binding.requesttype.setText("Accepted");
                holder.binding.requesttype.setTextColor(Color.GREEN);


            }
        });
        holder.binding.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RejectUser(userItem.getId(), 4, "Rejected");


            }
        });

        if ("Rejected".equals(userItem.getStatus())) {
            holder.binding.requesttype.setTextColor(Color.RED);
            holder.binding.requesttype.setText(userItem.getStatus());

        } else if ("Accepted".equals(userItem.getStatus())) {
            holder.binding.requesttype.setTextColor(Color.GREEN);
            holder.binding.requesttype.setText(userItem.getStatus());

        } else {
            holder.binding.requesttype.setTextColor(Color.GRAY);
        }


        if (userItem.getRole() == 1 || "Rejected".equals(userItem.getStatus()) || "Accepted".equals(userItem.getStatus())) { // Assuming role 1 means the user is already accepted
            holder.binding.accept.setVisibility(View.GONE);
            holder.binding.reject.setVisibility(View.GONE);

        } else {
            holder.binding.accept.setVisibility(View.VISIBLE);
            holder.binding.reject.setVisibility(View.VISIBLE);
        }
//        Glide.with(context)
//                .load(userItem.getImage())
//                .into(holder.binding.userImage);


    }

    @Override
    public int getItemCount() {
        return userItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemUserRequestBinding binding;

        public ViewHolder(@NonNull ItemUserRequestBinding binding) {

            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private void acceptUser(String userId, int role, String status) {
        Map<String, Object> roleMap = new HashMap<>();
        roleMap.put("role", role);
        roleMap.put("status", status);
        RetrofitClient.getInstance(context).getApiInterfaces().updateUserRole(userId, roleMap).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {

                    // Role updated successfully
                    Toast.makeText(context, "Request Accepted", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle unsuccessful response
                    Toast.makeText(context, "Failed to Accept", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Network error: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void RejectUser(String userId, int role, String status) {
        Map<String, Object> roleMap = new HashMap<>();
        roleMap.put("role", role);
        roleMap.put("status", status);
        RetrofitClient.getInstance(context).getApiInterfaces().updateUserRole(userId, roleMap).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {

                    // Role updated successfully
                    Toast.makeText(context, "Request Rejected", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle unsuccessful response
                    Toast.makeText(context, "Failed to Reject", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Network error: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }


    private void acceptUser(String userId, int role) {
        Map<String, Object> roleMap = new HashMap<>();
        roleMap.put("role", role);
        RetrofitClient.getInstance(context).getApiInterfaces().updateUserRole(userId, roleMap).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Request Accepted", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle unsuccessful response
                    Toast.makeText(context, "Failed to Accept", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Network error: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
}
