package com.visticsolution.posterbanao.model;

import java.util.List;

public class VideoTamplateCategory {

    private String id, name, image, orders, status, updated_at, created_at;
    private List<VideoTamplateModel> videos;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOrders() {
        return orders;
    }

    public void setOrders(String orders) {
        this.orders = orders;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public List<VideoTamplateModel> getVideos() {
        return videos;
    }

    public void setVideos(List<VideoTamplateModel> videos) {
        this.videos = videos;
    }
}
