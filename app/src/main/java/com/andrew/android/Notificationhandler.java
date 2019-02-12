package com.andrew.android;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

/**
 * Created by andrew on 18/11/2018.
 */

public class Notificationhandler{

    public static void createNotification(Context context)
    {
        
        // used to bring user to the main activity
        PendingIntent intent = PendingIntent.getActivity(context, 0, new Intent(context,MainActivity.class), 0);

        // creates the notification
        Notification n = new Notification.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("New Items available")
                .setContentText("Your subscribed news feed has new items")
                .setContentIntent(intent).build();

        // when the user presses, remove the notification
        n.flags |= Notification.FLAG_AUTO_CANCEL;

        // notify the user
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, n);


    }
}
