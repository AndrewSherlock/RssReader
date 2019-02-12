package com.andrew.android;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.ListView;

public class RssFeedManager extends Service
{
    String feed; // this will be the link of objects to add
    ListView ls; // this where we are going to add the objects to

    public RssFeedManager(ListView ls)
    {
        this.ls = ls;
        SharedPreferences sp = ls.getContext().getSharedPreferences("config", MODE_PRIVATE);
        if(sp == null)
            feed = "";
        else
            feed = sp.getString("url", "");

        getNewsFromFeed(feed, ls);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void getNewsFromFeed(String rssFeed, ListView ls) // the netwrk info reciever is called to do the networking of the rss reader in a seperate thread
    {
        // we pass in the list and the resorce id of our custom list item
       NetworkInfoReciever networkInfoReciever = new NetworkInfoReciever(ls,R.id.rss_list_item);
       networkInfoReciever.execute(rssFeed); // excecute the asynctask
    }
}
