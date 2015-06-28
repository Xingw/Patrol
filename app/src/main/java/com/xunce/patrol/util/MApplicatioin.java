package com.xunce.patrol.util;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.thinkland.sdk.android.JuheSDKInitializer;

import im.fir.sdk.FIR;

/**
 * Created by ssthouse on 2015/6/16.
 */
public class MApplicatioin extends Application {

    @Override
    public void onCreate() {
        FIR.init(this);

        super.onCreate();
        SDKInitializer.initialize(getApplicationContext());
        //聚合数据初始化
        JuheSDKInitializer.initialize(getApplicationContext());
    }
}
