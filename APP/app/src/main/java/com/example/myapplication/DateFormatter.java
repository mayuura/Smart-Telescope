package com.example.myapplication;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateFormatter {


    public static Date getCurrentDate() {
        return Calendar.getInstance().getTime();
    }

    public static Date getFollowingDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getCurrentDate());
        calendar.add(Calendar.DAY_OF_YEAR, 1); // Add one day
        return calendar.getTime();
    }

    public static String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }
}

