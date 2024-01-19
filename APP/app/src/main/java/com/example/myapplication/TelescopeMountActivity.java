package com.example.myapplication;

import static android.bluetooth.BluetoothProfile.STATE_CONNECTED;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.os.AsyncTask;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class TelescopeMountActivity extends AppCompatActivity {
    TextView instructionTextView;
    public static String mount;
    public String connection_status = null;
    private BluetoothLeService mBluetoothLeService;
    private ConnectBluetoothTask connectBluetoothTask;
    BluetoothGatt mBluetoothGatt;
    private float accel;
    private static final String DEVICE_A_ADDRESS = "E2:CE:30:0E:75:EE";
    TextView connection;
    public String service_uuid = "00000000-0001-11e1-9ab4-0002a5d5c51b\n";
    public String characteristic_uuid = "0000e000-0001-11e1-ac36-0002a5d5c51b\n";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telecope_mount);
        instructionTextView = findViewById(R.id.instructionTextView);
        Button raDecButton = findViewById(R.id.raDecButton);
        Button altAzButton = findViewById(R.id.altAzButton);
        connection = findViewById(R.id.connection_BLE);


        // Start automatic device connection

        /*ble = new BluetoothLeManager(this);
        if (ble.initialize()) {
            // Register a receiver to listen for the service connection event
            LocalBroadcastManager.getInstance(this).registerReceiver(
                    mServiceConnectedReceiver,
                    new IntentFilter("BluetoothLeServiceConnected")
            );
            ble.connectToDevice(DEVICE_A_ADDRESS);
            connection_status="CONNECTED";
        } else {
            // Bluetooth not supported on the device.
            // Handle accordingly, e.g., show a message to the user.
        }*/
        // Initialize Bluetooth adapter
        /*mBluetoothLeService = new BluetoothLeService();
        if (!mBluetoothLeService.initialize(this)) {
            finish();
        }
        Log.d("instance is null before", String.valueOf(mBluetoothLeService==null));
        // Connect to the device
        if(mBluetoothLeService.connect(DEVICE_A_ADDRESS)){
            connection_status="CONNECTED";
        }*/


        // Create a timer task to read data periodically
       /*Log.d("services size :", String.valueOf(mBluetoothLeService.getmBluetoothGatt().getServices().size()));
        mDataReader = new DataReader(mBluetoothLeService);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(mDataReader, 1000, 1000);*/
        mBluetoothLeService = new BluetoothLeService();
        connectBluetoothTask = new ConnectBluetoothTask(mBluetoothLeService);
        connectBluetoothTask.execute();

        //new ConnectAndReadTask(this, mBluetoothLeService).execute();


        raDecButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If Ra/Dec mount is selected, pass the information to MainActivity
                Intent intent = new Intent(TelescopeMountActivity.this, MainActivity.class);
                intent.putExtra("mountType", "Ra/Dec");
                mount = "Ra/Dec";
                startActivity(intent);
                finish();
            }
        });

        altAzButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If Alt-Az mount is selected, pass the information to MainActivity
                Intent intent = new Intent(TelescopeMountActivity.this, MainActivity.class);
                intent.putExtra("mountType", "Alt-Az");
                mount = "Alt-Az";
                startActivity(intent);
                finish();
            }
        });


    }



    public static String getMount() {
        return mount;
    }


    private class ConnectBluetoothTask extends AsyncTask<String, Void, String> {
        BluetoothLeService mBluetoothLeService;
        BluetoothManager mBluetoothManager;
        BluetoothAdapter mBluetoothAdapter;
        BluetoothDevice device;

        public ConnectBluetoothTask(BluetoothLeService mBluetoothLeService) {
            this.mBluetoothLeService = mBluetoothLeService;
        }

        @Override
        protected void onPreExecute() {
            mBluetoothManager = (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
            mBluetoothAdapter = mBluetoothManager.getAdapter();
        }

        @Override
        protected String doInBackground(String... strings) {
            if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
                return "Bluetooth not enabled";
            }

            device = mBluetoothAdapter.getRemoteDevice(DEVICE_A_ADDRESS);

            if(device == null) {
                return "Cannot connect to the device.";
            }

            if (!mBluetoothLeService.connect(device.getAddress())) {
                return "Connection failed.";
            }

            // Simulating data read sleep, replace this with actual read from microcontroller
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return "Connected";
        }

        @Override
        protected void onPostExecute(String result) {
            connection.setText(result);

            if (result.equals("Connected")) {
                // TODO: After a successful connection, initiate data reading from microcontroller

                // Get the BluetoothGatt instance from the BluetoothLeService
                BluetoothGatt bluetoothGatt = mBluetoothLeService.getBluetoothGatt();

                //readAccelerometerData(bluetoothGatt);//this one for obcp but can also be tweeked for stm32

                readSampleData(bluetoothGatt);//this one is for stm32 : see lab
            }
        }
        private float convertDataToFloat(BluetoothGattCharacteristic caract) {
            // Convert the data to float
            byte[] data=caract.getValue();
            ByteBuffer byteBuffer = ByteBuffer.wrap(data);
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
            return byteBuffer.getFloat();
        }
        private void readAccelerometerData(BluetoothGatt bluetoothGatt){
            if (bluetoothGatt != null) {
                // Discover services
                BluetoothGattService bluetoothGattService = bluetoothGatt.getService(UUID.fromString("0000A000-0000-1000-8000-00805F9B34FB"));

                if (bluetoothGattService != null) {

                    // Read acceleration_X characteristic
                    BluetoothGattCharacteristic accelerationXCharacteristic = bluetoothGattService.getCharacteristic(UUID.fromString("0000A002-0000-1000-8000-00805F9B34FB"));
                    mBluetoothLeService.readCharacteristic(accelerationXCharacteristic);
                    convertDataToFloat(accelerationXCharacteristic);//take this float and put it in the accel variable

                    // Read acceleration_y characteristic
                    BluetoothGattCharacteristic accelerationYCharacteristic = bluetoothGattService.getCharacteristic(UUID.fromString("0000A003-0000-1000-8000-00805F9B34FB"));
                    mBluetoothLeService.readCharacteristic(accelerationYCharacteristic);
                    convertDataToFloat(accelerationYCharacteristic);

                    // Read acceleration_Z characteristic
                    BluetoothGattCharacteristic accelerationZCharacteristic = bluetoothGattService.getCharacteristic(UUID.fromString("0000A004-0000-1000-8000-00805F9B34FB"));
                    mBluetoothLeService.readCharacteristic(accelerationZCharacteristic);
                    convertDataToFloat(accelerationZCharacteristic);
                }
            }
        }
        private void readSampleData(BluetoothGatt bluetoothGatt) {
            if (bluetoothGatt != null) {
                // Discover services
                BluetoothGattService bluetoothGattService = bluetoothGatt.getService(UUID.fromString(service_uuid));

                if (bluetoothGattService != null) {

                    // Read sample characteristic
                    BluetoothGattCharacteristic sampleCharacteristic = bluetoothGattService.getCharacteristic(UUID.fromString(characteristic_uuid));
                    mBluetoothLeService.readCharacteristic(sampleCharacteristic);
                    byte[] data=sampleCharacteristic.getValue();
                    ByteBuffer byteBuffer = ByteBuffer.wrap(data);
                    byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
                    accel=byteBuffer.getFloat();
                    Log.d("accel", String.valueOf(accel));
                }
            }
        }

    }




}
