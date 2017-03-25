package com.brandonhogan.liftscout.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.TypedValue;

import java.lang.reflect.Method;

/**
 * Created by Brandon on 3/4/2017.
 * Description :
 */

public class AttrUtil {

    public static int getAttributeRes(Resources.Theme theme, int attr) {

        TypedValue typedValue = new TypedValue();
        theme.resolveAttribute(attr, typedValue, true);
        return typedValue.data;

    }

    public static int getStyleAttributeRes(Activity activity, Resources.Theme theme, int attr) {

        int themeId = getThemeId(activity);

        TypedArray a = theme.obtainStyledAttributes(themeId, new int[] {attr});
        int attributeResourceId = a.getResourceId(0, 0);
        a.recycle();

        return attributeResourceId;
    }

    private static int getThemeId(Activity activity) {
        try {
            Class<?> wrapper = Context.class;
            Method method = wrapper.getMethod("getThemeResId");
            method.setAccessible(true);
            return (Integer) method.invoke(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
