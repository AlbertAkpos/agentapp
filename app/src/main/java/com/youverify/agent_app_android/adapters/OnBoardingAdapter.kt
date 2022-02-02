package com.youverify.agent_app_android.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.youverify.agent_app_android.R
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
            //we want to remove the back button when the first onboarding item shows up in the viewpager.
            binding.onBoardBackButton.isVisible = item != onboardingItems[0]
            //we want to change the various leves for the progressbar on different items
            when(item){
                onboardingItems[0] ->
                    binding.onBoardProgressBar.progress = 25
                    //add coresponding color
                onboardingItems[1] ->
                    binding.onBoardProgressBar.progress = 50
                    //add coresponding color
                onboardingItems[2] ->
                    binding.onBoardProgressBar.progress = 75
                   //add coresponding color
                onboardingItems[3] ->
                    binding.onBoardProgressBar.progress = 100
                   //add coresponding color
                else -> binding.onBoardProgressBar.progress = 0
            }


        }
    }
}