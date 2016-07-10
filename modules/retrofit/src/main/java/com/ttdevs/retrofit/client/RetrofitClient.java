/*
 * Created by ttdevs at 16-7-8 下午3:54.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.retrofit.client;

import com.ttdevs.retrofit.RetrofitManager;

/**
 * Github
 */
public class RetrofitClient {
    public static final String GIT_HUB = "http://api.github.com/";

    public static <T> T createService(String url, Class<T> serviceClass) {
        return RetrofitManager.getRetrofit(url).create(serviceClass);
    }
}
