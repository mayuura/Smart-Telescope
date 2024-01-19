package com.example.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;
import android.bluetooth.BluetoothGattCharacteristic;

import androidx.appcompat.app.AppCompatActivity;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.UUID;

public class BleActivity extends AppCompatActivity {
    private final static int REQUEST_ENABLE_BT = 1;
    BluetoothAdapter bluetoothAdapter;
    BluetoothLeScanner bluetoothLeScanner;

    private static final String DEVICE_A_ADDRESS ="E2:CE:30:0E:75:EE";

    public String service_uuid ="00000000-0001-11e1-9ab4-0002a5d5c51b";
    public String characteristic_uuid ="00e00000-0001-11e1-ac36-0002a5d5c51b";
    public String characteristic_uuid2 ="00140000-0001-11e1-ac36-0002a5d5c51b";
    TextView state;
    TextView dataTextView;
    private static final String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            return;
        }
        state = findViewById(R.id.stateTextView);
        dataTextView = findViewById(R.id.dataTextView);
        Button connectButton = findViewById(R.id.connectButton);
        connectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!bluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                } else {
                    connectToDevice();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_OK) {
            connectToDevice();
        }
    }

    private void connectToDevice() {
        bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
        bluetoothLeScanner.startScan(leScanCallback);
    }

    private ScanCallback leScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            BluetoothDevice device = result.getDevice();
            if (device.getAddress().equals(DEVICE_A_ADDRESS)) {
                // Connect to the device
                device.connectGatt(BleActivity.this, false, new BluetoothGattCallback() {
                    @Override
                    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                        super.onConnectionStateChange(gatt, status, newState);
                        if ((newState == BluetoothProfile.STATE_CONNECTED) && (status == BluetoothGatt.GATT_SUCCESS)) {
                            state.setText("Connected");
                            gatt.discoverServices();
                        } else if ((newState == BluetoothProfile.STATE_DISCONNECTED) && (status == BluetoothGatt.GATT_SUCCESS)) {
                            gatt.close();
                            state.setText("Disconnected");
                            Log.d("GattCallback", "onConnectionStateChange: DISCONNECTED");
                        }  else {
                            Log.e("GattCallback", "onConnectionStateChange: FAIL");
                        }
                        /*switch (newState) {
                            case BluetoothProfile.STATE_CONNECTED:
                                Log.i("GattCallback", "Connected");
                                state.setText("Connected");
                                gatt.discoverServices();
                                break;
                            case BluetoothProfile.STATE_DISCONNECTED:
                                Log.i("GattCallback", "Disconnected");
                                state.setText("Disconnected");
                                break;
                        }*/
                    }
                    @Override
                    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                        super.onServicesDiscovered(gatt, status);
                        if (status == BluetoothGatt.GATT_SUCCESS) {
                            // Services discovered, read sample data
                            readSampleData(gatt);
                        } else {
                            Log.e("GattCallback", "Service discovery failed with status: " + status);
                        }
                    }

                    @Override
                    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                        super.onCharacteristicChanged(gatt, characteristic);
                        // Characteristic value has changed, update UI
                        byte[] data = characteristic.getValue();
                        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
                        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
                        Log.d("characteristic changed","true");
                        float accel = byteBuffer.getFloat();
                        dataTextView.setText(String.valueOf(accel * 100));
                        Log.d("accel", String.valueOf(accel));
                    }

                    @Override
                    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                        super.onCharacteristicRead(gatt, characteristic, status);
                        if (status == BluetoothGatt.GATT_SUCCESS) {
                            // Characteristic read successfully
                            Log.d("characteristic value :", String.valueOf(characteristic.getStringValue(0)));
                            byte[] data=characteristic.getValue();
                            ByteBuffer byteBuffer = ByteBuffer.wrap(data);
                            byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
                            float accel=byteBuffer.getFloat();
                            dataTextView.setText(String.valueOf(accel*100));
                            Log.d("accel", String.valueOf(accel));
                        } else {
                            Log.e("GattCallback", "Characteristic read failed with status: " + status);
                        }
                    }
                    @Override
                    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                        if (status == BluetoothGatt.GATT_SUCCESS) {
                            Log.i("GattCallback", "Descriptor write successful!");
                        } else {
                            Log.e("GattCallback", "Descriptor write failed with status: " + status);
                        }
                    }




                });
                Toast.makeText(BleActivity.this, "Device found and trying to connect", Toast.LENGTH_SHORT).show();
                bluetoothLeScanner.stopScan(this);
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            Toast.makeText(BleActivity.this, "Scan failed with error: " + errorCode, Toast.LENGTH_SHORT).show();
        }
        private void readSampleData(BluetoothGatt bluetoothGatt) {
            if (bluetoothGatt != null) {
                // Discover services
                BluetoothGattService bluetoothGattService = bluetoothGatt.getService(UUID.fromString(service_uuid));

                if (bluetoothGattService != null) {

                    // Read sample characteristic
                    Log.d("characteristics", String.valueOf(bluetoothGattService.getCharacteristics().get(1).getUuid().toString()));
                    BluetoothGattCharacteristic sampleCharacteristic = bluetoothGattService.getCharacteristic(UUID.fromString(characteristic_uuid2));
                    Log.d("characteristic obj properties :", String.valueOf(sampleCharacteristic.getProperties()));
                    Log.d("read property flag :", String.valueOf(BluetoothGattCharacteristic.PROPERTY_READ));
                    //bluetoothGatt.readCharacteristic(sampleCharacteristic);
                    Log.d("notify property flag :", String.valueOf(BluetoothGattCharacteristic.PROPERTY_NOTIFY));

                    if (sampleCharacteristic != null) {
                        // Enable notifications for the characteristic

                        bluetoothGatt.setCharacteristicNotification(sampleCharacteristic, true);
                        Log.d("Notification enabled","true");

                        // Find descriptor for the characteristic (usually it's CLIENT_CHARACTERISTIC_CONFIG)
                        BluetoothGattDescriptor descriptor = sampleCharacteristic.getDescriptor(
                                UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG));
                        if (descriptor != null) {
                            // Set descriptor value to enable notification
                            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                            Log.d("value of writedescriptor", Arrays.toString(descriptor.getValue()) );
                            bluetoothGatt.writeDescriptor(descriptor);
                        } else {
                            Log.e("GattCallback", "Descriptor not found");
                        }
                        bluetoothGatt.readCharacteristic(sampleCharacteristic);

                    }

                }
            }
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //disconnectDevice();
    }

    private void disconnectDevice() {

        bluetoothLeScanner.stopScan(leScanCallback);
        Log.d("device disconnected","true");
    }
}
