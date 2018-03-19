package com.waracle.androidtest.http;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.LruCache;
import android.view.View;
import android.widget.ImageView;

import com.waracle.androidtest.cache.FileCache;
import com.waracle.androidtest.cache.PhotoToLoad;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by Riad on 20/05/2015.
 */
public class ImageLoader {

    private static final String TAG = ImageLoader.class.getSimpleName();

    public ImageLoader() { /**/ }

    private LruCache memoryCache;
    private FileCache fileCache;
    private Map imageViews = Collections.synchronizedMap(new WeakHashMap());
    private Context context;

    private Drawable mStubDrawable;

    public ImageLoader(Context ctx) {
        this.context = ctx;
        fileCache = new FileCache(context);
        init(context);
    }

    private void init(Context context) {
        final int memClass = ((ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE)).getMemoryClass();
        // 1/8 of the available mem
        final int cacheSize = 1024 * 1024 * memClass / 8;
        memoryCache = new LruCache(cacheSize);

        mStubDrawable = context.getResources().getDrawable(android.R.drawable.picture_frame);

    }

    /**
     * Simple function for loading a bitmap image from the web
     *
     * @param url       image url
     * @param imageView view to set image too.
     */
    public void load(String url, ImageView imageView) {

        imageViews.put(imageView, url);

        if (TextUtils.isEmpty(url)) {
            throw new InvalidParameterException("URL is empty!");
        }


        Bitmap bitmap = null;
        if (url != null && url.length() > 0)
            bitmap = (Bitmap) memoryCache.get(url);
        if (bitmap != null) {
            //the image is in the LRU Cache, we can use it directly
            imageView.setImageBitmap(bitmap);
        } else {
            //the image is not in the LRU Cache
            //set a default drawable a search the image
            imageView.setImageDrawable(mStubDrawable);
            if (url != null && url.length() > 0)
                loadImageData(imageView, url);
        }
    }


    private void loadImageData(ImageView imageView, String url) {
        new LoadBitmapTask().execute(url, imageView);
    }

    private Bitmap getBitmap(final String url) {
        final Bitmap[] ret = {null};
        //from SD cache
        File f = fileCache.getFile(url);
        if (f.exists()) {
            ret[0] = decodeFile(f);
            if (ret[0] != null)
                return ret[0];
        }

        //from web
        //your requester will fetch the bitmap from the web and store it in the phone using the fileCache
        new LoadImageTask(new LoadImageTask.AsyncResponseListener() {
            @Override
            public void processFinish(Bitmap output) {
                ret[0] = output;
                if(output!=null){
                    fileCache.createAFile(output, url);
                }
            }
        }).execute(url);

        return ret[0];

    }


    private Bitmap decodeFile(File f) {
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

    private boolean imageViewReused(PhotoToLoad photoToLoad) {
        //tag used here
        String tag = (String) imageViews.get(photoToLoad.imageView);
        if (tag == null || !tag.equals(photoToLoad.url))
            return true;
        return false;
    }

    class LoadBitmapTask extends AsyncTask {
        private PhotoToLoad mPhoto;

        @Override
        protected TransitionDrawable doInBackground(Object... params) {
            mPhoto = new PhotoToLoad((String) params[0], (ImageView) params[1]);

            if (imageViewReused(mPhoto))
                return null;
            Bitmap bmp = getBitmap(mPhoto.url);
            if (bmp == null) return null;

            memoryCache.put(mPhoto.url, bmp);

            // TransitionDrawable let you to make a crossfade animation between 2 drawables
            // It increase the sensation of smoothness
            TransitionDrawable td = null;
            if (bmp != null) {
                Drawable[] drawables = new Drawable[2];
                drawables[0] = mStubDrawable;
                drawables[1] = new BitmapDrawable(context.getResources(), bmp);
                td = new TransitionDrawable(drawables);
                td.setCrossFadeEnabled(true); //important if you have transparent bitmaps
            }

            return td;
        }


        @Override
        protected void onPostExecute(Object drawable) {
            if (imageViewReused(mPhoto)) {
                //imageview reused, just return
                return;
            }

            TransitionDrawable td = (TransitionDrawable) drawable;

            if (drawable != null) {
                // bitmap found, display it !
                mPhoto.imageView.setImageBitmap((Bitmap) memoryCache.get(mPhoto.url));
                mPhoto.imageView.setVisibility(View.VISIBLE);

                //a little crossfade
                td.startTransition(200);
            } else {
                //bitmap not found, display the default drawable
                mPhoto.imageView.setImageDrawable(mStubDrawable);
            }
        }


    }

}
