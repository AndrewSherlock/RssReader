package com.andrew.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class RSSBroadcastReciever extends BroadcastReceiver {

    SharedPreferences sp;
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        // gets the broadcasts from the scheduler
        this.context = context;
        sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        String url = sp.getString("url", "");

        // processes for updated items to check if we need to notify the user
        NetworkInfoReciever networkInfoReciever = new NetworkInfoReciever(false, this);
        networkInfoReciever.execute(url);
    }

    public void checkIfNotify(long lastUpdatedNews)
    {
        long lastCheck = sp.getLong("last_check", 0);

        if(lastUpdatedNews > lastCheck) // if the last item has been updated later then we last checked, fire notification
        {
            Notificationhandler.createNotification(context);
        }

    }
}