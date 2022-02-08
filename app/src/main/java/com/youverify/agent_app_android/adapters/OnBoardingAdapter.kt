package com.youverify.agent_app_android.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.youverify.agent_app_android.databinding.ActivityOnboardingBinding
import com.youverify.agent_app_android.databinding.OnboardingItemsBinding
import com.youverify.agent_app_android.model.OnBoardingItems
import com.youverify.agent_app_android.view.activity.MainActivity
import com.youverify.agent_app_android.view.activity.OnBoardingItemsActivity

class OnBoardingAdapter(private val onboardingItems: ArrayList<OnBoardingItems>, var context:Context) :
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
//            binding.onBoardBackButton.isVisible = item != onboardingItems[0]

            //change the various levels for the progressbar on different items
//            when(item){
//                onboardingItems[0] -> binding.onBoardProgressBar.progress = 25
//                onboardingItems[1] -> binding.onBoardProgressBar.progress = 50
//                onboardingItems[2] -> binding.onBoardProgressBar.progress = 75
//                onboardingItems[3] -> binding.onBoardProgressBar.progress = 100
//                else -> binding.onBoardProgressBar.progress = 0
//            }
//
//            binding.textSkip.setOnClickListener {
//                context.startActivity(Intent(context, MainActivity::class.java))
//            }
//            binding.onBoardBackButton.setOnClickListener {
//                bind(item = onboardingItems[onboardingItems.indexOf(item) - 1])
//            }

            //make sure you make moving to the next screen work, and also the fixing od the onboarding items on the screen.
            //look at click listener for skip, next and back buttons.
//            binding.onBoardNextButton.setOnClickListener{
//                setNextItem(onboardingItems.indexOf(item) + 1)
//                if(onboardingItems.indexOf(item) + 1 <= itemCount - 1){
//                    bind(item = onboardingItems[onboardingItems.indexOf(item) + 1])
//                    onBindViewHolder(this, onboardingItems.indexOf(item) + 1)
//                }else {
//                    //move to next screen
//                    context.startActivity(Intent(context, MainActivity::class.java))
//                }
//            }
        }

        //method to handle next button for onboarding
//        private fun setNextItem(count: Int){
//
//        }
    }
}