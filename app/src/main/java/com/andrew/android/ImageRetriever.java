package com.andrew.android;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

public class ImageRetriever extends AsyncTask<String, Void, Void>
{

    ImageView imageView;
    Drawable image;

    public ImageRetriever(ImageView imageView)
    {
        this.imageView = imageView;
    }

    @Override
    protected Void doInBackground(String... strings)
    {
        image = GetImageFromNetwork(strings[0]);
        return null;
    }

    private Drawable GetImageFromNetwork(String string)
    {
        try{
            InputStream is = (InputStream) new URL(string).getContent();
            Drawable imageDrawable = Drawable.createFromStream(is, "src name");
            return imageDrawable;
        } catch (Exception e) {
            System.out.println("Exc=" + e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        imageView.setImageDrawable(image);

    }
}
