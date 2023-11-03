package com.nirmiteepublic.clink.retrofit.utility;

public class UpdateData {
    private final String url, version;

    public UpdateData(String url, String version) {
        this.url = url;
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public String getVersion() {
        return version;
    }
}
