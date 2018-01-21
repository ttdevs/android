package com.ttdevs.dagger;

import android.os.Bundle;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class MainActivity extends BaseActivity {

    @Inject
    Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);  //一处声明，处处依赖注入
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        DaggerA01SimpleComponent.builder()
//                .build()
//                .inject(this);

    }
}
