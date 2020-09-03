/*
 * Created by ttdevs at 16-4-14 下午3:52.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.reactive.utils;

import android.util.Log;

public class LogUtils {

    public static final boolean DEBUG = true;
    public static final String TAG = ">>>>>";

    public static void debug(String msg) {
        if (DEBUG) {
            Log.d(TAG, msg);
        }
    }

    public static void print(String msg) {
        System.out.println(TAG + msg);
    }
}
