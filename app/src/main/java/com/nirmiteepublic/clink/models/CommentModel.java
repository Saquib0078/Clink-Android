package com.nirmiteepublic.clink.models;

import androidx.annotation.Nullable;

public class CommentModel {
    private String commentID, username, profileDP, number, comment, reply, replies, time;


    public CommentModel(String commentID, String username, String profileDP, String number, String comment, String replies, String time) {
        this.commentID = commentID;
        this.username = username;
        this.profileDP = profileDP;
        this.number = number;
        this.comment = comment;
        this.replies = replies;
        this.time = time;
    }

    public CommentModel(String commentID, String username, String profileDP, String number, String comment, String replies, String time, String reply) {
        this.commentID = commentID;
        this.username = username;
        this.profileDP = profileDP;
        this.number = number;
        this.comment = comment;
        this.replies = replies;
        this.time = time;
        this.reply = reply;
    }

    public String getCommentID() {
        return commentID;
    }

    public void setCommentID(String commentID) {
        this.commentID = commentID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfileDP() {
        return profileDP;
    }

    public void setProfileDP(String profileDP) {
        this.profileDP = profileDP;
    }

    public String getNumber() {
        return number;
    }

    public void setNum(String number) {
        this.number = number;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getReplies() {
        return replies;
    }

    public void setReplies(String replies) {
        this.replies = replies;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return false;
    }
}
