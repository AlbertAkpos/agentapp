package com.youverify.agent_app_android.view.fragment.home.profile

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.databinding.FragmentChangePasswordBinding


class ChangePasswordFragment : Fragment(R.layout.fragment_change_password) {
    private lateinit var binding: FragmentChangePasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{

        binding = FragmentChangePasswordBinding.inflate(layoutInflater)

        binding.saveChangesBtn.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog).create()
            val view = layoutInflater.inflate(R.layout.reset_password_dialog, null)
            val okButton = view.findViewById<TextView>(R.id.text_ok)
            dialogBuilder.setView(view)
            okButton.setOnClickListener{
                dialogBuilder.dismiss()
                findNavController().navigate(R.id.action_changePasswordFragment_to_profileFragment)
            }
            dialogBuilder.setCancelable(false)
            dialogBuilder.show()
            dialogBuilder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        binding.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        return binding.root
    }

    override fun onStop() {
        super.onStop()
        val navBar = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
        navBar?.visibility = View.VISIBLE
    }
}