package com.dodola.model;

public class DuitangInfo {

	private int height;
	private String albid = "";//id
	private String msg = "";//菜名
	private String isrc = "";//图片路径
    private int dishcommentnum = 0;//评论数目
	private int dishcollectionnum = 0;//收藏数目
	
	public int getWidth(){
		return 200;
	}
	public String getAlbid() {
		return albid;
	}

	public void setAlbid(String albid) {
		this.albid = albid;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getIsrc() {
		return isrc;
	}

	public void setIsrc(String isrc) {
		this.isrc = isrc;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public int getDishcommentnum(){
		return dishcommentnum;
	}

	public void setDishcommentnum(int number){
		this.dishcommentnum = number;
	}
	
	public int getDishcollectionnum(){
		return dishcollectionnum;
	}

	public void setDishcollectionnum(int number){
		this.dishcollectionnum = number;
	}
}
