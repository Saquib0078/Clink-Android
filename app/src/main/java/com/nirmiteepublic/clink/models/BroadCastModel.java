package com.nirmiteepublic.clink.models;

public class BroadCastModel {

    String username,description,imageurl,userimage;
    int likes;

    public BroadCastModel(String username, String description, String imageurl, String userimage, int likes) {
        this.username = username;
        this.description = description;
        this.imageurl = imageurl;
        this.userimage = userimage;
        this.likes = likes;
    }

    public BroadCastModel() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getUserimage() {
        return userimage;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
}
