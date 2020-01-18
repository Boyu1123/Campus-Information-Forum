package com.ustb.model;

import java.util.ArrayList;

import com.ustb.entity.E_Friend;

public class FriendData extends BeanData {
	private int id;
	private ArrayList<E_Friend> list;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ArrayList<E_Friend> getList() {
		return list;
	}

	public void setList(ArrayList<E_Friend> list) {
		this.list = list;
	}
	
}
