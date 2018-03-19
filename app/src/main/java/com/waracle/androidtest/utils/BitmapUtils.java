package com.waracle.androidtest.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Jacek on 2018-03-19.
 */

public class BitmapUtils {

    public static Bitmap convertToBitmap(byte[] data) {
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }


}
