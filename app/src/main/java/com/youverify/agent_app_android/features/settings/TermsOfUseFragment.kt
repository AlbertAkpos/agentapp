package com.youverify.agent_app_android.features.settings

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.youverify.agent_app_android.BuildConfig
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.databinding.FragmentPrivacyAndTermsBinding
import com.youverify.agent_app_android.features.HomeActivity
import com.youverify.agent_app_android.features.verification.id.UploadImageFragmentArgs
import com.youverify.agent_app_android.features.verification.training.TrainingFragment
import com.youverify.agent_app_android.util.AgentSharePreference
import com.youverify.agent_app_android.util.SharedPrefKeys

class TermsOfUseFragment : Fragment() {

    private lateinit var binding: FragmentPrivacyAndTermsBinding
    private lateinit var  webView : WebView
    private lateinit var homeActivity : HomeActivity
    private val args: TermsOfUseFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentPrivacyAndTermsBinding.inflate(layoutInflater)

        homeActivity = requireActivity() as HomeActivity
        webView = binding.webView

        configureWebView()
        return binding.root
    }

    override fun onStop() {
        super.onStop()
        homeActivity.showNavBar()
    }

    override fun onResume() {
        super.onResume()
        homeActivity.removeNavBar()
    }

    private fun configureWebView(){
        val webSettings = webView.settings
        true.also { webSettings.javaScriptEnabled = it }
        webView.webViewClient = CustomWebViewClient()

        if (args.value.isTerms) webView.loadUrl("https://youverify.co/terms-of-use")
        else webView.loadUrl("https://youverify.co/privacy-policy")
    }

    class CustomWebViewClient : WebViewClient(){
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            return false
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            view?.loadUrl("javascript:(function() { " +
                    "var head = document.getElementsByClassName('bg-white max-w-screen-xl mx-auto sm:px-8 px-6')[0].style.display='none'; " +
                    "var footer = document.getElementsByClassName('bg-blue-100 pt-12 pb-12')[0].style.display='none';})()")
        }
    }

}