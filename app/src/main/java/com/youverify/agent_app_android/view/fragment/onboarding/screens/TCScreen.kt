package com.youverify.agent_app_android.view.fragment.onboarding.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.databinding.FragmentTCScreenBinding

class TCScreen : Fragment() {

    private lateinit var binding: FragmentTCScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTCScreenBinding.inflate(layoutInflater)

        binding.button.setOnClickListener {
          findNavController().navigate(R.id.action_TCScreen_to_LoginScreen)
        }

        // Inflate the layout for this fragment
        return binding.root
    }

}