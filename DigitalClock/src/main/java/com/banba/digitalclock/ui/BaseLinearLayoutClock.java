package com.banba.digitalclock.ui;

import android.content.Context;
import android.text.format.Time;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import java.util.TimeZone;


/**
 * Abstract LinearLayout based clock.  Can (should) be extended by widgets that
 * need clock functionality that are also based on a LinearLayout.
 *
 * @author jonson
 */
public abstract class BaseLinearLayoutClock extends LinearLayout implements Clock {

    private final ClockHelper helper;

    public BaseLinearLayoutClock(Context context, AttributeSet attrs) {
        super(context, attrs);

        helper = new ClockHelper(new ClockHelper.OnTimeChangeListener() {
            @Override
            public void handleTimeChange(Time now) {
                onTimeChanged(now);
            }
        });
    }

    public BaseLinearLayoutClock(Context context) {
        this(context, null);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        helper.onAttachToWindow(getContext());
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        helper.onDetachedFromWindow(getContext());
    }

    @Override
    public void setTimeZone(TimeZone timeZone) {
        helper.setTimeZone(timeZone);
    }

    protected Time getNow() {
        return helper.mCalendar;
    }

}
