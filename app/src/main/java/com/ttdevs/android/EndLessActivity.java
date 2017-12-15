/*
 * Created by ttdevs at 16-4-14 下午3:52.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.android;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.ttdevs.android.endless.LinearLayoutFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class EndLessActivity extends BaseActivity {

    @BindView(R.id.tableLayout)
    TabLayout tableLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private List<Fragment> mFragmentList = new ArrayList<>();
    private final CharSequence[] TITLE = new CharSequence[]{"Line", "Grid"};

    private FragmentAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_less);

        mFragmentList.add(new LinearLayoutFragment());
        mFragmentList.add(new LinearLayoutFragment());

        tableLayout = (TabLayout) findViewById(R.id.tableLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        mAdapter = new FragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);
        tableLayout.setupWithViewPager(viewPager);
        tableLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    private class FragmentAdapter extends FragmentPagerAdapter {

        public FragmentAdapter(FragmentManager fm) {
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
