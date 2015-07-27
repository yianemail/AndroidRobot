package com.shuPeng.robotutil;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.baidu.android.pushservice.PushMessageReceiver;
import com.shuPeng.activity.MainActivity;
import com.shuPeng.entity.Comment;
import com.shuPeng.entity.Contents;

/**
 * 调用PushManager.startWork后，sdk将对push server发起绑定请求，这个过程是异步的。绑定请求的结果通过onBind返回。
 * 如果您需要用单播推送，需要把这里获取的channel id和user id上传到应用server中，再调用server接口用channel id和user
 * id给单个手机或者用户推送。 onMessage :透传 onUnbind :PushManager.stopWork() 的回调函数。
 * @param context  BroadcastReceiver的执行Context
 * @param errorCode绑定接口返回值，0 - 成功
 * @param appid 应用id。errorCode非0时为null
 * @param userId 应用user id。errorCode非0时为null
 * @param channelId 应用channel id。errorCode非0时为null
 * @param requestId    向服务端发起的请求id。在追查问题时有用；
 * @author luhuanju
 */
public class BaiduPushReceiver extends PushMessageReceiver {
	public static final String TAG = BaiduPushReceiver.class.getSimpleName();
   
	@Override
	public void onBind(Context context, int errorCode, String appid,
			String userId, String channelId, String requestId) {
		String responseString = "onBind errorCode=" + errorCode + " appid="
				+ appid + " userId=" + userId + " channelId=" + channelId
				+ " requestId=" + requestId;
		Log.d(TAG, responseString);
		if (errorCode==0) Comment.Userid=userId;
		Comment.Channelid=channelId;
	}

	@Override
	public void onDelTags(Context arg0, int arg1, List<String> arg2,
			List<String> arg3, String arg4) {

	}

	@Override
	public void onListTags(Context arg0, int arg1, List<String> arg2,
			String arg3) {

	}

	@Override
	public void onMessage(Context context, String message,
			String customContentString) {
		String messageString = "透传消息 message=\"" + message
				+ "\" customContentString=" + customContentString;
		Log.d(TAG, messageString);
		boolean isRunning=isRunningApp(context, Contents.PACKAGE_NAME);
		if (isRunning==true){
			System.out.println("正在运行 ");
			Comment.Msg=message;
			JumpActivity(context, message);
			return;
		}
		else if (isRunning==false){
			Comment.Msg=message;
			RobotUtil.buildNitifaction(context);
			return;
		}
	}

	@Override
	public void onNotificationArrived(Context context, String title,
			String description, String customContentString) {
		String notifyString = "onNotificationArrived  title=\"" + title
				+ "\" description=\"" + description + "\" customContent="
				+ customContentString;
		Log.d(TAG, notifyString);

	}

	@Override
	public void onNotificationClicked(Context context, String title,
			String description, String customContentString) {
		String notifyString = "通知点击 title=\"" + title + "\" description=\""
				+ description + "\" customContent=" + customContentString;
		Log.d(TAG, notifyString);

	}

	@Override
	public void onSetTags(Context arg0, int arg1, List<String> arg2,
			List<String> arg3, String arg4) {

	}

	@Override
	public void onUnbind(Context context, int errorCode, String requestId) {
		String responseString = "onUnbind errorCode=" + errorCode
				+ " requestId = " + requestId;
		Log.d(TAG, responseString);

	}

	public static boolean isRunningApp(Context context, String packageName) {
		boolean isAppRunning = false;
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningTaskInfo info : am.getRunningTasks(100)) {
			if (info.topActivity.getPackageName().equals(packageName) && info.baseActivity.getPackageName().equals(packageName)) {
				isAppRunning = true;
				break;
			}
		}
		return isAppRunning;
	}

	public void JumpActivity(Context context, String Msg) {
		Comment.Msg = Msg;
		Intent intent = new Intent();
		intent.setClass(context.getApplicationContext(), MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.getApplicationContext().startActivity(intent);

	}
}
