package com.brandonhogan.liftscout.utils.constants;

import java.util.ArrayList;

public class TodayTransforms {

    public static final String OVERSHOOT = "Overshoot";
    public static final String LINEAR = "Linear";
    public static final String FAST_OUT_LINEAR_IN = "Fast Out Linear In";
    public static final String BOUNCE = "Bounce";
    public static final String ACCELERATE_DECELERATE = "Accelerate Decelerate";
    public static final String DEFAULT = "Default";

    public static final ArrayList<String> TRANSFORMS = new ArrayList<String>() {{
        add(DEFAULT);
        add(OVERSHOOT);
        add(LINEAR);
        add(FAST_OUT_LINEAR_IN);
        add(BOUNCE);
        add(ACCELERATE_DECELERATE);
    }};
}
