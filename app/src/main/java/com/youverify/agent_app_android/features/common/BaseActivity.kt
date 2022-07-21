package com.youverify.agent_app_android.features.common

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment

abstract class BaseActivity: AppCompatActivity() {
    protected val navController by lazy {   val navHostFragment =
        supportFragmentManager.findFragmentById(getNavHostId()) as NavHostFragment
        navHostFragment.navController }

    abstract fun getNavHostId(): Int

    open fun setStartDestination() {
        val graphInflater = navController.navInflater
        val navGraph = graphInflater.inflate(navigationId)
        navGraph.setStartDestination(getStartDesination())
        navController.graph = navGraph
    }

    abstract fun getStartDesination(): Int
    abstract val navigationId: Int

}