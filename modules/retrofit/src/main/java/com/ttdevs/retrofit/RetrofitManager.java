/*
 * Created by ttdevs at 16-7-8 上午10:35.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.retrofit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ttdevs on 16/7/8.
 */
public class RetrofitManager {

    private static final String GIT_HUB = "http://api.github.com/";

    private static final OkHttpClient CLIENT = new OkHttpClient.Builder()
            .addInterceptor(new HeadersInterceptor())
            .build();

    private static final Retrofit.Builder BUILDER = new Retrofit.Builder()
            .client(CLIENT)
            .addConverterFactory(GsonConverterFactory.create());

    public static Retrofit getRetrofit(String url) {
        return BUILDER.baseUrl(url).build();
    }

//    public static <T> T createService(Class<T> serviceClass) {
//        return getRetrofit(GIT_HUB).create(serviceClass);
//    }
}
