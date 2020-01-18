package com.ustb.model;

import java.util.ArrayList;

import com.ustb.entity.E_Card;

public class CardData extends BeanData {
	private ArrayList<E_Card> list;

	public ArrayList<E_Card> getList() {
		return list;
	}

	public void setList(ArrayList<E_Card> list) {
		this.list = list;
	}
	
}
