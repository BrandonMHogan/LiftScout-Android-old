package com.brandonhogan.liftscout.foundation.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BhDate {

    private static final String detailedDateFormat = "MMMM d, yyyy";
    private static final String dayOfWeekDateFormat = "EEEE";

    public static String toStringDate (Date date) {
        SimpleDateFormat format = new SimpleDateFormat(detailedDateFormat, Locale.getDefault());
        SimpleDateFormat formatDay = new SimpleDateFormat(dayOfWeekDateFormat, Locale.getDefault());
        return formatDay.format(date) + ", " + format.format(date);
    }

    public static Date trimTimeFromDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);

        return cal.getTime();
    }

}
