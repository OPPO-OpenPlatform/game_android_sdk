package com.nearme.onlinesdk.demo;

import com.nearme.game.sdk.GameCenterSDK;
import com.nearme.game.sdk.common.util.AppUtil;
import android.app.Application;
import android.util.Log;

public class MyApplication extends Application {

	public void onCreate() {
		super.onCreate();
		String appSecret = "9C45A8b21a74f662dd2b80ea77F97695";
		GameCenterSDK.init(appSecret, this);	
	}
}

