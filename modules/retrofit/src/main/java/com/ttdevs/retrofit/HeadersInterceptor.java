/*
 * Created by ttdevs at 16-7-8 下午3:04.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.retrofit;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ttdevs on 16/7/8.
 */
public class HeadersInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        Request request = original.newBuilder()
                .header("User-Agent", "ttdevs")
                .header("Content-Type", "application/json; charset=utf-8")
                .header("Accept", "application/json")
                .header("token", "XXXOOO")
                .header("user_key", "haha")
                .method(original.method(), original.body())
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
        System.out.println(responseHeader);

        System.out.println("=====================================================");
        return response;
    }
}
