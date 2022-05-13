package com.youverify.agent_app_android.view.fragment.home.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.databinding.FragmentProfileBinding
import com.youverify.agent_app_android.view.activity.HomeActivity

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var binding: FragmentProfileBinding
    private val homeActivity =  HomeActivity()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View{
        binding = FragmentProfileBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.changePassBtn.setOnClickListener {
            homeActivity.removeNavBar()
            findNavController().navigate(R.id.action_profileFragment_to_changePasswordFragment)
        }
    }
}