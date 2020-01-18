package com.ustb.overlay;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.busline.BusLineItem;
import com.amap.api.services.busline.BusStationItem;
import com.amap.api.services.core.LatLonPoint;
import com.ustb.school.R;

/**
 * 閸忣兛姘︾痪鑳熅閸ユ儳鐪扮猾姹囷拷閸︺劑鐝宄版勾閸ョ稓PI闁插矉绱濇俊鍌涚亯鐟曚焦妯夌粈鍝勫彆娴溿倗鍤庣捄顖ょ礉閸欘垯浜掗悽銊︻劃缁粯娼甸崚娑樼紦閸忣兛姘︾痪鑳熅閸ユ儳鐪伴妴鍌氼渾娑撳秵寮х搾鎶芥付濮瑰偊绱濇稊鐔峰讲娴犮儴鍤滃鍗炲灡瀵ら缚鍤滅�姘疅閻ㄥ嫬鍙曟禍銈囧殠鐠侯垰娴樼仦鍌橈拷
 *
 * @since V2.1.0
 */
public class BusLineOverlay {
    private BusLineItem mBusLineItem;
    private AMap mAMap;
    private ArrayList<Marker> mBusStationMarks = new ArrayList<Marker>();
    private Polyline mBusLinePolyline;
    private List<BusStationItem> mBusStations;
    private BitmapDescriptor startBit, endBit, busBit;
    private Context mContext;

    /**
     * 闁俺绻冨銈嗙�闁姴鍤遍弫鏉垮灡瀵ゅ搫鍙曟禍銈囧殠鐠侯垰娴樼仦鍌橈拷
     *
     * @param context     瑜版挸澧燼ctivity閵嗭拷
     * @param amap        閸︽澘娴樼�纭呰杽閵嗭拷
     * @param busLineItem 閸忣兛姘︾痪鑳熅閵嗗倽顕涚憴浣规偝缁便垺婀囬崝鈩兡侀崸妤冩畱閸忣兛姘︾痪鑳熅閸滃苯鍙曟禍銈囩彲閻愮懓瀵橀敍鍧坥m.amap.api.services.busline閿涘鑵戦惃鍕 <strong><a href="../../../../../../Search/com/amap/api/services/busline/BusStationItem.html" title="com.amap.api.services.busline娑擃厾娈戠猾锟�BusStationItem</a></strong>閵嗭拷
     * @since V2.1.0
     */
    public BusLineOverlay(Context context, AMap amap, BusLineItem busLineItem) {
        mContext = context;
        mBusLineItem = busLineItem;
        this.mAMap = amap;
        mBusStations = mBusLineItem.getBusStations();
    }

    /**
     * 濞ｈ濮為崗顑挎唉缁捐儻鐭鹃崚鏉挎勾閸ュ彞鑵戦妴锟�     *
     * @since V2.1.0
     */
    public void addToMap() {
        try {
            List<LatLonPoint> pointList = mBusLineItem.getDirectionsCoordinates();
            List<LatLng> listPolyline = AMapServicesUtil.convertArrList(pointList);
            mBusLinePolyline = mAMap.addPolyline(new PolylineOptions()
                    .addAll(listPolyline).color(getBusColor())
                    .width(getBuslineWidth()));
            if (mBusStations.size() < 1) {
                return;
            }
            for (int i = 1; i < mBusStations.size() - 1; i++) {
                Marker marker = mAMap.addMarker(getMarkerOptions(i));
                mBusStationMarks.add(marker);
            }
            Marker markerStart = mAMap.addMarker(getMarkerOptions(0));
            mBusStationMarks.add(markerStart);
            Marker markerEnd = mAMap
                    .addMarker(getMarkerOptions(mBusStations.size() - 1));
            mBusStationMarks.add(markerEnd);
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    /**
     * 閸樼粯甯�usLineOverlay娑撳﹥澧嶉張澶屾畱Marker閵嗭拷
     *
     * @since V2.1.0
     */
    public void removeFromMap() {
        if (mBusLinePolyline != null) {
            mBusLinePolyline.remove();
        }
        try {
            for (Marker mark : mBusStationMarks) {
                mark.remove();
            }
            destroyBit();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void destroyBit() {
        if (startBit != null) {
            startBit.recycle();
            startBit = null;
        }
        if (endBit != null) {
            endBit.recycle();
            endBit = null;
        }
        if (busBit != null) {
            busBit.recycle();
            busBit = null;
        }
    }

    /**
     * 缁夎濮╅梹婊冦仈閸掓澘缍嬮崜宥囨畱鐟欏棜顬戦妴锟�     *
     * @since V2.1.0
     */
    public void zoomToSpan() {
        if (mAMap == null)
            return;
        try {
            List<LatLonPoint> coordin = mBusLineItem.getDirectionsCoordinates();
            if (coordin != null && coordin.size() > 0) {
                LatLngBounds bounds = getLatLngBounds(coordin);
                mAMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 5));
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private LatLngBounds getLatLngBounds(List<LatLonPoint> coordin) {
        LatLngBounds.Builder b = LatLngBounds.builder();
        for (int i = 0; i < coordin.size(); i++) {
            b.include(new LatLng(coordin.get(i).getLatitude(), coordin.get(i)
                    .getLongitude()));
        }
        return b.build();
    }

    private MarkerOptions getMarkerOptions(int index) {
        MarkerOptions options = new MarkerOptions()
                .position(
                        new LatLng(mBusStations.get(index).getLatLonPoint()
                                .getLatitude(), mBusStations.get(index)
                                .getLatLonPoint().getLongitude()))
                .title(getTitle(index)).snippet(getSnippet(index));
        if (index == 0) {
            options.icon(getStartBitmapDescriptor());
        } else if (index == mBusStations.size() - 1) {
            options.icon(getEndBitmapDescriptor());
        } else {
            options.anchor(0.5f, 0.5f);
            options.icon(getBusBitmapDescriptor());
        }
        return options;
    }

    protected BitmapDescriptor getStartBitmapDescriptor() {
        startBit = BitmapDescriptorFactory.fromResource(R.drawable.amap_start);
        return startBit;
    }

    protected BitmapDescriptor getEndBitmapDescriptor() {
        endBit = BitmapDescriptorFactory.fromResource(R.drawable.amap_end);
        return endBit;
    }

    protected BitmapDescriptor getBusBitmapDescriptor() {
        busBit = BitmapDescriptorFactory.fromResource(R.drawable.amap_bus);
        return busBit;
    }

    /**
     * 鏉╂柨娲栫粭鐞瞡dex閻ㄥ嚜arker閻ㄥ嫭鐖ｆ０妯革拷
     *
     * @param index 缁楊剙鍤戞稉鐙筧rker閵嗭拷
     * @return marker閻ㄥ嫭鐖ｆ０妯革拷
     * @since V2.1.0
     */
    protected String getTitle(int index) {
        return mBusStations.get(index).getBusStationName();

    }

    /**
     * 鏉╂柨娲栫粭鐞瞡dex閻ㄥ嚜arker閻ㄥ嫯顕涢幆鍛拷
     *
     * @param index 缁楊剙鍤戞稉鐙筧rker閵嗭拷
     * @return marker閻ㄥ嫯顕涢幆鍛拷
     * @since V2.1.0
     */
    protected String getSnippet(int index) {
        return "";
    }

    /**
     * 娴犲窇arker娑擃厼绶遍崚鏉垮彆娴溿倗鐝悙鐟版躬list閻ㄥ嫪缍呯純顔猴拷
     *
     * @param marker 娑擄拷閲滈弽鍥唶閻ㄥ嫬顕挒掳锟�
     * @return 鏉╂柨娲栫拠顨奱rker鐎电懓绨查惃鍕彆娴溿倗鐝悙鐟版躬list閻ㄥ嫪缍呯純顔猴拷
     * @since V2.1.0
     */
    public int getBusStationIndex(Marker marker) {
        for (int i = 0; i < mBusStationMarks.size(); i++) {
            if (mBusStationMarks.get(i).equals(marker)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 鏉╂柨娲栫粭鐞瞡dex閻ㄥ嫬鍙曟禍銈囩彲閻愬湱娈戞穱鈩冧紖閵嗭拷
     *
     * @param index 缁楊剙鍤戞稉顏勫彆娴溿倗鐝悙骞匡拷
     * @return 閸忣兛姘︾粩娆戝仯閻ㄥ嫪淇婇幁顖橈拷鐠囷箒顬�幖婊呭偍閺堝秴濮熷Ο鈥虫健閻ㄥ嫬鍙曟禍銈囧殠鐠侯垰鎷伴崗顑挎唉缁旀瑧鍋ｉ崠鍜冪礄com.amap.api.services.busline閿涘鑵戦惃鍕 <strong><a href="../../../../../../Search/com/amap/api/services/busline/BusStationItem.html" title="com.amap.api.services.busline娑擃厾娈戠猾锟�BusStationItem</a></strong>閵嗭拷
     * @since V2.1.0
     */
    public BusStationItem getBusStationItem(int index) {
        if (index < 0 || index >= mBusStations.size()) {
            return null;
        }
        return mBusStations.get(index);
    }

    protected int getBusColor() {
        return Color.parseColor("#537edc");
    }

    protected float getBuslineWidth() {
        return 18f;
    }
}
