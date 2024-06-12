package com.nirmiteepublic.clink.models;

public class GraphicsModelGson{
	private Graphicsdata graphicsdata;
	private String status;

	public void setGraphicsdata(Graphicsdata graphicsdata){
		this.graphicsdata = graphicsdata;
	}

	public Graphicsdata getGraphicsdata(){
		return graphicsdata;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}
}
