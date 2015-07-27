package com.shuPeng.robotutil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.ListView;

import com.shuPeng.LJrobot.R;
import com.shuPeng.activity.MainActivity;
import com.shuPeng.adapter.ChatAdapter;

public class RobotUtil {
	private static final int CHAT_ME = 0;
	private static List<HashMap<String, Object>> mChatList = new ArrayList<HashMap<String, Object>>();

	public static void AddChatContent(Context mContext, ListView mListView,
			String mContent, int mWho) {
		HashMap<String, Object> mChatHaspMap = new HashMap<String, Object>();
		mChatHaspMap.put("Sex", Integer.valueOf(mWho));
		mChatHaspMap.put("Content", mContent);
		mChatHaspMap.put("Head", mWho == CHAT_ME ? R.drawable.ic_launcher:R.drawable.ic_launcher);
		mChatList.add(mChatHaspMap);
		ChatAdapter mChatAdapter = new ChatAdapter(mContext, mChatList);
		mListView.setAdapter(mChatAdapter);
		mChatAdapter.notifyDataSetChanged();
		mListView.setSelection( mChatList.size()-1);
	}
	
	public static boolean isRunning(){
		return false;
		
	}
	@SuppressWarnings("deprecation")
	public static void buildNitifaction(Context context){
		
		 String svcName = Context.NOTIFICATION_SERVICE;  
	        NotificationManager notificationManager;  
	        notificationManager = (NotificationManager)context.getSystemService(svcName);  
	        //通过使用通知管理器，可以触发新的通知，修改现有的通知或者删除那些不再需要的通知。  
	          
	        /**Android提供了使用通知向用户传递信息的多种方式。 
	         * 1.状态栏图标 
	         * 2.扩展的通知状态绘制器 
	         * 3.额外的效果，比如声音和振动 
	         */  
	        //创建一个通知  
	        //选择一个Drawable来作为状态栏图标的显示  
	        int icon=R.drawable.ic_launcher;
	        //当启动通知时在状态栏显示的文本  
	        String tickerText = "秦哲，你好。";  
	        //扩展的状态栏按时间顺序排序通知  
	        long when = System.currentTimeMillis();  
	        Notification notification = new Notification(icon, tickerText, when);  
	          
	        notification.flags |= Notification.FLAG_AUTO_CANCEL;  
	        String expandedText = "我爱你";  
	        String expandedTitle = "ez";  
	        Intent intent = new Intent(context.getApplicationContext(), MainActivity.class);  
	        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        PendingIntent launchIntent = PendingIntent.getActivity(context, 0, intent, 0);  
	        notification.setLatestEventInfo(context, expandedTitle, expandedText, launchIntent);  
	        int notificationRef = 1;  
	        notificationManager.notify(notificationRef, notification);  
	}
	public static byte[] inputStreamtobyte(InputStream is) {
		byte data[] = new byte[1024];
		int len;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			while ((len = is.read(data)) > 0) {
				bos.write(data, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return bos.toByteArray();
	}
    
}
