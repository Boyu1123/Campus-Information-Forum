package com.ustb.model;

import com.ustb.entity.E_User;

public class UserData extends BeanData {

	private E_User user;
	private String photourl;
	public String getPhotourl() {
		return photourl;
	}

	public void setPhotourl(String photourl) {
		this.photourl = photourl;
	}

	public E_User getUser() {
		return user;
	}

	public void setUser(E_User user) {
		this.user = user;
	}
}
