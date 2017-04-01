package com.ttdevs.air.utils;

import android.bluetooth.BluetoothAdapter;
import android.os.Handler;

/**
 * Created by ttdevs
 * 2017-04-01 (android)
 * https://github.com/ttdevs
 */
public class BLEThread extends BaseWorkerThread {
    private Handler mHandler;
    private BluetoothAdapter mBluetoothAdapter;

    public BLEThread(Handler handler, BluetoothAdapter bluetoothAdapter) {
        mHandler = handler;
        mBluetoothAdapter = bluetoothAdapter;
    }

    @Override
    public boolean workerBefore() {
        return super.workerBefore();
    }

    @Override
    public void workerCycle() {

    }
}
