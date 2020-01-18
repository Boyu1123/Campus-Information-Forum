package com.ustb.adapter;

import java.util.ArrayList;

import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.ustb.entity.E_Activity;
import com.ustb.entity.E_Card;
import com.ustb.entity.E_Comment;
import com.ustb.entity.E_News;
import com.ustb.entity.E_Notice;
import com.ustb.entity.E_User;
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

public class CommentListViewAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<E_Comment> list;

	public CommentListViewAdapter(Context context, ArrayList<E_Comment> list) {
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
			arg1 = LayoutInflater.from(context).inflate(R.layout.item_commentlist,
					null);
			vh = new ViewHolder();
			vh.t_name = (TextView) arg1.findViewById(R.id.comment_author);
			vh.t_mess = (TextView) arg1.findViewById(R.id.comment_mess);
			vh.i_head = (ImageView) arg1.findViewById(R.id.comment_img);
			arg1.setTag(vh);
		} else {
			vh = (ViewHolder) arg1.getTag();
		}
		vh.t_name.setText(list.get(arg0).getUname());
		vh.t_mess.setText(list.get(arg0).getCmess());
		

		ImageUtils.getBitmapUtils(context).display(vh.i_head,
				URLConstants.BASEURL + list.get(arg0).getUhead(),
				new BitmapLoadCallBack<View>() {

					@Override
					public void onLoadCompleted(View arg0, String arg1,
							Bitmap arg2, BitmapDisplayConfig arg3,
							BitmapLoadFrom arg4) {
						Bitmap bitmap = ImageUtils.circleBitmap(arg2);
						vh.i_head.setImageBitmap(bitmap);
						vh.i_head.invalidate();
					}

					@Override
					public void onLoadFailed(View arg0, String arg1,
							Drawable arg2) {
						vh.i_head.setImageResource(R.drawable.head04);

					}
				});

		return arg1;
	}

	class ViewHolder {
		TextView t_name;
		TextView t_mess;
		ImageView i_head;
	}
}
