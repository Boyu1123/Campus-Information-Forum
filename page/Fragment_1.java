package com.ustb.page;

import java.util.ArrayList;

import com.ustb.adapter.ViewPagerFramentAdapter;
import com.ustb.school.R;
import com.ustb.status.NumberValue;
import com.ustb.status.StatusCode;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Fragment_1 extends Fragment implements OnClickListener {
	private ViewPager viewPager;
	private ViewPagerFramentAdapter adapter;
	private ArrayList<Fragment> list;
	private TextView t_1, t_2, t_line;
	private boolean line;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.from(getActivity()).inflate(R.layout.fragment_1,
				null);
		init(view);
		return view;
	}

	private void init(View view) {
		viewPager = (ViewPager) view.findViewById(R.id.viewpager1);
		list = new ArrayList<Fragment>();
		list.add(new Fragment_1_1());
		list.add(new Fragment_1_2());
		adapter = new ViewPagerFramentAdapter(getChildFragmentManager(), list);
		viewPager.setAdapter(adapter);
		viewPager.setOnPageChangeListener(listener);

		t_1 = (TextView) view.findViewById(R.id.fragment1_tongzhi);
		t_2 = (TextView) view.findViewById(R.id.fragment1_news);
		t_line = (TextView) view.findViewById(R.id.fragment1_line);
		t_1.setOnClickListener(this);
		t_2.setOnClickListener(this);
		t_1.setSelected(true);
	}

	/**
	 * 通过viewpager改变标示条的位置
	 */
	private OnPageChangeListener listener = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int arg0) {
			switch (arg0) {
			case 0:
				setLineChange(NumberValue.MOVEX, 0);
				line = false;
				break;

			case 1:
				setLineChange(0, NumberValue.MOVEX);
				line = true;
				break;
			}

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}
	};

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.fragment1_tongzhi:
			viewPager.setCurrentItem(0);
			if (line) {
				setLineChange(NumberValue.MOVEX, 0);
			}
			break;

		case R.id.fragment1_news:
			viewPager.setCurrentItem(1);
			if (!line) {
				setLineChange(0, NumberValue.MOVEX);
			}
			break;
		}

	}

	private void setLineChange(int x, int xx) {
		TranslateAnimation translateAnimation = new TranslateAnimation(x, xx,
				0, 0);
		translateAnimation.setDuration(150);
		translateAnimation.setFillAfter(true);
		t_line.startAnimation(translateAnimation);

	}

	@Override
	public void onDetach() {
		super.onDetach();
		try {
			java.lang.reflect.Field childFragmentManager = Fragment.class
					.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
