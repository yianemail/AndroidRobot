package com.shuPeng.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ListView;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.location.LocationClient;
import com.iflytek.cloud.SpeechRecognizer;
import com.shuPeng.LJrobot.R;
import com.shuPeng.entity.Comment;
import com.shuPeng.entity.Contents;
import com.shuPeng.robotutil.RobotUtil;
import com.shuPeng.robotutil.XunfeiVoice;
import com.shuPeng.thread.TheadLBS;
import com.shuPeng.ui.BinarySlidingMenu;
import com.shuPeng.ui.BinarySlidingMenu.OnMenuOpenListener;

public class MainActivity extends Activity implements OnClickListener {
	private BinarySlidingMenu mMenu;
	private ListView mChatListView;
	private static final int CHAT_ROBOT = 1;
	private XunfeiVoice mXunfeiVoice = new XunfeiVoice();
	private SpeechRecognizer mIat;
	private TheadLBS threadLBS = new TheadLBS();
	private LocationClient locationClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		initWidget();
		mMenu.setOnMenuOpenListener(new OnMenuOpenListener() {
			@Override
			public void onMenuOpen(boolean isOpen, int flag) {
				if (isOpen) {
				}
			}
		});
	}

	private void initWidget() {
		mIat = SpeechRecognizer.createRecognizer(this, null);
		mMenu = (BinarySlidingMenu) findViewById(R.id.id_menu);
		mChatListView = (ListView) findViewById(R.id.listview_chat);
		findViewById(R.id.linear_send).setOnClickListener(this);
		// findViewById(R.id.btn_text).setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.linear_send:
			mXunfeiVoice.Voicetoword(mIat, this, mChatListView, 0);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		threadLBS.ThreadLBS(this, locationClient);
		PushManager.startWork(getApplicationContext(),
				PushConstants.LOGIN_TYPE_API_KEY, Contents.BAIDU_PUSH_API_KEY);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (Comment.Msg != null)
			RobotUtil.AddChatContent(this, mChatListView, Comment.Msg,
					CHAT_ROBOT);
		mXunfeiVoice.voiceTTS(this, Comment.Msg);
		Comment.Msg = null;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mIat.cancel();
		mIat.destroy();
		locationClient.stop();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			finish();
			System.exit(0);
			return false;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
}
