package com.youverify.agent_app_android.view.fragment.onboarding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.model.OnBoardingItem

class OnBoardingItemsAdapter(private val onboardingItems: List<OnBoardingItem>)
    : RecyclerView.Adapter<OnBoardingItemsAdapter.OnBoardingViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardingViewHolder {
        return OnBoardingViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.onboarding_items,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: OnBoardingViewHolder, position: Int) {
        holder.bind(onboardingItems[position])
    }

    override fun getItemCount(): Int {
        return onboardingItems.size
    }

    inner class OnBoardingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val imageOnbarding = itemView.findViewById<ImageView>(R.id.imageOnBoarding)
        private val textTitle = itemView.findViewById<TextView>(R.id.textTitle)
        private val textDescription = itemView.findViewById<TextView>(R.id.textDesc)

        fun bind(onboardingItem: OnBoardingItem){
            imageOnbarding.setImageResource(onboardingItem.image)
            textTitle.text = onboardingItem.title
            textDescription.text = onboardingItem.description
        }
    }
}