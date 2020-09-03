/*
 * Created by ttdevs at 16-4-14 下午3:52.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.android;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ttdevs.android.tablayout.TabFragment;

import java.util.ArrayList;
import java.util.List;

public class TabLayoutActivity extends BaseActivity {

    private List<Fragment> mFragmentList = new ArrayList<>();

    private TabLayout tableLayout;
    private ViewPager viewPager;
    private ApnFragmentAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_layout);

        mFragmentList.add(new TabFragment());
        mFragmentList.add(new TabFragment());

        tableLayout = (TabLayout)findViewById(R.id.tableLayout);
        viewPager = (ViewPager)findViewById(R.id.viewPager);

        mAdapter = new ApnFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);
        tableLayout.setupWithViewPager(viewPager);
        tableLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    private class ApnFragmentAdapter extends FragmentPagerAdapter {
        CharSequence[] TITLE = new CharSequence[]{"TabOne", "TabTwo"};

        public ApnFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return TITLE.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLE[position];
        }
    }
}
