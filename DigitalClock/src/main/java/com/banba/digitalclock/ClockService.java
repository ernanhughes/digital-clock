package com.banba.digitalclock;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.banba.digitalclock.util.TimeToWords;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Ernan on 16/12/13.
 * Copyrite Banba Inc. 2013.
 */
public class ClockService extends IntentService {
    private static final DateFormat dateFormat =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private static final String TAG = "com.banba.digitalclock.ClockService";

    public static final String ACTION_UPDATE =
            "com.banba.digitalclock.ACTION_UPDATE";

    private static final int WIDGET_CATEGORY_HOME_SCREEN = 1;
    private static final int WIDGET_CATEGORY_KEYGUARD = 2;

    public ClockService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent.getAction().equals(ACTION_UPDATE)) {
            Calendar now = Calendar.getInstance();
            updateTime(now);
        }
    }

    private void updateTime(Calendar date) {
        Log.d(TAG, "Update: " + dateFormat.format(date.getTime()));
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        ComponentName name = new ComponentName(this, TextClockAppWidget.class);
        int[] appIds = manager.getAppWidgetIds(name);
        String[] words = TimeToWords.timeToWords(date);
        for (int id : appIds) {
            int layoutId = R.layout.appwidget;
            if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
            {
                if ( getAppWidgetCategory( manager, id ) == WIDGET_CATEGORY_KEYGUARD )
                {
                    layoutId = R.layout.keyguard;
                }
            }
            RemoteViews v = new RemoteViews(getPackageName(),
                    layoutId);
            updateTime(words, v);
            manager.updateAppWidget(id, v);
        }
        name = new ComponentName(this, AnalogClockAppWidget.class);
        appIds = manager.getAppWidgetIds(name);
        for (int id : appIds) {
            int layoutId = R.layout.analog_appwidget;
            RemoteViews v = new RemoteViews(getPackageName(),
                    layoutId);
            manager.updateAppWidget(id, v);
        }
    }

    @TargetApi( Build.VERSION_CODES.JELLY_BEAN )
    private int getAppWidgetCategory(AppWidgetManager manager, int id)
    {
        int category = WIDGET_CATEGORY_HOME_SCREEN;
        Bundle options = manager.getAppWidgetOptions( id );
        if ( options != null )
        {
            category = options.getInt("appWidgetCategory", 1);
        }
        return category;
    }

    private void updateTime(String[] words, RemoteViews views) {
        views.setTextViewText(R.id.hours, words[0]);
        if (words.length == 1) {
            views.setViewVisibility(R.id.minutes, View.INVISIBLE);
            views.setViewVisibility(R.id.tens, View.INVISIBLE);
        } else if (words.length == 2) {
            views.setViewVisibility(R.id.minutes, View.INVISIBLE);
            views.setViewVisibility(R.id.tens, View.VISIBLE);
            views.setTextViewText(R.id.tens, words[1]);
        } else {
            views.setViewVisibility(R.id.minutes, View.VISIBLE);
            views.setViewVisibility(R.id.tens, View.VISIBLE);
            views.setTextViewText(R.id.tens, words[1]);
            views.setTextViewText(R.id.minutes, words[2]);
        }
    }


}
