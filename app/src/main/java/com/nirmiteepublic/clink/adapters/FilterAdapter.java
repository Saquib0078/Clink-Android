package com.nirmiteepublic.clink.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.databinding.ItemFilteruserBinding;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;
import com.nirmiteepublic.clink.functions.utils.UserUtils;
import com.nirmiteepublic.clink.models.MergedUsersItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {

    List<MergedUsersItem> dataItems;
    Context context;

    public FilterAdapter(List<MergedUsersItem> dataItems, Context context) {
        this.dataItems = dataItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHolder(ItemFilteruserBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MergedUsersItem model = dataItems.get(position);

        if(model.getLName()!= null){
            holder.binding.userName.setText(model.getFName()+" "+model.getLName());

        }else{
            holder.binding.userName.setText(model.getFName());

        }        holder.binding.suggestion.setText(model.getDist());
        Glide.with(context)
                .load(RetrofitClient.USER_BASE_URL + "getUsermedia/" + model.getDp())
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .into(holder.binding.userImage);

        holder.binding.checkbox.setChecked(model.isSelected());

        holder.binding.checkbox.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            model.setSelected(isChecked);
            Toast.makeText(context, model.getNum(), Toast.LENGTH_SHORT).show();
        });


        if (UserUtils.isSuperAdmin()) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Options");
                    String[] options = {"Super-Admin", "Co-Admin","Make User","Ban User"};
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                acceptUser(model.getId(), 3);


                                Toast.makeText(context, "SuperAdmin Successful", Toast.LENGTH_SHORT).show();
                            } else if (which == 1) {
                                acceptUser(model.getId(), 2);

                                Toast.makeText(context, "Co-Admin Successful", Toast.LENGTH_SHORT).show();

                            }else if (which == 2) {
                                acceptUser(model.getId(), 1);

                                Toast.makeText(context, "Made User Successful", Toast.LENGTH_SHORT).show();

                            }else if (which == 3) {
                                acceptUser(model.getId(), 5);

                                Toast.makeText(context, "User Removed From Network", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

                    builder.create().show();

                    return false;
                }
            });

            if (model.getRole() == 3) { // Assuming role 1 means the user is already accepted
                holder.binding.role.setTextColor(ContextCompat.getColor(context, R.color.green));
                holder.binding.role.setText("Super-Admin");
            } else if (model.getRole() == 2) {
                holder.binding.role.setTextColor(ContextCompat.getColor(context, R.color.cyan_process));
                holder.binding.role.setText("Co-Admin");

            }

        }
    }


    @Override
    public int getItemCount() {
        return dataItems.size();
    }

    public void setData(List<MergedUsersItem> newData) {
        this.dataItems = newData;
        notifyDataSetChanged();
    }



    public List<MergedUsersItem> getSelectedUsers() {
        List<MergedUsersItem> selectedUsers = new ArrayList<>();
        for (MergedUsersItem user : dataItems) {
            if (user.isSelected()) {
                selectedUsers.add(user);
            }
        }
        return selectedUsers;
    }

    public void updateRecyclerView(List<MergedUsersItem> filteredUsers) {
        dataItems.clear();
        dataItems.addAll(filteredUsers);
        notifyDataSetChanged();
    }

    public void clearSelection() {
        for (MergedUsersItem user : dataItems) {
            user.setSelected(false);
        }
        notifyDataSetChanged();
    }

    public void selectAll() {
        for (MergedUsersItem user : dataItems) {
            user.setSelected(true);
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemFilteruserBinding binding;

        public ViewHolder(@NonNull ItemFilteruserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
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
