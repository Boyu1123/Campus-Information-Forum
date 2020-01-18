package com.ustb.page;

import android.os.Bundle;

import com.ustb.school.R;
import com.ustb.school.R.layout;
import com.ustb.school.R.menu;

import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Other_Aboutus extends Activity implements OnClickListener{
	private TextView t_mess;
	private Button btn_back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.other_aboutus);
		init();
	}
	private void init() {
		btn_back = (Button) findViewById(R.id.back);
		btn_back.setOnClickListener(this);
		t_mess = (TextView) findViewById(R.id.about_text);
		t_mess.setText("USTB 2017");
		
	}
	@Override
	public void onClick(View arg0) {
		finish();
		
	}

	

}
