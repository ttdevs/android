/*
 * Created by ttdevs at 16-4-14 下午3:52.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.jniutils;

import android.content.Context;

import com.ttdevs.jniutils.demo.JavaClass;

public class CipherUtils {

    static {
        System.loadLibrary("CipherUtils");
    }

    public static native String getCipherKey(JavaClass clazz);

    public static native String signature(String key);

    public static native int add(int x, int y);

    public static native void setModule(boolean isDebug);

    public static native void getAppSign(Context context);
}
