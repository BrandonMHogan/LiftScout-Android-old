package com.brandonhogan.liftscout.utils.constants;

import java.util.ArrayList;

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

    public static final ArrayList<String> MEASUREMENTS = new ArrayList<String>() {{
        add(POUNDS);
        add(KILOGRAMS);
    }};

    public static final ArrayList<String> MEASUREMENTS_COMPRESSED = new ArrayList<String>() {{
        add(LBS);
        add(KGS);
    }};

    public static final ArrayList<String> MEASUREMENTS_COMPRESSED_SINGLE = new ArrayList<String>() {{
        add(LB);
        add(KG);
    }};
}
