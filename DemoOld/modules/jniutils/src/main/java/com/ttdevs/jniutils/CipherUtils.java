/*
 * Created by ttdevs at 16-4-14 下午3:52.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.jniutils;

import android.content.Context;

public class CipherUtils {

    static {
        System.loadLibrary("CipherUtils");
    }

    /**
     * base64 编码
     *
     * @param dataString
     * @return
     */
    public static native String base64Encode(String dataString);

    /**
     * 签名认证
     *
     * @param context
     */
    public static native boolean authenticate(Context context);

    /**
     * 创建本地加密的Key
     *
     * @return
     */
    public static native String createCipherKey();
}
