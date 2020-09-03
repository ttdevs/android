/*
 * Created by ttdevs at 16-7-8 上午10:35.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ttdevs.retrofit.client.DefaultInterceptor;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ttdevs on 16/7/8.
 */
public class RetrofitManager {

    private static final String GIT_HUB = "https://api.github.com/";

    public static OkHttpClient getDefaultClient() {
        return getClient(null);
    }

    public static OkHttpClient getClient(Interceptor... interceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (null != interceptor && interceptor.length > 0) {
            for (int i = 0; i < interceptor.length; i++) {
                builder.addInterceptor(interceptor[i]);
            }
        }
        return builder.build();
    }

    public static Retrofit getRetrofit() {
        return getRetrofit(GIT_HUB);
    }

    public static Retrofit getRetrofit(String url) {
        return getRetrofit(url, getDefaultClient());
    }

    public static Retrofit getRetrofit(OkHttpClient client) {
        return getRetrofit(GIT_HUB, client);
    }

    public static Retrofit getRetrofit(String url, OkHttpClient client) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();
        Retrofit.Builder builder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson));

        if (null == client) {
            builder.client(getDefaultClient());
        } else {
            builder.client(client);
        }
        // TODO: 16/7/14
        if (null == url || url.length() == 0) {
            builder.baseUrl(GIT_HUB);
        } else {
            builder.baseUrl(url);
        }
        return builder.build();
    }

    public static <T> T createService(Class<T> serviceClass) {
        return getRetrofit().create(serviceClass);
    }
}
