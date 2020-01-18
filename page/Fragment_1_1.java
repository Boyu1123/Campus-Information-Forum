package com.ustb.page;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.ustb.adapter.F11ListViewAdapter;
import com.ustb.adapter.ViewPagerAdapter;
import com.ustb.baseutils.ImageHandler;
import com.ustb.baseutils.MyViewPager;
import com.ustb.callback.Callback;
import com.ustb.entity.E_Notice;
import com.ustb.http.connection.HttpServer;
import com.ustb.model.BeanData;
import com.ustb.model.NoticeData;
import com.ustb.parser.NoticeParser;
import com.ustb.school.R;
import com.ustb.status.StatusCode;
import com.ustb.url.URLConstants;
import com.ustb.utils.ExampleUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

public class Fragment_1_1 extends Fragment {
	public MyViewPager viewPager;
	public ImageHandler handler = new ImageHandler(new WeakReference<Fragment>(
			this));
	private ListView listView;
	private ArrayList<E_Notice> list;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.from(getActivity()).inflate(R.layout.fragment_1_1,
				null);
		init(view);
		return view;
	}

	private void init(View view) {
		viewPager = (MyViewPager) view.findViewById(R.id.fragment11_viewpager);
		addViewToViewpager();

		listView = (ListView) view.findViewById(R.id.fragment11_listview);
		list = new ArrayList<E_Notice>();
		E_Notice notice = new E_Notice();
		notice.setNtitle("计算机设计大赛");
		notice.setNmess("由教育部高等学校计算机类专业教学指导委员会、教育部高等学校软件工程专业教学指导委员会主办。");
		notice.setNauthor("ustb");
		list.add(notice);
		E_Notice notice1 = new E_Notice();
		notice1.setNtitle("校庆");
		notice1.setNmess("蓟门巍巍，满井苍苍。今年是北京科技大学建校65周年。");
		notice1.setNauthor("ustb");
		list.add(notice1);
		F11ListViewAdapter adapter = new F11ListViewAdapter(getActivity(), list);
		listView.setAdapter(adapter);
		//listView.setOnItemClickListener(listener);
		//addNotice();
	}

	private void addNotice() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", "getNotice");
		HttpServer.setPostRequest(params, new NoticeParser(),
				URLConstants.BASEURL + URLConstants.NOTICEURL, new Callback() {

					@Override
					public void success(BeanData beanData) {
						NoticeData data = (NoticeData) beanData;
						if (data.getCode() == StatusCode.Common.SUCCESS) {
							if (data.getFlag() == StatusCode.Dao.SELECT_SUCCESS) {
								list = data.getList();
								F11ListViewAdapter adapter = new F11ListViewAdapter(
										getActivity(), list);
								listView.setAdapter(adapter);
								listView.setOnItemClickListener(listener);
							} else {
								ExampleUtil.showToast("获取失败请重试", getActivity());
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
			E_Notice notice = list.get(arg2);
			Intent intent = new Intent(getActivity(), More_Notice.class);
			// Bundle bundle = new Bundle();
			// bundle.putSerializable("notice", notice);
			intent.putExtra("notice", notice);
			// intent.putExtras(bundle);
			startActivity(intent);
		}
	};

	/**
	 * viewpager工作设置
	 */
	private void addViewToViewpager() {
		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.item_f11_viewpager, null);
		ImageView imageView = (ImageView) view.findViewById(R.id.viewpager_img);
		imageView.setImageResource(R.drawable.home01);

		View view2 = LayoutInflater.from(getActivity()).inflate(
				R.layout.item_f11_viewpager, null);
		ImageView imageView2 = (ImageView) view2
				.findViewById(R.id.viewpager_img);
		imageView2.setImageResource(R.drawable.home02);

		View view3 = LayoutInflater.from(getActivity()).inflate(
				R.layout.item_f11_viewpager, null);
		ImageView imageView3 = (ImageView) view3
				.findViewById(R.id.viewpager_img);
		imageView3.setImageResource(R.drawable.home03);

		ArrayList<View> listView = new ArrayList<View>();
		listView.add(view);
		listView.add(view2);
		listView.add(view3);

		viewPager.setAdapter(new ViewPagerAdapter(listView));
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			// 配合Adapter的currentItem字段进行设置。
			@Override
			public void onPageSelected(int arg0) {

				handler.sendMessage(Message.obtain(handler,
						ImageHandler.MSG_PAGE_CHANGED, arg0, 0));
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			// 覆写该方法实现轮播效果的暂停和恢复
			@Override
			public void onPageScrollStateChanged(int arg0) {
				switch (arg0) {
				case ViewPager.SCROLL_STATE_DRAGGING:
					handler.sendEmptyMessage(ImageHandler.MSG_KEEP_SILENT);
					break;
				case ViewPager.SCROLL_STATE_IDLE:
					handler.sendEmptyMessageDelayed(
							ImageHandler.MSG_UPDATE_IMAGE,
							ImageHandler.MSG_DELAY);
					break;
				default:
					break;
				}
			}
		});

		// 开始轮播效果
		handler.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE,
				ImageHandler.MSG_DELAY);
	}
}
