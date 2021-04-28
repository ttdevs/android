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
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.ttdevs.air.utils.BLEUtils;
import com.ttdevs.air.utils.HexUtils;

import java.util.List;
import java.util.UUID;

public class BikeService extends Service {
    public static final String KEY_MAC = "key_mac";
    public static final int KEY_LOG = 0x01;

    //    public static final String MAC_BIKE = "82:EA:CA:00:00:01"; // bike
    public static final String MAC_BIKE = "00:15:83:30:B4:70"; // bt5

    //    private static final UUID UUID_RECEIVE = UUID.fromString("00001801-0000-1000-8000-00805f9b34fb");
//    private static final UUID UUID_CONTROL = UUID.fromString("00001802-0000-1000-8000-00805f9b34fb");
    private static final UUID UUID_RECEIVE = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;

    private BluetoothGatt mBluetoothGatt;

    private String mMAC = MAC_BIKE;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    public BikeService() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (null != intent) {
            mMAC = intent.getStringExtra(KEY_MAC);
        }
        if (TextUtils.isEmpty(mMAC)) {
            mMAC = MAC_BIKE;
        }
        mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (null == mBluetoothAdapter) {
            stopSelf();
            return START_NOT_STICKY;
        }

        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(mMAC);
        if (null == device) {
            stopSelf();
            return START_NOT_STICKY;
        }

        closeConnect();

        mBluetoothGatt = device.connectGatt(this, false, mCallBack);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //5.0设置的传输最大空间
            mBluetoothGatt.requestConnectionPriority(BluetoothGatt.CONNECTION_PRIORITY_HIGH);
            mBluetoothGatt.requestMtu(84);
        }
        print("Gatt connect");
        return START_STICKY;
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

            print(String.format("status:%d, newState:%d", status, newState));

            if (status != BluetoothGatt.GATT_SUCCESS) {
                closeConnect();
            }

            switch (newState) {
                case BluetoothProfile.STATE_CONNECTED:
                    print("连接GATT服务成功，开始发现服务...");
                    gatt.discoverServices();
                    break;
                case BluetoothProfile.STATE_DISCONNECTED:
                    print("断开GATT服务，Bye");
                    closeConnect();
                    break;

                default:
                    break;
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);

            print("发现服务：" + status);

            if (BluetoothGatt.GATT_SUCCESS == status) {
                List<BluetoothGattService> gattServices = gatt.getServices();
                if (null == gattServices || gattServices.size() == 0) {
                    return;
                }
                for (BluetoothGattService gattService : gattServices) {
                    String serviceUUID = gattService.getUuid().toString();
                    print("UUID GATT:" + serviceUUID);
                    List<BluetoothGattCharacteristic> characteristics = gattService.getCharacteristics();
                    for (BluetoothGattCharacteristic characteristic : characteristics) {
                        String uuid = characteristic.getUuid().toString();
                        print("UUID     Charac:" + uuid);
                        print("UUID     Status:" + getProperties(characteristic));
                        print("UUID     Status:" + characteristic.getInstanceId());
                        List<BluetoothGattDescriptor> descriptors = characteristic.getDescriptors();
                        for (BluetoothGattDescriptor descriptor : descriptors) {
                            print("UUID          Desc:" + descriptor.getUuid().toString());
                            print("UUID          Perm:" + String.valueOf(descriptor.getPermissions()));
                        }
                        if (UUID_RECEIVE.toString().equalsIgnoreCase(uuid)) {
                            mBluetoothGatt.setCharacteristicNotification(characteristic, true);
                            print("开始监听：" + uuid);
                        }
                    }
                }
            }
        }

        private String getProperties(BluetoothGattCharacteristic characteristic) {
            StringBuilder builder = new StringBuilder();
            if (BLEUtils.isCharacteristicReadable(characteristic)) {
                builder.append("Read ");
            }
            if (BLEUtils.isCharacteristicWriteable(characteristic)) {
                builder.append("Write ");
            }
            if (BLEUtils.isCharacteristicNotifiable(characteristic)) {
                builder.append("Notify ");
            }
            return builder.toString();
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);

            print("onCharacteristicRead:" + status);

            if (status == BluetoothGatt.GATT_SUCCESS) {
                print("GATT_Success");
            }
            byte[] data = characteristic.getValue();
            parseData(data);
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);

            print("onCharacteristicWrite:" + status);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);

            print("onCharacteristicChanged");

            parseData(characteristic.getValue());
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);

            print("onDescriptorRead:" + status);
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);

            print("onDescriptorWrite:" + status);
        }

        @Override
        public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
            super.onReliableWriteCompleted(gatt, status);

            print("onReliableWriteCompleted:" + status);
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);

            print(String.format("onReadRemoteRssi rssi:%d, status:%d", rssi, status));
        }

        @Override
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
            super.onMtuChanged(gatt, mtu, status);

            print(String.format("onMtuChanged mtu:%d, status:%d", mtu, status));
        }
    };

    private void writeValue() {
//        BluetoothGattCharacteristic cha = new BluetoothGattCharacteristic();
    }

    private void parseData(byte[] data) {
        print(HexUtils.bytesToHexString(data));
    }

    private void print(String msg) {
        print(KEY_LOG, msg);
    }

    private void print(int type, String msg) {
        Message message = new Message();
        message.what = type;
        message.obj = msg;
        mHandler.sendMessage(message);

        System.out.println(System.currentTimeMillis() + ">>>>>" + msg);
    }


    public static void comeOnBaby(Context context, String mac) {
        Intent service = new Intent(context, BikeService.class);
        service.putExtra(KEY_MAC, mac);
        context.startService(service);
    }
}
