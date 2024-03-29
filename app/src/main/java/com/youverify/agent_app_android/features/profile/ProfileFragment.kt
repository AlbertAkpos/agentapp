package com.youverify.agent_app_android.features.profile

import android.annotation.SuppressLint
import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.databinding.FragmentProfileBinding
import com.youverify.agent_app_android.features.HomeActivity
import com.youverify.agent_app_android.util.AgentSharePreference
import com.youverify.agent_app_android.util.SharedPrefKeys

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var homeActivity : HomeActivity
    private lateinit var profileImage : ImageView

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View{
        binding = FragmentProfileBinding.inflate(layoutInflater)
        homeActivity = requireActivity() as HomeActivity
        profileImage = binding.profileImage

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.changePassBtn.setOnClickListener {
            homeActivity.removeNavBar()
            findNavController().navigate(R.id.action_profileFragment_to_changePasswordFragment)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
        val firstName = AgentSharePreference(requireContext()).getString(SharedPrefKeys.FIRST_NAME)
        val lastName = AgentSharePreference(requireContext()).getString(SharedPrefKeys.LAST_NAME)
        val email = AgentSharePreference(requireContext()).getString(SharedPrefKeys.EMAIL)
        val phone = AgentSharePreference(requireContext()).getString(SharedPrefKeys.PHONE)
        val address = AgentSharePreference(requireContext()).getString(SharedPrefKeys.STATE_OF_RESIDENCE)

        binding.tvName.text = "$firstName  $lastName"
        binding.tvEmail.text = email
        binding.tvPhone.text = phone
        binding.tvAddress.text = address
        loadProfileImage(AgentSharePreference(requireContext()).getString(SharedPrefKeys.IMG_URL))
    }

    private fun loadProfileImage(imageUrl: String){
        Glide.with(requireContext())
            .load(imageUrl)
            .apply(RequestOptions.circleCropTransform())
            .into(profileImage)
    }
}