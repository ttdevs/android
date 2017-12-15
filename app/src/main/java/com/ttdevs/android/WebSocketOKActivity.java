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

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.ws.WebSocket;
import okhttp3.ws.WebSocketCall;
import okhttp3.ws.WebSocketListener;
import okio.Buffer;

public class WebSocketOKActivity extends BaseActivity {
    private static final int STATUS_CLOSE = 0;
    private static final int STATUS_CONNECT = 1;
    private static final int STATUS_MESSAGE = 2;

    @BindView(R.id.etIP)
    EditText etIP;
    @BindView(R.id.etPort)
    EditText etPort;
    @BindView(R.id.tvStatus)
    TextView tvStatus;
    @BindView(R.id.tvMsg)
    TextView tvMsg;
    @BindView(R.id.rgVersion)
    RadioGroup rgVersion;
    @BindView(R.id.etMessage)
    EditText etMessage;
    @BindView(R.id.svContent)
    ScrollView svContent;
    @BindView(R.id.viewMain)
    View viewMain;

    @BindView(R.id.btConnect)
    Button btConnect;
    @BindView(R.id.btDisconnect)
    Button btDisconnect;
    @BindView(R.id.btSend)
    Button btSend;

    private OKClient mClient;
    private WebSocket mWebSocket;
    private String mAddress;

    @OnClick({R.id.btConnect, R.id.btDisconnect, R.id.btSend, R.id.btPing})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btConnect:
                connectToServer();
                break;
            case R.id.btDisconnect:
                try {
                    if (null != mWebSocket) {
                        mWebSocket.close(-1, "From client, Bye!");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btSend:
                if (null != mClient) {
                    String msg = etMessage.getText().toString();
                    if (!TextUtils.isEmpty(msg)) {
                        try {
                            RequestBody body = RequestBody.create(MediaType.parse(""), msg);
                            mWebSocket.sendMessage(body);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return;
                        }
                        etMessage.setText("");
                    }
                }
                break;
            case R.id.btPing:
                try {
                    Buffer buffer = new Buffer().writeUtf8("hello");
                    mWebSocket.sendPing(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            default:
                break;
        }
    }

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
        setContentView(R.layout.activity_web_socket_ok);
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

        mClient = new OKClient();

        mAddress = String.format("ws://%s:%s", ip, port);

        OkHttpClient.Builder builder = new OkHttpClient.Builder().readTimeout(0, TimeUnit.NANOSECONDS);
        OkHttpClient okHttpClient = builder.build();
        Request request = new Request.Builder()
                .url(mAddress)
                .build();
        WebSocketCall socketCall = WebSocketCall.create(okHttpClient, request);
        socketCall.enqueue(mClient);

        tvStatus.setText(mAddress);
    }


    private class OKClient implements WebSocketListener {

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            mWebSocket = webSocket;

            Message msg = new Message();
            msg.what = STATUS_CONNECT;
            msg.obj = String.format("[Welcome：%s]", mAddress);
            mHandle.sendMessage(msg);
        }

        @Override
        public void onFailure(IOException e, Response response) {
            e.printStackTrace();
        }

        @Override
        public void onMessage(ResponseBody message) throws IOException {
            Message msg = new Message();
            msg.what = STATUS_MESSAGE;
            msg.obj = message.string();
            mHandle.sendMessage(msg);
        }

        @Override
        public void onPong(Buffer payload) {
            String value = parseBuffer(payload);

            Message msg = new Message();
            msg.what = STATUS_MESSAGE;
            msg.obj = "pong:" + value;
            mHandle.sendMessage(msg);
        }

        @Override
        public void onClose(int code, String reason) {
            Message msg = new Message();
            msg.what = STATUS_CLOSE;
            msg.obj = String.format("[Bye：%s \n %s]", mAddress, reason);
            mHandle.sendMessage(msg);
        }

        public String parseBuffer(Buffer buffer) {
            String result = "null";
            if (null == buffer) {
                return result;
            }
            byte[] data = buffer.readByteArray();
            if (null != data && data.length > 0) {
                return new String(data);
            }
            return result;
        }
    }
}
