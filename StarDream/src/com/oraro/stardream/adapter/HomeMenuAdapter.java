package com.oraro.stardream.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.oraro.stardream.R;
import com.oraro.stardream.util.DensityUtil;

public class HomeMenuAdapter extends AdapterBase<String> {
	private int[] mMenuIconArray = {
			R.drawable.selector_home_menu_item_dynamic,
			R.drawable.selector_home_menu_item_news,
			R.drawable.selector_home_menu_item_msg,
			R.drawable.selector_home_menu_item_doc,
			R.drawable.selector_home_menu_item_com,
			R.drawable.selector_home_menu_item_module,
			R.drawable.selector_home_menu_item_more };

	public HomeMenuAdapter() {
		super();
	}

	public HomeMenuAdapter(Context mContext) {
		super(mContext);
		mInflater = mInflater.from(mContext);
	}

	@Override
	protected View getExView(int position, View convertView, ViewGroup parent) {

		ViewHoler mHolder;
		if (convertView == null) {
			mHolder = new ViewHoler();
			convertView = mInflater.inflate(R.layout.home_menu_list_item, null);
			mHolder.mPic = (ImageView) convertView
					.findViewById(R.id.home_menu_userpic);
			mHolder.mName = (TextView) convertView
					.findViewById(R.id.home_menu_name);
			mHolder.mRemark = (TextView) convertView
					.findViewById(R.id.home_menu_remark);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHoler) convertView.getTag();
		}

		mHolder.mPic.setImageResource(mMenuIconArray[position]);
		mHolder.mName.setText(getList().get(position));
		if (position == 2) {
			mHolder.mRemark.setVisibility(View.VISIBLE);
			mHolder.mRemark.setText(15 + "");
		} else {
			mHolder.mRemark.setVisibility(View.GONE);
		}
		convertView.setLayoutParams(new AbsListView.LayoutParams(
				LayoutParams.FILL_PARENT, DensityUtil.dip2px(mContext, 50)));
		return convertView;
	}

	private static class ViewHoler {
		private ImageView mPic;
		private TextView mName;
		private TextView mRemark;

	}
}
