/*
 * Created by ttdevs at 16-9-23 上午10:31.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.android;

import android.os.Bundle;
import android.view.View;

import com.ttdevs.android.utils.Utils;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class CustomViewActivity extends BaseActivity {

    @OnClick({R.id.btSweepGradient, R.id.btEndless,
            R.id.btDrawCircle, R.id.btHybridView})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btSweepGradient:
                Utils.comeOnBaby(this, SweepGradientActivity.class);
                break;
            case R.id.btEndless:
                Utils.comeOnBaby(this, EndLessActivity.class);
                break;
            case R.id.btDrawCircle:
                Utils.comeOnBaby(this, IndicatorViewActivity.class);
                break;
            case R.id.btHybridView:
                Utils.comeOnBaby(this, HybridViewActivity.class);
                break;

            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view);
        ButterKnife.bind(this);
    }
}
