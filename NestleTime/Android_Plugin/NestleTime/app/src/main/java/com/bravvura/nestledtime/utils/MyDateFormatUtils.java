package com.bravvura.nestledtime.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 27-03-2018.
 */

public class MyDateFormatUtils {
    public static String getNewDate() {
        return getNewDate(new Date());
    }
    public static String getNewDate(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZ").format(date);
    }


    public static Date toDate(String dateStr) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZ").parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDoeDate(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }
}
