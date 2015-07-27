package com.shuPeng.robotutil;

import com.shuPeng.LJrobot.R;

import android.app.AlertDialog;
import android.content.Context;
/**
 * 注意网络问题会报错
 * @author luhuanju
 *
 */
public class ProcessDialog {
	private static AlertDialog mDialog;
	public static void showProcessDialog(Context context){
		mDialog = new AlertDialog.Builder(context).create();
		mDialog.show();
		mDialog.setContentView(R.layout.loading_process_dialog_anim);
	}
	public static void dismissProcessDialog(){
		mDialog.dismiss();
	}
}
