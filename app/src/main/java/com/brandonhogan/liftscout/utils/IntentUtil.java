package com.brandonhogan.liftscout.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.brandonhogan.liftscout.R;

/**
 * Created by Brandon on 3/9/2017.
 * Description :
 */

public class IntentUtil {

    public static void sendEmailIntent(Activity activity, String subject) {

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto",activity.getString(R.string.app_feedback), null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        activity.startActivity(Intent.createChooser(emailIntent, "Send feedback email from..."));
    }

    public static void sendURLIntent(Activity activity, String url) {

        if (url == null ||url.isEmpty())
            throw new NullPointerException("URL cannot be null or empty");

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        activity.startActivity(i);
    }
}
