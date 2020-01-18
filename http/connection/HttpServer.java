package com.ustb.http.connection;

import java.io.File;
import java.util.Map;

import android.util.Log;


import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.PreferencesCookieStore;
import com.ustb.callback.Callback;
import com.ustb.model.BeanData;
import com.ustb.parser.BeanParser;

/*
 * 实现通过http协议向服务器发送消息
 */
public class HttpServer {
	/*
	 * 向服务器发送消息超时时间
	 */
	private final static int TIMEOUT = 5000;
	/*
	 * 设置请求响应编码
	 */
	private final static String CHARSET = "UTF-8";

	/*
	 * 携带cookie
	 */
	public static PreferencesCookieStore preferencesCookieStore;

	/**
	 * 实现向服务器发送请求
	 * 
	 * @param params
	 *            实现向服务区传递值
	 * @param parser
	 *            解析器（{flag：1002，code：}==>java对象）
	 * @param url
	 * @param callback
	 *            用来实现把解析之后的结果传给界面 参数->解析->callback
	 */
	public static void setPostRequest(Map<String, Object> params,
			final BeanParser parser, String url, final Callback callback) {
		/**
		 * 创建一个参数对象，用来存储需要传递的参数 如果想实现把值传给服务器的话，那么就必须把值存到RequestParams
		 */
		RequestParams rp = new RequestParams();
		if (params != null) {
			for (String key : params.keySet()) {
				if (key.contains("File")) {
					rp.addBodyParameter(key, new File((String) params.get(key)));
				} else {
					rp.addBodyParameter(key, (String) params.get(key));
				}

			}
		}

		// 用来向服务器发送请求
		HttpUtils httpUtils = new HttpUtils();

		if (preferencesCookieStore != null) {
			httpUtils.configCookieStore(preferencesCookieStore);
		}
		httpUtils.configResponseTextCharset(CHARSET);
		httpUtils.configTimeout(TIMEOUT);

		// 向服务器发送请求的方法
		httpUtils.send(HttpMethod.POST, url, rp, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				callback.fail(arg1);

			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// 从服务器返回的json串
				String json = arg0.result;
				// Log.d("test", json);
				// 解析器是用来把json字符串变成java对象
				BeanData beanData = parser.parser(json);
				callback.success(beanData);
			}
		});
	}

	public static void sendMsg(Map<String, Object> params,
			final BeanParser parser, String url, final Callback callback) {
		/**
		 * 创建一个参数对象，用来存储需要传递的参数 如果想实现把值传给服务器的话，那么就必须把值存到RequestParams
		 */
		RequestParams rp = new RequestParams();
		if (params != null) {
			for (String key : params.keySet()) {
				if (key.contains("File")) {
					rp.addBodyParameter(key, new File((String) params.get(key)));
				} else {
					rp.addBodyParameter(key, (String) params.get(key));
				}

			}
		}

		// 用来向服务器发送请求
		HttpUtils httpUtils = new HttpUtils();

		if (preferencesCookieStore != null) {
			httpUtils.configCookieStore(preferencesCookieStore);
		}
		httpUtils.configResponseTextCharset(CHARSET);
		httpUtils.configTimeout(TIMEOUT);

		// 向服务器发送请求的方法
		httpUtils.send(HttpMethod.POST, url, rp, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				callback.fail(arg1);

			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// 从服务器返回的json串
				String json = arg0.result;
				// Log.d("test", json);
				// 解析器是用来把json字符串变成java对象
				if (parser == null) {
//					Log.d("test", "成功");
				} else {
					BeanData beanData = parser.parser(json);
					callback.success(beanData);

				}
			}
		});
	}
}
