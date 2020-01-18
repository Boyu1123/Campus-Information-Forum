package com.ustb.page;

import java.util.HashMap;
import java.util.Map;

import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
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
import com.ustb.utils.ImageUtils;
import com.ustb.utils.SPUtils;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class Fragment_4_Persoanl extends Activity implements OnClickListener {
	private Button btn_back, btn_submit;
	private TextView t_account;
	private ImageView head;
	private EditText e_name, e_info, e_email, e_sex;
	private MyApplication app;
	private E_User user;
	private String path = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_4_persoanl);
		init();
	}

	private void init() {
		app = (MyApplication) getApplication();
		head = (ImageView) findViewById(R.id.personal_img);
		t_account = (TextView) findViewById(R.id.personal_account);
		e_name = (EditText) findViewById(R.id.personal_name);
		e_info = (EditText) findViewById(R.id.personal_info);
		e_email = (EditText) findViewById(R.id.personal_email);
		e_sex = (EditText) findViewById(R.id.personal_sex);
		btn_submit = (Button) findViewById(R.id.personal_submit);
		btn_back = (Button) findViewById(R.id.back);
		btn_back.setOnClickListener(this);
		btn_submit.setOnClickListener(this);
		head.setOnClickListener(this);

		app = (MyApplication) getApplication();
		user = app.getUser();

		t_account.setText(user.getAccount());
		e_name.setText(user.getName());
		e_info.setText(user.getInfo());
		e_email.setText(user.getEmail());
		e_sex.setText(user.getSex());
		ImageUtils.getBitmapUtils(Fragment_4_Persoanl.this).display(head,
				URLConstants.BASEURL + app.getUser().getPhotourl(),
				new BitmapLoadCallBack<View>() {

					@Override
					public void onLoadCompleted(View arg0, String arg1,
							Bitmap arg2, BitmapDisplayConfig arg3,
							BitmapLoadFrom arg4) {
						// Bitmap bitmap = ImageUtils.circleBitmap(arg2);
						head.setImageBitmap(arg2);
						// head.invalidate();

					}

					@Override
					public void onLoadFailed(View arg0, String arg1,
							Drawable arg2) {
						head.setImageResource(R.drawable.head01);

					}
				});
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.personal_submit:
			updateUser();
			break;
		case R.id.personal_img:
			selectHead();
			break;
		}

	}

	private void updateUser() {
		String name = e_name.getText().toString();
		String info = e_info.getText().toString();
		String email = e_email.getText().toString();
		String sex = e_sex.getText().toString();
		if (ExampleUtil.isEmpty(name) || ExampleUtil.isEmpty(info)
				|| ExampleUtil.isEmpty(email) || ExampleUtil.isEmpty(sex)) {
			ExampleUtil.showToast("请填写完整信息", this);
		} else {
			// ExampleUtil.showToast(path, this);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("type", "updateUser");
			params.put("id", String.valueOf(user.getId()));
			params.put("account", user.getAccount());
			params.put("name", name);
			params.put("pass", user.getPass());
			params.put("email", email);
			params.put("sex", sex);
			params.put("info", info);
			params.put("admin", String.valueOf(user.getAdmin()));
			if (path == null) {
				params.put("photourl", user.getPhotourl());
			} else {
				params.put("File", path);
			}

			HttpServer.setPostRequest(params, new UserParser(),
					URLConstants.BASEURL + URLConstants.USERURL,
					new Callback() {

						@Override
						public void success(BeanData beanData) {
							if (beanData.getCode() == StatusCode.Common.SUCCESS) {
								if (beanData.getFlag() == StatusCode.Dao.UPDATE_SUCCESS) {
									UserData data = (UserData) beanData;
									ExampleUtil.showToast("资料修改成功",
											Fragment_4_Persoanl.this);
									user.setName(e_name.getText().toString());
									user.setInfo(e_info.getText().toString());
									user.setSex(e_sex.getText().toString());
									user.setEmail(e_email.getText().toString());
									if (path != null) {
										user.setPhotourl(data.getPhotourl());
										SPUtils.put(Fragment_4_Persoanl.this,
												"photourl", data.getPhotourl());
									}
									app.setUser(user);
									SPUtils.put(Fragment_4_Persoanl.this,
											"name", e_name.getText().toString());
									SPUtils.put(Fragment_4_Persoanl.this,
											"info", e_info.getText().toString());
									SPUtils.put(Fragment_4_Persoanl.this,
											"sex", e_sex.getText().toString());
									SPUtils.put(Fragment_4_Persoanl.this,
											"email", e_email.getText()
													.toString());
									//发送一个广播
									Intent intent = new Intent();
									intent.setAction("UPDATE_USER");
									sendBroadcast(intent);
								} else {
									ExampleUtil.showToast("更新资料失败请重试",
											Fragment_4_Persoanl.this);
								}
							} else {
								ExampleUtil.showToast("服务器繁忙",
										Fragment_4_Persoanl.this);
							}

						}

						@Override
						public void fail(String error) {
							ExampleUtil.showToast(error,
									Fragment_4_Persoanl.this);

						}
					});
		}

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
				head.setImageBitmap(BitmapFactory.decodeFile(path));
			}

		}
	}
}
