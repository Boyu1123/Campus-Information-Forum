package com.ustb.page;

import android.os.Bundle;

import com.ustb.school.R;
import com.ustb.school.R.id;
import com.ustb.school.R.layout;

import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class Fragment_4_Comment extends Activity implements OnClickListener {
	private Button btn_back;
	private ListView listView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_4_comment);
		init();
	}

	private void init() {
		btn_back = (Button) findViewById(R.id.back);
		btn_back.setOnClickListener(this);

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.back:
			finish();
			break;

		}

	}

}
