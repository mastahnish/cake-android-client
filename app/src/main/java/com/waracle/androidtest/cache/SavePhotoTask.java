package com.waracle.androidtest.cache;

import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Jacek on 2018-03-19.
 */

class SavePhotoTask extends AsyncTask<Object, String, String> {

    private static final String TAG = SavePhotoTask.class.getSimpleName();

    @Override
    protected String doInBackground(Object... params) {
        File photo=
                new File((File) params[0],
                        String.valueOf(params[2].hashCode()));

        if (photo.exists()) {
            photo.delete();
        }

        try {
            FileOutputStream fos=new FileOutputStream(photo.getPath());

            fos.write((byte[]) params[1]);
            fos.close();
        }
        catch (java.io.IOException e) {
            Log.e(TAG, "Exception in photoCallback", e);
        }

        return(null);
    }
}