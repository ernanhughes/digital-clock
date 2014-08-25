package com.banba.digitalclock;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.View;

public class AnalogClock extends View {

    private float mPropSize;
    private float mPropShadowRadius;
    private float mPropBorderRadius;
    private float mPropBorderWidth;
    private float mPropHourHeight;
    private float mPropHourHeightNegative;
    private float mPropMinuteHeight;
    private float mPropMinuteHeightNegative;
    private float mPropSecondHeight;
    private float mPropSecondHeightNegative;
    private float mPropHandsWidth;

    private Time mTime;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mPaint;


    public AnalogClock(Context context) {
        this(context, null);
    }

    public AnalogClock(Context context, AttributeSet attrs/*, int defStyle*/) {
        super(context, attrs/*, defStyle*/);
        TypedArray r = context.obtainStyledAttributes(attrs, R.styleable.AnalogClock/*, defStyle, 0*/);

        mPropSize = r.getDimension(R.styleable.AnalogClock_widget_analog_size, R.dimen.widget_analog_size);
        mPropShadowRadius = r.getDimension(R.styleable.AnalogClock_widget_analog_shadow_radius, R.dimen.widget_analog_shadow_radius);
        mPropBorderRadius = r.getDimension(R.styleable.AnalogClock_widget_analog_border_radius, R.dimen.widget_analog_border_radius);
        mPropBorderWidth = r.getDimension(R.styleable.AnalogClock_widget_analog_border_width, R.dimen.widget_analog_border_width);
        mPropHourHeight = r.getDimension(R.styleable.AnalogClock_widget_analog_hour_height, R.dimen.widget_analog_hour_height);
        mPropHourHeightNegative = r
                .getDimension(R.styleable.AnalogClock_widget_analog_hour_height_negative, R.dimen.widget_analog_hour_height_negative);
        mPropMinuteHeight = r.getDimension(R.styleable.AnalogClock_widget_analog_size, R.dimen.widget_analog_minute_height);
        mPropMinuteHeightNegative = r
                .getDimension(R.styleable.AnalogClock_widget_analog_size, R.dimen.widget_analog_minute_height_negative);
        mPropSecondHeight = r.getDimension(R.styleable.AnalogClock_widget_analog_size, R.dimen.widget_analog_second_height);
        mPropSecondHeightNegative = r
                .getDimension(R.styleable.AnalogClock_widget_analog_size, R.dimen.widget_analog_second_second_negative);
        mPropHandsWidth = r.getDimension(R.styleable.AnalogClock_widget_analog_size, R.dimen.widget_analog_hands_width);


        mTime = new Time();
        mBitmap = Bitmap.createBitmap(Math.round(mPropSize),
                Math.round(mPropSize), Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setColor(r.getColor(R.color.widget_stroke_color));
//        mPaint.setShadowLayer(mPropShadowRadius, 0, 0,
//                r.getColor(R.color.widget_shadow_color));
    }

    public Bitmap draw() {
        float center = mPropSize / 2;
        mTime.setToNow();

        mCanvas.drawColor(0, Mode.CLEAR);

        // Draw clock circle
        mPaint.setStrokeWidth(mPropBorderWidth);
        mCanvas.drawCircle(center, center, mPropBorderRadius, mPaint);

        // Hands
        mPaint.setStrokeWidth(mPropHandsWidth);

        // Draw hour hand
        mCanvas.save();
        mCanvas.rotate(mTime.hour * 30 + mTime.minute / 2, center, center);
        mCanvas.drawLine(center, center - mPropHourHeight, center, center
                + mPropHourHeightNegative, mPaint);
        mCanvas.restore();

        // Draw minute hand
        mCanvas.save();
        mCanvas.rotate(mTime.minute * 6, center, center);
        mCanvas.drawLine(center, center - mPropMinuteHeight, center, center
                + mPropMinuteHeightNegative, mPaint);

        // Draw second hand
        mCanvas.save();
        mCanvas.rotate(mTime.second * 6, center, center);
        mCanvas.drawLine(center, center - mPropSecondHeight, center, center
                + mPropSecondHeightNegative, mPaint);

        mCanvas.restore();

        return mBitmap;
    }

}