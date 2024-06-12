package com.nirmiteepublic.clink.models;

import java.util.List;

public class ChipBtnResponse{
	private List<ChipBtnsItem> chipBtns;
	private String status;

	public void setChipBtns(List<ChipBtnsItem> chipBtns){
		this.chipBtns = chipBtns;
	}

	public List<ChipBtnsItem> getChipBtns(){
		return chipBtns;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}
}