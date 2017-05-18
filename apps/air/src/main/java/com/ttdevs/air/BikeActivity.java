package com.ttdevs.air;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.ttdevs.air.service.BLEService;
import com.ttdevs.air.service.BikeService;

public class BikeActivity extends BaseActivity {

    private static final long SCAN_PERIOD = 10000;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (null == msg.obj) {
                return;
            }
            String message = msg.obj.toString();
            switch (msg.what) {
                case BikeService.KEY_LOG:
                    tvLog.setText(tvLog.getText() + "\n" + message);
                    break;

                default:
                    break;
            }
        }
    };

    private TextView tvLog;

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike);

        tvLog = (TextView) findViewById(R.id.tvLog);

        mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        stopService(new Intent(this, BikeService.class));
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btConnect:
                BikeService.comeOnBaby(this, BikeService.MAC_BIKE);
                break;
            case R.id.btDisconnect:
                stopService(new Intent(this, BikeService.class));
                break;
            case R.id.btScan:
                mScanning = !mScanning;
                scanLeDevice(mScanning);
                break;

            default:
                break;
        }
    }

    private boolean mScanning;

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            String format = "Name:%s, Mac:%s, Type:%s";
            String msg = String.format(format, device.getName(), device.getAddress(), device.getType());
            print(msg);
        }
    };

    private void print(String msg){
        System.out.println(">>>>>" + msg);
    }
}
