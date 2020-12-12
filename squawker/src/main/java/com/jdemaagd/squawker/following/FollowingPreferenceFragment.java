package com.jdemaagd.squawker.following;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.google.firebase.messaging.FirebaseMessaging;
import com.jdemaagd.squawker.R;

public class FollowingPreferenceFragment extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    private final static String LOG_TAG = FollowingPreferenceFragment.class.getSimpleName();

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.following_squawker);
    }

    /**
     * Triggered when shared preferences changes
     * When a person is followed or un-followed
     *
     * @param sharedPreferences SharedPreferences file
     * @param key               key of preference which was changed
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);

        if (preference != null && preference instanceof SwitchPreferenceCompat) {
            boolean isOn = sharedPreferences.getBoolean(key, false);

            if (isOn) {
                FirebaseMessaging.getInstance().subscribeToTopic(key);
                Log.d(LOG_TAG, "Subscribing to " + key);
            } else {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(key);
                Log.d(LOG_TAG, "Un-subscribing to " + key);
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}