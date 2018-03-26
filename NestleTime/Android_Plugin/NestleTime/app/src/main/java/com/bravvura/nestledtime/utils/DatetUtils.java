package com.bravvura.nestledtime.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 23-03-2018.
 */

public class DatetUtils {
    public static String getDateElementString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM, yyyy");
        return dateFormat.format(date);
    }
}
