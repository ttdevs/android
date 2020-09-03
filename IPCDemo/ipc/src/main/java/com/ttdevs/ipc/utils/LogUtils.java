package com.ttdevs.ipc.utils;

import android.text.TextUtils;
import android.util.Log;

/**
 * This is LogUtils
 *
 * @author : ttdevs@gmail.com
 * @date : 2020/09/02
 */
public class LogUtils {
    private static String mTag = "IPC";

    /**
     * Init log tag
     *
     * @param tag
     */
    public static void init(String tag) {
        mTag = tag;
    }

    /**
     * Debug msg
     *
     * @param msg
     */
    public static void d(String msg) {
        if (TextUtils.isEmpty(msg)) {
            msg = "";
        }
        Log.d(mTag, msg);
    }
}
