/*
 * Created by ttdevs at 16-4-14 下午3:52.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.android.utils;

import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;

import com.ttdevs.android.AndroidApplication;

public class Utils {
    /**
     * 启动Activity
     *
     * @param context
     * @param cls
     */
    public static void comeOnBaby(Context context, Class<?> cls) {
        if (null == context) { // TODO use Application Context
            LogUtils.showToastLong("context is null");
            return;
        }
        if (null == cls) {
            LogUtils.showToastLong("cls is null");
            return;
        }
        context.startActivity(new Intent(context, cls));
    }

    /**
     * 获取 ActionBar的高度
     * android:layout_height="?attr/actionBarSize";
     *
     * @return
     */
    public static int getActionBarHeight() {
        Context context = AndroidApplication.getContext();
        TypedValue tv = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true);
        return context.getResources().getDimensionPixelSize(tv.resourceId);
    }
}
