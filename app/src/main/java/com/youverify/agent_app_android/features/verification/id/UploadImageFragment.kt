package com.youverify.agent_app_android.features.verification.id

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
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
import com.youverify.agent_app_android.data.model.verification.upload.UploadImageResponse
import com.youverify.agent_app_android.databinding.FragmentUploadImageBinding
import com.youverify.agent_app_android.util.ProgressLoader
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileDescriptor
import java.io.IOException
import javax.inject.Inject


@AndroidEntryPoint
class UploadImageFragment : Fragment(R.layout.fragment_upload_image) {

    @Inject
    lateinit var progressLoader: ProgressLoader
    private lateinit var binding: FragmentUploadImageBinding
    private val uploadViewModel: UploadViewModel by viewModels()
    private val args: UploadImageFragmentArgs by navArgs()
    private lateinit var imagePath: File

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUploadImageBinding.inflate(layoutInflater)

        configureUI()

        println(args.verifyIdRequest.toString())
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

    private fun selectImageFromGallery() = openUtility()

    private fun openUtility() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        resultLauncherUtility.launch(intent)
    }

    private var resultLauncherUtility =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                documentEventUtility(data)
            }
        }

    private fun documentEventUtility(data: Intent?) {
        if (data != null) {
            try {
                val selectedImageUri: Uri? = data.data
                if (selectedImageUri != null) {
                    val imagepath = getPath(selectedImageUri)

                    if (imagepath != null) {

                        val uri: Uri? = selectedImageUri
                        val uriString = uri.toString()
                        val myFile = File(uriString)
                        var displayName: String? = null
                        displayName = retrieveSelected(uriString, uri, displayName, myFile)
                        imagePath = File(displayName!!)
                        binding.uploadText.text = displayName
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getPath(uri: Uri?): Bitmap? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)

        val cursor: Cursor =
            requireActivity().contentResolver.query(uri!!, projection, null, null, null)!!

        if (cursor.moveToFirst()) {
            if (Build.VERSION.SDK_INT >= 29) {
                try {
                    requireActivity().contentResolver.openFileDescriptor(uri, "r").use { img ->
                        if (img != null) {
                            return BitmapFactory.decodeFileDescriptor(img.fileDescriptor)
                        }
                    }
                } catch (ex: IOException) {
                }
            } else {
                try {
                    val parcelFileDescriptor =
                        requireActivity().contentResolver.openFileDescriptor(uri, "r")
                    val fileDescriptor: FileDescriptor = parcelFileDescriptor!!.fileDescriptor
                    val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
                    parcelFileDescriptor.close()
                    return image
                } catch (ex: IOException) {
                    ex.printStackTrace()
                }
            }
            cursor.close()
        }
        return null
    }


    @SuppressLint("Range")
    private fun retrieveSelected(
        uriString: String,
        uri: Uri?,
        displayName: String?,
        myFile: File,
    ): String? {
        var displayName1 = displayName
        if (uriString.startsWith("content://")) {
            var cursor: Cursor? = null
            try {
                if (uri != null) {
                    cursor = requireActivity().contentResolver.query(uri, null, null, null, null)
                    if (cursor != null && cursor.moveToFirst()) {
                        displayName1 =
                            cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    }
                }
            } finally {
                cursor!!.close()
            }
        } else if (uriString.startsWith("file://")) {
            displayName1 = myFile.name
        }
        return displayName1
    }


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
                        progressLoader.show(message = "Submitting...")
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

    private fun passUploadData(uploadResponse: UploadImageResponse?) {
        println("Response: $uploadResponse")
    }
}