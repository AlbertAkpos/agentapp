package com.youverify.agent_app_android.features.verification.areas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.databinding.FragmentSaveAreasBinding


class SaveAreasFragment : Fragment(R.layout.fragment_save_areas) {

    private lateinit var binding : FragmentSaveAreasBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentSaveAreasBinding.inflate(layoutInflater)

        binding.okBtn.setOnClickListener {
            findNavController().navigate(R.id.action_saveAreasFragment_to_dashboardFragment)
        }
        return binding.root
    }

}