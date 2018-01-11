package cn.read.ui.fragments;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import cn.read.R;

/**
 * Created by lw on 2017-03-13.
 */
public class AboutFragment extends PreferenceFragmentCompat {
    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.about_preference_fragment);
    }
}
