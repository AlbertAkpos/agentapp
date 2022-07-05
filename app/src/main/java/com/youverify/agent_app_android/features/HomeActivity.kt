package com.youverify.agent_app_android.features

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.data.model.verification.refresh_token.TokenRequest
import com.youverify.agent_app_android.databinding.ActivityMainBinding
import com.youverify.agent_app_android.features.verification.reset_token.TokenViewModel
import com.youverify.agent_app_android.features.verification.reset_token.TokenViewState
import com.youverify.agent_app_android.util.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    @Inject
    lateinit var progressLoader: ProgressLoader
    private lateinit var binding: ActivityMainBinding
    private val tokenViewModel: TokenViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionBar?.hide()
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)

        navView.labelVisibilityMode =
            NavigationBarView.LABEL_VISIBILITY_LABELED        //set the bottom navigation to always be labelled
    }

    fun showNavBar() {
        val bottomNav = binding.navView
        bottomNav.showNavBar()
    }

    fun removeNavBar() {
        val bottomNav = binding.navView
        bottomNav.removeNavBar()
    }

    override fun onResume() {
        super.onResume()

        refreshToken()
    }

    private fun refreshToken() {
        val refreshToken = AgentSharePreference(this).getString(SharedPrefKeys.REFRESH_TOKEN)
        val agentId = AgentSharePreference(this).getString(SharedPrefKeys.AGENT_ID)

        val tokenRequest = TokenRequest(refreshToken, agentId)

        tokenViewModel.refreshToken(tokenRequest = tokenRequest)

        lifecycleScope.launchWhenCreated {
            tokenViewModel.tokenChannel.collect {
                when (it) {
                    is TokenViewState.Loading -> {
                        progressLoader.show(message = "Please wait...")
                    }
                    is TokenViewState.Success -> {
                        progressLoader.hide()
                        AgentSharePreference(this@HomeActivity).setString(
                            SharedPrefKeys.REFRESH_TOKEN,
                            it.tokenResponse?.data?.refreshToken!!
                        )
                        AgentSharePreference(this@HomeActivity).setString(
                            SharedPrefKeys.TOKEN,
                            it.tokenResponse.data.accessToken
                        )
                    }
                    is TokenViewState.Failure -> {
                        progressLoader.hide()
                        Toast.makeText(this@HomeActivity, it.errorMessage, Toast.LENGTH_LONG)
                            .show()
                    }
                    else -> {}
                }
            }
        }
    }
}