package com.nirmiteepublic.clink.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.adapters.viewholders.ItemTaskViewHolder;
import com.nirmiteepublic.clink.databinding.ItemTaskBinding;
import com.nirmiteepublic.clink.functions.utils.UserUtils;
import com.nirmiteepublic.clink.models.TaskModel;
import com.nirmiteepublic.clink.ui.activity.pages.TaskDescryptionActivity;

import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<TaskModel> taskModelList;
    Context context;
    private int lastPosition = -1;


    public TasksAdapter(List<TaskModel> taskModelList) {
        this.taskModelList = taskModelList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ItemTaskViewHolder(ItemTaskBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), parent.getContext(), taskModelList.get(viewType),this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ItemTaskViewHolder) holder).bind();
        TaskModel taskModel = taskModelList.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserUtils.setTaskId(taskModel.getId());
                Intent intent = new Intent(context, TaskDescryptionActivity.class);

                intent.putExtra("imageID", taskModel.getImageID());
                intent.putExtra("taskID", taskModel.getId());
                Toast.makeText(context, ""+taskModel.getId(), Toast.LENGTH_SHORT).show();
                context.startActivity(intent);
            }
        });
        setAnimation(holder.itemView,position);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return taskModelList.size();
    }

    public void removeItem(int position) {
        if (position >= 0 && position < taskModelList.size()) {
            taskModelList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, taskModelList.size());
        }
    }
    private void setAnimation(View viewToAnimate,int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_right);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

}
