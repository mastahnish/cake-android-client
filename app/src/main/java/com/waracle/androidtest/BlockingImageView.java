package com.waracle.androidtest;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;

public class BlockingImageView extends android.support.v7.widget.AppCompatImageView {
    private boolean mBlockLayout;

    public BlockingImageView(Context context) {
        super(context);
    }

    public BlockingImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BlockingImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void requestLayout() {
        if (!mBlockLayout) {
            super.requestLayout();
        }
    }

    @Override
    public void setImageResource(int resId) {
        mBlockLayout = true;
        super.setImageResource(resId);
        mBlockLayout = false;
    }

    @Override
    public void setImageURI(Uri uri) {
        mBlockLayout = true;
        super.setImageURI(uri);
        mBlockLayout = false;
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        mBlockLayout = true;
        super.setImageDrawable(drawable);
        mBlockLayout = false;
    }
}