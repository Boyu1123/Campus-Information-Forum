package com.ustb.page;

import java.util.HashMap;
import java.util.Map;

import com.ustb.application.MyApplication;
import com.ustb.callback.Callback;
import com.ustb.entity.E_Activity;
import com.ustb.http.connection.HttpServer;
import com.ustb.map.Map_Show;
import com.ustb.model.BeanData;
import com.ustb.parser.ActivityParser;
import com.ustb.parser.JoinParser;
import com.ustb.school.R;
import com.ustb.school.R.layout;
import com.ustb.school.R.menu;
import com.ustb.status.StatusCode;
import com.ustb.url.URLConstants;
import com.ustb.utils.ExampleUtil;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class More_Activity extends Activity implements OnClickListener {
	private Button btn_back, btn_submit;
	private LinearLayout btn_tel;
	private TextView t_title, t_info, t_time, t_place, t_mess, t_author, t_tel,
			t_number;
	private E_Activity activity;
	private boolean clickflag = true;
	private MyApplication app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_activity);
		init();
	}

	private void init() {
		t_tel = (TextView) findViewById(R.id.activity_tel);
		t_title = (TextView) findViewById(R.id.activity_title);
		t_info = (TextView) findViewById(R.id.activity_info);
		t_time = (TextView) findViewById(R.id.activity_time);
		t_place = (TextView) findViewById(R.id.activity_place);
		t_mess = (TextView) findViewById(R.id.activity_mess);
		t_author = (TextView) findViewById(R.id.activity_author);
		t_number = (TextView) findViewById(R.id.activity_renshu);
		btn_submit = (Button) findViewById(R.id.activity_submit);
		btn_tel = (LinearLayout) findViewById(R.id.btn_tel);
		btn_back = (Button) findViewById(R.id.back);
		btn_back.setOnClickListener(this);
		btn_tel.setOnClickListener(this);

		app = (MyApplication) getApplication();
		activity = (E_Activity) getIntent().getSerializableExtra("activity");

		switch (activity.getAcode()) {
		case 0:
			// 结束活动
			btn_submit.setBackgroundColor(Color.parseColor("#808080"));
			btn_submit.setText("已结束");
			btn_submit.setOnClickListener(null);
			break;
		case 1:
			// 最新活动
			btn_submit.setOnClickListener(this);
			clickflag = true;
			break;
		case 2:
			// 最热活动
			btn_submit.setOnClickListener(this);
			clickflag = true;
			break;

		}

		t_title.setText(activity.getAtitle());
		t_info.setText(activity.getAinfo());
		t_time.setText(activity.getAstarttime());
		t_mess.setText(activity.getAmess());
		t_author.setText(activity.getUsername());
		t_number.setText(activity.getApeople() + "\\" + activity.getAhave());
		t_tel.setText(activity.getAtel());
		t_place.setText(activity.getAplace());
		if (t_place.getText().toString().contains("北京")) {
			t_place .getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
			t_place.setOnClickListener(this);
		}

		if (activity.getAhave() >= activity.getApeople()) {
			btn_submit.setText("活动人已满");
			btn_submit.setBackgroundColor(Color.parseColor("#808080"));
			btn_submit.setOnClickListener(null);
		}

		isJoin();
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.activity_submit:
			if (activity.getAhave() == activity.getApeople()) {

			} else {
				if (clickflag) {
					Intent intent = new Intent(More_Activity.this,
							Activity_Join.class);
					startActivityForResult(intent, 1);
				} else {
					outActivity();
				}
			}

			break;
		case R.id.btn_tel:
			call();
			break;
		case R.id.activity_place:
			Intent intent = new Intent(More_Activity.this, Map_Show.class);
			intent.putExtra("place", t_place.getText().toString());
			startActivity(intent);
			break;

		}

	}

	private void outActivity() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", "endActivity");
		params.put("aid", String.valueOf(activity.getAid()));
		HttpServer.setPostRequest(params, new ActivityParser(),
				URLConstants.BASEURL + URLConstants.ACTIVITYURL,
				new Callback() {

					@Override
					public void success(BeanData beanData) {
						if (beanData.getCode() == StatusCode.Common.SUCCESS) {
							if (beanData.getFlag() == StatusCode.Dao.UPDATE_SUCCESS) {
								btn_submit.setBackgroundColor(Color
										.parseColor("#808080"));
								btn_submit.setText("已结束");
								btn_submit.setOnClickListener(null);
							} else {
								ExampleUtil.showToast("申请失败",
										More_Activity.this);
							}
						} else {
							ExampleUtil.showToast("服务器繁忙", More_Activity.this);
						}

					}

					@Override
					public void fail(String error) {
						ExampleUtil.showToast(error, More_Activity.this);

					}
				});

	}

	private void call() {
		String phoneNumber = t_tel.getText().toString();
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_CALL);
		intent.setData(Uri.parse("tel:" + phoneNumber));
		startActivity(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		String joinname = data.getStringExtra("joinname");
		String jointel = data.getStringExtra("jointel");

		if (resultCode == 1) {
			if (joinname.equals("")) {
			} else {
				joinActivity(joinname, jointel);
			}

		}
		super.onActivityResult(requestCode, resultCode, data);

	}

	private void joinActivity(String joinname, String jointel) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", "joinActivity");
		params.put("joinname", joinname);
		params.put("jointel", jointel);
		params.put("userid", String.valueOf(app.getUser().getId()));
		params.put("aid", String.valueOf(activity.getAid()));

		HttpServer.setPostRequest(params, new JoinParser(),
				URLConstants.BASEURL + URLConstants.JOINURL, new Callback() {

					@Override
					public void success(BeanData beanData) {
						if (beanData.getCode() == StatusCode.Common.SUCCESS) {
							if (beanData.getFlag() == StatusCode.Dao.INSERT_SUCCESS) {
								btn_submit.setText("已参加");
								btn_submit.setOnClickListener(null);
								t_number.setText(activity.getApeople() + "\\"
										+ (activity.getAhave() + 1));
							} else {
								ExampleUtil.showToast("参加失败请重试",
										More_Activity.this);
							}
						} else {
							ExampleUtil.showToast("服务器繁忙", More_Activity.this);
						}

					}

					@Override
					public void fail(String error) {
						ExampleUtil.showToast(error, More_Activity.this);

					}
				});

	}

	private void isJoin() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", "isJoin");
		params.put("aid", String.valueOf(activity.getAid()));
		params.put("userid", String.valueOf(app.getUser().getId()));
		HttpServer.setPostRequest(params, new JoinParser(), URLConstants.BASEURL+URLConstants.JOINURL, new Callback() {
			
			@Override
			public void success(BeanData beanData) {
				if (beanData.getCode() == StatusCode.Common.SUCCESS) {
					if (beanData.getFlag() == StatusCode.Dao.UPDATE_SUCCESS) {
						btn_submit.setText("已参加");
						btn_submit.setOnClickListener(null);
					} else {
						
					}
				} else {
					ExampleUtil.showToast("服务器繁忙", More_Activity.this);
				}

				
			}
			
			@Override
			public void fail(String error) {
				ExampleUtil.showToast(error, More_Activity.this);
				
			}
		});

	}
}
