package com.oraro.stardream.ui;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;

import com.oraro.stardream.R;
import com.oraro.stardream.adapter.SquareListAdapter;
import com.oraro.stardream.ui.base.BaseListFragment;
import com.oraro.thenextone.widget.pulltorefresh.PullToRefreshListView;

public class SquareFragment extends BaseListFragment {
	private SquareListAdapter mMianListAdapter;
	private ArrayList<String> mModels;// 主列表

	// header
	private LinearLayout headView;
	
	public static SquareFragment newInstance() {
		SquareFragment newFragment = new SquareFragment();
//	        Bundle bundle = new Bundle();
//	        bundle.putString("hello", s);
//	        newFragment.setArguments(bundle);
	        return newFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View parentView = inflater.inflate(R.layout.squarefragment, container,
				false);
		initListView(parentView);
		return parentView;
	}

	private void initListView(View v) {
		lvPullrefresh = (PullToRefreshListView) v
				.findViewById(R.id.lv_normal_listview);
		mMainList = lvPullrefresh.getRefreshableView();
		lvPullrefresh.setCanLoadMore(true);
		initListViewListener();
		mMianListAdapter = new SquareListAdapter(mContext);
		mModels = new ArrayList<String>();
		obtainData();
		initListHeader();
		mMianListAdapter.appendToList(mModels);
		mMainList.setAdapter(mMianListAdapter);
		mMainList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 > 0) {
//					Intent it = new Intent(mContext, WeiboContentActivity.class);
//					startActivity(it);
				}
			}
		});
	}

	private void initListHeader() {
		headView = (LinearLayout) LayoutInflater.from(mContext).inflate(
				R.layout.squarefragment_list_header, null);
		mMainList.addHeaderView(headView);
	}

	@Override
	public void onStartRefresh() {
		super.onStartRefresh();
		obtainData();
		mMianListAdapter.appendToList(mModels);
		refreshComplete();
	}

	@Override
	public void onStartLoadMore() {
		// TODO Auto-generated method stub
		super.onStartLoadMore();
		obtainData();
		mMianListAdapter.appendToList(mModels);
		loadedComplete();
	}

	private void obtainData() {
		mModels.clear();
		mModels.add("1");
		mModels.add("1");
		mModels.add("1");
	}

}
