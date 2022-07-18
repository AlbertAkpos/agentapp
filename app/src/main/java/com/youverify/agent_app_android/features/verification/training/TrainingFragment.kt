package com.youverify.agent_app_android.features.verification.training

import android.app.Activity
import android.os.Bundle
import android.util.Base64.encodeToString
import java.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.youverify.agent_app_android.BuildConfig
import com.youverify.agent_app_android.databinding.FragmentTrainingBinding
import com.youverify.agent_app_android.features.HomeActivity
import com.youverify.agent_app_android.util.AgentSharePreference
import com.youverify.agent_app_android.util.SharedPrefKeys

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

        configureWebView()

        return binding.root
    }


    private fun configureWebView(){
        val webSettings = webView.settings
        true.also {
            webSettings.javaScriptEnabled = it
            webSettings.javaScriptCanOpenWindowsAutomatically = it
            webSettings.domStorageEnabled = it
        }

        val encodedToken = encodeString(AgentSharePreference(requireContext()).getString(SharedPrefKeys.REFRESH_TOKEN))
        val encodedAgentId = encodeString(AgentSharePreference(requireContext()).getString(SharedPrefKeys.AGENT_ID))

        webView.loadUrl("${BuildConfig.AGENT_TRAINING_PORTAL}?encodedData={\"token\":\"$encodedToken\", \"agentId\":\"$encodedAgentId\"}")
        webView.webViewClient = CustomWebViewClient(requireActivity())

        displayTrainingPortal()
    }

    class CustomWebViewClient(private val activity: Activity): WebViewClient(){
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            return false
        }
    }


    private fun displayTrainingPortal(){
       if (webView.isFocused && webView.canGoBack()) webView.goBack()
    }

    private fun encodeString(value: String): String {
        return Base64.getEncoder().encodeToString(value.toByteArray())
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