/*
 * Created by ttdevs at 16-9-20 下午5:41.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.android;

import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ttdevs.android.view.CircleProgress;

public class SweepGradientActivity extends BaseActivity {
    private SeekBar sbProgress;
    private CircleProgress cpProgress;
    private TextView tvValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sweep_gradient);

        sbProgress = (SeekBar) findViewById(R.id.sbProgress);
        tvValue = (TextView) findViewById(R.id.tvValue);
        cpProgress = (CircleProgress) findViewById(R.id.cpProgress);
        sbProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cpProgress.setProgress(progress / 100f);
                tvValue.setText(String.valueOf(cpProgress.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
