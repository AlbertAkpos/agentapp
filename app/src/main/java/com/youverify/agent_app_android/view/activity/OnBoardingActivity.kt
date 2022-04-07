package com.youverify.agent_app_android.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.youverify.agent_app_android.databinding.ActivityOnboardingBinding

class OnBoardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
    }
}