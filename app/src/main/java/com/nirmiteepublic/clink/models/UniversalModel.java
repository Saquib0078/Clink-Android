package com.nirmiteepublic.clink.models;

import java.util.List;

public class UniversalModel {
    private  String title;
    private String type;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public UniversalModel(String title, String type, String createdAt, String id) {
        this.title = title;
        this.type = type;
        this.createdAt = createdAt;
        this.id = id;
    }

    private String createdAt;

    String id;

    public UniversalModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> chipButtonList;
    public List<GraphicModel> graphicModelList;



    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public List<String> getChipButtonList() {
        return chipButtonList;
    }

    public UniversalModel setChipButtonList(List<String> chipButtonList) {
        this.chipButtonList = chipButtonList;
        return this;
    }

    public List<GraphicModel> getGraphicModelList() {
        return graphicModelList;
    }

    public UniversalModel setGraphicModelList(List<GraphicModel> graphicModelList) {
        this.graphicModelList = graphicModelList;
        return this;
    }
}
