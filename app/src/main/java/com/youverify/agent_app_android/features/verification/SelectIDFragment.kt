package com.youverify.agent_app_android.features.verification

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.databinding.FragmentSelectIdBinding
import com.youverify.agent_app_android.features.HomeActivity


class SelectIDFragment : Fragment(R.layout.fragment_select_id) {

    private lateinit var binding: FragmentSelectIdBinding
    private lateinit var homeActivity: HomeActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{

        binding = FragmentSelectIdBinding.inflate(layoutInflater)
        homeActivity = requireActivity() as HomeActivity
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureUI()
    }


    private fun configureUI(){
        //passing the array adapter for states in the autocomplete textview
        val idType = resources.getStringArray(R.array.idType)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.id_type_drop_down_item, idType)
        binding.selectIdEditText.setAdapter(arrayAdapter)
        binding.selectIdEditText.setOnClickListener {
            binding.selectIdEditText.showDropDown()
        }

        binding.selectIdEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.layoutDateOfBirth.visibility = View.VISIBLE
                binding.layoutEnterBvn.visibility = View.VISIBLE
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.continueButton.setOnClickListener {
            findNavController().navigate(R.id.action_selectIDFragment_to_uploadPassportFragment)
        }

        binding.backBtn.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    override fun onStop() {
        super.onStop()
        homeActivity.showNavBar()
    }
}