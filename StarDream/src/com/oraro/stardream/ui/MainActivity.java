package com.oraro.stardream.ui;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.oraro.stardream.R;
import com.oraro.stardream.adapter.HomeMenuAdapter;
import com.oraro.stardream.util.DensityUtil;

public class MainActivity extends SherlockFragmentActivity {

	public static final String FragmentNUM = "Fragment_number";

	private Context mContext;

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private RelativeLayout headView;

	// headview
	private ImageView header_userpic;
	private TextView header_name;
	private TextView header_remark;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private HomeMenuAdapter mMenuAdapter;
	private String[] mMenuArray;

	private Fragment DynamicFra, NewsFra, MsgFra, SettingsFra;
	private Bundle DynamicArgs, NewsArgs, MsgArgs, SettingsArgs;

	private Intent intent;
	private ArrayList<String> mMenuStrList;

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		initDrawer();
		initActionBar();
		selectItem(1);
	}

	private void initView() {
		mContext = this;
		mTitle = mDrawerTitle = getTitle();
		mMenuArray = getResources().getStringArray(R.array.mune_array);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerLayout.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
			}
		});
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		mDrawerList.setDivider(getResources().getDrawable(
				R.drawable.home_menu_listline));

	}

	private void initDrawer() {
		initMenuHeader();
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		mMenuStrList = new ArrayList<String>();
		for (int i = 0; i < mMenuArray.length; i++) {
			mMenuStrList.add(mMenuArray[i]);
		}
		mMenuAdapter = new HomeMenuAdapter(mContext);
		mMenuAdapter.appendToList(mMenuStrList);
		mDrawerList.setAdapter(mMenuAdapter);
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
	}

	private void initMenuHeader() {
		headView = (RelativeLayout) LayoutInflater.from(this).inflate(
				R.layout.home_menu_list_header, null);
		header_userpic = (ImageView) headView
				.findViewById(R.id.home_menu_header_userpic);
		header_name = (TextView) headView
				.findViewById(R.id.home_menu_header_name);
		header_remark = (TextView) headView
				.findViewById(R.id.home_menu_header_remark);
		headView.setLayoutParams(new AbsListView.LayoutParams(
				LayoutParams.MATCH_PARENT, DensityUtil.dip2px(this, 100)));
		mDrawerList.addHeaderView(headView);
	}

	private void initActionBar() {
		getSupportActionBar().setHomeButtonEnabled(true);// home是否可以点击
		getSupportActionBar().setDisplayShowHomeEnabled(true);// logo
		getSupportActionBar().setDisplayShowTitleEnabled(true);// 是否显示title
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);// 返回按钮
		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.action_bar_dra));
		getSupportActionBar().setLogo(R.drawable.icon_menu);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {
			public void onDrawerClosed(View view) {
				getSupportActionBar().setTitle(mTitle);
				supportInvalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(mDrawerTitle);
				supportInvalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if (mDrawerLayout.isDrawerVisible(GravityCompat.START)) {
				mDrawerLayout.closeDrawer(GravityCompat.START);
			} else {
				mDrawerLayout.openDrawer(GravityCompat.START);
			}
			break;

		case R.id.action_write_daodao:
			break;
		default:
			break;
		}

		return false;
	}

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	private void selectItem(int position) {
		switch (position) {
		case 0:
			break;
		case 1:
			DynamicFra = MainContainerActivity.newInstance();
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.content_frame, DynamicFra).commit();
			mDrawerList.setItemChecked(position, true);
			setTitle(mMenuArray[position - 1]);
			mDrawerLayout.closeDrawer(mDrawerList);
			break;
		default:
			break;
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getSupportActionBar().setTitle(mTitle);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}