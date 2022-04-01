package com.youverify.agent_app_android.view.fragment.sign_in_up.ui.reset_password

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.databinding.FragmentCheckEmailBinding


class CheckEmailFragment : Fragment(R.layout.fragment_check_email) {
    private lateinit var binding: FragmentCheckEmailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentCheckEmailBinding.inflate(layoutInflater)

        binding.buttonOpenEmail.setOnClickListener {
            findNavController().navigate(R.id.action_checkEmailFragment_to_createNewPasswordFragment)
        }

        return binding.root
    }
}