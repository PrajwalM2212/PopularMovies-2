package com.example.prajwalm.popularmovies;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

/**
 * Created by prajwalm on 13/08/17.
 */

public class Preference extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences_map);

        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        int count = preferenceScreen.getPreferenceCount();

        for (int i = 0; i < count; i++) {

            android.support.v7.preference.Preference p = preferenceScreen.getPreference(i);

            if (!(p instanceof CheckBoxPreference)) {

                String value = sharedPreferences.getString(p.getKey(), "");
                setPreferenceSummary(p, value);

            }

        }

    }

    public void setPreferenceSummary(android.support.v7.preference.Preference preference, String value) {


        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;

            int prefIndex = listPreference.findIndexOfValue(value);
            if (prefIndex >= 0) {

                listPreference.setSummary(listPreference.getEntries()[prefIndex]);


            }

        }

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {


        android.support.v7.preference.Preference p = findPreference(key);
        if (p != null) {

            if (!(p instanceof CheckBoxPreference)) {


                String value = sharedPreferences.getString(p.getKey(), "");
                setPreferenceSummary(p, value);


            }


        }


    }

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);


    }

    public void onDestroy(){


       super.onDestroy();

        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);







    } }
