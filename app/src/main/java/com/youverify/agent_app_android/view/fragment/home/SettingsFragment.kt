package com.youverify.agent_app_android.view.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceFragmentCompat
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.databinding.FragmentDashboardBinding
import com.youverify.agent_app_android.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment(){

//    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
////        activity?.setContentView(R.layout.fragment_settings)
//        setPreferencesFromResource(R.layout.fragment_settings, rootKey)
//    }

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSettingsBinding.inflate(layoutInflater)

        return binding.root
    }
}