package com.nirmiteepublic.clink.models;

public class UserItem {
    private String userName, text;
    private String userImage;

    public UserItem(String userName, String text, String userImage) {
        this.userName = userName;
        this.userImage = userImage;
        this.text = text;
    }

    public String getUserName() {
        return userName;
    }

    public String getText() {
        return text;
    }

    public String getUserImage() {
        return userImage;
    }

}

