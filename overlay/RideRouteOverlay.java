package com.ustb.overlay;

import java.util.List;

import android.content.Context;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.RidePath;
import com.amap.api.services.route.RideStep;
import com.ustb.map.util.AMapUtil;
import com.ustb.school.R;

/**
 * ����·��ͼ���ࡣ�ڸߵµ�ͼAPI����Ҫ��ʾ����·�߹滮�������ô�������������·��ͼ�㡣�粻��������Ҳ�����Լ������Զ��������·��ͼ�㡣
 * @since V3.5.0
 */
public class RideRouteOverlay extends RouteOverlay {

	private PolylineOptions mPolylineOptions;
	
	private BitmapDescriptor walkStationDescriptor= null;

	private RidePath ridePath;
	/**
	 * ͨ��˹��캯������·��ͼ�㡣
	 * @param context ��ǰactivity��
	 * @param amap ��ͼ����
	 * @param path ����·�߹滮��һ�������������������ģ���·����ѯ��com.amap.api.services.route���е��� <strong><a href="../../../../../../Search/com/amap/api/services/route/WalkStep.html" title="com.amap.api.services.route�е���">WalkStep</a></strong>��
	 * @param start ��㡣�����������ģ��ĺ��Ļ��com.amap.api.services.core���е���<strong><a href="../../../../../../Search/com/amap/api/services/core/LatLonPoint.html" title="com.amap.api.services.core�е���">LatLonPoint</a></strong>��
	 * @param end �յ㡣�����������ģ��ĺ��Ļ��com.amap.api.services.core���е���<strong><a href="../../../../../../Search/com/amap/api/services/core/LatLonPoint.html" title="com.amap.api.services.core�е���">LatLonPoint</a></strong>��
	 * @since V3.5.0
	 */
	public RideRouteOverlay(Context context, AMap amap, RidePath path,
							LatLonPoint start, LatLonPoint end) {
		super(context);
		this.mAMap = amap;
		this.ridePath = path;
		startPoint = AMapUtil.convertToLatLng(start);
		endPoint = AMapUtil.convertToLatLng(end);
	}
	/**
	 * �������·�ߵ���ͼ�С�
	 * @since V3.5.0
	 */
	public void addToMap() {
		
		initPolylineOptions();
		try {
			List<RideStep> ridePaths = ridePath.getSteps();
			mPolylineOptions.add(startPoint);
			for (int i = 0; i < ridePaths.size(); i++) {
				RideStep rideStep = ridePaths.get(i);
				LatLng latLng = AMapUtil.convertToLatLng(rideStep
						.getPolyline().get(0));
				
				addRideStationMarkers(rideStep, latLng);
				addRidePolyLines(rideStep);
			}
			mPolylineOptions.add(endPoint);
			addStartAndEndMarker();
			
			showPolyline();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}


	/**
	 * @param rideStep
	 */
	private void addRidePolyLines(RideStep rideStep) {
		mPolylineOptions.addAll(AMapUtil.convertArrList(rideStep.getPolyline()));
	}
	/**
	 * @param rideStep
	 * @param position
	 */
	private void addRideStationMarkers(RideStep rideStep, LatLng position) {
		addStationMarker(new MarkerOptions()
				.position(position)
				.title("\u65B9\u5411:" + rideStep.getAction()
						+ "\n\u9053\u8DEF:" + rideStep.getRoad())
				.snippet(rideStep.getInstruction()).visible(nodeIconVisible)
				.anchor(0.5f, 0.5f).icon(walkStationDescriptor));
	}
	
	 /**
     * ��ʼ���߶�����
     */
    private void initPolylineOptions() {
    	
    	if(walkStationDescriptor == null) {
    		walkStationDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.amap_ride);
    	}
        mPolylineOptions = null;
        mPolylineOptions = new PolylineOptions();
        mPolylineOptions.color(getDriveColor()).width(getRouteWidth());
    }
	 private void showPolyline() {
	        addPolyLine(mPolylineOptions);
	    }
}
