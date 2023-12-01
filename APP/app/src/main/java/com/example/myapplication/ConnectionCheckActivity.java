package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class ConnectionCheckActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Your code to check Bluetooth and internet connections goes here

        // If connections are successful, move to the next activity
        Intent intent = new Intent(ConnectionCheckActivity.this, TelescopeMountActivity.class);
        startActivity(intent);
        finish();
    }
}
