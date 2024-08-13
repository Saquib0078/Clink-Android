package com.nirmiteepublic.clink.adapters.viewholders;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.adapters.CommentsAdapter;
import com.nirmiteepublic.clink.databinding.BottomSheetCommentThreeDotsBinding;
import com.nirmiteepublic.clink.databinding.ItemCommentBinding;
import com.nirmiteepublic.clink.functions.listeners.CommentDeleteCallbackListener;
import com.nirmiteepublic.clink.functions.listeners.ReplyCallbackListener;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;
import com.nirmiteepublic.clink.functions.retrofit.res.PegaCallback;
import com.nirmiteepublic.clink.functions.retrofit.res.PegaResponseManager;
import com.nirmiteepublic.clink.functions.utils.UserUtils;
import com.nirmiteepublic.clink.functions.utils.Utils;
import com.nirmiteepublic.clink.functions.viewmanagers.PegaAppCompatActivity;
import com.nirmiteepublic.clink.models.CommentModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ItemCommentViewHolder extends RecyclerView.ViewHolder {

    private final Context context;
    private final CommentModel commentModel;
    private final String broadcastID;
    private final ItemCommentBinding binding;
    private final List<CommentModel> commentModelList = new ArrayList<>();
    private final int position;
    private final ReplyCallbackListener replyCallbackListener;
    private final CommentDeleteCallbackListener commentDeleteCallbackListener;
    private CommentsAdapter adapter;
    private boolean isLoading;

    public ItemCommentViewHolder(ItemCommentBinding binding, Context context, CommentModel commentModel, String broadcastID, int position, ReplyCallbackListener replyCallbackListener, CommentDeleteCallbackListener commentDeleteCallbackListener) {
        super(binding.getRoot());
        this.binding = binding;
        this.context = context;
        this.commentModel = commentModel;
        this.broadcastID = broadcastID;
        this.position = position;
        this.replyCallbackListener = replyCallbackListener;
        this.commentDeleteCallbackListener = commentDeleteCallbackListener;
    }

    @SuppressLint("SetTextI18n")
    public void bind() {
        String dp = RetrofitClient.PROFILE_IMAGE+commentModel.getProfileDP();


        binding.username.setText(commentModel.getUsername());
        binding.time.setText(Utils.getTimeAgo(commentModel.getTime()));
        binding.comments.setText(commentModel.getComment());
        Glide.with(context)
                .load(dp)
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .into(binding.userImage);
        if (commentModel.getReplies() != null) {
            binding.viewReplies.setVisibility(View.VISIBLE);
            binding.viewReplyText.setText("View " + commentModel.getReplies() + " Replies");
            binding.viewReplies.setOnClickListener(v -> loadReplies(commentModel.getCommentID()));
            Glide.with(context)
                    .load(dp)
                    .placeholder(R.drawable.default_image)
                    .into(binding.userImage);
        }

        if (UserUtils.getUserNumber().equals(commentModel.getNumber()) || UserUtils.isAdmin()) {
            binding.threeDots.setVisibility(View.VISIBLE);
            binding.threeDots.setOnClickListener(v -> {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
                BottomSheetCommentThreeDotsBinding threeDotsBinding = BottomSheetCommentThreeDotsBinding.inflate(((PegaAppCompatActivity) context).getLayoutInflater());
                threeDotsBinding.getRoot().setBackgroundTintMode(PorterDuff.Mode.CLEAR);
                threeDotsBinding.getRoot().setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT));
                threeDotsBinding.getRoot().setBackgroundColor(Color.TRANSPARENT);
                bottomSheetDialog.setContentView(threeDotsBinding.getRoot());
                bottomSheetDialog.show();

                threeDotsBinding.deletePost.setOnClickListener(v1 -> {
                    bottomSheetDialog.cancel();
                    deleteComment();
                });

            });
        }

        binding.reply.setOnClickListener(v -> replyCallbackListener.onReply(position, commentModel.getUsername(), commentModel.getCommentID()));

        adapter = new CommentsAdapter(commentModelList, broadcastID, null, commentDeleteCallbackListener);
        binding.recyclerView.setAdapter(adapter);
    }

    private void deleteComment() {
        RetrofitClient.getInstance(context).getApiInterfaces().deleteCommentBroadcast(broadcastID, commentModel.getCommentID()).enqueue(new PegaResponseManager(new PegaCallback((Activity) context) {
            @Override
            public void onSuccess(@Nullable JSONObject data) {
                commentDeleteCallbackListener.onDelete(position);
            }
        }));
    }

    @SuppressLint("SetTextI18n")
    private void loadReplies(String commentID) {
        if (isLoading) {
            return;
        }
        isLoading = true;
        binding.viewReplyText.setText("Loading...");
        int size = commentModelList.size();
        RetrofitClient.getInstance(context).getApiInterfaces().getBroadcastCommentReplies(broadcastID, commentID, size).enqueue(new PegaResponseManager(new PegaCallback((Activity) context, false) {
            @Override
            public void onSuccess(@Nullable JSONObject... data) {
                if (data != null && data.length != 0) {
                    for (JSONObject object : data) {
                        commentModelList.add(new Gson().fromJson(object.toString(), CommentModel.class));
                    }
                    isLoading = false;
                    adapter.notifyItemRangeInserted(size, commentModelList.size());
                    int moreComments = Integer.parseInt(commentModel.getReplies()) - commentModelList.size();
                    if (moreComments > 0) {
                        binding.viewReplyText.setText("View " + moreComments + " Replies");
                    } else {
                        binding.viewReplies.setVisibility(View.GONE);
                    }
                } else {
                    binding.viewReplies.setVisibility(View.GONE);
                }
            }
        }));
    }

    public void addReply(CommentModel model) {
        commentModelList.add(model);
        adapter.notifyItemInserted(commentModelList.size() - 1);
    }

}
