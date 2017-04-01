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
//                BLEService.comeOnBaby(this, MAC_BT5);
                start();
                break;
            case R.id.btDisconnect:
                stopService(new Intent(this, BLEService.class));
                break;

            default:
                break;
        }
    }








    private void start(){
        mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();

        if (null != mBluetoothAdapter) {
            BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(MAC_BT5);
            if (null != device) {
                mBluetoothGatt = device.connectGatt(this, true, new BluetoothGattCallback() {
                    @Override
                    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                        super.onConnectionStateChange(gatt, status, newState);

                        switch (newState) {
                            case BluetoothProfile.STATE_CONNECTED:
                                print("连接GATT服务成功，开始Service搜索...");
                                mBluetoothGatt.discoverServices();
                                break;
                            case BluetoothProfile.STATE_DISCONNECTED:
                                print("断开GATT Server连接.");
                                break;

                            default:
                                break;
                        }

                    }

                    @Override
                    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                        super.onServicesDiscovered(gatt, status);

                        print("发现服务...");

                        if (BluetoothGatt.GATT_SUCCESS == status) {
                            BluetoothGattService gattService = mBluetoothGatt.getService(UUID.fromString(UUID_BT5_SERVICE));
                            BluetoothGattCharacteristic characteristic = gattService.getCharacteristic(UUID.fromString(UUID_BT5_DATA));

                            final int charaProp = characteristic.getProperties();
                            print("Properties:" + charaProp);
                            mBluetoothGatt.setCharacteristicNotification(characteristic, true);
                            mBluetoothGatt.readCharacteristic(characteristic);

                            print("开始监听...");
                        }
                    }

                    @Override
                    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                        super.onCharacteristicRead(gatt, characteristic, status);

                        if (BluetoothGatt.GATT_SUCCESS == status) {
                            byte[] data = characteristic.getValue();
                            parseData(data);
                        }
                    }

                    @Override
                    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                        super.onCharacteristicWrite(gatt, characteristic, status);
                    }

                    @Override
                    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                        super.onCharacteristicChanged(gatt, characteristic);
                    }

                    @Override
                    public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                        super.onDescriptorRead(gatt, descriptor, status);
                    }

                    @Override
                    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                        super.onDescriptorWrite(gatt, descriptor, status);
                    }

                    @Override
                    public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
                        super.onReliableWriteCompleted(gatt, status);
                    }

                    @Override
                    public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
                        super.onReadRemoteRssi(gatt, rssi, status);
                    }

                    @Override
                    public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
                        super.onMtuChanged(gatt, mtu, status);
                    }
                });
                print("new connect:" + mBluetoothGatt.connect());
            }
        }
    }

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;

    private BluetoothGatt mBluetoothGatt;
    private static final String UUID_BT5_SERVICE = "0000ffe0-0000-1000-8000-00805f9b34fb";
    private static final String UUID_BT5_DATA = "0000ffe1-0000-1000-8000-00805f9b34fb";
    public static final int KEY_LOG = 0x01;
    public static final int KEY_PM25 = 0x02;
    private String mMAC = MAC_BT5;

    private void closeConnect() {
        if (null != mBluetoothGatt) {
            mBluetoothGatt.disconnect();
            mBluetoothGatt.close();
            mBluetoothGatt = null;
        }
    }


    private void parseData(byte[] data) {
        String pm25 = parsePM25Data(data);
        print(KEY_PM25, pm25);
    }

    private String parsePM25Data(byte[] data) {
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

        return result.toString();
    }

    public static int byte2int(byte high, byte low) {
        int targets = ((high << 8) & 0xff00) | (low & 0xff);
        return targets;
    }

    private void print(String msg) {
        print(KEY_LOG, msg);
    }

    private void print(int type, String msg) {
        Message message = new Message();
        message.what = type;
        message.obj = msg;
        mHandler.sendMessage(message);

        System.out.println(">>>>>" + msg);
    }
}
