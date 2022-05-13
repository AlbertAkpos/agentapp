package com.youverify.agent_app_android.presentation.verification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.databinding.FragmentUploadPassportBinding


class UploadPassportFragment : Fragment(R.layout.fragment_upload_passport) {

    private lateinit var binding : FragmentUploadPassportBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentUploadPassportBinding.inflate(layoutInflater)

        configureUI()

        return binding.root
    }

    private fun configureUI(){
        binding.verifyBtn.setOnClickListener {
            findNavController().navigate(R.id.action_uploadPassportFragment_to_verificationResponseFragment)
        }

        binding.backBtn.setOnClickListener {
            activity?.onBackPressed()
        }
    }
}