package com.oraro.stardream.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.oraro.stardream.util.LogUtil;
import com.oraro.thenextone.widget.pulltorefresh.PullToRefreshBaseView.OnLoadMoreListener;
import com.oraro.thenextone.widget.pulltorefresh.PullToRefreshListView;

public class BaseListFragment extends SherlockFragment {
	public Context mContext;
	public ListView mMainList;
	public PullToRefreshListView lvPullrefresh;
	public int currentPage = 1;
	public boolean isHasMore = true;
	public boolean isRefreshTask = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = getActivity();
	}

	/**
	 * 子类要想获取相应的监听事件,必须在oncreate中调用
	 * */
	public void initListViewListener() {
		mMainList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				onClickItem(arg0, arg1, arg2, arg3);
			}

		});

		lvPullrefresh
				.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
					@Override
					public void onRefresh() {
						onStartRefresh();
					}
				});

		lvPullrefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
			@Override
			public void onLoadMore() {
				onStartLoadMore();
			}
		});
	}

	public void onClickItem(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

	}

	public void onStartRefresh() {
		isRefreshTask = true;
		resetPage();
		resetListViewStatue();
	}

	public void onStartLoadMore() {
		LogUtil.i(this.getClass().getName(),
				"onStartLoadMore come into currentPage =" + currentPage);
		isRefreshTask = false;
	}

	// public void successObtainData(RrtMsg result) {
	// loadedComplete();
	// }

	// public void failObtainData(RrtMsg result) {
	// if (result.getStatus() == ResultCode.RESULT_NO_DATA) {
	// LogUtil.i(this.getClass().getName() + ": ", "没有找到相关数据");
	// }
	// }

	public void setCanLoadMore(boolean isCanLoadMore) {
		lvPullrefresh.setCanLoadMore(isCanLoadMore);
	}

	public void startLoadingData() {
		lvPullrefresh.showRefreshing();
	}

	public void loadMoreComplete() {
		lvPullrefresh.onLoadMoreComplete();
	}

	public void resetPage() {
		currentPage = 1;
	}

	public void resetListViewStatue() {
		lvPullrefresh.onRefreshAllData();
	}

	public void changeHasMoreStatue(int hasmore) {
		// isHasMore = hasmore == RrtMsg.HAS_NEXT_PAGE ? true : false;
		isHasMore = true;
		if (isHasMore) {
			currentPage++;
		} else {
			loadedDataCompleteTip();
		}
		setCanLoadMore(isHasMore);
	}

	private void loadedDataCompleteTip() {
		if (currentPage == 1) {
			loadedOnlyOnePage();
		} else {
			loadedAllComplete();
		}
	}

	public void refreshComplete() {
		lvPullrefresh.onRefreshComplete();
	}

	public void loadedComplete() {
		lvPullrefresh.onLoadMoreComplete();
	}

	public void loadedAllComplete() {
		lvPullrefresh.onLoadAllDataComplete();
	}

	public void loadedOnlyOnePage() {

		lvPullrefresh.onLoadOnlyOnePage();

	}

	public void loadedNothingTip() {
		lvPullrefresh.onNothingTip();
	}

	// public void setCurrentTaskNUll() {
	// if (mTask != null)
	// mTask.cancel(true);
	// mTask = null;
	// }

	
	/**
	 * 
	 * @return getWindowParams()[0]宽度， getWindowParams()[1]高度
	 * 
	 */
	public DisplayMetrics getDisplayMetrics() {
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm;
	}
}
