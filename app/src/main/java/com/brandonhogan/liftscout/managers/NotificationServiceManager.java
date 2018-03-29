package com.brandonhogan.liftscout.managers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.activities.MainActivity;

/**
 * Created by Brandon on 3/6/2017.
 * Description :
 */

public class NotificationServiceManager {

    public static final String NOTIFICATION_ID = "notificationServiceManagerIdentifier";

    public static final String REST_TIMER_TRIGGER_NOTIFICATION = "RestTimerTriggerNotification";
    public static final String REST_TIMER_TRIGGER_EXERCISE_ID = "RestTimerTriggerExerciseId";
    public static final String REST_TIMER_TRIGGER_TIME = "RestTimerTriggerTime";
    public static final String REST_TIMER_TRIGGER_DATE = "RestTimerTriggerDate";




    private NotificationCompat.Builder builder;
    private Context context;
    private int id;
    private int exerciseId;
    private long date;
    private int time;
    private String CHANNEL_ID = "ls_channel_01";
    private CharSequence name = "notificationChannel";
    int importance = NotificationManager.IMPORTANCE_HIGH;
    private android.app.NotificationManager notificationManager;


    public void RestTimerNotification(Context context,
                                      int id,
                                      boolean running,
                                      String title,
                                      int exerciseId,
                                      long date,
                                      int time,
                                      android.app.NotificationManager notificationManager) {

        this.notificationManager = notificationManager;
        this.context = context;
        this.id = id;
        this.exerciseId = exerciseId;
        this.date = date;
        this.time = time;



//        if(builder == null) {
//            builder = new NotificationCompat.Builder(context);
//            builder.setAutoCancel(false);
//            builder.setOnlyAlertOnce(true);
//            builder.setVisibility(Notification.VISIBILITY_PRIVATE);
//            builder.setSmallIcon(R.drawable.ic_timer_white_24dp);
//            builder.setOngoing(running);
//            builder.setContentTitle(context.getString(R.string.app_name));
//        }

//        Intent stopIntent = new Intent("com.brandonhogan.liftscout.STOP");
//        PendingIntent stopPendingIntent = PendingIntent.getBroadcast(context,
//                0, stopIntent, 0);
//
//
//        Intent startIntent = new Intent("com.brandonhogan.liftscout.STARTPAUSE");
//        PendingIntent startPendingIntent = PendingIntent.getBroadcast(context,
//                0, startIntent, 0);
//
//
//        Intent intent = new Intent(context, SearchViewActions.class)
//                    .putExtra(REST_TIMER_TRIGGER_TIME, 555)
//                    .putExtra(NOTIFICATION_ID, REST_TIMER_TRIGGER_NOTIFICATION)
//                    .putExtra(REST_TIMER_TRIGGER_EXERCISE_ID, exerciseId)
//                    .putExtra(REST_TIMER_TRIGGER_DATE, 999)
//                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        builder = new NotificationCompat.Builder(context)
                .setContentText(context.getString(R.string.app_name))
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_timer_white_24dp)
                .setAutoCancel(false)
                .setOngoing(running)
                .setChannelId(CHANNEL_ID)
                .setOnlyAlertOnce(true);
//                .setContentIntent(
//                        PendingIntent.getActivity(context, 0,
//                                intent, 0));
                                        //.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP), 0));
//                .addAction(
//                        running ? R.drawable.ic_pause_white_48dp
//                                : R.drawable.ic_play_arrow_white_48dp,
//                        running ? context.getString(R.string.all) : context
//                                .getString(R.string.start), startPendingIntent)
//                .addAction(R.drawable.ic_stop_white_48dp,
//                        context.getString(R.string.stop), stopPendingIntent);
    }

    public void updateTimerText(String text, int timer) {
        builder.setContentText(text);
        this.time = timer;

        builder.setContentIntent(PendingIntent.getActivity(context, 0,
                new Intent(context, MainActivity.class)
                    .putExtra(NOTIFICATION_ID, REST_TIMER_TRIGGER_NOTIFICATION)
                    .putExtra(REST_TIMER_TRIGGER_TIME, time)
                    .putExtra(REST_TIMER_TRIGGER_EXERCISE_ID, exerciseId)
                    .putExtra(REST_TIMER_TRIGGER_DATE, date), PendingIntent.FLAG_UPDATE_CURRENT));


        //builder.mNotification.flags |= Notification.FLAG_NO_CLEAR;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        notificationManager.notify(id, builder.build());
    }

    public void clearNotification() {
        notificationManager.cancel(id);
    }
}
