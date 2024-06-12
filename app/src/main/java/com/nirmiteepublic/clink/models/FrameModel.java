package com.nirmiteepublic.clink.models;

public class FrameModel {
    private String category_id;
    private String code;
    private String created_at;

    /* renamed from: id  reason: collision with root package name */
    private String f803id;
    private String status;
    private int thumbnail;
    private String title;
    private String type;
    private String updated_at;

    public FrameModel(int thumbnail, String title, String type) {
        this.thumbnail = thumbnail;
        this.title = title;
        this.type = type;
    }

    public String getId() {
        return this.f803id;
    }

    public void setId(String id2) {
        this.f803id = id2;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getThumbnail() {
        return this.thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public String getCategory_id() {
        return this.category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUpdated_at() {
        return this.updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_at() {
        return this.created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
