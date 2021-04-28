/*
 * Created by ttdevs at 16-7-14 下午3:27.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.retrofit.service;

import com.ttdevs.retrofit.module.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GitHubService {


    @GET("/users/{user}")
    Call<User> userInfo(@Path("user") String user);

}