package com.youverify.agent_app_android.features.verification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.databinding.FragmentSelectAreasBinding
import com.youverify.agent_app_android.features.HomeActivity

class  SelectAreasFragment : Fragment(R.layout.fragment_select_areas) {

    private lateinit var binding: FragmentSelectAreasBinding
    private lateinit var homeActivity: HomeActivity
    private lateinit var chipGroup: ChipGroup
    private lateinit var autoCompText: AutoCompleteTextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentSelectAreasBinding.inflate(layoutInflater)
        homeActivity = requireActivity() as HomeActivity
        autoCompText = binding.selectAreasEditText

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureUI()
    }

    private fun configureUI(){
        //passing the array adapter for states in the autocomplete textview
        autoCompText.dropDownHorizontalOffset = 550
        val areas = resources.getStringArray(R.array.areas)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.areas_drop_down_item, areas)
        autoCompText.setAdapter(arrayAdapter)
        autoCompText.setOnClickListener {
            autoCompText.showDropDown()
        }

        binding.doneBtn.setOnClickListener {
            homeActivity.removeNavBar()
            findNavController().navigate(R.id.action_selectAreasFragment_to_saveAreasFragment)
        }

        binding.backBtn.setOnClickListener {
            activity?.onBackPressed()
        }
    }


    private fun addChip(text: String){
        val chip = Chip(requireContext())
        chip.text = text

        chip.isCloseIconVisible = true

        chip.setChipIconResource(R.drawable.ic_close)

        chip.setOnCloseIconClickListener{
            chipGroup.removeView(chip)
        }

        chipGroup.addView(chip)
    }

    override fun onStart() {
        super.onStart()
        homeActivity.showNavBar()
    }
}