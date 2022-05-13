package com.youverify.agent_app_android.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.databinding.ActivityMainBinding
import com.youverify.agent_app_android.util.removeNavBar
import com.youverify.agent_app_android.util.showNavBar

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionBar?.hide()
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)

        navView.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_LABELED        //set the bottom navigation to always be labelled
    }

    fun showNavBar(){
        val bottomNav = binding.navView
        bottomNav.showNavBar()
    }

    fun removeNavBar(){
        val bottomNav = binding.navView
        bottomNav.removeNavBar()
    }
}