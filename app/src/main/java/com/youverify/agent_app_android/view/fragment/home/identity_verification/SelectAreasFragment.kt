package com.youverify.agent_app_android.view.fragment.home.identity_verification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.databinding.FragmentSelectAreasBinding

class SelectAreasFragment : Fragment(R.layout.fragment_select_areas) {

    private lateinit var binding: FragmentSelectAreasBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentSelectAreasBinding.inflate(layoutInflater)

        return binding.root
    }
}