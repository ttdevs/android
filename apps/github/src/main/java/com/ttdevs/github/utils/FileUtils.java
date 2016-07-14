/*
 * Created by ttdevs at 16-7-11 下午2:19.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.github.utils;

import android.content.res.AssetManager;
import android.os.Environment;

import com.ttdevs.github.AndroidApp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {

    public static final String BasePathName = "ttdevs";

    /**
     * 获取项目文件根目录，例如：/sdcard/demo/
     *
     * @return
     */
    public static final String getBaseFileDir() {
        String path = Environment.getExternalStorageDirectory() + File.separator + BasePathName + File.separator;
        File pathFile = new File(path);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        return path;
    }

    /**
     * 读取Assets
     *
     * @param fileName
     * @return
     */
    public static String readAssets(String fileName) {
        InputStream inputStream = null;
        try {
            AssetManager assets = AndroidApp.getContext().getAssets();
            inputStream = assets.open(fileName);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            return new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            inputStream = null;
        }
        return null;
    }
}
