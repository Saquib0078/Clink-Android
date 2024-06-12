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
import com.nirmiteepublic.clink.databinding.ItemTaskBinding;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;
import com.nirmiteepublic.clink.functions.utils.UserUtils;
import com.nirmiteepublic.clink.functions.utils.Utils;
import com.nirmiteepublic.clink.models.TaskModel;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemTaskViewHolder extends RecyclerView.ViewHolder {

    private final Context context;
    private final TaskModel taskModel;
    ItemTaskBinding binding;

    public ItemTaskViewHolder(ItemTaskBinding binding, Context context, TaskModel taskModel) {
        super(binding.getRoot());
        this.binding = binding;
        this.context = context;
        this.taskModel = taskModel;
    }

    public void bind() {
        Glide.with(itemView)
                .load(Utils.loadTaskImage(taskModel.getImageID()))
                .fitCenter().placeholder(ContextCompat.getDrawable(context, R.drawable.round_image_placeholder))
                .into(binding.taskImage);

        if(UserUtils.isSuperAdmin()){
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showDeleteConfirmationDialog();
                    return false;
                }
            });
        }

        binding.taskName.setText(taskModel.getTaskName());
        binding.date.setText(taskModel.getDate());
        binding.taskDescription.setText(taskModel.getTaskDescription());
        binding.time.setText(taskModel.getTime());
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Item");
        builder.setMessage("Are you sure you want to delete this item?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              RetrofitClient.getInstance(context).getApiInterfaces().DeleteTask(taskModel.getId()).enqueue(new Callback<ResponseBody>() {
                  @Override
                  public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                      Toast.makeText(context, "Task Deleted SuccessFully", Toast.LENGTH_SHORT).show();
                  }

                  @Override
                  public void onFailure(Call<ResponseBody> call, Throwable t) {
                      Toast.makeText(context, "Failed To Delete task", Toast.LENGTH_SHORT).show();
                  }
              });
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

}