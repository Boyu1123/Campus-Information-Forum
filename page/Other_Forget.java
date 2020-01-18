package com.ustb.page;

import java.util.HashMap;
import java.util.Map;

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

public class Other_Forget extends Activity implements OnClickListener {
	private Button btn_back, btn_submit;
	private EditText e_account, e_email, e_pass, e_passtoo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.other_forget);
		init();
	}

	private void init() {
		e_account = (EditText) findViewById(R.id.forget_account);
		e_email = (EditText) findViewById(R.id.forget_email);
		e_pass = (EditText) findViewById(R.id.forget_pass);
		e_passtoo = (EditText) findViewById(R.id.forget_passtoo);
		btn_submit = (Button) findViewById(R.id.forget_submit);
		btn_back = (Button) findViewById(R.id.back);
		btn_back.setOnClickListener(this);
		btn_submit.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.forget_submit:
			dealMess();
			break;
		}

	}

	private void dealMess() {
		String account = e_account.getText().toString();
		String email = e_email.getText().toString();
		String pass = e_pass.getText().toString();
		String passtoo = e_passtoo.getText().toString();
		if (ExampleUtil.isEmpty(account) || ExampleUtil.isEmpty(email)
				|| ExampleUtil.isEmpty(pass) || ExampleUtil.isEmpty(passtoo)) {
			ExampleUtil.showToast("请填写完整信息", this);
		} else {
			if (pass.equals(passtoo)) {
				updatePass(account, email, pass);
			} else {
				ExampleUtil.showToast("两次密码输入不同", this);
				e_pass.setText("");
				e_passtoo.setText("");
			}
		}

	}

	private void updatePass(String account, String email, String pass) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", "changePass");
		params.put("account", account);
		params.put("email", email);
		params.put("pass", pass);
		HttpServer.setPostRequest(params, new UserParser(),
				URLConstants.BASEURL + URLConstants.USERURL, new Callback() {

					@Override
					public void success(BeanData beanData) {
						if (beanData.getCode() == StatusCode.Common.SUCCESS) {
							if (beanData.getFlag() == StatusCode.Dao.UPDATE_SUCCESS) {
								ExampleUtil.showToast("修改成功,请登录", Other_Forget.this);
								finish();
							} else {
								ExampleUtil.showToast("提供的账号或者邮箱错误，请重试", Other_Forget.this);
								e_account.setText("");
								e_email.setText("");
								e_pass.setText("");
								e_passtoo.setText("");
							}
						} else {
							ExampleUtil.showToast("服务器繁忙", Other_Forget.this);
						}

					}

					@Override
					public void fail(String error) {
						ExampleUtil.showToast(error, Other_Forget.this);

					}
				});

	}
}
