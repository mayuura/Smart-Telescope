package com.example.myapplication;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HorizonDataParser {


    public static int getCurrentHour() {
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH");
        return Integer.parseInt(dateFormat.format(currentDate));
    }

    public static String getRowForHour(String data, int currentHour) {
        String[] lines = data.split("\n");

        // Assuming the data is hourly, and the hour is represented in the first 2 characters of each line
        String searchHour = String.format("%02d"+":00", currentHour);
        String header="*****************************************************************************************************************************************************\n" +
                " Date__(UT)__HR:MN     R.A._____(ICRF)_____DEC  R.A.__(a-apparent)__DEC  dRA*cosD d(DEC)/dt  Sky_motion  Sky_mot_PA  RelVel-ANG  Lun_Sky_Brt  sky_SNR\n" +
                "*****************************************************************************************************************************************************\n";
        for (String line : lines) {
            if (line.contains(searchHour)) {
                return header+line;
            }
        }

        return null; // Hour not found in data
    }
}
