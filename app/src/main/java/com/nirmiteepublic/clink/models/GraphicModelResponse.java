package com.nirmiteepublic.clink.models;

import java.util.List;

public class GraphicModelResponse{
	private List<GraphicDataItem> graphicData;
	private String status;

	public void setGraphicData(List<GraphicDataItem> graphicData){
		this.graphicData = graphicData;
	}

	public List<GraphicDataItem> getGraphicData(){
		return graphicData;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}
}