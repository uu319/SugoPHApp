package com.example.pikot.sugophapp.RandomClasses;

import java.util.Date;

public class DateHandler {

    public static int minuteBetween(Date d1, Date d2) {
        return (int) ((d2.getTime() - d1.getTime()) / android.text.format.DateUtils.SECOND_IN_MILLIS);
    }

    public static int hoursBetween(Date d1, Date d2) {
        return (int) ((d2.getTime() - d1.getTime()) / android.text.format.DateUtils.MINUTE_IN_MILLIS);
    }

    public static int daysBetween(Date d1, Date d2) {
        return (int) ((d2.getTime() - d1.getTime()) / android.text.format.DateUtils.DAY_IN_MILLIS);
    }
}
