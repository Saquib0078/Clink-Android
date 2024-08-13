package com.nirmiteepublic.clink.models;

import com.google.gson.annotations.SerializedName;

public class DataSecondary{

	@SerializedName("intr")
	private String intr;

	@SerializedName("vill")
	private String vill;

	@SerializedName("wpn")
	private String wpn;

	@SerializedName("teh")
	private String teh;

	@SerializedName("num")
	private String num;

	@SerializedName("edu")
	private String edu;

	@SerializedName("__v")
	private int v;

	@SerializedName("dist")
	private String dist;

	@SerializedName("_id")
	private String id;

	@SerializedName("lang")
	private String lang;

	@SerializedName("lMark")
	private String lMark;

	@SerializedName("ward")
	private String ward;

	@SerializedName("booth")
	private String booth;

	@SerializedName("insta")
	private String insta;

	@SerializedName("fb")
	private String fb;

	public void setIntr(String intr){
		this.intr = intr;
	}

	public String getIntr(){
		return intr;
	}

	public void setVill(String vill){
		this.vill = vill;
	}

	public String getVill(){
		return vill;
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

	public void setNum(String num){
		this.num = num;
	}

	public String getNum(){
		return num;
	}

	public void setEdu(String edu){
		this.edu = edu;
	}

	public String getEdu(){
		return edu;
	}

	public void setV(int v){
		this.v = v;
	}

	public int getV(){
		return v;
	}

	public void setDist(String dist){
		this.dist = dist;
	}

	public String getDist(){
		return dist;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setLang(String lang){
		this.lang = lang;
	}

	public String getLang(){
		return lang;
	}

	public void setLMark(String lMark){
		this.lMark = lMark;
	}

	public String getLMark(){
		return lMark;
	}

	public void setWard(String ward){
		this.ward = ward;
	}

	public String getWard(){
		return ward;
	}

	public void setBooth(String booth){
		this.booth = booth;
	}

	public String getBooth(){
		return booth;
	}

	public void setInsta(String insta){
		this.insta = insta;
	}

	public String getInsta(){
		return insta;
	}

	public void setFb(String fb){
		this.fb = fb;
	}

	public String getFb(){
		return fb;
	}
}