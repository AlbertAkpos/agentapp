package com.youverify.agent_app_android.view.fragment.home.identity_verification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.databinding.FragmentVerificationFailedBinding

class VerificationFailedFragment : Fragment(R.layout.fragment_verification_failed) {
    private lateinit var binding : FragmentVerificationFailedBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{

        binding = FragmentVerificationFailedBinding.inflate(layoutInflater)

        return binding.root
    }
}