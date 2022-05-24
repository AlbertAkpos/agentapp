package com.youverify.agent_app_android.features.login

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.data.model.login.LoginRequest
import com.youverify.agent_app_android.data.model.login.LoginResponseData
import com.youverify.agent_app_android.databinding.FragmentLoginBinding
import com.youverify.agent_app_android.features.HomeActivity
import com.youverify.agent_app_android.util.ProgressLoader
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    @Inject
    lateinit var progressLoader: ProgressLoader
    private lateinit var binding: FragmentLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()

    private lateinit var emailLayout: TextInputLayout
    private lateinit var passLayout: TextInputLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)

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
                emailLayout.errorIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_error)
                false
            }
            !Patterns.EMAIL_ADDRESS.matcher(value).matches() -> {
                emailLayout.error = "Invalid email"
                emailLayout.errorIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_error)
                false
            }
            else -> {
                emailLayout.error = null
                emailLayout.isErrorEnabled = false
                true
            }
        }
    }

    private fun validatePassword(value: String): Boolean {
        return when {
            value.isEmpty() -> {
                passLayout.error = "Field cannot be empty"
                passLayout.errorIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_error)
                false
            }
            value.length < 8 -> {
                passLayout.error = "Invalid password"
                passLayout.errorIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_error)
                false
            }
            else -> {
                passLayout.error = null
                passLayout.isErrorEnabled = false
                true
            }
        }
    }

    private fun validateFields(): Boolean {
        return validateEmail() && validatePassword(passLayout.editText?.text.toString().trim())
    }

    private fun registerListeners() {
        //click listeners
        binding.textViewSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_LoginScreen_to_SignUpScreen)
        }

        binding.buttonSignIn.setOnClickListener {
            login()
        }

        binding.forgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_LoginScreen_to_resetPasswordFragment)
        }
    }

    private fun login() {
        if (validateFields()) {
            val loginRequest = LoginRequest(
                email = emailLayout.editText?.text.toString().trim(),
                password = passLayout.editText?.text.toString().trim()
            )

            loginViewModel.login(loginRequest = loginRequest)

            lifecycleScope.launchWhenCreated {
                loginViewModel.loginChannel.collect {
                    when (it) {
                        is LoginViewState.Loading -> {
                            progressLoader.show(message = "Logging In...")
                        }
                        is LoginViewState.Success -> {
                            progressLoader.hide()
                            loginSuccessfulOrFailed(it.loginResponseData)
                            startActivity(Intent(requireContext(), HomeActivity::class.java))
                            activity?.finish()
                        }
                        is LoginViewState.Failure -> {
                            progressLoader.hide()
                            Toast.makeText(requireContext(), it.errorMessage, Toast.LENGTH_LONG)
                                .show()
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private fun loginSuccessfulOrFailed(loginResponse: LoginResponseData?) {
        Toast.makeText(requireContext(), "Login Successful", Toast.LENGTH_SHORT).show()
        println("Response: $loginResponse")
    }
}