package com.example.android.newsapp;

/**
 * Created by sgomezp on 28/06/2017.
 */

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.android.newsapp.MainActivity.LOG_TAG;


/**
 * Helper methods related to requesting and receiving News data from The Guardian.
 */

public class QueryUtils {

    private static final int READ_TIME_OUT = 10000;
    private static final int CONNECT_TIME_OUT = 15000;

    // Constructor
    private QueryUtils() {
    }

    /**
     * Query the The Guardian dataset and return a list of {@link News} objects.
     */
    public static List<News> fetchNewsData(String requestUrl) {
        // Create URL object

        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.i(LOG_TAG, "en fecthNewsData. En el catch: " + e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link News}s
        List<News> news = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link News}s
        return news;
    }

    /**
     * Returns new URL object from given string URL
     */

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);

        } catch (MalformedURLException e) {
            Log.i(LOG_TAG, "Error creating URL: " + e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = null;

        // If the URL is null, then return early

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(READ_TIME_OUT /* 10000 milliseconds)*/);
            urlConnection.setConnectTimeout(CONNECT_TIME_OUT /* 15000 milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }

        }
        return jsonResponse;

    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link News} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<News> extractFeatureFromJson(String newsJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding news to
        List<News> news = new ArrayList<News>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(newsJSON);

            // if there are no books  in the query return early
            if (baseJsonResponse.getJSONObject("response").getInt("total") == 0) {
                return null;
            }

            // Extract the JSONArray associated with the key called "results",
            // which represents a list of news.
            JSONArray newsArray = baseJsonResponse.getJSONObject("response").getJSONArray("results");

            // For each news in the newsArray, create an {@link News} object
            for (int i = 0; i < newsArray.length(); i++) {

                // Get a single news at position i within the list of news
                JSONObject currentNews = newsArray.getJSONObject(i);

                // Extract the value for the key called "webTitle"
                String headline = currentNews.getString("webTitle");

                // Extract the value for the key called "sectionName"
                String section = currentNews.getString("sectionName");

                // Extract the value for the key called "webPublicationDate"
                String date = currentNews.getString("webPublicationDate");
                date = formatDateTime(date);

                // Extract the value for the key called "webUrl"
                String url = currentNews.getString("webUrl");

                // Create a new {@link News} object with the headline, section, date,
                // and url from the JSON response.
                News newsItem = new News(headline, section, date, url);

                // Add the new {@link News} to the list of news.
                news.add(newsItem);
            }


        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the news JSON results", e);
        }

        // Return the list of news
        return news;
    }

    private static String formatDateTime(String rawDate) {
        String datePattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        SimpleDateFormat formatDate = new SimpleDateFormat(datePattern, Locale.US);
        try {
            Date pJsonDate = formatDate.parse(rawDate);
            String patternDateFormat = "MMMM dd yyyy";
            SimpleDateFormat dateFormatted = new SimpleDateFormat(patternDateFormat, Locale.US);
            return dateFormatted.format(pJsonDate);
        } catch (ParseException e) {
            Log.i(LOG_TAG, "Error Parsing JSON date: " + e);
            return "";

        }


    }


}
