package com.youverify.agent_app_android.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.youverify.agent_app_android.databinding.OnboardingItemsBinding

open class OnBoardingItemsActivity : AppCompatActivity() {
    private lateinit var binding: OnboardingItemsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = OnboardingItemsBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.textSkip.setOnClickListener {
//            startActivity(Intent(applicationContext, MainActivity::class.java))
//            finish()
//        }

//        binding.onBoardNextButton.setOnClickListener {
//            startActivity(Intent(applicationContext, MainActivity::class.java))
//            finish()
//        }
    }

    // we want to go to the main activity if we hit the skip text
    fun onClickButton(){
        startActivity(Intent(applicationContext, MainActivity::class.java))
        finish()
    }
}