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
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;

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

    /**
     * 获取手机信息
     *
     * @param context
     */
    public static void printSystemInfo(Context context) {
        try {
            // MAC地址(如:1C:B0:94:ED:6C:AC)
            WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = manager.getConnectionInfo();
            String macAddress = info.getMacAddress();

            // imei maybe null
            TelephonyManager TelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String imei = TelephonyMgr.getDeviceId();

        } catch (Exception e) {
            e.printStackTrace();
        }

        int sdk = Build.VERSION.SDK_INT;// API版本号(如:14)
        String version = Build.VERSION.RELEASE; // 系统版本号(如:4.0)
        String model = Build.MODEL;// 手机型号(如:Galaxy Nexus)
        String serial = Build.SERIAL;
    }
}
