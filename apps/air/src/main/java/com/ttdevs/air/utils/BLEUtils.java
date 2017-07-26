package com.ttdevs.air.utils;

import android.bluetooth.BluetoothGattCharacteristic;

/**
 * From: http://stackoverflow.com/questions/21300915/android-how-to-read-ble-properties-readable-writable-notifiable-gatt-characteris
 * <p>
 * Created by ttdevs
 * 2017-05-18 (android)
 * https://github.com/ttdevs
 */
public class BLEUtils {

    /**
     * @return Returns <b>true</b> if property is writable
     */
    public static boolean isCharacteristicWriteable(BluetoothGattCharacteristic pChar) {
        return (pChar.getProperties() & (BluetoothGattCharacteristic.PROPERTY_WRITE | BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE)) != 0;
    }

    /**
     * @return Returns <b>true</b> if property is Readable
     */
    public static boolean isCharacteristicReadable(BluetoothGattCharacteristic pChar) {
        return ((pChar.getProperties() & BluetoothGattCharacteristic.PROPERTY_READ) != 0);
    }

    /**
     * @return Returns <b>true</b> if property is supports notification
     */
    public static boolean isCharacteristicNotifiable(BluetoothGattCharacteristic pChar) {
        return (pChar.getProperties() & BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0;
    }


}
