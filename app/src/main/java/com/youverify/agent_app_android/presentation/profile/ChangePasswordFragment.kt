package com.youverify.agent_app_android.presentation.profile

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.databinding.FragmentChangePasswordBinding
import com.youverify.agent_app_android.presentation.HomeActivity


class ChangePasswordFragment : Fragment(R.layout.fragment_change_password) {
    private lateinit var binding: FragmentChangePasswordBinding
    private lateinit var homeActivity : HomeActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{

        binding = FragmentChangePasswordBinding.inflate(layoutInflater)
        homeActivity = requireActivity() as HomeActivity

        configureUI()
        return binding.root
    }

    override fun onStop() {
        super.onStop()
        homeActivity.showNavBar()
    }

    private fun configureUI(){
        binding.saveChangesBtn.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog).create()
            val view = layoutInflater.inflate(R.layout.reset_password_dialog, null)
            val okButton = view.findViewById<TextView>(R.id.text_ok)
            dialogBuilder.setView(view)
            okButton.setOnClickListener{
                dialogBuilder.dismiss()
                findNavController().navigate(R.id.action_changePasswordFragment_to_profileFragment)
            }
            dialogBuilder.setCancelable(false)
            dialogBuilder.show()
            dialogBuilder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        binding.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
    }
}