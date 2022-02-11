package com.youverify.agent_app_android.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.youverify.agent_app_android.databinding.OnboardingItemsBinding

open class OnBoardingItemsActivity : AppCompatActivity() {
    private lateinit var binding: OnboardingItemsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = OnboardingItemsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}