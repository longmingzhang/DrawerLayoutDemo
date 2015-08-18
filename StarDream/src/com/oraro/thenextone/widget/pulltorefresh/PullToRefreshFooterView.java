/**
 * @file XFooterView.java
 * @create Mar 31, 2012 9:33:43 PM
 * @author Maxwin
 * @description XListView's footer
 */
package com.oraro.thenextone.widget.pulltorefresh;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oraro.stardream.R;

public class PullToRefreshFooterView extends LinearLayout {
	public final static int STATE_NORMAL = 0;
	public final static int STATE_READY = 1;
	public final static int STATE_LOADING = 2;
	public final static int LOADED = 3;
	public final static int LOADED_ALL = 4;
	public final static int LOAD_EXCEPTION = 5;
	public final static int LOAD_NOTHING = 7;
	public final static int LOAD_ONLY_ONE_PAGE = 8;
	public final static int STATE_HIDE_ALL_VIEW = 6;

	private Context mContext;

	private View mContentView;
	private View mProgressBar;
	private TextView mHintView;

	private Handler hideContextViewHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			mContentView.setVisibility(View.GONE);
		};
	};

	public PullToRefreshFooterView(Context context) {
		super(context);
		initView(context);
	}

	public PullToRefreshFooterView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public void setState(int state) {
		mHintView.setVisibility(View.INVISIBLE);
		mProgressBar.setVisibility(View.INVISIBLE);
		mHintView.setVisibility(View.INVISIBLE);
		mContentView.setVisibility(View.VISIBLE);
		if (state == STATE_HIDE_ALL_VIEW) {
			mContentView.setVisibility(View.GONE);
		} else if (state == STATE_READY) {
			mHintView.setVisibility(View.VISIBLE);
			mHintView.setText(R.string.pull_lv_footer_hint_ready);
		} else if (state == STATE_LOADING) {
			show();
			mHintView.setVisibility(View.VISIBLE);
			mProgressBar.setVisibility(View.VISIBLE);
			mHintView.setText(R.string.pull_lv_header_hint_loading);
		} else if (state == LOADED) {
			mContentView.setVisibility(View.GONE);

		} else if (state == LOADED_ALL) {
			show();
			mHintView.setVisibility(View.VISIBLE);
			mHintView.setText(R.string.pull_lv_header_hint_loaded_all);
			hideContextViewHandler.sendEmptyMessageDelayed(0, 3500);
		} else if (state == LOAD_EXCEPTION) {
			show();
			mHintView.setVisibility(View.VISIBLE);
			mHintView.setText(R.string.pull_lv_footer_hint_load_exception);
			hideContextViewHandler.sendEmptyMessageDelayed(0, 5500);
		
		} else if(state == LOAD_NOTHING){
			show();
			mHintView.setVisibility(View.VISIBLE);
			mHintView.setText(R.string.pull_lv_footer_hint_load_nothing);
		}else if(state == LOAD_ONLY_ONE_PAGE){
			hide();
		}
			else {
			hide();
			mHintView.setVisibility(View.VISIBLE);
			mHintView.setText(R.string.pull_lv_footer_hint_normal);
		}
	}

	public void setBottomMargin(int height) {
		if (height < 0)
			return;
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView
				.getLayoutParams();
		lp.bottomMargin = height;
		mContentView.setLayoutParams(lp);
	}

	public int getBottomMargin() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView
				.getLayoutParams();
		return lp.bottomMargin;
	}

	/**
	 * normal status
	 */
	public void normal() {
		mHintView.setVisibility(View.VISIBLE);
		mProgressBar.setVisibility(View.GONE);
	}

	/**
	 * loading status
	 */
	public void loading() {
		mHintView.setVisibility(View.GONE);
		mProgressBar.setVisibility(View.VISIBLE);
	}

	/**
	 * hide footer when disable pull load more
	 */
	public void hide() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView
				.getLayoutParams();
		lp.height = 0;
		mContentView.setLayoutParams(lp);
	}

	/**
	 * show footer
	 */
	public void show() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView
				.getLayoutParams();
		lp.height = LayoutParams.WRAP_CONTENT;
		mContentView.setLayoutParams(lp);
	}

	private void initView(Context context) {
		mContext = context;
		LinearLayout moreView = (LinearLayout) LayoutInflater.from(mContext)
				.inflate(R.layout.item_pull_lv_footer, null);
		addView(moreView);
		moreView.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

		mContentView = moreView.findViewById(R.id.xlistview_footer_content);
		mProgressBar = moreView.findViewById(R.id.mz_pb_pulllv_footer);
		mHintView = (TextView) moreView
				.findViewById(R.id.mz_tv_pulllv_footer_hint);
	}

}
