/*
 * Created by ttdevs at 16-8-24 上午10:55.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.android;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_10;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.drafts.Draft_75;
import org.java_websocket.drafts.Draft_76;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.channels.NotYetConnectedException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WebSocketActivity extends BaseActivity {
    private static final int STATUS_CLOSE = 0;
    private static final int STATUS_CONNECT = 1;
    private static final int STATUS_MESSAGE = 2;

    @Bind(R.id.etIP)
    EditText etIP;
    @Bind(R.id.etPort)
    EditText etPort;
    @Bind(R.id.tvStatus)
    TextView tvStatus;
    @Bind(R.id.tvMsg)
    TextView tvMsg;
    @Bind(R.id.rgVersion)
    RadioGroup rgVersion;
    @Bind(R.id.etMessage)
    EditText etMessage;
    @Bind(R.id.svContent)
    ScrollView svContent;
    @Bind(R.id.viewMain)
    View viewMain;

    @Bind(R.id.btConnect)
    Button btConnect;
    @Bind(R.id.btDisconnect)
    Button btDisconnect;
    @Bind(R.id.btSend)
    Button btSend;

    @OnClick({R.id.btConnect, R.id.btDisconnect, R.id.btSend})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btConnect:
                connectToServer();
                break;
            case R.id.btDisconnect:
                if (null != mClient) {
                    mClient.close();
                }
                break;
            case R.id.btSend:
                if (null != mClient) {
                    String msg = etMessage.getText().toString();
                    if (!TextUtils.isEmpty(msg)) {
                        try {
                            mClient.send(msg);
                        } catch (NotYetConnectedException e) {
                            e.printStackTrace();
                            return;
                        }
                        etMessage.setText("");
                    }
                }
                break;

            default:
                break;
        }
    }

    private Client mClient;
    private Handler mHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String message = String.format("[%d] %s\n", System.currentTimeMillis(), msg.obj.toString());
            tvMsg.append(message);

            switch (msg.what) {
                case STATUS_CONNECT:
                    btConnect.setEnabled(false);
                    btDisconnect.setEnabled(true);
                    btSend.setEnabled(true);
                    break;
                case STATUS_CLOSE:
                    btConnect.setEnabled(true);
                    btDisconnect.setEnabled(false);
                    btSend.setEnabled(false);
                    break;
                case STATUS_MESSAGE:
                    // TODO: 16/8/24
                    break;
                default:
                    break;
            }
            svContent.postDelayed(new Runnable() {
                @Override
                public void run() {
                    svContent.fullScroll(View.FOCUS_DOWN);
                }
            }, 100);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_socket);
        ButterKnife.bind(this);


        System.setProperty("java.net.preferIPv6Addresses", "false");
        System.setProperty("java.net.preferIPv4Stack", "true");
    }

    private void connectToServer() {
        String ip = etIP.getText().toString();
        String port = etPort.getText().toString();
        if (TextUtils.isEmpty(ip) || TextUtils.isEmpty(port)) {
            Snackbar.make(viewMain, "IP and Port 不能为空", Snackbar.LENGTH_LONG).show();
            return;
        }
        String address = String.format("ws://%s:%s", ip, port);
        Draft draft = null;
        switch (rgVersion.getCheckedRadioButtonId()) {
            case R.id.rbDraft10:
                draft = new Draft_10();
                break;
            case R.id.rbDraft17:
                draft = new Draft_17();
                break;
            case R.id.rbDraft75:
                draft = new Draft_75();
                break;
            case R.id.rbDraft76:
                draft = new Draft_76();
                break;

            default:
                draft = new Draft_17();
                break;
        }
        try {
            URI uri = new URI(address);
            mClient = new Client(uri, draft);
            mClient.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        tvStatus.setText(address);
    }

    private class Client extends WebSocketClient {

        public Client(URI serverURI) {
            super(serverURI);
        }

        public Client(URI serverUri, Draft draft) {
            super(serverUri, draft);
        }

        @Override
        public void onOpen(ServerHandshake handShakeData) {
            Message msg = new Message();
            msg.what = STATUS_CONNECT;
            msg.obj = String.format("[Welcome：%s]", getURI());
            mHandle.sendMessage(msg);
        }

        @Override
        public void onMessage(String message) {
            Message msg = new Message();
            msg.what = STATUS_MESSAGE;
            msg.obj = message;
            mHandle.sendMessage(msg);
        }

        @Override
        public void onClose(int code, String reason, boolean remote) {
            Message msg = new Message();
            msg.what = STATUS_CLOSE;
            msg.obj = String.format("[Bye：%s]", getURI());
            mHandle.sendMessage(msg);
        }

        @Override
        public void onError(Exception ex) {
            ex.printStackTrace();
        }
    }
}
