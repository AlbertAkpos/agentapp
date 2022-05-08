package com.youverify.agent_app_android.view.fragment.home.task.screens

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
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.databinding.FragmentTaskDetailsBinding

class TaskDetailsFragment : Fragment(R.layout.fragment_task_details) {
    private lateinit var binding: FragmentTaskDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentTaskDetailsBinding.inflate(layoutInflater)

        configureUI()

        return binding.root
    }


    //implement this function
    private fun configureUI(){
        binding.accessLocBtn.setOnClickListener {
           binding.layoutAccessLoc.endIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_up2)
            binding.choiceLayout.visibility = View.VISIBLE
        }

        binding.yesBtn.setOnClickListener {
            binding.proceedButton.visibility = View.VISIBLE

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
            binding.submitButton.visibility = View.GONE
        }

        binding.noBtn.setOnClickListener {
            binding.proceedButton.visibility = View.GONE

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
            binding.submitButton.visibility = View.VISIBLE
        }

        binding.reasonInput.setOnClickListener {
            showBottomBar()
        }

        binding.submitButton.setOnClickListener {
            submit()
        }

        binding.toolbar.setNavigationOnClickListener {
            showNavBar()
            activity?.onBackPressed()
        }

        binding.proceedButton.setOnClickListener {
            findNavController().navigate(R.id.action_taskDetailsFragment_to_accessBuildingFragment)
        }
    }

    private fun submit(){
        val dialogBuilder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog).create()
        val view = layoutInflater.inflate(R.layout.reset_password_dialog, null)
        val successText = view.findViewById<TextView>(R.id.text_pass_changed)
        val okButton = view.findViewById<TextView>(R.id.text_ok)
        dialogBuilder.setView(view)

        successText.text = "Report Submitted"
        okButton.setOnClickListener{
            dialogBuilder.dismiss()
            activity?.onBackPressed()
        }
        dialogBuilder.setCancelable(false)
        dialogBuilder.show()
        dialogBuilder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun showNavBar() {
        val navBar = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
        navBar?.visibility = View.VISIBLE
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