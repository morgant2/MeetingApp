package com.meetingapp.fragments;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.meetingapp.R;

public class SettingsFragment extends PreferenceFragment
        implements OnSharedPreferenceChangeListener {

    private SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        //Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        prefs.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        prefs.unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("pref_dark_theme")){
            getActivity().recreate();
        }
    }
}

//
//public class SettingsFragment extends PreferenceFragment {
//    private SharedPreferences.OnSharedPreferenceChangeListener mListener;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        addPreferencesFromResource(R.xml.preferences);
//        setHasOptionsMenu(true);
//        mListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
//            @Override
//            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//                if (!key.equals("pref_dark_theme")) {
//                    return;
//                }
////                Intent intent = new Intent(getActivity(), InformationActivity.class);
////                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
////                startActivity(intent);
//            }
//        };
//    }
//}