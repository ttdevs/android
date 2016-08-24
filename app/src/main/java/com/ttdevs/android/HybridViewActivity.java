/*
 * Created by ttdevs at 16-8-15 上午10:37.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.android;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ttdevs.hybrid.scroller.HomeView;

public class HybridViewActivity extends BaseActivity {
    public static final String URL = "http://one.boohee.com/api/v1/goods/908/detail.html?app_device=Android&os_version=5.1.1&app_version=5.5.9-debug&version_code=104&channel=&app_key=one&token=ypPePNVnyzdJaezP3tzh";

    private HomeView mHomeView;
    private EditText etLength;
    private Button btReload;
    private TextView tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hybrid_view);

        mHomeView = (HomeView) findViewById(R.id.homeView);
        etLength = (EditText) findViewById(R.id.et_length);

        btReload = (Button) findViewById(R.id.bt_reload);
        btReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHeaderView();
                mHomeView.loadUrl(URL);
            }
        });
    }

    private void setHeaderView() {
        if (null == tvContent) {
            tvContent = new TextView(this);
            tvContent.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            tvContent.setGravity(Gravity.CENTER_HORIZONTAL);

            mHomeView.setHeaderView(tvContent);
        }

        int length = Integer.parseInt(etLength.getText().toString());
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(String.format("Index: %d \n", i));
        }

        tvContent.setText(builder.toString());
        tvContent.invalidate();
        mHomeView.invalidate();
    }
}
