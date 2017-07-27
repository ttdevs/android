package com.ttdevs.message;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class HandlerThreadActivity extends AppCompatActivity {

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            String threadName = Thread.currentThread().getName();
            tvLog.setText(String.format("当前线程：%s Msg：%s", threadName, msg.obj));
        }
    };

    private TextView tvLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler_thread);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvLog = (TextView)findViewById(R.id.tvLog);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

        initData();
    }

    private Handler mWorker;

    private void initData() {
        HandlerThread worker = new HandlerThread("worker");
        worker.start();
        mWorker = new Handler(worker.getLooper());
    }

    private void sendMessage() {
        mWorker.post(new Runnable() {
            @Override
            public void run() {
                print("我工作在线程：" + Thread.currentThread().getName());
                try {
                    Thread.sleep(6 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sendMsgToUI();
            }

            private void sendMsgToUI(){
                Message msg = new Message();
                msg.obj = Thread.currentThread().getName() + "工作完成，这是结果";
                mHandler.sendMessage(msg);
            }
        });
    }

    private static void print(String msg) {
        System.out.println(">>>>>" + msg);
    }
}
