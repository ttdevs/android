/*
 * Created by ttdevs at 16-7-11 下午2:19.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.github.utils;

import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ttdevs.github.AndroidApp;

public class LogUtils {
    public static final boolean DEBUG = true;
    public static final String TAG = ">>>>>";

    public static void debug(String msg) {
        if (DEBUG) {
            System.err.println(TAG + msg);
            Log.d(TAG, msg);
        }
    }

    public static void showToastLong(String msg) {
        Toast.makeText(AndroidApp.getContext(), msg, Toast.LENGTH_LONG).show();
    }

    public static void showToastShort(String msg) {
        Toast.makeText(AndroidApp.getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public static void showSnack(View view, String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
    }
}
