/*
 * Created by ttdevs at 16-6-7 下午4:53.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.android;

import android.os.Bundle;
import android.view.View;

import com.ttdevs.circleview.CircleIndicator;
import com.ttdevs.circleview.CircleProgress;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CircleViewActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.ci_1)
    CircleIndicator ci1;

    @Bind(R.id.cp_1)
    CircleProgress cp1;
    @Bind(R.id.cp_2)
    CircleProgress cp2;
    @Bind(R.id.cp_3)
    CircleProgress cp3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cicle_view);
        ButterKnife.bind(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_progress:
                testProgress();
                break;
            case R.id.bt_indicator:
                testIndicator();
                break;

            default:
                break;
        }
    }

    private void testProgress() {
        String title = "身体年龄";
        String content = "23";
        String unit = "岁";
        String alert = "显年轻4岁";
        cp1.setContent(title, content, unit, alert);
        cp1.setIndicatorValue(10f, 60f, 33f, 29f, 20, 30, 40, 50);
        cp2.setContent(title, content, unit, alert);
        cp2.setIndicatorValue(10f, 60f, 33f, 29f, 20, 30, 40, 50);
        cp3.setContent(title, content, unit, alert);
        cp3.setIndicatorValue(10f, 60f, 33f, 29f, 20, 30, 40, 50);
    }

    private void testIndicator() {

    }
}
