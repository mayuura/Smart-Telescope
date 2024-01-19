package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.location.Location;
import android.location.LocationManager;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity  {

    private static final int REQUEST_LOCATION =1 ;
    private TextView pitchInput, rollInput, yawInput, latitudeInput, longitudeInput;
    private double currentRa;
    private double currentDec;
    private TextView resultText;
    private Button calculateButton;

    private LocationManager locationManager;


    // Default values
    private double defaultPitch = 0.0;
    private double defaultRoll = 0.0;
    private double defaultYaw = 0.0;
    private double defaultLatitude ;
    private double defaultLongitude ;
    private double currentAlt=0.4; //Km

    private String currentDay=DateFormatter.formatDate(DateFormatter.getCurrentDate());
    private String nextDay=DateFormatter.formatDate(DateFormatter.getFollowingDate());

    private List<CelestialObject> listObservable=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //permissions
        ActivityCompat.requestPermissions(this,new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //
        handleGPS(locationManager);

        //handleBLE : probably using an async task

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
        pitchInput.setHint(getString(R.string.hint_pitch));
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


                //Horizons API URL for jupiter
                String horizonsApiUrl="https://ssd.jpl.nasa.gov/api/horizons.api?format=text&MAKE_EPHEM=YES&OBJ_DATA='YES'&COMMAND=599&EPHEM_TYPE=OBSERVER&CENTER='coord@399'&COORD_TYPE=GEODETIC&START_TIME='"+currentDay+"'&STOP_TIME='"+nextDay+"'&STEP_SIZE='1 HOURS'&QUANTITIES='1,2,3,47,48'&REF_SYSTEM='ICRF'&CAL_FORMAT='CAL'&CAL_TYPE='M'&TIME_DIGITS='MINUTES'&ANG_FORMAT='HMS'&APPARENT='AIRLESS'&RANGE_UNITS='AU'&SUPPRESS_RANGE_RATE='NO'&SKIP_DAYLT='NO'&SOLAR_ELONG='0,180'&EXTRA_PREC='NO'&R_T_S_ONLY='NO'&CSV_FORMAT='NO'";
                String apiUrl="https://api.visibleplanets.dev/v3?latitude="+defaultLatitude+"&longitude="+defaultLongitude+"&showCoords=true";
                //new HorizonsQueryAsyncTask().execute(horizonsApiUrl);
                new ApiQueryAsyncTask().execute(apiUrl);
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

    private void handleGPS(LocationManager locationManager){
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            //Write Function To enable gps

            OnGPS();
        }
        else
        {
            //GPS is already On then
            Log.d("getLocation method", "true");

            getLocation();
        }
    }

    private void getLocation() {

        //Check Permissions again

        if (ActivityCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this,

                Manifest.permission.ACCESS_COARSE_LOCATION) !=PackageManager.PERMISSION_GRANTED)
        {
            Log.d("first condition","true");
            ActivityCompat.requestPermissions(this,new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        else
        {
            Location LocationGps= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location LocationNetwork=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location LocationPassive=locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (LocationGps !=null)
            {
                defaultLatitude=LocationGps.getLatitude();
                defaultLongitude=LocationGps.getLongitude();
                Log.d("lat1", String.valueOf(LocationGps.getLatitude()));


            }
            else if (LocationNetwork !=null)
            {
                defaultLatitude=LocationNetwork.getLatitude();
                defaultLongitude=LocationNetwork.getLongitude();
                Log.d("lat2", String.valueOf(LocationNetwork.getLatitude()));

            }
            else if (LocationPassive !=null)
            {
                defaultLatitude=LocationPassive.getLatitude();
                defaultLongitude=LocationPassive.getLongitude();
                Log.d("lat3", String.valueOf(LocationPassive.getLatitude()));


            }
            else
            {
                Toast.makeText(this, "Can't Get Your Location", Toast.LENGTH_SHORT).show();
                Log.d("error","true");
            }

        }

    }

    private void OnGPS() {

        final AlertDialog.Builder builder= new AlertDialog.Builder(this);

        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });
        final AlertDialog alertDialog=builder.create();
        alertDialog.show();
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
            //from Simbad
            for (CelestialObject celestialObject : listObservable) {
                objectNames.add(celestialObject.getIdentifier());
            }
            //from Api


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
                        handleSelectedObject(listObservable.get(position - 1));
                        //we can add another if statement for the other list coming from the api
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
            listObservable.addAll(CelestialObject.asciiToObjects(result));
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
            String targetAltitude=selectedObject.getAlt();
            String targetAz=selectedObject.getAz();

            String instructions="";
            if(TelescopeMountActivity.getMount().equals("Ra/Dec")) {
                 instructions = TelescopeAdjustmentCalculator.calculateTelescopeAdjustment(targetRa, targetDec, currentRa, currentDec);
            }
            else{
                if(targetAltitude=="" || targetAz=="") {
                    instructions = TelescopeAdjustmentCalculator.calculateTelescopeAdjustment2(targetRa, targetDec, currentRa, currentDec);
                }
                else{
                    instructions=TelescopeAdjustmentCalculator.calculateTelescopeAdjustment3(targetAltitude,targetAz);
                }
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

    private class ApiQueryAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            // Perform the Horizons query in the background
            try {
                return HttpUtils.fetchData(urls[0]);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void onPostExecute(String result) {
            // Handle the Horizons query result on the main thread
            if (result != null) {
                Log.d("ApiQueryResult", result);
                // Handle the response (parse and extract the data)
                handleApiQueryResult(result);
                // Call any other methods or update UI as needed
            } else {
                Log.e("ApiQueryResult", "Error in Horizons query");
                // Handle error condition, update UI or show an error message
            }
        }

        private void handleApiQueryResult(String result)  {
            //1-parse the result string

            Log.d("string size", String.valueOf(result.length()));
            try {
                ApiDataParser.test(result);
                List<CelestialObject> listFromJson=ApiDataParser.jsonToObjects(result);
                Log.d("check",listObservable.size()+"  "+listFromJson);
                listObservable=listFromJson;

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            //Log.d("row test",HorizonDataParser.getRowForHour(result,currentHour));
            //2-fill the celestial object with its details

        }
        private  <T> List<T> concatenateLists(List<T> list1, List<T> list2) {
            // Create a new list and add all elements from list1
            List<T> result = new ArrayList<>(list1);

            // Add all elements from list2
            result.addAll(list2);

            return result;
        }

        //method to reduce the actual output into the relevant data we need

    }

}

