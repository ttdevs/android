/*
 * Created by ttdevs at 16-4-14 下午3:52.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.reactive;

import android.text.TextUtils;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCancellationSignal;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.ttdevs.reactive.utils.BitmapUtils;
import com.ttdevs.reactive.utils.LogUtils;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class QiniuUploader {

    // 重用 uploadManager。一般地，只需要创建一个 uploadManager 对象
    private static final UploadManager mUploadManager = new UploadManager(init());

    private static Configuration init() {
        Configuration config = new Configuration.Builder()
                // .chunkSize(256 * 1024)  //分片上传时，每片的大小。 默认 256K
                // .putThreshhold(512 * 1024)  // 启用分片上传阀值。默认 512K
                // .connectTimeout(10) // 链接超时。默认 10秒
                // .responseTimeout(60) // 服务器响应超时。默认 60秒
                // .recorder(recorder)  // recorder 分片上传时，已上传片记录器。默认 null
                // .recorder(recorder, keyGen)  // keyGen 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
                // .zone(Zone.zone0) // 设置区域，指定不同区域的上传域名、备用域名、备用IP。默认 Zone.zone0
                .build();
        return config;
    }

    public static void upload(final UploadHandler handler, String... pathList) {
        upload(QiniuConfig.Prefix.a, handler, pathList);
    }

    public static void upload(final QiniuConfig.Prefix prefix, final UploadHandler handler, String... pathList) {
        Observable.from(pathList)
                .subscribeOn(Schedulers.newThread())
                .flatMap(new Func1<String, Observable<QiniuModel>>() {
                    @Override
                    public Observable<QiniuModel> call(String path) {
                        QiniuModel info = new QiniuModel();
                        info.path = path;
                        info.key = QiniuConfig.createFileName(prefix);
                        // TODO
                        info.key = "a/2016/03/15/8ec9e8e6-4dba-4627-aecf-57cfdf316637";

                        return Observable.just(info);
                    }
                })
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<QiniuModel, Observable<QiniuModel>>() {
                    @Override
                    public Observable<QiniuModel> call(QiniuModel info) {
                        LogUtils.debug("flatMap request:" + Thread.currentThread().getName());

                        String jsonBody = QiniuConfig.getRequestBody(info.key);
                        RequestBody body = RequestBody.create(QiniuConfig.JSON, jsonBody);
                        Request request = new Request.Builder()
                                .url(QiniuConfig.getURL())
                                .addHeader("app_device", "Android")
                                .addHeader("User-Agent", "Android/OkHttp")
                                .post(body)
                                .build();

                        OkHttpClient client = new OkHttpClient();

                        try {
                            Response response = client.newCall(request).execute();
                            String responseString = response.body().string();

                            JSONObject object = new JSONObject(responseString);
                            info.token = object.getString("upload_token");
                        } catch (Exception e) {
                            e.printStackTrace();

                            throw new RuntimeException(e.getMessage());
                        }

                        return Observable.just(info);
                    }
                })
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<QiniuModel, Observable<QiniuModel>>() {
                    @Override
                    public Observable<QiniuModel> call(final QiniuModel info) {
                        if (TextUtils.isEmpty(info.token)) {
                            return null;
                        }

                        UploadOptions options = new UploadOptions(null, "image/jpeg", false, new UpProgressHandler() {
                            @Override
                            public void progress(String key, double percent) {
                                handler.onProgress(info.path, percent);
                            }
                        }, new UpCancellationSignal() {
                            @Override
                            public boolean isCancelled() {
                                return handler.isCancled();
                            }
                        });

                        final CountDownLatch countDownLatch = new CountDownLatch(1);

                        UpCompletionHandler upCompletionHandler = new UpCompletionHandler() {
                            @Override
                            public void complete(String key, ResponseInfo response, JSONObject result) {
                                if (response.isOK()) {
                                    info.hash = result.optString("hash");
                                } else {
                                    String msg = String.format("%s: %s", info.path, response.toString());
                                    throw new RuntimeException(msg);
                                }

                                countDownLatch.countDown();
                            }
                        };
                        if (new File(info.path).length() > QiniuConfig.MAX_SIZE) {
                            byte[] data = BitmapUtils.compressBitmap(info.path,
                                    QiniuConfig.MAX_SIZE,
                                    QiniuConfig.MAX_WIDTH,
                                    QiniuConfig.MAX_HEIGHT);
                            if (null == data || data.length == 0) {
                                return null;
                            }

                            mUploadManager.put(data, info.key, info.token, upCompletionHandler, options);
                        } else {
                            mUploadManager.put(info.path, info.key, info.token, upCompletionHandler, options);
                        }

                        try {
                            countDownLatch.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            throw new RuntimeException(e.getMessage());
                        }

                        return Observable.just(info);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<QiniuModel>() {
                    final List<QiniuModel> result = new ArrayList<>();

                    @Override
                    public void onCompleted() {
                        for (QiniuModel info : result) {
                            if (TextUtils.isEmpty(info.hash)) {
                                handler.onError("upload fail");
                                return;
                            }
                        }

                        handler.onSuccess(result);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();

                        handler.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(QiniuModel info) {
                        result.add(info);
                    }
                });
    }
}
