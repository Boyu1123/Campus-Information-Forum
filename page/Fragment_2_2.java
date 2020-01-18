package com.ustb.page;

import java.util.HashMap;
import java.util.Map;

import com.ustb.application.MyApplication;
import com.ustb.callback.Callback;
import com.ustb.http.connection.HttpServer;
import com.ustb.model.BeanData;
import com.ustb.parser.CardParser;
import com.ustb.school.R;
import com.ustb.status.StatusCode;
import com.ustb.url.URLConstants;
import com.ustb.utils.ExampleUtil;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class Fragment_2_2 extends Fragment implements OnClickListener {
	private EditText e_title, e_mess;
	private Button btn_submit;
	private ImageView i_img1, i_img2;
	private String path1 = null;
	private String path2 = null;
	private MyApplication app;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.from(getActivity()).inflate(R.layout.fragment_2_2,
				null);
		init(view);
		return view;
	}

	private void init(View view) {
		e_title = (EditText) view.findViewById(R.id.newcard_title);
		e_mess = (EditText) view.findViewById(R.id.newcard_mess);
		btn_submit = (Button) view.findViewById(R.id.card_submit);
		i_img1 = (ImageView) view.findViewById(R.id.card_img1);
		// i_img2 = (ImageView) view.findViewById(R.id.card_img2);

		btn_submit.setOnClickListener(this);
		i_img1.setOnClickListener(this);
		// i_img2.setOnClickListener(this);

		app = (MyApplication) getActivity().getApplication();
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.card_img1:
			selectHead(1);
			break;
		// case R.id.card_img2:
		// selectHead(2);
		// break;
		case R.id.card_submit:
			sendCard();
			break;

		}

	}

	private void sendCard() {
		String title = e_title.getText().toString();
		String mess = e_mess.getText().toString();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", "addCard");
		params.put("title", title);
		params.put("mess", mess);
		params.put("userid", String.valueOf(app.getUser().getId()));
		if (path1 != null) {
			params.put("photoFile", path1);
		}
		if (path2 != null) {
			params.put("photoFile", path2);
		}
		HttpServer.setPostRequest(params, new CardParser(),
				URLConstants.BASEURL + URLConstants.CARDURL, new Callback() {
					@Override
					public void success(BeanData beanData) {
						if (beanData.getCode() == StatusCode.Common.SUCCESS) {
							if (beanData.getFlag() == StatusCode.Dao.INSERT_SUCCESS) {
								ExampleUtil.showToast("发帖成功", getActivity());
							} else {
								ExampleUtil.showToast("发表帖子失败", getActivity());
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

	/**
	 * 头像的选择
	 */
	private void selectHead(int i) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_PICK);
		intent.setType("image/*");
		startActivityForResult(intent, i);
	}

	/**
	 * 头像选择后返回图片路径
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		Uri uri = data.getData();
		// 内容解析器对象
		ContentResolver con = getActivity().getContentResolver();
		// 根据地址查询图片
		Cursor c = con.query(uri, null, null, null, null);
		// 获取路径
		if (c.moveToNext()) {
			// String picNo = c.getString(0); //图片的编号
			path1 = c.getString(1);
			// String imagesize = c.getString(2);
			// String imagename = c.getString(3);
		}
		i_img1.setImageBitmap(BitmapFactory.decodeFile(path1));
	}
}
