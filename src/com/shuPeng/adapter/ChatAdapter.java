package com.shuPeng.adapter;

import java.util.HashMap;
import java.util.List;

import com.shuPeng.LJrobot.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChatAdapter extends BaseAdapter {
	private static final int[] CHAT_ITEM_XML = { R.layout.chatitem_me,
		R.layout.chatitem_robot };
private int[] CHAT_WEIGHT = { R.id.chatlist_image_me,
		R.id.chatlist_text_me, R.id.chatlist_image_other,
		R.id.chatlist_text_other };
	private List<HashMap<String, Object>> mChatList;
	private Context mContext;
	private static final int CHAT_ME = 0;
	private static final int CHAT_ROBOT=1;

	public ChatAdapter(Context paramContext,
			List<HashMap<String, Object>> paramList) {
		this.mContext = paramContext;
		this.mChatList = paramList;
	}

	@Override
	public int getCount() {
		return mChatList.size();
	}

	public Object getItem(int paramInt) {
		return mChatList.get(paramInt);
	}
	
	@Override
	public int getItemViewType(int position)
	{
		HashMap<String, Object> msg = mChatList.get(position);
		if( msg.get("Sex")==Integer.valueOf(CHAT_ME))
			return CHAT_ME;
		else
			return CHAT_ROBOT;
	}

	@Override
	public int getViewTypeCount()
	{
		return 2;
	}

	public long getItemId(int paramInt) {
		return paramInt;
	}

	public View getView(int arg0, View view, ViewGroup paramViewGroup) {
		int mSex = (Integer) mChatList.get(arg0).get("Sex");
		ChatViewHolder localChatViewHolder = null;
		if(view==null){
			localChatViewHolder=new ChatViewHolder();
			if(mChatList.get(arg0).get("Sex")==Integer.valueOf(CHAT_ME)){
				view=LayoutInflater.from(mContext).inflate(CHAT_ITEM_XML[0],null);
			}else{
				view=LayoutInflater.from(mContext).inflate(CHAT_ITEM_XML[1],null);
			}
			localChatViewHolder.mImageView = ((ImageView) view
					.findViewById(this.CHAT_WEIGHT[(0 + mSex * 2)]));
			localChatViewHolder.mTextView = ((TextView) view
					.findViewById(this.CHAT_WEIGHT[(1 + mSex * 2)]));
			localChatViewHolder.mTextView.setText((mChatList.get(arg0)).get(
					"Content").toString());
			view.setTag(localChatViewHolder);
		}
		else {
			localChatViewHolder = (ChatViewHolder) view.getTag();
		}
		return view;
	}
}

class ChatViewHolder {
	ImageView mImageView;
	TextView mTextView;
}