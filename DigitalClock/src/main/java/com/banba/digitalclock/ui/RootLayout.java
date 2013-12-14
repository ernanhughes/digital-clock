package com.banba.digitalclock.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by Ernan on 14/12/13.
 * Copyrite Banba Inc. 2013.
 */
public class RootLayout extends FrameLayout {
    private RootLayoutListener mRootLayoutListener;
    private boolean mCancelCurrentEvent;

    public RootLayout(Context context) {
        super(context);
    }

    public RootLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RootLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            // ACTION_UP doesn't seem to reliably get called. Otherwise
            // should postDelayed on ACTION_UP instead of ACTION_DOWN.
            case MotionEvent.ACTION_DOWN:
                if (mRootLayoutListener != null && !mRootLayoutListener.isAwake()) {
                    mCancelCurrentEvent = true;
                    mRootLayoutListener.onAwake();
                } else {
                    mCancelCurrentEvent = false;
                }
                break;
        }
        return mCancelCurrentEvent;
    }

    public void setRootLayoutListener(RootLayoutListener rootLayoutListener) {
        mRootLayoutListener = rootLayoutListener;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mRootLayoutListener != null) {
            mRootLayoutListener.onSizeChanged(w, h);
        }
    }

    public static interface RootLayoutListener {
        void onAwake();
        void onSizeChanged(int width, int height);
        boolean isAwake();
    }
}
