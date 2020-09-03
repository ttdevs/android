/*
 * Created by ttdevs at 16-7-14 下午2:52.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ttdevs.retrofit.module.User;
import com.ttdevs.retrofit.service.ExampleService;
import com.ttdevs.retrofit.service.GitHubService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static junit.framework.Assert.assertEquals;

public class RetrofitUnitTest {

    @Test
    public void simpleDemo() throws Exception {
        String baseURL = "https://api.github.com/";
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
        OkHttpClient client = okHttpBuilder.build();
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(baseURL);
        Retrofit retrofit = retrofitBuilder.build();
        GitHubService service = retrofit.create(GitHubService.class);
        Call<User> github = service.userInfo("ttdevs");

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        github.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                print(response.body().getName());

                countDownLatch.countDown();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                countDownLatch.countDown();
            }
        });
        countDownLatch.await();
    }

    @Test
    public void useFullUrl() throws Exception {
        ExampleService service = RetrofitManager.createService(ExampleService.class);

        String url = "http://www.weather.com.cn/adat/sk/101020100.html";
        Call<ResponseBody> example = service.weatherReport(url);

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        example.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    print(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                countDownLatch.countDown();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                countDownLatch.countDown();
            }
        });
        countDownLatch.await();
    }

    @Test
    public void requestHeader() throws Exception {
        OkHttpClient client = RetrofitManager.getClient(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .header("Time-Zone", "Asia/Shanghai") //https://developer.github.com/v3/#timezones
                        .header("user_key", "I_am_user_key")
                        .method(original.method(), original.body())
                        .build();
                okhttp3.Response response = chain.proceed(request);
                return response;
            }
        });
        Retrofit retrofit = RetrofitManager.getRetrofit(client);
        ExampleService service = retrofit.create(ExampleService.class);

        String url = "http://www.weather.com.cn/adat/sk/101020100.html";
        Call<ResponseBody> example = service.requestWithHeader(url);

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        example.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    print(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                countDownLatch.countDown();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                countDownLatch.countDown();
            }
        });
        countDownLatch.await();
    }

    @Test
    public void requestParams() throws Exception {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();

                        HttpUrl originalHttpUrl = original.url();

                        HttpUrl url = originalHttpUrl
                                .newBuilder()
                                .addQueryParameter("token", "I_am_user_token")
                                .addQueryParameter("user_key", "I_am_user_user_key")
                                .build();

                        Request request = original
                                .newBuilder()
                                .url(url)
                                .build();

                        okhttp3.Response response = chain.proceed(request);
                        return response;
                    }
                })
                .build();
        String baseURL = "http://119.29.29.29/";
        Retrofit retrofit = RetrofitManager.getRetrofit(baseURL, client);
        ExampleService service = retrofit.create(ExampleService.class);

        String domain = "one.boohee.com";
        Map<String, String> options = new HashMap<>();
        options.put("name", "ttdevs");
        options.put("country", "中国");

        Call<ResponseBody> example = service.requestWithParams(domain, options);

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        example.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    print(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                countDownLatch.countDown();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                countDownLatch.countDown();
            }
        });
        countDownLatch.await();
    }

    @Test
    public void synchronousRequest() throws Exception {
        Retrofit retrofit = RetrofitManager.getRetrofit();
        GitHubService service = retrofit.create(GitHubService.class);
        Call<User> example = service.userInfo("ttdevs");
        Response<User> response = example.execute();
        print(response.body().getName());
    }

    @Test
    public void requestLogging() throws Exception {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
//        logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = RetrofitManager.getClient(logging);

        Retrofit retrofit = RetrofitManager.getRetrofit(client);
        ExampleService service = retrofit.create(ExampleService.class);

        String url = "http://www.weather.com.cn/adat/sk/101020100.html";
        Call<ResponseBody> example = service.requestWithHeader(url);

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        example.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    print(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                countDownLatch.countDown();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                countDownLatch.countDown();
            }
        });
        countDownLatch.await();
    }

    /**
     * 使用：<br/>
     * <pre>
     *     @HeaderMap Map<String, String> header
     * </pre>
     *
     * @throws Exception
     */
    @Test
    public void requestWithHeaderMap() throws Exception {
        Retrofit retrofit = RetrofitManager.getRetrofit();
        ExampleService service = retrofit.create(ExampleService.class);

        Map<String, String> header = new HashMap<>();
        header.put("model", "MX4 Pro");
        header.put("version", "Android 5.1.1");

        String url = "http://www.weather.com.cn/adat/sk/101020100.html";
        Call<ResponseBody> example = service.requestWithHeaderMap(url, header);
        Response<ResponseBody> response = example.execute();
        print(response.body().string());
    }

    public static void print(String message) {
        System.out.println(message);
    }


    @Test
    public void javaTest() throws Exception {
        final String[] keys = {"carbohydrate,碳水化合物"};

        for (String keyValue : keys) {
            System.out.println(keyValue);
            String key = keyValue.split(",")[0];
            String value = keyValue.split(",")[1];
            System.out.println(key);
            System.out.println(value);
        }
    }


    @Test
    public void modelPost() throws Exception {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient client = RetrofitManager.getClient(logging);

        Retrofit retrofit = RetrofitManager.getRetrofit(client);
        ExampleService service = retrofit.create(ExampleService.class);

        String url = "http://www.weather.com.cn/adat/sk/101020100.html";
        User user = new User();
        user.setName("ttdevs");
        Call<ResponseBody> example = service.modelPost(url, user);

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        example.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    print(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                countDownLatch.countDown();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                countDownLatch.countDown();
            }
        });
        countDownLatch.await();
    }

    @Test
    public void withRequestBody() throws Exception {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient client = RetrofitManager.getClient(logging);

        Retrofit retrofit = RetrofitManager.getRetrofit(client);
        ExampleService service = retrofit.create(ExampleService.class);

        String url = "http://www.weather.com.cn/adat/sk/101020100.html";

        JSONObject result = new JSONObject();
        try {
            result.put("record", "hello");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), result.toString());
        Call<ResponseBody> example = service.withRequestBody(url, body);

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        example.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    print(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                countDownLatch.countDown();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                countDownLatch.countDown();
            }
        });
        countDownLatch.await();
    }
}