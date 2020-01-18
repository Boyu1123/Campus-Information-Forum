package com.ustb.adapter;

import java.util.ArrayList;

import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.ustb.entity.E_Activity;
import com.ustb.entity.E_Card;
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

public class ActivityListViewAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<E_Activity> list;

	public ActivityListViewAdapter(Context context, ArrayList<E_Activity> list) {
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
			arg1 = LayoutInflater.from(context).inflate(R.layout.item_activity,
					null);
			vh = new ViewHolder();
			vh.t_title = (TextView) arg1.findViewById(R.id.activity_title);
			vh.t_info = (TextView) arg1.findViewById(R.id.activity_info);
			vh.t_author = (TextView) arg1.findViewById(R.id.activity_author);
			vh.t_time = (TextView) arg1.findViewById(R.id.activity_time);
			arg1.setTag(vh);
		} else {
			vh = (ViewHolder) arg1.getTag();
		}

		vh.t_title.setText(list.get(arg0).getAtitle());
		vh.t_info.setText(list.get(arg0).getAinfo());
		vh.t_author.setText(list.get(arg0).getUsername());
		vh.t_time.setText(list.get(arg0).getAstarttime());

		return arg1;
	}

	class ViewHolder {
		TextView t_title;
		TextView t_info;
		TextView t_author;
		TextView t_time;
	}
}
