package com.youverify.agent_app_android.view.fragment.onboarding.screens

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.databinding.FragmentFourthScreenBinding
import com.youverify.agent_app_android.databinding.FragmentThirdScreenBinding

class FourthScreen : Fragment() {
      private lateinit var binding: FragmentFourthScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFourthScreenBinding.inflate(layoutInflater)
        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)

        //move to TCScreen screen
        binding.onBoardNextButton.setOnClickListener {
            findNavController().navigate(R.id.action_viewPagerFragment_to_TCScreen)
            onBoardingFinished()
        }

        //move to TCScreen on skip
        binding.textSkip.setOnClickListener {
            findNavController().navigate(R.id.action_viewPagerFragment_to_TCScreen)
            onBoardingFinished()
        }

        //move to the previous screen on back button press
        binding.onBoardBackButton.setOnClickListener {
            viewPager?.currentItem = 2
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun onBoardingFinished(){
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("Finished", true)
        editor.apply()
    }

}