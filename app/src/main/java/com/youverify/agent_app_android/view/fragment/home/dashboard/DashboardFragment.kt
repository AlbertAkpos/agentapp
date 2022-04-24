package com.youverify.agent_app_android.view.fragment.home.dashboard

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment(R.layout.fragment_dashboard) {
    private lateinit var binding: FragmentDashboardBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(layoutInflater)

        binding.notificationIcon.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_notificationsFragment)
        }

        binding.selectTodayBtn.setOnClickListener {
            showFilterDialog()
        }

        //if select period button is clicked, slide the bottom bar from beneath
        binding.selectPeriodBtn.setOnClickListener{
            showBottomBar()
//            setupRangePickerDialog()
        }

//        Handler(Looper.getMainLooper()).postDelayed({
//            showCompleteOnboardingDialog()
//        }, 500)

        return binding.root
    }


    private fun setupRangePickerDialog() {
        val builder: MaterialDatePicker.Builder<*> = MaterialDatePicker.Builder.dateRangePicker()
        val constraintsBuilder = CalendarConstraints.Builder()
        try {
            builder.setCalendarConstraints(constraintsBuilder.build())
            val picker: MaterialDatePicker<*> = builder.build()
            getDateRange(picker)
            picker.show(parentFragmentManager, picker.toString())
        } catch (e:IllegalArgumentException){
        }
    }


    private fun getDateRange(materialCalendarPicker:MaterialDatePicker<out Any>) {
        materialCalendarPicker.addOnPositiveButtonClickListener {
            Log.e("DateRangeText", materialCalendarPicker.headerText)
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
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setGravity(Gravity.BOTTOM)
        dialog.window?.attributes?.windowAnimations = R.style.BottomDialogAnimation
    }

    private fun showFilterDialog() {
        var selectedOption : String
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


    private fun showCompleteOnboardingDialog(){
        val dialogBuilder =
            AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog).create()
        val view = layoutInflater.inflate(R.layout.complete_onboarding_dialog, null)
        val checkCompleteTraining = view.findViewById<CheckBox>(R.id.check_complete_training)
        val checkVerifyIdentity = view.findViewById<CheckBox>(R.id.check_verify_identity)
        val checkSelectPrefAreas = view.findViewById<CheckBox>(R.id.check_select_areas)
        dialogBuilder.setView(view)
        checkVerifyIdentity.setOnClickListener {
            dialogBuilder.dismiss()
            findNavController().navigate(R.id.action_dashboardFragment_to_selectIDFragment)
        }

        checkSelectPrefAreas.setOnClickListener {
            dialogBuilder.dismiss()
            findNavController().navigate(R.id.action_dashboardFragment_to_selectAreasFragment)
        }
        dialogBuilder.setCancelable(false)
        dialogBuilder.show()
        dialogBuilder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}