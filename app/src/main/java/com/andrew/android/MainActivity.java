package com.andrew.android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private RssFeedManager rssFeedManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService(new Intent(this, RssUpdateScheduler.class));
        setContentView(R.layout.activity_main);
        ListView ls = findViewById(R.id.rss_feed);

        rssFeedManager = new RssFeedManager(ls); // gets our feed

        // if we want to config the settings
        final Button configButton = findViewById(R.id.config_button);
        configButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(configButton.getContext(), Configuration.class);
                intent.putExtra("editor", true);
                startActivity(intent);
            }
        });

        setLastCheck();

    }

    private void setLastCheck()
    {
        // used to check against the last time you looked at the feed for notifications
        SharedPreferences.Editor editor = getSharedPreferences("config", Context.MODE_PRIVATE).edit();
        editor.putLong("last_check", System.currentTimeMillis()); // adds the timestamp of the last time to shared preferences
        editor.commit();
    }


}
