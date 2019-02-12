package com.andrew.android;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

public class RssUpdateScheduler extends Service
{
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        // begin the service of the alarmanger calling the broadcast handler in which checks the notifications
        setAlarm();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void setAlarm()
    {
        // alarm manager calls the broadcast reciever every 5 minutes to check if we need to update the user of new items
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent reciever = new Intent(this, RSSBroadcastReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 344324545, reciever, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), (60000), pendingIntent); // called every 5 mins
    }
}


