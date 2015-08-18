package com.oraro.stardream.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.actionbarsherlock.app.SherlockFragment;
import com.oraro.stardream.R;
import com.oraro.stardream.adapter.MyFragmentPagerAdapter;

public class MainContainerActivity extends SherlockFragment {
	
	//viewPager
	private ViewPager containerVpager;
	private List<Fragment> subViews = new ArrayList<Fragment>();
	private MyFragmentPagerAdapter containerPagerAdapter;
	private RadioGroup tabRG;
	
	
	public static MainContainerActivity newInstance() {
		MainContainerActivity newFragment = new MainContainerActivity();
	        return newFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View parentView = inflater.inflate(R.layout.activity_main_container, container,
				false);
		initPagerView(parentView);
		return parentView;
	}
	
	private void initPagerView(View parentView) {
		Fragment square = SquareFragment.newInstance();
		Fragment square1 = JobListFragment.newInstance();
		Fragment square2 = SquareFragment.newInstance();
		subViews.add(square);
		subViews.add(square1);
		subViews.add(square2);

		tabRG = (RadioGroup) parentView.findViewById(R.id.rg_main_topTab);
		tabRG.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_main_tab_one:
					containerVpager.setCurrentItem(0);
					break;
					
				case R.id.rb_main_tab_two:
					containerVpager.setCurrentItem(1);
					break;
					
				case R.id.rb_main_tab_three:
					containerVpager.setCurrentItem(2);
					break;

				default:
					break;
				}
				
			}
		});
		containerVpager = (ViewPager) parentView.findViewById(R.id.vp_main_container);
		containerPagerAdapter = new MyFragmentPagerAdapter(
				getActivity().getSupportFragmentManager(), subViews);
		containerVpager.setAdapter(containerPagerAdapter);
		containerVpager.setCurrentItem(0);
		 containerVpager.setOnPageChangeListener(new
		 MyOnPageChangeListener());
	}
	
	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int arg0) {
			((RadioButton)tabRG.getChildAt(arg0)).setChecked(true);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}


	
}