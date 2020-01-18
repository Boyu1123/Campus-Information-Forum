package com.ustb.page;

import java.util.HashMap;
import java.util.Map;

import com.ustb.application.MyApplication;
import com.ustb.callback.Callback;
import com.ustb.http.connection.HttpServer;
import com.ustb.model.BeanData;
import com.ustb.parser.UserParser;
import com.ustb.school.R;
import com.ustb.school.R.layout;
import com.ustb.school.R.menu;
import com.ustb.status.StatusCode;
import com.ustb.url.URLConstants;
import com.ustb.utils.ExampleUtil;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Other_ChangePass extends Activity implements OnClickListener {
	private Button btn_back, btn_submit;
	private EditText e_oldPass, e_pass, e_passtoo;
	private MyApplication app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.other_changepass);
		init();
	}

	private void init() {
		btn_back = (Button) findViewById(R.id.back);
		btn_submit = (Button) findViewById(R.id.change_suubmit);
		e_oldPass = (EditText) findViewById(R.id.change_oldpass);
		e_pass = (EditText) findViewById(R.id.change_pass);
		e_passtoo = (EditText) findViewById(R.id.change_passtoo);
		btn_back.setOnClickListener(this);
		btn_submit.setOnClickListener(this);
		app = (MyApplication) getApplication();
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.change_suubmit:
			changePass();
			break;

		default:
			break;
		}

	}

	private void changePass() {
		String oldpass = e_oldPass.getText().toString();
		String pass = e_pass.getText().toString();
		String passtoo = e_passtoo.getText().toString();
		if (ExampleUtil.isEmpty(oldpass) || ExampleUtil.isEmpty(pass)
				|| ExampleUtil.isEmpty(passtoo)) {
			ExampleUtil.showToast("请填写完整信息", Other_ChangePass.this);
		} else {
			if (pass.equals(passtoo)) {
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("type", "updatePass");
				params.put("userid", String.valueOf(app.getUser().getId()));
				params.put("oldpass", oldpass);
				params.put("pass", pass);
				HttpServer.setPostRequest(params, new UserParser(),
						URLConstants.BASEURL + URLConstants.USERURL,
						new Callback() {

							@Override
							public void success(BeanData beanData) {
								if (beanData.getCode() == StatusCode.Common.SUCCESS) {
									if (beanData.getFlag() == StatusCode.Dao.INSERT_SUCCESS) {
										ExampleUtil.showToast("修改密码成功",
												Other_ChangePass.this);
										e_oldPass.setText("");
										e_pass.setText("");
										e_passtoo.setText("");
									} else {
										ExampleUtil.showToast("旧密码填写错误",
												Other_ChangePass.this);
									}
								} else {
									ExampleUtil.showToast("服务器繁忙",
											Other_ChangePass.this);
								}

							}

							@Override
							public void fail(String error) {
								ExampleUtil.showToast(error,
										Other_ChangePass.this);

							}
						});
			} else {
				ExampleUtil.showToast("服务器繁忙", Other_ChangePass.this);
			}
		}

	}

}
