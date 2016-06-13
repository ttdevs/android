/*
 * Created by ttdevs at 16-6-7 下午4:53.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.android;

import android.os.Bundle;
import android.view.View;

import com.ttdevs.circleview.CircleProgress;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CircleViewActivity extends BaseActivity implements View.OnClickListener {

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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_progress:
                cp1.animateProgress(45);
                break;

            default:
                break;
        }
    }
}
