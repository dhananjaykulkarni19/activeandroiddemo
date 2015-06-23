package com.activeandroiddemo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by dhananjay on 23/6/15.
 */
public class Utils {

    public static float KelvinToCelsius(double kelvinValue){
        return (float) (kelvinValue - 273.15);
    }

    public static String getDate(long timeStamp){

        Date date = new Date(timeStamp*1000L);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getDefault());
        String currentDate = dateFormat.format(date);
        return currentDate;
    }
}
