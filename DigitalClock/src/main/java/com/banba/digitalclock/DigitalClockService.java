package com.banba.digitalclock;

import android.animation.AnimatorSet;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.service.dreams.DreamService;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.banba.digitalclock.ui.ClockHelper;
import com.banba.digitalclock.ui.DigitalFlipClock;
import com.banba.digitalclock.ui.RootLayout;

/**
 * Created by Ernan on 13/12/13.
 * Copyrite Banba Inc. 2013.
 */
public class DigitalClockService extends DreamService implements OnClickListener, ClockHelper.OnTimeChangeListener {

    public static final String PREF_DAYDREAM_COLOR = "pref_daydream_color";
    public static final String PREF_DAYDREAM_NIGHT_MODE = "pref_daydream_night_mode";
    public static final String PREF_DAYDREAM_ANIMATION = "pref_daydream_animation";


    private static final int ANIMATION_HAS_ROTATE = 0x1;
    private static final int ANIMATION_HAS_SLIDE = 0x2;
    private static final int ANIMATION_HAS_FADE = 0x4;

    private static final int ANIMATION_NONE = 0;
    private static final int ANIMATION_FADE = ANIMATION_HAS_FADE;
    private static final int ANIMATION_SLIDE = ANIMATION_FADE | ANIMATION_HAS_SLIDE;
    private static final int ANIMATION_PENDULUM = ANIMATION_SLIDE | ANIMATION_HAS_ROTATE;

    private static final int SECONDS_MILLIS = 1000;
    private static final int CYCLE_INTERVAL_MILLIS = 20 * SECONDS_MILLIS;
    private static final int FADE_MILLIS = 5 * SECONDS_MILLIS;
    private static final int TRAVEL_ROTATE_DEGREES = 3;
    private static final float SCALE_WHEN_MOVING = 0.85f;


    private Handler mHandler = new Handler();
    private int mTravelDistance;
    private int mForegroundColor;
    private int mAnimation;

    private ViewGroup mDaydreamContainer;
    private ViewGroup mExtensionsContainer;
    private AnimatorSet mSingleCycleAnimator;

    private boolean mAttached;
    private boolean mNeedsRelayout;
    private boolean mMovingLeft;
    private boolean mManuallyAwoken;

    final ClockHelper helper;

    DigitalFlipClock flipClock = null;

    public DigitalClockService() {
        helper = new ClockHelper(new ClockHelper.OnTimeChangeListener() {
            @Override
            public void handleTimeChange(Time now) {
                System.out.print(now);
//                if(null != flipClock)
//                    flipClock.onTimeChanged();
            }
        });
        matcher = new IntentFilter();
        matcher.addAction(CREATE);
        matcher.addAction(CANCEL);
    }

    public static final String CREATE = "CREATE";
    public static final String CANCEL = "CANCEL";

    private IntentFilter matcher;

    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        String notificationId = intent.getStringExtra("notificationId");

        if (matcher.matchAction(action)) {
            execute(action, notificationId);
        }
    }

    private void execute(String action, String notificationId) {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        Cursor c = RemindMe.db.query(Notification.TABLE_NAME, null, "_id = ?",
//                new String[]{notificationId}, null, null, null);
//
//        if (c.moveToFirst()) {
//            Intent i = new Intent(this, AlarmReceiver.class);
//            i.putExtra("id", c.getLong(c.getColumnIndex(Notification.COL_ID)));
//            i.putExtra("msg", c.getString(c.getColumnIndex(Notification.COL_MSG)));
//
//            PendingIntent pi = PendingIntent.getBroadcast(this, 0, i,
//                    PendingIntent.FLAG_UPDATE_CURRENT);
//
//            long time = c.getLong(c.getColumnIndex(Notification.COL_DATETIME));
//            if (CREATE.equals(action)) {
//                am.set(AlarmManager.RTC_WAKEUP, time, pi);
//
//            } else if (CANCEL.equals(action)) {
//                am.cancel(pi);
//            }
//        }
//        c.close();
    }

    @Override
    public void onDreamingStarted() {
        super.onDreamingStarted();
    }

    @Override
    public void onDreamingStopped() {

        super.onDreamingStopped();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        mAttached = true;
        setInteractive(true);
        setFullscreen(true);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        mForegroundColor = sp.getInt(PREF_DAYDREAM_COLOR,
                Preferences.DEFAULT_WIDGET_FOREGROUND_COLOR);
        String animation = sp.getString(PREF_DAYDREAM_ANIMATION, "");
        if ("none".equals(animation)) {
            mAnimation = ANIMATION_NONE;
        } else if ("slide".equals(animation)) {
            mAnimation = ANIMATION_SLIDE;
        } else if ("fade".equals(animation)) {
            mAnimation = ANIMATION_FADE;
        } else {
            mAnimation = ANIMATION_PENDULUM;
        }
        setScreenBright(!sp.getBoolean(PREF_DAYDREAM_NIGHT_MODE, true));
        setContentView(R.layout.flipclock);
        helper.onAttachToWindow(getApplicationContext());
        flipClock = (DigitalFlipClock) findViewById(R.id.cwClock);

        Toast.makeText(this, "Lock Service Created", Toast.LENGTH_LONG).show();
        Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/digital-7.ttf");


    }

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            mHandler.postDelayed(mUpdateTimeTask, 1000);
        }
    };

    @Override
    public void onDetachedFromWindow() {
        mHandler.removeCallbacksAndMessages(null);
        mAttached = false;
        super.onDetachedFromWindow();
        helper.onDetachedFromWindow(getApplicationContext());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mHandler.removeCallbacks(mCycleRunnable);
        layoutDream();
    }

    private void layoutDream() {
        setContentView(R.layout.flipclock);
        mNeedsRelayout = true;
        renderDaydream(true);

        mHandler.removeCallbacks(mCycleRunnable);
        mHandler.postDelayed(mCycleRunnable, CYCLE_INTERVAL_MILLIS - FADE_MILLIS);

//        EdgeEffectUtil.tryChangeEdgeEffects(
//                (ScrollView) findViewById(R.id.extensions_scroller),
//                mForegroundColor);
    }

    private void renderDaydream(final boolean restartAnimation) {
        if (!mAttached) {
            return;
        }

        if (restartAnimation) {
            // Only modify fullscreen state if this render will restart an animation (enter a new
            // cycle)
            setFullscreen(true);
        }

        final Resources res = getResources();

        mDaydreamContainer = (ViewGroup) findViewById(R.id.daydream_container);
        RootLayout rootContainer = (RootLayout)
                findViewById(R.id.daydream_root);
        if (mTravelDistance == 0) {
            mTravelDistance = rootContainer.getWidth() / 4;
        }
        rootContainer.setRootLayoutListener(new RootLayout.RootLayoutListener() {
            @Override
            public void onAwake() {
                mManuallyAwoken = true;
                setFullscreen(false);
                mHandler.removeCallbacks(mCycleRunnable);
                mHandler.postDelayed(mCycleRunnable, CYCLE_INTERVAL_MILLIS);
                mDaydreamContainer.animate()
                        .alpha(1f)
                        .rotation(0)
                        .scaleX(1f)
                        .scaleY(1f)
                        .translationX(0f)
                        .translationY(0f)
                        .setDuration(res.getInteger(android.R.integer.config_shortAnimTime));
                if (mSingleCycleAnimator != null) {
                    mSingleCycleAnimator.cancel();
                }
            }

            @Override
            public boolean isAwake() {
                return mManuallyAwoken;
            }

            @Override
            public void onSizeChanged(int width, int height) {
                mTravelDistance = width / 4;
            }
        });

        DisplayMetrics displayMetrics = res.getDisplayMetrics();

        int screenWidthDp = (int) (displayMetrics.widthPixels * 1f / displayMetrics.density);
        int screenHeightDp = (int) (displayMetrics.heightPixels * 1f / displayMetrics.density);

    }


    public Runnable mCycleRunnable = new Runnable() {
        @Override
        public void run() {
            mManuallyAwoken = false;
            float outAlpha = 1f;
            if ((mAnimation & ANIMATION_HAS_FADE) != 0) {
                outAlpha = 0f;
            }
            mDaydreamContainer.animate().alpha(outAlpha).setDuration(FADE_MILLIS)
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            renderDaydream(true);
                            mHandler.removeCallbacks(mCycleRunnable);
                            mHandler.postDelayed(mCycleRunnable,
                                    CYCLE_INTERVAL_MILLIS - FADE_MILLIS);
                            mDaydreamContainer.animate().alpha(1f).setDuration(FADE_MILLIS);
                        }
                    });
        }
    };

    @Override
    public void onClick(View v) {

    }

    @Override
    public void handleTimeChange(Time now) {

    }
}
