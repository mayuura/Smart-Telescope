package com.example.myapplication;

import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class TelescopeMountActivity extends AppCompatActivity {
    TextView instructionTextView;
    public static String mount;
    public String connection_status;
    TextView connection;
    private BluetoothLeService bluetoothLeService;
    private DeviceConnectionManager connectionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telecope_mount);
        instructionTextView = findViewById(R.id.instructionTextView);
        Button raDecButton = findViewById(R.id.raDecButton);
        Button altAzButton = findViewById(R.id.altAzButton);
        connection=findViewById(R.id.connection_BLE);

        //display connection check
        // Initialize BluetoothLeService
        bluetoothLeService = new BluetoothLeService();

        // Initialize DeviceConnectionManager
        connectionManager = new DeviceConnectionManager(this, bluetoothLeService);

        // Start automatic device connection


        BroadcastReceiver mConnectionStatusReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int connectionStatus = intent.getIntExtra(BluetoothLeService.EXTRA_CONNECTION_STATUS, -1);
                if (connectionStatus == BluetoothProfile.STATE_CONNECTED) {
                    // Device is connected
                    Log.d("status", "Device is connected.");
                    connection_status="Device is connected.";
                } else if (connectionStatus == BluetoothProfile.STATE_DISCONNECTED) {
                    // Device is disconnected
                    Log.d("status", "Device is disconnected.");
                    connection_status="Device is disconnected.";
                }
            }
        };
        if(mConnectionStatusReceiver!=null) {
            registerReceiver(mConnectionStatusReceiver, new IntentFilter(BluetoothLeService.ACTION_CONNECTION_STATUS));
        }
        connection.setText(connectionManager.startDeviceConnection()+" "+connection_status);

        raDecButton.setOnClickListener(new View.OnClickListener() {
            @Override
           public void onClick(View v) {
                // If Ra/Dec mount is selected, pass the information to MainActivity
                Intent intent = new Intent(TelescopeMountActivity.this, MainActivity.class);
                intent.putExtra("mountType", "Ra/Dec");
                mount="Ra/Dec";
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
                mount="Alt-Az";
                startActivity(intent);
                finish();
            }
        });


    }
    public static String getMount(){
        return mount;
    }
}
