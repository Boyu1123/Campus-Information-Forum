package com.ustb.map;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.amap.api.services.route.RouteSearch.BusRouteQuery;
import com.amap.api.services.route.RouteSearch.DriveRouteQuery;
import com.amap.api.services.route.RouteSearch.OnRouteSearchListener;
import com.amap.api.services.route.RouteSearch.WalkRouteQuery;
import com.ustb.overlay.BusRouteOverlay;
import com.ustb.overlay.DrivingRouteOverlay;
import com.ustb.overlay.WalkRouteOverlay;
import com.ustb.school.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Map_Show extends Activity implements OnClickListener,
		OnGeocodeSearchListener, OnRouteSearchListener {
	private Button btn_back, btn_local, btn_place, btn_car, btn_bus, btn_walk,
			btn_go, btn_likego;
	private LinearLayout map_l;
	private String place;

	MapView mMapView = null;
	AMap aMap;
	private boolean islocalsucc = false;
	private double llong;
	private double llat;
	// 经纬度值:40.23576,116.218147 联合大学
	// private LatLonPoint latLonPoint = new LatLonPoint(40.23576, 116.218147);
	private LatLonPoint latLonPoint;
	private LatLonPoint startLatLonPoint, endLatLonPoint;
	private Marker geoMarker, regeoMarker;// 锚点,在地图上进行绘制的点
	private String addressName;

	// 声明AMapLocationClient类对象
	public AMapLocationClient mLocationClient = null;
	// 声明AMapLocationClientOption对象
	public AMapLocationClientOption mLocationOption = null;
	private GeocodeSearch geocoderSearch;

	private int routeType = 1;// 1代表公交模式，2代表驾车模式，3代表步行模式
	private int busMode = RouteSearch.BusDefault;// 公交默认模式
	private int drivingMode = RouteSearch.DrivingDefault;// 驾车默认模式
	private int walkMode = RouteSearch.WalkDefault;// 步行默认模式
	private BusRouteResult busRouteResult;// 公交模式查询结果
	private DriveRouteResult driveRouteResult;// 驾车模式查询结果
	private WalkRouteResult walkRouteResult;// 步行模式查询结果
	private RouteSearch routeSearch;

	private int code = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_show);
		// 获取地图控件引用
		mMapView = (MapView) findViewById(R.id.map);
		// 在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
		mMapView.onCreate(savedInstanceState);

		if (aMap == null) {
			aMap = mMapView.getMap();
		}
		init();
	}

	private void init() {
		btn_back = (Button) findViewById(R.id.back);
		btn_back.setOnClickListener(this);
		place = getIntent().getStringExtra("place");

		btn_local = (Button) findViewById(R.id.m_local);
		btn_place = (Button) findViewById(R.id.m_place);
		btn_car = (Button) findViewById(R.id.m_car);
		btn_bus = (Button) findViewById(R.id.m_bus);
		btn_walk = (Button) findViewById(R.id.m_walk);
		btn_go = (Button) findViewById(R.id.btn_go);
		btn_likego = (Button) findViewById(R.id.btn_likego);
		btn_local.setOnClickListener(this);
		btn_place.setOnClickListener(this);
		btn_car.setOnClickListener(this);
		btn_bus.setOnClickListener(this);
		btn_walk.setOnClickListener(this);
		btn_go.setOnClickListener(this);
		btn_likego.setOnClickListener(this);
		map_l = (LinearLayout) findViewById(R.id.map_l);
		map_l.setVisibility(View.GONE);

		getLocationPlace();

		geocoderSearch = new GeocodeSearch(this);
		geocoderSearch.setOnGeocodeSearchListener(this);

		geoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f).icon(
				BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));// 将geoMarker设置成蓝色
		regeoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_RED)));// 红色
		routeSearch = new RouteSearch(this);
		routeSearch.setRouteSearchListener(this);
		getLatlon(place);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.m_local:
			findLocal();
			map_l.setVisibility(View.GONE);
			break;
		case R.id.m_place:
			findPlace();
			map_l.setVisibility(View.GONE);
			break;
		case R.id.m_car:
			searchRoute(2);
			code = 1;
			map_l.setVisibility(View.VISIBLE);
			break;
		case R.id.m_bus:
			searchRoute(1);
			map_l.setVisibility(View.GONE);
			break;
		case R.id.m_walk:
			searchRoute(3);
			code = 2;
			map_l.setVisibility(View.VISIBLE);
			break;
		case R.id.btn_go:
			Intent intent1 = new Intent(Map_Show.this, Map_Navi.class);
			intent1.putExtra("type", 0);
			intent1.putExtra("code", code);
			intent1.putExtra("start",
					new NaviLatLng(startLatLonPoint.getLatitude(),
							startLatLonPoint.getLongitude()));
			intent1.putExtra("end", new NaviLatLng(endLatLonPoint.getLatitude(),
					endLatLonPoint.getLongitude()));
			startActivity(intent1);
			break;
		case R.id.btn_likego:
			Intent intent2 = new Intent(Map_Show.this, Map_Navi.class);
			intent2.putExtra("type", 1);
			intent2.putExtra("code", code);
			intent2.putExtra("start",
					new NaviLatLng(startLatLonPoint.getLatitude(),
							startLatLonPoint.getLongitude()));
			intent2.putExtra("end", new NaviLatLng(endLatLonPoint.getLatitude(),
					endLatLonPoint.getLongitude()));
			startActivity(intent2);
			break;

		default:
			break;
		}

	}

	private void findLocal() {
		aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(llat,
				llong), 16));// 可视区域动画是指从当前可视区域转换到一个指定位置的可视区域的过程
		geoMarker.setPosition(new LatLng(llat, llong));

		// 设置当前地图显示为当前位置
		// aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
		// new LatLng(llat, llong), 16));
		// MarkerOptions markerOptions = new MarkerOptions();
		// markerOptions.position(new LatLng(llat, llong));
		// markerOptions.title("当前位置");
		// markerOptions.visible(true);
		// BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
		// .fromBitmap(BitmapFactory.decodeResource(
		// getResources(), R.drawable.gps_point));
		// markerOptions.icon(bitmapDescriptor);
		// aMap.addMarker(markerOptions);

	}

	private void findPlace() {
		aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
				endLatLonPoint.getLatitude(), endLatLonPoint.getLongitude()),
				16));// 可视区域动画是指从当前可视区域转换到一个指定位置的可视区域的过程
		geoMarker.setPosition(new LatLng(endLatLonPoint.getLatitude(),
				endLatLonPoint.getLongitude()));

	}

	private void getLocationPlace() {
		// 初始化定位
		mLocationClient = new AMapLocationClient(getApplicationContext());
		// 设置定位回调监听
		mLocationClient.setLocationListener(mLocationListener);
		// 初始化AMapLocationClientOption对象
		mLocationOption = new AMapLocationClientOption();
		// 设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
		mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
		mLocationOption.setOnceLocationLatest(true);
		// 设置是否返回地址信息（默认返回地址信息）
		mLocationOption.setNeedAddress(true);
		// 给定位客户端对象设置定位参数
		mLocationClient.setLocationOption(mLocationOption);
		mLocationOption.setWifiActiveScan(true);
		mLocationOption.setMockEnable(false);
		mLocationOption.setInterval(2000);
		// 启动定位
		mLocationClient.startLocation();
	}

	// 声明定位回调监听器
	public AMapLocationListener mLocationListener = new AMapLocationListener() {

		@Override
		public void onLocationChanged(AMapLocation amapLocation) {
			if (amapLocation != null) {
				if (amapLocation.getErrorCode() == 0) {
					// 可在其中解析amapLocation获取相应内容。
					Log.e("test", amapLocation.getAddress());
					llong = amapLocation.getLongitude();
					llat = amapLocation.getLatitude();
					startLatLonPoint = new LatLonPoint(llat, llong);
					islocalsucc = true;

					// 设置当前地图显示为当前位置
					// aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
					// new LatLng(llat, llong), 16));
					// MarkerOptions markerOptions = new MarkerOptions();
					// markerOptions.position(new LatLng(llat, llong));
					// markerOptions.title("当前位置");
					// markerOptions.visible(true);
					// BitmapDescriptor bitmapDescriptor =
					// BitmapDescriptorFactory
					// .fromBitmap(BitmapFactory.decodeResource(
					// getResources(), R.drawable.gps_point));
					// markerOptions.icon(bitmapDescriptor);
					// aMap.addMarker(markerOptions);
				} else {
					// 定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
					Log.e("AmapError", "location Error, ErrCode:"
							+ amapLocation.getErrorCode() + ", errInfo:"
							+ amapLocation.getErrorInfo());
				}
			}
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
		mMapView.onDestroy();
		if (mLocationClient != null) {
			mLocationClient.onDestroy();// 销毁定位客户端。
		}

	}

	@Override
	protected void onStop() {
		super.onStop();
		if (mLocationClient != null) {

			mLocationClient.stopLocation();// 停止定位
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
		mMapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
		mMapView.onPause();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// 在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState
		// (outState)，保存地图当前的状态
		mMapView.onSaveInstanceState(outState);
	}

	/**
	 * 响应地理编码,查询经纬度
	 */
	public void getLatlon(final String name) {

		GeocodeQuery query = new GeocodeQuery(name, "北京");// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，或citycode、adcode，null为全国
		geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
	}

	/**
	 * 响应逆地理编码，查询地址
	 */
	public void getAddress(final LatLonPoint latLonPoint) {
		RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
				GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
		geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
	}

	/**
	 * 地理编码查询回调
	 * 
	 * @param result
	 *            地理编码返回的结果
	 * @param rCode
	 */
	@Override
	public void onGeocodeSearched(GeocodeResult result, int rCode) {
		Log.e("test", rCode + "");
		// aMap.clear();
		if (rCode == 1000) {
			if (result != null && result.getGeocodeAddressList() != null
					&& result.getGeocodeAddressList().size() > 0) {
				GeocodeAddress address = result.getGeocodeAddressList().get(0);
				endLatLonPoint = address.getLatLonPoint();
				aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
						new LatLng(endLatLonPoint.getLatitude(), endLatLonPoint
								.getLongitude()), 16));// 可视区域动画是指从当前可视区域转换到一个指定位置的可视区域的过程
				geoMarker.setPosition(new LatLng(endLatLonPoint.getLatitude(),
						endLatLonPoint.getLongitude()));
				// addressName = "经纬度值:" + address.getLatLonPoint() + "\n位置描述:"
				// + address.getFormatAddress();
				// endLatLonPoint = new LatLonPoint(address.getLatLonPoint()
				// .getLatitude(), address.getLatLonPoint().getLongitude());
				// System.out.println(addressName);
				Log.e("test", addressName);
				// Toast.makeText(RouteActivity.this,
				// addressName,Toast.LENGTH_LONG).show();
			} else {
				// 没有地址
				Log.e("test", "没有地址");
			}
		} else if (rCode == 27) {// 网络连接有问题
		} else if (rCode == 32) {// 没有key在官网获取
		} else {// 未知错误
		}
	}

	/**
	 * 逆地理编码回调
	 * 
	 * @param result
	 * @param rCode
	 */
	@Override
	public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
		aMap.clear();
		if (rCode == 0) {
			if (result != null && result.getRegeocodeAddress() != null
					&& result.getRegeocodeAddress().getFormatAddress() != null) {
				addressName = result.getRegeocodeAddress().getFormatAddress()
						+ "附近";
				aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
						new LatLng(latLonPoint.getLatitude(), latLonPoint
								.getLongitude()), 15));
				regeoMarker.setPosition(new LatLng(latLonPoint.getLatitude(),
						latLonPoint.getLongitude()));
			} else {
			}
		} else if (rCode == 27) {// 错误如上
		} else if (rCode == 32) {
		} else {
		}
	}

	@Override
	public void onBusRouteSearched(BusRouteResult result, int rCode) {
		if (rCode == 1000) {
			if (result != null && result.getPaths() != null
					&& result.getPaths().size() > 0) {
				busRouteResult = result;
				BusPath busPath = busRouteResult.getPaths().get(0);
				aMap.clear();// 清理地图上的所有覆盖物
				BusRouteOverlay routeOverlay = new BusRouteOverlay(this, aMap,// 第一个参数是context，2.是地图
						busPath, busRouteResult.getStartPos(),// 3.公交线路，4,5参数没找到，api上面没有，连相关的方法都没看到
						busRouteResult.getTargetPos());
				routeOverlay.removeFromMap();// 去掉BusRouteOverlay上所有的Marker
				routeOverlay.addToMap();// 添加公交线路到地图
				routeOverlay.zoomToSpan();// 移动镜头当前视角
			} else {
			}
		}
	}

	@Override
	public void onDriveRouteSearched(DriveRouteResult result, int arg1) {
		Log.e("test", arg1 + "");
		if (arg1 == 1000) {
			if (result != null && result.getPaths() != null
					&& result.getPaths().size() > 0) {
				driveRouteResult = result;
				DrivePath drivePath = driveRouteResult.getPaths().get(0);
				aMap.clear();// 清理地图上的所有覆盖物
				// fload drivePath.getDistance()
				DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
						this, aMap, drivePath, driveRouteResult.getStartPos(),
						driveRouteResult.getTargetPos(), null);
				drivingRouteOverlay.removeFromMap();
				drivingRouteOverlay.addToMap();
				drivingRouteOverlay.zoomToSpan();
			} else {
			}
		}

	}

	@Override
	public void onRideRouteSearched(RideRouteResult arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onWalkRouteSearched(WalkRouteResult result, int rCode) {
		if (rCode == 1000) {
			if (result != null && result.getPaths() != null
					&& result.getPaths().size() > 0) {
				walkRouteResult = result;
				WalkPath walkPath = walkRouteResult.getPaths().get(0);
				aMap.clear();// 清理地图上的所有覆盖物
				WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(this,
						aMap, walkPath, walkRouteResult.getStartPos(),
						walkRouteResult.getTargetPos());
				walkRouteOverlay.removeFromMap();
				walkRouteOverlay.addToMap();
				walkRouteOverlay.zoomToSpan();
			} else {
			}
		}

	}

	/**
	 * 点击搜索按钮开始Route搜索
	 */
	public void searchRoute(int i) {
		routeType = i;
		// 经纬度值:40.212627,116.23843 万科
		// startLatLonPoint = new LatLonPoint(40.23576, 116.218147);
		// endLatLonPoint = new LatLonPoint(40.212627, 116.23843);
		searchRouteResult(startLatLonPoint, endLatLonPoint);

	}

	public void searchRouteResult(LatLonPoint startPoint, LatLonPoint endPoint) {
		final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
				startPoint, endPoint);
		if (routeType == 1) {// 公交路径规划
			BusRouteQuery query = new BusRouteQuery(fromAndTo, busMode, "北京", 0);// 第一个参数表示路径规划的起点和终点，第二个参数表示公交查询模式，第三个参数表示公交查询城市区号，第四个参数表示是否计算夜班车，0表示不计算
			routeSearch.calculateBusRouteAsyn(query);// 异步路径规划公交模式查询异步处理。----根据指定的参数来计算公交换乘路径的异步处理。只支持市内公交换乘。
		} else if (routeType == 2) {// 驾车路径规划
			DriveRouteQuery query = new DriveRouteQuery(fromAndTo, drivingMode,
					null, null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
			routeSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
		} else if (routeType == 3) {// 步行路径规划
			WalkRouteQuery query = new WalkRouteQuery(fromAndTo, walkMode);
			routeSearch.calculateWalkRouteAsyn(query);// 异步路径规划步行模式查询
		}
		// System.out.println("========searchRouteResult()=========");
	}

}
