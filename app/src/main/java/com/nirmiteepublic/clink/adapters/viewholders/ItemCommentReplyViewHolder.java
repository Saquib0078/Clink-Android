package com.nirmiteepublic.clink.adapters.viewholders;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.databinding.ItemCommentReplyBinding;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;
import com.nirmiteepublic.clink.functions.utils.UserUtils;
import com.nirmiteepublic.clink.functions.utils.Utils;
import com.nirmiteepublic.clink.models.CommentModel;

public class ItemCommentReplyViewHolder extends RecyclerView.ViewHolder {

    private final Context context;
    private final CommentModel commentModel;
    private final ItemCommentReplyBinding binding;

    public ItemCommentReplyViewHolder(ItemCommentReplyBinding binding, Context context, CommentModel commentModel) {
        super(binding.getRoot());
        this.binding = binding;
        this.context = context;
        this.commentModel = commentModel;
    }

    @SuppressLint("SetTextI18n")
    public void bind() {
        String dp = RetrofitClient.PROFILE_IMAGE+commentModel.getProfileDP();

        Glide.with(context)
                .load(dp)
                .fitCenter()
                .error(R.drawable.default_image) // Error image if loading fails
                .placeholder(R.drawable.default_image) // Placeholder image while loading
                .into(binding.userImage);
        binding.username.setText(commentModel.getUsername());
        binding.time.setText(Utils.getTimeAgo(commentModel.getTime()));
        binding.comments.setText(commentModel.getReply());

    }
}
