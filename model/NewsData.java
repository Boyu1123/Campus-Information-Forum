package com.ustb.model;

import java.util.ArrayList;

import com.ustb.entity.E_News;

public class NewsData extends BeanData {
	private ArrayList<E_News> list;

	public ArrayList<E_News> getList() {
		return list;
	}

	public void setList(ArrayList<E_News> list) {
		this.list = list;
	}
}
