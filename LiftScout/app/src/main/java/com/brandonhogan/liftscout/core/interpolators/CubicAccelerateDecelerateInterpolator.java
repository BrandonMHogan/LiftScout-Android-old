package com.brandonhogan.liftscout.core.interpolators;

import android.view.animation.Interpolator;

/**
 * Created by Brandon on 2/14/2017.
 * Description :
 */

public class CubicAccelerateDecelerateInterpolator implements Interpolator
{
    @Override
    public float getInterpolation(float t)
    {
        return t * t;
    }
}
