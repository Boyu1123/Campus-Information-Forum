package com.ustb.page;

import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.ustb.entity.E_News;
import com.ustb.entity.E_Notice;
import com.ustb.school.R;
import com.ustb.school.R.layout;
import com.ustb.school.R.menu;
import com.ustb.url.URLConstants;
import com.ustb.utils.ImageUtils;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class More_News extends Activity implements OnClickListener {
	private Button btn_back;
	private TextView t_title, t_author, t_time, t_mess;
	private ImageView i_photo1, i_photo2, i_collect;
	private boolean isCollect = false;
	private E_News news;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_news);
		init();
	}

	private void init() {
		t_title = (TextView) findViewById(R.id.news_title);
		t_author = (TextView) findViewById(R.id.news_author);
		t_time = (TextView) findViewById(R.id.news_time);
		t_mess = (TextView) findViewById(R.id.news_mess);
		i_photo1 = (ImageView) findViewById(R.id.news_photo1);
//		i_photo2 = (ImageView) findViewById(R.id.news_photo2);
		i_collect = (ImageView) findViewById(R.id.news_collect);
		btn_back = (Button) findViewById(R.id.back);
		btn_back.setOnClickListener(this);
		i_collect.setOnClickListener(this);

		news = (E_News) getIntent().getSerializableExtra("news");
		t_title.setText(news.getNewstitle());
		t_author.setText(news.getNewsauthor());
		t_mess.setText(news.getNewsmess());
		t_time.setText(news.getNewsdate());

		ImageUtils.getBitmapUtils(this).display(i_photo1,
				URLConstants.BASEURL + news.getNewsphoto1(),
				new BitmapLoadCallBack<View>() {

					@Override
					public void onLoadCompleted(View arg0, String arg1,
							Bitmap arg2, BitmapDisplayConfig arg3,
							BitmapLoadFrom arg4) {
						i_photo1.setImageBitmap(arg2);
					}

					@Override
					public void onLoadFailed(View arg0, String arg1,
							Drawable arg2) {
						i_photo1.setImageResource(R.drawable.g1);

					}
				});
//		ImageUtils.getBitmapUtils(this).display(i_photo2,
//				URLConstants.BASEURL + news.getNewsphoto2(),
//				new BitmapLoadCallBack<View>() {
//
//					@Override
//					public void onLoadCompleted(View arg0, String arg1,
//							Bitmap arg2, BitmapDisplayConfig arg3,
//							BitmapLoadFrom arg4) {
//						i_photo2.setImageBitmap(arg2);
//					}
//
//					@Override
//					public void onLoadFailed(View arg0, String arg1,
//							Drawable arg2) {
//						i_photo1.setImageResource(R.drawable.g1);
//
//					}
//				});
		i_collect.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.news_collect:
			if (!isCollect) {
				i_collect.setSelected(true);
				isCollect = true;
			} else {
				i_collect.setSelected(false);
				isCollect = false;
			}
			break;
		}

	}

}
