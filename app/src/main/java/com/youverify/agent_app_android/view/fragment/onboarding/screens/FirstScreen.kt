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

class FirstScreen : Fragment() {
    private lateinit var binding : FragmentFirstScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFirstScreenBinding.inflate(layoutInflater)
        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)

        binding.onBoardNextButton.setOnClickListener {
            viewPager?.currentItem = 1
        }

        //move to TCScreen on skip
        binding.textSkip.setOnClickListener {
            findNavController().navigate(R.id.action_viewPagerFragment_to_TCScreen)
        }


        // Inflate the layout for this fragment
        return binding.root
    }


}