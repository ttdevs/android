package com.ttdevs.dagger;

import dagger.Module;

/**
 * Created by ttdevs on 22/01/2018.
 */
@Module
public class A01SimpleModule {
    private MainActivity activity;

    public A01SimpleModule(MainActivity activity) {
        this.activity = activity;
    }
}
