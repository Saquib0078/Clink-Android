package com.nirmiteepublic.clink.models;

import java.util.List;

public class MergedUserResponse{
	private List<MergedUsersItem> mergedUsers;

	public void setMergedUsers(List<MergedUsersItem> mergedUsers){
		this.mergedUsers = mergedUsers;
	}

	public List<MergedUsersItem> getMergedUsers(){
		return mergedUsers;
	}
}