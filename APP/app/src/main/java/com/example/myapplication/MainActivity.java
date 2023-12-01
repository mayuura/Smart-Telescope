package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity  {
    private TextView pitchInput, rollInput, yawInput, latitudeInput, longitudeInput;
    private double currentRa;
    private double currentDec;
    private TextView resultText;
    private Button calculateButton;


    // Default values
    private double defaultPitch = 0.0;
    private double defaultRoll = 0.0;
    private double defaultYaw = 0.0;
    private double defaultLatitude = 47.2520176;
    private double defaultLongitude = 5.9939724;

    @Override
    protected void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //all these inputs are meant to be retrieved from the stm32
        pitchInput = findViewById(R.id.pitchInput);
        rollInput = findViewById(R.id.rollInput);
        yawInput = findViewById(R.id.yawInput);
        latitudeInput = findViewById(R.id.latitudeInput);
        longitudeInput = findViewById(R.id.longitudeInput);
        resultText = findViewById(R.id.resultText);
        calculateButton = findViewById(R.id.calculateButton);

        // Set default values
        pitchInput.setText(String.valueOf(defaultPitch));
        rollInput.setText(String.valueOf(defaultRoll));
        yawInput.setText(String.valueOf(defaultYaw));
        latitudeInput.setText(String.valueOf(defaultLatitude));
        longitudeInput.setText(String.valueOf(defaultLongitude));
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {//what happens when we click the button


                double ra = calculateRA(defaultRoll, defaultLongitude);
                double dec = calculateDec(defaultPitch, defaultYaw, defaultLatitude);
                currentRa=ra;
                currentDec=dec;

                resultText.setText(ra + "d " + dec + "d");


                // Construct the Simbad API URL
                String simbadApiUrl = "https://simbad.cds.unistra.fr/simbad/sim-coo?output.format=ASCII&Coord="+ra+"d+"+dec+"d&Radius=10&Radius.unit=arcmin";

                // Execute the Simbad API query asynchronously
                new SimbadQueryAsyncTask().execute(simbadApiUrl);
            }
        });
        // Locate the "Previous" button in your layout
        Button previousButton = findViewById(R.id.previousButton);

        // Set a click listener for the "Previous" button
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start TelescopeMountActivity
                Intent telescopeMountIntent = new Intent(MainActivity.this, TelescopeMountActivity.class);
                startActivity(telescopeMountIntent);
                finish(); // Optional: finish the current activity if you don't want to keep it in the back stack
            }
        });


    }


    private double calculateRA( double roll, double longitude) {
        //all angles are in degrees

        //double lambda=5.9939724; longitude in degrees T=23; %the number of centuries since the J2000 epoch
        double LST=calculateLST(longitude);

        double alpha=roll*Math.PI/180;

        // Ra=LST_alpha
        return LST-alpha;
    }
    private double calculateLST(double longitude) {
        double JD_J2000 = 2451545.0; // reference
        double JD = calculateJD(); // JD of the current day D = JD - JD_J2000;

        double D = JD - JD_J2000;
        double T = D / 36525;

        // Calculate GMST
        double theta0 = 280.46061837 + 360.98564736629 * (JD - JD_J2000) + (0.000387933 * T * T) - (T * T * T / 38710000.0);
        double gmst = theta0 % 360.0;

        // Calculate LST using the local longitude
        double lst = gmst + longitude;

        // Normalize LST to the range [0, 360)
        if (lst < 0) {
            lst += 360.0;
        }

        return lst;
    }

    private double calculateDec(double pitch,double yaw, double latitude) {
        //all angles are in degrees
        double delta_0=pitch*Math.PI/180;
        double phi=latitude*Math.PI/180; //latitude=47.2520176 in our example
        double theta=yaw*Math.PI/180;
        // Calculate Dec based on user input.
        double dec= Math.asin(Math.sin(delta_0) * Math.sin(phi) + Math.cos(delta_0) * Math.cos(phi) * Math.cos(theta)); //this result is in radians
        return dec* 180 / Math.PI;//convert dec to degrees
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
        return 367 * year - Math.floor(7 * (year + Math.floor((month + 9) / 12.0)) / 4.0)
                + Math.floor(275 * month / 9.0) + day + 1721013.5
                + (hour + minute / 60.0 + second / 3600.0) / 24.0;

    }





    private class SimbadQueryAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            // Perform the Simbad query in the background
            try {
                return HttpUtils.fetchData(urls[0]);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void onPostExecute(String result) {
            // Handle the Simbad query result on the main thread
            if (result != null) {
                Log.d("SimbadQueryResult", result);
                // Handle the  response (parse and extract the data )
                handleSimbadQueryResult(result);
                createMenu(result);

            } else {
                Log.e("SimbadQueryResult", "Error in Simbad query");
                resultText.setText("Error");
            }
        }
        private void createMenu(String result){

            // Populate the Spinner with object names
            Spinner objectSpinner = findViewById(R.id.objectSpinner);
            List<String> objectNames = new ArrayList<>();
            objectNames.add("Select an object");  // Default item

            for (CelestialObject celestialObject : CelestialObject.asciiToObjects(result)) {
                objectNames.add(celestialObject.getIdentifier());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, objectNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            objectSpinner.setAdapter(adapter);

            // Set a listener for item selection
            objectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    // Handle the selected item
                    String selectedObjectName = objectNames.get(position);
                    if (!selectedObjectName.equals("Select an object")) {
                        // Perform actions based on the selected object
                        // For example, display details or perform another API query
                        handleSelectedObject(CelestialObject.asciiToObjects(result).get(position - 1));
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // Do nothing here
                }
            });
        }

        // Method to handle the Simbad query result on the main thread , for now i am just using it to debug
        private void handleSimbadQueryResult(String result) {

            Log.d("processed data :", CelestialObject.paresedDataToString(CelestialObject.asciiParser(result)));
            Log.d("objects :", CelestialObject.listOfObjectsToString(CelestialObject.asciiToObjects(result)));
        }
        private void handleSelectedObject(CelestialObject selectedObject) {

            createAlertDialog(selectedObject);
            Log.d("Selected Object", selectedObject.toString());
        }

        //create an alert dialog that displays the object details
        private void createAlertDialog(CelestialObject selectedObject){

            // Create and configure an AlertDialog
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Instructions");

            String targetRa=selectedObject.getRa();
            String targetDec=selectedObject.getDec();
            Log.d("targetRa : ",targetRa);
            Log.d("targetDec : ",targetDec);
            String instructions="";
            if(TelescopeMountActivity.getMount().equals("Ra/Dec")) {
                 instructions = TelescopeAdjustmentCalculator.calculateTelescopeAdjustment(targetRa, targetDec, currentRa, currentDec);
            }
            else{
                instructions = TelescopeAdjustmentCalculator.calculateTelescopeAdjustment2(targetRa, targetDec, currentRa, currentDec);

            }
            String description = "The selected object is: "+selectedObject.getType();
            builder.setMessage(description+"\n"+instructions);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss(); // Close the dialog when the "OK" button is clicked
                }
            });

            // Show the AlertDialog
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

    }
}

