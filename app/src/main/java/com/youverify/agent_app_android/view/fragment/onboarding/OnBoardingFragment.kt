package com.youverify.agent_app_android.view.fragment.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.databinding.FragmentOnboardingBinding
import com.youverify.agent_app_android.model.OnBoardingItem
import kotlin.properties.Delegates


class OnBoardingFragment : Fragment(R.layout.fragment_onboarding) {

    private lateinit var binding: FragmentOnboardingBinding
    private lateinit var onboardingItemsAdapter: OnBoardingItemsAdapter
    private var isFromTCScreen by Delegates.notNull<Boolean>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnboardingBinding.inflate(layoutInflater)

        binding.onBoardBackButton.isVisible = false
        binding.viewPager.isUserInputEnabled = false
        setOnBoardingItems()
        configureNextButton()
        configureBackButton()
        configureSkipText()
        return binding.root
    }

    override fun onStop() {
        super.onStop()
        binding.onBoardBackButton.isVisible = true
    }

    //this function sets the items of the viewPager
    private fun setOnBoardingItems() {
        onboardingItemsAdapter = OnBoardingItemsAdapter(
            listOf(
                OnBoardingItem(
                    image = R.drawable.ic_vp_image_one,
                    title = "Easily manage Tasks",
                    description = "Slide right to accept a \ntask or slide left to decline"
                ),
                OnBoardingItem(
                    image = R.drawable.ic_vp_image_two,
                    title = "Real-time tracking",
                    description = "The app identifies your location with the \nGeo-tag feature while on duty."
                ),
                OnBoardingItem(
                    image = R.drawable.ic_vp_image_three,
                    title = "Earn money",
                    description = "Smile to the bank when you \nwork as our agent."
                ),
                OnBoardingItem(
                    image = R.drawable.ic_vp_image_four,
                    title = "Get started",
                    description = "To get your first task, choose your location \nto request for task around you."
                )
            )
        )

        binding.viewPager.adapter = onboardingItemsAdapter
    }

    //this function listens to next button
    private fun configureNextButton() {
        binding.onBoardNextButton.setOnClickListener {
            when (binding.viewPager.currentItem) {
                0 -> {
                    binding.viewPager.currentItem = 1
                    binding.onBoardProgressBar.progress = 50
                    binding.onBoardBackButton.isVisible = true
                }
                1 -> {
                    binding.viewPager.currentItem = 2
                    binding.onBoardProgressBar.progress = 75
                }
                2 -> {
                    binding.viewPager.currentItem = 3
                    binding.onBoardProgressBar.progress = 100
                }
                else -> {
                    findNavController().navigate(R.id.action_viewPagerFragment_to_TCScreen)
                }
            }
        }
    }

    //this function listens to back button
    private fun configureBackButton() {
        binding.onBoardBackButton.setOnClickListener {
            when (binding.viewPager.currentItem) {
                1 -> {
                    binding.onBoardBackButton.isVisible = false
                    binding.viewPager.currentItem = 0
                    binding.onBoardProgressBar.progress = 25
                }
                2 -> {
                    binding.viewPager.currentItem = 1
                    binding.onBoardProgressBar.progress = 50
                }
                3 -> {
                    binding.viewPager.currentItem = 2
                    binding.onBoardProgressBar.progress = 75
                }
            }
        }
    }


    private fun configureSkipText() {
        binding.textSkip.setOnClickListener {
            findNavController().navigate(R.id.action_viewPagerFragment_to_TCScreen)
        }
    }
}