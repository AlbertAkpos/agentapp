package com.youverify.agent_app_android.features.resetpassword

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.data.model.resetpassword.Email
import com.youverify.agent_app_android.data.model.resetpassword.ResetPassResponse
import com.youverify.agent_app_android.databinding.FragmentResetTokenBinding
import com.youverify.agent_app_android.util.ProgressLoader
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ResetPassFragment : Fragment(R.layout.fragment_reset_token) {

    @Inject
    lateinit var progressLoader: ProgressLoader
    private lateinit var binding: FragmentResetTokenBinding
    private val resetPassViewModel: ResetPassViewModel by viewModels()
    private lateinit var emailLayout: TextInputLayout


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentResetTokenBinding.inflate(layoutInflater)

        emailLayout = binding.emailLayout

        registerListeners()

        return binding.root
    }

    private fun registerListeners(){
        binding.sendInstructionBtn.setOnClickListener {
           sendInstructions()
        }

        binding.toolBar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
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

    private fun sendInstructions(){
        if (validateEmail()) {
            val email = Email(
                email = emailLayout.editText?.text.toString().trim()
            )

            resetPassViewModel.sendResetToken(email = email)

            lifecycleScope.launchWhenCreated {
                resetPassViewModel.resetPassChannel.collect {
                    when (it) {
                        is ResetPassViewState.Loading -> {
                            progressLoader.show(message = "Sending...")
                        }
                        is ResetPassViewState.Success -> {
                            progressLoader.hide()
                            resetTokenSent(it.resetPassResponse!!)

                            showSuccessDialog()
                        }
                        is ResetPassViewState.Failure -> {
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

    private fun showSuccessDialog() {
        val dialogBuilder =
            AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog).create()
        val view = layoutInflater.inflate(R.layout.reset_password_dialog, null)
        val okButton = view.findViewById<TextView>(R.id.text_ok)
        dialogBuilder.setView(view)

        okButton.setOnClickListener {
            dialogBuilder.dismiss()
            findNavController().navigate(R.id.action_resetPasswordFragment_to_LoginScreen)
        }
        dialogBuilder.setCancelable(false)
        dialogBuilder.show()
        dialogBuilder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun resetTokenSent(resetPassResponse: ResetPassResponse) {
        Toast.makeText(requireContext(), "Reset token sent", Toast.LENGTH_SHORT).show()
        println("Response: $resetPassResponse")
    }
}