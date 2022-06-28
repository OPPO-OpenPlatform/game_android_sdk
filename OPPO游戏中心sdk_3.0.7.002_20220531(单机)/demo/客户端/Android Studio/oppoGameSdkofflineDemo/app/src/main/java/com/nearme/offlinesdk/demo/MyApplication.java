package   com.nearme.offlinesdk.demo;

import com.nearme.game.sdk.GameCenterSDK;
import android.app.Application;

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		String appSecret = "149210d64b5e0b8eE0B6e5DAf642229F";
		
		GameCenterSDK.init(appSecret, this);

	}
	}
