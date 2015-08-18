package com.oraro.stardream.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.oraro.stardream.R;

public class FindStaffAdapter extends AdapterBase<String> {

	public FindStaffAdapter() {
		super();
	}

	public FindStaffAdapter(Context mContext) {
		super(mContext);
		mInflater = mInflater.from(mContext);
	}

	@Override
	protected View getExView(int position, View convertView, ViewGroup parent) {

		ViewHoler mHolder;
		if (convertView == null) {
			mHolder = new ViewHoler();
			convertView = mInflater.inflate(R.layout.item_adapter_findstaff,
					null);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHoler) convertView.getTag();
		}
//		mHolder.mName.setText("佟丽娅" + position);
		return convertView;
	}

	private static class ViewHoler {

	}
}
