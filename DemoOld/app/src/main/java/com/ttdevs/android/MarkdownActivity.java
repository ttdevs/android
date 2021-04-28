/*
 * Created by ttdevs at 16-4-14 下午3:52.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.android;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.ttdevs.android.utils.LogUtils;
import com.ttdevs.markdown.MarkDownView;

import butterknife.BindView;
import butterknife.OnClick;

public class MarkdownActivity extends BaseActivity {

    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.mdv_content)
    MarkDownView mdvContent;

    @OnClick({R.id.bt_load})
    public void onClick(View view) {
        String content = etContent.getEditableText().toString();
        if (TextUtils.isEmpty(content)) {
            LogUtils.showSnack(etContent, "Content is null!");
            return;
        }
        mdvContent.loadMarkdown(content);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_markdown);

    }
}
