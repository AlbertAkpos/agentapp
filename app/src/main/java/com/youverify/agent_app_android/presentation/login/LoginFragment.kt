package com.youverify.agent_app_android.presentation.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.databinding.FragmentLoginBinding
import com.youverify.agent_app_android.presentation.HomeActivity


class LoginFragment : Fragment(R.layout.fragment_login) {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var emailLayout : TextInputLayout
    private lateinit var passLayout : TextInputLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater)

        emailLayout = binding.emailLayout
        passLayout = binding.passLayout

        registerListeners()

        return binding.root
    }


    private fun validateEmail(): Boolean {
        val value = emailLayout.editText?.text.toString().trim()

        return when {
            value.isEmpty() -> {
                emailLayout.error = "Field cannot be empty"
                emailLayout.errorIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_error)
                false
            }
            !Patterns.EMAIL_ADDRESS.matcher(value).matches() -> {
                emailLayout.error = "Invalid email"
                emailLayout.errorIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_error)
                false
            }
            else -> {
                emailLayout.error = null
                emailLayout.isErrorEnabled = false
                true
            }
        }
    }

    private fun validatePassword(value : String): Boolean {
        val checkSpaces = "\\A\\w{1,20}\\z"

        return when {
            value.isEmpty() -> {
                passLayout.error = "Field cannot be empty"
                passLayout.errorIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_error)
                false
            }
            value.length < 8 -> {
                passLayout.error = "Password must be up to 8 characters"
                passLayout.errorIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_error)
                false
            }
            !value.matches(checkSpaces.toRegex()) -> {
                passLayout.error = "No whitespaces are allowed!"
                passLayout.errorIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_error)
                false
            }
            else -> {
                passLayout.error = null
                passLayout.isErrorEnabled = false
                true
            }
        }
    }

    private fun validateFields(){
        validateEmail()
        validatePassword(passLayout.editText?.text.toString().trim())
    }

    private fun login(){

    }

    private fun registerListeners(){
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
    }
}