package com.shuPeng.robotutil;

import com.iflytek.cloud.SpeechUtility;
import com.shuPeng.entity.Contents;

import android.app.Application;

public class XunfeiSpeechApp extends Application {

	@Override
	public void onCreate() {
		// 应用程序入口处调用,避免手机内存过小,杀死后台进程后通过历史intent进入Activity造成SpeechUtility对象为null
		// 如在Application中调用初始化，需要在Mainifest中注册该Applicaiton
		// 注意：此接口在非主进程调用会返回null对象，如需在非主进程使用语音功能，请增加参数：SpeechConstant.FORCE_LOGIN+"=true"
		// 参数间使用“,”分隔。
		// 设置你申请的应用appid
		SpeechUtility.createUtility(XunfeiSpeechApp.this, "appid="+Contents.XUNFEI_KEY);
		super.onCreate();
	}
}
