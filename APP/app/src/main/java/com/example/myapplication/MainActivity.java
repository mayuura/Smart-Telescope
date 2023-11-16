package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity  {
    private EditText pitchInput, rollInput, yawInput, latitudeInput, longitudeInput;
    private TextView resultText;
    private Button calculateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //all these inputs are meant to be retrieved from the stm32
        pitchInput = findViewById(R.id.pitchInput);
        rollInput = findViewById(R.id.rollInput);
        yawInput = findViewById(R.id.yawInput);
        latitudeInput = findViewById(R.id.latitudeInput);
        longitudeInput = findViewById(R.id.longitudeInput);
        resultText = findViewById(R.id.resultText);
        calculateButton = findViewById(R.id.calculateButton);

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                double pitch = Double.parseDouble(pitchInput.getText().toString());
                double roll = Double.parseDouble(rollInput.getText().toString());
                double yaw = Double.parseDouble(yawInput.getText().toString());
                double latitude = Double.parseDouble(latitudeInput.getText().toString());
                double longitude = Double.parseDouble(longitudeInput.getText().toString());

                double ra = calculateRA(roll, longitude);
                double dec = calculateDec(pitch, yaw, latitude);


                resultText.setText(ra + "d " + dec + "d");


                // Construct the Simbad API URL
                String simbadApiUrl = "https://simbad.cds.unistra.fr/simbad/sim-coo?output.format=ASCII&Coord="+3908.4429453+"d+"+-1.442668530641793+"d&Radius=3&Radius.unit=arcmin";

                // Execute the Simbad API query asynchronously
                new SimbadQueryAsyncTask().execute(simbadApiUrl);
            }
        });
    }


    private double calculateRA( double roll, double longitude) {
        double Ra;
        double JD_J2000= 2451545.0;//reference
        double JD=calculateJD();//JD of the current day D= JD-JD_J2000;

        double D= JD-JD_J2000;
        //double lambda=5.9939724; longitude in degrees T=23; %the number of centuries since the J2000 epoch
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
    private String Ra_to_sexa(double ra){
        return "20 33 44.4";
    }
    private String Dec_to_sexa(double dec){
        return"-01 25 34";
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
                resultText.setText(result);
                // Handle the  response (parse and extract the data )
                handleSimbadQueryResult(result);
            } else {
                Log.e("SimbadQueryResult", "Error in Simbad query");
                resultText.setText("Error");
            }
        }

        // Method to handle the Simbad query result on the main thread
        private void handleSimbadQueryResult(String result) {

            Log.d("processed data :", paresedDataToString(asciiParser(result)));
            Log.d("objects :", listOfObjectsToString(asciiToObjects(result)));
            resultText.setText(listOfObjectsToString(asciiToObjects(result)));
        }

        private List<String> asciiParser(String simbadResponse) {

            // Split the response into lines
            String[] lines = simbadResponse.split("\n");
            List<String> data = new ArrayList<>();
            for (int i = 5; i < lines.length; i++) {
                String[] columns = lines[i].split("\\|");

                String number = columns[0];
                String distAsec = (columns.length > 1) ? columns[1] : "";
                String identifier = (columns.length > 2) ? columns[2] : "";
                String type = (columns.length > 3) ? columns[3] : "";
                String coord = (columns.length > 4) ? columns[4] : "";

                data.add(number);
                data.add(distAsec);
                data.add(identifier);
                data.add(type);
                data.add(coord);
            }
            return data;
        }
        private String paresedDataToString(List<String> list){
            String out="";
            for(String line : list){
                out+=line+"\n";
            }
            return out;
        }
        private List<CelestialObject> asciiToObjects(String simbadResponse) {

            // Split the response into lines
            String[] lines = simbadResponse.split("\n");
            List<CelestialObject> list = new ArrayList<>();
            for (int i = 0; i < lines.length; i++) {


                String[] columns = lines[i].split("\\|");
                // Skip lines that are headers or irrelevant
                if (columns.length < 5 || !isInteger(columns[0])) {
                    continue;
                }
                String number = columns[0];
                String distAsec = (columns.length > 1) ? columns[1] : "";
                String identifier = (columns.length > 2) ? columns[2] : "";
                String type = (columns.length > 3) ? columns[3] : "";
                String coord = (columns.length > 4) ? columns[4] : "";
                String typeDescription=SimbadObjectType.getDescription(type);

                //create the object
                CelestialObject object=new CelestialObject(identifier,type+": "+typeDescription,coord,distAsec);
                list.add(object);
            }
            return list;
        }
        private String listOfObjectsToString(List<CelestialObject> list){
            String out="";
            for(CelestialObject object : list){
                out+=object.toString()+"\n";
            }
            return out;
        }
        public  boolean isInteger(String str) {
            try {
                Integer.parseInt(str);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
    }
}

