package com.brandonhogan.liftscout.utils.constants;

import java.util.ArrayList;

/**
 * Created by Brandon on 3/9/2017.
 * Description :
 */

public class ConstantValues {

    public static final ArrayList<Double> increments = new ArrayList<Double>() {{
        add(0.5);
        add(1.0);
        add(2.0);
        add(2.5);
        add(5.0);
        add(10.0);
    }};

    public static final ArrayList<String> increments_string = new ArrayList<String>() {{
        add("0.5");
        add("1.0");
        add("2.0");
        add("2.5");
        add("5.0");
        add("10.0");
    }};

    public static final double INCREMENT_KG_DEFAULT = 2.5;
    public static final double INCREMENT_LB_DEFAULT = 5.0;
    public static final int REST_TIME_DEFAULT = 60;

}
