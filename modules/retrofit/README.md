# Retrofit初探

## 0x00 Retrofit

很久以前就计划要研究一下，但是一直拖到现在，不知道是因为优先级放的比较低还是因为拖延症。总只，现在要完整的看一遍。

由于这个已经出来很久了，所以我就直接选择了Refrofit2，没有去关注Refrofit1，等看完Refrofit2有时间的话再去看看Refrofit1，看看它们的变迁。好了，先从Refrofit2开始吧。

## 0x01 简介

### 简介 

如果你接触Android时间比较长，从最初的自己封装线程，到使用异步任务，到开源的各种第三方网络请求框架如xUtils，到谷歌的Volley，androider总在探索一种更好的网络请求形式。Retrofit就是这样的一款产品。

PS：从上面一句话，我们也可以看到，android生态在不断的进步和完善～～

### 引入

两个问题，gradle和混淆：

- Gradle：

	- Retrofit： ` implementation 'com.squareup.retrofit2:retrofit:2.1.0' `
	- JSON解析器： `implementation 'com.squareup.retrofit2:converter-gson:2.1.0' `
	
- Proguard：

	``` shell
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
	``` 

## 0x02 从一个demo开始

``` java 
public class ExampleUnitTest {
    @Test
    public void test_retrofit() throws Exception{
        String url = "http://api.github.com/";
        OkHttpClient client = new OkHttpClient.Builder()
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
```

## 0x03 第一个问题：Header

执行上面的小demo，我们通过抓包发现它的header信息如下：

``` shell
Host: api.github.com:443
Proxy-Connection: Keep-Alive
User-Agent: okhttp/3.3.0
```

这个header信息是不是很简单，如果你用这样的请求去写爬虫的话，多数情况下是得不到结果的。而实际情况也是：我们需要在header中使用标准http头信息，也可能需要加入自定义的头信息，比如token等。Retrofit2没有直接给他们提供这样的方法，但是我们知道，Retrofit2用的是Okhttp3，因此，我们可以从Okhttp3中下手，代码如下：

- 创建 `HeadersInterceptor` 类，代码如下：
	
	``` java
public class HeadersInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        Request request = original.newBuilder()
                .header("User-Agent", "ttdevs")
                .header("Content-Type", "application/json; charset=utf-8")
                .header("Accept", "application/json")
                .header("token", "abcdefg_ttdevs_hijklmn")
                .header("user_key", "ttdevs")
                .method(original.method(), original.body())
                .build();


        long t1 = System.nanoTime();
        String requestHeader = String.format(">>>>>Sending request %s on %s%n%s",
                request.url(), chain.connection(), request.headers())
        System.out.println(requestHeader);

        Response response = chain.proceed(request);

        long t2 = System.nanoTime();
        System.out.println(String.format(">>>>>Received response for %s in %.1fms%n%s",
                response.request().url(), (t2 - t1) / 1e6d, response.headers()));

        System.out.println("=====================================================");
        return response;
    }
}
	```

- 在 `Okhttp` 中加入 `HeadersInterceptor`

	``` java
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
	```

这样在运行上面的代码，我们就可以在 Http 请求的 Header 中看到我们加入的信息了。

源码参考：[ttdevs](https://github.com/ttdevs/android/tree/master/modules/retrofit)

参考：

1. https://github.com/square/retrofit
2. http://square.github.io/retrofit/
3. https://futurestud.io/blog/retrofit-add-custom-request-header
