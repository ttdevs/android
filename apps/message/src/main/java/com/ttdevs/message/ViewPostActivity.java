package com.ttdevs.message;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ViewPostActivity extends AppCompatActivity {
    private TextView tvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);

        printMsg("onCreate");

        tvInfo = (TextView) findViewById(R.id.tvInfo);
        tvInfo.post(new Runnable() {
            @Override
            public void run() {
                printMsg("Width:" + tvInfo.getWidth());
                printMsg("Height:" + tvInfo.getHeight());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        printMsg("onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();

        printMsg("onResume");
    }

    private static void printMsg(String msg) {
        System.out.println(">>>>>" + msg);
    }
}
