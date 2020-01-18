package com.ustb.page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.ustb.adapter.F12ListViewAdapter;
import com.ustb.callback.Callback;
import com.ustb.entity.E_News;
import com.ustb.entity.E_Notice;
import com.ustb.http.connection.HttpServer;
import com.ustb.model.BeanData;
import com.ustb.model.NewsData;
import com.ustb.parser.NewsParser;
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
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class Fragment_1_2 extends Fragment {
	private ListView listView;
	private ArrayList<E_News> list;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.from(getActivity()).inflate(R.layout.fragment_1_2,
				null);
		init(view);
		return view;
	}

	private void init(View view) {
//		E_News news = new E_News();
//		news.setNewstitle("新闻一");
//		news.setNewsmess("今天吉星高照今天吉星高照今天吉星高照今天吉星高照今天吉星高照今天吉星高照今天吉星高照今天吉星高照今天吉星高照今天吉星高照今天吉星高照");
//		news.setNewsauthor("王晓霞");
//		E_News news1 = new E_News();
//		news1.setNewstitle("新闻二");
//		news1.setNewsmess("吉星高照");
//		news1.setNewsauthor("王霞");

		list = new ArrayList<E_News>();
//		list.add(news1);
//		list.add(news);
		listView = (ListView) view.findViewById(R.id.f12_listview);
//		F12ListViewAdapter adapter = new F12ListViewAdapter(getActivity(), list);
//		listView.setAdapter(adapter);
		listView.setOnItemClickListener(listener);
		getNews();
	}

	private void getNews() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", "getNews");
		HttpServer.setPostRequest(params, new NewsParser(),
				URLConstants.BASEURL + URLConstants.NEWSURL, new Callback() {

					@Override
					public void success(BeanData beanData) {
						NewsData newsData = (NewsData) beanData;
						if (newsData.getCode() == StatusCode.Common.SUCCESS) {
							if (newsData.getFlag() == StatusCode.Dao.SELECT_SUCCESS) {
								list = newsData.getList();
								F12ListViewAdapter adapter = new F12ListViewAdapter(getActivity(), list);
								listView.setAdapter(adapter);
								
							} else {
								ExampleUtil.showToast("获取新闻失败", getActivity());
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
			E_News news = list.get(arg2);
			Intent intent = new Intent(getActivity(), More_News.class);
			intent.putExtra("news", news);
			startActivity(intent);
		}
	};
}
