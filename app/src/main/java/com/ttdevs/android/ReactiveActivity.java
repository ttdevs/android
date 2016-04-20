/*
 * Created by ttdevs at 16-4-14 下午3:52.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.android;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.ttdevs.android.utils.LogUtils;
import com.ttdevs.reactive.QiniuConfig;
import com.ttdevs.reactive.QiniuModel;
import com.ttdevs.reactive.QiniuUploader;
import com.ttdevs.reactive.UploadHandler;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReactiveActivity extends BaseActivity {


    @OnClick({R.id.bt_upload, R.id.bt_jni})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_upload:
                uploadImage();
                break;
            case R.id.bt_jni:
                jniTest();
                break;

            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reactive);
        ButterKnife.bind(this);

    }


    private void jniTest() {
        String param = "abc";
//        LogUtils.debug(BooheeUtils.signature(param));

        LogUtils.debug(QiniuConfig.signature(param));
    }

    private void uploadImage() {
        String basePath = Environment.getExternalStorageDirectory() + "/ttdevs/";
        String image1 = basePath + "1.png";
        String image2 = basePath + "2.jpg";
        String image4 = basePath + "4.jpg";
        String image10 = basePath + "10.gif";

        LogUtils.debug(Thread.currentThread().getName());
        QiniuUploader.upload(new UploadHandler() {
            @Override
            public void onProgress(String filePath, double percent) {
                // LogUtils.debug(String.format("%s:%f", filePath, percent));
            }

            @Override
            public void onSuccess(List<QiniuModel> infos) {
                LogUtils.debug(Thread.currentThread().getName());

                for (QiniuModel info : infos) {
                    LogUtils.showToastShort(info.hash);
                    LogUtils.debug(String.format("hash:%s key:%s", info.hash, info.key));
                }
            }

            @Override
            public void onError(String msg) {
                LogUtils.debug(msg);
            }
        }, image1, image2, image4, image10);
    }
}
