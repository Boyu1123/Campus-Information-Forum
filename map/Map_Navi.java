package com.ustb.map;

import com.amap.api.col.el;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewOptions;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.NaviLatLng;
import com.ustb.school.R;
import com.ustb.school.R.layout;
import com.ustb.school.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.Menu;

public class Map_Navi extends NaviBaseActivity {
	private int code;
	private int type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_navi);
		mAMapNaviView = (AMapNaviView) findViewById(R.id.navi_view);
		mAMapNaviView.onCreate(savedInstanceState);
		mAMapNaviView.setAMapNaviViewListener(this);

		init();
	}

	private void init() {
		Intent intent = getIntent();
		code = intent.getIntExtra("code", 0);
		type = intent.getIntExtra("type", 0);

		NaviLatLng start = intent.getParcelableExtra("start");
		NaviLatLng end = intent.getParcelableExtra("end");
		sList.add(start);
		eList.add(end);

		AMapNaviViewOptions options = mAMapNaviView.getViewOptions();
		// // TODO: 16/3/2 最好有一个正经的示例图片
		options.setTilt(0); // 0 表示使用2D地图
		options.setCarBitmap(BitmapFactory.decodeResource(this.getResources(),
				R.drawable.gps_point));
		// options.setFourCornersBitmap(BitmapFactory.decodeResource(this.getResources(),
		// R.drawable.end));
		mAMapNaviView.setViewOptions(options);
	}

	@Override
	public void onInitNaviSuccess() {
		super.onInitNaviSuccess();
		int strategy = 0;
		try {
			// 再次强调，最后一个参数为true时代表多路径，否则代表单路径
			strategy = mAMapNavi.strategyConvert(true, false, false, false,
					false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (code == 1) {
			
			mAMapNavi.calculateDriveRoute(sList, eList, mWayPointList, strategy);
		}else {
			mAMapNavi.calculateWalkRoute(sList.get(0), eList.get(0)); // 步行导航
			
		}
	}

	@Override
	public void onCalculateRouteSuccess() {
		super.onCalculateRouteSuccess();
		if (type == 0) {
			mAMapNavi.startNavi(NaviType.GPS);

		} else {
			mAMapNavi.startNavi(NaviType.EMULATOR);

		}
		// 模拟导航
	}

}
