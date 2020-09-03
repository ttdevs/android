/*
 * Created by ttdevs at 16-4-14 下午3:52.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.android;

import android.app.Application;
import android.content.Context;

import com.ttdevs.android.conceal.ConcealUtil;

public class AndroidApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;

        initApplication();
    }

    private void initApplication() {
        ConcealUtil.init(mContext);
    }

    public static Context getContext() {
        return mContext;
    }
}
