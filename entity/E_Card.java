package com.ustb.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class E_Card implements Serializable {
	private int cardid;
	private String cardtitle;
	private String cardmess;
	private int userid;
	private String username;
	private String cardphoto1;
	private String cardphoto2;
	private String carddate;
	private int num;

	public int getCardid() {
		return cardid;
	}

	public void setCardid(int cardid) {
		this.cardid = cardid;
	}

	public String getCardtitle() {
		return cardtitle;
	}

	public void setCardtitle(String cardtitle) {
		this.cardtitle = cardtitle;
	}

	public String getCardmess() {
		return cardmess;
	}

	public void setCardmess(String cardmess) {
		this.cardmess = cardmess;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCardphoto1() {
		return cardphoto1;
	}

	public void setCardphoto1(String cardphoto1) {
		this.cardphoto1 = cardphoto1;
	}

	public String getCardphoto2() {
		return cardphoto2;
	}

	public void setCardphoto2(String cardphoto2) {
		this.cardphoto2 = cardphoto2;
	}

	public String getCarddate() {
		return carddate;
	}

	public void setCarddate(String carddate) {
		this.carddate = carddate;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

}
