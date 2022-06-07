package com.youverify.agent_app_android.features.verification.id

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.data.model.upload.UploadResponse
import com.youverify.agent_app_android.databinding.FragmentUploadPassportBinding
import com.youverify.agent_app_android.util.ProgressLoader
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject


@AndroidEntryPoint
class UploadPassportFragment : Fragment(R.layout.fragment_upload_passport) {

    @Inject
    lateinit var progressLoader: ProgressLoader
    private lateinit var binding: FragmentUploadPassportBinding
    private val uploadViewModel: UploadViewModel by viewModels()
    private val args: UploadPassportFragmentArgs by navArgs()
    private lateinit var imagePath: File

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUploadPassportBinding.inflate(layoutInflater)

        configureUI()

        println(args.uploadInfo.toString())
        return binding.root
    }

    private fun configureUI() {
        binding.submitBtn.setOnClickListener {
            uploadImage()
        }

        binding.backBtn.setOnClickListener {
            activity?.onBackPressed()
        }


        binding.rootUpload.setOnClickListener {
            selectImageFromGallery()
        }
    }

    private val selectImageFromGalleryResult = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri.let {
            if (uri != null) {
                previewImage.setImageURI(uri)     //setting the image on the image view here
                binding.imagesLayout.visibility = View.VISIBLE

                println(uri.path)
                imagePath = File(uri.path.toString())
                println(imagePath)
            }
        }
    }

    private val previewImage by lazy { binding.imageView }     //postpone imageview object creation to when needed

    private fun selectImageFromGallery() = selectImageFromGalleryResult.launch("image/*")

    private fun uploadImage() {

        val filePart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "files",
            imagePath.name,
            imagePath.asRequestBody("image/*".toMediaTypeOrNull())
        )

        println("Upload Request: $filePart")
        uploadViewModel.uploadImage(uploadRequest = filePart)

        lifecycleScope.launchWhenCreated {
            uploadViewModel.uploadChannel.collect {
                when (it) {
                    is UploadViewState.Loading -> {
                        progressLoader.show(message = "Saving photo...")
                    }
                    is UploadViewState.Success -> {
                        progressLoader.hide()
                        passUploadData(it.uploadResponse)
                        findNavController().navigate(R.id.action_uploadPassportFragment_to_verificationResponseFragment)
                    }
                    is UploadViewState.Failure -> {
                        progressLoader.hide()
                        Toast.makeText(requireContext(), it.errorMessage, Toast.LENGTH_LONG)
                            .show()
                        findNavController().navigate(R.id.action_uploadPassportFragment_to_verificationFailedFragment)
                    }
                    else -> {}
                }
            }
        }
    }

    private fun passUploadData(uploadResponse: UploadResponse?) {
        println("Response: $uploadResponse")
    }
}