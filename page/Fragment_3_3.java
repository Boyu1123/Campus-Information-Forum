package com.ustb.page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.ustb.adapter.F33ListViewAdapter;
import com.ustb.callback.Callback;
import com.ustb.entity.E_Card;
import com.ustb.entity.E_News;
import com.ustb.http.connection.HttpServer;
import com.ustb.model.BeanData;
import com.ustb.model.CardData;
import com.ustb.parser.CardParser;
import com.ustb.school.R;
import com.ustb.status.StatusCode;
import com.ustb.url.URLConstants;
import com.ustb.utils.ExampleUtil;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class Fragment_3_3 extends Fragment {
	private ListView listView;
	private ArrayList<E_Card> list;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.from(getActivity()).inflate(R.layout.fragment_3_3,
				null);
//		ImageView imageView = (ImageView) view.findViewById(R.id.viewpager_img);
//		imageView.setImageResource(R.drawable.shop);
		init(view);
		return view;
	}

	private void init(View view) {
		list = new ArrayList<E_Card>();
		listView = (ListView) view.findViewById(R.id.f33_listview);
		listView.setOnItemClickListener(listener);
		getCard();
	}

	private void getCard() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", "getCard");
		HttpServer.setPostRequest(params, new CardParser(),
				URLConstants.BASEURL + URLConstants.CARDURL, new Callback() {

					@Override
					public void success(BeanData beanData) {
						CardData data = (CardData) beanData;
						if (data.getCode() == StatusCode.Common.SUCCESS) {
							if (data.getFlag() == StatusCode.Dao.SELECT_SUCCESS) {
								list = data.getList();
								F33ListViewAdapter adapter = new F33ListViewAdapter(getActivity(), list);
								listView.setAdapter(adapter);
								
							} else {
								ExampleUtil.showToast("获取帖子信息失败", getActivity());
							}
						} else {
							ExampleUtil.showToast("服务器繁忙", getActivity());
						}

					}

					@Override
					public void fail(String error) {
						ExampleUtil.showToast(error, getActivity());

					}
				});
	}

	private OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			E_Card card = list.get(arg2);
			Intent intent = new Intent(getActivity(), More_Card.class);
			intent.putExtra("card", card);
			startActivity(intent);
		}
	};
}
