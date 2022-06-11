package com.youverify.agent_app_android.features.settings

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.youverify.agent_app_android.databinding.FragmentSettingsBinding
import com.youverify.agent_app_android.features.OnBoardingActivity
import com.youverify.agent_app_android.util.AgentSharePreference

class SettingsFragment : Fragment(){

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSettingsBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.signOutText.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes") { _, _ ->
                    AgentSharePreference(requireContext()).clear("TOKEN")
                    startActivity(Intent(requireContext(), OnBoardingActivity::class.java))
                    activity?.finish()
                }
                .setNegativeButton("No", null)
                .show()
        }
    }
}