package com.youverify.agent_app_android.features.profile

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.data.model.profile.ChangePassRequest
import com.youverify.agent_app_android.databinding.FragmentChangePasswordBinding
import com.youverify.agent_app_android.features.HomeActivity
import com.youverify.agent_app_android.util.ProgressLoader
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ChangePasswordFragment : Fragment(R.layout.fragment_change_password) {

    @Inject
    lateinit var progressLoader: ProgressLoader
    private val profileViewModel: ProfileViewModel by viewModels()
    private lateinit var binding: FragmentChangePasswordBinding
    private lateinit var homeActivity: HomeActivity
    private lateinit var oldPassLayout: TextInputLayout
    private lateinit var newPassLayout: TextInputLayout
    private lateinit var confirmPassLayout: TextInputLayout
    private lateinit var saveButton: Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentChangePasswordBinding.inflate(layoutInflater)
        homeActivity = requireActivity() as HomeActivity

        oldPassLayout = binding.oldPasswordLayout
        newPassLayout = binding.newPasswordLayout
        confirmPassLayout = binding.confirmPasswordLayout
        saveButton = binding.saveChangesBtn

        configureUI()
        return binding.root
    }

    private fun configureUI() {
        binding.checkConfirmPassLabel.visibility = View.GONE
        binding.checkPassLabel.visibility = View.GONE

        binding.saveChangesBtn.setOnClickListener {
            changePassword()
        }

        binding.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
    }

    override fun onStop() {
        super.onStop()
        homeActivity.showNavBar()
    }

    private fun validatePassword(value: String, layout: TextInputLayout): Boolean {
        return when {
            value.isEmpty() -> {
                layout.error = "Cannot be empty"
                layout.errorIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_error)
                false
            }
            value.length < 8 -> {
                layout.error = "Must be up to 8 characters long"
                layout.errorIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_error)
                false
            }
            else -> {
                layout.error = null
                layout.isErrorEnabled = false
                true
            }
        }
    }

    private fun validateFields(): Boolean {
        val oldPassword = oldPassLayout.editText?.text.toString().trim()
        val newPassword = newPassLayout.editText?.text.toString().trim()
        val confirmPassword = confirmPassLayout.editText?.text.toString().trim()

        if (validatePassword(oldPassword, oldPassLayout) &&
            validatePassword(newPassword, newPassLayout) &&
            validatePassword(confirmPassword, confirmPassLayout)
        ) {
            return if (newPassword == confirmPassword) {
                confirmPassLayout.error = null
                confirmPassLayout.isErrorEnabled = false
                true
            } else {
                newPassLayout.error = "Both passwords must match"
                confirmPassLayout.error = "Both passwords must match"
                newPassLayout.errorIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_error)
                confirmPassLayout.errorIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_error)
                false
            }
        } else {
            return false
        }
    }

    private fun changePassword() {
        if (validateFields()) {
            val changePassRequest = ChangePassRequest(
                oldPassword = oldPassLayout.editText?.text.toString().trim(),
                newPassword = newPassLayout.editText?.text.toString().trim()
            )

            profileViewModel.changePassword(changePassRequest = changePassRequest)

            lifecycleScope.launchWhenCreated {
                profileViewModel.changePassChannel.collect {
                    when (it) {
                        is ChangePassViewState.Loading -> {
                            progressLoader.show(message = "Please wait...")
                        }
                        is ChangePassViewState.Success -> {
                            progressLoader.hide()
                            changePassSuccessful(it.changePassResponse?.message)
                            showSuccessDialog(it.changePassResponse?.message)
                        }
                        is ChangePassViewState.Failure -> {
                            progressLoader.hide()
                            Toast.makeText(requireContext(), it.errorMessage, Toast.LENGTH_SHORT)
                                .show()
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private fun changePassSuccessful(changePassResponse: String?) {
        println(changePassResponse)
    }

    private fun showSuccessDialog(changePassResponse: String?) {
        val dialogBuilder =
            AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog).create()
        val view = layoutInflater.inflate(R.layout.change_pass_dialog, null)
        val successText = view.findViewById<TextView>(R.id.text_link_sent)
        successText.text = changePassResponse
        val okButton = view.findViewById<TextView>(R.id.text_ok)
        dialogBuilder.setView(view)
        okButton.setOnClickListener {
            dialogBuilder.dismiss()
            findNavController().navigate(R.id.action_changePasswordFragment_to_profileFragment)
        }
        dialogBuilder.setCancelable(false)
        dialogBuilder.show()
        dialogBuilder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}