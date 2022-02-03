package com.youverify.agent_app_android.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.youverify.agent_app_android.databinding.ActivityOnboardingBinding
import com.youverify.agent_app_android.databinding.OnboardingItemsBinding
import com.youverify.agent_app_android.model.OnBoardingItems

class OnBoardingAdapter(private val onboardingItems: ArrayList<OnBoardingItems>) :
    RecyclerView.Adapter<OnBoardingAdapter.OnBoardingViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardingViewHolder {
        return OnBoardingViewHolder(
            OnboardingItemsBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: OnBoardingViewHolder, position: Int) {
        holder.apply {
            bind(onboardingItems[position])
        }
    }

    override fun getItemCount(): Int {
        return onboardingItems.size
    }

    inner class OnBoardingViewHolder(private val binding: OnboardingItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: OnBoardingItems) {
            binding.textTitle.text = item.title
            binding.textDesc.text = item.description
            binding.imageOnBoarding.setImageResource(item.image)

            //remove the back button when the first onboarding item shows up in the viewpager.
            binding.onBoardBackButton.isVisible = item != onboardingItems[0]

            //change the various levels for the progressbar on different items
            when(item){
                onboardingItems[0] -> {
                    binding.onBoardProgressBar.progress = 25
                    binding.onBoardNextButton.isClickable = false
                }
                onboardingItems[1] -> {
                    binding.onBoardProgressBar.progress = 50
                    binding.onBoardNextButton.isClickable = false
                }
                onboardingItems[2] -> {
                    binding.onBoardProgressBar.progress = 75
                    binding.onBoardNextButton.isClickable = false
                }
                onboardingItems[3] -> {
                    binding.onBoardProgressBar.progress = 100
                    binding.onBoardNextButton.isClickable = true
                }
                else -> {
                    binding.onBoardProgressBar.progress = 0
                    binding.onBoardNextButton.isClickable = false
                }
            }
        }
    }
}