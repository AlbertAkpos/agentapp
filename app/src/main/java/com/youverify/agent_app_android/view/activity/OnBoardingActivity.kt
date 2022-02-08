package com.youverify.agent_app_android.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.adapters.OnBoardingAdapter
import com.youverify.agent_app_android.databinding.ActivityOnboardingBinding
import com.youverify.agent_app_android.model.OnBoardingItems


private lateinit var binding: ActivityOnboardingBinding

class OnBoardingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)

        val onboardingViewPager = binding.onBoardingViewPager
        onboardingViewPager.adapter = setOnBoardingItems()

    }

    private fun setOnBoardingItems(): OnBoardingAdapter {
        val onboardingItems = arrayListOf<OnBoardingItems>()

        val items1 = OnBoardingItems(
            title = "Easily manage tasks",
            description = "Slide right to accept a task or slide left to decline.",
            image = R.drawable.ic_vp_image_one
        )

        val items2 = OnBoardingItems(
            title = "Real-time tracking",
            description = "The app identifies your location with the Geo-tag feature while on duty.",
            image = R.drawable.ic_vp_image_two
        )

        val items3 = OnBoardingItems(
            title = "Earn money",
            description = "Smile to the bank when you work as our agent.",
            image = R.drawable.ic_vp_image_three
        )

        val items4 = OnBoardingItems(
            title = "Get started",
            description = "To get your first task, choose your location to request for task around you.",
            image = R.drawable.ic_vp_image_four
        )

        onboardingItems.add(items1)
        onboardingItems.add(items2)
        onboardingItems.add(items3)
        onboardingItems.add(items4)

        return OnBoardingAdapter(onboardingItems, this)
    }
}