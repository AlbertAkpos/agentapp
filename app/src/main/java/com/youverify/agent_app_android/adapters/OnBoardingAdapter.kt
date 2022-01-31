package com.youverify.agent_app_android.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.youverify.agent_app_android.databinding.OnboardingItemsBinding
import com.youverify.agent_app_android.model.OnBoardingItems

class OnBoardingAdapter(private val onboardingItems : ArrayList<OnBoardingItems>): RecyclerView.Adapter<OnBoardingAdapter.OnBoardingViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardingViewHolder {
        return OnBoardingViewHolder(OnboardingItemsBinding
            .inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: OnBoardingViewHolder, position: Int) {
        holder.apply {
            bind(onboardingItems[position])
        }
    }

    override fun getItemCount(): Int {
        return onboardingItems.size
    }

    inner class OnBoardingViewHolder(private val binding: OnboardingItemsBinding):
        RecyclerView.ViewHolder(binding.root){

        fun bind(item: OnBoardingItems){
          binding.textTitle.text = item.title
          binding.textDesc.text = item.description
          binding.imageOnBoarding.setImageResource(item.image)
        }
    }
}