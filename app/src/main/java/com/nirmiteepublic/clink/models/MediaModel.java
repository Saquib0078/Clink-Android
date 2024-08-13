package com.nirmiteepublic.clink.models;

import android.net.Uri;

public class MediaModel {
    private final Uri uri;
    private final long dateAdded, duration;
    private final int mediaType;
    private final String mimeType, filePath;

    public MediaModel(Uri uri, long dateAdded, long duration, int mediaType, String mimeType, String filePath) {
        this.uri = uri;
        this.dateAdded = dateAdded;
        this.duration = duration;
        this.mediaType = mediaType;
        this.mimeType = mimeType;
        this.filePath = filePath;
    }

    public long getDuration() {
        return duration;
    }

    public Uri getUri() {
        return uri;
    }

    public long getDateAdded() {
        return dateAdded;
    }

    public int getMediaType() {
        return mediaType;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getFilePath() {
        return filePath;
    }
}
