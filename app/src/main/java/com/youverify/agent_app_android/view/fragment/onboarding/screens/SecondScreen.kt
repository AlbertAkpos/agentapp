package com.youverify.agent_app_android.view.fragment.onboarding.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.databinding.FragmentFirstScreenBinding
import com.youverify.agent_app_android.databinding.FragmentSecondScreenBinding

class SecondScreen : Fragment() {
    private lateinit var binding: FragmentSecondScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSecondScreenBinding.inflate(layoutInflater)
        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)

        binding.onBoardNextButton.setOnClickListener {
            viewPager?.currentItem = 2
        }

        //move to TCScreen on skip
        binding.textSkip.setOnClickListener {
            findNavController().navigate(R.id.action_viewPagerFragment_to_TCScreen)
        }

        //move to the previous screen on back button press
        binding.onBoardBackButton.setOnClickListener {
            viewPager?.currentItem = 0
        }

        // Inflate the layout for this fragment
        return binding.root
    }
}