package com.brandonhogan.liftscout.foundation.utils;

import java.text.SimpleDateFormat;
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

}
