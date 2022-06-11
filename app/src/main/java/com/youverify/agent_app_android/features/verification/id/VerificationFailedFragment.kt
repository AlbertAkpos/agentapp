package com.youverify.agent_app_android.features.verification.id

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.databinding.FragmentVerificationFailedBinding

class VerificationFailedFragment : Fragment(R.layout.fragment_verification_failed) {
    private lateinit var binding : FragmentVerificationFailedBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{

        binding = FragmentVerificationFailedBinding.inflate(layoutInflater)

        binding.tryAgainBtn.setOnClickListener {
            findNavController().navigate(R.id.action_verificationFailedFragment_to_dashboardFragment)
        }

        return binding.root
    }
}