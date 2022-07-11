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

        val webSettings = webView.settings
        true.also {
            webSettings.javaScriptEnabled = it
            webSettings.javaScriptCanOpenWindowsAutomatically = it
        }

        //webView performance improvement
        webSettings.domStorageEnabled = true
//        webSettings.builtInZoomControls = true
//        webSettings.displayZoomControls = false
//        webSettings.useWideViewPort = true
//        webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
//        webSettings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
//        webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING
//        webSettings.savePassword = true
//        webSettings.saveFormData = true
//        webSettings.setAppCacheEnabled(true)
//        webSettings.setEnableSmoothTransition(true)

        val encodedToken = encodeString(AgentSharePreference(requireContext()).getString(SharedPrefKeys.REFRESH_TOKEN))
        val encodedAgentId = encodeString(AgentSharePreference(requireContext()).getString(SharedPrefKeys.AGENT_ID))

        webView.loadUrl("http://agent-training.dev.youverify.co/?encodedData={\"token\":\"$encodedToken\", \"agentId\":\"$encodedAgentId\"}")
        webView.webViewClient = CustomWebViewClient(requireActivity())

        displayTrainingPortal()

        return binding.root
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
//        encodeToString(value.toByteArray(), 0)
    }

    private fun decodeString(value: String){
        val decoder: Base64.Decoder = Base64.getDecoder()
        val decoded = String(decoder.decode(value))
        println("Decoded Data: $decoded")
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