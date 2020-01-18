package com.ustb.page;

import java.util.HashMap;
import java.util.Map;

import com.ustb.callback.Callback;
import com.ustb.entity.E_Activity;
import com.ustb.http.connection.HttpServer;
import com.ustb.model.BeanData;
import com.ustb.parser.ActivityParser;
import com.ustb.school.R;
import com.ustb.school.R.layout;
import com.ustb.school.R.menu;
import com.ustb.status.StatusCode;
import com.ustb.url.URLConstants;
import com.ustb.utils.ExampleUtil;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class More_ActivityPersonal extends Activity implements OnClickListener {
	private Button btn_back, btn_submit,btn_join;
	private LinearLayout btn_tel;
	private TextView t_title, t_info, t_time, t_place, t_mess, t_author, t_tel,
			t_number;
	private E_Activity activity;
	private int activitycode;
	private boolean clickflag = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_activitypersonal);
		init();
	}

	private void init() {
		t_tel = (TextView) findViewById(R.id.activitypersonal_tel);
		t_title = (TextView) findViewById(R.id.activitypersonal_title);
		t_info = (TextView) findViewById(R.id.activitypersonal_info);
		t_time = (TextView) findViewById(R.id.activitypersonal_time);
		t_place = (TextView) findViewById(R.id.activitypersonal_place);
		t_mess = (TextView) findViewById(R.id.activitypersonal_mess);
		t_author = (TextView) findViewById(R.id.activitypersonal_author);
		t_number = (TextView) findViewById(R.id.activitypersonal_renshu);
		btn_submit = (Button) findViewById(R.id.activitypersonal_submit);
		btn_tel = (LinearLayout) findViewById(R.id.btnpersonal_tel);
		btn_back = (Button) findViewById(R.id.back);
		btn_back.setOnClickListener(this);
		btn_tel.setOnClickListener(this);
		btn_submit.setOnClickListener(this);
		btn_join = (Button) findViewById(R.id.activitypersonal_join);
		btn_join.setOnClickListener(this);
		activity = (E_Activity) getIntent().getSerializableExtra("activity");

		activitycode = activity.getAcode();
		switch (activitycode) {
		case 0:
			// 结束活动
			btn_submit.setBackgroundColor(Color.parseColor("#808080"));
			btn_submit.setText("已结束");
			clickflag = false;
			break;
		case 1:
			// 最新活动
			clickflag = true;
			break;
		case 2:
			// 最新活动
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
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.activitypersonal_submit:
			if (clickflag) {
				new AlertDialog.Builder(More_ActivityPersonal.this).setTitle("温馨提示")//设置对话框标题  
			     .setMessage("确定要结束活动吗？")//设置显示的内容  
			     .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮  
			         @Override  
			         public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件  
			        	 endActivity();
			         }  
			  
			     }).setNegativeButton("返回",new DialogInterface.OnClickListener() {//添加返回按钮  
			  
			         @Override  
			         public void onClick(DialogInterface dialog, int which) {//响应事件  
			             // TODO Auto-generated method stub  
			         }  
			  
			     }).show();//在按键响应事件中显示此对话框  
				
			}
			break;
		case R.id.btn_tel:
			call();
			break;
		case R.id.activitypersonal_join:
			Intent intent = new Intent(More_ActivityPersonal.this, Activity_JoinPeople.class);
			intent.putExtra("aid", activity.getAid());
			startActivity(intent);
			break;

		}

	}

	private void endActivity() {
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
								clickflag = false;
							} else {
								ExampleUtil.showToast("申请失败",
										More_ActivityPersonal.this);
							}
						} else {
							ExampleUtil.showToast("服务器繁忙",
									More_ActivityPersonal.this);
						}

					}

					@Override
					public void fail(String error) {
						ExampleUtil
								.showToast(error, More_ActivityPersonal.this);

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

}
