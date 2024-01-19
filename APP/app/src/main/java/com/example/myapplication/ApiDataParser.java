package com.example.myapplication;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ApiDataParser {
    private static final String TAG = "JsonParsingExample";
    public static void test(String jsonData) throws JSONException {
        // Parse the JSON string
        JSONObject json = new JSONObject(jsonData);

        // Extract meta information
        printMetaInfo(json);

        // Extract and print data
        for(CelestialObject object : jsonToObjects(jsonData)){
         Log.d(object.getIdentifier(),object.toString());
        }
    }
    private static void printMetaInfo(JSONObject json) throws JSONException {
        JSONObject meta = json.getJSONObject("meta");
        String time = meta.getString("time");
        double latitude = meta.getDouble("latitude");
        double longitude = meta.getDouble("longitude");
        boolean aboveHorizon = meta.getBoolean("aboveHorizon");

        Log.d(TAG, "Time: " + time);
        Log.d(TAG, "Latitude: " + latitude);
        Log.d(TAG, "Longitude: " + longitude);
        Log.d(TAG, "Above Horizon: " + aboveHorizon);
    }

    public static List<CelestialObject> jsonToObjects(String jsonData) throws JSONException {
        JSONObject json = new JSONObject(jsonData);
        JSONArray dataArray = json.getJSONArray("data");
        List<CelestialObject> out=new ArrayList<>();
        for (int i = 0; i < dataArray.length(); i++) {
            JSONObject planetData = dataArray.getJSONObject(i);

            // Extract and print planet data
            out.add(jsonToObject(planetData));
            //create corresponding celestial object

        }
        return out;
    }

    private static CelestialObject jsonToObject(JSONObject planetData) throws JSONException {
        String name = planetData.getString("name");
        String constellation = planetData.getString("constellation");

        JSONObject rightAscension = planetData.getJSONObject("rightAscension");
        int raHours = rightAscension.getInt("hours");
        int raMinutes = rightAscension.getInt("minutes");
        double raSeconds = rightAscension.getDouble("seconds");

        JSONObject declination = planetData.getJSONObject("declination");
        boolean declinationNegative = declination.getBoolean("negative");
        int decDegrees = declination.getInt("degrees");
        int decArcMinutes = declination.getInt("arcminutes");
        double decArcSeconds = declination.getDouble("arcseconds");

        double altitude = planetData.getDouble("altitude");
        double azimuth = planetData.getDouble("azimuth");
        boolean aboveHorizonPlanet = planetData.getBoolean("aboveHorizon");

        double magnitude = planetData.getDouble("magnitude");
        boolean nakedEyeObject = planetData.getBoolean("nakedEyeObject");
        String ra=raHours + " " + raMinutes + " " + raSeconds;
        String dec=(declinationNegative ? "-" : "+") + decDegrees + " " + decArcMinutes + " " + decArcSeconds;
        String coordinates=ra+" "+dec;


        return new CelestialObject(name,"Planet",coordinates,"",Double.toString(altitude),Double.toString(azimuth));
    }
    private static double convertDeclinationToDegrees(String declinationString) {
        // Example input: "+26° 47' 26.7\""

        // Split the input string to extract degrees, arcminutes, and arcseconds
        String[] parts = declinationString.split(" ");

        // Extract values
        int degrees = Integer.parseInt(parts[0]);
        int arcMinutes = Integer.parseInt(parts[1]);
        double arcSeconds = Double.parseDouble(parts[2]);

        // Calculate the result in degrees
        double result = degrees + arcMinutes / 60.0 + arcSeconds / 3600.0;

        // Check if the declination is negative
        if (declinationString.startsWith("-")) {
            result = -result;
        }

        return result;
    }
}
