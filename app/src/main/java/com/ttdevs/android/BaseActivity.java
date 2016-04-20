/*
 * Created by ttdevs at 16-4-14 下午3:52.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.android;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;


import com.ttdevs.android.utils.Utils;

import butterknife.ButterKnife;

/**
 * Base Activity
 */
public class BaseActivity extends AppCompatActivity {

    private CoordinatorLayout viewRootLayout;
    private Toolbar viewToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base); // 先执行这行，然后执行子类setContentView
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        viewRootLayout = (CoordinatorLayout) findViewById(R.id.viewRootLayout);
        View content = LayoutInflater.from(this).inflate(layoutResID, null);

        int marginLayoutParams = MarginLayoutParams.MATCH_PARENT;
        MarginLayoutParams params = new MarginLayoutParams(marginLayoutParams, marginLayoutParams);
        params.topMargin = Utils.getActionBarHeight();
        viewRootLayout.addView(content, params);

        viewToolbar = (Toolbar) findViewById(R.id.viewToolbar);
        if (viewToolbar != null) {
            setSupportActionBar(viewToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 设置返回按钮可见
            getSupportActionBar().setDisplayShowHomeEnabled(true); // 设置是否显示logo图标
            getSupportActionBar().setHomeButtonEnabled(true); // 设置左上角的图标可点击
        }

        ButterKnife.bind(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed(); // go back
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
