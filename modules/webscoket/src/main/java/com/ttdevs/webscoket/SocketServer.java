/*
 * Created by ttdevs at 16-8-26 下午1:48.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.webscoket;

import org.java_websocket.WebSocket;
import org.java_websocket.framing.Framedata;
import org.java_websocket.framing.FramedataImpl1;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Collection;

public class SocketServer extends WebSocketServer {
    private static final int PORT = 2333;

    public static void main(String[] args) {
        SocketServer server = new SocketServer(PORT);
        server.start();

        try {
            String ip = InetAddress.getLocalHost().getHostAddress();
            int port = server.getPort();
            print(String.format("服务已启动: %s:%d", ip, port));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        InputStreamReader in = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(in);

        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();

        while (true) {
            try {
                String msg = reader.readLine();
                server.broadcastMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public SocketServer(int port) {
        super(new InetSocketAddress(port));
    }

    public SocketServer(InetSocketAddress address) {
        super(address);
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        String address = webSocket.getRemoteSocketAddress().getAddress().getHostAddress();
        String message = String.format("(%s) <加入>", address);
        broadcastMessage(message);
        print(message);
    }

    @Override
    public void onClose(WebSocket webSocket, int code, String reason, boolean remote) {
        String address = webSocket.getRemoteSocketAddress().getAddress().getHostAddress();
        String message = String.format("(%s) <离开>", address);
        broadcastMessage(message);
        print(message);
    }

    @Override
    public void onMessage(final WebSocket webSocket, String msg) {
        String address = webSocket.getRemoteSocketAddress().getAddress().getHostAddress();
        String message = String.format("(%s) %s", address, msg);
        broadcastMessage(message);
        print(message);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000 * 3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                FramedataImpl1 resp = new FramedataImpl1(Framedata.Opcode.PING);
                resp.setFin(true);
                webSocket.sendFrame(resp);
            }
        }).start();
    }

    @Override
    public void onWebsocketPong(WebSocket conn, Framedata f) {
        super.onWebsocketPong(conn, f);

        print("pong");
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        if (null != webSocket) {
            if (!webSocket.isClosed()) {
                webSocket.close(0);
            }
        }
        e.printStackTrace();
    }

    /**
     * 广播收到消息
     *
     * @param msg
     */
    private void broadcastMessage(String msg) {
        Collection<WebSocket> connections = connections();
        synchronized (connections) {
            for (WebSocket client : connections) {
                client.send(msg);
            }
        }
    }

    private static void print(String msg) {
        System.out.println(String.format("[%d] %s", System.currentTimeMillis(), msg));
    }
}
