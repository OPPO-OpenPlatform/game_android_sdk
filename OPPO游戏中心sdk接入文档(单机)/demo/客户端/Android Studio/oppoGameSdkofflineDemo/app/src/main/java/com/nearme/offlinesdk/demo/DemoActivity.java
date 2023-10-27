package com.nearme.offlinesdk.demo;

import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import com.nearme.game.sdk.GameCenterSDK;
import com.nearme.game.sdk.callback.*;
import com.nearme.game.sdk.common.model.ApiResult;
import com.nearme.game.sdk.common.model.biz.PayInfo;
import com.nearme.game.sdk.common.model.biz.ReportUserGameInfoParam;
import com.nearme.game.sdk.common.model.biz.ReqUserInfoParam;
import com.nearme.game.sdk.common.util.AppUtil;
import com.nearme.game.sdk.pay.PayResponse;


import android.app.Activity;

import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class DemoActivity extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
	

		super.onCreate(savedInstanceState);
		
		

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_demo);
		
		findViewById(R.id.test_login).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				doLogin();
			}

			private void doLogin() {
				GameCenterSDK.getInstance().doLogin(DemoActivity.this, new ApiCallback() {
					
					@Override
					public void onSuccess(String arg0) {
						doGetTokenAndSsoid();
					}
					
					@Override
					public void onFailure(String arg0, int arg1) {
						Toast.makeText(DemoActivity.this,arg0,Toast.LENGTH_LONG).show();
					}
				});
			}
		});
		
		
		findViewById(R.id.send_custom_role_info).setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				sendRoleInfo();
			}
		});
		
		findViewById(R.id.test_pay).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {

						doPay();

					}
				});

		findViewById(R.id.test_Verified).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getVerifiedInfo();
			}
		});
		findViewById(R.id.test_exit_guid).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {

						GameCenterSDK.getInstance().onExit(DemoActivity.this,
								new GameExitCallback() {

									@Override
									public void exitGame() {
										// CP 实现游戏退出操作，也可以直接调用
										// AppUtil工具类里面的实现直接强杀进程~
										AppUtil.exitGameProcess(DemoActivity.this);



									}
								});

					}
				});
						
	}

	private void getVerifiedInfo() {
		GameCenterSDK.getInstance().doGetVerifiedInfo(new ApiCallback() {
			@Override
			public void onSuccess(String resultMsg) {
				try {
					//解析年龄age
					int age=Integer.parseInt(resultMsg);
					if (age < 18) {
						Toast.makeText(DemoActivity.this, "已实名但未成年，CP开始处理防沉迷",Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(DemoActivity.this, "已实名且已成年，尽情玩游戏吧~",Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(String resultMsg, int resultCode) {
				if(resultCode == ApiResult.RESULT_CODE_VERIFIED_FAILED_AND_RESUME_GAME){
					Toast.makeText(DemoActivity.this, resultMsg+"，还可以继续玩游戏",Toast.LENGTH_SHORT).show();
				}else if(resultCode == ApiResult.RESULT_CODE_VERIFIED_FAILED_AND_STOP_GAME){
					Toast.makeText(DemoActivity.this, resultMsg+",CP自己处理退出游戏",Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	private void doGetUserInfoByCpClient(String token, String ssoid) {
		GameCenterSDK.getInstance().doGetUserInfo(
				new ReqUserInfoParam(token, ssoid), new ApiCallback() {

					@Override
					public void onSuccess(String resultMsg) {
						Toast.makeText(DemoActivity.this, resultMsg,
								Toast.LENGTH_LONG).show();
					}

					@Override
					public void onFailure(String resultMsg, int resultCode) {

					}
				});
	}

	public void doGetTokenAndSsoid() {
		GameCenterSDK.getInstance().doGetTokenAndSsoid(new ApiCallback() {

			@Override
			public void onSuccess(String resultMsg) {
				try {
					JSONObject json = new JSONObject(resultMsg);
					String token = json.getString("token");
					String ssoid = json.getString("ssoid");
					doGetUserInfoByCpClient(token, ssoid);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(String content, int resultCode) {

			}
		});
	}

	private void sendRoleInfo() {
		GameCenterSDK.getInstance().doReportUserGameInfoData(
				new ReportUserGameInfoParam("default", "default",0, "default", "default", "default", null), new ApiCallback() {

					public void onSuccess(String resultMsg) {
						Toast.makeText(DemoActivity.this, "success",
								Toast.LENGTH_LONG).show();
					}

					@Override
					public void onFailure(String resultMsg, int resultCode) {
						Toast.makeText(DemoActivity.this, resultMsg,
								Toast.LENGTH_LONG).show();
					}
				});

	}

	

	private void doPay() {
		// cp支付参数
		int amount = 1; // 支付金额，单位分
		String notifyURL="http://gamecenter.wanyol.com:8080/gamecenter/callback_test_url";
		String	notifyURL1=notifyURL;
		PayInfo payInfo = new PayInfo(System.currentTimeMillis()
				+ new Random().nextInt(1000) + "", "自定义字段", amount);
		payInfo.setProductDesc("购买后获得500-钻石金色宝箱");
		payInfo.setProductName("商品名称");

		payInfo.setUseCachedChannel(false);
		
		// 支付结果服务器回调地址，不通过服务端回调发货的游戏可以不用填写~
		payInfo.setCallbackUrl(notifyURL1);

		GameCenterSDK.getInstance().doSinglePay(this, payInfo,
				new SinglePayCallback() {

					@Override
					public void onSuccess(String resultMsg) {
						// add OPPO 支付成功处理逻辑~
						Toast.makeText(DemoActivity.this, "支付成功",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onFailure(String resultMsg, int resultCode) {
						// add OPPO 支付失败处理逻辑~
						if (PayResponse.CODE_CANCEL != resultCode) {
							Toast.makeText(DemoActivity.this, "支付失败",
									Toast.LENGTH_SHORT).show();
						} else {
							// 取消支付处理
							Toast.makeText(DemoActivity.this, "支付取消",
									Toast.LENGTH_SHORT).show();
						}
					}
					public void onCallCarrierPay(PayInfo payInfo, boolean bySelectSMSPay) {
						// TODO Auto-generated method stub
						Toast.makeText(DemoActivity.this, "运营商支付",
								Toast.LENGTH_SHORT).show();
					}

					public void onCallCarrierPay(PayInfo arg0) {
						// TODO Auto-generated method stub
						
					}
				});
	}


}