package com.nirmiteepublic.clink.adapters.viewholders;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.adapters.MeetAdapter;
import com.nirmiteepublic.clink.databinding.ItemMeetBinding;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;
import com.nirmiteepublic.clink.functions.utils.UserUtils;
import com.nirmiteepublic.clink.functions.utils.Utils;
import com.nirmiteepublic.clink.models.MeetModel;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemMeetViewHolder extends RecyclerView.ViewHolder {

    private final Context context;
    private final MeetModel meetModel;
    private final ItemMeetBinding binding;
    private final MeetAdapter adapter;

    public ItemMeetViewHolder(ItemMeetBinding binding, Context context, MeetModel meetModel, MeetAdapter meetAdapter) {
        super(binding.getRoot());
        this.binding = binding;
        this.context = context;
        this.meetModel = meetModel;
        this.adapter = meetAdapter;
    }

    public void bind() {
        // Load meet image using Glide
        Glide.with(itemView)
                .load(Utils.loadmeetImage(meetModel.getImageID()))
                .fitCenter()
                .placeholder(ContextCompat.getDrawable(context, R.drawable.clinklogo))
                .error(ContextCompat.getDrawable(context, R.drawable.clinklogo))
                .into(binding.taskImage);

        // Set other meet details
        binding.taskName.setText(meetModel.getMeetName());
        binding.date.setText(meetModel.getDate());
        binding.taskDescription.setText(meetModel.getMeetDescription());
        binding.time.setText(meetModel.getTime());

        // Show/hide live indicator based on meet live status
        if (!meetModel.isLive()) {
            binding.live.setVisibility(View.GONE);
        } else {
            binding.live.setVisibility(View.VISIBLE);
        }

        // Set long click listener for super admins to show options dialog
        if (UserUtils.isSuperAdmin()) {
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showOptionsDialog();
                    return true;
                }
            });
        }
    }

    private void showOptionsDialog() {
        // Inflate custom dialog layout
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_meeting_options, null);

        // Find views in dialog layout
        TextView optionDelete = dialogView.findViewById(R.id.optionDelete);
        TextView optionStartLive = dialogView.findViewById(R.id.optionStartLive);
        TextView optionCloseLive = dialogView.findViewById(R.id.optionCloseLive);
           if(meetModel.isLive()){
               optionCloseLive.setVisibility(View.VISIBLE);
           }else{
               optionStartLive.setVisibility(View.VISIBLE);

           }
        // Build AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();

        // Set click listeners for dialog options
        optionDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showDeleteConfirmationDialog();
            }
        });

        optionStartLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                updateLiveStatus(meetModel.getId(), true);
            }
        });

        optionCloseLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                updateLiveStatus(meetModel.getId(), false);
            }
        });
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to delete this meeting?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteMeeting();
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }

    private void deleteMeeting() {
        RetrofitClient.getInstance(context).getApiInterfaces().DeleteMeet(meetModel.getId()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Remove the item from the adapter
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        adapter.removeItem(position);
                        Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Failed to delete. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Network error. Failed to delete.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateLiveStatus(String meetId, boolean liveStatus) {
        Map<String, Boolean> liveMap = new HashMap<>();
        liveMap.put("live", liveStatus);

        RetrofitClient.getInstance(context).getApiInterfaces().updateLive(meetId, liveMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Update the local model
                    meetModel.setLive(liveStatus);

                    // Update the UI
                    updateLiveStatusUI(liveStatus);

                    Toast.makeText(context, "Live status updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to update live status", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Network error, please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateLiveStatusUI(boolean liveStatus) {
        if (liveStatus) {
            binding.live.setVisibility(View.VISIBLE);
        } else {
            binding.live.setVisibility(View.GONE);
        }
        adapter.notifyItemChanged(getAdapterPosition());
    }
}
