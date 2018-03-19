package com.waracle.androidtest.http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Jacek on 2018-03-19.
 */

public class LoadImageTask extends AsyncTask<String, Void, Bitmap> {

    private static final String TAG = LoadImageTask.class.getSimpleName();

    public interface AsyncResponseListener {
        void processFinish(Bitmap output);
    }


    private AsyncResponseListener listener;
    private WeakReference<ImageView> viewWeakReference = null;

    public LoadImageTask(AsyncResponseListener listener) {
        this.listener = listener;
        this.viewWeakReference = null;
    }

    public LoadImageTask(ImageView imageView) {
        this.viewWeakReference = new WeakReference<>(imageView);
    }


    @Override
    protected Bitmap doInBackground(String... strings) {
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        Bitmap resultBitmap = null;

        try {
            connection = (HttpURLConnection) new URL(strings[0]).openConnection();
            connection.connect();
            try {
                // Read data from workstation
                inputStream = connection.getInputStream();
                resultBitmap = BitmapFactory.decodeStream(inputStream);
                return resultBitmap;
            } catch (IOException e) {
                // Read the error from the workstation
                inputStream = connection.getErrorStream();
                return resultBitmap;
            }


        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
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

        if (viewWeakReference != null) {
            ImageView imageView = viewWeakReference.get();

            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}
