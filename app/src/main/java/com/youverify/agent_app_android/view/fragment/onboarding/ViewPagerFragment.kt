package com.youverify.agent_app_android.view.fragment.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.youverify.agent_app_android.databinding.FragmentViewPagerBinding
import com.youverify.agent_app_android.view.fragment.onboarding.screens.FirstScreen
import com.youverify.agent_app_android.view.fragment.onboarding.screens.FourthScreen
import com.youverify.agent_app_android.view.fragment.onboarding.screens.SecondScreen
import com.youverify.agent_app_android.view.fragment.onboarding.screens.ThirdScreen


class ViewPagerFragment : Fragment() {

    private lateinit var binding: FragmentViewPagerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewPagerBinding.inflate(layoutInflater)

        // add all the fragment screens to the list
        val fragmentList = arrayListOf(
            FirstScreen(),
            SecondScreen(),
            ThirdScreen(),
            FourthScreen()
        )

        val adapter = ViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        binding.viewPager.adapter = adapter

        return binding.root
    }

}