package com.ustb.entity;

public class E_Join {
	private int jid;
	private String jname;
	private String jtel;
	private int aid;
	private int userid;
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public int getJid() {
		return jid;
	}
	public void setJid(int jid) {
		this.jid = jid;
	}
	public String getJname() {
		return jname;
	}
	public void setJname(String jname) {
		this.jname = jname;
	}
	public String getJtel() {
		return jtel;
	}
	public void setJtel(String jtel) {
		this.jtel = jtel;
	}
	public int getAid() {
		return aid;
	}
	public void setAid(int aid) {
		this.aid = aid;
	}
}
