package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.InputStream;
import java.util.List;


public class MainActivity extends AppCompatActivity implements ScriptExecutionListener {
    private EditText pitchInput, rollInput, yawInput, latitudeInput, longitudeInput;
    private TextView resultText;
    private Button calculateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pitchInput = findViewById(R.id.pitchInput);
        rollInput = findViewById(R.id.rollInput);
        yawInput = findViewById(R.id.yawInput);
        latitudeInput = findViewById(R.id.latitudeInput);
        longitudeInput = findViewById(R.id.longitudeInput);
        resultText = findViewById(R.id.resultText);
        calculateButton = findViewById(R.id.calculateButton);

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double pitch = Double.parseDouble(pitchInput.getText().toString());
                double roll = Double.parseDouble(rollInput.getText().toString());
                double yaw = Double.parseDouble(yawInput.getText().toString());
                double latitude = Double.parseDouble(latitudeInput.getText().toString());
                double longitude = Double.parseDouble(longitudeInput.getText().toString());

                double ra = calculateRA(roll, longitude);
                double dec = calculateDec(pitch, yaw, latitude);

                //DatabaseQueryTask queryTask = new DatabaseQueryTask(MainActivity.this); // Pass the MainActivity instance as the listener
                //queryTask.execute(ra, dec, (double) 3); // Pass your desired radius as the third parameter
                resultText.setText(ra+"d "+dec+"d");
            }
        });
    }

    // Implement the conversion and database query methods.
    private double calculateRA( double roll, double longitude) {
        double Ra;
        double JD_J2000= 2451545.0;//reference
        double JD=calculateJD();//JD of the current day D= JD-JD_J2000;

        double D= JD-JD_J2000;
        double lambda=5.9939724; //longitude in degrees T=23; %the number of centuries since the J2000 epoch
        double T=23;
        double T0=280.46061837;
        double LST=100.46 + 0.985647 * D + longitude + 15 * (T - T0);;
        double alpha=roll*Math.PI/180;

        // Calculate RA based on user input.
        Ra=LST-alpha;

        return Ra;
    }

    private double calculateDec(double pitch,double yaw, double latitude) {

        double delta_0=pitch*Math.PI/180;
        double phi=latitude; //latitude=47.2520176 in our example
        double theta=yaw*Math.PI/180;
        // Calculate Dec based on user input.
        double dec= Math.asin(Math.sin(delta_0) * Math.sin(phi) + Math.cos(delta_0) * Math.cos(phi) * Math.cos(theta));
        return dec;
    }



    private double calculateJD() {
        // Get the current date and time
        Calendar currentDateTime = Calendar.getInstance();

        // Extract the year, month, day, and time components
        int year = currentDateTime.get(Calendar.YEAR);
        int month = currentDateTime.get(Calendar.MONTH) + 1;  // Month is 0-based, so add 1
        int day = currentDateTime.get(Calendar.DAY_OF_MONTH);
        int hour = currentDateTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentDateTime.get(Calendar.MINUTE);
        int second = currentDateTime.get(Calendar.SECOND);

        // Use these components to calculate the JD
        double JD = 367 * year - Math.floor(7 * (year + Math.floor((month + 9) / 12.0)) / 4.0)
                + Math.floor(275 * month / 9.0) + day + 1721013.5
                + (hour + minute / 60.0 + second / 3600.0) / 24.0;

        return JD;
    }
    // Additional code for the ScriptExecutionListener
    @Override
    public void onScriptExecuted(List<String> output, int exitCode) {
        // This method is called when the script execution is finished
        // Handle the script execution results here
        // Update your UI or perform any other necessary action
        resultText.setText("Script execution completed. Exit code: " + exitCode);
        // Handle the output data in the 'output' variable
    }

}

