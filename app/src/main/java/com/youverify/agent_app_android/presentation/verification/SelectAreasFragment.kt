package com.youverify.agent_app_android.presentation.verification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.databinding.FragmentSelectAreasBinding

class  SelectAreasFragment : Fragment(R.layout.fragment_select_areas) {

    private lateinit var binding: FragmentSelectAreasBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentSelectAreasBinding.inflate(layoutInflater)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureUI()
    }

    private fun configureUI(){
        //passing the array adapter for states in the autocomplete textview
        val areas = resources.getStringArray(R.array.areas)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.areas_drop_down_item, areas)
        binding.selectAreasEditText.setAdapter(arrayAdapter)
        binding.selectAreasEditText.setOnClickListener {
            binding.selectAreasEditText.showDropDown()
        }

        binding.doneBtn.setOnClickListener {
            findNavController().navigate(R.id.action_selectAreasFragment_to_saveAreasFragment)
        }

        binding.backBtn.setOnClickListener {
            activity?.onBackPressed()
        }
    }
}