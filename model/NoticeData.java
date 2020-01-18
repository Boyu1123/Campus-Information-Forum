package com.ustb.model;

import java.util.ArrayList;

import com.ustb.entity.E_Notice;

public class NoticeData extends BeanData {
	private ArrayList<E_Notice> list;

	public ArrayList<E_Notice> getList() {
		return list;
	}

	public void setList(ArrayList<E_Notice> list) {
		this.list = list;
	}
}
