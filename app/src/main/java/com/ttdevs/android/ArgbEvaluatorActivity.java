package com.ttdevs.android;

import android.animation.ArgbEvaluator;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

public class ArgbEvaluatorActivity extends BaseActivity implements SeekBar.OnSeekBarChangeListener {

    private int mStartColor = Color.parseColor("#FFFE8D60");
    private int mEndColor = Color.parseColor("#FF69D37B");

    private View viewMain;
    private SeekBar seekBar;

    private ArgbEvaluator mEvaluator = new ArgbEvaluator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_argb_evaluator);

        viewMain = findViewById(R.id.viewMain);

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(this);

        updateBackground(seekBar.getProgress());
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        updateBackground(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private void updateBackground(int progress){
        float fraction = progress / 100.0f;
        int color = (int)mEvaluator.evaluate(fraction, mStartColor, mEndColor);
        viewMain.setBackgroundColor(color);
    }
}
