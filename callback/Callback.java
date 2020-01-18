package com.ustb.callback;

import com.ustb.model.BeanData;


public interface Callback {
	
	public void success(BeanData beanData);

	
	public void fail(String error);
}
