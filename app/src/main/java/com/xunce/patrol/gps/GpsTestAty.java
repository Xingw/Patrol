package com.xunce.patrol.gps;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import com.xunce.patrol.R;

/**
 * Created by ssthouse on 2015/6/16.
 */

/**
 * 尚需 不保存删除数据库
 * 插标志
 * 传工程名称
 */
public class GpsTestAty extends Activity {
    private static final String TAG = "gpsAty";
    private String patrol_name="123";//传入巡检项目工程名，用于查询该工程的路径
    LatLng lastlines;
    MapView mMapView = null;
    BaiduMap mBaiduMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.gps_text_aty);

        // 设置地图绘制每一帧时的回调接口
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();

        //稻草的记录方法
        HistoryLocationScanner historyLocationScanner =
                LocationTracker.getHistoryLocationScanner(this);
        List<OneLocationRecord> oneLocationRecordList =
                historyLocationScanner.getLocationRecordData(patrol_name);

        List<LatLng> latLngList = new ArrayList<>();
        for (OneLocationRecord oneLocationRecord : oneLocationRecordList) {
            latLngList.add(new LatLng(oneLocationRecord.getLatitude(),
                    oneLocationRecord.getLongitude()));
        }
        Log.e(TAG, latLngList.size() + "这是我的记录点的的数目");
        for(int i = 0;i < latLngList.size(); i ++){
            Log.i(TAG,"经度:"+latLngList.get(i).longitude);
            Log.i(TAG,"纬度:"+latLngList.get(i).latitude);
            Drawlines(latLngList.get(i));
        }
        initOverlay();//设置起始点标志
    }

    private void initOverlay(){
        MyOverlay myOverlay=new MyOverlay(this,mBaiduMap,114.439309,30.516422);
        myOverlay.showView();
        LatLng ll =myOverlay.mMarker.getPosition();
        Button button = new Button(getApplicationContext());
        button.setBackgroundResource(R.drawable.popup);
        button.setText("起点");
        InfoWindow mInfoWindow;
        mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(button), ll, -47, null);
        mBaiduMap.showInfoWindow(mInfoWindow);
    }

    private void Drawlines(LatLng lines){
        if(lastlines==null)lastlines=lines;
        else{
            List<LatLng> points = new ArrayList<LatLng>();
            points.add(lastlines);
            points.add(lines);
            OverlayOptions ooPolyline = new PolylineOptions().width(10)
                    .color(0xAAFF0000).points(points);
            mBaiduMap.addOverlay(ooPolyline);
            lastlines=lines;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
}
