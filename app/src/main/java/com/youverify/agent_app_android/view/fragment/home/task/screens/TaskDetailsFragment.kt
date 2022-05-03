package com.youverify.agent_app_android.view.fragment.home.task.screens

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.databinding.FragmentTaskDetailsBinding

class TaskDetailsFragment : Fragment(R.layout.fragment_task_details) {
    private lateinit var binding: FragmentTaskDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentTaskDetailsBinding.inflate(layoutInflater)

        binding.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        binding.submitButton.setOnClickListener {
            submit()
        }

//        configureUI()

        return binding.root
    }


    //implement this function
    private fun configureUI(){
        val idType = resources.getStringArray(R.array.idType)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.locate_address_drop_down, idType)
        binding.accessLocBtn.setAdapter(arrayAdapter)

        binding.accessLocBtn.setOnClickListener {
            binding.accessLocBtn.showDropDown()
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
}