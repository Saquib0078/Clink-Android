package com.nirmiteepublic.clink.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nirmiteepublic.clink.adapters.viewholders.ItemTaskCommentReplyViewHolder;
import com.nirmiteepublic.clink.adapters.viewholders.ItemTaskCommentViewHolder;
import com.nirmiteepublic.clink.adapters.viewholders.ItemProgressViewHolder;
import com.nirmiteepublic.clink.adapters.viewholders.ItemTaskCommentReplyViewHolder;
import com.nirmiteepublic.clink.adapters.viewholders.ItemTaskCommentViewHolder;
import com.nirmiteepublic.clink.databinding.ItemCommentBinding;
import com.nirmiteepublic.clink.databinding.ItemCommentReplyBinding;
import com.nirmiteepublic.clink.databinding.ItemProgressBinding;
import com.nirmiteepublic.clink.functions.listeners.CommentDeleteCallbackListener;
import com.nirmiteepublic.clink.functions.listeners.ReplyCallbackListener;
import com.nirmiteepublic.clink.models.CommentModel;

import java.util.ArrayList;
import java.util.List;

public class TaskCommentAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<CommentModel> commentModelList;
    private final List<ItemTaskCommentViewHolder> commentViewHolders = new ArrayList<>();
    private final String taskID;
    private final ReplyCallbackListener replyCallbackListener;
    private final CommentDeleteCallbackListener commentDeleteCallbackListener;

    public TaskCommentAdapter(List<CommentModel> commentModelList, String taskID, ReplyCallbackListener replyCallbackListener, CommentDeleteCallbackListener commentDeleteCallbackListener) {
        this.commentModelList = commentModelList;
        this.taskID = taskID;
        this.replyCallbackListener = replyCallbackListener;
        this.commentDeleteCallbackListener = commentDeleteCallbackListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (commentModelList.get(viewType) == null) {
            return new ItemProgressViewHolder(ItemProgressBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else if (commentModelList.get(viewType).getComment() == null) {
            return new ItemTaskCommentReplyViewHolder(ItemCommentReplyBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), parent.getContext(), commentModelList.get(viewType));
        }
        return new ItemTaskCommentViewHolder(ItemCommentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), parent.getContext(), commentModelList.get(viewType), taskID, viewType, replyCallbackListener, commentDeleteCallbackListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemProgressViewHolder) {
            ((ItemProgressViewHolder) holder).bind();
            return;
        } else if (holder instanceof ItemTaskCommentReplyViewHolder) {
            ((ItemTaskCommentReplyViewHolder) holder).bind();
            return;
        }
        commentViewHolders.add((ItemTaskCommentViewHolder) holder);
        ((ItemTaskCommentViewHolder) holder).bind();
    }

    public void addReply(CommentModel model, int position) {
        commentViewHolders.get(position).addReply(model);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return commentModelList.size();
    }

}
