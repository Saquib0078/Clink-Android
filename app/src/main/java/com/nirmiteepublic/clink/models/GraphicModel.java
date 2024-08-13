package com.nirmiteepublic.clink.models;

public class GraphicModel {
    private final String type;
    private final String graphicID;
    private String id;


    public GraphicModel(String type, String graphicID, String id) {
        this.type = type;
        this.graphicID = graphicID;
        this.id = id;
    }

    public GraphicModel(String type, String graphicID) {
        this.type = type;
        this.graphicID = graphicID;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getGraphicID() {
        return graphicID;
    }
}
