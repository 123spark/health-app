package com.lhj.classic.bluetooth.ble.comm;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;


public interface MyGattListener {

    /**
     * 连接
     *
     * @param gatt
     */
    void onConnect(BluetoothGatt gatt);

    /**
     * 断开
     *
     * @param gatt
     */
    void onDisconnect(BluetoothGatt gatt);

    /**
     * 发现
     *
     * @param gatt
     */
    void onServiceDiscover(BluetoothGatt gatt);

    /**
     * 读
     *
     * @param gatt
     * @param characteristic
     * @param status
     */
    void onCharacteristicRead(BluetoothGatt gatt,
                              BluetoothGattCharacteristic characteristic,
                              int status);

    /**
     * 写
     *
     * @param gatt
     * @param characteristic
     * @param status
     */
    void onCharacteristicWrite(BluetoothGatt gatt,
                               BluetoothGattCharacteristic characteristic, int status);
}
