package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

public class DeviceConnectionManager {
    private static final String TAG = DeviceConnectionManager.class.getSimpleName();

    private final Context context;
    private final BluetoothLeService bluetoothLeService;
    private static final String DEVICE_A_ADDRESS ="E2:CE:30:0E:75:EE" ;
    private static final long CONNECTION_RETRY_INTERVAL = 10000; // 10 seconds
    private final Handler connectionHandler = new Handler();
    private final Runnable connectionRunnable = this::connectToDeviceA;

    public DeviceConnectionManager(Context context, BluetoothLeService bluetoothLeService) {
        this.context = context;
        this.bluetoothLeService = bluetoothLeService;
    }

    public String startDeviceConnection() {
        // Start connecting to Device A
        return connectToDeviceA();
    }

    private String connectToDeviceA() {
        String msg;
        if (bluetoothLeService.initialize(this.context)) {
            // Attempt to connect to Device A
            if (bluetoothLeService.connect(DEVICE_A_ADDRESS)) {
                // Connection initiated successfully
                Log.d(TAG, "Connection to Device A initiated successfully.");
                msg="Connection to Telescope initiated successfully.";
            } else {
                // Connection failed, retry after a delay
                Log.e(TAG, "Failed to connect to Device A. Retrying in " + CONNECTION_RETRY_INTERVAL + " milliseconds.");
                connectionHandler.postDelayed(connectionRunnable, CONNECTION_RETRY_INTERVAL);
                msg="Failed to connect to Telescope. Retrying in " + CONNECTION_RETRY_INTERVAL + " milliseconds.";
            }
        } else {
            Log.e(TAG, "Bluetooth initialization failed.");
            msg="Bluetooth initialization failed.";
        }
        return msg;
    }


    public void stopDeviceConnection() {
        // Stop any ongoing connection attempts
        connectionHandler.removeCallbacks(connectionRunnable);
    }

    // Add any additional methods or callbacks as needed
}

