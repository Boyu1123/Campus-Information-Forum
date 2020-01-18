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
import com.ustb.model.CardData;
import com.ustb.parser.CardParser;
import com.ustb.school.R;
import com.ustb.school.R.layout;
import com.ustb.school.R.menu;
import com.ustb.status.StatusCode;
import com.ustb.url.URLConstants;
import com.ustb.utils.ExampleUtil;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class Fragment_4_Tianzi extends Activity implements OnClickListener {
	private Button btn_back;
	private ListView listView;
	private ArrayList<E_Card> list;
	private MyApplication app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_4_tianzi);
		init();
	}

	private void init() {
		app = (MyApplication) getApplication();

		btn_back = (Button) findViewById(R.id.back);
		btn_back.setOnClickListener(this);
		listView = (ListView) findViewById(R.id.f4_card_listview);
		list = new ArrayList<E_Card>();
		listView.setOnItemClickListener(listener);
		
		getMyCard();
	}

	private void getMyCard() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", "getMyCard");
		int userid = app.getUser().getId();
		params.put("userid", String.valueOf(userid));
		HttpServer.setPostRequest(params, new CardParser(),
				URLConstants.BASEURL + URLConstants.CARDURL, new Callback() {

					@Override
					public void success(BeanData beanData) {
						CardData cardData = (CardData) beanData;
						if (cardData.getCode() == StatusCode.Common.SUCCESS) {
							if (cardData.getFlag() == StatusCode.Dao.SELECT_SUCCESS) {
								list = cardData.getList();
								F21ListViewAdapter adapter = new F21ListViewAdapter(
										Fragment_4_Tianzi.this, list);
								listView.setAdapter(adapter);
							} else {
								ExampleUtil.showToast("获取信息失败，请重试",
										Fragment_4_Tianzi.this);
							}
						} else {
							ExampleUtil.showToast("服务器繁忙",
									Fragment_4_Tianzi.this);
						}

					}

					@Override
					public void fail(String error) {
						ExampleUtil.showToast(error, Fragment_4_Tianzi.this);

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
			Intent intent = new Intent(Fragment_4_Tianzi.this, More_Card.class);
			intent.putExtra("card", card);
			startActivity(intent);
		}
	};
}
