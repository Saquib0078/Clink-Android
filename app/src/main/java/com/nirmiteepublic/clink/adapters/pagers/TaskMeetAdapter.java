package com.nirmiteepublic.clink.adapters.pagers;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nirmiteepublic.clink.databinding.ItemTaskmeetCardBinding;
import com.nirmiteepublic.clink.models.TaskMeetModel;

import java.util.List;

public class TaskMeetAdapter extends RecyclerView.Adapter<TaskMeetAdapter.TaskMeetViewHolder> {

    Context context;
    private List<TaskMeetModel> taskMeetModels;

    public TaskMeetAdapter(List<TaskMeetModel> taskMeetModels) {
        this.taskMeetModels = taskMeetModels;
    }

    @NonNull
    @Override
    public TaskMeetAdapter.TaskMeetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new TaskMeetAdapter.TaskMeetViewHolder(ItemTaskmeetCardBinding.inflate(((Activity) parent.getContext()).getLayoutInflater(), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TaskMeetAdapter.TaskMeetViewHolder holder, int position) {
        TaskMeetModel model = taskMeetModels.get(position);
        holder.binding.taskDescription.setText(model.getDescryption());
        holder.binding.meetName.setText(model.getTitle());
        holder.binding.date.setText(model.getDate());
        holder.binding.startTime.setText(model.getStarttime());
        Glide.with(context)
                .load(model.getImageurl())
                .into(holder.binding.taskImage);
    }

    @Override
    public int getItemCount() {
        return taskMeetModels.size();
    }

    public class TaskMeetViewHolder extends RecyclerView.ViewHolder {
        ItemTaskmeetCardBinding binding;

        public TaskMeetViewHolder(@NonNull ItemTaskmeetCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
