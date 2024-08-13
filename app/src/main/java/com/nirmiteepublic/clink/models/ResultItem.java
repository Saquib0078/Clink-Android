package com.nirmiteepublic.clink.models;

public class ResultItem{
	private String district;
	private int count;
	private String village;
	private String tehsil;

	public void setDistrict(String district){
		this.district = district;
	}

	public String getDistrict(){
		return district;
	}

	public void setCount(int count){
		this.count = count;
	}

	public int getCount(){
		return count;
	}

	public void setVillage(String village){
		this.village = village;
	}

	public String getVillage(){
		return village;
	}

	public void setTehsil(String tehsil){
		this.tehsil = tehsil;
	}

	public String getTehsil(){
		return tehsil;
	}
}
