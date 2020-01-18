package com.ustb.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class E_News implements Serializable{
	private int newsid;
	private String newstitle;
	private String newsmess;
	private String newsphoto1;
	private String newsphoto2;
	private String newsauthor;
	private String newsdate;
	public String getNewsdate() {
		return newsdate;
	}

	public void setNewsdate(String newsdate) {
		this.newsdate = newsdate;
	}

	public int getNewsid() {
		return newsid;
	}

	public void setNewsid(int newsid) {
		this.newsid = newsid;
	}

	public String getNewstitle() {
		return newstitle;
	}

	public void setNewstitle(String newstitle) {
		this.newstitle = newstitle;
	}

	public String getNewsmess() {
		return newsmess;
	}

	public void setNewsmess(String newsmess) {
		this.newsmess = newsmess;
	}

	public String getNewsphoto1() {
		return newsphoto1;
	}

	public void setNewsphoto1(String newsphoto1) {
		this.newsphoto1 = newsphoto1;
	}

	public String getNewsphoto2() {
		return newsphoto2;
	}

	public void setNewsphoto2(String newsphoto2) {
		this.newsphoto2 = newsphoto2;
	}

	public String getNewsauthor() {
		return newsauthor;
	}

	public void setNewsauthor(String newsauthor) {
		this.newsauthor = newsauthor;
	}

}
