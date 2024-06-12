package com.nirmiteepublic.clink.models;

public class ChipBtnsItem{
	private String createdAt;
	private int v;
	private String id;
	private String chipButtonList;
	private String updatedAt;

	public ChipBtnsItem(String id, String chipButtonList) {
		this.id = id;
		this.chipButtonList = chipButtonList;
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
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setChipButtonList(String chipButtonList){
		this.chipButtonList = chipButtonList;
	}

	public String getChipButtonList(){
		return chipButtonList;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}
}
