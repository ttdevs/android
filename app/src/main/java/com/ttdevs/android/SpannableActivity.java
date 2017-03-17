package com.ttdevs.android;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

public class SpannableActivity extends AppCompatActivity {

    private TextView tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spannable);

        tvContent = (TextView)findViewById(R.id.tvContent);

        testSpannable();
    }

    private void testSpannable() {
        String desc = "这里是测试文字 1 份";
        SpannableStringBuilder builder = new SpannableStringBuilder(desc);
        ForegroundColorSpan span = new ForegroundColorSpan(Color.RED);
        int before = desc.indexOf(" ");
        int after = desc.lastIndexOf(" ");
        builder.setSpan(span, before, after, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        tvContent.setText(builder);
    }
}
