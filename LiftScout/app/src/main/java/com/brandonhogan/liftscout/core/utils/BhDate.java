package com.brandonhogan.liftscout.core.utils;


import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.brandonhogan.liftscout.core.utils.Constants.MONTH_YEAR_DATE_FORMAT;
import static com.brandonhogan.liftscout.core.utils.Constants.SIMPLE_DATE_FORMAT;

public class BhDate {

    private static final String detailedDateFormat = "MMMM d, yyyy";
    private static final String dayOfWeekDateFormat = "EEEE";

    public static String toStringDate (Date date) {
        SimpleDateFormat format = new SimpleDateFormat(detailedDateFormat, Locale.getDefault());
        SimpleDateFormat formatDay = new SimpleDateFormat(dayOfWeekDateFormat, Locale.getDefault());
        return formatDay.format(date) + ", " + format.format(date);
    }

    public static Date toDateFromString(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat(SIMPLE_DATE_FORMAT, Locale.getDefault());

        try {
            return format.parse(dateString);
        }
        catch (java.text.ParseException ex) {
            Log.e("BhDate", ex.getMessage());
        }

        return null;
    }

    public static String toSimpleStringDate(Date date) {
        return new SimpleDateFormat(SIMPLE_DATE_FORMAT, Locale.getDefault()).format(date);
    }

    public static String toSimpleStringDate(long date) {
        return new SimpleDateFormat(SIMPLE_DATE_FORMAT, Locale.getDefault()).format(date);
    }

    public static String toSimpleDateRange(long start, long end) {
        return toSimpleDateRange(new Date(start), new Date(end));
    }

    public static String toSimpleDateRange(Date start, Date end) {
        String startDate = new SimpleDateFormat(SIMPLE_DATE_FORMAT, Locale.getDefault()).format(start);
        String endDate = new SimpleDateFormat(SIMPLE_DATE_FORMAT, Locale.getDefault()).format(end);

        return startDate + " - " + endDate;
    }

    public static String toMonthYearStringDate(Date date) {
        return new SimpleDateFormat(MONTH_YEAR_DATE_FORMAT, Locale.getDefault()).format(date);
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
