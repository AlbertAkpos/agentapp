package com.youverify.agent_app_android.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.youverify.agent_app_android.databinding.OnboardingItemsBinding

class OnBoardingItemsActivity : AppCompatActivity() {
    private lateinit var binding: OnboardingItemsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = OnboardingItemsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // we want to go back to the previous item on the list if we hit the back button
        binding.onBoardBackButton.setOnClickListener {
            // call getItem() function

        }

        // we want to go to the main activity if we hit the skip text
        binding.textSkip.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            println("clicked skip")
        }

        // we want to go to the main activity if we hit the skip text
        binding.onBoardNextButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            println("clicked next")
        }
    }
}