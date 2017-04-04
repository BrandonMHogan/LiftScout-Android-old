package com.brandonhogan.liftscout.utils;


import android.util.Log;

import com.brandonhogan.liftscout.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.brandonhogan.liftscout.utils.Constants.MONTH_YEAR_DATE_FORMAT;
import static com.brandonhogan.liftscout.utils.Constants.SIMPLE_DATE_FORMAT;

public class DateUtil {

    private static final String detailedDateFormat = "MMMM d, yyyy";
    private static final String dayOfWeekDateFormat = "EEEE";

    public static int toRelativeDateRes(Date date) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(DateUtil.trimTimeFromDate(new Date()));

        if (date.equals(cal.getTime())) {
            return R.string.today;
        }

        cal.add(Calendar.DATE, -1);
        if (date.equals(cal.getTime())) {
            return R.string.yesterday;
        }

        cal.add(Calendar.DATE, 2);
        if (date.equals(cal.getTime())) {
            return R.string.tomorrow;
        }

        else return 0;
    }

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
            Log.e("DateUtil", ex.getMessage());
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
