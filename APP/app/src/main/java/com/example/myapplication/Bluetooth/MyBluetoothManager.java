package com.example.myapplication.Bluetooth;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

public class MyBluetoothManager {
    private BluetoothAdapter bluetoothAdapter;
    private Context context;
    private BluetoothAdapter.LeScanCallback currentLeScanCallback; // Maintain a reference to the current callback

    public MyBluetoothManager(Context context) {
        BluetoothManager btManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = btManager.getAdapter();
    }

    public boolean isBluetoothSupported() {
        return bluetoothAdapter != null;
    }

    public boolean isBluetoothEnabled() {
        return bluetoothAdapter.isEnabled();
    }

    public void startDeviceDiscovery(BluetoothAdapter.LeScanCallback leScanCallback) {
        if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        currentLeScanCallback=leScanCallback;
        bluetoothAdapter.startLeScan(leScanCallback);
    }

    public void stopDeviceDiscovery() {
        if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        bluetoothAdapter.stopLeScan(currentLeScanCallback);
    }

    public BluetoothDevice getRemoteDevice(String address) {
        return bluetoothAdapter.getRemoteDevice(address);
    }
}
