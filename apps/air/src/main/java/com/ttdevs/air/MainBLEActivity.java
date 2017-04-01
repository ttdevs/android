package com.ttdevs.air;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.ttdevs.air.service.BLEService;

import java.util.List;
import java.util.UUID;

public class MainBLEActivity extends BaseActivity {
    private static final String MAC_BT5 = "00:15:83:30:B4:70";
    private static final String MAC_BIKE = "82:EA:CA:00:00:01";

    private TextView tvLog, tvPM25;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (null == msg.obj) {
                return;
            }
            String message = msg.obj.toString();
            switch (msg.what) {
                case BLEService.KEY_LOG:
                    tvLog.setText(tvLog.getText() + "\n" + message);
                    break;
                case BLEService.KEY_PM25:
                    tvPM25.setText(message);
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ble);

        tvPM25 = (TextView) findViewById(R.id.tvPM25);
        tvLog = (TextView) findViewById(R.id.tvLog);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btConnect:
                BLEService.comeOnBaby(this, MAC_BT5);
                break;
            case R.id.btDisconnect:
                stopService(new Intent(this, BLEService.class));
                break;

            default:
                break;
        }
    }
}
