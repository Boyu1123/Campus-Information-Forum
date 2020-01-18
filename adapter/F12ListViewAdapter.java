package com.ustb.adapter;

import java.util.ArrayList;

import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.ustb.entity.E_News;
import com.ustb.entity.E_Notice;
import com.ustb.school.R;
import com.ustb.url.URLConstants;
import com.ustb.utils.ImageUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class F12ListViewAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<E_News> list;

	public F12ListViewAdapter(Context context, ArrayList<E_News> list) {
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
		final ViewHolder vh;
		if (arg1 == null) {
			arg1 = LayoutInflater.from(context).inflate(R.layout.item_f12list,
					null);
			vh = new ViewHolder();
			vh.imageView = (ImageView) arg1.findViewById(R.id.f11_img);
			vh.t_title = (TextView) arg1.findViewById(R.id.f11_title);
			vh.t_mess = (TextView) arg1.findViewById(R.id.f11_mess);
			arg1.setTag(vh);
		} else {
			vh = (ViewHolder) arg1.getTag();
		}

		vh.t_title.setText(list.get(arg0).getNewstitle());
		vh.t_mess.setText(list.get(arg0).getNewsmess());
		vh.imageView.setImageResource(R.drawable.g1);

		ImageUtils.getBitmapUtils(context).display(vh.imageView,
				URLConstants.BASEURL + list.get(arg0).getNewsphoto1(),
				new BitmapLoadCallBack<View>() {

					@Override
					public void onLoadCompleted(View arg0, String arg1,
							Bitmap arg2, BitmapDisplayConfig arg3,
							BitmapLoadFrom arg4) {

						vh.imageView.setImageBitmap(arg2);

					}

					@Override
					public void onLoadFailed(View arg0, String arg1,
							Drawable arg2) {
						vh.imageView.setImageResource(R.drawable.g1);

					}
				});

		return arg1;
	}

	class ViewHolder {
		ImageView imageView;
		TextView t_title;
		TextView t_mess;
	}
}
