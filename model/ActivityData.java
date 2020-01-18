package com.ustb.model;

import java.util.ArrayList;

import com.ustb.entity.E_Activity;

public class ActivityData extends BeanData {
	private ArrayList<E_Activity> list;

	public ArrayList<E_Activity> getList() {
		return list;
	}

	public void setList(ArrayList<E_Activity> list) {
		this.list = list;
	}
}
