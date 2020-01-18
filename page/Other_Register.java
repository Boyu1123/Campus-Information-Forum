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

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class Other_Register extends Activity implements OnClickListener {
	private Button btn_back, btn_login;
	private TextView t_judge;
	private EditText e_account, e_pass, e_passtoo, e_name, e_email;
	private RadioGroup rg;
	private RadioButton rb;
	private boolean isJudge = false;
	private String path;
	private ImageView imageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.other_register);
		init();
	}

	private void init() {
		imageView = (ImageView) findViewById(R.id.register_img);
		rg = (RadioGroup) findViewById(R.id.register_rg);
		e_account = (EditText) findViewById(R.id.register_account);
		e_pass = (EditText) findViewById(R.id.register_pass);
		e_passtoo = (EditText) findViewById(R.id.register_pass_too);
		e_name = (EditText) findViewById(R.id.register_name);
		e_email = (EditText) findViewById(R.id.register_email);
		t_judge = (TextView) findViewById(R.id.register_judge);
		btn_back = (Button) findViewById(R.id.back);
		btn_login = (Button) findViewById(R.id.register_login);
		btn_back.setOnClickListener(this);
		t_judge.setOnClickListener(this);
		btn_login.setOnClickListener(this);
		imageView.setOnClickListener(this);
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// 获取变更后的选中项的ID
				int radioButtonId = arg0.getCheckedRadioButtonId();
				// 根据ID获取RadioButton的实例
				rb = (RadioButton) findViewById(radioButtonId);

			}
		});
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.register_judge:
			// 检测用户名是否存在
			if (isJudge) {

			} else {
				if (ExampleUtil.isEmpty(e_account.getText().toString())) {
					ExampleUtil.showToast("请先输入账号", Other_Register.this);
				} else {
					judgeAccount();
				}
			}
			break;

		case R.id.register_login:
			if (isJudge) {
				addUser();
			} else {
				ExampleUtil.showToast("请先进行账号检测", Other_Register.this);
			}
			break;
		case R.id.register_img:
			selectHead();
			break;
		}

	}

	/**
	 * 检测用户名是否存在
	 */
	private void judgeAccount() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", "judgeAccount");
		params.put("account", e_account.getText().toString());
		HttpServer.setPostRequest(params, new UserParser(),
				URLConstants.BASEURL + URLConstants.USERURL, new Callback() {

					@Override
					public void success(BeanData beanData) {
						if (beanData.getCode() == StatusCode.Common.SUCCESS) {
							if (beanData.getFlag() == StatusCode.Dao.SELECT_SUCCESS) {

								t_judge.setText("成功");
								t_judge.setBackgroundColor(Color
										.parseColor("#03C893"));
								isJudge = true;
							} else {
								e_account.setText("");
								ExampleUtil.showToast("该账户已经存在，请重试",
										Other_Register.this);
							}
						} else {
							ExampleUtil.showToast("服务器繁忙,请重试",
									Other_Register.this);
						}
					}

					@Override
					public void fail(String error) {
						ExampleUtil.showToast(error, Other_Register.this);

					}
				});
	}

	/**
	 * 增加用户
	 */
	private void addUser() {
		String account = e_account.getText().toString();
		String name = e_name.getText().toString();
		String pass = e_pass.getText().toString();
		String passtoo = e_passtoo.getText().toString();
		String email = e_email.getText().toString();
		String sex = rb.getText().toString();
		if (ExampleUtil.isEmpty(account) || ExampleUtil.isEmpty(name)
				|| ExampleUtil.isEmpty(pass) || ExampleUtil.isEmpty(passtoo)
				|| ExampleUtil.isEmpty(email)|| ExampleUtil.isEmpty(path)) {
			ExampleUtil.showToast("填写所有信息以及选择头像", Other_Register.this);
		} else {
			if (pass.equals(passtoo)) {
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("type", "addUser");
				params.put("account", account);
				params.put("name", name);
				params.put("pass", pass);
				params.put("email", email);
				params.put("File", path);
				params.put("sex", sex);
				params.put("info", "这个用户很懒，什么都没有留下");
				params.put("admin", "0");
				HttpServer.setPostRequest(params, new UserParser(),
						URLConstants.BASEURL + URLConstants.USERURL,
						new Callback() {

							@Override
							public void success(BeanData beanData) {
								if (beanData.getCode() == StatusCode.Common.SUCCESS) {
									if (beanData.getFlag() == StatusCode.Dao.INSERT_SUCCESS) {
										backWithData();
									} else {
										ExampleUtil.showToast("注册失败，请重试",
												Other_Register.this);
									}
								} else {
									ExampleUtil.showToast("服务器繁忙，请重试",
											Other_Register.this);
								}

							}

							@Override
							public void fail(String error) {
								// TODO Auto-generated method stub

							}
						});
			} else {
				ExampleUtil.showToast("两次密码不同，请重新输入", Other_Register.this);
				e_pass.setText("");
				e_passtoo.setText("");
			}
		}

	}

	/**
	 * 注册成功后直接返回登录界面并携带数据
	 */
	private void backWithData() {
		Intent intent = new Intent("REGISTER_OK");
		intent.putExtra("account", e_account.getText().toString());
		intent.putExtra("pass", e_pass.getText().toString());
		sendBroadcast(intent);
		finish();

	}

	/**
	 * 头像的选择
	 */
	private void selectHead() {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_PICK);
		intent.setType("image/*");
		startActivityForResult(intent, 1);
	}

	/**
	 * 头像选择后返回图片路径
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				Uri uri = data.getData();
				// 内容解析器对象
				ContentResolver con = getContentResolver();
				// 根据地址查询图片
				Cursor c = con.query(uri, null, null, null, null);
				// 获取路径
				if (c.moveToNext()) {
					// String picNo = c.getString(0); //图片的编号
					path = c.getString(1);
					// String imagesize = c.getString(2);
					// String imagename = c.getString(3);
				}
				imageView.setImageBitmap(BitmapFactory.decodeFile(path));
			}

		}
	}
}
