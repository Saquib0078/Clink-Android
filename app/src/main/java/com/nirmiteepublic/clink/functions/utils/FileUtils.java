package com.nirmiteepublic.clink.functions.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;

public class FileUtils {

    // Method to get the MIME type from the URI
    public static String getMimeType(Context context, Uri uri) {
        String mimeType = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = context.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }

    // Method to get the file extension from the URI
    public static String getFileExtension(Context context, Uri uri) {
        String mimeType = getMimeType(context, uri);
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);
    }

    // Method to check if the file is an image
    public static boolean isImageFile(Context context, Uri uri) {
        String mimeType = getMimeType(context, uri);
        return mimeType != null && mimeType.startsWith("image/");
    }

    // Method to check if the file is a video
    public static boolean isVideoFile(Context context, Uri uri) {
        String mimeType = getMimeType(context, uri);
        return mimeType != null && mimeType.startsWith("video/");
    }
}
