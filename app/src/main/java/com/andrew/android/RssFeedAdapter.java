package com.andrew.android;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class RssFeedAdapter extends ArrayAdapter {


    private ArrayList<RssObject> rssObjects;
    Context context;

    // custom list adapter for the rss objects
    public RssFeedAdapter(@NonNull Context context, int resource, ArrayList rssObjects) {
        super(context, resource, rssObjects);
        this.context = context;
        this.rssObjects = rssObjects;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View adapterView = inflater.inflate(R.layout.rss_list_item, parent, false); // inflate the list item view

        // get the rss object at the position
        final RssObject item = rssObjects.get(position);

        // add the headline of the object
        TextView headLine = adapterView.findViewById(R.id.headline);
        headLine.setText(item.getHeadline());

        // add the description
        TextView description = adapterView.findViewById(R.id.description);
        description.setText(item.getDesc());

        //asynctask to grab image from url
        ImageRetriever imageRetriever = new ImageRetriever((ImageView) adapterView.findViewById(R.id.item_image));
        imageRetriever.execute(item.getImageURL());

        // if we push the item, go to web page
        adapterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebPageView.class);
                intent.putExtra("url", item.getItemURL());
                context.startActivity(intent);
            }
        });

        return adapterView;
    }

}
