package com.nirmiteepublic.clink.models;

import java.util.List;

public class Graphicsdata{
	private String createdAt;
	private int v;
	private List<String> graphicModelList;
	private String id;
	private String title;
	private String type;
	private String updatedAt;

	public Graphicsdata(List<String> graphicModelList, String title, String type) {
		this.graphicModelList = graphicModelList;
		this.title = title;
		this.type = type;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setV(int v){
		this.v = v;
	}

	public int getV(){
		return v;
	}

	public void setGraphicModelList(List<String> graphicModelList){
		this.graphicModelList = graphicModelList;
	}

	public List<String> getGraphicModelList(){
		return graphicModelList;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}
}