package com.waracle.androidtest.http;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.waracle.androidtest.http.HttpUtils.CONNECTION_TIMEOUT;
import static com.waracle.androidtest.http.HttpUtils.PROPERTY_FIELD;
import static com.waracle.androidtest.http.HttpUtils.PROPERTY_VALUE;

/**
 * Created by Jacek on 2018-03-19.
 */

public class LoadItemsTask extends AsyncTask<URL, Void, JSONArray> {

    public interface AsyncResponseListener {
        void processFinish(JSONArray output);
    }

    private AsyncResponseListener listener;

    public LoadItemsTask(AsyncResponseListener listener) {
        this.listener = listener;
    }

    @Override
    protected JSONArray doInBackground(URL... urls) {
        HttpURLConnection urlConnection = null;
        StringBuilder result = new StringBuilder();
        try {

            urlConnection = (HttpURLConnection) urls[0].openConnection();
            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
            urlConnection.setReadTimeout(CONNECTION_TIMEOUT);
            urlConnection.setInstanceFollowRedirects(true);
            urlConnection.setRequestProperty(PROPERTY_FIELD, PROPERTY_VALUE);
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            return new JSONArray(result.toString());

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(JSONArray jsonArray) {
       listener.processFinish(jsonArray);
    }
}
