package com.example.myapplication;

import android.os.AsyncTask;

public class BluetoothCommunicationTask extends AsyncTask<Void, Void, Void> {
    @Override
    protected Void doInBackground(Void... params) {
        // Implement Bluetooth communication with STM32 here.
        // Receive sensor data.
        //process sensor data
        // Call the DatabaseQueryTask to perform the database query.
        return null;
    }
    protected long[] processData(long[] data){
        long[] processed_data= new long[2]; //Ra and Dec
        //formulas to calculate Ra and Dec using the euler angles
        return processed_data;
    }
}

