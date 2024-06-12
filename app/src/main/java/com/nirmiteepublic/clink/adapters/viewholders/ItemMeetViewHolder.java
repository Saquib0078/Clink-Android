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
import com.nirmiteepublic.clink.databinding.ItemMeetBinding;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;
import com.nirmiteepublic.clink.functions.utils.UserUtils;
import com.nirmiteepublic.clink.functions.utils.Utils;
import com.nirmiteepublic.clink.models.MeetModel;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemMeetViewHolder extends RecyclerView.ViewHolder {

    private final Context context;
    private final MeetModel meetModel;
    private final ItemMeetBinding binding;

    public ItemMeetViewHolder(ItemMeetBinding binding, Context context, MeetModel meetModel) {
        super(binding.getRoot());
        this.binding = binding;
        this.context = context;
        this.meetModel = meetModel;
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
                   showDeleteConfirmationDialog();
                   return false;
               }
           });
       }
        if(meetModel.isLive()==false){
        binding.live.setVisibility(View.GONE);
        }else {
            binding.live.setVisibility(View.VISIBLE);
        }

    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Item");
        builder.setMessage("Are you sure you want to delete this item?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RetrofitClient.getInstance(context).getApiInterfaces().DeleteMeet(meetModel.getId()).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(context, "Failed To Delete", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }



}