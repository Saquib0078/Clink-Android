package com.nirmiteepublic.clink.models;

import java.util.List;

public class BroadcastModel {
    private String type;
    private String broadcastID;
    private List<String> ImageUrls;
    private String num;
    private String profileDP;
    private String username;
    private String description;
    private String time;
    private String pinned;

    public String getBroadcastUrl() {
        return broadcastUrl;
    }

    public void setBroadcastUrl(String broadcastUrl) {
        this.broadcastUrl = broadcastUrl;
    }

    private String broadcastUrl;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    private String _id;
    private int likes, comments;
    private boolean isLiked;


    public BroadcastModel(String type) {
        this.type = type;
    }

    public BroadcastModel() {
    }

    public String getPinned() {
        return pinned;
    }

    public List<String> getBroadcastImages() {
        return ImageUrls;
    }

    public void setBroadcastImages(List<String> broadcastImages) {
        this.ImageUrls = broadcastImages;
    }

    public void setPinned(String pinned) {
        this.pinned = pinned;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getType() {
        return type;
    }

    public boolean isImage() {
        return type.equals("png") || type.equals("jpeg") || type.equals("jpg");
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBroadcastID() {
        return broadcastID;
    }

    public void setBroadcastID(String broadcastID) {
        this.broadcastID = broadcastID;
    }

    public String getProfileDP() {
        return profileDP;
    }

    public void setProfileDP(String profileDP) {
        this.profileDP = profileDP;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLikes() {
        return String.valueOf(likes);
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void addLike() {
        likes++;
    }

    public void removeLike() {
        likes--;
    }

    public String getComments() {
        return String.valueOf(comments);
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }
}
