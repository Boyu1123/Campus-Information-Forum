package com.ustb.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class E_Activity implements Serializable{
	private int aid;
	private String atitle;
	private String amess;
	private String astarttime;
	private String aplace;
	private String ainfo;
	private int apeople;
	private int userid;
	private String username;
	private String date;
	private int acode;
	private String atel;
	private int ahave;
	
	public String getAtel() {
		return atel;
	}
	public void setAtel(String atel) {
		this.atel = atel;
	}
	public int getAhave() {
		return ahave;
	}
	public void setAhave(int ahave) {
		this.ahave = ahave;
	}
	public int getAcode() {
		return acode;
	}
	public void setAcode(int acode) {
		this.acode = acode;
	}
	public int getAid() {
		return aid;
	}
	public void setAid(int aid) {
		this.aid = aid;
	}
	public String getAtitle() {
		return atitle;
	}
	public void setAtitle(String atitle) {
		this.atitle = atitle;
	}
	public String getAmess() {
		return amess;
	}
	public void setAmess(String amess) {
		this.amess = amess;
	}
	public String getAstarttime() {
		return astarttime;
	}
	public void setAstarttime(String astarttime) {
		this.astarttime = astarttime;
	}
	public String getAplace() {
		return aplace;
	}
	public void setAplace(String aplace) {
		this.aplace = aplace;
	}
	public String getAinfo() {
		return ainfo;
	}
	public void setAinfo(String ainfo) {
		this.ainfo = ainfo;
	}
	public int getApeople() {
		return apeople;
	}
	public void setApeople(int apeople) {
		this.apeople = apeople;
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
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	
}
