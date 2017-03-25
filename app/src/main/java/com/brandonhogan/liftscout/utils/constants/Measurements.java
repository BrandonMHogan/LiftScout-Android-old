package com.brandonhogan.liftscout.utils.constants;

/**
 * Created by Brandon on 2/17/2017.
 * Description :
 */

public class Measurements {
    public static final String KILOGRAMS = "Kilograms";
    public static final String POUNDS = "Pounds";

    public static final String KGS = "kgs";
    public static final String LBS = "lbs";

    public static final String KG = "kg";
    public static final String LB = "lb";


    public static String getCompressedType(String measurement, boolean plural) {
        if (measurement.equals(Measurements.KILOGRAMS))
            return plural ? Measurements.KGS : Measurements.KG;
        else
            return plural ? Measurements.LBS : Measurements.LB;
    }
}
