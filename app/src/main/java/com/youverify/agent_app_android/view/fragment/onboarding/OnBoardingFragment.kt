package com.youverify.agent_app_android.view.fragment.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.databinding.FragmentOnboardingBinding
import com.youverify.agent_app_android.model.OnBoardingItem
import com.youverify.agent_app_android.view.fragment.onboarding.OnBoardingItemsAdapter
import kotlin.math.abs


class OnBoardingFragment : Fragment(R.layout.fragment_onboarding), ViewPager2.PageTransformer {

    private val IS_DONE_VIEWING = "Is viewing"
    private var isFromTCScreen: Boolean = false
    private lateinit var binding: FragmentOnboardingBinding
    private lateinit var onboardingItemsAdapter: OnBoardingItemsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnboardingBinding.inflate(layoutInflater)
        if (savedInstanceState != null) {
            isFromTCScreen = savedInstanceState.getBoolean(IS_DONE_VIEWING, false)
        }

        binding.onboardBackBtn.isVisible = isFromTCScreen
        binding.viewPager.isUserInputEnabled = false
        binding.viewPager.setPageTransformer(this)

        setOnBoardingItems()
        configureNextButton()
        configureBackButton()
        configureSkipText()

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(IS_DONE_VIEWING, isFromTCScreen)
    }

    //this function sets the items of the viewPager
    private fun setOnBoardingItems() {
        val onboardingItems = listOf(
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

        onboardingItemsAdapter = OnBoardingItemsAdapter()
        onboardingItemsAdapter.setList(onboardingItems)
        binding.viewPager.adapter = onboardingItemsAdapter
    }

    //this function listens to next button
    private fun configureNextButton() {
        binding.onboardNextBtn.setOnClickListener {
            when (binding.viewPager.currentItem) {
                0 -> {
                    binding.viewPager.currentItem = 1
                    binding.onboardProgressBar.progress = 50
                    binding.onboardBackBtn.isVisible = true
                }
                1 -> {
                    binding.viewPager.currentItem = 2
                    binding.onboardProgressBar.progress = 75
                }
                2 -> {
                    binding.viewPager.currentItem = 3
                    binding.onboardProgressBar.progress = 100
                }
                else -> {
                    findNavController().navigate(R.id.action_viewPagerFragment_to_TCScreen)
                    isFromTCScreen = true
                }
            }
        }
    }

    //this function listens to back button
    private fun configureBackButton() {
        binding.onboardBackBtn.setOnClickListener {
            when (binding.viewPager.currentItem) {
                1 -> {
                    binding.onboardBackBtn.isVisible = false
                    binding.viewPager.currentItem = 0
                    binding.onboardProgressBar.progress = 25
                }
                2 -> {
                    binding.viewPager.currentItem = 1
                    binding.onboardProgressBar.progress = 50
                }
                3 -> {
                    binding.viewPager.currentItem = 2
                    binding.onboardProgressBar.progress = 75
                }
            }
        }
    }


    private fun configureSkipText() {
        binding.textSkip.setOnClickListener {
            findNavController().navigate(R.id.action_viewPagerFragment_to_TCScreen)
        }
    }

    //pop animation for view pager
    override fun transformPage(page: View, position: Float) {
        page.translationX = -position * page.width

                if (abs(position) < 0.5) {
            page.visibility = View.VISIBLE
            page.scaleX = 1 - abs(position)
            page.scaleY = 1 - abs(position)
        } else if (abs(position) > 0.5) {
            page.visibility = View.GONE
        }
    }
}