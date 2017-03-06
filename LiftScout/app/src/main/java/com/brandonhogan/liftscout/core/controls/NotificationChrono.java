package com.brandonhogan.liftscout.core.controls;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.activities.MainActivity;

/**
 * Created by Brandon on 3/6/2017.
 * Description :
 */

public class NotificationChrono {

    private NotificationCompat.Builder builder;
    private int id;
    private NotificationManager notificationManager;


    public NotificationChrono(Context context, int id, boolean running,
                               String title,
                              NotificationManager notificationManager) {

        this.notificationManager = notificationManager;
        this.id = id;

//        Intent stopIntent = new Intent("com.brandonhogan.liftscout.STOP");
//        PendingIntent stopPendingIntent = PendingIntent.getBroadcast(context,
//                0, stopIntent, 0);
//
//
//        Intent startIntent = new Intent("com.brandonhogan.liftscout.STARTPAUSE");
//        PendingIntent startPendingIntent = PendingIntent.getBroadcast(context,
//                0, startIntent, 0);
//

        builder = new NotificationCompat.Builder(context)
                .setContentText(context.getString(R.string.app_name))
                .setContentTitle(title)
                .setSmallIcon(R.drawable.icon_grey_xxhdpi)
                .setAutoCancel(false)
                .setOngoing(running)
                .setOnlyAlertOnce(true)
                .setContentIntent(
                        PendingIntent.getActivity(context, 10,
                                new Intent(context, MainActivity.class)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP), 0));
//                .addAction(
//                        running ? R.drawable.ic_pause_white_48dp
//                                : R.drawable.ic_play_arrow_white_48dp,
//                        running ? context.getString(R.string.all) : context
//                                .getString(R.string.start), startPendingIntent)
//                .addAction(R.drawable.ic_stop_white_48dp,
//                        context.getString(R.string.stop), stopPendingIntent);
    }

    public void updateNotification(String text) {
        builder.setContentText(text);
        notificationManager.notify(id, builder.build());
    }

    public void clearNotification() {
        notificationManager.cancel(id);
    }
}
