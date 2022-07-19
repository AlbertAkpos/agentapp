package com.youverify.agent_app_android.features.settings

import android.app.AlertDialog
import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.content.Intent
import android.os.*
import android.os.VibrationEffect.DEFAULT_AMPLITUDE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.impl.utils.ContextUtil.getApplicationContext
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.data.model.settings.TermsOrPrivacy
import com.youverify.agent_app_android.databinding.FragmentSettingsBinding
import com.youverify.agent_app_android.features.HomeActivity
import com.youverify.agent_app_android.features.OnBoardingActivity
import com.youverify.agent_app_android.util.AgentSharePreference
import com.youverify.agent_app_android.util.AgentStatus
import com.youverify.agent_app_android.util.SharedPrefKeys
import timber.log.Timber

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var homeActivity : HomeActivity
    private lateinit var isTerms : TermsOrPrivacy

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSettingsBinding.inflate(layoutInflater)
        homeActivity = requireActivity() as HomeActivity
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerListeners()
    }

    private fun registerListeners(){
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.signOutText.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes") { _, _ ->
                    AgentSharePreference(requireContext()).clear(SharedPrefKeys.TOKEN)
                    AgentSharePreference(requireContext()).clear(SharedPrefKeys.AGENT_STATUS)
                    AgentSharePreference(requireContext()).clear(SharedPrefKeys.IMG_URL)
                    startActivity(Intent(requireContext(), OnBoardingActivity::class.java))
                    activity?.finish()
                }
                .setNegativeButton("No", null)
                .show()
        }

        binding.trainingBtn.setOnClickListener{
            findNavController().navigate(R.id.action_settingsFragment_to_trainingFragment)
            homeActivity.removeNavBar()
        }

        binding.termsBtn.setOnClickListener {
            val action = SettingsFragmentDirections.actionSettingsFragmentToTermsOfUseFragment(TermsOrPrivacy(true))
            findNavController().navigate(action)
            homeActivity.removeNavBar()
        }

        with(binding){
            privacyBtn.setOnClickListener {
                val action = SettingsFragmentDirections.actionSettingsFragmentToTermsOfUseFragment(TermsOrPrivacy(false))
                findNavController().navigate(action)
                homeActivity.removeNavBar()
            }
        }

        with(binding) {
            vibrateSwitch.setOnCheckedChangeListener { compoundButton, b ->
                val currentStatus = binding.vibrateSwitch.isChecked as? Boolean
                if (currentStatus!!) setPhoneVibration()
                else removePhoneFromVibrate()
            }
        }
    }

    private fun removePhoneFromVibrate() {}

    private fun setPhoneVibration() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                getApplicationContext(requireContext()).getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            val vibrator = vibratorManager.defaultVibrator
            vibrator.vibrate(VibrationEffect.createOneShot(500, DEFAULT_AMPLITUDE))
        }else{
            val vibrator =  getApplicationContext(requireContext()).getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(VibrationEffect.createOneShot(500, DEFAULT_AMPLITUDE))
        }
    }

    override fun onResume() {
        super.onResume()
        homeActivity.showNavBar()
    }
}