package com.ttdevs.ipc.server;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ttdevs.ipc.utils.LogUtils;

/**
 * This is MainActivity
 *
 * @author : ttdevs@gmail.com
 * @date : 2020/08/31
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LogUtils.init(">>>>>Server ");

        onBackPressed();
    }
}