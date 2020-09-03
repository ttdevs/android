package com.ttdevs.ipc.utils;

import android.content.Intent;

/**
 * This is Constant
 *
 * @author : ttdevs huahu_wu@human-horizons.com
 * @date : 2020/09/02
 */
public class Constant {
    public static final String SERVER_ACTION = "com.ttdevs.ipc";
    public static final String SERVER_PKG_NAME = "com.ttdevs.ipc.server";
    public static final String SERVER_CLASS_NAME = "com.ttdevs.ipc.server.StudentService";

    /**
     * Intent for connect to server
     *
     * @return
     */
    public static final Intent getServerIntent() {
        Intent intent = new Intent(SERVER_ACTION);
        intent.setClassName(SERVER_PKG_NAME, SERVER_CLASS_NAME);
        return intent;
    }
}
