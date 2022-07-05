package com.youverify.agent_app_android.features.onboarding

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.databinding.FragmentEnablePermissionsBinding
import com.youverify.agent_app_android.util.AgentSharePreference
import com.youverify.agent_app_android.util.SharedPrefKeys

class EnablePermissionsFragment : Fragment(R.layout.fragment_enable_permissions) {

    private lateinit var binding: FragmentEnablePermissionsBinding

    private lateinit var location: SwitchCompat
    private lateinit var photos: SwitchCompat
    private lateinit var camera: SwitchCompat
    private lateinit var notification: SwitchCompat

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEnablePermissionsBinding.inflate(layoutInflater)

        location = binding.switchLocAccess
        photos = binding.switchPhotos
        camera = binding.switchCamera
        notification = binding.switchPushNotifications

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ensurePermissions()

        binding.verifyButton.setOnClickListener {
            onBoardingFinished()
            findNavController().navigate(R.id.action_enableLocationFragment_to_LoginScreen)
        }

        binding.verifyBackBtn.setOnClickListener {
            activity?.onBackPressed()
        }
    }


    //Expressions to check if single permission has been granted and ask for it if not
    private val locationPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        when (it) {
            true -> {
                location.isChecked = true
                Toast.makeText(requireContext(), "Permission has been granted", Toast.LENGTH_SHORT)
                    .show()
            }
            false -> {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
                location.isChecked = false
            }
        }
    }

    private val photosPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        when (it) {
            true -> {
                photos.isChecked = true
                Toast.makeText(requireContext(), "Permission has been granted", Toast.LENGTH_SHORT)
                    .show()
            }
            false -> {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
                photos.isChecked = false
            }
        }
    }

    private val cameraPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        when (it) {
            true -> {
                camera.isChecked = true
                Toast.makeText(requireContext(), "Permission has been granted", Toast.LENGTH_SHORT)
                    .show()
            }
            false -> {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
                camera.isChecked = false
            }
        }
    }

    private fun requestLocationPermission(op: ActivityResultLauncher<String>, permission: String) {
        if (shouldShowRequestPermissionRationale(permission)) {
            //tell user why this permission is needed
            op.launch(permission)
        } else {
            op.launch(permission)
        }
    }


    private fun ensurePermissions() {
        location.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                requestLocationPermission(
                    locationPermissionsLauncher,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    requestLocationPermission(
                        locationPermissionsLauncher,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    )
                }

                if (notification.isChecked && photos.isChecked && camera.isChecked) {
                    binding.verifyButton.visibility = View.VISIBLE
                } else {
                    binding.verifyButton.visibility = View.GONE
                }
            } else {
                binding.verifyButton.visibility = View.GONE
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }

        photos.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                photosPermissionsLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)

                if (location.isChecked && notification.isChecked && camera.isChecked) {
                    binding.verifyButton.visibility = View.VISIBLE
                } else {
                    binding.verifyButton.visibility = View.GONE
                }
            } else {
                binding.verifyButton.visibility = View.GONE
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }

        camera.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                cameraPermissionsLauncher.launch(Manifest.permission.CAMERA)
                if (location.isChecked && photos.isChecked && notification.isChecked) {
                    binding.verifyButton.visibility = View.VISIBLE
                } else {
                    binding.verifyButton.visibility = View.GONE
                }
            } else {
                binding.verifyButton.visibility = View.GONE
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }

        notification.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(requireContext(), "Permission has been granted", Toast.LENGTH_SHORT)
                    .show()
                if (location.isChecked && photos.isChecked && camera.isChecked) {
                    binding.verifyButton.visibility = View.VISIBLE
                } else {
                    binding.verifyButton.visibility = View.GONE
                }
            } else {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT)
                    .show()
                binding.verifyButton.visibility = View.GONE
            }
        }
    }

    private fun onBoardingFinished() {
        AgentSharePreference(requireContext()).setBoolean(SharedPrefKeys.ONBOARDING_FINISHED, true)
    }
}