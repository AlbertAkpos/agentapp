package com.youverify.agent_app_android.view.fragment.onboarding.screens

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.databinding.FragmentEnableLocationBinding

class EnableLocationFragment : Fragment(R.layout.fragment_enable_location) {

    private lateinit var binding : FragmentEnableLocationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{

        binding = FragmentEnableLocationBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ensurePermissions()

        binding.verifyButton.setOnClickListener {
            onBoardingFinished()
            findNavController().navigate(R.id.action_enableLocationFragment_to_LoginScreen)
        }

        binding.verifyBackBtn.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun ensurePermissions() {
        binding.switchPushNotifications.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                if (binding.switchLocAccess.isChecked &&
                    binding.switchPhotos.isChecked &&
                    binding.switchCamera.isChecked){
                    binding.verifyButton.visibility = View.VISIBLE
                }
            }else{
                binding.verifyButton.visibility = View.GONE
            }
        }
    }

    private fun onBoardingFinished(){
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("Finished", true)
        editor.apply()
    }
}