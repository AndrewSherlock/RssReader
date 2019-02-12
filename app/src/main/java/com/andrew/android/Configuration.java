package com.andrew.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;

public class Configuration extends AppCompatActivity {

    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        // get our shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        String url = sharedPreferences.getString("url", ""); // retrieve the link of the RSS feed

        boolean wantsToEdit = getIntent().getBooleanExtra("editor", false); // if we entered the config mode from the feed to change settings

        if(isValidURL(url) && !wantsToEdit) // if we dont want to edit and we have a url already, go straight to the main activity
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        enterValuesToField(); // populate the fields we have already entered

        saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDetails();
            }
        });
    }

    private void enterValuesToField()
    {
        // gets the values from our shared preferences and adds them to the fields
        SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
        EditText urlField = findViewById(R.id.feed_url);
        urlField.setText(sp.getString("url", ""));

        EditText itemsField = findViewById(R.id.number_items);
        itemsField.setText(String.valueOf(sp.getInt("item_num", 20)));
    }

    private void saveDetails()
    {
        EditText urlTextView = findViewById(R.id.feed_url); // grab the url field to get the value
        String url = urlTextView.getText().toString();
        int itemAmount = 20; // default the number of items

        TextView numOfItems = findViewById(R.id.number_items);

        if(numOfItems.getText().length() > 0) // make sure we have a value before trying to parse
            itemAmount = Integer.parseInt(numOfItems.getText().toString());

        if(!isValidURL(url))
        { // not a valid url, dont continue
            Toast.makeText(this, "Not a valid url.", Toast.LENGTH_LONG).show();
            return;
        }

        // here we save the values to shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("url", url);
        editor.putInt("item_num", itemAmount);
        editor.apply();

        // change to the main activity
        Intent rss_feed = new Intent(this, MainActivity.class);
        startActivity(rss_feed);
        finish();

    }

    private  boolean isValidURL(String url)
    {
        try {
            URL linkToRss = new URL(url); // try to parse, if fails return false
        } catch (MalformedURLException e)
        {
            return false;
        }

        // else return true
        return true;
    }
}
