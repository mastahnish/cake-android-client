package com.waracle.androidtest.cache;

import android.widget.ImageView;

/**
 * Created by Jacek on 2018-03-19.
 */

public class PhotoToLoad {
    public String url;
    public ImageView imageView;

    public PhotoToLoad(String u, ImageView i) {
        url = u;
        imageView = i;
    }
}
