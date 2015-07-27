package com.shuPeng.thread;

import android.content.Context;
import android.os.Handler;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.shuPeng.entity.Comment;

public class TheadLBS {
	private LocationClient mLocationClient;
    private Context mContext;
	public void ThreadLBS(Context context, LocationClient locationClient) {
		this.mContext=context;
		this.mLocationClient=locationClient;
		getBaiduLBS(mContext);
		getLBSinfo(mContext);
	}
	public  void getBaiduLBS(Context context) {
		mLocationClient = new LocationClient(context.getApplicationContext());
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 是否打开GPS
		option.setCoorType("bd09ll"); // 设置返回值的坐标类型。
		option.setProdName("LocationDemo"); // 设置产品线名称。强烈建议您使用自定义的产品线名称，方便我们以后为您提供更高效准确的定位服务。
		option.setScanSpan(500); // 设置定时定位的时间间隔。单位毫秒
		mLocationClient.setLocOption(option);
		mLocationClient.registerLocationListener(new BDLocationListener() {
			@Override
			public void onReceiveLocation(BDLocation arg0) {
				if (arg0 != null) {
					Comment.Longitude = arg0.getLongitude();
					Comment.Latitude = arg0.getLatitude();
					mLocationClient.stop();
				} else {
					return;
				}
			}
		});
		mLocationClient.start();
		mLocationClient.requestLocation();
	}
	public void getLBSinfo(Context context) {
		final Handler handler = new Handler();
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				handler.postDelayed(this, 1200000);
				getBaiduLBS(mContext);
			}
		};
		handler.postDelayed(runnable, 1200000);
	}

}
