package com.brandonhogan.liftscout.core.aaadev;

import android.app.Activity;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.widget.Toast;

/**
 * Created by Brandon on 2/24/2017.
 * Description :
 */

public class ScreenDensityChecker {

    public static void displayDensity(Activity activity) {
        //Determine density
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int density = metrics.densityDpi;

        if (density == DisplayMetrics.DENSITY_HIGH) {
            Toast.makeText(activity, "DENSITY_HIGH... Density is " + String.valueOf(density), Toast.LENGTH_LONG).show();
        }
        else if (density == DisplayMetrics.DENSITY_MEDIUM) {
            Toast.makeText(activity, "DENSITY_MEDIUM... Density is " + String.valueOf(density), Toast.LENGTH_LONG).show();
        }
        else if (density == DisplayMetrics.DENSITY_LOW) {
            Toast.makeText(activity, "DENSITY_LOW... Density is " + String.valueOf(density), Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(activity, "Density is neither HIGH, MEDIUM OR LOW.  Density is " + String.valueOf(density), Toast.LENGTH_LONG).show();
        }
    }

    public static void displayScreenSize(Activity activity) {
        //Determine screen size
        if ((activity.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
            Toast.makeText(activity, "Large screen", Toast.LENGTH_LONG).show();
        }
        else if ((activity.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            Toast.makeText(activity, "Normal sized screen", Toast.LENGTH_LONG).show();
        }
        else if ((activity.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
            Toast.makeText(activity, "Small sized screen", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(activity, "Screen size is neither large, normal or small", Toast.LENGTH_LONG).show();
        }
    }
}
