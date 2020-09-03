/*
 * Created by ttdevs at 16-4-14 下午3:52.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.android;

import android.os.Bundle;
import android.widget.ViewFlipper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewFlipperActivity extends BaseActivity {

    @BindView(R.id.vf_notice)
    ViewFlipper vfNotice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_fillper);
        ButterKnife.bind(this);

        vfNotice.setFlipInterval(3000);
        vfNotice.startFlipping();
    }
}
