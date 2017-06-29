package com.example.android.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by sgomezp on 28/06/2017.
 */

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    // TAG for messages
    private static final String LOG_TAG = NewsLoader.class.getName();

    // Query URL

    private String mUrl;

    /**
     * Construct a new {@link NewsLoader}.
     *
     * @param context of the activity
     * @param url     to load data from
     */

    public NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {

        forceLoad();
    }

    /**
     * This is on a background thread.
     */

    @Override
    public List<News> loadInBackground() {

        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of news.
        return QueryUtils.fetchNewsData(mUrl);
    }

}

