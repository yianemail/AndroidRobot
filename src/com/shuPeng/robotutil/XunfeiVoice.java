package com.shuPeng.robotutil;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.shuPeng.entity.Comment;
import com.shuPeng.httputil.ChatAsyncTask;

public class XunfeiVoice {
	private Context mContext;
	public static final String PREFER_NAME = "com.iflytek.setting";
	private String TAG = XunfeiVoice.class.getSimpleName();
	private static final int CHAT_ME = 0;
	private SpeechRecognizer mIat;
	private RecognizerDialog mIatDialog;
	private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
	private SharedPreferences mSharedPreferences;
	private String mEngineType = SpeechConstant.TYPE_CLOUD;
	private ListView mChatListView;
	int ret = 0;
	private int count;

	public void Voicetoword(SpeechRecognizer mIat2, Context context,
			ListView chatListView, int i) {
		count = i;
		mIat = mIat2;
		mContext = context;
		mChatListView = chatListView;
		mIatDialog = new RecognizerDialog(context, null);
		mSharedPreferences = context.getSharedPreferences(PREFER_NAME,
				Activity.MODE_PRIVATE);
		setParam();
		boolean isShowDialog = mSharedPreferences.getBoolean("iat_show", false);
		if (isShowDialog) {
			mIatDialog.setListener(recognizerDialogListener);
			mIatDialog.show();
		}
		// 不显示对话框
		else {
			ret = mIat.startListening(recognizerListener);
		}

	}

	private void setParam() {
		mIat.setParameter(SpeechConstant.PARAMS, null);
		mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
		mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
		String lag = mSharedPreferences.getString("iat_language_preference",
				"mandarin");
		if (lag.equals("en_us")) {
			mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
		} else {
			mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
			mIat.setParameter(SpeechConstant.ACCENT, lag);
		}
		// 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
		mIat.setParameter(SpeechConstant.VAD_BOS,
				mSharedPreferences.getString("iat_vadbos_preference", "4000"));
		// 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
		mIat.setParameter(SpeechConstant.VAD_EOS,
				mSharedPreferences.getString("iat_vadeos_preference", "2000"));
		// 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
		mIat.setParameter(SpeechConstant.ASR_PTT,
				mSharedPreferences.getString("iat_punc_preference", "0"));
		// 设置音频保存路径，保存音频格式仅为pcm，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
		// mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH,
		// Environment.getExternalStorageDirectory()
		// + "/iflytek/wavaudio.pcm");
		// 设置听写结果是否结果动态修正，为“1”则在听写过程中动态递增地返回结果，否则只在听写结束之后返回最终结果
		// 注：该参数暂时只对在线听写有效
		mIat.setParameter(SpeechConstant.ASR_DWA,
				mSharedPreferences.getString("iat_dwa_preference", "0"));
	}

	private RecognizerDialogListener recognizerDialogListener = new RecognizerDialogListener() {
		public void onResult(RecognizerResult results, boolean isLast) {
			printResult(results);
		}
		public void onError(SpeechError error) {
		}
	};
	public void voiceTTS(Context context, String word) {
		SpeechSynthesizer mTts = SpeechSynthesizer.createSynthesizer(context,
				null);
		mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
		mTts.setParameter(SpeechConstant.SPEED, "50");// 设置语速
		mTts.setParameter(SpeechConstant.VOLUME, "80");// 设置音量,范围 0~100
		mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
		mTts.startSpeaking(word, new SynthesizerListener() {
			@Override
			public void onSpeakResumed() {
			}

			@Override
			public void onSpeakProgress(int arg0, int arg1, int arg2) {
			}

			@Override
			public void onSpeakPaused() {
			}

			@Override
			public void onSpeakBegin() {
			}

			@Override
			public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {

			}

			@Override
			public void onCompleted(SpeechError arg0) {

			}

			@Override
			public void onBufferProgress(int arg0, int arg1, int arg2,
					String arg3) {

			}
		});

	}

	private RecognizerListener recognizerListener = new RecognizerListener() {

		@Override
		public void onBeginOfSpeech() {
			ProcessDialog.showProcessDialog(mContext);
		}

		@Override
		public void onError(SpeechError error) {
			ProcessDialog.dismissProcessDialog();
			Toast.makeText(mContext, "error", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onEndOfSpeech() {
			ProcessDialog.dismissProcessDialog();
		}

		@Override
		public void onResult(RecognizerResult results, boolean isLast) {
			Log.d(TAG, results.getResultString());
			ProcessDialog.dismissProcessDialog();
			printResult(results);
			if (isLast) {

			}
		}

		@Override
		public void onVolumeChanged(int volume) {
		}

		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
		}
	};

	private void printResult(RecognizerResult results) {
		String text = JsonParser.parseIatResult(results.getResultString());
		String sn = null;
		try {
			JSONObject resultJson = new JSONObject(results.getResultString());
			sn = resultJson.optString("sn");
		} catch (JSONException e) {
			e.printStackTrace();
			return;
		}
		mIatResults.put(sn, text);
		StringBuffer resultBuffer = new StringBuffer();
		for (String key : mIatResults.keySet()) {
			resultBuffer.append(mIatResults.get(key));
		}
		Log.v(TAG, resultBuffer.toString());
		if (count == 0) {
			RobotUtil.AddChatContent(mContext, mChatListView,
					resultBuffer.toString(), CHAT_ME);
			new ChatAsyncTask(mContext, resultBuffer.toString(),
					Comment.Channelid, mChatListView).execute();
			count++;
		}

	}

}
