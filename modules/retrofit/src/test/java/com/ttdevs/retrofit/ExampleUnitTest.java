/*
 * Created by ttdevs at 16-7-6 下午5:25.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.retrofit;

import com.ttdevs.retrofit.module.User;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
//    @Test
//    public void addition_isCorrect() throws Exception {
//        assertEquals(4, 2 + 2);
//    }
//
//    @Test
//    public void github_request() throws Exception {
//        final CountDownLatch countDownLatch = new CountDownLatch(1);
//
//        GitHubService gitHubService = RetrofitClient.createService(
//                RetrofitClient.GIT_HUB,
//                GitHubService.class);
//        Call<User> ttdevs = gitHubService.userInfo("ttdevs");
//        ttdevs.enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//                StringBuilder builder = new StringBuilder();
//                builder.append(String.format("Code: %d Message: %s \n", response.code(), response.message()));
//                builder.append("Headers: \n");
//
//                Headers headers = response.headers();
//                for (int i = 0; i < headers.size(); i++) {
//                    String name = headers.name(i);
//                    String value = headers.get(name);
//                    builder.append(String.format("%s: %s \n", name, value));
//                }
//
//                System.out.println(builder.toString());
//
//                okhttp3.Response raw = response.raw();
//                try {
//                    System.out.println(String.format("\n\nMessage: %s \n", raw.body().string()));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                countDownLatch.countDown();
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//                t.printStackTrace();
//                countDownLatch.countDown();
//            }
//        });
//
//        countDownLatch.await();
//    }


    @Test
    public void test_retrofit() throws Exception {
        String url = "http://api.github.com/";
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HeadersInterceptor())
                .build();
        Retrofit.Builder builder = new Retrofit.Builder()
                .client(client)
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        GitHubService github = retrofit.create(GitHubService.class);
        Call<User> ttdevs = github.userInfo("ttdevs");

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        ttdevs.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User ttdevs = response.body();

                String message = String.format(
                        "I'm %s.\n%s",
                        ttdevs.getName(),
                        ttdevs.getHtml_url());
                System.out.println(message);

                countDownLatch.countDown();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                countDownLatch.countDown();
            }
        });
        countDownLatch.await();
    }
}