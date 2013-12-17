package com.banba.digitalclock;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;

/**
 * Created by Ernan on 13/12/13.
 * Copyrite Banba Inc. 2013.
 */
public class Preferences extends PreferenceActivity {

    static final String COMPONENT_TIME = "time";
    static final String COMPONENT_DATE = "date";

    static final String PREF_STYLE_TIME = "pref_style_time";
    static final String PREF_STYLE_DATE = "pref_style_date";

    static final String PREF_HIDE_SETTINGS = "pref_hide_settings"; // deprecated
    static final String PREF_SETTINGS_BUTTON = "pref_settings_button";
    static final String PREF_AGGRESSIVE_CENTERING = "pref_aggressive_centering";

    static final String PREF_SETTINGS_BUTTON_HIDDEN = "hidden";
    static final String PREF_SETTINGS_BUTTON_IN_WIDGET = "inwidget";
    static final String PREF_SETTINGS_BUTTON_IN_LAUNCHER = "inlauncher";

    static final String PREF_HOMESCREEN_FOREGROUND_COLOR = "pref_homescreen_foreground_color";
    static final String PREF_HOMESCREEN_BACKGROUND_OPACITY = "pref_homescreen_background_opacity";
    static final String PREF_HOMESCREEN_HIDE_CLOCK = "pref_homescreen_hide_clock";

    static final String PREF_LOCKSCREEN_FOREGROUND_COLOR = "pref_lockscreen_foreground_color";
    static final String PREF_LOCKSCREEN_BACKGROUND_OPACITY = "pref_lockscreen_background_opacity";
    static final String PREF_LOCKSCREEN_HIDE_CLOCK = "pref_lockscreen_hide_clock";

    public static final int DEFAULT_WIDGET_FOREGROUND_COLOR = Color.WHITE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new PreferencesFragment()).commit();
    }

    /**
     * This fragment shows the preferences for the first header.
     */
    public static class PreferencesFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // Make sure default values are applied.  In a real app, you would
            // want this in a shared function that is used to retrieve the
            // SharedPreferences wherever they are needed.
            PreferenceManager.setDefaultValues(getActivity(),
                    R.xml.prefs, false);
            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.prefs);
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(this);
            // TODO update preferences
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences()
                    .unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            updatePreference(key);
        }

        private void updatePreference(String key) {
            Preference pref = findPreference(key);

            if (pref instanceof ListPreference) {
                ListPreference listPref = (ListPreference) pref;
                pref.setSummary(listPref.getEntry());
                return;
            }

            if (pref instanceof EditTextPreference) {
                EditTextPreference editPref = (EditTextPreference) pref;
                editPref.setSummary(editPref.getText());
                return;
            }

            if (pref instanceof RingtonePreference) {
//                Uri ringtoneUri = Uri.parse(RemindMe.getRingtone());
//                Ringtone ringtone = RingtoneManager.getRingtone(this, ringtoneUri);
//                if (ringtone != null) pref.setSummary(ringtone.getTitle(this));
            }
        }
    }
}
