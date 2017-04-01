package com.ttdevs.air.utils;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelUuid;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class ReceiveDataThread extends BaseWorkerThread {
    public static final UUID DEFAULT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    public static final UUID UUID_RECEIVE = UUID.fromString("00001801-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_CONTROL = UUID.fromString("00001802-0000-1000-8000-00805f9b34fb");
    private static final int SIZE = 32;

    private Handler mHandler;
    private BluetoothDevice mDevice;
    private BluetoothSocket mSocket;
    private InputStream mIn;
    private byte[] tempBuffer, readBuffer;
    private int mBytes, mCount;

    public ReceiveDataThread(BluetoothDevice device) {
        this(device, null);
    }

    public ReceiveDataThread(BluetoothDevice device, Handler handler) {
        mDevice = device;
        mHandler = handler;
    }

    @Override
    public boolean workerBefore() {
        try {
            if (null == mSocket) {
                UUID uuid = DEFAULT_UUID;
                ParcelUuid[] uuids = mDevice.getUuids();
                if (null != uuids && uuids.length > 0) {
                    uuid = uuids[0].getUuid();
                }
                mSocket = mDevice.createRfcommSocketToServiceRecord(uuid);
            }
            mSocket.connect(); // 阻塞的

            mIn = mSocket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        tempBuffer = new byte[SIZE];
        readBuffer = new byte[SIZE];
        return super.workerBefore();
    }

    @Override
    public void workerCycle() {
        try {
            mBytes = mIn.read(readBuffer);
            System.arraycopy(readBuffer, 0, tempBuffer, mCount, mBytes);
            mCount += mBytes;
            if (mCount >= SIZE) {
                parseData(tempBuffer);
                mCount = 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void workerAfter() {
        super.workerAfter();

        try {
            if (null != mSocket) {
                mSocket.close();
                mSocket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseData(byte[] data) {
        String format = "%s:  %d ug/m3 \n";
        StringBuilder result = new StringBuilder();

        int CF_PM1_0 = 4;
        int CF_PM2_5 = 6;
        int CF_PM10_ = 8;
        int EN_PM1_0 = 10;
        int EN_PM2_5 = 12;
        int EN_PM10_ = 14;

        result.append(String.format(format, "PM1.0", byte2int(data[EN_PM1_0], data[EN_PM1_0 + 1])));
        result.append(String.format(format, "PM2.5", byte2int(data[EN_PM2_5], data[EN_PM2_5 + 1])));
        result.append(String.format(format, "PM10 ", byte2int(data[EN_PM10_], data[EN_PM10_ + 1])));
        result.append("\n");
        result.append(String.format(format, "dev PM1.0", byte2int(data[CF_PM1_0], data[CF_PM1_0 + 1])));
        result.append(String.format(format, "dev PM2.5", byte2int(data[CF_PM2_5], data[CF_PM2_5 + 1])));
        result.append(String.format(format, "dev PM10 ", byte2int(data[CF_PM10_], data[CF_PM10_ + 1])));

        if (null != mHandler) {
            Message msg = new Message();
            msg.obj = result.toString();
            mHandler.sendMessage(msg);
        }

        System.err.println("<> " + result.toString());
    }


    public static int byte2int(byte high, byte low) {
        int targets = ((high << 8) & 0xff00) | (low & 0xff);
        return targets;
    }
}