package com.example.android.newsapp;

/**
 * Created by sgomezp on 28/06/2017.
 */

public class News {

    private String mHeadline;
    private String mSection;
    private String mDate;
    private String mUrl;

    public News(String headline, String section, String date, String url) {
        mHeadline = headline;
        mSection = section;
        mDate = date;
        mUrl = url;
    }

    // Getters and Setters

    public String getHeadline() {
        return mHeadline;
    }

    public void setHeadline(String headline) {
        mHeadline = headline;

    }

    public String getSection() {
        return mSection;
    }


    public void setSection(String section) {
        mSection = section;
    }


    public String getDate() {
        return mDate;
    }


    public void setDate(String date) {
        mDate = date;
    }


    public String getUrl() {
        return mUrl;
    }


    public void setUrl(String url) {
        mUrl = url;
    }


    @Override
    public String toString() {
        return "News{" +
                "headline='" + mHeadline + '\'' +
                ", url='" + mUrl + '\'' +
                ", date='" + mDate + '\'' +
                ", section='" + mSection + '\'' +
                '}';
    }

}
