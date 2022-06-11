package com.youverify.agent_app_android.features.verification.areas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.databinding.FragmentSelectAreasBinding
import com.youverify.agent_app_android.features.HomeActivity
import com.youverify.agent_app_android.util.AgentSharePreference
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class  SelectAreasFragment : Fragment(R.layout.fragment_select_areas) {

    private lateinit var binding: FragmentSelectAreasBinding
    private val selectAreaViewModel : SelectAreasViewModel by viewModels()
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
        chipGroup = binding.chipGroup

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureUI()
        fetchAreas()
    }

    private fun configureUI(){
//        autoCompText.dropDownHorizontalOffset = 550
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

    private fun sendSelectedAreas(){

    }

    private fun fetchAreas(){
        val state = AgentSharePreference(requireContext()).getString("STATE_OF_RESIDENCE")
        val responseData = selectAreaViewModel.getStateLgas(state.lowercase(Locale.getDefault()))

        responseData.observe(requireActivity()){
            if (!it.isNullOrEmpty()){
                val arrayAdapter = ArrayAdapter(requireContext(), R.layout.areas_drop_down_item, it)
                for (lga in it){
                    addChip(lga)
                }
                autoCompText.setAdapter(arrayAdapter)
            }
        }
    }

    private fun addChip(text: String){
        val chip = Chip(requireContext())
        chip.text = text
        chip.textSize = 12F
        chip.closeIconSize = 24F
        chip.chipCornerRadius = 8F
        chip.isCloseIconVisible = true
        chip.iconStartPadding = 0F
        chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        chip.closeIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_chip_close)
        chip.closeIconTint = ContextCompat.getColorStateList(requireContext(), R.color.colorPrimaryDark)
        chip.chipBackgroundColor = ContextCompat.getColorStateList(requireContext(), R.color.viewBackgroundColor)

        chip.setOnCloseIconClickListener {
            chipGroup.removeView(it)
        }
        chipGroup.addView(chip)
    }

    override fun onStart() {
        super.onStart()
        homeActivity.removeNavBar()
    }
}