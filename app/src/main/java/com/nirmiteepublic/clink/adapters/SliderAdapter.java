package com.nirmiteepublic.clink.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.functions.utils.Utils;
import com.nirmiteepublic.clink.ui.activity.pages.broadcast.FullImageActivity;
import com.nirmiteepublic.clink.ui.activity.pages.broadcast.VideoPlayerActivity;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterViewHolder> {

    private final List<String> mSliderItems;
    Context context;

    public SliderAdapter(List<String> mSliderItems, String type, List<Uri> mSliderItemsUri, Context mContext, View.OnClickListener doubleTapListener) {
        this.mSliderItems = mSliderItems;
        this.type = type;
        this.mSliderItemsUri = mSliderItemsUri;
        this.mContext = mContext;
        this.doubleTapListener = doubleTapListener;
    }

    private final String type;
    private final List<Uri> mSliderItemsUri;

    Context mContext;
    View.OnClickListener doubleTapListener;

    // Updated Constructor

    @Override
    public SliderAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        context=parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_multiple_image_view, null);
        return new SliderAdapterViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterViewHolder viewHolder, final int position) {
      
        viewHolder.mediaContainer.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            private final GestureDetector gestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    doubleTapListener.onClick(viewHolder.mediaContainer);
                    return super.onDoubleTap(e);
                }
            });

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

//        String imageId = mSliderItems.get(position);
//        String mediaUrl = Utils.loadBroadcastMedia(imageId, type);
        String mediaUrl = null;

        if (mSliderItems != null && !mSliderItems.isEmpty()) {
            String imageId = mSliderItems.get(position);
            mediaUrl = Utils.loadBroadcastMedia(imageId, type);
        } else if (mSliderItemsUri != null && !mSliderItemsUri.isEmpty()) {
            mediaUrl = mSliderItemsUri.get(position).toString();
        }

        if (mediaUrl == null) {
            // Handle the case where no valid media URL is available
            viewHolder.imageViewBackground.setImageResource(com.visticsolution.posterbanao.R.drawable.placeholder);
            viewHolder.playerView.setVisibility(View.GONE);
            viewHolder.imageViewBackground.setVisibility(View.VISIBLE);
            return;
        }
        if (type.equals("mp4")) {
            loadVideo(viewHolder, mediaUrl);
        } else {
            viewHolder.playerView.setVisibility(View.GONE);
            viewHolder.imageViewBackground.setVisibility(View.VISIBLE);
            Glide.with(viewHolder.itemView)
                    .load(mediaUrl)
                    .fitCenter()
                    .into(viewHolder.imageViewBackground);

            final String url=mediaUrl;
            viewHolder.imageViewBackground.setOnClickListener(v -> {
                Intent intent = new Intent(mContext, FullImageActivity.class);
                intent.putExtra("IMAGE_URL", url);
                mContext.startActivity(intent);
            });
        }
    }

    private void loadVideo(SliderAdapterViewHolder viewHolder, String sliderItem) {
        ExoPlayer player;
        viewHolder.playerView.setVisibility(View.VISIBLE);
        viewHolder.imageViewBackground.setVisibility(View.GONE);

        viewHolder.progressBar.setVisibility(View.VISIBLE);

        player = new SimpleExoPlayer.Builder(mContext).build();

        MediaItem mediaItem = MediaItem.fromUri(sliderItem);
        player.setMediaItem(mediaItem);

        viewHolder.playerView.setPlayer(player);

        player.prepare();
        player.setPlayWhenReady(true);
        player.setVolume(0f);

        viewHolder.play.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, VideoPlayerActivity.class);
            intent.putExtra("VIDEO_URL", sliderItem);
            mContext.startActivity(intent);
        });

        viewHolder.mediaContainer.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, VideoPlayerActivity.class);
            intent.putExtra("VIDEO_URL", sliderItem);
            mContext.startActivity(intent);
        });

        player.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                Player.Listener.super.onPlaybackStateChanged(playbackState);

                if (playbackState == PlaybackState.STATE_PLAYING) {
                    viewHolder.play.setVisibility(View.GONE);
                }
            }

            @Override
            public void onRenderedFirstFrame() {
                float videoAspectRatio = player.getVideoFormat().width / (float) player.getVideoFormat().height;
                boolean isLandscape = videoAspectRatio > 1.0;

                ViewGroup.LayoutParams layoutParams = viewHolder.playerView.getLayoutParams();
                if (isLandscape) {
                    layoutParams.height = (int) mContext.getResources().getDimension(com.visticsolution.posterbanao.R.dimen._190sdp);
                } else {
                    layoutParams.height = (int) mContext.getResources().getDimension(com.visticsolution.posterbanao.R.dimen._440sdp);
                }
                viewHolder.playerView.setLayoutParams(layoutParams);

                viewHolder.progressBar.setVisibility(View.GONE);
            }
        });

        viewHolder.soundToggleButton.setOnClickListener(v -> {
            if (player.getVolume() > 0) {
                player.setVolume(0f);
                viewHolder.soundToggleButton.setImageResource(R.drawable.volumeoff);
            } else {
                player.setVolume(1f);
                viewHolder.soundToggleButton.setImageResource(R.drawable.volumeon);
            }
        });
    }

    @Override
    public int getCount() {
        if (mSliderItems != null) {
            return mSliderItems.size();
        } else if (mSliderItemsUri != null) {
            return mSliderItemsUri.size();
        }
        return 0;    }

    static class SliderAdapterViewHolder extends SliderViewAdapter.ViewHolder {
        private final PlayerView playerView;

        View itemView;
        ImageView imageViewBackground;
        ImageView play;
        ImageView soundToggleButton;
        ProgressBar progressBar;
        RelativeLayout mediaContainer;

        public SliderAdapterViewHolder(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.slider_image_view);
            playerView = itemView.findViewById(R.id.postVideo);
            progressBar = itemView.findViewById(R.id.progressBar);
            soundToggleButton = itemView.findViewById(R.id.soundToggleButton);
            mediaContainer = itemView.findViewById(R.id.mediaContainer);
            play = itemView.findViewById(R.id.play);
            this.itemView = itemView;


        }
    }
}