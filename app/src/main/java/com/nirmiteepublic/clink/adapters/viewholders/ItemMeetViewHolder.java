package com.nirmiteepublic.clink.adapters.viewholders;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
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
        this.adapter=meetAdapter;
    }

    public void bind() {

        Glide.with(itemView)
                .load(Utils.loadmeetImage(meetModel.getImageID()))
                .fitCenter().placeholder(ContextCompat.getDrawable(context, R.drawable.round_image_placeholder))
                .into(binding.taskImage);
        binding.taskName.setText(meetModel.getMeetName());
        binding.date.setText(meetModel.getDate());
        binding.taskDescription.setText(meetModel.getMeetDescription());
        binding.time.setText(meetModel.getTime());

       if(UserUtils.isSuperAdmin()){
           itemView.setOnLongClickListener(new View.OnLongClickListener() {
               @Override
               public boolean onLongClick(View v) {
                   showOptionsDialog();
                   return true;
               }
           });
       }
        if(!meetModel.isLive()){
        binding.live.setVisibility(View.GONE);
        }else {
            binding.live.setVisibility(View.VISIBLE);
        }

    }

    private void showOptionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Select Action");

        // Array of options to display in the dialog
        String[] options = {"Delete Meeting", "Do Meeting Live","Close Live Meeting"};

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        // Delete option clicked
                        showDeleteConfirmationDialog();
                        break;
                    case 1:
                        // Update option clicked
                        updateLiveStatus(meetModel.getId(), true);
                        break;
                    case 2:
                        updateLiveStatus(meetModel.getId(), false);
                        break;
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showDeleteConfirmationDialog() {


                RetrofitClient.getInstance(context).getApiInterfaces().DeleteMeet(meetModel.getId()).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            int position = getAdapterPosition();
                            adapter.removeItem(position);
                            Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(context, "Failed To Delete", Toast.LENGTH_SHORT).show();
                    }
                });
            }




    public void updateLiveStatus(String meetId, boolean liveStatus) {
        Map<String, Boolean> liveMap = new HashMap<>();
        liveMap.put("live", liveStatus);

        // Convert liveMap to RequestBody

        RetrofitClient.getInstance(context).getApiInterfaces().updateLive(meetId, liveMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    adapter.notifyItemChanged(getAdapterPosition());
                    Toast.makeText(context, "Live status updated successfully", Toast.LENGTH_SHORT).show();
                    // Handle successful response if needed
                } else {
                    Toast.makeText(context, "Failed to update live status", Toast.LENGTH_SHORT).show();
                    // Handle unsuccessful response if needed
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Network error, please try again", Toast.LENGTH_SHORT).show();
                // Handle failure/error if needed
            }
        });
    }
}