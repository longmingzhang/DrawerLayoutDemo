package com.oraro.thenextone.widget.pulltorefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.oraro.stardream.R;
import com.oraro.stardream.util.LogUtil;

public abstract class PullToRefreshBaseView<T extends View> extends
		LinearLayout {

	private final String TAG = "PullToRefreshBaseView-";
	static final float FRICTION = 2.0f;
	static final float THRESHOLD = 0.0001f;

	static final int PULL_TO_REFRESH = 0x0;
	static final int RELEASE_TO_REFRESH = 0x1;
	static final int REFRESHING = 0x2;

	private float mLastY = -1; // save event y
	private final static int PULL_LOAD_MORE_DELTA = -20;

	private int mState = PULL_TO_REFRESH;
	private int mTouchSlop;
	private int mHeaderHeight;

	private float mInitialMotionY;
	private float mLastMotionX;
	private float mLastMotionY;

	private boolean mIsBeingDragged = false;
	private boolean mDisableScroll = false;
	private boolean mCanPullDown = true;
	private boolean mIsLoadingMore = false;
	private boolean mCanPullUpLoadMore = true;
	private boolean mIsShowLoadAllData = false;
	private boolean mIsUndoubtedListView = true;
	private boolean isShowedLoadAllDataTip = false;

	// header view and footer view
	private PullToRefreshHeaderView mHeaderLayout;
	private PullToRefreshFooterView mFooterLayout;
	private OnRefreshListener mOnRefreshListener;
	private OnLoadMoreListener mOnLoadMoreListener;

	T mRefreshableView;

	public PullToRefreshBaseView(Context context) {
		super(context);
		init(context, null);
	}

	public PullToRefreshBaseView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	/**
	 * 获取当前封装的view
	 * 
	 */
	public final T getRefreshableView() {
		return mRefreshableView;
	}

	/**
	 * 是否支持下拉刷新
	 */
	public final boolean isPullToRefreshEnabled() {
		return mCanPullDown;
	}

	/**
	 * 当正在更新时， 是否还能拖动
	 */
	public final boolean isDisableScrollingWhileRefreshing() {
		return mDisableScroll;
	}

	/**
	 * 返回控件是否还在刷新状态
	 */
	public final boolean isRefreshing() {
		return mState == REFRESHING;
	}

	/**
	 * 设置是否支持刷新时仍能对当前view进行操作
	 * 
	 * @param disableScrollingWhileRefreshing
	 *            - 如果为true 则在刷新状态下 不响应touch事件
	 */
	public final void setDisableScrollingWhileRefreshing(
			boolean disableScrollingWhileRefreshing) {
		this.mDisableScroll = disableScrollingWhileRefreshing;
	}

	public void setCanLoadMore(boolean isCanLoadMore) {
		mCanPullUpLoadMore = isCanLoadMore;
	}

	public void setIsUndoubtedListView(boolean isUndoubtedListView) {
		this.mIsUndoubtedListView = isUndoubtedListView;
	}

	/**
	 * 结束当前的刷新状态， 更新UI并隐藏Refreshing View
	 */
	public final void onRefreshComplete() {
		mIsLoadingMore = false;
		if (mState != PULL_TO_REFRESH) {
			mHeaderLayout.setRefreshDate();
			resetHeader();
		}
	}
	
	public final void onLoadDataException(){
		onRefreshComplete();
		mIsLoadingMore = false;
		mFooterLayout.setState(PullToRefreshFooterView.LOAD_EXCEPTION);
	}
	
	public final void onNothingTip(){
		onRefreshComplete();
		mIsLoadingMore = false;
		mFooterLayout.setState(PullToRefreshFooterView.LOAD_NOTHING);
	}

	public final void onLoadMoreComplete() {
		mIsLoadingMore = false;
		mFooterLayout.setState(PullToRefreshFooterView.LOADED);
		mRefreshableView.setEnabled(true);
	}

	public final void onLoadAllDataComplete() {
		LogUtil.i(TAG, "onLoadAllDataComplete come into");
		mCanPullUpLoadMore = false;
		mIsLoadingMore = false;
		mIsShowLoadAllData = true;
		isShowedLoadAllDataTip =  true;
		mFooterLayout.setState(PullToRefreshFooterView.LOADED_ALL);
	}
	
	public final void onLoadOnlyOnePage() {
		LogUtil.i(TAG, "onLoadAllDataComplete come into");
		mCanPullUpLoadMore = false;
		mIsLoadingMore = false;
		mIsShowLoadAllData = true;
		isShowedLoadAllDataTip =  true;
		mFooterLayout.setState(PullToRefreshFooterView.LOAD_ONLY_ONE_PAGE);
	}
	
	public final void onRefreshAllData(){
		LogUtil.i(TAG, "onRefreshAllData come into");
		mCanPullUpLoadMore = true;
		mIsShowLoadAllData = false;
		isShowedLoadAllDataTip =  false;
	}

	/**
	 * 给控件添加刷新监听器
	 * 
	 * @param listener
	 *            - 监听器
	 */
	public final void setOnRefreshListener(OnRefreshListener listener) {
		mOnRefreshListener = listener;
	}

	/**
	 * 给控件添加加载更多监听器
	 * 
	 * @param listener
	 *            - 监听器
	 */
	public void setOnLoadMoreListener(OnLoadMoreListener listener) {
		this.mOnLoadMoreListener = listener;
	}

	/**
	 * 设置下拉刷新属性
	 * 
	 * @param enable
	 *            下拉刷新是否可用
	 */
	public final void setPullToRefreshEnabled(boolean enable) {
		this.mCanPullDown = enable;
	}

	/**
	 * 当控件需要释放时显示的文字
	 * 
	 * @param releaseLabel
	 *            - 显示的信息
	 */
	public void setReleaseLabel(String releaseLabel) {
		if (null != mHeaderLayout) {
			mHeaderLayout.setReleaseLabel(releaseLabel);
		}
	}

	/**
	 * 当控件正在下拉时显示的文字
	 * 
	 * @param pullLabel
	 *            - 显示的信息
	 */
	public void setPullLabel(String pullLabel) {
		if (null != mHeaderLayout) {
			mHeaderLayout.setPullLabel(pullLabel);
		}
	}

	/**
	 * 控件在刷新状态时显示的文字
	 * 
	 * @param refreshingLabel
	 *            - 显示的信息
	 */
	public void setRefreshingLabel(String refreshingLabel) {
		if (null != mHeaderLayout) {
			mHeaderLayout.setRefreshingLabel(refreshingLabel);
		}
	}

	// at bottom, trigger
	@Override
	public final boolean onTouchEvent(MotionEvent event) {
		boolean shouldHandleEvent = false;
		if (mCanPullDown && !mIsLoadingMore) {
			if (!(isRefreshing() && mDisableScroll)) {
				if (!(event.getAction() == MotionEvent.ACTION_DOWN && event
						.getEdgeFlags() != 0)) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_MOVE:
						if (mIsBeingDragged) {
							mLastMotionY = event.getY();
							this.pullEvent();
							shouldHandleEvent = true;
						}

						break;
					case MotionEvent.ACTION_DOWN:

						if (isReadyForPull()) {
							mLastMotionY = mInitialMotionY = event.getY();
							shouldHandleEvent = true;
							;
						}
						break;
					case MotionEvent.ACTION_CANCEL:
					case MotionEvent.ACTION_UP:
						if (mIsBeingDragged) {
							mIsBeingDragged = false;

							if (mState == RELEASE_TO_REFRESH
									&& null != mOnRefreshListener) {
								setRefreshingInternal(true);
								mOnRefreshListener.onRefresh();
							} else {
								scrollTo(0, 0);
							}
							shouldHandleEvent = true;
							;
						}
						break;
					}
				}
			}
		}
		return shouldHandleEvent;
	}

	private void updateFooterHeight(float delta) {
		// LogUtil.i(TAG, "updateFooterHeight==" + delta);
		if (mState == REFRESHING)
			return;
		if (delta < PULL_LOAD_MORE_DELTA) {
			startLoadMore();
		}
		((ListView) mRefreshableView)
				.setSelection(((ListView) mRefreshableView).getCount() - 1); // scroll
																				// to
																				// bottom
	}

	private void startLoadMore() {
		 LogUtil.i(TAG, "startLoadMore come into");
		if (!mCanPullUpLoadMore && mIsShowLoadAllData && !isShowedLoadAllDataTip) {
			onLoadAllDataComplete();
			return;
		}
		if (mIsShowLoadAllData)
			return;

		if (isOnlyHasVisibleItem())
			return;

		if (mIsLoadingMore)
			return;
		if (mState == REFRESHING || mState == RELEASE_TO_REFRESH)
			return;

		mIsLoadingMore = true;
		mFooterLayout.setState(PullToRefreshFooterView.STATE_LOADING);
		if (mOnLoadMoreListener != null) {
			mOnLoadMoreListener.onLoadMore();
		}
		// mRefreshableView.setEnabled(false);
		 LogUtil.i(TAG, "startLoadMore end");
	}

	public boolean isOnlyHasVisibleItem() {
		if ((((ListView) mRefreshableView).getChildCount() == ((ListView) mRefreshableView)
				.getCount()) && mCanPullUpLoadMore) {
			return true;
		}
		return false;
	}

	@Override
	public final boolean onInterceptTouchEvent(MotionEvent event) {

		if (!mCanPullDown) {
			return false;
		}

		if (mLastY == -1) {
			mLastY = event.getRawY();
		}

		if (isRefreshing() && mDisableScroll) {
			return true;
		}

		final int action = event.getAction();

		if (action == MotionEvent.ACTION_CANCEL
				|| action == MotionEvent.ACTION_UP) {
			mIsBeingDragged = false;
			return false;
		}

		if (action != MotionEvent.ACTION_DOWN && mIsBeingDragged) {
			return true;
		}

		switch (action) {
		case MotionEvent.ACTION_MOVE:
			final float deltaY = event.getRawY() - mLastY;
			mLastY = event.getRawY();
			// LogUtil.e(TAG, "mLastY--move--" + mLastY);
			// LogUtil.e(TAG, "mCanPullUpLoadMore--mCanPullUpLoadMore--" +
			// mCanPullUpLoadMore);
			if (mRefreshableView instanceof ListView && mIsUndoubtedListView) {
				int lastVisiblePosition = ((ListView) mRefreshableView)
						.getLastVisiblePosition();
				int childCount = ((ListView) mRefreshableView).getCount() - 1;
				if (lastVisiblePosition == childCount) {
					updateFooterHeight(deltaY);
				}
			}

			if (isReadyForPull() && deltaY > 0) {
				final float y = event.getY();
				final float dy = y - mLastMotionY;
				final float yDiff = Math.abs(dy);
				final float xDiff = Math.abs(event.getX() - mLastMotionX);

				if (yDiff > mTouchSlop && yDiff > xDiff) {
					if (dy >= THRESHOLD && isReadyForPullDown()) {
						mLastMotionY = y;
						mIsBeingDragged = true;
					}
				}
			}
			break;
		case MotionEvent.ACTION_DOWN:
			mLastY = event.getRawY();
			// LogUtil.e(TAG, "mLastY--down" + mLastY);
			if (isReadyForPull()) {
				mLastMotionY = mInitialMotionY = event.getY();
				mLastMotionX = event.getX();
				mIsBeingDragged = false;
			}
			break;
		default:
			mLastY = -1; // reset
			break;
		}

		return mIsBeingDragged;
	}

	/**
	 * 添加view到当前容器
	 * 
	 * @param context
	 *            -上下文实例
	 * @param refreshableView
	 *            - view
	 */
	protected void addRefreshableView(Context context, T refreshableView) {
		addView(refreshableView, new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, 0, 1.0f));
	}

	/**
	 * 提供给衍生类来实现返回一个创建的view,在你需要用到自定义的view的时候（例如自定义的ListView）
	 * 重写这一方法返回一个自己定义的view的实例
	 * 
	 * 注意给view设置一个ID, 特别是使用 ListActivity 或者 ListFragment
	 * 
	 * @param context
	 *            - 上下文实例
	 * @param attrs
	 *            - 封装的AttributeSet
	 * @return Refreshable View 的实例
	 */
	protected abstract T createRefreshableView(Context context,
			AttributeSet attrs);

	/**
	 * 提供给衍生类来实现返回当前View是否可以进行下拉操作，当往下话滑动的时候
	 * 
	 * @return View 的当前状态允许下拉操作 返回true（如 处于listview的顶端）
	 */
	protected abstract boolean isReadyForPullDown();

	/**
	 * 重置HeaderView的位置和 view的状态
	 */
	protected void resetHeader() {
		mState = PULL_TO_REFRESH;
		mIsBeingDragged = false;

		if (null != mHeaderLayout) {
			mHeaderLayout.reset();
		}

		scrollTo(0, 0);
	}

	public void addFooterView() {
		// if (mIsLoadingMore == false) {
		// mIsLoadingMore = true;
		// ((ListView)mRefreshableView).addFooterView(mFooterLayout);
		if (getChildCount() == 2) {
			// addView(mFooterLayout, 2, new LinearLayout.LayoutParams(
			// ViewGroup.LayoutParams.FILL_PARENT,
			// ViewGroup.LayoutParams.WRAP_CONTENT));
			System.out.println("添加footer ----");
			mFooterLayout.setState(PullToRefreshFooterView.LOADED_ALL);
			((ListView) mRefreshableView).addFooterView(mFooterLayout);
		}
		// }
	}

	public void addFooterView(ListView list) {
		// LogUtil.i(TAG, "addFooterView come into");
		mFooterLayout.setState(PullToRefreshFooterView.STATE_HIDE_ALL_VIEW);
		list.addFooterView(mFooterLayout);
	}

	/*
	 * 初始等待加载调用 *
	 */
	public void showRefreshing() {
		mState = REFRESHING;
		setRefreshingInternal(true);
		mOnRefreshListener.onRefresh();
	}

	/**
	 * 
	 * @param doScroll
	 */
	public void setRefreshingInternal(boolean doScroll) {
		mState = REFRESHING;

		if (null != mHeaderLayout) {
			mHeaderLayout.refreshing();
		}

		if (doScroll) {
			scrollTo(0, -mHeaderHeight);
		}
	}

	/**
	 * headView 滑动到目的位置
	 * 
	 * @param y
	 *            - 目标位置的纵坐标
	 */
	protected final void setHeaderScroll(int y) {
		scrollTo(0, y);
	}

	/**
	 * 初始化View
	 * 
	 * @param context
	 *            - 上下文实例
	 * @param attrs
	 *            - 封装的AttributeSet
	 */
	private void init(Context context, AttributeSet attrs) {

		setOrientation(LinearLayout.VERTICAL);

		mTouchSlop = ViewConfiguration.getTouchSlop();

		mRefreshableView = this.createRefreshableView(context, attrs);

		this.addRefreshableView(context, mRefreshableView);

		// 下拉刷新显示信息
		String pullLabel = context
				.getString(R.string.pull_to_refresh_pull_label);
		String refreshingLabel = context
				.getString(R.string.pull_to_refresh_refreshing_label);
		String releaseLabel = context
				.getString(R.string.pull_to_refresh_release_label);

		// 添加headerView
		mHeaderLayout = new PullToRefreshHeaderView(context, releaseLabel,
				pullLabel, refreshingLabel);
		addView(mHeaderLayout, 0, new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));
		measureView(mHeaderLayout);
		mHeaderHeight = mHeaderLayout.getMeasuredHeight();

		// 隐藏headerView
		setPadding(0, -mHeaderHeight, 0, 0);

		// init footer view
		mFooterLayout = new PullToRefreshFooterView(context);
	}

	/**
	 * 设置View的尺寸
	 * 
	 * @param child
	 *            -需要设置尺寸的子View
	 */
	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}

		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	/**
	 * 响应下拉的触摸事件
	 * 
	 * @return 当需要处理事件时返回true
	 */
	private boolean pullEvent() {

		final int newHeight;
		final int oldHeight = this.getScrollY();

		newHeight = Math.round(Math.min(mInitialMotionY - mLastMotionY, 0)
				/ FRICTION);

		setHeaderScroll(newHeight);

		if (newHeight != 0) {
			if (mState == PULL_TO_REFRESH
					&& mHeaderHeight < Math.abs(newHeight)) {
				mState = RELEASE_TO_REFRESH;
				mHeaderLayout.releaseToRefresh();
				return true;
			} else if (mState == RELEASE_TO_REFRESH
					&& mHeaderHeight >= Math.abs(newHeight)) {
				mState = PULL_TO_REFRESH;
				mHeaderLayout.pullToRefresh();
				return true;
			}
		}

		return oldHeight != newHeight;
	}

	/**
	 * 是否能下拉
	 * 
	 */
	private boolean isReadyForPull() {
		return isReadyForPullDown();
	}

	/**
	 * 刷新的监听器
	 */
	public static interface OnRefreshListener {
		public void onRefresh();
	}

	/**
	 * 导入更多监听
	 */
	public static interface OnLoadMoreListener {
		public void onLoadMore();
	}
}
