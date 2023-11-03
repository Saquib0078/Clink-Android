package com.nirmiteepublic.clink.models;

public class TaskMeetModel {
    private String title;
    private String descryption;
    private String date;
    private String starttime;
    private String imageurl;

    public TaskMeetModel(String title, String descryption, String date, String starttime, String imageurl) {
        this.title = title;
        this.descryption = descryption;
        this.date = date;
        this.starttime = starttime;
        this.imageurl = imageurl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescryption() {
        return descryption;
    }

    public void setDescryption(String descryption) {
        this.descryption = descryption;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
}
