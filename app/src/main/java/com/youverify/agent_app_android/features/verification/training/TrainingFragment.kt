package com.youverify.agent_app_android.features.verification.training

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.youverify.agent_app_android.databinding.FragmentTrainingBinding
import com.youverify.agent_app_android.features.HomeActivity

class TrainingFragment : Fragment() {

    private lateinit var binding : FragmentTrainingBinding
    private lateinit var  webView : WebView
    private lateinit var homeActivity : HomeActivity


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{

        binding = FragmentTrainingBinding.inflate(layoutInflater)
        homeActivity = requireActivity() as HomeActivity
        webView = binding.webView

        val webSettings = webView.settings
        true.also { webSettings.javaScriptEnabled = it }

        //webView performance improve
        webView.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        webView.settings.domStorageEnabled = true
        webView.settings.setAppCacheEnabled(true)
        webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        webView.settings.builtInZoomControls = true
        webView.settings.displayZoomControls = false

        webSettings.domStorageEnabled = true
        webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING
        webSettings.useWideViewPort = true
        webSettings.savePassword = true
        webSettings.saveFormData = true
        webSettings.setEnableSmoothTransition(true)

        webView.loadUrl("https://agent-training.dev.youverify.co/")
        webView.webViewClient = WebViewClient()

        displayTrainingPortal()

        return binding.root
    }

    private fun displayTrainingPortal(){
       if (webView.isFocused && webView.canGoBack()) webView.goBack()
    }

    override fun onStop() {
        super.onStop()
        homeActivity.showNavBar()
    }

    override fun onResume() {
        super.onResume()
        homeActivity.removeNavBar()
    }
}