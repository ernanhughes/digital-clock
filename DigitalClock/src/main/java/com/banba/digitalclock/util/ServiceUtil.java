package com.banba.digitalclock.util;

import android.app.ActivityManager;
import android.content.Context;

/**
 * Created by Ernan on 14/12/13.
 * Copyrite Banba Inc. 2013.
 */
public class ServiceUtil {
    public static boolean isMyServiceRunning(Context context, String className) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (className.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
