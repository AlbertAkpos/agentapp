package com.youverify.agent_app_android.view.fragment.sign_in_up.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.databinding.FragmentLoginBinding
import com.youverify.agent_app_android.view.activity.HomeActivity


class LoginFragment : Fragment(R.layout.fragment_login) {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater)

        //click listeners
        binding.textViewSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_LoginScreen_to_SignUpScreen)
        }

        binding.buttonSignIn.setOnClickListener {
            startActivity(Intent(requireContext(), HomeActivity::class.java))
            activity?.finish()
        }

        binding.forgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_LoginScreen_to_resetPasswordFragment)
        }

        return binding.root
    }

}