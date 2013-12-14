package com.banba.digitalclock;

import android.app.backup.BackupAgentHelper;
import android.app.backup.SharedPreferencesBackupHelper;

/**
 * Created by Ernan on 14/12/13.
 * Copyrite Banba Inc. 2013.
 */
public class BackupAgent extends BackupAgentHelper {
    private static final String PREFS_BACKUP_KEY = "prefs";

    // Allocate a helper and add it to the backup agent
    @Override
    public void onCreate() {
        // Compute the default preferences filename.
        String defaultPrefsFilename = getPackageName() + "_preferences";
        addHelper(PREFS_BACKUP_KEY,
                new SharedPreferencesBackupHelper(this, defaultPrefsFilename));
    }
}
