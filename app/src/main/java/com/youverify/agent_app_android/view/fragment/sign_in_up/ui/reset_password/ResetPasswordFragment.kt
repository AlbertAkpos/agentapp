package com.youverify.agent_app_android.view.fragment.sign_in_up.ui.reset_password

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.databinding.FragmentResetPasswordBinding

class ResetPasswordFragment : Fragment(R.layout.fragment_reset_password) {
    private lateinit var binding: FragmentResetPasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentResetPasswordBinding.inflate(layoutInflater)

        binding.btnSendInstructions.setOnClickListener {
            findNavController().navigate(R.id.action_resetPasswordFragment_to_checkEmailFragment)
        }

        return binding.root
    }
}