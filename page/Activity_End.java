package com.ustb.page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.ustb.adapter.ActivityListViewAdapter;
import com.ustb.callback.Callback;
import com.ustb.entity.E_Activity;
import com.ustb.http.connection.HttpServer;
import com.ustb.model.ActivityData;
import com.ustb.model.BeanData;
import com.ustb.parser.ActivityParser;
import com.ustb.school.R;
import com.ustb.status.StatusCode;
import com.ustb.url.URLConstants;
import com.ustb.utils.ExampleUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class Activity_End extends Activity implements OnClickListener {
	private Button btn_back;
	private ListView listView;
	private ArrayList<E_Activity> list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_end);
		init();
	}

	private void init() {
		btn_back = (Button) findViewById(R.id.back);
		btn_back.setOnClickListener(this);
		listView = (ListView) findViewById(R.id.end_listview);
		list = new ArrayList<E_Activity>();
		listView.setOnItemClickListener(listener);
		getEndActivity();
		
	}

	private void getEndActivity() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", "getActivity");
		params.put("acode", "0");
		HttpServer.setPostRequest(params, new ActivityParser(),
				URLConstants.BASEURL + URLConstants.ACTIVITYURL,
				new Callback() {

					@Override
					public void success(BeanData beanData) {
						ActivityData activityData = (ActivityData) beanData;
						if (activityData.getCode() == StatusCode.Common.SUCCESS) {
							if (activityData.getFlag() == StatusCode.Dao.SELECT_SUCCESS) {
								list = activityData.getList();
								ActivityListViewAdapter adapter = new ActivityListViewAdapter(
										Activity_End.this, list);
								listView.setAdapter(adapter);							} else {
								ExampleUtil.showToast("获取活动失败，请重试",
										Activity_End.this);
							}
						} else {
							ExampleUtil.showToast("服务器繁忙", Activity_End.this);
						}

					}

					@Override
					public void fail(String error) {
						ExampleUtil.showToast(error, Activity_End.this);

					}
				});

	}
	
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.back:
			finish();
			break;

		}

	}
	
	private OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			E_Activity activity= list.get(arg2);
			Intent intent = new Intent(Activity_End.this, More_Activity.class);
			intent.putExtra("activity", activity);
			startActivity(intent);
		}
	};
}
