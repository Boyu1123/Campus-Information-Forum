package com.ustb.model;

import java.util.ArrayList;

import com.ustb.entity.E_Comment;

public class CommentData extends BeanData {
	private ArrayList<E_Comment> list;

	public ArrayList<E_Comment> getList() {
		return list;
	}

	public void setList(ArrayList<E_Comment> list) {
		this.list = list;
	}
	
}
