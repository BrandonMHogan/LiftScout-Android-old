package com.brandonhogan.liftscout.core.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import io.realm.Realm;

public class DatabaseOutput {

    private static final String TAG = "DatabaseOutput";

    public static void SendRealmToPhone(Activity activity) {

        Log.d(TAG, "Print Realm DB Start...");

        // init realm
        Realm realm = Realm.getDefaultInstance();

        File exportRealmFile = null;
        try {
            // get or create an "export.realm" file
            exportRealmFile = new File(activity.getExternalCacheDir(), "export.realm");

            // if "export.realm" already exists, delete
            exportRealmFile.delete();

            // copy current realm to "export.realm"
            realm.writeCopyTo(exportRealmFile);

        } catch (IOException e) {
            Log.d(TAG, "Print Realm DB Failed. :" + e.getMessage());
            e.printStackTrace();
        }
        realm.close();

      //   init email intent and add export.realm as attachment
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, "bhogan@lwolf.com");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Android Log : Realm Directory");
        intent.putExtra(Intent.EXTRA_TEXT, "realm db directory location at call request.");
        Uri u = Uri.fromFile(exportRealmFile);
        intent.putExtra(Intent.EXTRA_STREAM, u);

      //   start email intent
        activity.startActivity(Intent.createChooser(intent, "Realm DB Log Directory"));


        Log.d(TAG, exportRealmFile.toString());
        Log.d(TAG, "Print Realm DB Finished;");
    }
}
