package com.youverify.agent_app_android.features.signup

import android.app.AlertDialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.textfield.TextInputLayout
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.data.model.signup.SignUpRequest
import com.youverify.agent_app_android.data.model.signup.SignUpResponseData
import com.youverify.agent_app_android.databinding.FragmentSignUpBinding
import com.youverify.agent_app_android.util.ProgressLoader
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.abs


@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    @Inject
    lateinit var progressLoader: ProgressLoader
    private val signUpViewModel: SignUpViewModel by viewModels()
    private lateinit var binding: FragmentSignUpBinding

    private lateinit var firstNameLayout: TextInputLayout
    private lateinit var lastNameLayout: TextInputLayout
    private lateinit var phoneLayout: TextInputLayout
    private lateinit var emailLayout: TextInputLayout
    private lateinit var stateOfResLayout: TextInputLayout
    private lateinit var passLayout: TextInputLayout
    private lateinit var confirmPassLayout: TextInputLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSignUpBinding.inflate(layoutInflater)
        firstNameLayout = binding.firstNameLayout
        lastNameLayout = binding.lastNameLayout
        phoneLayout = binding.phoneLayout
        emailLayout = binding.emailLayout
        stateOfResLayout = binding.stateOfResidenceLayout
        passLayout = binding.passwordLayout
        confirmPassLayout = binding.confirmPassLayout

        registerListeners()
        getStates()

        return binding.root
    }

    private fun registerListeners() {
        binding.stateOfResidenceInput.setOnClickListener {
            binding.stateOfResidenceInput.showDropDown()
        }

        binding.textviewSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_SignUpScreen_to_LoginScreen)
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_SignUpScreen_to_LoginScreen)
        }


        binding.signUpButton.setOnClickListener {
            signUp()
        }

        //show the sign up text on collapse of toolbar and vice versa
        binding.appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            val percentage = (abs(verticalOffset).toFloat() / binding.appbar.totalScrollRange)

            when {
                abs(verticalOffset) == binding.appbar.totalScrollRange -> {
                    //  Collapsed
                    //Show TextView here
                    binding.signUpText.visibility = View.VISIBLE
                    binding.toolbar.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorDark
                        )
                    )
                }
                verticalOffset == 0 -> {
                    //Expanded
                    //Hide TextView here
                    binding.signUpText.visibility = View.GONE
                    binding.toolbar.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorPrimaryDark
                        )
                    )
                }
                else -> {
                    //In Between
                    binding.toolbar.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorPrimaryDark
                        )
                    )
                    binding.signUpText.visibility = View.GONE
                    binding.signUpText.animate().alpha(percentage)
                }
            }
        })
    }

    //method to display dialog to user showing to check verify email
    private fun showEmailVerificationDialog() {
        val dialogBuilder =
            AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog).create()
        val view = layoutInflater.inflate(R.layout.details_verification_dialog, null)
        val okButton = view.findViewById<TextView>(R.id.text_ok)
        dialogBuilder.setView(view)

        okButton.setOnClickListener {
            dialogBuilder.dismiss()
            clearFields()
            findNavController().navigate(R.id.action_SignUpScreen_to_LoginScreen)
        }
        dialogBuilder.setCancelable(false)
        dialogBuilder.show()
        dialogBuilder.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
    }

    private fun clearFields() {
        firstNameLayout.editText?.text?.clear()
        lastNameLayout.editText?.text?.clear()
        phoneLayout.editText?.text?.clear()
        emailLayout.editText?.text?.clear()
        stateOfResLayout.editText?.text?.clear()
        passLayout.editText?.text?.clear()
        confirmPassLayout.editText?.text?.clear()
    }

    private fun validateFirstName(): Boolean {
        val value = firstNameLayout.editText?.text.toString().trim()

        return when {
            value.isEmpty() -> {
                firstNameLayout.error = "Field cannot be empty"
                firstNameLayout.errorIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_error)
                false
            }
            !value.matches("^[a-zA-Z]+(([',.\\-][a-zA-Z])?[a-zA-Z]*)*\$".toRegex()) -> {
                firstNameLayout.error = "Name cannot contain number or special character"
                firstNameLayout.errorIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_error)
                false
            }
            else -> {
                firstNameLayout.error = null
                firstNameLayout.isErrorEnabled = false
                true
            }
        }
    }

    private fun validateLastName(): Boolean {
        val value = lastNameLayout.editText?.text.toString().trim()

        return when {
            value.isEmpty() -> {
                lastNameLayout.error = "Field cannot be empty"
                lastNameLayout.errorIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_error)
                false
            }
            !value.matches("^[a-zA-Z]+(([',.\\-][a-zA-Z])?[a-zA-Z]*)*\$".toRegex()) -> {
                lastNameLayout.error = "Name contains invalid characters or whitespaces"
                lastNameLayout.errorIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_error)
                false
            }
            else -> {
                lastNameLayout.error = null
                lastNameLayout.isErrorEnabled = false
                true
            }
        }
    }

    private fun validatePhone(): Boolean {
        val value = phoneLayout.editText?.text.toString().trim()
        val checkSpaces = "\\A\\w{1,11}\\z"

        return when {
            value.isEmpty() -> {
                phoneLayout.error = "Field cannot be empty"
                phoneLayout.errorIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_error)
                false
            }
            !value.matches(checkSpaces.toRegex()) -> {
                phoneLayout.error = "No whitespaces are allowed!"
                phoneLayout.errorIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_error)
                false
            }
            value.length < 11 || value.length > 11 -> {
                phoneLayout.error = "Phone number less than 11 digits"
                phoneLayout.errorIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_error)
                false
            }
            !value.matches("^([0]{1})[0-9]{10}\$".toRegex()) -> {
                phoneLayout.error = "Invalid phone number"
                phoneLayout.errorIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_error)
                false
            }
            else -> {
                phoneLayout.error = null
                phoneLayout.isErrorEnabled = false
                true
            }
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

    private fun validateStateOfResidence(): Boolean {
        val value = stateOfResLayout.editText?.text.toString().trim()

        return when {
            value.isEmpty() -> {
                stateOfResLayout.error = "Field cannot be empty"
                stateOfResLayout.errorIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_error)
                false
            }
            else -> {
                stateOfResLayout.error = null
                stateOfResLayout.isErrorEnabled = false
                true
            }
        }
    }

    private fun validatePassword(value: String): Boolean {
        val checkSpaces = "\\A\\w{1,20}\\z"

        return when {
            value.isEmpty() -> {
                passLayout.error = "Field cannot be empty"
                passLayout.errorIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_error)
                false
            }
            value.length < 8 -> {
                passLayout.error = "Password must be up to 8 characters"
                passLayout.errorIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_error)
                false
            }
            !value.matches(checkSpaces.toRegex()) -> {
                passLayout.error = "No whitespaces are allowed!"
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

    private fun inputsIsValid(): Boolean {
        val password = passLayout.editText?.text.toString().trim()
        val confirmPassword = confirmPassLayout.editText?.text.toString().trim()

        validateFirstName()
        validateLastName()
        validatePhone()
        validateEmail()
        validateStateOfResidence()

        return if (validatePassword(password) && validatePassword(confirmPassword)) {
            if (password == confirmPassword) {
                confirmPassLayout.error = null
                confirmPassLayout.isErrorEnabled = false
                true
            } else {
                passLayout.error = "Passwords do not match"
                confirmPassLayout.error = "Passwords do not match"
                passLayout.errorIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_error)
                confirmPassLayout.errorIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_error)
                false
            }
        } else false
    }

    //   call to api service to sign up the user
    private fun signUp() {
        if (inputsIsValid()) {
            val fieldPartnerId = "628df5053fad01748fdee367"
            val signUpRequest = SignUpRequest(
                firstName = firstNameLayout.editText?.text.toString().trim(),
                lastName = lastNameLayout.editText?.text.toString().trim(),
                phoneNumber = phoneLayout.editText?.text.toString().trim(),
                emailAddress = emailLayout.editText?.text.toString().trim(),
                stateOfResidence = stateOfResLayout.editText?.text.toString().trim(),
                fieldPartnerId = fieldPartnerId,
                password = passLayout.editText?.text.toString().trim()
            )

            signUpViewModel.signUpYvAgent(signUpRequest = signUpRequest)

            lifecycleScope.launchWhenCreated {
                signUpViewModel.signUpChannel.collect {
                    when (it) {
                        is SignUpViewState.Loading -> {
                            progressLoader.show(message = "Signing Up...")
                        }
                        is SignUpViewState.Success -> {
                            progressLoader.hide()
                            signUpSuccessful(it.signUpResponseData)
                            Handler(Looper.getMainLooper()).postDelayed({
                                showEmailVerificationDialog()
                            }, 500)
                        }
                        is SignUpViewState.Failure -> {
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

    private fun signUpSuccessful(signUpResponse: SignUpResponseData?) {
        Toast.makeText(requireContext(), "SignUp Successful", Toast.LENGTH_SHORT).show()
        println("Successful: $signUpResponse")
    }

    private fun getStates() {
        val responseLiveData = signUpViewModel.getStates()
        responseLiveData.observe(requireActivity()) {
            if (it != null) {
                val arrayAdapter =
                    ArrayAdapter(requireContext(), R.layout.states_drop_down_item, it)
                binding.stateOfResidenceInput.setAdapter(arrayAdapter)
            } else {
                Toast.makeText(requireContext(), "No data available", Toast.LENGTH_SHORT).show()
            }
        }
    }
}