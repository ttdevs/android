/*
 * Created by ttdevs at 16-7-14 下午3:28.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.retrofit.client;

import com.google.gson.Gson;
import com.ttdevs.retrofit.module.Error;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ttdevs on 16/7/8.
 */
public class DefaultInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        HttpUrl url = original.url().newBuilder()
                .addQueryParameter("token", "eATQzos9vG9hFK4Uk218")
                .addQueryParameter("user_key", "da4811fa-4526-4168-a598-2a7ceb942982")
                .build();

        Request request = original.newBuilder()
                .header("User-Agent", "ttdevs") //https://developer.github.com/v3/#user-agent-required
                .header("Content-Type", "application/json; charset=utf-8")
                .header("Accept", "application/json")
                .header("Accept", "application/vnd.github.v3+json") //https://developer.github.com/v3/#current-version
                .header("token", "eATQzos9vG9hFK4Uk218")
                .header("Time-Zone", "Asia/Shanghai") //https://developer.github.com/v3/#timezones
                .header("user_key", "haha")
                .method(original.method(), original.body())
                .url(url)
                .build();


        long t1 = System.nanoTime();
        String reqeustHeader = String.format(
                ">>>>>Sending request %s on %s%n%s",
                request.url(),
                chain.connection(),
                request.headers()
        );
        System.out.println(reqeustHeader);

        Response response = chain.proceed(request);

        long t2 = System.nanoTime();
        String responseHeader = String.format(
                ">>>>>Received response for %s in %.1fms%n%s",
                response.request().url(),
                (t2 - t1) / 1e6d, response.headers()
        );
//        System.out.println(responseHeader);
        if (response.code() >= 400) {
            String body = response.body().string();
            Error message = new Gson().fromJson(body, Error.class);
            System.err.println(message.getMessage());
        }
        System.out.println("=====================================================");
        return response;
    }
}
