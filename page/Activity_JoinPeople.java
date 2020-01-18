package com.ustb.page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.ustb.adapter.JoinPeopleListViewAdapter;
import com.ustb.callback.Callback;
import com.ustb.entity.E_Join;
import com.ustb.http.connection.HttpServer;
import com.ustb.model.BeanData;
import com.ustb.model.JoinData;
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
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class Activity_JoinPeople extends Activity implements OnClickListener {
	private Button btn_back;
	private ListView listView;
	private ArrayList<E_Join> list;
	private int aid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_joinpeople);
		init();
	}

	private void init() {
		btn_back = (Button) findViewById(R.id.back);
		listView = (ListView) findViewById(R.id.join_list);
		listView.setOnItemClickListener(listener);
		list = new ArrayList<E_Join>();
		btn_back.setOnClickListener(this);

		aid = getIntent().getIntExtra("aid", 0);
		getPeople();
	}

	private void getPeople() {
		if (aid!=0) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("type", "getJoin");
			params.put("aid", String.valueOf(aid));
			HttpServer.setPostRequest(params, new JoinParser(), URLConstants.BASEURL+URLConstants.JOINURL, new Callback() {
				
				@Override
				public void success(BeanData beanData) {
					JoinData joinData = (JoinData) beanData;
					if (joinData.getCode()==StatusCode.Common.SUCCESS) {
						if (joinData.getFlag()==StatusCode.Dao.SELECT_SUCCESS) {
							list = joinData.getList();
							JoinPeopleListViewAdapter adapter = new JoinPeopleListViewAdapter(Activity_JoinPeople.this, list);
							listView.setAdapter(adapter);
						}else {
							ExampleUtil.showToast("获取列表超时", Activity_JoinPeople.this);
						}
					}else {
						ExampleUtil.showToast("服务器繁忙", Activity_JoinPeople.this);
					}
					
				}
				
				@Override
				public void fail(String error) {
					ExampleUtil.showToast(error, Activity_JoinPeople.this);
					
				}
			});
		}

	}

	@Override
	public void onClick(View arg0) {
		finish();

	}

	private OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			String phoneNumber = list.get(arg2).getJtel();
			call(phoneNumber);

		}
	};
	
	private void call(String phoneNumber) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_CALL);
		intent.setData(Uri.parse("tel:" + phoneNumber));
		startActivity(intent);
	}

}
