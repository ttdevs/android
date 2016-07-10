/*
 * Created by ttdevs at 16-7-8 上午10:42.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.retrofit;

import com.ttdevs.retrofit.module.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GitHubService {
//    @Headers({
//            "Accept: application/vnd.github.v3.full+json",
//            "User-Agent: Retrofit-Sample-App"
//    })
//    @GET("users/{user}/repos")
//    Call<List<Repo>> listRepos(@Path("user") String user);

    @GET("/users/{user}")
    Call<User> userInfo(@Path("user") String user);
}