package com.ustb.page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.ustb.adapter.F21ListViewAdapter;
import com.ustb.application.MyApplication;
import com.ustb.callback.Callback;
import com.ustb.entity.E_Card;
import com.ustb.http.connection.HttpServer;
import com.ustb.model.BeanData;
import com.ustb.model.CollectData;
import com.ustb.parser.CollectParser;
import com.ustb.school.R;
import com.ustb.school.R.id;
import com.ustb.school.R.layout;
import com.ustb.status.StatusCode;
import com.ustb.url.URLConstants;
import com.ustb.utils.ExampleUtil;

import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class Fragment_4_Collect extends Activity implements OnClickListener {
	private Button btn_back;
	private ListView listView;
	private ArrayList<E_Card> list;
	private MyApplication app;
	private MyBorad myBorad;
	private F21ListViewAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_4_collect);
		init();
	}

	private void init() {
		btn_back = (Button) findViewById(R.id.back);
		btn_back.setOnClickListener(this);
		listView = (ListView) findViewById(R.id.f4_collect_listview);
		listView.setOnItemClickListener(listener);
		list = new ArrayList<E_Card>();
		app = (MyApplication) getApplication();
		getMyCollect();
		
		myBorad = new MyBorad();
		registerReceiver(myBorad, new IntentFilter("UPDATE_COLLECT"));
	}

	private void getMyCollect() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", "MyCollect");
		params.put("userid", String.valueOf(app.getUser().getId()));
		HttpServer.setPostRequest(params, new CollectParser(), URLConstants.BASEURL+URLConstants.COLLECTURL, new Callback() {
			
			@Override
			public void success(BeanData beanData) {
				CollectData collectData = (CollectData) beanData;
				if (collectData.getCode()==StatusCode.Common.SUCCESS) {
					if (collectData.getFlag()==StatusCode.Dao.UPDATE_SUCCESS) {
						list = collectData.getList();
						adapter = new F21ListViewAdapter(Fragment_4_Collect.this, list);
						listView.setAdapter(adapter);
						
					}else {
						ExampleUtil.showToast("获取收藏列表失败", Fragment_4_Collect.this);
					}
				}else {
					ExampleUtil.showToast("服务器繁忙", Fragment_4_Collect.this);
				}
				
			}
			
			@Override
			public void fail(String error) {
				ExampleUtil.showToast(error, Fragment_4_Collect.this);
				
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
			E_Card card = list.get(arg2);
			Intent intent = new Intent(Fragment_4_Collect.this, More_Card.class);
			intent.putExtra("card", card);
			intent.putExtra("sid", arg2);
			startActivity(intent);
		}
	};
	
	class MyBorad extends BroadcastReceiver{

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			int index = arg1.getIntExtra("index", 100);
			list.remove(index);
			adapter.notifyDataSetChanged();
		}
		
	}
	
	@Override
	protected void onDestroy() {
		unregisterReceiver(myBorad);
		super.onDestroy();
	}

}
