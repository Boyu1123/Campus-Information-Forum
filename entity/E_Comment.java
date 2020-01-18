package com.ustb.entity;

public class E_Comment {
	private int cid;
	private int cardid;
	private int uid;
	private String cmess;
	private E_User e_User;
	private E_Card e_Card;
	private String uname;
	private String uhead;
	private String ctitle;
	private String date;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getUhead() {
		return uhead;
	}

	public void setUhead(String uhead) {
		this.uhead = uhead;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getCtitle() {
		return ctitle;
	}

	public void setCtitle(String ctitle) {
		this.ctitle = ctitle;
	}

	public E_User getE_User() {
		return e_User;
	}

	public void setE_User(E_User e_User) {
		this.e_User = e_User;
	}

	public E_Card getE_Card() {
		return e_Card;
	}

	public void setE_Card(E_Card e_Card) {
		this.e_Card = e_Card;
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public int getCardid() {
		return cardid;
	}

	public void setCardid(int cardid) {
		this.cardid = cardid;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getCmess() {
		return cmess;
	}

	public void setCmess(String cmess) {
		this.cmess = cmess;
	}

}
