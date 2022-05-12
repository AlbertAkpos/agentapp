package com.youverify.agent_app_android.view.fragment.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.databinding.FragmentTCScreenBinding


class TCScreen : Fragment(R.layout.fragment_t_c_screen) {

    private lateinit var binding: FragmentTCScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTCScreenBinding.inflate(layoutInflater)

        binding.buttonAccept.setOnClickListener {
            findNavController().navigate(R.id.action_TCScreen_to_enableLocationFragment)
        }

        binding.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        // Inflate the layout for this fragment
        return binding.root
    }
}