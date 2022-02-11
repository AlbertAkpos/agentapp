package com.youverify.agent_app_android.view.fragment.home

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.youverify.agent_app_android.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_settings, rootKey)
    }
}