package com.nirmiteepublic.clink.models;

import com.google.gson.annotations.SerializedName;

public class DataItem {
	private boolean isSelected;

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean selected) {
		isSelected = selected;
	}
	@SerializedName("insta")
	private String insta;

	@SerializedName("lName")
	private String lName;

	@SerializedName("role")
	private int role;

	@SerializedName("vill")
	private String vill;

	@SerializedName("num")
	private String num;

	@SerializedName("dist")
	private String dist;

	@SerializedName("source")
	private String source;

	@SerializedName("ward")
	private String ward;

	@SerializedName("lMark")
	private String lMark;

	@SerializedName("booth")
	private String booth;

	@SerializedName("regTime")
	private String regTime;

	@SerializedName("intr")
	private String intr;

	@SerializedName("fName")
	private String fName;

	@SerializedName("wpn")
	private String wpn;

	@SerializedName("teh")
	private String teh;

	@SerializedName("edu")
	private String edu;

	@SerializedName("lang")
	private String lang;

	@SerializedName("fb")
	private String fb;

	@SerializedName("Image")
	private String image;

	@SerializedName("FrameName")
	private String frameName;

	@SerializedName("FrameAdd")
	private String frameAdd;

	@SerializedName("completed")
	private boolean completed;

	public void setInsta(String insta){
		this.insta = insta;
	}

	public String getInsta(){
		return insta;
	}

	public void setLName(String lName){
		this.lName = lName;
	}

	public String getLName(){
		return lName;
	}

	public void setRole(int role){
		this.role = role;
	}

	public int getRole(){
		return role;
	}

	public void setVill(String vill){
		this.vill = vill;
	}

	public String getVill(){
		return vill;
	}

	public void setNum(String num){
		this.num = num;
	}

	public String getNum(){
		return num;
	}

	public void setDist(String dist){
		this.dist = dist;
	}

	public String getDist(){
		return dist;
	}

	public void setSource(String source){
		this.source = source;
	}

	public String getSource(){
		return source;
	}

	public void setWard(String ward){
		this.ward = ward;
	}

	public String getWard(){
		return ward;
	}

	public void setLMark(String lMark){
		this.lMark = lMark;
	}

	public String getLMark(){
		return lMark;
	}

	public void setBooth(String booth){
		this.booth = booth;
	}

	public String getBooth(){
		return booth;
	}

	public void setRegTime(String regTime){
		this.regTime = regTime;
	}

	public String getRegTime(){
		return regTime;
	}

	public void setIntr(String intr){
		this.intr = intr;
	}

	public String getIntr(){
		return intr;
	}

	public void setFName(String fName){
		this.fName = fName;
	}

	public String getFName(){
		return fName;
	}

	public void setWpn(String wpn){
		this.wpn = wpn;
	}

	public String getWpn(){
		return wpn;
	}

	public void setTeh(String teh){
		this.teh = teh;
	}

	public String getTeh(){
		return teh;
	}

	public void setEdu(String edu){
		this.edu = edu;
	}

	public String getEdu(){
		return edu;
	}

	public void setLang(String lang){
		this.lang = lang;
	}

	public String getLang(){
		return lang;
	}

	public void setFb(String fb){
		this.fb = fb;
	}

	public String getFb(){
		return fb;
	}

	public void setImage(String image){
		this.image = image;
	}

	public String getImage(){
		return image;
	}

	public void setFrameName(String frameName){
		this.frameName = frameName;
	}

	public String getFrameName(){
		return frameName;
	}

	public void setFrameAdd(String frameAdd){
		this.frameAdd = frameAdd;
	}

	public String getFrameAdd(){
		return frameAdd;
	}

	public void setCompleted(boolean completed){
		this.completed = completed;
	}

	public boolean isCompleted(){
		return completed;
	}
}