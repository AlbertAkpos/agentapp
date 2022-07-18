package com.youverify.agent_app_android.features.dashboard

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.core.functional.ResultState
import com.youverify.agent_app_android.databinding.FragmentDashboardBinding
import com.youverify.agent_app_android.features.HomeActivity
import com.youverify.agent_app_android.util.AgentSharePreference
import com.youverify.agent_app_android.util.AgentStatus
import com.youverify.agent_app_android.util.SharedPrefKeys
import com.youverify.agent_app_android.util.extension.setColor
import com.youverify.agent_app_android.util.extension.toast
import com.youverify.agent_app_android.util.helper.isLollipopPlus
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class DashboardFragment : Fragment(R.layout.fragment_dashboard) {
    private lateinit var binding: FragmentDashboardBinding
    private lateinit var homeActivity: HomeActivity

    private var isVerified: Boolean = false
    private var isTrained: Boolean = false
    private var prefAreasIsChosen: Boolean = false

    private val viewModel by activityViewModels<DashboardViewModel>()

    @Inject
    lateinit var sharePreference: AgentSharePreference


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(layoutInflater)
        homeActivity = requireActivity() as HomeActivity

        binding.notificationIcon.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_notificationsFragment)
        }

        binding.selectTodayBtn.setOnClickListener {
            showFilterDialog()
        }

        //if select period button is clicked, slide the bottom bar from beneath
        binding.selectPeriodBtn.setOnClickListener {
            showBottomBar()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setObservers()
    }

    private fun setupUI()   {
        with(binding) {
            agentVisibilitySwitch.setOnCheckedChangeListener { compoundButton, b ->
                val currentStatus = binding.agentVisibilitySwitch.tag as? String
                Timber.d("Tag ==> $currentStatus")
                currentStatus?.let {
                    val status = if (currentStatus == AgentStatus.ONINE) AgentStatus.OFFLINE else AgentStatus.ONINE
                    viewModel.updateAgentStatus(status)
                }

            }
        }
    }

    private fun setObservers() {
        viewModel.agentVisibiltyStatus.observe(viewLifecycleOwner) {
            val state = it ?: return@observe
            Timber.d("Status ==> $state")
            updateStatus(state)
        }

        viewModel.updateAgentStatusState.observe(viewLifecycleOwner) {
            val state = it.getContentIfNotHandled() ?: return@observe
            binding.agentVisibilitySwitch.isEnabled = state !is ResultState.Loading
            when(state) {
                is ResultState.Error -> {
                    context?.toast(state.error)
                   viewModel.updateAgentVisibility(sharePreference.agentVisiblityStatus)
                }
                else -> {}
            }
        }
    }

    private fun updateStatus(state: String) {
        val check = state == AgentStatus.ONINE
        binding.agentVisibilitySwitch.isChecked = check
        binding.agentVisibilitySwitch.tag = state
        val showText = if (check) "On duty" else "Off duty"
        binding.agentVisibilitySwitch.text = showText

        binding.toolBar.setColor(check, R.color.colorPrimaryDark, R.color.off_duty)

        if (isLollipopPlus()) {
            val window = activity?.window
            window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            val color = if (check) R.color.colorPrimaryDark else R.color.off_duty
            window?.statusBarColor = ContextCompat.getColor(requireContext(), color)
        }

        val curvedBackground = if (check) R.drawable.curved_appbar else R.drawable.curved_appbar_inactive
        binding.toolbarDropped.setBackgroundResource(curvedBackground)
    }

    //verify that user has finished onboarding
    private fun verificationDone(): Boolean {
        isVerified = AgentSharePreference(requireContext()).getBoolean(SharedPrefKeys.IS_VERIFIED)
        isTrained = AgentSharePreference(requireContext()).getBoolean(SharedPrefKeys.IS_TRAINED)
        prefAreasIsChosen =
            AgentSharePreference(requireContext()).getBoolean(SharedPrefKeys.PREF_AREAS, false)
        return isTrained && isVerified && prefAreasIsChosen
    }

    private fun setupRangePickerDialog() {
        val builder: MaterialDatePicker.Builder<*> = MaterialDatePicker.Builder.dateRangePicker()
        val constraintsBuilder = CalendarConstraints.Builder()
        builder.setCalendarConstraints(constraintsBuilder.build())
        val picker: MaterialDatePicker<*> = builder.build()
        getDateRange(picker)
        picker.show(parentFragmentManager, picker.toString())
    }

    private fun getDateRange(materialCalendarPicker: MaterialDatePicker<out Any>) {
        materialCalendarPicker.addOnPositiveButtonClickListener {
            Timber.e(materialCalendarPicker.headerText)
        }
        materialCalendarPicker.addOnNegativeButtonClickListener { }
        materialCalendarPicker.addOnCancelListener { }
    }

    private fun showBottomBar() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_select_period_layout)

        val calendar = dialog.findViewById<CalendarView>(R.id.calendar_view)
        val applyButton = dialog.findViewById<Button>(R.id.button_apply)
        val drawerHandle = dialog.findViewById<View>(R.id.drawer_handle)

        applyButton.setOnClickListener {
            dialog.dismiss()
            Toast.makeText(requireContext(), "Clicked Calendar", Toast.LENGTH_SHORT).show()
        }

        dialog.show()
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setGravity(Gravity.BOTTOM)
        dialog.window?.attributes?.windowAnimations = R.style.BottomDialogAnimation
    }

    private fun showFilterDialog() {
        var selectedOption: String
        val dialogBuilder =
            AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog).create()
        val view = layoutInflater.inflate(R.layout.performance_card_dialog, null)
        val dayButton = view.findViewById<RadioButton>(R.id.dayButton)
        val weekButton = view.findViewById<RadioButton>(R.id.weekButton)
        val monthButton = view.findViewById<RadioButton>(R.id.monthButton)
        dialogBuilder.setView(view)

        dayButton.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed({
                dialogBuilder.dismiss()
                binding.selectTodayBtn.setText("Today")
            }, 500)
        }
        weekButton.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed({
                dialogBuilder.dismiss()
                selectedOption = weekButton.text.toString()
                binding.selectTodayBtn.setText(selectedOption)
            }, 500)
        }
        monthButton.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed({
                dialogBuilder.dismiss()
                selectedOption = monthButton.text.toString()
                binding.selectTodayBtn.setText(selectedOption)
            }, 500)
        }

        dialogBuilder.setCancelable(false)
        dialogBuilder.show()
        dialogBuilder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun showCompleteOnboardingDialog() {
        val dialogBuilder =
            AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog).create()
        val view = layoutInflater.inflate(R.layout.complete_onboarding_dialog, null)
        val trainingCheck = view.findViewById<CheckBox>(R.id.check_complete_training)
        val verifyIdCheck = view.findViewById<CheckBox>(R.id.check_verify_identity)
        val prefAreasCheck = view.findViewById<CheckBox>(R.id.check_select_areas)
        dialogBuilder.setView(view)

        if (isTrained) {
            trainingCheck.isChecked = true
            trainingCheck.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        }

        if (isVerified) {
            verifyIdCheck.isChecked = true
            verifyIdCheck.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        }

        if (prefAreasIsChosen) {
            prefAreasCheck.isChecked = true
            prefAreasCheck.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        }

        trainingCheck.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_trainingFragment)
            dialogBuilder.dismiss()
            homeActivity.removeNavBar()
        }

        verifyIdCheck.setOnClickListener {
            dialogBuilder.dismiss()
            homeActivity.removeNavBar()
            findNavController().navigate(R.id.action_dashboardFragment_to_selectIDFragment)

        }

        prefAreasCheck.setOnClickListener {
            dialogBuilder.dismiss()
            homeActivity.removeNavBar()
            findNavController().navigate(R.id.action_dashboardFragment_to_selectAreasFragment)

        }

        dialogBuilder.setCancelable(false)
        dialogBuilder.show()
        dialogBuilder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun showCompletedDialog(){
        val dialogBuilder =
            AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog).create()
        val view = layoutInflater.inflate(R.layout.onboarding_completed_dialog, null)
        dialogBuilder.setView(view)
        dialogBuilder.setCancelable(false)
        dialogBuilder.show()
        dialogBuilder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onResume() {
        super.onResume()
        homeActivity.showNavBar()
        val agentStatus =
            AgentSharePreference(requireContext()).getString(SharedPrefKeys.AGENT_STATUS)
        if ((verificationDone() && agentStatus == "IN_ACTIVE")) {
            showCompletedDialog()
        }else if (!verificationDone()){
            showCompleteOnboardingDialog()
        }
    }
}