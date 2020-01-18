package com.ustb.page;

import android.os.Bundle;

import com.ustb.school.R;
import com.ustb.school.R.id;
import com.ustb.school.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

public class Fragment_4_Set extends Activity implements OnClickListener {
	private Button btn_back;
	private RelativeLayout r_about,r_changePass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_4_set);
		init();
	}

	private void init() {
		btn_back = (Button) findViewById(R.id.back);
		btn_back.setOnClickListener(this);

		r_about = (RelativeLayout) findViewById(R.id.set_aboutus);
		r_about.setOnClickListener(this);
		
		r_changePass = (RelativeLayout) findViewById(R.id.set_changePass);
		r_changePass.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.set_aboutus:
			Intent intent = new Intent(Fragment_4_Set.this, Other_Aboutus.class);
			startActivity(intent);
			break;
		case R.id.set_changePass:
			Intent intent2 = new Intent(Fragment_4_Set.this, Other_ChangePass.class);
			startActivity(intent2);
			break;
		}

	}

}
