package com.youverify.agent_app_android.view.fragment.home.dashboard

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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

//        Handler(Looper.getMainLooper()).postDelayed({
//            showCompleteOnboardingDialog()
//        }, 500)

        return binding.root
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
//        dialogBuilder.window?.setLayout(1100, 1600)
        dialogBuilder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}