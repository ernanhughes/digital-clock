package com.banba.digitalclock.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.banba.digitalclock.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ernan on 14/12/13.
 * Copyrite Banba Inc. 2013.
 */
public class DigitalFlipClock extends BaseLinearLayoutClock {


    public static final Map<Integer, Integer> mod10DigitToDrawable = new HashMap<Integer, Integer>();

    static {
    }

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

    Context mContext;
    Animation in;
    Animation out;


    public DigitalFlipClock(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        in = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_top);
        out = AnimationUtils.loadAnimation(mContext, R.anim.slide_out_bottom);

        init();
    }

    Handler mHandler;
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            Time t = new Time();
            t.setToNow();
            onTimeChanged(t);
            mHandler.postDelayed(mUpdateTimeTask, 1000);
        }
    };

    public DigitalFlipClock(Context context) {
        this(context, null);
    }


    public static int[] getDigitDrawables(boolean twentyFourHour, int[] digits) {

        int[] ints = new int[4];

        // should be special
        ints[0] = getHour0Drawable(twentyFourHour, digits[0]);
        ints[1] = getHour1Drawable(twentyFourHour, digits[0], digits[1]);

        // this one should be special
        ints[2] = getMinute0Drawable(digits[2]);
        ints[3] = mod10DigitToDrawable.get(digits[3]);
        return ints;
    }

    /**
     * Special treatment, in 24 hour:
     * 23->00
     * Hour 0 goes from 2-> 0
     * <p/>
     * 12 hour:
     * 12->01
     * Hour 0 goes from 1 -> 0
     *
     * @param twentyFourHour
     * @param hour0
     * @return
     */
    public static int getHour0Drawable(boolean twentyFourHour, int hour0) {
        if (twentyFourHour && hour0 == 0) {
//            return R.anim.flip_2_0;
        } else if (!twentyFourHour && hour0 == 0) {
//            return R.anim.flip_1_0;
        } else {
            return mod10DigitToDrawable.get(hour0);
        }
        return mod10DigitToDrawable.get(hour0);
    }

    /**
     * 24 hour:
     * 23 -> 00
     * Hour 1 goes from 3-> 0
     * <p/>
     * 12 hour:
     * 12 -> 01
     * Hour 1 goes from 2 -> 1
     * (however the case 10->11 still must work)
     *
     * @param twentyFourHour
     * @param hour1
     * @return
     */
    public static int getHour1Drawable(boolean twentyFourHour, int hour0, int hour1) {
        if (twentyFourHour && hour1 == 0) {
  //          return R.anim.flip_3_0;
        } else if (!twentyFourHour && hour0 == 0 && hour1 == 1) {
    //        return R.anim.flip_2_1;
        } else {
            return mod10DigitToDrawable.get(hour1);
        }
        return mod10DigitToDrawable.get(hour1);
    }

    /**
     * Special treatment needed for the tick 59->00.  Minute 0
     * goes from 5->0.
     *
     * @param minute0
     * @return
     */
    public static int getMinute0Drawable(int minute0) {
        if (minute0 == 0) {
     //       return R.anim.flip_5_0;
        } else {
            return mod10DigitToDrawable.get(minute0);
        }
        return mod10DigitToDrawable.get(minute0);
    }


    private TextSwitcher shour0 = null;
    private TextSwitcher shour1;
    private TextSwitcher sminute0;
    private TextSwitcher sminute1;
    private TextSwitcher ssecond0;
    private TextSwitcher ssecond1;


    // set the animation type of textSwitcher
    int[] last = null;

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View layout = inflater.inflate(R.layout.inc_flipclock, this);

        shour0 = (TextSwitcher) layout.findViewById(R.id.dcHour0);
        processText(shour0);
        //   hour0.setTypeface(tf, Typeface.BOLD);
        shour1 = (TextSwitcher) layout.findViewById(R.id.dcHour1);
        processText(shour1);
        //  hour1.setTypeface(tf, Typeface.BOLD);
        sminute0 = (TextSwitcher) layout.findViewById(R.id.dcMinute0);
        processText(sminute0);
        // minute0.setTypeface(tf, Typeface.BOLD);
        sminute1 = (TextSwitcher) layout.findViewById(R.id.dcMinute1);
        processText(sminute1);
        // minute1.setTypeface(tf, Typeface.BOLD);
        ssecond0 = (TextSwitcher) layout.findViewById(R.id.dcSecond0);
        processText(ssecond0);
        // second0.setTypeface(tf, Typeface.BOLD);
        ssecond1 = (TextSwitcher) layout.findViewById(R.id.dcSecond1);
        processText(ssecond1);
        //second1.setTypeface(tf, Typeface.BOLD);

        mHandler = new Handler();
        mHandler.removeCallbacks(mUpdateTimeTask);
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }


    void processText(TextSwitcher v) {
        v.setFactory(new ViewSwitcher.ViewFactory() {
            public View makeView() {
                TextView myText = new TextView(mContext);
                Typeface tf = Typeface.createFromAsset(mContext.getAssets(), "fonts/digital-7.ttf");
                myText.setTypeface(tf, Typeface.BOLD);
                myText.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                myText.setTextSize(100);
                myText.setTextColor(Color.BLUE);
                return myText;
            }
        });
        v.setInAnimation(in);
        v.setOutAnimation(out);
    }

    boolean firsttime = true;

    @Override
    public void onTimeChanged(Time now) {


        if (null != shour0) {
            int[] vals;
            int[] drawables;
            boolean twentyFourHour = DateFormat.is24HourFormat(getContext());

            if (twentyFourHour) {
                vals = get24HourDigits(now);
            } else {
                vals = get12HourDigits(now);
            }

            if (firsttime) {
                shour0.setText(String.valueOf(vals[0]));
                shour1.setText(String.valueOf(vals[1]));
                sminute0.setText(String.valueOf(vals[2]));
                sminute1.setText(String.valueOf(vals[3]));
                ssecond0.setText(String.valueOf(vals[4]));
                ssecond1.setText(String.valueOf(vals[5]));
                firsttime = false;
            }

            if (last != null && last[0] != vals[0])
                shour0.setText(String.valueOf(vals[0]));
            if (last != null && last[1] != vals[1])
                shour1.setText(String.valueOf(vals[1]));
            if (last != null && last[2] != vals[2])
                sminute0.setText(String.valueOf(vals[2]));
            if (last != null && last[3] != vals[3])
                sminute1.setText(String.valueOf(vals[3]));
            if (last != null && last[4] != vals[4])
                ssecond0.setText(String.valueOf(vals[4]));
            if (last != null && last[5] != vals[5]) {
                ssecond1.setText(String.valueOf(vals[5]));
            }
            last = vals;
        }
    }

    public static Animation inFromRightAnimation() {

        Animation inFromRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, +1.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f
        );
        inFromRight.setDuration(350);
        inFromRight.setInterpolator(new AccelerateInterpolator());
        return inFromRight;
    }

    public static Animation outToLeftAnimation() {
        Animation outtoLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f
        );
        outtoLeft.setDuration(350);
        outtoLeft.setInterpolator(new AccelerateInterpolator());
        return outtoLeft;
    }

    // for the next movement
    public static Animation inFromLeftAnimation() {
        Animation inFromLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, -1.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f
        );
        inFromLeft.setDuration(350);
        inFromLeft.setInterpolator(new AccelerateInterpolator());
        return inFromLeft;
    }

    public static Animation outToRightAnimation() {
        Animation outtoRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f
        );
        outtoRight.setDuration(350);
        outtoRight.setInterpolator(new AccelerateInterpolator());
        return outtoRight;
    }

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


    /**
     * Ensure the animation is run on the UI thread
     *
     * @param iv
     */
    private void startAnimation(final TextView iv) {
        iv.post(new Runnable() {
            @Override
            public void run() {
                AnimationDrawable frameAnimation =
                        (AnimationDrawable) iv.getBackground();
                frameAnimation.start();
            }
        });
    }

}
