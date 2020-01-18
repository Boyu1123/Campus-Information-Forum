package com.ustb.page;

import com.ustb.school.R;
import com.ustb.school.R.layout;
import com.ustb.school.R.menu;
import com.ustb.utils.ExampleUtil;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Activity_Join extends Activity implements OnClickListener {
	private Button btn_back, btn_submit;
	private EditText e_name, e_tel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_join);
		init();
	}

	private void init() {
		btn_back = (Button) findViewById(R.id.back);
		btn_submit = (Button) findViewById(R.id.join_submit);
		e_name = (EditText) findViewById(R.id.join_name);
		e_tel = (EditText) findViewById(R.id.join_tel);
		btn_back.setOnClickListener(this);
		btn_submit.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.back:
			Intent mIntent1 = new Intent();
			mIntent1.putExtra("joinname","");
			mIntent1.putExtra("jointel","");
			// 设置结果，并进行传送
			this.setResult(1, mIntent1);
			finish();
			break;
		case R.id.join_submit:
			String name =  e_name.getText().toString();
			String tel = e_tel.getText().toString();
			if (ExampleUtil.isEmpty(name)||ExampleUtil.isEmpty(tel)) {
				ExampleUtil.showToast("请填写完整信息", Activity_Join.this);
			}else {
				Intent mIntent = new Intent();
				mIntent.putExtra("joinname",name);
				mIntent.putExtra("jointel", tel);
				// 设置结果，并进行传送
				this.setResult(1, mIntent);
				finish();
			}
			break;

		default:
			break;
		}

	}

}
