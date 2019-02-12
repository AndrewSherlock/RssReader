package com.andrew.android;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class NetworkInfoReciever extends AsyncTask<String, Void, ArrayList>
{

    private ListView ls;
    private int list_item_id;

    private boolean shouldDisplay = true;
    private RSSBroadcastReciever rssBroadcastReciever;
    public long lastUpdatedNews;

    // used to get the items for the list
    public NetworkInfoReciever(ListView textView, int rss_list_item)
    {
        ls = textView;
        list_item_id = rss_list_item;
    }

    // used to get the items for notifications
    public NetworkInfoReciever(boolean shouldDisplay, RSSBroadcastReciever rssBroadcastReciever)
    {
        this.rssBroadcastReciever = rssBroadcastReciever;
        this.shouldDisplay = shouldDisplay;
    }

    @Override
    protected ArrayList doInBackground(String... strings)
    {
        ArrayList list = new ArrayList();
        URL linkToRss = getUrl(strings[0]); /// gets our RSS link
        boolean hasProcessedFirst = false; // used to update the first value to get the timestamp, if we have an greater value then the last
                                            // user check, send notification, else dont
        int numberOfItemsToFetch = 1; // used for the user defined number of objects

        if(ls != null) // no list availible, skip
            numberOfItemsToFetch = ls.getContext().getSharedPreferences("config",Context.MODE_PRIVATE).getInt("item_num", 20);

        if(numberOfItemsToFetch <= 0)
        {
            numberOfItemsToFetch = 1;
        }


        try{
            HttpURLConnection connection = (HttpURLConnection) linkToRss.openConnection();
            connection.setRequestMethod("GET");
            connection.connect(); // connect to the RSS feed

            // gets the RSS into xml format so we can begin processing
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document xmlFile = documentBuilder.parse(connection.getInputStream());

            Element root = xmlFile.getDocumentElement(); // grab the first element

            NodeList childNodes = root.getChildNodes(); // get child nodes
            childNodes = childNodes.item(1).getChildNodes(); // get the list from that we are interested in

            for(int i = 0; i < childNodes.getLength(); i++)
            {
                // RSS feeds have an author tag which we need to skip, make sure we are getting an item
                if(childNodes.item(i).getNodeName().equals("item"))
                {
                    Node n = childNodes.item(i); // get the node
                    NodeList values = n.getChildNodes(); // grab the nodes children

                    String title = "";
                    String description = "";
                    String url = "";
                    String imageUrl = "";
                    numberOfItemsToFetch--;

                    // process each of the fields in the node
                    for(int x = 0; x < values.getLength(); x++)
                    {
                        if(values.item(x).getNodeName().equals("pubDate") && !hasProcessedFirst)
                        {
                            // we have the time stamp to process
                            long timeStamp = parseDateToTimeStamp(values.item(x).getTextContent());
                            lastUpdatedNews = timeStamp;
                            hasProcessedFirst = true;
                        }

                        // gets the values of the node
                        if(values.item(x).getNodeName().equals("title"))
                            title = values.item(x).getTextContent();
                        if(values.item(x).getNodeName().equals("description"))
                            description = values.item(x).getTextContent();
                        if(values.item(x).getNodeName().equals("link"))
                            url = values.item(x).getTextContent();

                        // used to get the image link
                        if(values.item(x).getNodeName().equals("enclosure"))
                        {
                            NamedNodeMap nm = values.item(x).getAttributes();

                            for( int c = 0; c < nm.getLength(); c++)
                            {

                                if(nm.item(c).getTextContent().substring(0, 4).equals("http"))
                                    imageUrl = nm.item(c).getTextContent();
                            }
                        }

                    }

                    // add them to an RSS Object class
                    RssObject rss = new RssObject(title, description, imageUrl, url);
                    list.add(rss); // add to the list

                    // got enough for the list, return them
                    if(numberOfItemsToFetch == 0 && ls != null)
                        return  list;

                }
            }
        } catch(Exception e)
        {
            System.out.println(e.toString());
        }

        return list;
    }

    private long parseDateToTimeStamp(String date)
    {
        long timeStamp = 0;

        // begin stripping the stuff we dont want from the timestamp
        int firstSpace = date.indexOf(" ");
        String dateWithNoDay = date.substring(firstSpace + 1, date.length());

        int plusSign = dateWithNoDay.indexOf("+");
        String dateWithNoPlus = dateWithNoDay.substring(0, plusSign - 1);

        // parse string to date
        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM yyyy HH:mm:ss");

        // get the time stamp
        try{
            Date d = dateFormat.parse(dateWithNoPlus);
            timeStamp = d.getTime();
        } catch(ParseException e)
        {
            System.out.println("Failed parsing string");
        }

        return timeStamp;

    }

    // parse the url string to a url
    private URL getUrl(String link)
    {
        try {
            URL linkToRss = new URL(link);
            return linkToRss;
        } catch (MalformedURLException e)
        {}

        return null;
    }

    @Override
    protected void onPostExecute(ArrayList items) {

        // we dont want this to be in a list, called the notify method and bail
        if(!shouldDisplay)
        {
            rssBroadcastReciever.checkIfNotify(lastUpdatedNews);
            return;
        }

        // else add to the list
        super.onPostExecute(items);
        ArrayAdapter<RssObject> feedAdapter = new RssFeedAdapter(ls.getContext(), list_item_id, items);
        ls.setAdapter(feedAdapter);
    }
}
