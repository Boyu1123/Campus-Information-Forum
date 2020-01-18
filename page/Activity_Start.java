package com.ustb.page;

import java.util.HashMap;
import java.util.Map;

import com.ustb.application.MyApplication;
import com.ustb.callback.Callback;
import com.ustb.http.connection.HttpServer;
import com.ustb.model.ActivityData;
import com.ustb.model.BeanData;
import com.ustb.parser.ActivityParser;
import com.ustb.school.R;
import com.ustb.status.StatusCode;
import com.ustb.url.URLConstants;
import com.ustb.utils.ExampleUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Activity_Start extends Activity implements OnClickListener {
	private Button btn_back, btn_submit;
	private EditText e_title, e_info, e_time, e_place, e_people, e_mess,e_tel;
	private MyApplication app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		init();
	}

	private void init() {
		e_tel = (EditText) findViewById(R.id.a_tel);
		e_title = (EditText) findViewById(R.id.a_title);
		e_info = (EditText) findViewById(R.id.a_info);
		e_time = (EditText) findViewById(R.id.a_time);
		e_place = (EditText) findViewById(R.id.a_place);
		e_people = (EditText) findViewById(R.id.a_people);
		e_mess = (EditText) findViewById(R.id.a_mess);
		btn_submit = (Button) findViewById(R.id.a_submit);
		btn_back = (Button) findViewById(R.id.back);
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
		case R.id.a_submit:
			addAcitvity();
			break;

		}

	}

	private void addAcitvity() {
		String title = e_title.getText().toString();
		String info = e_info.getText().toString();
		String time = e_time.getText().toString();
		String place = e_place.getText().toString();
		String people = e_people.getText().toString();
		String mess = e_mess.getText().toString();
		String tel = e_tel.getText().toString();
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", "addActivity");
		params.put("title", title);
		params.put("info", info);
		params.put("time", time);
		params.put("place", place);
		params.put("people", people);
		params.put("mess", mess);
		params.put("tel", tel);
		params.put("userid", String.valueOf(app.getUser().getId()));

		HttpServer.setPostRequest(params, new ActivityParser(),
				URLConstants.BASEURL + URLConstants.ACTIVITYURL,
				new Callback() {

					@Override
					public void success(BeanData beanData) {
						ActivityData activityData = (ActivityData) beanData;
						if (activityData.getCode() == StatusCode.Common.SUCCESS) {
							if (activityData.getFlag() == StatusCode.Dao.INSERT_SUCCESS) {
								ExampleUtil.showToast("发布活动成功",
										Activity_Start.this);
//								e_title.setText("");
//								e_info.setText("");
//								e_time.setText("");
//								e_place.setText("");
//								e_people.setText("");
//								e_mess.setText("");
								finish();
							} else {
								ExampleUtil.showToast("发布活动失败",
										Activity_Start.this);
							}
						} else {
							ExampleUtil.showToast("服务器繁忙", Activity_Start.this);
						}

					}

					@Override
					public void fail(String error) {
						ExampleUtil.showToast(error, Activity_Start.this);

					}
				});

	}
}
