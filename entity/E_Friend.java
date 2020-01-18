package com.ustb.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class E_Friend implements Serializable{
	private int id;
	private int friendid;
	private String friendname;
	private String friendhead;
	private String friendinfo;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getFriendid() {
		return friendid;
	}
	public void setFriendid(int friendid) {
		this.friendid = friendid;
	}
	public String getFriendname() {
		return friendname;
	}
	public void setFriendname(String friendname) {
		this.friendname = friendname;
	}
	public String getFriendhead() {
		return friendhead;
	}
	public void setFriendhead(String friendhead) {
		this.friendhead = friendhead;
	}
	public String getFriendinfo() {
		return friendinfo;
	}
	public void setFriendinfo(String friendinfo) {
		this.friendinfo = friendinfo;
	}
	
}
