package com.nirmiteepublic.clink.functions.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;

import androidx.core.content.FileProvider;

import com.amplitude.BuildConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageDownloaderTask extends AsyncTask<String, Void, Uri> {

    private final Context context;
    private final ImageDownloadListener listener;

    public ImageDownloaderTask(Context context, ImageDownloadListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected Uri doInBackground(String... urls) {
        String imageUrl = urls[0];
        Uri imageUri = null;
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();

            // Check if the connection was successful
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                // Check if bitmap decoding was successful
                if (bitmap != null) {
                    // Save the downloaded image to local storage
                    File imageFile = saveBitmapToFile(bitmap);

                    // Get a Uri for the saved image file
                    if (imageFile != null) {
                        imageUri = FileProvider.getUriForFile(context,
                                BuildConfig.APPLICATION_ID + ".provider", imageFile);
                    }
                } else {
                    // Bitmap decoding failed
                    // Handle the situation, log an error, or notify the listener
                }
            } else {
                // Connection failed
                // Handle the situation, log an error, or notify the listener
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception, log an error, or notify the listener
        }
        return imageUri;
    }

    @Override
    protected void onPostExecute(Uri imageUri) {
        if (listener != null) {
            listener.onImageDownloaded(imageUri);
        }
    }

    private File saveBitmapToFile(Bitmap bitmap) {
        // Save the bitmap to a file in the cache directory
        File cacheDir = context.getExternalCacheDir();
        if (cacheDir != null) {
            File imageFile = new File(cacheDir, "downloaded_image.jpg");
            try {
                FileOutputStream fos = new FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
                return imageFile;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public interface ImageDownloadListener {
        void onImageDownloaded(Uri imageUri);
    }
}
