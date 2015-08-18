package com.oraro.stardream.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oraro.stardream.R;

public class SquareListAdapter extends AdapterBase<String> {

	public SquareListAdapter() {
		super();
	}

	public SquareListAdapter(Context mContext) {
		super(mContext);
		mInflater = mInflater.from(mContext);
	}

	@Override
	protected View getExView(int position, View convertView, ViewGroup parent) {

		ViewHoler mHolder;
		if (convertView == null) {
			mHolder = new ViewHoler();
			convertView = mInflater.inflate(R.layout.squarefragment_list_item,
					null);
			mHolder.mPic = (ImageView) convertView
					.findViewById(R.id.squarefragment_list_item_userpic);
			mHolder.mMark = (ImageView) convertView
					.findViewById(R.id.squarefragment_list_item_iv_mark);
			mHolder.mName = (TextView) convertView
					.findViewById(R.id.squarefragment_list_item_username);
			mHolder.mTime = (TextView) convertView
					.findViewById(R.id.squarefragment_list_item_time);
			mHolder.mShareStr = (TextView) convertView
					.findViewById(R.id.squarefragment_list_item_sharecontent);
			mHolder.mPics_ll = (LinearLayout) convertView
					.findViewById(R.id.squarefragment_list_item_ll);
			mHolder.mOriginalStr = (TextView) convertView
					.findViewById(R.id.squarefragment_list_item_content);
			mHolder.mShareNum = (TextView) convertView
					.findViewById(R.id.squarefragment_list_item_tv_sharenum);
			mHolder.mCommentNum = (TextView) convertView
					.findViewById(R.id.squarefragment_list_item_tv_commentnum);
			mHolder.mZanNum = (TextView) convertView
					.findViewById(R.id.squarefragment_list_item_tv_zannum);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHoler) convertView.getTag();
		}
//		imageLoader
//				.displayImage(
//						"http://www.qqleju.com/uploads/allimg/130703/03-054536_142.jpg",
//						mHolder.mPic, options);
//		mHolder.mName.setText("佟丽娅" + position);
		return convertView;
	}

	private static class ViewHoler {
		private ImageView mPic;
		private ImageView mMark;
		private TextView mName;
		private TextView mTime;
		private TextView mShareStr;
		private LinearLayout mPics_ll;
		private TextView mOriginalStr;
		private TextView mShareNum;
		private TextView mCommentNum;
		private TextView mZanNum;

	}
}
