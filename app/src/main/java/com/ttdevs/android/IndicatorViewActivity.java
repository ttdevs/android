/*
 * Created by ttdevs at 16-6-7 下午4:53.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.android;

import android.os.Bundle;
import android.view.View;

import com.ttdevs.indicator.CircleIndicator;
import com.ttdevs.indicator.CircleProgress;
import com.ttdevs.indicator.IndicatorItem;
import com.ttdevs.indicator.LineIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IndicatorViewActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.ci_1)
    CircleIndicator ci1;

    @BindView(R.id.cp_1)
    CircleProgress cp1;
    @BindView(R.id.cp_2)
    CircleProgress cp2;
    @BindView(R.id.cp_3)
    CircleProgress cp3;

    @BindView(R.id.li_progress)
    LineIndicator liProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cicle_view);
        ButterKnife.bind(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_line:
                testLineProgress();
                break;
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

    private void testLineProgress() {
        String leftAlert = "开始";
        String leftContent = "60.0公斤";
        String rightAlert = "目标";
        String rightContent = "50.0公斤";
        liProgress.setContent(leftAlert, leftContent, rightAlert, rightContent);
        liProgress.setIndicator(60f, 50.0f, 55);
    }

    private void testProgress() {
        String title = "身体年龄";
        String content = "23";
        String unit = "岁";
        String alert = "显年轻4岁";
        cp1.setContent(title, content, unit, alert);
        cp1.setIndicatorValue(10f, 60f, 33f, "实际年龄", 29f, 20, 30, 40, 50);
        cp2.setContent(title, content, unit, alert);
        cp2.setIndicatorValue(10f, 60f, 33f, 29f, 20, 30, 40, 50);
        cp3.setContent(title, content, unit, alert);
        cp3.setIndicatorValue(10f, 60f, 33f, 29f, 20, 30, 40, 50);
    }

    private void testIndicator() {
        int mCircleGreen = getResources().getColor(com.ttdevs.indicator.R.color.circle_green);
        int mCircleYellow = getResources().getColor(com.ttdevs.indicator.R.color.circle_yellow);
        int mCircleRed = getResources().getColor(com.ttdevs.indicator.R.color.circle_red);

        List<IndicatorItem> dividerIndicator = new ArrayList<>();
        IndicatorItem item1 = new IndicatorItem();
        item1.start = 5;
        item1.end = 13;
        item1.value = "过低";
        item1.color = mCircleYellow;
        dividerIndicator.add(item1);

        IndicatorItem item2 = new IndicatorItem();
        item2.start = 13;
        item2.end = 20;
        item2.value = "正常";
        item2.color = mCircleGreen;
        dividerIndicator.add(item2);

        IndicatorItem item3 = new IndicatorItem();
        item3.start = 20;
        item3.end = 35;
        item3.value = "过高";
        item3.color = mCircleYellow;
        dividerIndicator.add(item3);

        IndicatorItem item4 = new IndicatorItem();
        item4.start = 35;
        item4.end = 60;
        item4.value = "超级高";
        item4.color = mCircleRed;
        dividerIndicator.add(item4);

        String title = "体脂率";
        String content = "23";
        String unit = "％";
        String alert = "体脂过高";
        ci1.setContentColor(mCircleRed, mCircleRed);
        ci1.setContent(title, content, unit, alert);
        ci1.setIndicatorValue(dividerIndicator, 19);
    }
}
