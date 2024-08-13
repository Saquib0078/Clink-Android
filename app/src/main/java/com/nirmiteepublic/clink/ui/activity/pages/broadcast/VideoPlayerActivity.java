package com.nirmiteepublic.clink.ui.activity.pages.broadcast;

import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.nirmiteepublic.clink.R;

public class VideoPlayerActivity extends AppCompatActivity implements SensorEventListener {

    private SimpleExoPlayer player;
    private PlayerView playerView;
    private ImageView fullScreenButton;
    private ProgressBar progressBar;
    private boolean isFullScreen = false;

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;

    private float[] accelerometerReading = new float[3];
    private float[] magnetometerReading = new float[3];
    private float[] rotationMatrix = new float[9];
    private float[] orientationAngles = new float[3];

    private boolean orientationLocked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        playerView = findViewById(R.id.player_view);
        fullScreenButton = playerView.findViewById(R.id.exo_fullscreen_icon);
        progressBar = findViewById(R.id.progress_bar);

        progressBar.setVisibility(View.VISIBLE);

        String videoUrl = getIntent().getStringExtra("VIDEO_URL");

        initializePlayer(videoUrl);

        fullScreenButton.setOnClickListener(v -> toggleFullScreen());

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        if (savedInstanceState != null) {
            isFullScreen = savedInstanceState.getBoolean("isFullScreen", false);
            orientationLocked = savedInstanceState.getBoolean("orientationLocked", false);
            updateFullScreenButtonIcon();
        }
    }

    private void initializePlayer(String videoUrl) {
        player = new SimpleExoPlayer.Builder(this).build();
        playerView.setPlayer(player);

        MediaItem mediaItem = MediaItem.fromUri(Uri.parse(videoUrl));
        player.setMediaItem(mediaItem);

        player.prepare();
        player.setPlayWhenReady(true);
        player.setVolume(1f);

        player.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                switch (playbackState) {
                    case Player.STATE_BUFFERING:
                        progressBar.setVisibility(View.VISIBLE);
                        break;
                    case Player.STATE_READY:
                    case Player.STATE_ENDED:
                        progressBar.setVisibility(View.GONE);
                        break;
                    case Player.STATE_IDLE:
                        progressBar.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onPlayerError(PlaybackException error) {
                // Handle player errors
                Toast.makeText(VideoPlayerActivity.this, "Error playing video: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void toggleFullScreen() {
        isFullScreen = !isFullScreen;
        if (isFullScreen) {
            enterFullScreen();
        } else {
            exitFullScreen();
        }
        updateFullScreenButtonIcon();
    }

    private void updateFullScreenButtonIcon() {
        fullScreenButton.setImageResource(isFullScreen ? R.drawable.ic_fullscreen_shrink : R.drawable.ic_fullscreen_expand);
    }

    private void enterFullScreen() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        );
    }

    private void exitFullScreen() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.setPlayWhenReady(false);
        }
        unregisterSensors();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player != null) {
            player.setPlayWhenReady(true);
        }
        registerSensors();
    }

    @Override
    protected void onStop() {
        super.onStop();
        releasePlayer();
        unregisterSensors();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isFullScreen", isFullScreen);
        outState.putBoolean("orientationLocked", orientationLocked);
    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    private void registerSensors() {
        if (!orientationLocked) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
            sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
        }
    }

    private void unregisterSensors() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, accelerometerReading, 0, accelerometerReading.length);
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, magnetometerReading, 0, magnetometerReading.length);
        }

        updateOrientationAngles();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do nothing for now
    }

    private void updateOrientationAngles() {
        SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerReading, magnetometerReading);
        SensorManager.getOrientation(rotationMatrix, orientationAngles);

        float pitch = (float) Math.toDegrees(orientationAngles[1]);
        float roll = (float) Math.toDegrees(orientationAngles[2]);

        if (!orientationLocked) {
            if (Math.abs(pitch) < 30 && Math.abs(roll) < 30) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            } else if (Math.abs(pitch) > 60) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else if (Math.abs(roll) > 60) {
                if (roll < 0) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                }
            }
        }
    }

    // Add a method to toggle orientation lock
    public void toggleOrientationLock(View view) {
        orientationLocked = !orientationLocked;
        if (orientationLocked) {
            unregisterSensors();
        } else {
            registerSensors();
        }
        Toast.makeText(this, orientationLocked ? "Orientation locked" : "Orientation unlocked", Toast.LENGTH_SHORT).show();
    }
}