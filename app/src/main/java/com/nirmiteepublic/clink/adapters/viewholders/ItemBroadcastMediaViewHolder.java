package com.nirmiteepublic.clink.adapters.viewholders;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.databinding.BottomSheetBroadcastThreeDotsBinding;
import com.nirmiteepublic.clink.databinding.ItemBroadcastMediaBinding;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;
import com.nirmiteepublic.clink.functions.retrofit.res.PegaCallback;
import com.nirmiteepublic.clink.functions.retrofit.res.PegaResponseManager;
import com.nirmiteepublic.clink.functions.utils.UserUtils;
import com.nirmiteepublic.clink.functions.utils.Utils;
import com.nirmiteepublic.clink.functions.viewmanagers.PegaAppCompatActivity;
import com.nirmiteepublic.clink.models.BroadcastModel;
import com.nirmiteepublic.clink.ui.activity.pages.broadcast.CommentBroadcastActivity;
import com.nirmiteepublic.clink.ui.activity.pages.broadcast.UpdateBroadcast;
import com.nirmiteepublic.clink.ui.activity.pages.broadcast.VideoPlayerActivity;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemBroadcastMediaViewHolder extends RecyclerView.ViewHolder {

    private final Context context;
    private ExoPlayer player;
    private final BroadcastModel model;
    public ItemBroadcastMediaBinding binding;
    private String videoFilePath;
    private long downloadId;
    private DownloadManager downloadManager;
    private BroadcastReceiver downloadReceiver;

    public ItemBroadcastMediaViewHolder(ItemBroadcastMediaBinding binding, Context context, BroadcastModel model) {
        super(binding.getRoot());
        this.binding = binding;
        this.context = context;
        this.model = model;
        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        downloadReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long receivedDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L);
                if (receivedDownloadId == downloadId) {
                    addToGallery(videoFilePath);
                }
            }
        };
    }

    @SuppressLint("ClickableViewAccessibility")
    public void bind() {

        if (model.isImage()) {
            binding.soundToggleButton.setVisibility(View.GONE);
            binding.play.setVisibility(View.GONE);
            loadImage();
        } else {
            loadVideo();
        }

        binding.download.setOnClickListener(v -> {
            if (model.isImage()) {
                Toast.makeText(context, "Image is Downloading", Toast.LENGTH_SHORT).show();
                downloadImage();
                return;
            }
            Toast.makeText(context, "Video is Downloading", Toast.LENGTH_SHORT).show();
            downloadVideo();
        });

        binding.username.setText(model.getUsername());
        binding.description.setText(model.getDescription());
        binding.likeCount.setText(model.getLikes());
        binding.commentCount.setText(model.getComments());
        binding.date.setText(Utils.getTimeAgo(model.getTime()));
        String dp = RetrofitClient.PROFILE_IMAGE + model.getProfileDP();
        Glide.with(context)
                .load(dp)
                .fitCenter()
                .placeholder(R.drawable.default_image)
                .into(binding.userImage);
        if (model.isLiked()) setLiked();

        binding.like.setOnClickListener(v -> likePost(false));
        binding.comment.setOnClickListener(v -> ((PegaAppCompatActivity) context).openActivityWithRightAnim(new Intent(context, CommentBroadcastActivity.class).putExtra("broadcastID", model.getBroadcastID())));

        binding.postImage.setOnTouchListener(new View.OnTouchListener() {
            private final GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    likePost(true);
                    doubleTapLike();
                    return super.onDoubleTap(e);
                }
            });

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

        binding.share.setOnClickListener(v -> {
            if (model.isImage()) {
                shareImageBroadcast();
            } else {
                shareVideoBroadcast();
            }
        });


        if (UserUtils.isAdmin()) {
            binding.threeDots.setVisibility(View.VISIBLE);
            binding.threeDots.setOnClickListener(v -> {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
                BottomSheetBroadcastThreeDotsBinding threeDotsBinding = BottomSheetBroadcastThreeDotsBinding.inflate(((PegaAppCompatActivity) context).getLayoutInflater());
                threeDotsBinding.getRoot().setBackgroundTintMode(PorterDuff.Mode.CLEAR);
                threeDotsBinding.getRoot().setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT));
                threeDotsBinding.getRoot().setBackgroundColor(Color.TRANSPARENT);
                bottomSheetDialog.setContentView(threeDotsBinding.getRoot());
                bottomSheetDialog.show();

                if (UserUtils.isSuperAdmin() || UserUtils.getUserNumber().equals(model.getNum())) {
                    threeDotsBinding.deletePost.setVisibility(View.VISIBLE);
                    threeDotsBinding.updatePost.setVisibility(View.VISIBLE);
                }

                if (UserUtils.isAdmin()) {


                    if (model.getPinned() == null) {
                        threeDotsBinding.pin.setVisibility(View.VISIBLE);
                        threeDotsBinding.unpin.setVisibility(View.GONE);
                        threeDotsBinding.pin.setOnClickListener(v12 -> {
                            bottomSheetDialog.cancel();
                            pinBroadcast();
                        });
                    } else {
                        threeDotsBinding.unpin.setVisibility(View.VISIBLE);
                        threeDotsBinding.pin.setVisibility(View.GONE);
                        threeDotsBinding.unpin.setOnClickListener(v12 -> {
                            bottomSheetDialog.cancel();
                            unpinBroadcast();
                        });
                    }
                }

                threeDotsBinding.updatePost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(context, UpdateBroadcast.class);
                        intent.putExtra("desc",model.getDescription());
                        intent.putExtra("type",model.getType());
                        intent.putExtra("broadcastId",model.getBroadcastID());
                        intent.putExtra("isImage",model.isImage());
                        context.startActivity(intent);
                        getBroadcastbyId(model.getBroadcastID());
                    }
                });


                threeDotsBinding.deletePost.setOnClickListener(v13 -> {
                    bottomSheetDialog.cancel();
                    new AlertDialog.Builder(context)
                            .setTitle("Are You Sure?")
                            .setMessage("Once You delete the Broadcast. There's no way to retrieve it.")
                            .setPositiveButton("Delete", (dialog, which) -> {
                                dialog.dismiss();
                                deleteBroadcast();
                            })
                            .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                            .show();
                });
                threeDotsBinding.share.setOnClickListener(v1 -> {
                    bottomSheetDialog.cancel();
                    if (model.isImage()) {
                        shareImageBroadcast();
                    } else {
                        shareVideoBroadcast();
                    }
                });
            });
        }
    }


    private void getBroadcastbyId(String id) {
        RetrofitClient.getInstance(context).getApiInterfaces().getBroadcastById(id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                    try {
                        System.out.println(response.body().string());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    private void deleteBroadcast() {
        RetrofitClient.getInstance(context).getApiInterfaces().deleteBroadcast(model.getBroadcastID()).enqueue(new PegaResponseManager(new PegaCallback((Activity) context) {
            @Override
            public void onSuccess(@Nullable JSONObject data) {
                ((PegaAppCompatActivity) context).onRestartRequest();
            }
        }));
    }

    private void shareVideoBroadcast() {
        videoFilePath = Utils.loadBroadcastMedia(model.getBroadcastID(), model.getType());
        String videoUrl = "<a href=\"" + videoFilePath + "\">Click to watch video</a>";

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, videoUrl);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Sharing Video URL");
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(Intent.createChooser(shareIntent, "Share video URL using"));
    }

    private void unpinBroadcast() {
        model.setPinned(null);
        RetrofitClient.getInstance(context).getApiInterfaces().unpinBroadcast(model.getBroadcastID()).enqueue(new PegaResponseManager(new PegaCallback((Activity) context) {
            @Override
            public void onSuccess(@Nullable JSONObject data) {
                ((PegaAppCompatActivity) context).onRestartRequest();
            }
        }));
    }

    private void pinBroadcast() {
        model.setPinned("");
        RetrofitClient.getInstance(context).getApiInterfaces().pinBroadcast(model.getBroadcastID()).enqueue(new PegaResponseManager(new PegaCallback((Activity) context) {
            @Override
            public void onSuccess(@Nullable JSONObject data) {
                ((PegaAppCompatActivity) context).onRestartRequest();
            }
        }));
    }

    private void loadImage() {
        binding.postImage.setVisibility(View.VISIBLE);
        binding.postVideo.setVisibility(View.GONE);
        Glide.with(itemView)
                .load(Utils.loadBroadcastMedia(model.getBroadcastID(), model.getType()))
                .fitCenter()
                .placeholder(ContextCompat.getDrawable(context, R.drawable.round_image_placeholder))
                .into(binding.postImage);
    }

    private void loadVideo() {
        binding.postVideo.setVisibility(View.VISIBLE);
        binding.postImage.setVisibility(View.GONE);

        // Show the progress bar initially
        binding.progressBar.setVisibility(View.VISIBLE);

        // Initialize ExoPlayer
        player = new SimpleExoPlayer.Builder(context).build();

        // Prepare the media source
        MediaItem mediaItem = MediaItem.fromUri(Uri.parse(Utils.loadBroadcastMedia(model.getBroadcastID(), model.getType())));
        player.setMediaItem(mediaItem);

        // Attach player to the view
        binding.postVideo.setPlayer(player);

        // Prepare and start playing the video
        player.prepare();
        player.setPlayWhenReady(true);
        player.setVolume(0f);

        // Hide play button as video will start automatically
        binding.play.setVisibility(View.VISIBLE);
        binding.play.setOnClickListener(v -> {
            Intent intent = new Intent(context, VideoPlayerActivity.class);
            intent.putExtra("VIDEO_URL", Utils.loadBroadcastMedia(model.getBroadcastID(), model.getType()));
            context.startActivity(intent);
        });
        binding.mediaContainer.setOnClickListener(v -> {
            Intent intent = new Intent(context, VideoPlayerActivity.class);
            intent.putExtra("VIDEO_URL", Utils.loadBroadcastMedia(model.getBroadcastID(), model.getType()));
            context.startActivity(intent);
        });
        // Adjust video layout based on the aspect ratio
        player.addListener(new Player.Listener() {
            @Override
            public void onRenderedFirstFrame() {
                float videoAspectRatio = player.getVideoFormat().width / (float) player.getVideoFormat().height;
                boolean isLandscape = videoAspectRatio > 1.0;

                // Adjust height based on orientation
                ViewGroup.LayoutParams layoutParams = binding.postVideo.getLayoutParams();
                if (isLandscape) {
                    // Decrease height for landscape
                    layoutParams.height = (int) context.getResources().getDimension(com.visticsolution.posterbanao.R.dimen._190sdp);
                } else {
                    // Use original height for portrait or square videos
                    layoutParams.height = (int) context.getResources().getDimension(com.visticsolution.posterbanao.R.dimen._440sdp);
                }
                binding.postVideo.setLayoutParams(layoutParams);

                // Hide progress bar when rendering starts
                binding.progressBar.setVisibility(View.GONE);
            }
        });

        // Set up sound toggle button
        binding.soundToggleButton.setOnClickListener(v -> {
            if (player.getVolume() > 0) {
                player.setVolume(0f);
                binding.soundToggleButton.setImageResource(R.drawable.volumeoff);
            } else {
                player.setVolume(1f);
                binding.soundToggleButton.setImageResource(R.drawable.volumeon);
            }
        });
    }

    private void shareImageBroadcast() {
        binding.postImage.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(binding.postImage.getDrawingCache());
        binding.postImage.setDrawingCacheEnabled(false);

        File imagePath = new File(context.getCacheDir(), model.getBroadcastID() + "." + model.getType());
        try {
            FileOutputStream fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Uri imageUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", imagePath);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Sharing Image");

        context.startActivity(Intent.createChooser(shareIntent, "Share image using"));
    }

    private void doubleTapLike() {
        YoYo.with(Techniques.BounceIn)
                .repeat(0)
                .duration(500)
                .onStart(animator -> binding.loved.setVisibility(View.VISIBLE))
                .onEnd(animator -> {
                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_out);
                    binding.loved.startAnimation(animation);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            // No action needed
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            binding.loved.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                            // No action needed
                        }
                    });
                })
                .playOn(binding.loved);
    }

    private void likePost(boolean isDoubleTap) {
        if (model.isLiked()) {
            if (!isDoubleTap) {
                removeLike();
            }
            return;
        }

        model.addLike();
        model.setLiked(true);
        binding.likeCount.setText(model.getLikes());

        YoYo.with(Techniques.BounceIn)
                .repeat(0)
                .duration(500)
                .playOn(binding.like);

        setLiked();

        RetrofitClient.getInstance(context).getApiInterfaces().likeBroadcast(model.getBroadcastID()).enqueue(new PegaResponseManager(new PegaCallback((Activity) context, false) {
        }));
    }

    private void setLiked() {
        binding.like.setColorFilter(ContextCompat.getColor(context, R.color.wave), PorterDuff.Mode.SRC_IN);
        binding.like.setBackground(ContextCompat.getDrawable(context, R.drawable.loved_icon));
    }

    private void removeLike() {
        model.setLiked(false);
        model.removeLike();
        binding.likeCount.setText(model.getLikes());
        binding.like.setColorFilter(ContextCompat.getColor(context, R.color.black), PorterDuff.Mode.SRC_IN);
        binding.like.setBackground(ContextCompat.getDrawable(context, R.drawable.love_icon));

        RetrofitClient.getInstance(context).getApiInterfaces().removeLikeBroadcast(model.getBroadcastID()).enqueue(new PegaResponseManager(new PegaCallback((Activity) context, false) {
        }));
    }

    public void downloadImage() {
        String imageUrl = RetrofitClient.BASE_URL + "user/getUsermedia/" + model.getBroadcastID() + "." + model.getType();
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(imageUrl));
        request.setTitle("Image Download");
        request.setDescription("Downloading image file");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, "image." + model.getType());
        downloadManager.enqueue(request);
    }

    public void downloadVideo() {
        videoFilePath = Utils.loadBroadcastMedia(model.getBroadcastID(), model.getType());
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(videoFilePath));
        request.setTitle("Video Download");
        request.setDescription("Downloading video file");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "video.mp4");
        downloadId = downloadManager.enqueue(request);
        // Register the download receiver
        context.registerReceiver(downloadReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    public void addToGallery(String filePath) {
        context.unregisterReceiver(downloadReceiver);
        MediaScannerConnection.scanFile(
                context,
                new String[]{filePath},
                null,
                (path, uri) -> {
                    if (uri != null) {
                        Log.d("AddToGallery", "File scanned and added to media store: " + path);
                    } else {
                        Log.e("AddToGallery", "Failed to scan file: " + path);
                    }
                }
        );
    }

    private void clearDownloadView() {
        binding.progressBar.setVisibility(View.GONE);
        binding.postVideo.setVisibility(View.GONE);
    }

    private void resetDownloadView() {
        clearDownloadView();
        binding.postVideo.setVisibility(View.VISIBLE);
    }


    private void releasePlayer() {
        if (player != null) {
            context.unregisterReceiver(downloadReceiver);
            player.release();
            player = null;
        }
    }

    public void stopVideo() {
        if (player != null) {
            player.setPlayWhenReady(false);
            player.release();
            player = null;
            binding.postVideo.setPlayer(null);
        }
    }

    public void muteVideo() {
        if (player != null) {
            player.setVolume(0f); // Set volume to 0 for muting
        }
    }

    // Method to unmute the video (if applicable)
    public void unmuteVideo() {
        if (player != null) {
            player.setVolume(1f); // Set volume back to 1 for unmuting
        }
    }

}