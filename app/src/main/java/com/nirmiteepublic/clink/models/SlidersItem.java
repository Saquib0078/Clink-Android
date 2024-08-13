package com.nirmiteepublic.clink.models;

public class SlidersItem{
	private String slider;
	private String createdAt;
	private int v;
	private String _id;
	private String updatedAt;

	public void setSlider(String slider){
		this.slider = slider;
	}

	public String getSlider(){
		return slider;
	}

	public SlidersItem(String slider) {
		this.slider = slider;
	}

	public SlidersItem() {
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

	public void setId(String id){
		this._id = _id;
	}

	public String getId(){
		return _id;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}
}
