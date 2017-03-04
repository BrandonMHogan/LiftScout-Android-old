package com.brandonhogan.liftscout.core.utils;

import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by Brandon on 3/4/2017.
 * Description :
 */

public class ColorUtil {

    public static int getAttributeColor(Resources.Theme theme, int attr) {

        TypedValue typedValue = new TypedValue();
        theme.resolveAttribute(attr, typedValue, true);
        return typedValue.data;
    }
}
