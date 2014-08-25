package com.banba.digitalclock;

import android.app.Application;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;

/**
 * Created by Ernan on 02/01/14.
 * Copyrite Banba Inc. 2013.
 */
public class DigitalClockApp extends Application {
    //    public static DbHelper dbHelper;
    public static SQLiteDatabase db;
    public static SharedPreferences sp;

    @Override
    public void onCreate() {
        super.onCreate();

        PreferenceManager.setDefaultValues(this, R.xml.prefs, false);
        sp = PreferenceManager.getDefaultSharedPreferences(this);

//        dbHelper = new DbHelper(this);
//        db = dbHelper.getWritableDatabase();
    }

    public static String getRingtone() {
//        return sp.getString(RemindMe.RINGTONE_PREF, DEFAULT_NOTIFICATION_URI.toString());
        return null;
    }
}
