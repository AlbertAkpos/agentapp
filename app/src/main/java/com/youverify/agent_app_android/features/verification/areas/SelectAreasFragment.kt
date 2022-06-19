package com.youverify.agent_app_android.features.verification.areas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.data.model.verification.areas.PrefAreaRequest
import com.youverify.agent_app_android.data.model.verification.areas.PrefAreasResponseData
import com.youverify.agent_app_android.databinding.FragmentSelectAreasBinding
import com.youverify.agent_app_android.features.HomeActivity
import com.youverify.agent_app_android.util.AgentSharePreference
import com.youverify.agent_app_android.util.ProgressLoader
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


@AndroidEntryPoint
class  SelectAreasFragment : Fragment(R.layout.fragment_select_areas) {

    @Inject
    lateinit var progressLoader: ProgressLoader
    private lateinit var binding: FragmentSelectAreasBinding
    private val selectAreaViewModel : SelectAreasViewModel by viewModels()
    private lateinit var homeActivity: HomeActivity
    private lateinit var chipGroup: ChipGroup
    private lateinit var linearLayout: LinearLayoutCompat
    private lateinit var autoCompText: AutoCompleteTextView
    private lateinit var state: String
    private var count: Int = 0
    private lateinit var prefAreas : ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentSelectAreasBinding.inflate(layoutInflater)
        homeActivity = requireActivity() as HomeActivity
        linearLayout = binding.dropDownInner
//        autoCompText = binding.selectAreasEditText
        chipGroup = binding.chipGroup

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefAreas = arrayListOf()
        configureUI()
        fetchAreas()
    }

    private fun configureUI(){
        binding.areasBtn.setOnClickListener {
//            if(binding.dropDownLayout.visibility == View.VISIBLE){
//                binding.dropDownLayout.visibility = View.GONE
//                binding.areasBtn.setImageResource(R.drawable.ic_arrow_up2)
//            }else{
//                binding.dropDownLayout.visibility = View.VISIBLE
//                binding.areasBtn.setImageResource(R.drawable.ic_arrow_down2)
//            }
        }

        binding.doneBtn.setOnClickListener {
            saveAreas()
            homeActivity.removeNavBar()

        }

        binding.backBtn.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun sendSelectedAreas(){

    }

    private fun fetchAreas(){
        state = AgentSharePreference(requireContext()).getString("STATE_OF_RESIDENCE")

        progressLoader.show(message = "Fetching areas...")
        val responseData = selectAreaViewModel.getStateLgas(state.lowercase(Locale.getDefault()))
        responseData.observe(requireActivity()){
            if (!it.isNullOrEmpty()){
                progressLoader.hide()
                val lparams = LinearLayoutCompat.LayoutParams(
                    LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT
                )
//                val arrayAdapter = ArrayAdapter(requireContext(), R.layout.areas_drop_down_item, it)
                for (lga in it){
                    val checkBox = CheckBox(context)
                    checkBox.layoutParams = lparams
                    checkBox.text = lga
                    linearLayout.addView(checkBox)
                    addChip(lga)
                }
            }
        }
    }

    private fun saveAreas(){
        val prefAreaRequest = PrefAreaRequest(
            stateOfResidence = state,
            preferredAreas = prefAreas
        )

        selectAreaViewModel.saveAreas(prefAreaRequest)

        lifecycleScope.launchWhenCreated {
            selectAreaViewModel.prefAreasChannel.collect{
                when (it) {
                    is PrefAreasViewState.Loading -> {
                        progressLoader.show(message = "Saving...")
                    }
                    is PrefAreasViewState.Success -> {
                        progressLoader.hide()
                        saveAreasSuccessful(it.prefAreasResponseData)
                        AgentSharePreference(requireContext()).setBoolean("PREF_AREAS", true)
                        findNavController().navigate(R.id.action_selectAreasFragment_to_saveAreasFragment)
                    }
                    is PrefAreasViewState.Failure -> {
                        progressLoader.hide()
                        Snackbar.make(requireView(), "Malformed/Invalid token", Snackbar.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        }
    }

    private fun saveAreasSuccessful(signUpResponseData: PrefAreasResponseData?) {
        println(signUpResponseData)
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

        chip.setOnClickListener {
//            autoCompText.setText("")
            if(count < 3){
                prefAreas.add(chip.text.toString())
                chipGroup.removeView(it)
                count++
            }else{
                Snackbar.make(requireView(), "Cannot select more than 3 areas", Snackbar.LENGTH_SHORT).show()
            }
        }
        chipGroup.addView(chip)
    }

    override fun onStart() {
        super.onStart()
        count = 0
        homeActivity.removeNavBar()
    }
}