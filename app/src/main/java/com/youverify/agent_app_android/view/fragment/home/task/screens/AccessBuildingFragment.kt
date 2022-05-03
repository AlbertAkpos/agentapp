package com.youverify.agent_app_android.view.fragment.home.task.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.databinding.FragmentAccessBuildingBinding

class AccessBuildingFragment : Fragment() {

    private lateinit var binding: FragmentAccessBuildingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccessBuildingBinding.inflate(layoutInflater)

        return binding.root
    }
}