package com.nirmiteepublic.clink.ui.activity.pages;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.adapters.CommentsAdapter;
import com.nirmiteepublic.clink.adapters.TaskCommentAdapter;
import com.nirmiteepublic.clink.databinding.ActivityTaskCommentBinding;
import com.nirmiteepublic.clink.functions.listeners.CommentDeleteCallbackListener;
import com.nirmiteepublic.clink.functions.listeners.ReplyCallbackListener;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;
import com.nirmiteepublic.clink.functions.retrofit.res.PegaCallback;
import com.nirmiteepublic.clink.functions.retrofit.res.PegaResponseManager;
import com.nirmiteepublic.clink.functions.utils.UserUtils;
import com.nirmiteepublic.clink.functions.viewmanagers.PegaAppCompatActivity;
import com.nirmiteepublic.clink.models.CommentModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class TaskCommentActivity extends PegaAppCompatActivity {
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().isEmpty()) {
                binding.post.setTextColor(Color.parseColor("#CBCDD3"));
            } else {
                binding.post.setTextColor(ContextCompat.getColor(getActivity(), R.color.royal_blue));
            }
        }
    };
    List<CommentModel> commentModelList = new ArrayList<>();
    boolean isLoading;
    private String taskID;
    private TaskCommentAdapter commentsAdapter;
    private JSONObject commentObject = new JSONObject();
    private ReplyCallbackListener replyCallbackListener;
    private CommentDeleteCallbackListener commentDeleteCallbackListener;

    ActivityTaskCommentBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityTaskCommentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setWindowThemeThird();
        setBackWithRightAnim();

        Glide.with(this)
                .load(UserUtils.getUserDp())
                .placeholder(R.drawable.default_image)
                .into(binding.profileImage);


        binding.back.setOnClickListener(v -> onBackPressed());

        taskID = UserUtils.getTaskId();
        replyCallbackListener = (position, name, commentID) -> {
            binding.replyingToText.setText("Replying to " + name);
            binding.replyingTo.setVisibility(View.VISIBLE);

            binding.comment.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(binding.comment, InputMethodManager.SHOW_IMPLICIT);

            try {
                commentObject.put("position", position);
                commentObject.put("name", name);
                commentObject.put("commentID", commentID);
                commentObject.put("taskID", taskID);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        };

        commentDeleteCallbackListener = (position) -> {
            commentModelList.remove(position);
            commentsAdapter.notifyItemRemoved(position);
            commentsAdapter.notifyItemRangeChanged(position, commentModelList.size());

        };

        commentsAdapter = new TaskCommentAdapter(commentModelList, taskID, replyCallbackListener, commentDeleteCallbackListener);

        binding.recyclerView.setAdapter(commentsAdapter);
        binding.recyclerView.setHasFixedSize(false);
        binding.comment.addTextChangedListener(textWatcher);

        binding.closeReplyTo.setOnClickListener(v -> {
            commentObject = new JSONObject();
            binding.replyingTo.setVisibility(View.GONE);
        });


        binding.post.setOnClickListener(v -> {
            String comment = binding.comment.getText().toString().trim();
            if (comment.isEmpty() || binding.post.getText().toString().equals("Posting...")) {
                return;
            }
            try {
                binding.comment.setText("");
                if (commentObject.has("commentID")) {
                    commentObject.put("reply", comment);
                    addReply(commentObject);
                    return;
                }
                commentObject.put("taskID", taskID);
                commentObject.put("comment", comment);
                addComment(commentObject);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });

        loadComments();

    }

    @SuppressLint("SetTextI18n")
    private void addReply(JSONObject commentData) throws JSONException {
        binding.post.setText("Posting...");
        binding.post.setTextColor(Color.parseColor("#CBCDD3"));

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), commentData.toString());
        RetrofitClient.getInstance(this).getApiInterfaces().replyCommentTask(requestBody).enqueue(new PegaResponseManager(new PegaCallback(this, false) {
            @Override
            public void onResponse() {
                binding.post.setText("Post");
            }

            @Override
            public void onSuccess(@Nullable JSONObject data) {
                if (data != null) {
                    try {
                        if (binding.replyingTo.getVisibility() == View.VISIBLE) {
                            binding.closeReplyTo.performClick();
                        }
                        commentsAdapter.addReply(new CommentModel(null, UserUtils.getUserFirstName() + " " + UserUtils.getUserLastName(), UserUtils.getUserDp(), UserUtils.getUserNumber(), null, null, data.getString("time"), commentData.getString("reply")), commentData.getInt("position"));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }));
    }

    @SuppressLint("SetTextI18n")
    private void addComment(JSONObject commentData) {
        binding.post.setText("Posting...");
        binding.post.setTextColor(Color.parseColor("#CBCDD3"));

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), commentData.toString());
        RetrofitClient.getInstance(this).getApiInterfaces().commentTask(requestBody).enqueue(new PegaResponseManager(new PegaCallback(this, false) {
            @Override
            public void onResponse() {
                binding.post.setText("Post");
            }

            @Override
            public void onSuccess(@Nullable JSONObject data) {
                if (data != null) {
                    try {
                        commentModelList.add(0, new CommentModel(data.getString("commentID"), UserUtils.getUserFirstName() + " " + UserUtils.getUserLastName(), UserUtils.getUserDp(), UserUtils.getUserNumber(), commentData.getString("comment"), null, data.getString("time")));
                        commentsAdapter = new TaskCommentAdapter(commentModelList, taskID, replyCallbackListener, commentDeleteCallbackListener);
                        binding.recyclerView.setAdapter(commentsAdapter);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }));
    }

    private void loadComments() {
        RetrofitClient.getInstance(this).getApiInterfaces().getTaskComments(taskID, 0).enqueue(new PegaResponseManager(new PegaCallback(this, false) {
            @Override
            public void onSuccess(@Nullable JSONObject... data) {
                binding.progressBar.setVisibility(View.GONE);
                if (data != null) {
                    for (JSONObject object : data) {
                        commentModelList.add(new Gson().fromJson(object.toString(), CommentModel.class));
                    }
                    commentsAdapter.notifyItemRangeInserted(0, commentModelList.size());
                    initScrollListener();
                }
            }
        }));
    }

    private void loadMoreComments() {
        int size = commentModelList.size();
        commentModelList.add(null);
        commentsAdapter.notifyItemInserted(size);
        binding.recyclerView.scrollToPosition(size);

        RetrofitClient.getInstance(this).getApiInterfaces().getTaskComments(taskID, size).enqueue(new PegaResponseManager(new PegaCallback(this, false) {
            @Override
            public void onSuccess(@Nullable JSONObject... data) {
                commentModelList.remove(size);
                commentsAdapter.notifyItemRemoved(size);
                if (data != null) {
                    for (JSONObject object : data) {
                        commentModelList.add(new Gson().fromJson(object.toString(), CommentModel.class));
                    }
                    isLoading = false;
                }
            }
        }));

    }

    private void initScrollListener() {
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == commentModelList.size() - 1) {
                        isLoading = true;
                        loadMoreComments();
                    }
                }
            }
        });
    }


}
