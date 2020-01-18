package com.ustb.page;

import java.util.HashMap;
import java.util.Map;

import com.ustb.application.MyApplication;
import com.ustb.callback.Callback;
import com.ustb.entity.E_User;
import com.ustb.http.connection.HttpServer;
import com.ustb.model.BeanData;
import com.ustb.model.UserData;
import com.ustb.parser.UserParser;
import com.ustb.school.R;
import com.ustb.school.R.layout;
import com.ustb.school.R.menu;
import com.ustb.status.StatusCode;
import com.ustb.url.URLConstants;
import com.ustb.utils.ExampleUtil;
import com.ustb.utils.SPUtils;

import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Other_Login extends Activity implements OnClickListener {
	private Button btn_back, btn_login;
	private TextView t_register, t_forget;
	private EditText e_account, e_pass;
	private MyBroadcast myBroadcast;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.other_login);
		init();
	}

	private void init() {
		btn_back = (Button) findViewById(R.id.back);
		btn_login = (Button) findViewById(R.id.login_in);
		t_register = (TextView) findViewById(R.id.login_register);
		t_forget = (TextView) findViewById(R.id.login_forget);
		e_account = (EditText) findViewById(R.id.login_account);
		e_pass = (EditText) findViewById(R.id.login_pass);
		btn_login.setOnClickListener(this);
		btn_back.setOnClickListener(this);
		t_register.setOnClickListener(this);
		t_forget.setOnClickListener(this);

		myBroadcast = new MyBroadcast();
		registerReceiver(myBroadcast, new IntentFilter("REGISTER_OK"));
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.login_in:
			loginIn();
			break;
		case R.id.login_register:

			Intent registerintent = new Intent(Other_Login.this,
					Other_Register.class);
			startActivity(registerintent);
			break;
		case R.id.login_forget:
			Intent forgetintent = new Intent(Other_Login.this,
					Other_Forget.class);
			startActivity(forgetintent);
			break;

		}

	}

	/**
	 * 登录数据填写判断
	 */
	private void loginIn() {
		String account = e_account.getText().toString();
		String pass = e_pass.getText().toString();
		if (account.equals("") || pass.equals("")) {
			Toast.makeText(Other_Login.this, "请输入用户名和密码", Toast.LENGTH_SHORT)
					.show();
		} else {
			userJudge(account, pass);
		}
	}

	/**
	 * 登录时验证判断
	 * 
	 * @param name
	 * @param pass
	 */
	private void userJudge(String account, String pass) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", "judgeUser");
		params.put("account", account);
		params.put("pass", pass);
		HttpServer.setPostRequest(params, new UserParser(),
				URLConstants.BASEURL + URLConstants.USERURL, new Callback() {

					@Override
					public void success(BeanData beanData) {
						successBack(beanData);
					}

					@Override
					public void fail(String error) {
						ExampleUtil.showToast(error, Other_Login.this);

					}
				});

	}

	/**
	 * 广播接收器
	 * 
	 * @author mac
	 * 
	 */
	class MyBroadcast extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			String account = arg1.getStringExtra("account");
			String pass = arg1.getStringExtra("pass");
			if (account.equals("")) {
				// 忘记密码时候什么不操作
			} else {
				// 成功注册后直接登录
				e_account.setText(account);
				e_pass.setText(pass);
				loginIn();
			}
		}
	}

	/**
	 * 注销广播
	 */
	@Override
	protected void onDestroy() {
		unregisterReceiver(myBroadcast);

		super.onDestroy();
	}

	/**
	 * 登录成功后执行的操作
	 * 
	 * @param beanData
	 */
	public void successBack(BeanData beanData) {
		UserData userData = (UserData) beanData;
		if (userData.getCode() == StatusCode.Common.SUCCESS) {
			if (userData.getFlag() == StatusCode.Login.LOGIN_SUCCESS) {

				E_User user = userData.getUser();
				MyApplication app = (MyApplication) getApplication();
				app.setUser(user);
				app.setLoginCode(true);

				SPUtils.put(this, "logincode", 1);
				SPUtils.put(this, "id", user.getId());
				SPUtils.put(this, "account", user.getAccount());
				SPUtils.put(this, "name", user.getName());
				SPUtils.put(this, "pass", user.getPass());
				SPUtils.put(this, "email", user.getEmail());
				SPUtils.put(this, "info", user.getInfo());
				SPUtils.put(this, "photourl", user.getPhotourl());
				SPUtils.put(this, "admin", user.getAdmin());
				SPUtils.put(this, "sex", user.getSex());

				Intent mIntent = new Intent();
				mIntent.setAction("UPDATE_USER");
				sendBroadcast(mIntent);
				finish();

			}
			if (userData.getFlag() == StatusCode.Login.LOGIN_PASS_FAIL) {
				Toast.makeText(Other_Login.this, "密码错误请重新输入密码",
						Toast.LENGTH_SHORT).show();
				e_pass.setText("");
			}

			if (userData.getFlag() == StatusCode.Login.LOGIN_ACCOUNT_FAIL) {
				Toast.makeText(Other_Login.this, "账号不存在", Toast.LENGTH_SHORT)
						.show();
				e_account.setText("");
				e_pass.setText("");
			}
		} else {
			Toast.makeText(Other_Login.this, "服务器繁忙，请重试", Toast.LENGTH_SHORT)
					.show();
		}
	}

}
