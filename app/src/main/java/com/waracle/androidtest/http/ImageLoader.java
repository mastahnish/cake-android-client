package com.waracle.androidtest.http;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.LruCache;
import android.widget.ImageView;

import com.waracle.androidtest.cache.FileCache;
import com.waracle.androidtest.cache.PhotoToLoad;
import com.waracle.androidtest.utils.BitmapUtils;

import java.io.File;
import java.security.InvalidParameterException;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by Riad on 20/05/2015.
 */
public class ImageLoader {

    private static final String TAG = ImageLoader.class.getSimpleName();

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

        mStubDrawable = context.getResources().getDrawable(android.R.drawable.gallery_thumb);

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
        if (url != null && url.length() > 0){
            bitmap = (Bitmap) memoryCache.get(url);
        }

        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageDrawable(mStubDrawable);

            if (url != null && url.length() > 0){
                loadImageData(imageView, url);
            }
        }
    }


    private void loadImageData(ImageView imageView, String url) {
        new LoadBitmapTask().execute(url, imageView);
    }

    private Bitmap getBitmap(final PhotoToLoad mPhoto) {
        final Bitmap[] ret = {null};
        //from SD cache
        File f = fileCache.getFile(mPhoto.url);
        if (f.exists()) {
            ret[0] = BitmapUtils.decodeFile(f);
            if (ret[0] != null)
                return ret[0];
        }

        new LoadImageTask(new LoadImageTask.AsyncResponseListener() {
            @Override
            public void processFinish(Bitmap output) {
                ret[0] = output;
                if (output != null) {
                    decorateImageView(mPhoto.imageView, output);
                    fileCache.createAFile(output, mPhoto.url);
                }
            }
        }).execute(mPhoto.url);

        return ret[0];

    }

    private void decorateImageView(final ImageView iv, final Bitmap bitmap){
        if(iv!=null && bitmap!=null){
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    iv.setImageBitmap(bitmap);
                }
            });
        }
    }

    private boolean imageViewReused(PhotoToLoad photoToLoad) {
        String tag = (String) imageViews.get(photoToLoad.imageView);
        if (tag == null || !tag.equals(photoToLoad.url))
            return true;
        return false;
    }

    class LoadBitmapTask extends AsyncTask<Object, Void, TransitionDrawable> {
        private PhotoToLoad mPhoto;
        private Bitmap bmp = null;

        @Override
        protected TransitionDrawable doInBackground(Object... params) {
            mPhoto = new PhotoToLoad((String) params[0], (ImageView) params[1]);

            if (imageViewReused(mPhoto))
                return null;
            bmp = getBitmap(mPhoto);
            if (bmp == null) return null;

            memoryCache.put(mPhoto.url, bmp);

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
        protected void onPostExecute(TransitionDrawable drawable) {
            if (imageViewReused(mPhoto)) {
                return;
            }

            if (drawable != null) {
                mPhoto.imageView.setImageDrawable(drawable/*(Bitmap) memoryCache.get(mPhoto.url)*/);

                drawable.startTransition(500);
            } else {
                mPhoto.imageView.setImageDrawable(mStubDrawable);
            }
        }


    }

}
