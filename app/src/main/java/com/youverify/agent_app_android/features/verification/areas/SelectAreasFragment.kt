package com.youverify.agent_app_android.features.verification.areas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.youverify.agent_app_android.util.extension.visibleIf
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class  SelectAreasFragment : Fragment(R.layout.fragment_select_areas) {

    @Inject
    lateinit var progressLoader: ProgressLoader
    private lateinit var binding: FragmentSelectAreasBinding
    private val selectAreaViewModel : SelectAreasViewModel by viewModels()
    private lateinit var homeActivity: HomeActivity
    private lateinit var chipGroup: ChipGroup
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CheckBoxAdapter
    private lateinit var state: String
    private var count: Int = 0
    private lateinit var prefAreas : ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentSelectAreasBinding.inflate(layoutInflater)
        homeActivity = requireActivity() as HomeActivity
        recyclerView = binding.dropDownInner
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
        binding.dropDown.visibleIf(!binding.dropDown.isVisible)

        binding.areasBtn.setOnClickListener {
            binding.dropDown.visibleIf(!binding.dropDown.isVisible)
        }

        binding.doneBtn.setOnClickListener {
            saveAreas()
            homeActivity.removeNavBar()

        }

        binding.backBtn.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun initRecyclerView(lgaList: ArrayList<String>){
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = CheckBoxAdapter(
            {item -> checkBoxChecked(item)},
            {item -> uncheckBoxChecked(item)}
        )

        recyclerView.setItemViewCacheSize(lgaList.size)
        recyclerView.adapter = adapter
        adapter.setItems(lgaList)
    }

    private fun fetchAreas(){
        state = AgentSharePreference(requireContext()).getString("STATE_OF_RESIDENCE")

        progressLoader.show(message = "Fetching areas, please wait...")
        val responseData = selectAreaViewModel.getStateLgas(state.lowercase(Locale.getDefault()))
        responseData.observe(requireActivity()){
            if (!it.isNullOrEmpty()){
                progressLoader.hide()
                initRecyclerView(it)
            }
        }
    }

    private fun checkBoxChecked(lga: String) : Boolean{
        return if(!prefAreas.contains(lga) && prefAreas.size < 3){
            addChip(lga)
            prefAreas.add(lga)
            true
        }else if (prefAreas.contains(lga)){
            Snackbar.make(requireView(), "Already added $lga", Snackbar.LENGTH_SHORT).show()
            false
        }else{
            Snackbar.make(requireView(), "Cannot select more than 3 areas", Snackbar.LENGTH_SHORT).show()
            false
        }
    }

    private fun uncheckBoxChecked(text: String) {
        for (i in 0 until chipGroup.childCount){
            val chip = chipGroup[i] as Chip
            println(chip.text)
            if (chip.text == text){
                chipGroup.removeViewAt(i)
                prefAreas.removeAt(i)
                break
            }
        }
    }

    private fun saveAreas(){

        if(!prefAreas.isNullOrEmpty()){
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
                            Snackbar.make(requireView(), it.errorMessage, Snackbar.LENGTH_SHORT).show()
                        }
                        else -> {}
                    }
                }
            }
        }else{
            Snackbar.make(requireView(), "Please select preffered areas", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun saveAreasSuccessful(signUpResponseData: PrefAreasResponseData?) {
        println(signUpResponseData)
    }

    private fun addChip(text: String){
        val chip = Chip(requireContext())
        chip.text = text
        chip.textSize = 12F
//        chip.closeIconSize = 24F
        chip.chipCornerRadius = 8F
//        chip.isCloseIconVisible = true
//        chip.iconStartPadding = 0F
        chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
//        chip.closeIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_chip_close)
//        chip.closeIconTint = ContextCompat.getColorStateList(requireContext(), R.color.colorPrimaryDark)
        chip.chipBackgroundColor = ContextCompat.getColorStateList(requireContext(), R.color.viewBackgroundColor)

        chip.setOnCloseIconClickListener {
//            chipGroup.removeView(it)
//            prefAreas.remove(text)
            uncheckBoxChecked(text)
        }

        chipGroup.addView(chip)
    }

    override fun onStart() {
        super.onStart()
        count = 0
        homeActivity.removeNavBar()
    }
}