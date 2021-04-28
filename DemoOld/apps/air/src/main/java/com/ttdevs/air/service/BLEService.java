package com.ttdevs.air.service;

import android.app.Service;
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
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.ttdevs.air.utils.HexUtils;

import java.util.List;
import java.util.UUID;

public class BLEService extends Service {
    public static final String KEY_MAC = "key_mac";
    public static final int KEY_LOG = 0x01;
    public static final int KEY_PM25 = 0x02;

    private static final String MAC_BT5 = "00:15:83:30:B4:70";
    private static final String MAC_BIKE = "82:EA:CA:00:00:01";

    private static final UUID UUID_RECEIVE = UUID.fromString("00001801-0000-1000-8000-00805f9b34fb");
    private static final UUID UUID_CONTROL = UUID.fromString("00001802-0000-1000-8000-00805f9b34fb");
    private static final String UUID_BT5_DATA = "0000ffe1-0000-1000-8000-00805f9b34fb";

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;

    private BluetoothGatt mBluetoothGatt;

    private String mMAC = MAC_BT5;

    private Handler mHandler = new Handler();

    public BLEService() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (null != intent) {
            mMAC = intent.getStringExtra(KEY_MAC);
        }
        if (TextUtils.isEmpty(mMAC)) {
            mMAC = MAC_BT5;
        }
        mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();

        if (null != mBluetoothAdapter) {
            BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(mMAC);
            if (null != device) {
                mBluetoothGatt = device.connectGatt(this, false, mCallBack);
                mBluetoothGatt.connect();
                return START_STICKY;
            }
        }

        stopSelf();
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        closeConnect();
    }

    private void closeConnect() {
        if (null != mBluetoothGatt) {
            mBluetoothGatt.close();
            mBluetoothGatt.disconnect();
            mBluetoothGatt = null;
        }
    }

    private final BluetoothGattCallback mCallBack = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);

            switch (newState) {
                case BluetoothProfile.STATE_CONNECTED:
                    print("连接GATT服务成功，开始Service搜索...");
                    gatt.discoverServices();
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
                List<BluetoothGattService> gattServices = gatt.getServices();
                if (null == gattServices || gattServices.size() == 0) {
                    return;
                }
                for (BluetoothGattService gattService : gattServices) {
                    List<BluetoothGattCharacteristic> characteristics = gattService.getCharacteristics();
                    for (BluetoothGattCharacteristic characteristic : characteristics) {
                        String uuid = characteristic.getUuid().toString();
                        print("UUID    Cha:" + uuid);
                        if (UUID_BT5_DATA.equalsIgnoreCase(uuid)) {
                            mBluetoothGatt.setCharacteristicNotification(characteristic, true);
                            print("开始监听...");
                        }
                    }
                }
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);

            byte[] data = characteristic.getValue();
            parseData(data);
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);

            byte[] data = characteristic.getValue();
            parseData(data);
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
    };


    private void parseData(byte[] data) {
        String msg = HexUtils.bytesToHexString(data);
        print(msg);
//        String pm25 = parsePM25Data(data);
//        print(KEY_PM25, pm25);
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

    public static void comeOnBaby(Context context, String mac) {
        Intent service = new Intent(context, BLEService.class);
        service.putExtra(BLEService.KEY_MAC, MAC_BT5);
        context.startService(service);
    }
}
