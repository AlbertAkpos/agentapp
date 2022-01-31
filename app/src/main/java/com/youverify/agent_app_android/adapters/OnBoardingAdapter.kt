package com.youverify.agent_app_android.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.youverify.agent_app_android.databinding.OnboardingItemsBinding
import com.youverify.agent_app_android.model.OnBoardingItems

class OnBoardingAdapter: RecyclerView.Adapter<OnBoardingAdapter.OnBoardingViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardingViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: OnBoardingViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    inner class OnBoardingViewHolder(val binding: OnboardingItemsBinding):
        RecyclerView.ViewHolder(binding.root){

        fun bind(item: OnBoardingItems){
          binding.textTitle.text = item.title
          binding.textDesc.text = item.description
          binding.imageOnBoarding.setImageResource(item.image)
        }
    }
}