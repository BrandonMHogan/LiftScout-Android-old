package com.brandonhogan.liftscout.core.interpolators;

import android.view.animation.Interpolator;

/**
 * Created by Brandon on 2/14/2017.
 * Description :
 */

public class HesitateInterpolator implements Interpolator {
    public HesitateInterpolator() {}
    public float getInterpolation(float t) {
        float x=2.0f*t-1.0f;
        return 0.5f*(x*x*x + 1.0f);
    }
}