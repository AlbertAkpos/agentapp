package com.youverify.agent_app_android.view.fragment.home.notifications

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.databinding.FragmentTaskDetailsBinding

class TaskDetailsFragment : Fragment(R.layout.fragment_task_details) {
    private lateinit var binding: FragmentTaskDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentTaskDetailsBinding.inflate(layoutInflater)

        binding.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        return binding.root
    }

}