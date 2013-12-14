package com.banba.digitalclock.ui;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.HashMap;
import java.util.Map;

import android.widget.TextClock;

/**
 * Created by Ernan on 14/12/13.
 * Copyrite Banba Inc. 2013.
 */
public class DigitalFlipClock extends TextClock {

    public static int[] get24HourDigits(Time time) {
        int[] ints = new int[6];
        ints[0] = time.hour / 10;
        ints[1] = time.hour % 10;
        ints[2] = time.minute / 10;
        ints[3] = time.minute % 10;
        ints[4] = time.second / 10;
        ints[5] = time.second % 10;
        return ints;
    }

    public static int[] get12HourDigits(Time time) {
        int[] ints = new int[6];

        int hour = time.hour % 12;
        // 0 or 1
        ints[0] = hour == 0 || hour > 9 ? 1 : 0;
        ints[1] = hour == 0 ? 2 : (hour % 10);
        ints[2] = time.minute / 10;
        ints[3] = time.minute % 10;
        ints[4] = time.second / 10;
        ints[5] = time.second % 10;
        return ints;
    }

    public DigitalFlipClock(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DigitalFlipClock(Context context) {
        this(context, null);
    }


//    public static int[] getDigitDrawables(boolean twentyFourHour, int[] digits) {
//
//        int[] ints = new int[4];
//
//        // should be special
//        ints[0] = getHour0Drawable(twentyFourHour, digits[0]);
//        ints[1] = getHour1Drawable(twentyFourHour, digits[0], digits[1]);
//
//        // this one should be special
//        ints[2] = getMinute0Drawable(digits[2]);
//        ints[3] = mod10DigitToDrawable.get(digits[3]);
//        return ints;
//    }
//
//    /**
//     * Special treatment, in 24 hour:
//     * 23->00
//     * Hour 0 goes from 2-> 0
//     *
//     * 12 hour:
//     * 12->01
//     * Hour 0 goes from 1 -> 0
//     *
//     * @param twentyFourHour
//     * @param hour0
//     * @return
//     */
//    public static int getHour0Drawable(boolean twentyFourHour, int hour0) {
//        if (twentyFourHour && hour0 == 0) {
//            return R.anim.flip_2_0;
//        } else if (!twentyFourHour && hour0 == 0) {
//            return R.anim.flip_1_0;
//        } else {
//            return mod10DigitToDrawable.get(hour0);
//        }
//    }
//
//    /**
//     * 24 hour:
//     * 23 -> 00
//     * Hour 1 goes from 3-> 0
//     *
//     * 12 hour:
//     * 12 -> 01
//     * Hour 1 goes from 2 -> 1
//     * (however the case 10->11 still must work)
//     *
//     * @param twentyFourHour
//     * @param hour1
//     * @return
//     */
//    public static int getHour1Drawable(boolean twentyFourHour, int hour0, int hour1) {
//        if (twentyFourHour && hour1 == 0) {
//            return R.anim.flip_3_0;
//        } else if (!twentyFourHour && hour0 == 0 && hour1 == 1) {
//            return R.anim.flip_2_1;
//        } else {
//            return mod10DigitToDrawable.get(hour1);
//        }
//    }
//
//    /**
//     * Special treatment needed for the tick 59->00.  Minute 0
//     * goes from 5->0.
//     *
//     * @param minute0
//     * @return
//     */
//    public static int getMinute0Drawable(int minute0) {
//        if (minute0 == 0) {
//            return R.anim.flip_5_0;
//        } else {
//            return mod10DigitToDrawable.get(minute0);
//        }
//    }
//
//
//    private TextView hour0;
//    private TextView hour1;
//    private TextView minute0;
//    private TextView minute1;
//    private TextView second0;
//    private TextView second1;
//
//    int[] last = null;
//
//    public DigitalFlipClock(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        init();
//    }
//
//    public DigitalFlipClock(Context context) {
//        this(context, null);
//    }
//
//    private void init() {
//        LayoutInflater inflater = (LayoutInflater) getContext()
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//        View layout = inflater.inflate(R.layout.inc_flipclock, this);
//        hour0 = (TextView) layout.findViewById(R.id.dcHour0);
//        hour1 = (TextView) layout.findViewById(R.id.dcHour1);
//        minute0 = (TextView) layout.findViewById(R.id.dcMinute0);
//        minute1 = (TextView) layout.findViewById(R.id.dcMinute1);
//        second0 = (TextView) layout.findViewById(R.id.dcSecond0);
//        second1 = (TextView) layout.findViewById(R.id.dcSecond1);
//    }
//
//    @Override
//    public void onTimeChanged(Time now) {
//
//        int[] vals;
//        int[] drawables;
//        boolean twentyFourHour = DateFormat.is24HourFormat(getContext());
//
//        if (twentyFourHour) {
//            vals = get24HourDigits(now);
//        } else {
//            vals = get12HourDigits(now);
//        }
//
//        drawables = getDigitDrawables(twentyFourHour, vals);
//
//        hour0.setBackgroundResource(drawables[0]);
//        hour1.setBackgroundResource(drawables[1]);
//        minute0.setBackgroundResource(drawables[2]);
//        minute1.setBackgroundResource(drawables[3]);
//        second0.setBackgroundResource(drawables[4]);
//        second1.setBackgroundResource(drawables[5]);
//
//
////		if (last != null && last[0] != vals[0])
//        startAnimation(hour0);
////		if (last != null && last[1] != vals[1])
//        startAnimation(hour1);
////		if (last != null && last[2] != vals[2])
//        startAnimation(minute0);
////		if (last != null && last[3] != vals[3])
//        startAnimation(minute1);
//
//        startAnimation(second0);
////		if (last != null && last[3] != vals[3])
//        startAnimation(second1);
//
//        last = vals;
//
//    }
//
//    /**
//     * Ensure the animation is run on the UI thread
//     *
//     * @param iv
//     */
//    private void startAnimation(final TextView iv) {
//        iv.post(new Runnable() {
//            @Override
//            public void run() {
//                AnimationDrawable frameAnimation =
//                        (AnimationDrawable) iv.getBackground();
//                frameAnimation.start();
//            }
//        });
//    }

}
