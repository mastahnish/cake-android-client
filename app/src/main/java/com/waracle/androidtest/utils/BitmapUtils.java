package com.waracle.androidtest.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Jacek on 2018-03-19.
 */

public class BitmapUtils {

    public static Bitmap decodeFile(File f) {
        Bitmap ret = null;
        try {
            FileInputStream is = new FileInputStream(f);
            ret = BitmapFactory.decodeStream(is, null, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
    }

}
