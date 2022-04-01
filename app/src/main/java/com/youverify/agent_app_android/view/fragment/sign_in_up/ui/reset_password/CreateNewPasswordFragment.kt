package com.youverify.agent_app_android.view.fragment.sign_in_up.ui.reset_password

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
            findNavController().navigate(R.id.action_createNewPasswordFragment_to_LoginScreen)
        }
        return binding.root
    }
}