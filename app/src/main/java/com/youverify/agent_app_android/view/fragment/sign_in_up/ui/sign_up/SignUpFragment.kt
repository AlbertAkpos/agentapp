package com.youverify.agent_app_android.view.fragment.sign_in_up.ui.sign_up

import android.app.AlertDialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.databinding.FragmentSignUpBinding
import com.youverify.agent_app_android.view.activity.HomeActivity


class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

//    private lateinit var loginViewModel: LoginViewModel
    private var _binding: FragmentSignUpBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{

        _binding = FragmentSignUpBinding.inflate(layoutInflater)

        binding.textviewSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_SignUpScreen_to_LoginScreen)
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_SignUpScreen_to_LoginScreen)
        }

        binding.buttonSignUp.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed({
                showEmailVerificationDialog()
            }, 500)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())[LoginViewModel::class.java]

        //passing the array adapter for states in the autocomplete textview
        val states = resources.getStringArray(R.array.states)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.states_drop_down_item, states)
        binding.stateOfResidence.setAdapter(arrayAdapter)
        binding.stateOfResidence.setOnClickListener {
            binding.stateOfResidence.showDropDown()
        }
    }

    //method to display dialog to user showing to check verify email
    private fun showEmailVerificationDialog(){
        val dialogBuilder =
            AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog).create()
        val view = layoutInflater.inflate(R.layout.details_verification_dialog, null)
        val okButton = view.findViewById<TextView>(R.id.text_ok)
        dialogBuilder.setView(view)

        okButton.setOnClickListener {
            dialogBuilder.dismiss()
            startActivity(Intent(requireContext(), HomeActivity::class.java))
            activity?.finish()
        }
        dialogBuilder.setCancelable(false)
        dialogBuilder.show()
        dialogBuilder.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
    }

//    private fun updateUiWithUser(model: LoggedInUserView) {
//        val welcome = getString(R.string.welcome) + model.displayName
//        // TODO : initiate successful logged in experience
//        val appContext = context?.applicationContext ?: return
//        Toast.makeText(appContext, welcome, Toast.LENGTH_LONG).show()
//    }
//
//    private fun showLoginFailed(@StringRes errorString: Int) {
//        val appContext = context?.applicationContext ?: return
//        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}