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
import android.os.Environment
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
import com.google.android.material.snackbar.Snackbar
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.data.model.verification.upload.UploadImageResponse
import com.youverify.agent_app_android.data.model.verification.id.VerifyIDRequest
import com.youverify.agent_app_android.databinding.FragmentUploadImageBinding
import com.youverify.agent_app_android.util.AgentSharePreference
import com.youverify.agent_app_android.util.ProgressLoader
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.*
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

//        println(args.verifyIdRequest.toString())
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

                        val uri: Uri = selectedImageUri
                        imagePath = copyFile("Agents", "IdImage", uri)
                        val uriString = uri.toString()
                        val myFile = File(uriString)
                        var displayName: String? = null
                        displayName = retrieveSelected(uriString, uri, displayName, myFile)
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

    private fun copyFile(directoryName: String, filename: String, contentUri: Uri): File {
        var inputStream: InputStream? = null
        var outputStream: FileOutputStream? = null
        val directory = File(
            context?.applicationContext?.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
            directoryName
        )
        if (!directory.exists()) {
            directory.mkdir()
        }
        val file = File(directory, filename)
        return try {
            val fileSize = 4096
            val fileReader = ByteArray(fileSize)

            inputStream = context?.contentResolver?.openInputStream(contentUri)
            outputStream = FileOutputStream(file)
            var filesDownloaded: Double = 0.0
            while (true) {
                val read = inputStream?.read(fileReader)
                if (read == -1) {
                    break
                }
                outputStream.write(fileReader, 0, read ?: 0)
                filesDownloaded += read ?: 0

            }
            outputStream.flush()
            inputStream?.close()
            outputStream.close()
            file.absoluteFile
        } catch (exp: Exception) {
            exp.printStackTrace()
            File("")
        } finally {
            inputStream?.close()
            outputStream?.close()
        }
    }

    private fun uploadImage() {

        if(binding.uploadText.text == "Upload"){
            Snackbar.make(requireView(), "Please select a photo", Snackbar.LENGTH_SHORT).show()
        }else{
            val filePart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "files",
                imagePath.name,
                imagePath.asRequestBody("image/png".toMediaTypeOrNull())
            )

            uploadViewModel.uploadImage(uploadRequest = filePart)

            lifecycleScope.launchWhenCreated {
                uploadViewModel.uploadChannel.collect {
                    when (it) {
                        is UploadViewState.Loading -> {
                            progressLoader.show(message = "Uploading Image...")
                        }
                        is UploadViewState.Success -> {
                            progressLoader.hide()
                            passUploadData(it.uploadResponse)
                        }
                        is UploadViewState.Failure -> {
                            progressLoader.hide()
                            Snackbar.make(requireView(), it.errorMessage, Snackbar.LENGTH_LONG).show()
                        }
                        else -> {progressLoader.hide()}
                    }
                }
            }
        }
    }

    private fun passUploadData(uploadResponse: UploadImageResponse?) {
        val verifyIdRequestDummy = VerifyIDRequest(   //Remove this after this endpoint has being rectified
            type = "NIN",
            reference = "11111111111",
            firstName = "Sarah",
            lastName = "Doe",
            dateOfBirth = "1988-04-04",
            imageUrl = "https://i.pinimg.com/originals/93/8d/53/938d536057ba50567ff2c9964386b473.jpg"
        )

        val token = AgentSharePreference(requireContext()).getString("TOKEN")
        val verificationDetails : VerifyIDRequest = args.verifyIdRequest
        verificationDetails.imageUrl = uploadResponse?.data?.get(0)?.location ?: ""

        submitIdInfo(verifyIdRequestDummy)
    }

    private fun submitIdInfo(verificationDetails: VerifyIDRequest){
        val verifyIdRequestDummy = VerifyIDRequest(   //Remove this after this endpoint has being rectified
            type = "NIN",
            reference = "11111111111",
            firstName = "Sarah",
            lastName = "Doe",
            dateOfBirth = "1988-04-04",
            imageUrl = "https://i.pinimg.com/originals/93/8d/53/938d536057ba50567ff2c9964386b473.jpg"
        )

        uploadViewModel.verifyId(verifyIdRequestDummy)

        lifecycleScope.launchWhenCreated {
            uploadViewModel.verifyIdChannel.collect {
                when (it) {
                    is VerifyIdViewState.Loading -> {
                        progressLoader.show(message = "Submitting...")
                    }
                    is VerifyIdViewState.Success -> {
                        progressLoader.hide()
                        AgentSharePreference(requireContext()).setBoolean("IS_VERIFIED",
                            it.verifyIdResponse?.data?.isVerified ?: false
                        )
                        findNavController().navigate(R.id.action_uploadPassportFragment_to_verificationResponseFragment)
                    }
                    is VerifyIdViewState.Failure -> {
                        progressLoader.hide()
                        Snackbar.make(requireView(), it.errorMessage, Snackbar.LENGTH_LONG).show()
                        findNavController().navigate(R.id.action_uploadPassportFragment_to_verificationFailedFragment)
                    }
                    else -> {progressLoader.hide()}
                }
            }
        }
    }

}