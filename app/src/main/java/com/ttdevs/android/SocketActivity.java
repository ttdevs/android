package com.ttdevs.android;

import android.os.Bundle;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);
    }

    private Socket mSocket;
    private InputStream mIn;
    private OutputStream mOut;

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_socket:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mSocket = new Socket("192.168.2.2", 17919);
                            mIn = mSocket.getInputStream();
                            mOut = mSocket.getOutputStream();
                            mOut.write("hello".getBytes());
                            mOut.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                            System.out.println(">>>>> socket");
                        }
                    }
                }).start();
                break;
            case R.id.bt_send:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mOut.write("hello".getBytes());
                            mOut.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                            System.out.println(">>>>> out");
                        }
                    }
                }).start();
                break;
            case R.id.bt_in_read:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mIn.read(new byte[20]);
                        } catch (IOException e) {
                            e.printStackTrace();
                            System.out.println(">>>>> in read");
                        }
                    }
                }).start();
                break;
            case R.id.bt_in_close:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mIn.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                            System.out.println(">>>>> in");
                        }
                    }
                }).start();
                break;
            case R.id.bt_out_close:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mOut.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                            System.out.println(">>>>> out");
                        }
                    }
                }).start();
                break;

            default:
                break;
        }
    }
}
