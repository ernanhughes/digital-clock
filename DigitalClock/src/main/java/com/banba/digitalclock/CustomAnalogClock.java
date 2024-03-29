package com.banba.digitalclock;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;


public class CustomAnalogClock extends View {

    private Drawable mHourHand;
    private Drawable mMinuteHand;
    private Drawable mSecondHand;
    private Drawable mDial;

    private int mDialWidth;
    private int mDialHeight;

    private float mSeconds;
    private float mMinutes;
    private float mHour;

    Context mContext;

    public CustomAnalogClock(Context context) {
        super(context);
    }

    public CustomAnalogClock(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomAnalogClock(Context context, AttributeSet attrs,
                             int defStyle) {
        super(context, attrs, defStyle);
        Resources r = context.getResources();
        TypedArray a =
                context.obtainStyledAttributes(
                        attrs, R.styleable.AnalogClock, defStyle, 0);
        mContext = context;
        // mDial = a.getDrawable(com.android.internal.R.styleable.AnalogClock_dial);
        // if (mDial == null) {
        mDial = r.getDrawable(R.drawable.clock_dial);
        // }

        //  mHourHand = a.getDrawable(com.android.internal.R.styleable.AnalogClock_hand_hour);
        //  if (mHourHand == null) {
        mHourHand = r.getDrawable(R.drawable.clock_hour);
        //  }

        //   mMinuteHand = a.getDrawable(com.android.internal.R.styleable.AnalogClock_hand_minute);
        //   if (mMinuteHand == null) {
        mMinuteHand = r.getDrawable(R.drawable.clock_minute);
        mSecondHand = r.getDrawable(R.drawable.clockgoog_minute);
        //   }

        mDialWidth = mDial.getIntrinsicWidth();
        mDialHeight = mDial.getIntrinsicHeight();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        float hScale = 1.0f;
        float vScale = 1.0f;

        if (widthMode != MeasureSpec.UNSPECIFIED && widthSize < mDialWidth) {
            hScale = (float) widthSize / (float) mDialWidth;
        }

        if (heightMode != MeasureSpec.UNSPECIFIED && heightSize < mDialHeight) {
            vScale = (float) heightSize / (float) mDialHeight;
        }

        float scale = Math.min(hScale, vScale);

        setMeasuredDimension(resolveSize((int) (mDialWidth * scale), widthMeasureSpec),
                resolveSize((int) (mDialHeight * scale), heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //Here you can set the size of your clock
        int availableWidth = 70;
        int availableHeight = 70;

        //Actual size
        int x = availableWidth / 2;
        int y = availableHeight / 2;

        final Drawable dial = mDial;
        int w = dial.getIntrinsicWidth();
        int h = dial.getIntrinsicHeight();

        boolean scaled = false;

        if (availableWidth < w || availableHeight < h) {
            scaled = true;
            float scale = Math.min((float) availableWidth / (float) w,
                    (float) availableHeight / (float) h);
            canvas.save();
            canvas.scale(scale, scale, x, y);
        }

        dial.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
        dial.draw(canvas);

        canvas.save();
        canvas.rotate(mHour / 12.0f * 360.0f, x, y);
        w = mHourHand.getIntrinsicWidth();
        h = mHourHand.getIntrinsicHeight();
        mHourHand.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
        mHourHand.draw(canvas);
        canvas.restore();

        canvas.save();
        canvas.rotate(mMinutes / 60.0f * 360.0f, x, y);
        w = mMinuteHand.getIntrinsicWidth();
        h = mMinuteHand.getIntrinsicHeight();
        mMinuteHand.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
        mMinuteHand.draw(canvas);
        canvas.restore();

        canvas.save();
        canvas.rotate(mSeconds, x, y);
        w = mSecondHand.getIntrinsicWidth();
        h = mSecondHand.getIntrinsicHeight();
        mSecondHand.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
        mSecondHand.draw(canvas);
        canvas.restore();
        if (scaled) {
            canvas.restore();
        }
    }

    public void setTime(int hours, int minutes, int seconds) {
        mSeconds = 6.0f * seconds;
        mMinutes = minutes + seconds / 60.0f;
        mHour = hours + mMinutes / 60.0f;
    }

}
