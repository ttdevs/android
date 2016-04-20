/*
 * Created by ttdevs at 16-4-14 下午3:52.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

/*
 * Created by TedYin 5/7/15 2:19 PM.
 * Copyright (c) 2015 Boohee, Inc. All rights reserved.
 */

package com.ttdevs.reactive.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Bitmap工具类
 * Created by ted on 5/7/15.
 */
public class BitmapUtils {

    /**
     * 压缩图片，现根据储存进行等比例压缩，然后根据大小进行质量压缩
     *
     * @param path    图片路径
     * @param maxSize 最大图片大小
     * @param rqsW    要压缩到的宽度
     * @param rqsH    要压缩到的高度
     * @return 压缩后的图片byte数组
     */
    public static byte[] compressBitmap(String path, int maxSize, int rqsW, int rqsH) {
        if (TextUtils.isEmpty(path)) return null;
        try {
            // 首先根据图片制定的尺寸大小来压缩
            WeakReference<Bitmap> bitmap = cprsBmpBySize(path, rqsW, rqsH);
            // 进行自动旋转
            WeakReference<Bitmap> rotatedBmp = autoRotateBitmap(path, bitmap);
            if (bitmap != null && rotatedBmp != null && bitmap.get() != rotatedBmp.get()) {
                bitmap.get().recycle();
                bitmap.clear();
            }
            // 然后根据图片的size大小来进行图片的质量压缩
            return cprsBmpByQuality(rotatedBmp, maxSize);
        } catch (Exception o) {
            return null;
        }
    }

    /**
     * 根据指定大小,进行质量压缩
     *
     * @param bmp     目标图片
     * @param maxSize 最大的图片尺寸单位
     * @return 压缩后的byte数组
     */
    private static byte[] cprsBmpByQuality(WeakReference<Bitmap> bmp, int maxSize) {
        if (bmp == null || bmp.get() == null) return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.get().compress(Bitmap.CompressFormat.JPEG, 70, baos);
        bmp.get().recycle();
        return baos.toByteArray();
    }

    /**
     * 根据指定大小，按比例压缩图片
     *
     * @param path bitmap source path
     * @return Bitmap {@link Bitmap}
     */
    private static WeakReference<Bitmap> cprsBmpBySize(String path, int rqsW, int rqsH) {
        if (TextUtils.isEmpty(path)) return null;
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = caculateInSampleSize(options, rqsW, rqsH);
        options.inJustDecodeBounds = false;
        return new WeakReference<Bitmap>(BitmapFactory.decodeFile(path, options));
    }

    /**
     * caculate the bitmap sampleSize
     *
     * @return inSampleSize
     */
    private static int caculateInSampleSize(BitmapFactory.Options options, int rqsW, int rqsH) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (rqsW == 0 || rqsH == 0) return 1;
        if (height > rqsH || width > rqsW) {
            final int heightRatio = Math.round((float) height / (float) rqsH);
            final int widthRatio = Math.round((float) width / (float) rqsW);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * 压缩指定路径图片，并将其保存在缓存目录中
     *
     * @param context 上下文
     * @return 压缩后的图片文件
     */
    public static File cprsBmpToFile(Context context, String path, int maxSize, int rqsW, int rqsH) {
        byte[] bytes = compressBitmap(path, maxSize, rqsW, rqsH);
        if (bytes == null || bytes.length == 0) return null;
        File srcFile = new File(path);
        String desPath = getImageCacheDir(context) + srcFile.getName();
        File file = new File(desPath);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes);
            fos.close();
        } catch (Exception e) {
            file = null;
        }
        return file;
    }

    /**
     * 获取图片缓存路径
     *
     * @param context 上下文
     * @return 缓存路径
     */
    private static String getImageCacheDir(Context context) {
        String dir = context.getCacheDir() + "cache" + File.separator;
        File file = new File(dir);
        if (!file.exists()) file.mkdirs();
        return dir;
    }

    /**
     * 获取图片的BitmapFactory.Options对象
     *
     * @param path 图片路径
     * @return BitmapFactory.Options对象
     */
    public static BitmapFactory.Options getBitmapOptions(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false;
        return options;
    }

    /**
     * 获取图片的旋转角度
     *
     * @param path 图片路径
     * @return 图片的角度
     */
    public static int getDegress(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转图片到指定角度
     *
     * @param bitmap  需要调整方向的图片
     * @param degress 需要旋转的角度
     * @return 调整后的图片
     */
    public static WeakReference<Bitmap> rotateBitmap(WeakReference<Bitmap> bitmap, int degress) {
        if (bitmap == null || bitmap.get() == null) return null;
        Matrix m = new Matrix();
        m.postRotate(degress);
        return new WeakReference<Bitmap>(Bitmap.createBitmap(bitmap.get(), 0, 0, bitmap.get().getWidth()
                , bitmap.get().getHeight(), m, true));
    }

    /**
     * 将图片转为正向
     *
     * @param path 图片路径
     * @param bmp  图片路径对应的图片对象
     * @return 返回旋转后的图片
     */
    public static WeakReference<Bitmap> autoRotateBitmap(String path, WeakReference<Bitmap> bmp) {
        if (TextUtils.isEmpty(path) || bmp == null || bmp.get() == null) return bmp;
        int degree = getDegress(path);
        if (degree != 0) {
            bmp = rotateBitmap(bmp, degree);
        }
        return bmp;
    }
}
