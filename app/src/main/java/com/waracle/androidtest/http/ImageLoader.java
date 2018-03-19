package com.waracle.androidtest.http;

import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.security.InvalidParameterException;

/**
 * Created by Riad on 20/05/2015.
 */
public class ImageLoader {

    private static final String TAG = ImageLoader.class.getSimpleName();

    public ImageLoader() { /**/ }



    /**
     * Simple function for loading a bitmap image from the web
     *
     * @param url       image url
     * @param imageView view to set image too.
     */
    public void load(String url, ImageView imageView) {
        if (TextUtils.isEmpty(url)) {
            throw new InvalidParameterException("URL is empty!");
        }

        // Can you think of a way to improve loading of bitmaps
        // that have already been loaded previously??

        try {
            loadImageData(imageView, url);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private static void loadImageData(final ImageView imageView, String url) throws IOException {
        new LoadImageTask(imageView).execute(url);
    }




}
