/*
 * Created by ttdevs at 16-4-14 下午3:52.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.android.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

public class SysUtils {

    /**
     * 获取apk的签名,一长串数字
     *
     * @param context 上下文
     * @return 签名数字串
     */
    public static String getAppSignature(Context context) {
        String result = null;
        try {
            PackageManager manager = context.getPackageManager();
            String pkgName = context.getPackageName();
            PackageInfo packageInfo = manager.getPackageInfo(pkgName, PackageManager.GET_SIGNATURES);
            Signature[] signatures = packageInfo.signatures;
            if (null != signatures && signatures.length > 0) {
                Signature signature = signatures[0];
                int hashCode = signature.hashCode();
                result = signature.toCharsString();
                LogUtils.debug(result);
                System.out.println(hashCode + "|" + result);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }
}
