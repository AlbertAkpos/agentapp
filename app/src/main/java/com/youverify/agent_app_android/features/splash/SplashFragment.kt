package com.youverify.agent_app_android.features.splash

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.databinding.FragmentSplashBinding
import com.youverify.agent_app_android.features.HomeActivity
import com.youverify.agent_app_android.util.AgentSharePreference

class SplashFragment : Fragment(R.layout.fragment_splash) {
    private lateinit var binding: FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashBinding.inflate(layoutInflater)

        //carefully handle user onboarding.
        Handler(Looper.getMainLooper()).postDelayed({
            if(onBoardingFinished()){ //we have finished onboarding, go to login
                if(tokenIsValid()) {    //but check if we have logged in before, go to home
                    startActivity(Intent(requireContext(), HomeActivity::class.java))
                    activity?.finish()
                }else{
                    findNavController().navigate(R.id.action_splashFragment_to_LoginScreen)
                }
            }else {    //onboard user
                findNavController().navigate(R.id.action_splashFragment_to_onboardingFragment)
            }
        }, 1500)

        // Inflate the layout for this fragment
        return binding.root
    }

    //write to sharedPref if onboarding has finished.
    private fun onBoardingFinished(): Boolean{
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("Finished", false)
    }

    //getting the token from sharedPreferences
    private fun tokenIsValid(): Boolean{
        return AgentSharePreference(requireContext()).getString("TOKEN") != ""
    }
}