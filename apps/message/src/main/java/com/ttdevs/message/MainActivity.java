package com.ttdevs.message;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            printMsg("handleMessage:" + msg.obj);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btSendMsg:
                defaultSendMsg();
                break;
            case R.id.btHandlerThread:
                startActivity(new Intent(this, HandlerThreadActivity.class));
                break;
            case R.id.btViewPost:
                startActivity(new Intent(this, ViewPostActivity.class));
                break;
            case R.id.btQuit:
                getMainLooper().quit();
                break;

            default:
                break;
        }
    }

    private void defaultSendMsg() {
        Message msg = new Message();
        msg.obj = "这是最简单的使用方法";
        mHandler.sendMessage(msg);
        printMsg("Is MainLooper: " + (mHandler.getLooper() == getMainLooper()));
    }

    private static void printMsg(String msg) {
        System.out.println(">>>>>" + msg);
    }
}
