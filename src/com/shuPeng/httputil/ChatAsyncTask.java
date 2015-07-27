package com.shuPeng.httputil;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.shuPeng.entity.Comment;
import com.shuPeng.entity.Contents;
import com.shuPeng.robotutil.RobotUtil;
import com.shuPeng.robotutil.XunfeiVoice;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;

public class ChatAsyncTask extends AsyncTask<Void, Void, String> {
	private Context mContext;
	private String content;
	private String channelid;
	private ListView mChatListView;
	private static final int CHAT_ROBOT = 1;
	private String result;
	private XunfeiVoice xunfeiVoice;
	StringBuffer sb=new StringBuffer(Contents.ASK_HTTP);
	public ChatAsyncTask(Context mContext, String content,String channelid,ListView mChatListView) {
		this.mContext=mContext;
		this.content=content;
		this.channelid=channelid;
		this.mChatListView=mChatListView;
	}
	@Override
	protected String doInBackground(Void... arg0) {
	
		String path=sb.append(channelid).append("&text=").append(content).append("&gps=").append(Comment.Longitude).append(",").append(Comment.Latitude).append("&os=android").toString();	
		System.out.println(path);
		try {
			URL url=new URL(path);
			try {
				HttpURLConnection connection=(HttpURLConnection) url.openConnection();
				connection.setDoInput(true);
				connection.connect();
				connection.setConnectTimeout(3000);
				InputStream is=connection.getInputStream();
				result=new String(RobotUtil.inputStreamtobyte(is));
				return result;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return result;
	}
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if(result!=null);
		//xunfeiVoice.voiceTTS(mContext, result);
		//RobotUtil.AddChatContent(mContext, mChatListView, result.toString(), CHAT_ROBOT);
		RobotUtil.AddChatContent(mContext, mChatListView, "你好", CHAT_ROBOT);
		
	}
}
