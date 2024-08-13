package com.nirmiteepublic.clink.functions.helpers;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.util.Patterns;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;

public class UrlPreviewHelper {
    private static final HashMap<String, UrlPreviewData> previewCache = new HashMap<>();

    public static void getUrlPreview(Context context, String url, TextView titleView, TextView descriptionView, ImageView imageView, ProgressBar glideProgress) {
        // Check if the preview is already in the cache
        if (previewCache.containsKey(url)) {
            UrlPreviewData cachedData = previewCache.get(url);
            updateViews(context, cachedData, titleView, descriptionView, imageView, glideProgress);
            return;
        }

        // If not in cache, proceed with the AsyncTask
        new AsyncTask<String, Void, UrlPreviewData>() {
            @Override
            protected UrlPreviewData doInBackground(String... params) {
                String url = params[0];
                UrlPreviewData previewData = new UrlPreviewData();

                if (url == null || url.isEmpty()) {
                    return previewData;
                }

                // Add protocol if missing
                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    url = "https://" + url;
                }

                if (!Patterns.WEB_URL.matcher(url).matches()) {
                    return previewData;
                }

                try {
                    Document doc = Jsoup.connect(url).get();

                    Elements ogTitle = doc.select("meta[property=og:title]");
                    Elements ogDescription = doc.select("meta[property=og:description]");
                    Elements ogImage = doc.select("meta[property=og:image]");

                    previewData.title = ogTitle.attr("content");
                    previewData.description = ogDescription.attr("content");
                    previewData.imageUrl = ogImage.attr("content");

                    if (previewData.title.isEmpty()) {
                        previewData.title = doc.title();
                    }

                    if (previewData.description.isEmpty()) {
                        Elements metaDescription = doc.select("meta[name=description]");
                        previewData.description = metaDescription.attr("content");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                return previewData;
            }

            @Override
            protected void onPostExecute(UrlPreviewData result) {
                if (result != null) {
                    // Add the result to the cache
                    previewCache.put(url, result);
                    updateViews(context, result, titleView, descriptionView, imageView, glideProgress);
                }
            }
        }.execute(url);
    }

    private static void updateViews(Context context, UrlPreviewData data, TextView titleView, TextView descriptionView, ImageView imageView, ProgressBar glideProgress) {
        titleView.setText(data.title);
        descriptionView.setText(data.description);
        if (!data.imageUrl.isEmpty()) {
            Glide.with(context)
                    .load(data.imageUrl)
                    .fitCenter()
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            glideProgress.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            glideProgress.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(imageView);
        } else {
            glideProgress.setVisibility(View.GONE);
        }
    }

    public static void clearCache() {
        previewCache.clear();
    }

    public static class UrlPreviewData {
        public String title = "";
        public String description = "";
        public String imageUrl = "";
    }
}