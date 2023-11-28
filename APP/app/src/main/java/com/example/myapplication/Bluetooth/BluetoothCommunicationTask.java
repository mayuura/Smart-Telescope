package com.example.myapplication.Bluetooth;


import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.AsyncTask;

import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

public class BluetoothCommunicationTask extends AsyncTask<Void, Void, Void> {
    private Context context;
    private MyBluetoothManager bluetoothManager;
    private DeviceScanCallback deviceScanCallback;
    private BluetoothDeviceConnector deviceConnector;
    private GattCallback gattCallback;
    private List<BluetoothDevice> discoveredDevices = new ArrayList<>();
    private AlertDialog deviceSelectionDialog;

    public BluetoothCommunicationTask(Context context, DeviceScanCallback deviceScanCallback) {
        this.context = context;
        this.deviceScanCallback = deviceScanCallback;
        this.bluetoothManager = new MyBluetoothManager(context);
        this.deviceConnector = new BluetoothDeviceConnector(context);
        this.gattCallback = new GattCallback();
    }

    @Override
    protected Void doInBackground(Void... params) {
        bluetoothManager.startDeviceDiscovery(new BluetoothAdapter.LeScanCallback() {
            @Override
            public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                // Notify the UI or other components about the discovered device
                deviceScanCallback.onDeviceFound(device);

                // Add discovered devices to the list
                discoveredDevices.add(device);
            }
        });

        // Wait for a device to be discovered (you may add a timeout or other conditions)

        // Stop device discovery before showing the device selection dialog
        bluetoothManager.stopDeviceDiscovery();

        // Show the device selection dialog
        showDeviceSelectionDialog();

        return null;
    }

    private void showDeviceSelectionDialog() {
        // Build a dialog with a list of discovered devices
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Select a device");
        final String[] deviceNames = getDeviceNamesArray(discoveredDevices);
        builder.setItems(deviceNames, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Connect to the selected device
                BluetoothDevice selectedDevice = discoveredDevices.get(which);
                boolean isConnected = deviceConnector.connectToDevice(selectedDevice, gattCallback);
                if (!isConnected) {
                    // Handle connection failure
                    cancel(true);
                }

                // Process sensor data if connected
                startRealTimeDataUpdates();
            }
        });

        // Create and show the dialog
        deviceSelectionDialog = builder.create();
        deviceSelectionDialog.show();
    }

    private String[] getDeviceNamesArray(List<BluetoothDevice> devices) {
        String[] deviceNames = new String[devices.size()];
        for (int i = 0; i < devices.size(); i++) {
            if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

            }
            deviceNames[i] = devices.get(i).getName();
        }
        return deviceNames;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        // Update UI or trigger other actions based on the Bluetooth communication result
    }

    @Override
    protected void onCancelled() {
        // Handle the case where the task is canceled (e.g., connection failure)
    }

    private void startRealTimeDataUpdates() {
        // Implement real-time data updates here using BluetoothGatt methods
        // For example, use gattCallback.onCharacteristicChanged to receive data updates
        // Update the UI with processed data in real-time
    }

    // Add other methods or overrides as needed
}


