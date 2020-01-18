package com.ustb.page;

import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
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
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class More_Notice extends Activity implements OnClickListener {
	private Button btn_back;
	private E_Notice notice;
	private TextView t_title, t_author, t_time, t_mess;
	private ImageView i_photo, i_collect;
	private boolean isCollect = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_notice);
		init();
	}

	private void init() {
		t_title = (TextView) findViewById(R.id.notice_title);
		t_author = (TextView) findViewById(R.id.notice_author);
		t_time = (TextView) findViewById(R.id.notice_time);
		t_mess = (TextView) findViewById(R.id.notice_mess);
		i_photo = (ImageView) findViewById(R.id.notice_photo);
		i_collect = (ImageView) findViewById(R.id.notice_collect);
		btn_back = (Button) findViewById(R.id.back);
		btn_back.setOnClickListener(this);
		i_collect.setOnClickListener(this);

		notice = (E_Notice) getIntent().getSerializableExtra("notice");
		
		t_title.setText(notice.getNtitle());
		t_author.setText(notice.getNauthor());
		t_mess.setText(notice.getNmess());
		t_time.setText(notice.getNdate());
		
		ImageUtils.getBitmapUtils(this).display(i_photo,
				URLConstants.BASEURL + notice.getNurl(),
				new BitmapLoadCallBack<View>() {

					@Override
					public void onLoadCompleted(View arg0, String arg1,
							Bitmap arg2, BitmapDisplayConfig arg3,
							BitmapLoadFrom arg4) {
						i_photo.setImageBitmap(arg2);
					}

					@Override
					public void onLoadFailed(View arg0, String arg1,
							Drawable arg2) {
						i_photo.setImageResource(R.drawable.g1);

					}
				});
		
		i_collect.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.notice_collect:
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
