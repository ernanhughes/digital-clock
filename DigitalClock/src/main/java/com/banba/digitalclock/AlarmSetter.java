package com.banba.digitalclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Ernan on 02/01/14.
 * Copyrite Banba Inc. 2013.
 */
public class AlarmSetter extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, AlarmService.class);
        service.setAction(AlarmService.CREATE);
        context.startService(service);
    }
}
