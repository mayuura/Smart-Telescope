package com.example.myapplication.Bluetooth;

import android.bluetooth.BluetoothDevice;

public interface DeviceScanCallback {
    void onDeviceFound(BluetoothDevice device);
}

