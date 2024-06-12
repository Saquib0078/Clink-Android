package com.nirmiteepublic.clink.models;

import java.util.List;

public class SliderImageResponse{
	private List<SlidersItem> sliders;
	private String status;

	public void setSliders(List<SlidersItem> sliders){
		this.sliders = sliders;
	}

	public List<SlidersItem> getSliders(){
		return sliders;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}
}