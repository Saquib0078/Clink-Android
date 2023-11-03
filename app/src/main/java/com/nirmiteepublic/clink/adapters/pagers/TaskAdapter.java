package com.nirmiteepublic.clink.adapters.pagers;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nirmiteepublic.clink.databinding.ItemTaskLayoutBinding;
import com.nirmiteepublic.clink.databinding.ItemTaskmeetCardBinding;
import com.nirmiteepublic.clink.models.TaskModel;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewholder> {

    List<TaskModel> model;
Context context;
    public TaskAdapter(List<TaskModel> model) {
        this.model = model;
    }

    @NonNull
    @Override
    public TaskAdapter.TaskViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context= parent.getContext();
        return new TaskAdapter.TaskViewholder(ItemTaskLayoutBinding.inflate(((Activity) parent.getContext()).getLayoutInflater(), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.TaskViewholder holder, int position) {
       TaskModel taskModel=model.get(position);
       holder.binding.meet.setText(taskModel.getTitle());
       holder.binding.taskDescription.setText(taskModel.getDescryption());
       holder.binding.startTime.setText(taskModel.getTime());
       holder.binding.date.setText(taskModel.getDate());

        Glide.with(context)
                .load(taskModel.getImage())
                .into(holder.binding.taskImage);
    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public class TaskViewholder extends RecyclerView.ViewHolder {
        ItemTaskLayoutBinding binding;
        public TaskViewholder(@NonNull ItemTaskLayoutBinding binding) {
            super(binding.getRoot());
          this.binding=binding;
        }
    }
}
