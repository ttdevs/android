/*
 * Created by ttdevs at 16-8-18 下午4:45.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.hybrid.expand;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ttdevs.hybrid.R;

/**
 * Created by ttdevs on 16/8/18.
 */
public class CouponItemView extends RelativeLayout {
    View viewDescription;
    TextView tvMoney, tvLimitProduction, tvValid, tvLimitMoney, tvDescription;
    CheckBox cbStatus;

    public CouponItemView(Context context) {
        this(context, null);
    }

    public CouponItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CouponItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.item_view_coupon, this);

        cbStatus = (CheckBox) findViewById(R.id.view_status);
        tvMoney = (TextView) findViewById(R.id.tv_money);
        tvLimitProduction = (TextView) findViewById(R.id.tv_limit_money);
        tvValid = (TextView) findViewById(R.id.tv_valid);
        tvLimitMoney = (TextView) findViewById(R.id.tv_limit_money);
        tvDescription = (TextView) findViewById(R.id.tv_description);
        viewDescription = findViewById(R.id.view_description);

        cbStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                viewDescription.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });
    }

    public void setMoney(int money) {
        tvMoney.setText(String.valueOf(money));
    }

    public void setLimitProduction(String value) {
        tvLimitProduction.setText(value);
    }

    public void setValid(String value) {
        tvValid.setText(value);
    }

    public void setLimitMoney(String value) {
        tvLimitMoney.setText(value);
    }

    public void setDescription(String value) {
        tvDescription.setText(value);
    }
}
