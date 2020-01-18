package com.ustb.adapter;

import java.util.ArrayList;

import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.ustb.entity.E_Activity;
import com.ustb.entity.E_Card;
import com.ustb.entity.E_Join;
import com.ustb.entity.E_News;
import com.ustb.entity.E_Notice;
import com.ustb.school.R;
import com.ustb.url.URLConstants;
import com.ustb.utils.ImageUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class JoinPeopleListViewAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<E_Join> list;

	public JoinPeopleListViewAdapter(Context context, ArrayList<E_Join> list) {
		super();
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder vh;
		if (arg1 == null) {
			arg1 = LayoutInflater.from(context).inflate(R.layout.item_joinpeople,
					null);
			vh = new ViewHolder();
			vh.t_name = (TextView) arg1.findViewById(R.id.itemjoin_name);
			vh.t_tel = (TextView) arg1.findViewById(R.id.itemjoin_tel);
			arg1.setTag(vh);
		} else {
			vh = (ViewHolder) arg1.getTag();
		}

		vh.t_name.setText(list.get(arg0).getJname());
		vh.t_tel.setText(list.get(arg0).getJtel());
		
		return arg1;
	}

	class ViewHolder {
		TextView t_name;
		TextView t_tel;
		
	}
}
