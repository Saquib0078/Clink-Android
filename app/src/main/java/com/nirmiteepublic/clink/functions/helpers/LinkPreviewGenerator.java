package com.nirmiteepublic.clink.functions.helpers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkPreviewGenerator {
    private static final Pattern URL_PATTERN = Pattern.compile(
            "((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$",
            Pattern.CASE_INSENSITIVE);

    public static String extractUrl(String text) {
        Matcher matcher = URL_PATTERN.matcher(text);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    public static void generateLinkPreview(String url, LinkPreviewCallback callback) {
        new Thread(() -> {
            try {
                Document doc = Jsoup.connect(url).get();
                String title = doc.title();
                String description = "";
                String imageUrl = "";

                Elements metaTags = doc.getElementsByTag("meta");
                for (org.jsoup.nodes.Element metaTag : metaTags) {
                    String property = metaTag.attr("property");
                    String content = metaTag.attr("content");

                    if ("og:description".equals(property)) {
                        description = content;
                    } else if ("og:image".equals(property)) {
                        imageUrl = content;
                    }
                }

                if (description.isEmpty()) {
                    description = doc.select("meta[name=description]").attr("content");
                }

                callback.onLinkPreviewGenerated(url, title, description, imageUrl);
            } catch (IOException e) {
                e.printStackTrace();
                callback.onError(e);
            }
        }).start();
    }

    public interface LinkPreviewCallback {
        void onLinkPreviewGenerated(String url, String title, String description, String imageUrl);
        void onError(Exception e);
    }
}
