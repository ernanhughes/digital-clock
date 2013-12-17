package com.banba.digitalclock.ui;

import android.text.format.Time;

import java.util.TimeZone;

/**
 * Represents a clock that has timezone support.
 *
 * @author jonson
 */
public interface Clock {

    public void setTimeZone(TimeZone timeZone);

    public void onTimeChanged(Time now);

}
