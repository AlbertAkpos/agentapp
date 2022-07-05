package com.youverify.agent_app_android.features.verification.id

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.databinding.FragmentVerificationSuccessBinding
import com.youverify.agent_app_android.features.HomeActivity

class VerificationResponseFragment : Fragment(R.layout.fragment_verification_success) {

    private lateinit var binding : FragmentVerificationSuccessBinding
    private lateinit var homeActivity: HomeActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{

        binding = FragmentVerificationSuccessBinding.inflate(layoutInflater)
        homeActivity = requireActivity() as HomeActivity

        binding.okBtn.setOnClickListener {
            findNavController().navigate(R.id.action_verificationResponseFragment_to_dashboardFragment)
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        homeActivity.removeNavBar()
    }
}