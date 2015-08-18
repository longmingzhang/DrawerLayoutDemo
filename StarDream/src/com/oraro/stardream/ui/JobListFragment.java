package com.oraro.stardream.ui;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.oraro.stardream.R;
import com.oraro.stardream.adapter.FindStaffAdapter;
import com.oraro.stardream.ui.base.BaseListFragment;
import com.oraro.thenextone.widget.pulltorefresh.PullToRefreshListView;

public class JobListFragment extends BaseListFragment {
	private FindStaffAdapter mFindStaffAdpater;
	private ArrayList<String> mModels;// 主列表
	
	public static JobListFragment newInstance() {
		JobListFragment newFragment = new JobListFragment();
	        return newFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View parentView = inflater.inflate(R.layout.activity_jobstaff, container,
				false);
		initListView(parentView);
		initListViewListener();
		return parentView;
	}

	private void initListView(View v) {
		lvPullrefresh = (PullToRefreshListView) v
				.findViewById(R.id.lv_normal_listview);
		mMainList = lvPullrefresh.getRefreshableView();
		lvPullrefresh.setCanLoadMore(true);
		initListViewListener();
		
		mFindStaffAdpater = new FindStaffAdapter(mContext);
		
		mModels = new ArrayList<String>();
		obtainData();
		mFindStaffAdpater.appendToList(mModels);
		
		mMainList.setAdapter(mFindStaffAdpater);
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
	
	@Override
	public void onClickItem(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		super.onClickItem(arg0, arg1, arg2, arg3);
//		Intent intent = new Intent(getActivity(), JobDetailActivity.class);
//		startActivity(intent);
	}
	
	@Override
	public void onStartRefresh() {
		super.onStartRefresh();
		obtainData();
		mFindStaffAdpater.appendToList(mModels);
		refreshComplete();
	}

	@Override
	public void onStartLoadMore() {
		super.onStartLoadMore();
		obtainData();
		mFindStaffAdpater.appendToList(mModels);
		loadedComplete();
	}

	private void obtainData() {
		mModels.clear();
		mModels.add("1");
		mModels.add("1");
		mModels.add("1");
	}

}
