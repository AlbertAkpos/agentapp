package com.youverify.agent_app_android.view.fragment.home.identity_verification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.databinding.FragmentVerificationFailedBinding
import com.youverify.agent_app_android.databinding.FragmentVerificationSuccessBinding

class VerificationResponseFragment : Fragment(R.layout.fragment_verification_success) {

    private lateinit var binding : FragmentVerificationSuccessBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{

        binding = FragmentVerificationSuccessBinding.inflate(layoutInflater)

        return binding.root
    }

}