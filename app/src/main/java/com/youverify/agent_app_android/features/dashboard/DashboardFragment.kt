package com.youverify.agent_app_android.features.dashboard

import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.view.*
import android.widget.*
import androidx.camera.core.impl.utils.ContextUtil.getApplicationContext
import androidx.core.content.ContextCompat
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
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
import com.youverify.agent_app_android.util.extension.visibleIf
import com.youverify.agent_app_android.util.helper.*
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import kotlin.math.abs


@AndroidEntryPoint
class DashboardFragment : Fragment(R.layout.fragment_dashboard) {
    private lateinit var binding: FragmentDashboardBinding
    private lateinit var homeActivity: HomeActivity

    private var isVerified: Boolean = false
    private var isTrained: Boolean = false
    private var prefAreasIsChosen: Boolean = false
    private lateinit var dialog: AlertDialog

    private val viewModel by activityViewModels<DashboardViewModel>()

    @Inject
    lateinit var sharePreference: AgentSharePreference

    @Inject
    lateinit var dateProvider: DateProvider


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
            setupRangePickerDialog()
            //showBottomBar()
        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setObservers()

        val yesterday = formatDate(dateProvider.getFirstDateOfCurrentMonth())

        Timber.d("First day: $yesterday")





        if (savedInstanceState == null) {
            val todaysDate = Date()
            val dateFrom7DaysAgo = getDateFromDayAgo(daysAgo = 7)
            fetchAnalytics(dateFrom7DaysAgo, todaysDate)
            processPerformanceStats(PerformanceStats.day)
        }

        showPieChart(33)
    }

    private fun fetchAnalytics(startDate: Date, endDate: Date) {
        val viewDateFormat = "dd MMMM yyyy"
        val startInApiFormat = formatDate(startDate)
        val startInDomainFormat = formatDate(startDate, output = viewDateFormat)

        val endDateApiFormat = formatDate(endDate)
        val endDateDomainFormat = formatDate(endDate, output = viewDateFormat)
        val durationString = getString(
            R.string._8th_july_2021_to_30th_august_2021,
            startInDomainFormat,
            endDateDomainFormat
        )
        binding.durationText.setText(Html.fromHtml(durationString), TextView.BufferType.SPANNABLE)
        viewModel.fetchAgentAnalytics(startInApiFormat, endDateApiFormat)
    }

    private fun fetchPrevious(startDate: Date, endDate: Date) {
        val startInApiFormat = formatDate(startDate)
        val endDateApiFormat = formatDate(endDate)
        viewModel.fetchPreviousPerfromance(startInApiFormat, endDateApiFormat)
    }

    private fun fetchCurrent(startDate: Date, endDate: Date) {
        val startInApiFormat = formatDate(startDate)
        val endDateApiFormat = formatDate(endDate)
        viewModel.fetchCurrentPerformance(startInApiFormat, endDateApiFormat)
    }



    private fun setupUI() {
        with(binding) {
            agentVisibilitySwitch.setOnCheckedChangeListener { compoundButton, b ->
                val currentStatus = binding.agentVisibilitySwitch.tag as? String
                Timber.d("Tag ==> $currentStatus")
                currentStatus?.let {
                    val status =
                        if (currentStatus == AgentStatus.ONINE) AgentStatus.OFFLINE else AgentStatus.ONINE
                    viewModel.updateAgentStatus(status)
                }

            }
        }
    }

    private fun showPieChart(value: Int) {
        val pieEntries: ArrayList<PieEntry> = ArrayList()
        val label = "type"

        val negative = value < 0
        val color = if (negative) "#FF0000" else "#006400"
        val absValue = abs(value)
        Timber.d("Absolute ==> $absValue")

        //initializing data
        val typeAmountMap: MutableMap<String, Int> = HashMap()
        val current = if (negative) 100 - absValue else absValue
        val previous = if (negative) absValue else 100 - absValue

        typeAmountMap["Current"] = 100 -  absValue
        typeAmountMap["Previous"] = absValue


        //initializing colors for the entries
        val colors: ArrayList<Int> = ArrayList()



        colors.add(Color.parseColor(color))
        colors.add(Color.parseColor("#ffffff"))


        //input data and fit data into pie chart entry
        for (type in typeAmountMap.keys) {
            pieEntries.add(PieEntry(typeAmountMap[type]!!.toFloat(), type))
        }

        //collecting the entries with label name

        //collecting the entries with label name
        val pieDataSet = PieDataSet(pieEntries, label)
        //setting text size of the value
        pieDataSet.valueTextSize = 12f
        //providing color list for coloring different entries
        pieDataSet.colors = colors
        //grouping the data set from entry to chart
        val pieData = PieData(pieDataSet)
        //showing the value of the entries, default true if not set
        pieData.setDrawValues(true)
        pieData.setValueTextColor(Color.WHITE)

        with(binding.profileProgressBar) {
           this.setData(pieData)
            this.invalidate()
            this.holeRadius = 70F
            this.setHoleColor(Color.TRANSPARENT)
            this.transparentCircleRadius = 70F
            this.setDrawSliceText(false)
            this.description.isEnabled = false
            this.legend.isEnabled = false

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
            onLoading(state is ResultState.Loading)

            when (state) {
                is ResultState.Error -> {
                    context?.toast(state.error)
                    viewModel.updateAgentVisibility(sharePreference.agentVisiblityStatus)
                }
                else -> {}
            }
        }

        viewModel.analyticsDataState.observe(viewLifecycleOwner) {
            val state = it ?: return@observe
            onLoading(state is ResultState.Loading)

            when (state) {
                is ResultState.Error -> {
                    binding.completedTasks.text = "-- --"
                    binding.queriedTasks.text = "-- --"
                }
                is ResultState.Success -> {
                    binding.completedTasks.text = state.data.completed.toString()
                    binding.queriedTasks.text = state.data.queried.toString()
                }
                else -> {}
            }
        }

        viewModel.previousPerformanceDataState.observe(viewLifecycleOwner) {
            val state = it ?: return@observe
            onLoading(state is ResultState.Loading)

            when(state) {
                is ResultState.Success -> {
                    binding.previousPerformance.text = state.data.completed.toString()
                }
                else -> {
                    binding.previousPerformance.text = "-- --"
                }
            }
        }

        viewModel.performanceCurrent.observe(viewLifecycleOwner) {
            val state = it ?: return@observe
            onLoading(state is ResultState.Loading)

            when(state) {
                is ResultState.Success -> binding.currentPerformance.text = state.data.completed.toString()
                else -> binding.currentPerformance.text = "-- --"
            }
        }

        viewModel.todaysPerformance.observe(viewLifecycleOwner) {
            val state = it ?: return@observe
            onLoading(state is ResultState.Loading)
            when(state) {
                is ResultState.Success -> {
                    binding.numberText.text = state.data.completed.toString()
                    state.data.completionTimeInMilliseconds?.let { time ->
                        binding.displayTime.text = formatAsTime(time.toLong())
                    }
                }
            }
        }

        viewModel.chartPercentage.observe(viewLifecycleOwner) {
            val change  = it ?: return@observe
            showPieChart(change)
        }
    }

    private fun onLoading(loading: Boolean) {
        binding.indicator.visibleIf(loading)
    }

    private fun updateStatus(state: String) {
        val check = state == AgentStatus.ONINE
        binding.agentVisibilitySwitch.isChecked = check
        binding.agentVisibilitySwitch.tag = state
        val showText = if (check) "Online" else "Offline"
        binding.agentVisibilitySwitch.text = showText

        binding.toolBar.setColor(check, R.color.colorPrimaryDark, R.color.off_duty)

        if (isLollipopPlus()) {
            val window = activity?.window
            window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            val color = if (check) R.color.colorPrimaryDark else R.color.off_duty
            window?.statusBarColor = ContextCompat.getColor(requireContext(), color)
        }

        val curvedBackground =
            if (check) R.drawable.curved_appbar else R.drawable.curved_appbar_inactive
        val notifyIcon =
            if (check) R.drawable.ic_bell_icon else R.drawable.ic_notify_off_duty
        val dayBkg =
            if (check) R.drawable.curved_button else R.drawable.curved_btn_inactive
        binding.notificationIcon.setImageDrawable(ContextCompat.getDrawable(requireContext(), notifyIcon))
        binding.daySelector.setBackgroundResource(dayBkg)
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
        val builder = MaterialDatePicker.Builder.dateRangePicker()
        builder.setTitleText("Select period")
        builder.setTheme(R.style.MaterialCalendarTheme)
        val constraintsBuilder = CalendarConstraints.Builder()
        builder.setCalendarConstraints(constraintsBuilder.build())
        val picker = builder.build()
        getDateRange(picker)
        picker.show(parentFragmentManager, picker.toString())
    }

    private fun getDateRange(materialCalendarPicker: MaterialDatePicker<Pair<Long, Long>>) {
        materialCalendarPicker.addOnPositiveButtonClickListener {
            val startDate = getDate(it.first)
            val endDate = getDate(it.second)
            fetchAnalytics(startDate, endDate)
            Timber.e(materialCalendarPicker.headerText)
        }
        materialCalendarPicker.addOnNegativeButtonClickListener { }
        materialCalendarPicker.addOnCancelListener { }
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
                processPerformanceStats(PerformanceStats.day)
                binding.bottomTodayTxt.text = "Today"
                binding.bottomYesterdayTxt.text = "Yesterday"
            }, 100)
        }
        weekButton.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed({
                dialogBuilder.dismiss()
                selectedOption = weekButton.text.toString()
                binding.selectTodayBtn.setText(selectedOption)
                processPerformanceStats(PerformanceStats.week)
                binding.bottomTodayTxt.text = "This week"
                binding.bottomYesterdayTxt.text = "Last week"
            }, 100)
        }
        monthButton.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed({
                dialogBuilder.dismiss()
                selectedOption = monthButton.text.toString()
                binding.selectTodayBtn.setText(selectedOption)
                binding.bottomTodayTxt.text = "This month"
                binding.bottomYesterdayTxt.text = "Last month"
                processPerformanceStats(PerformanceStats.month)
            }, 100)
        }

        dialogBuilder.setCancelable(false)
        dialogBuilder.show()
        dialogBuilder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun processPerformanceStats(type: Int) {
        if (!isOreoPlus()) return
        when (type) {
            PerformanceStats.day -> {
                val yesterdayDate = dateProvider.getYesterdayDate()
                fetchPrevious(yesterdayDate, yesterdayDate)
                fetchCurrent(Date(), Date())
            }

            PerformanceStats.week -> {
                val startDate = dateProvider.getFirstDayPreviousWeek()
                val endDate = dateProvider.getLastDayOfPreviousWeek()
                fetchPrevious(startDate, endDate)

                val currentWeekFirstDay = dateProvider.getFirstDateOfCurrentWeek()
                fetchCurrent(currentWeekFirstDay, Date())
            }

            PerformanceStats.month -> {
                val startDate = dateProvider.getPreviousMonthFirstDate()
                val endDate = dateProvider.getPreviousMonthLastDate()
                fetchPrevious(startDate, endDate)

                val firstDayOfMonth = dateProvider.getFirstDateOfCurrentMonth()
                fetchCurrent(firstDayOfMonth, Date())
            }

        }
    }

    object PerformanceStats {
        const val day = 1
        const val week = 2
        const val month = 3
    }

    private fun showCompleteOnboardingDialog() {
        dialog =
            AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog).create()
        val view = layoutInflater.inflate(R.layout.complete_onboarding_dialog, null)
        val trainingCheck = view.findViewById<CheckBox>(R.id.check_complete_training)
        val verifyIdCheck = view.findViewById<CheckBox>(R.id.check_verify_identity)
        val prefAreasCheck = view.findViewById<CheckBox>(R.id.check_select_areas)
        dialog.setView(view)

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
            dialog.dismiss()
            homeActivity.removeNavBar()
        }

        verifyIdCheck.setOnClickListener {
            dialog.dismiss()
            homeActivity.removeNavBar()
            findNavController().navigate(R.id.action_dashboardFragment_to_selectIDFragment)
        }

        prefAreasCheck.setOnClickListener {
            dialog.dismiss()
            homeActivity.removeNavBar()
            findNavController().navigate(R.id.action_dashboardFragment_to_selectAreasFragment)
        }

        dialog.setCancelable(false)
        dialog.show()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
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

    override fun onPause() {
        super.onPause()
        if (this::dialog.isInitialized && dialog.isShowing){
            dialog.dismiss()
        }
    }
}