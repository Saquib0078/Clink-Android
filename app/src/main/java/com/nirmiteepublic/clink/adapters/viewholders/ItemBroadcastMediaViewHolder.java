package com.nirmiteepublic.clink.adapters.viewholders;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.MediaController;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
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

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;

import timber.log.Timber;

public class ItemBroadcastMediaViewHolder extends RecyclerView.ViewHolder {

    private final Context context;
    private final BroadcastModel model;
    public ItemBroadcastMediaBinding binding;
    private DownloadManager downloadManager;
    private long downloadId;
    String videoFilePath;
    public ItemBroadcastMediaViewHolder(ItemBroadcastMediaBinding binding, Context context, BroadcastModel model) {
        super(binding.getRoot());
        this.binding = binding;
        this.context = context;
        this.model = model;
    }

    @SuppressLint("ClickableViewAccessibility")
    public void bind() {
        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);


        if (model.isImage()) {
            loadImage();
        } else {
            loadVideo();
        }
        binding.username.setText(model.getUsername());
        binding.description.setText(model.getDescription());
        binding.likeCount.setText(model.getLikes());
        binding.commentCount.setText(model.getComments());
        binding.date.setText(Utils.getTimeAgo(model.getTime()));
        String dp=RetrofitClient.PROFILE_IMAGE+model.getProfileDP();

        Glide.with(context)
                .load(dp)
                .fitCenter()
                .placeholder(R.drawable.default_image) // Set placeholder image resource
                .into(binding.userImage);
        if (model.isLiked()) setLiked();

        binding.like.setOnClickListener(v -> likePost(false));
        binding.comment.setOnClickListener(v -> ((PegaAppCompatActivity) context).openActivityWithRightAnim(new Intent(context, CommentBroadcastActivity.class).putExtra("broadcastID", model.getBroadcastID())));
        binding.download.setOnClickListener(v -> {
        });
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

        binding.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(model.isImage()){
                    shareImageBroadcast();
                }else {
                    shareVideoBroadcast();

                }
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

                threeDotsBinding.deletePost.setOnClickListener(v13 -> {
                    bottomSheetDialog.cancel();
                    new AlertDialog.Builder(context).setTitle("Are You Sure?").setMessage("Once You delete the Broadcast. There's no way to retrieve it.").setPositiveButton("Delete", (dialog, which) -> {
                        dialog.dismiss();
                        deleteBroadcast();
                    }).setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss()).show();
                });
                threeDotsBinding.share.setOnClickListener(v1 -> {
                    bottomSheetDialog.cancel();
                    shareImageBroadcast();
                });

            });
        }
    }

    private void shareVideoBroadcast() {
        // Assuming model.getBroadcastID() returns the file path of the video
         videoFilePath = Utils.loadBroadcastMedia(model.getBroadcastID(), model.getType());
        Toast.makeText(context, ""+videoFilePath, Toast.LENGTH_SHORT).show();
        Intent shareIntent = new Intent(Intent.ACTION_SEND);

        // Set the MIME type of the content to "text/plain" since it's a URL
        shareIntent.setType("text/plain");

        // Add the video URL to the Intent as text
        shareIntent.putExtra(Intent.EXTRA_TEXT, videoFilePath);

        // Set optional extras like subject
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Sharing Video URL");

        // Start the activity with the Intent, and choose a sharing method
        context.startActivity(Intent.createChooser(shareIntent, "Share video URL using"));
    }

    private void deleteBroadcast() {
        RetrofitClient.getInstance(context).getApiInterfaces().deleteBroadcast(model.getBroadcastID()).enqueue(new PegaResponseManager(new PegaCallback((Activity) context) {
            @Override
            public void onSuccess(@Nullable JSONObject data) {
                ((PegaAppCompatActivity) context).onRestartRequest();
            }
        }));
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
        Glide.with(itemView).load(RetrofitClient.USER_BASE_URL+"getUsermedia/"+model.getBroadcastID()+"."+model.getType()).fitCenter().placeholder(ContextCompat.getDrawable(context, R.drawable.round_image_placeholder)).into(binding.postImage);
    }

//    private void loadVideo() {
//        binding.postVideo.setVisibility(View.VISIBLE);
//        binding.postImage.setVisibility(View.GONE);
//
//        binding.postVideo.setVideoURI(Uri.parse(Utils.loadBroadcastMedia(model.getBroadcastID(), model.getType())));
//
//        MediaController mediaController=new MediaController(context);
//        binding.postVideo.setMediaController(mediaController);
//        mediaController.setAnchorView(binding.postVideo);
//        binding.postVideo.setOnPreparedListener(mp -> {
//            mp.setLooping(true);
//            binding.postVideo.start();
//
//        });
//        binding.postVideo.setOnErrorListener(new MediaPlayer.OnErrorListener() {
//            @Override
//            public boolean onError(MediaPlayer mp, int what, int extra) {
//                // Handle error and possibly retry connection
//                Timber.tag("MediaError").e("Error occurred: " + what + ", " + extra);
//                return true;
//            }
//        });
//
//    }


    private void loadVideo() {
        binding.postVideo.setVisibility(View.VISIBLE);
        binding.postImage.setVisibility(View.GONE);

        binding.postVideo.setVideoURI(Uri.parse(Utils.loadBroadcastMedia(model.getBroadcastID(), model.getType())));
        binding.postVideo.setOnPreparedListener(mp -> {
            mp.setLooping(true);
            binding.postVideo.start();
            binding.playButton.setVisibility(View.GONE);
            binding.pauseButton.setVisibility(View.VISIBLE);
        });

        binding.postVideo.setOnErrorListener((mp, what, extra) -> {
            Timber.tag("MediaError").e("Error occurred: " + what + ", " + extra);
            return true;
        });

        binding.playButton.setOnClickListener(v -> {
            if (!binding.postVideo.isPlaying()) {
                binding.postVideo.start();
                binding.playButton.setVisibility(View.GONE);
                binding.pauseButton.setVisibility(View.VISIBLE);
            }
        });

        binding.pauseButton.setOnClickListener(v -> {
            if (binding.postVideo.isPlaying()) {
                binding.postVideo.pause();
                binding.pauseButton.setVisibility(View.GONE);
                binding.playButton.setVisibility(View.VISIBLE);
            }
        });

//        binding.soundButton.setOnClickListener(v -> {
//            if (binding.postVideo.isPlaying()) {
//                if (getCurrentVolume() == 1.0f) {
//                    setVolume(0.0f, 0.0f);
//                    binding.soundButton.setImageResource(R.drawable.ic_sound_off);
//                } else {
//                    setVolume(1.0f, 1.0f);
//                    binding.soundButton.setImageResource(R.drawable.ic_sound_on);
//                }
//            }
//        });
    }

//    private float getCurrentVolume() {
//        try {
//            Class<?> videoViewClass = Class.forName("android.widget.VideoView");
//            Field mMediaPlayerField = videoViewClass.getDeclaredField("mMediaPlayer");
//            mMediaPlayerField.setAccessible(true);
//            MediaPlayer mediaPlayer = (MediaPlayer) mMediaPlayerField.get(binding.postVideo);
//            if (mediaPlayer != null) {
//                // You need to maintain the volume state because there is no direct getter method for volume
//                return mediaPlayer.getVolume();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return 1.0f; // Default to full volume
//    }

    private void setVolume(float leftVolume, float rightVolume) {
        try {
            Class<?> videoViewClass = Class.forName("android.widget.VideoView");
            Field mMediaPlayerField = videoViewClass.getDeclaredField("mMediaPlayer");
            mMediaPlayerField.setAccessible(true);
            MediaPlayer mediaPlayer = (MediaPlayer) mMediaPlayerField.get(binding.postVideo);
            if (mediaPlayer != null) {
                mediaPlayer.setVolume(leftVolume, rightVolume);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
//        shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this image!");

        context.startActivity(Intent.createChooser(shareIntent, "Share image using"));
    }

    private void doubleTapLike() {
        YoYo.with(Techniques.BounceIn).repeat(0).duration(500).onStart(animator -> binding.loved.setVisibility(View.VISIBLE)).onEnd(animator -> {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_out);
            binding.loved.startAnimation(animation);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    binding.loved.setVisibility(View.GONE);

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }).playOn(binding.loved);
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

        binding.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadFile();
            }
        });
        binding.likeCount.setText(model.getLikes());

        YoYo.with(Techniques.BounceIn).repeat(0).duration(500).playOn(binding.like);

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

    private void downloadFile() {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(videoFilePath));
        request.setTitle("Video Download");
        request.setDescription("Downloading video file");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "video.mp4");

        downloadId = downloadManager.enqueue(request);
    }

    private final BroadcastReceiver downloadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long receivedDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (receivedDownloadId == downloadId) {
                // Download completed for the requested downloadId
                // Add the downloaded file to the device's gallery here
                addToGallery(videoFilePath);
            }
        }
    };

    private void addToGallery(String filePath) {
        MediaScannerConnection.scanFile(
                context,
                new String[]{filePath},  // Array of file paths to be scanned
                null,  // MIME types of files to be scanned
                new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        // Scan completed
                        if (uri != null) {
                            // File was successfully added to the media store
                            // You can use the returned Uri if needed
                            Log.d("AddToGallery", "File scanned and added to media store: " + path);
                        } else {
                            // Failed to add file to the media store
                            Log.e("AddToGallery", "Failed to scan file: " + path);
                        }
                    }
                }
        );
    }

}
