package com.youverify.agent_app_android.features.task.views

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.*
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.data.model.TaskItem
import com.youverify.agent_app_android.data.model.tasks.TasksDomain
import com.youverify.agent_app_android.databinding.BuildingTypesLayoutBinding
import com.youverify.agent_app_android.databinding.FragmentTaskDetailsBinding
import com.youverify.agent_app_android.databinding.RadioButtonLayoutBinding
import com.youverify.agent_app_android.features.HomeActivity
import com.youverify.agent_app_android.features.task.TaskViewModel
import com.youverify.agent_app_android.util.SingleEvent
import com.youverify.agent_app_android.util.extension.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TaskDetailsFragment : Fragment(R.layout.fragment_task_details) {
    private lateinit var binding: FragmentTaskDetailsBinding


    private val viewModel by activityViewModels<TaskViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentTaskDetailsBinding.inflate(layoutInflater)
        configureUI()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClicks()
        setObservers()
    }

    private fun setupClicks() = with(binding) {
        proceedToCaptureDetailsBtn.setOnClickListener {
            canLocateAddressContainer.show()
            it.gone()
        }

        yesBtn.setOnClickListener { viewModel.canYouLocateTheAddressState.postValue(SingleEvent(true)) }

        noBtn.setOnClickListener { viewModel.canYouLocateTheAddressState.postValue(SingleEvent(false)) }

        buildingTypeInput.setOnClickListener { openBuildingTypeSheet() }
    }

    private fun openBuildingTypeSheet() {
        var dialog: MaterialDialog? = null
        val binding = BuildingTypesLayoutBinding.inflate(layoutInflater)
        val radioButton = RadioButtonLayoutBinding.inflate(layoutInflater)
        for (item in viewModel.typesOfBuildings) {
            binding.radioGroup.removeAllViews()
            radioButton.radio.text = item
            binding.radioGroup.addView(radioButton.root)
        }
        binding.radioGroup.setOnCheckedChangeListener { radioGroup, i ->
            val button = radioGroup?.findViewById<RadioButton>(i)
            if (button?.isChecked == true) {
                val selectedBuilding = button.text?.toString() ?: ""
                dialog?.dismiss()
                viewModel.taskAnswers = viewModel.taskAnswers.copy(buildingType = selectedBuilding)
            }
        }

        dialog = context?.inflateBottomSheet(binding.root)
    }

    private fun setObservers() {
        viewModel.taskItemState.observe(viewLifecycleOwner) {
            val taskItem = it.getContentIfNotHandled() ?: return@observe
            updateUI(taskItem)
        }

        viewModel.canYouLocateTheAddressState.observe(viewLifecycleOwner) {
            val state = it.getContentIfNotHandled() ?: return@observe
                binding.yesCanLocateAddress.setColor(state, R.color.colorDark, R.color.white)
                binding.noCantLocateAddress.setColor(!state, R.color.colorDark, R.color.white)
                binding.proceedToCaptureDetailsBtn.visibleIf(state && binding.canLocateAddressContainer.visibility == View.GONE)
                if (!state) { binding.canLocateAddressContainer.gone() }
                binding.cantLocateAddressContainer.visibleIf(!state)
        }
    }

    private fun updateUI(taskItem: TasksDomain.AgentTask) = with(binding) {
        verificationTypeText.text = taskItem.verificationType
        candidateAddress.text = taskItem.address
    }

    //implement this function
    private fun configureUI(){
        binding.accessLocBtn.setOnClickListener {
           binding.layoutAccessLoc.endIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_up2)
            binding.choiceLayout.visibility = View.VISIBLE
        }

        binding.yesBtn.setOnClickListener {


            binding.selectReasonText.visibility = View.GONE
            binding.reasonLayout.visibility = View.GONE
            binding.imagesText.visibility = View.GONE
            binding.uploadView.visibility = View.GONE
            binding.getTagText.visibility = View.GONE
            binding.geoTagLayout.visibility = View.GONE
            binding.landmarkText.visibility = View.GONE
            binding.landmarkLayout.visibility = View.GONE
            binding.infoText.visibility = View.GONE
            binding.infoLayout.visibility = View.GONE
        }

        binding.noBtn.setOnClickListener {

            binding.selectReasonText.visibility = View.VISIBLE
            binding.reasonLayout.visibility = View.VISIBLE
            binding.imagesText.visibility = View.VISIBLE
            binding.uploadView.visibility = View.VISIBLE
            binding.getTagText.visibility = View.VISIBLE
            binding.geoTagLayout.visibility = View.VISIBLE
            binding.landmarkText.visibility = View.VISIBLE
            binding.landmarkLayout.visibility = View.VISIBLE
            binding.infoText.visibility = View.VISIBLE
            binding.infoLayout.visibility = View.VISIBLE
        }

        binding.reasonInput.setOnClickListener {
            showBottomBar()
        }



        binding.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

    }

    private fun submit(){
        val dialogBuilder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog).create()
        val view = layoutInflater.inflate(R.layout.success_dialog, null)
        val okButton = view.findViewById<TextView>(R.id.text_ok)
        dialogBuilder.setView(view)

        okButton.setOnClickListener{
            dialogBuilder.dismiss()
            activity?.onBackPressed()
        }
        dialogBuilder.setCancelable(false)
        dialogBuilder.show()
        dialogBuilder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun showBottomBar() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_select_reason)

        val choice1 = dialog.findViewById<AppCompatRadioButton>(R.id.not_exist_btn)
        val choice2 = dialog.findViewById<AppCompatRadioButton>(R.id.incorrect_btn)


        choice1.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed({
                binding.reasonInput.setText(choice1.text.toString())
                dialog.dismiss()
            }, 500)
        }

        choice2.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed({
                binding.reasonInput.setText(choice2.text.toString())
                dialog.dismiss()
            }, 500)
        }

        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setGravity(Gravity.BOTTOM)
        dialog.window?.attributes?.windowAnimations = R.style.BottomDialogAnimation
    }
}