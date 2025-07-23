package com.nearme.offlinesdk.demo;

import android.app.Application;

import com.nearme.game.sdk.GameCenterSDK;

public class MyApplication extends Application {

    public void onCreate() {
        super.onCreate();
        String appSecret = "9C45A8b21a74f662dd2b80ea77F97695";
        GameCenterSDK.initialize(this, appSecret);
    }
}

