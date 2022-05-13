package com.youverify.agent_app_android.presentation.onboarding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.databinding.OnboardingItemsBinding
import com.youverify.agent_app_android.data.model.OnBoardingItem

class OnBoardingItemsAdapter()
    : RecyclerView.Adapter<OnBoardingViewHolder>(){

    private val onboardingItemList = ArrayList<OnBoardingItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardingViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding : OnboardingItemsBinding = DataBindingUtil.inflate(layoutInflater, R.layout.onboarding_items, parent, false)
        return OnBoardingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OnBoardingViewHolder, position: Int) {
        holder.bind(onboardingItemList[position])
    }

    override fun getItemCount(): Int {
        return onboardingItemList.size
    }

    fun setList(itemList: List<OnBoardingItem>){
        onboardingItemList.addAll(itemList)
    }
}


class OnBoardingViewHolder(val binding : OnboardingItemsBinding) : RecyclerView.ViewHolder(binding.root){
    private val imageOnbarding = binding.imageOnBoarding
    private val textTitle = binding.textTitle
    private val textDescription = binding.textDesc

    fun bind(onboardingItem: OnBoardingItem){
        imageOnbarding.setImageResource(onboardingItem.image)
        textTitle.text = onboardingItem.title
        textDescription.text = onboardingItem.description
    }
}