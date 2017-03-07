package com.brandonhogan.liftscout.core.utils;

import android.os.Bundle;
import android.util.Log;

/**
 * Created by Brandon on 3/7/2017.
 * Description :
 */

public class LogUtil {

    public static void printIntents(Bundle bundle, String tag) {
        if (bundle != null) {
            for (String key : bundle.keySet()) {
                Object value = bundle.get(key);
                Log.d(tag, String.format("%s %s (%s)", key,
                        value.toString(), value.getClass().getName()));
            }
        }
    }
}
