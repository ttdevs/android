/*
 * Created by ttdevs at 16-4-14 下午3:52.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.android;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ScrollView;

import com.ttdevs.android.utils.SysUtils;
//import com.ttdevs.jniutils.CipherUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class JNIActivity extends BaseActivity {


    @BindView(R.id.view_main)
    ScrollView viewMain;

    @OnClick({R.id.bt_authenticate,
            R.id.bt_base64_encode,
            R.id.bt_device_info})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_authenticate:
                SysUtils.getAppSignature(this);
//                CipherUtils.authenticate(this);
                break;
            case R.id.bt_base64_encode:
                String str = "{\"bucket\":\"onetest\",\"keys\":[\"/one/2016-08-09/160402b4-0470-49e0-b408-b580675cf910.png\",\"/bingo/2015-12-09/160402b4-0470-49e0-b408-b580675cf910.png\",\"/status/2014-10-09/160402b4-0470-49e0-b408-b580675cf910.png\",\"/food/2014-10-09/160402b4-0470-49e0-b408-b580675cf910.png\"]}";
//                showSnackbar(viewMain, CipherUtils.base64Encode(str));
                break;
            case R.id.bt_device_info:
                SysUtils.printSystemInfo(this);
                break;

            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jni);
        ButterKnife.bind(this);
    }

    private void showSnackbar(View view, String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }
}
