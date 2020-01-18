package com.ustb.page;

import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.ustb.application.MyApplication;
import com.ustb.school.R;
import com.ustb.url.URLConstants;
import com.ustb.utils.ImageUtils;
import com.ustb.utils.SPUtils;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Fragment_4 extends Fragment implements OnClickListener {
	private ImageView head;
	private RelativeLayout r_tianzi, r_comment, r_activity, r_collect, r_set,
			r_login;
	private TextView t_name;
	private MyApplication app;
	private TextView t_login, t_admin;
	private MyBroad myBroad;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.from(getActivity()).inflate(R.layout.fragment_4,
				null);
		app = (MyApplication) getActivity().getApplication();
		init(view);
		return view;
	}

	private void init(View view) {
		t_admin = (TextView) view.findViewById(R.id.personal_admin);
		t_name = (TextView) view.findViewById(R.id.personal_name);
		head = (ImageView) view.findViewById(R.id.personal_head);
		r_tianzi = (RelativeLayout) view.findViewById(R.id.persoanl_tianzi);
		r_comment = (RelativeLayout) view.findViewById(R.id.persoanl_comment);
		r_activity = (RelativeLayout) view.findViewById(R.id.persoanl_activity);
		r_collect = (RelativeLayout) view.findViewById(R.id.persoanl_collect);
		r_set = (RelativeLayout) view.findViewById(R.id.persoanl_set);
		r_login = (RelativeLayout) view.findViewById(R.id.persoanl_login);
		t_login = (TextView) view.findViewById(R.id.p7);

		head.setOnClickListener(this);
		r_tianzi.setOnClickListener(this);
		r_comment.setOnClickListener(this);
		r_activity.setOnClickListener(this);
		r_collect.setOnClickListener(this);
		r_set.setOnClickListener(this);
		r_login.setOnClickListener(this);
		if (app.isLoginCode()) {
			t_login.setText("注销");
			initUserMess();
		} else {
			t_login.setText("登录");
		}

		myBroad = new MyBroad();
		getActivity()
				.registerReceiver(myBroad, new IntentFilter("UPDATE_USER"));
		
		r_comment.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.personal_head:
			if (app.isLoginCode()) {
				Intent personalintent = new Intent(getActivity(),
						Fragment_4_Persoanl.class);
				startActivity(personalintent);
			} else {
				login();
			}
			break;
		case R.id.persoanl_tianzi:
			if (app.isLoginCode()) {
				Intent tianziintent = new Intent(getActivity(),
						Fragment_4_Tianzi.class);
				startActivity(tianziintent);
			} else {
				login();
			}
			break;
		case R.id.persoanl_comment:
			if (app.isLoginCode()) {
				Intent commentintent = new Intent(getActivity(),
						Fragment_4_Comment.class);
				startActivity(commentintent);
			} else {
				login();
			}
			break;
		case R.id.persoanl_activity:
			if (app.isLoginCode()) {
				Intent activityintent = new Intent(getActivity(),
						Fragment_4_Activity.class);
				startActivity(activityintent);
			} else {
				login();
			}
			break;

		case R.id.persoanl_collect:
			if (app.isLoginCode()) {
				Intent collectintent = new Intent(getActivity(),
						Fragment_4_Collect.class);
				startActivity(collectintent);
			} else {
				login();
			}
			break;
		case R.id.persoanl_set:
			Intent setintent = new Intent(getActivity(), Fragment_4_Set.class);
			startActivity(setintent);
			break;
		case R.id.persoanl_login:
			login();
			break;
		}

	}

	/**
	 * 点击登录时候进行的判断操作
	 */
	private void login() {
		if (app.isLoginCode()) {
			new AlertDialog.Builder(getActivity())
					.setTitle("温馨提示")
					.setMessage("确定要注销当前用户吗")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {// 确定按钮的响应事件
									app.setLoginCode(false);
									app.setUser(null);
									t_name.setText("未登录");
									SPUtils.remove(getActivity(), "logincode");
									t_login.setText("登录");
									head.setImageResource(R.drawable.head01);
								}

							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {// 添加返回按钮
								@Override
								public void onClick(DialogInterface dialog,
										int which) {// 响应事件
								}

							}).show();// 在按键响应事件中显示此对话框
		} else {
			Intent loginintent = new Intent(getActivity(), Other_Login.class);
			startActivity(loginintent);
		}

	}

	/**
	 * 当用户时登录状态时候加载部分信息
	 */
	private void initUserMess() {
		t_name.setText(app.getUser().getName());
		// if (app.getUser().getAdmin()==0) {
		t_admin.setText("普通用户");
		// }
		ImageUtils.getBitmapUtils(getActivity()).display(head,
				URLConstants.BASEURL + app.getUser().getPhotourl(),
				new BitmapLoadCallBack<View>() {

					@Override
					public void onLoadCompleted(View arg0, String arg1,
							Bitmap arg2, BitmapDisplayConfig arg3,
							BitmapLoadFrom arg4) {
						Bitmap bitmap = ImageUtils.circleBitmap(arg2);
						head.setImageBitmap(bitmap);
						head.invalidate();

					}

					@Override
					public void onLoadFailed(View arg0, String arg1,
							Drawable arg2) {
						head.setImageResource(R.drawable.head01);

					}
				});

	}

	/**
	 * 注册广播
	 * 
	 * @author ASUS
	 * 
	 */
	class MyBroad extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			t_login.setText("注销");
			t_name.setText(app.getUser().getName());
			ImageUtils.getBitmapUtils(getActivity()).display(head,
					URLConstants.BASEURL + app.getUser().getPhotourl(),
					new BitmapLoadCallBack<View>() {

						@Override
						public void onLoadCompleted(View arg0, String arg1,
								Bitmap arg2, BitmapDisplayConfig arg3,
								BitmapLoadFrom arg4) {
							Bitmap bitmap = ImageUtils.circleBitmap(arg2);
							head.setImageBitmap(bitmap);
							head.invalidate();

						}

						@Override
						public void onLoadFailed(View arg0, String arg1,
								Drawable arg2) {
							head.setImageResource(R.drawable.head01);

						}
					});
		}

	}

	@Override
	public void onDestroy() {
		getActivity().unregisterReceiver(myBroad);
		super.onDestroy();
	}
}
