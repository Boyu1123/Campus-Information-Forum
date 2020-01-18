package com.ustb.application;

import cn.sharesdk.framework.ShareSDK;

import com.ustb.entity.E_User;

import android.app.Application;

public class MyApplication extends Application {
	private E_User user;
	private boolean loginCode;

	@Override
	public void onCreate() {
		ShareSDK.initSDK(this);
		super.onCreate();
	}

	public E_User getUser() {
		return user;
	}

	public void setUser(E_User user) {
		this.user = user;
	}

	public boolean isLoginCode() {
		return loginCode;
	}

	public void setLoginCode(boolean loginCode) {
		this.loginCode = loginCode;
	}

}
