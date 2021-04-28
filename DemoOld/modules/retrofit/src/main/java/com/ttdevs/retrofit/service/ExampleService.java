/*
 * Created by ttdevs at 16-7-14 下午3:27.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.retrofit.service;


import com.ttdevs.retrofit.module.User;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface ExampleService {

    // http://www.weather.com.cn/adat/sk/101010100.html
    @Headers("User-Agent: Your-App-Name")
    @GET
    public Call<ResponseBody> weatherReport(@Url String url);

    @Headers({
            "Accept: application/json",
            "User-Agent: ttdevs"
    })
    @GET
    public Call<ResponseBody> requestWithHeader(@Url String url);

    // http://119.29.29.29/d?dn=one.boohee.com
    @GET("/d")
    public Call<ResponseBody> requestWithParams(@Query("dn") String domain,
                                                @QueryMap(encoded = false) Map<String, String> options);

    // http://119.29.29.29/d?dn=ttdevs.vicp.com
    @GET("/d")
    public Call<ResponseBody> singleParams(@Query("dn") String domain);

    @GET("/record")
    public Call<ResponseBody> multiParams(@QueryMap(encoded = false) Map<String, String> options);

    @GET
    public Call<ResponseBody> requestWithHeaderMap(@Url String url, @HeaderMap Map<String, String> header);

    @POST("/send")
    public Call<ResponseBody> modelPost(@Url String url, @Body User user);

    @POST("/upload")
    public Call<ResponseBody> withRequestBody(@Url String url, @Body RequestBody body);
}