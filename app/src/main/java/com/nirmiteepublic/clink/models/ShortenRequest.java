package com.nirmiteepublic.clink.models;

public class ShortenRequest {
    private String originalUrl;

    public ShortenRequest(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }
}
