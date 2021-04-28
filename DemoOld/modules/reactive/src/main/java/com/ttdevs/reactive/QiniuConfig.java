/*
 * Created by ttdevs at 16-4-14 下午3:52.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.reactive;

import android.util.Base64;

import com.squareup.okhttp.MediaType;
import com.ttdevs.reactive.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class QiniuConfig {

    public enum Prefix {
        p, a, t
    }

    public static int MAX_SIZE = 1024 * 300; // 默认最大上传为0.5M的图片
    public static int MAX_WIDTH = 480;  //默认最大图片宽度
    public static int MAX_HEIGHT = 800; //默认最大图片高度

    public static int CODE_FLAG = Base64.NO_PADDING;

    public static final String HMAC_SHA1 = "HmacSHA1";
    public static final String CHARSET = "UTF-8";
    private static final Boolean DEBUG = true;

    private static final String API_URL = "/api/v1/uploaders/credential.json";

    private static final String DEV_URL = "http://one.iboohee.cn";
    private static final String PRO_URL = "http://one.boohee.com";

    private static final String QA_BUCKET_NAME = "onetest";
    private static final String PRO_BUCKET_NAME = "bohe-one";

    private static final String DEV_KEY = "d154bc1a7f6e4581";
    private static final String DEV_SECRET = "14588f1bdda13a05510e15a8c2b657c13ed979f2";
//    private static final String DEV_KEY = "1a00a324f67b4c20"; // iOS
//    private static final String DEV_SECRET = "3ee3abf560c533673315711bb84159290a5a719d"; // iOS

    private static final String PRO_KEY = "e9735690d8fedf34";
    private static final String PRO_SECRET = "1c34b98ff74f965de7ab4b614078c6b5c50d1562";


    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static String getURL() {
        return (DEBUG ? DEV_URL : PRO_URL) + API_URL;
    }

    private static String getBucketKey() {
        return DEBUG ? QA_BUCKET_NAME : PRO_BUCKET_NAME;
    }

    private static String getAppKey() {
        return DEBUG ? DEV_KEY : PRO_KEY;
    }

    private static String getSecretKey() {
        return DEBUG ? DEV_SECRET : PRO_SECRET;
    }

    /**
     * <pre>
     *      context_params: "{'bucket': 'onetest', 'key': 'asdasdasdas'}" 经过base64编码
     *      sign: "#{app_key}#{context_params}" 拼接起来后使用secret_key签名，再对签名结果base64编码
     * </pre>
     *
     * @return
     */
    public static String getRequestBody(String key) {
        JSONObject body = new JSONObject();
        JSONObject context = new JSONObject();
        try {
            context.put("bucket", getBucketKey());
            context.put("key", key);

            String contextStr = contextParams(context.toString());
            body.put("context_params", contextStr);
            body.put("sign", signature(contextStr));

            LogUtils.debug(key);
            LogUtils.debug(contextStr);
            LogUtils.debug(signature(contextStr));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return body.toString();
    }

    public static String createFileName(Prefix prefix) {
        String name = java.util.UUID.randomUUID().toString();
        return String.format("%s/%s/%s", prefix.name(), getDateString(), name);
    }

    private static String getDateString() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        try {
            return df.format(new Date());
        } catch (Exception e) {

        }
        return "1970/01/01";
    }

    /**
     * 上下文
     *
     * @param json
     * @return
     */
    private static String contextParams(String json) {
        try {
            return Base64.encodeToString(json.getBytes(CHARSET), CODE_FLAG);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 参数签名
     *
     * @param contextParams
     * @return
     */
    public static String signature(String contextParams) {
        String secret = getSecretKey();
        try {
            String dataStr = getAppKey() + contextParams;
            byte[] dataBytes = encryptHMAC(dataStr.getBytes(), secret);
            return Base64.encodeToString(dataBytes, CODE_FLAG);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * HMAC加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    private static byte[] encryptHMAC(byte[] data, String key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1);
        Mac mac = Mac.getInstance(HMAC_SHA1);
        mac.init(secretKey);
        return mac.doFinal(data);
    }
}
