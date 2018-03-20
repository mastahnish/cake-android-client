package com.waracle.androidtest.http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.waracle.androidtest.http.HttpUtils.CONNECTION_TIMEOUT;

/**
 * Created by Jacek on 2018-03-19.
 */

public class LoadImageTask extends AsyncTask<String, Void, Bitmap> {

    private static final String TAG = LoadImageTask.class.getSimpleName();

    public interface AsyncResponseListener {
        void processFinish(Bitmap output);
    }


    private AsyncResponseListener listener;

    public LoadImageTask(AsyncResponseListener listener) {
        this.listener = listener;
    }


    @Override
    protected Bitmap doInBackground(String... params) {
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        Bitmap resultBitmap = null;

        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.setReadTimeout(CONNECTION_TIMEOUT);
            connection.setInstanceFollowRedirects(true);
            connection.connect();
            try {
                inputStream = connection.getInputStream();
                resultBitmap = BitmapFactory.decodeStream(inputStream);

                return resultBitmap;
            } catch (IOException e) {
                Log.d(TAG, "IOException error: " + e.getMessage());
                return resultBitmap;
            }

        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "IOException error: " + e.getMessage());
        } finally {
            // Close the input stream if it exists.
            StreamUtils.close(inputStream);

            // Disconnect the connection
            if (connection != null) {
                connection.disconnect();
            }
        }
        return resultBitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        listener.processFinish(bitmap);
    }
}
