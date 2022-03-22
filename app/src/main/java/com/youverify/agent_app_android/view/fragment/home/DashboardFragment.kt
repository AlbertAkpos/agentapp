package com.youverify.agent_app_android.view.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.databinding.FragmentDashboardBinding
import com.youverify.agent_app_android.databinding.FragmentProfileBinding

class DashboardFragment : Fragment(R.layout.fragment_dashboard) {
    private lateinit var binding: FragmentDashboardBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentDashboardBinding.inflate(layoutInflater)

        return binding.root
    }
}