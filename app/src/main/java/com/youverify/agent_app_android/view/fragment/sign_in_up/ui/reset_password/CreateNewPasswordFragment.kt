package com.youverify.agent_app_android.view.fragment.sign_in_up.ui.reset_password

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.databinding.FragmentCreateNewPasswordBinding


class CreateNewPasswordFragment : Fragment(R.layout.fragment_create_new_password) {

    private lateinit var binding : FragmentCreateNewPasswordBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentCreateNewPasswordBinding.inflate(layoutInflater)

        binding.buttonSaveChanges.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog).create()
            val view = layoutInflater.inflate(R.layout.reset_password_dialog, null)
            val okButton = view.findViewById<TextView>(R.id.text_ok)
            dialogBuilder.setView(view)
            okButton.setOnClickListener{
                dialogBuilder.dismiss()
                findNavController().navigate(R.id.action_createNewPasswordFragment_to_LoginScreen)
            }
            dialogBuilder.setCancelable(false)
            dialogBuilder.show()
            dialogBuilder.window?.setLayout(900, 950)
        }
        return binding.root
    }
}