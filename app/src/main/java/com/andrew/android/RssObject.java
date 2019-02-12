package com.andrew.android;

/**
 * Created by andrew on 12/11/2018.
 */

public class RssObject
{
    private String headLine;
    private String desc;
    private String imageURL;
    private String itemURL;

    public RssObject(String headLine, String desc, String imageURL, String itemURL) {
        this.headLine = headLine;
        this.desc = desc;
        this.imageURL = imageURL;
        this.itemURL = itemURL;
    }

    public String getHeadline() {
        return headLine;
    }

    public String getDesc() {
        return desc;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getItemURL() {
        return itemURL;
    }
}
