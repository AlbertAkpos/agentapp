package com.youverify.agent_app_android.features.task.views

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.ThumbnailUtils
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.*
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.core.functional.ResultState
import com.youverify.agent_app_android.data.model.tasks.TasksDomain
import com.youverify.agent_app_android.data.model.tasks.TasksDto
import com.youverify.agent_app_android.databinding.*
import com.youverify.agent_app_android.features.common.adapter.createColorsAdapter
import com.youverify.agent_app_android.features.common.adapter.createImagesAdapter
import com.youverify.agent_app_android.features.customview.PaintView
import com.youverify.agent_app_android.features.task.TaskViewModel
import com.youverify.agent_app_android.features.verification.id.UploadViewModel
import com.youverify.agent_app_android.features.verification.id.UploadViewState
import com.youverify.agent_app_android.util.*
import com.youverify.agent_app_android.util.extension.*
import com.youverify.agent_app_android.util.helper.FileHelper
import com.youverify.agent_app_android.util.helper.LocationHelper
import com.youverify.agent_app_android.util.helper.createMultipart
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class TaskDetailsFragment : Fragment(R.layout.fragment_task_details) {
    private lateinit var binding: FragmentTaskDetailsBinding



    @Inject
    lateinit var locationHelper: LocationHelper

    private var colorDialog: MaterialDialog? = null

    @Inject
    lateinit var fileHelper: FileHelper

    @Inject
    lateinit var progressLoader: ProgressLoader


    private val viewModel by activityViewModels<TaskViewModel>()

    private val uploadViewModel by viewModels<UploadViewModel>()

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permission ->
        val somePermissionsNotGranted = permissions.any {
            ContextCompat.checkSelfPermission(
                requireContext(),
                it
            ) != PackageManager.PERMISSION_GRANTED
        }
        if (somePermissionsNotGranted) {
            context?.showDialog(
                "Permission required",
                message = "You need to accept all permissions for us to get your location",
                negativeTitle = "Cancel",
                positiveTitle = "Continue"
            ) {}
        } else {
            getCurrentLocation()
        }
    }


    private val imageResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            val granted = result.entries.all {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    it.key
                ) == PackageManager.PERMISSION_GRANTED
            }
            if (granted) {
                // Pick image file
                pickImage()
            } else {
                context?.showDialog(message = "Please accept all permissions to continue")
            }
        }

    private val pickImageLaucher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val imageBitMap = it.data?.extras?.get("data") as? Bitmap
                Timber.d("Image picked ==> $imageBitMap")

                if (imageBitMap != null) {
                    val file = fileHelper.writeToFile(imageBitMap, "image")

                    Timber.d("File gotten: ${file?.absolutePath}")
                    if (file != null) {
                        val requestBody = createMultipart(file)
                        uploadViewModel.uploadImage(requestBody, file)
                    }
                }
            }
        }

    private val imagesAdapter = createImagesAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupClicks()
        setObservers()
    }

    private fun setupUI() = with(binding) {
        noImageList.adapter = imagesAdapter
        canAccessBuildingContainer.yesImageList.adapter = imagesAdapter
        reasonLayout.autoClearError()
        canAccessBuildingContainer.buildTypeLayout.autoClearError()
        canAccessBuildingContainer.buildingColorLayout.autoClearError()
        canAccessBuildingContainer.gateColorLayout.autoClearError()
        noGeoTaglayout.autoClearError()
        canAccessBuildingContainer.yesGeoTaglayout.autoClearError()
        reasonLayout.autoClearError()
        canAccessBuildingContainer.whoConfirmedAddressLayout.autoClearError()

    }

    private fun setupClicks() = with(binding) {
        proceedToCaptureDetailsBtn.setOnClickListener {
            canLocateAddressContainer.show()
            it.gone()
            Timber.d("Scroll view focus")
            scrollView.post {
                scrollView.smoothScrollTo(0, choiceBuildingAccessayout.y.toInt() + 40)
            }
        }

        yesBtn.setOnClickListener { viewModel.canYouLocateTheAddressState.postValue(SingleEvent(true)) }

        noBtn.setOnClickListener { viewModel.canYouLocateTheAddressState.postValue(SingleEvent(false)) }

        canAccessBuildingContainer.buildingTypeInput.setOnClickListener {
            showMessagesBottomSheet( "Select building type", viewModel.typesOfBuildings) { selectedBuilding ->
                canAccessBuildingContainer.buildingTypeInput.setText(selectedBuilding)
                viewModel.taskAnswers = viewModel.taskAnswers.copy(buildingType = selectedBuilding)
            }
        }

        canAccessBuildingContainer.buildingColorInput.setOnClickListener { showColorButtomSheet {
            binding.canAccessBuildingContainer.buildingColorInput.setText(it.name)
            viewModel.taskAnswers = viewModel.taskAnswers.copy(buildingColor = it.name)
        }}

        canAccessBuildingContainer.gateColorInput.setOnClickListener { showColorButtomSheet {
            binding.canAccessBuildingContainer.gateColorInput.setText(it.name)
        } }

        noGeoTaginput.setOnClickListener { getCurrentLocation() }

        canAccessBuildingContainer.yesGeoTaginput.setOnClickListener { getCurrentLocation() }

        cantLocateAddressReasonInput.setOnClickListener {
            showMessagesBottomSheet( "Select reason", viewModel.cantLocateAddressReasons) {
                binding.cantLocateAddressReasonInput.setText(it)
                viewModel.taskAnswers = viewModel.taskAnswers.copy(rejectionReason = it)
            }
        }


        candidateNotImageUploadBtn.setOnClickListener { onPickImageClicked() }

        canAccessBuildingContainer.candidateImageUploadBtn.setOnClickListener { onPickImageClicked() }

        binding.accessLocBtn.setOnClickListener {
            binding.layoutAccessLoc.endIconDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_up2)
            binding.choiceLayout.show()
        }

        noSubmitButton.setOnClickListener { onNoSubmissionButtionClicked() }

        canAccessBuildingContainer.whoConfirmedAdressInput.setOnClickListener {
            showMessagesBottomSheet("Who confirmed address",  viewModel.candidateAddressConfirmedBy) { selected ->
                canAccessBuildingContainer.whoConfirmedAdressInput.setText(selected)
                viewModel.taskAnswers = viewModel.taskAnswers.copy(confirmedBy = selected)
            }
        }

        canAccessBuildingContainer.buildHasGate.setOnClickListener { viewModel.doesBuildingHaveGate.postValue(SingleEvent(true)) }
        canAccessBuildingContainer.buildDontHaveGate.setOnClickListener {
            viewModel.doesBuildingHaveGate.postValue(
                SingleEvent(
                    false
                )
            )
        }

        canAccessBuildingContainer.yesSubmitBtn.setOnClickListener {
            validateYesSubmission { taskItem ->
                viewModel.updateAndSubmitTask(taskItem)
            }
        }

        canAccessBuildingContainer.candidateLiveHereBtn.setOnClickListener {
            viewModel.doesCandidateLiveAtAddress.postValue(
                SingleEvent(true)
            )
        }
        canAccessBuildingContainer.candidateDontLiveHereBtn.setOnClickListener {
            viewModel.doesCandidateLiveAtAddress.postValue(
                SingleEvent(false)
            )
        }

        toolbar.setNavigationOnClickListener { navigateUp() }

        canAccessBuildingContainer.signBtn.setOnClickListener { showSignatureDialog() }

        signBtnTwo.setOnClickListener { showSignatureDialog() }

        yesCanAccessBuildingBtn.setOnClickListener { viewModel.canAccessBuildingState.postValue(SingleEvent(true)) }

        noCantAccessBuildingBtn.setOnClickListener { viewModel.canAccessBuildingState.postValue(
            SingleEvent(false)
        ) }

        cantAccessBuildingContainer.notifyBusinessBtn.setOnClickListener { onNotifyBusinessClicked() }

    }


    private fun doTaskReset() {
        viewModel.uploadedImages.clear()
        viewModel.imagesPicked.value = arrayListOf<File>()
    }

    private fun onNotifyBusinessClicked() {
            viewModel.submitTask(TasksDto.SubmitTaskRequest(message = "Address not accessible"), viewModel.currentTask?.id.toString())
    }

    private fun validateYesSubmission(callback: (taskItem: TasksDomain.SubmitTask) -> Unit) {
        val typeOfBuilding = binding.canAccessBuildingContainer.buildingTypeInput.text?.toString()
        val buildingColor = binding.canAccessBuildingContainer.buildingColorInput.text?.toString()
        val hasGate = viewModel.taskAnswers.hasGate
        val agentSignature = viewModel.taskAnswers.signatureLink
        val confirmedBy = binding.canAccessBuildingContainer.whoConfirmedAdressInput.text?.toString() ?: ""
        val needsConfirmation = viewModel.taskAnswers.needsConfirmation
        val latLng = viewModel.taskAnswers.latLong
        var gateColor = "Nil"
        var message = "Candidate lives there"

        if (typeOfBuilding.isNullOrEmpty()) {
            binding.canAccessBuildingContainer.buildTypeLayout.error = "Please select a building type"
            binding.scrollView.scrollTo(0, binding.canAccessBuildingContainer.buildTypeLayout.y.toInt() + 500)
            return
        }

        if (buildingColor.isNullOrEmpty()) {
            binding.canAccessBuildingContainer.buildingColorLayout.error = "Please pick a building color"
            binding.scrollView.scrollTo(0, binding.canAccessBuildingContainer.buildingColorLayout.y.toInt() + 500)
            return
        }

        if (hasGate) {
            gateColor = binding.canAccessBuildingContainer.gateColorInput.text?.toString() ?: ""
            if (gateColor.isNullOrEmpty()) {
                binding.canAccessBuildingContainer.gateColorLayout.error = "Please pick a gate color"
                binding.scrollView.scrollTo(0, binding.canAccessBuildingContainer.gateColorLayout.y.toInt() + 500)

                return
            }
        } else gateColor = "Nil"

        if (needsConfirmation == null) {
            context?.showDialog( title = "Incomplete form", message = "Please see question \"Does the candidate live here?\"")
            return
        }

        if (confirmedBy.isNullOrEmpty()) {
            binding.canAccessBuildingContainer.whoConfirmedAddressLayout.error = "Please select who confirmed address"
            binding.scrollView.scrollTo(0, binding.canAccessBuildingContainer.whoConfirmedAddressLayout.y.toInt()+ 500)
            return
        }


        if (agentSignature.isEmpty() && viewModel.offlineSignature.isEmpty()) {
            context?.showDialog(title = "Incomplete form",  message = "Please input your signature")
            binding.scrollView.scrollTo(0, binding.canAccessBuildingContainer.signBtn.y.toInt() + 500)
            return
        }

        if (viewModel.uploadedImages.isEmpty()  && viewModel.offlinePhotos.isEmpty()) {
            context?.showDialog(title = "Incomplete form", message = "Please add images")
            binding.scrollView.scrollTo(0, binding.canAccessBuildingContainer.candidateImageUploadBtn.y.toInt() + 500)
            return
        }

        if (latLng == null) {
            binding.canAccessBuildingContainer.yesGeoTaglayout.error = "Please geo tag your location to continue"
            binding.scrollView.scrollTo(0, binding.canAccessBuildingContainer.yesGeoTaglayout.y.toInt() + 500)
            return
        }

        if (needsConfirmation) {
           message =  if (confirmedBy == Constants.NO_ONE) {
                "Candidate does not live there"
            } else {
                "Could not confirm that the candidate lives there"
            }
        }

        val photos = viewModel.uploadedImages.map {
            val coordinates = TasksDto.UpdateTaskLocation(lat = latLng.lat, long = latLng.long)
            TasksDto.UpdateTaskPhoto(url = it, location = coordinates)
        }

        val updateTaskRequest = TasksDto.UpdateTaskRequest(
            gatePresent = hasGate,
            buildingColour = buildingColor,
            agentSignature = agentSignature,
            confirmedBy = confirmedBy,
            buildingType = typeOfBuilding,
            gateColor = gateColor ?: "Nil",
            photos = photos,
            location = TasksDto.Coordinates(long = latLng.long, lat = latLng.lat)
        )


        val submitRequest = TasksDto.SubmitTaskRequest(message)

        val taskItem = TasksDomain.SubmitTask(
            taskId = viewModel.currentTask?.id.toString(),
            updateTaskRequest = updateTaskRequest,
            message = confirmedBy,
            subitTaskRequest = submitRequest,
            offlineSignature = viewModel.offlineSignature,
            offlinePhotos = viewModel.offlinePhotos
        )

        if (viewModel.offlinePhotos.isNotEmpty() || viewModel.offlineSignature.isNotEmpty()) {
            viewModel.updateTaskOnLocale(taskItem)
            context?.showDialog(title = "Notice", message = "Task has been saved offline") {
                navigateUp()
            }
            return
        }

        callback(taskItem)

    }

    private fun onNoSubmissionButtionClicked() {
        validateNoSubmition { request ->
            viewModel.updateAndSubmitTask(request)
        }
    }

    private fun validateNoSubmition(callback: (submitRequest: TasksDomain.SubmitTask) -> Unit) {
        val cantLocateAddressReason = binding.cantLocateAddressReasonInput.text?.toString()
        val noOfImages = viewModel.imagesPicked.value?.size ?: 0
        val nearestLandmark = binding.landmarkInput.text?.toString()
        val additionalInfo = binding.infoInput.text?.toString() ?: ""
        val latLng = viewModel.taskAnswers.latLong
        val agentSignature = viewModel.taskAnswers.signatureLink

        if (noOfImages < 1 && viewModel.offlinePhotos.isEmpty()) {
            context?.showDialog(
                title = "Incomplete form",
                message = "Please take pictures of the place"
            )
            return
        }

        if (cantLocateAddressReason.isNullOrEmpty()) {
            binding.reasonLayout.error = "Please select a reason"
            return
        }

        if (nearestLandmark.isNullOrEmpty()) {
            binding.landmarkLayout.error = "Please enter the nearest landmark"
            return
        }

        if (latLng == null) {
            binding.noGeoTaglayout.error = "Please geo tag your location to continue"
            return
        }

        if (agentSignature.isEmpty() && viewModel.offlineSignature.isEmpty()) {
            context?.showDialog(title = "Incomplete form",  message = "Please input your signature")
            return
        }


        val photos = viewModel.uploadedImages.map {
            val coordinates = TasksDto.UpdateTaskLocation(lat = latLng.lat, long = latLng.long)
            TasksDto.UpdateTaskPhoto(url = it, location = coordinates)
        }

        val updateTaskRequest = TasksDto.UpdateTaskRequest(
            gatePresent = false,
            buildingColour = "Nil",
            agentSignature = agentSignature,
            confirmedBy = "Nil",
            buildingType = "Nil",
            gateColor = "Nill",
            photos = photos,
            location = TasksDto.Coordinates(long = latLng.long, lat = latLng.lat)
        )


        val submitRequest = TasksDto.SubmitTaskRequest(cantLocateAddressReason)

        val taskItem = TasksDomain.SubmitTask(
            taskId = viewModel.currentTask?.id.toString(),
            updateTaskRequest = updateTaskRequest,
            message = cantLocateAddressReason,
            subitTaskRequest = submitRequest,
            offlinePhotos = viewModel.offlinePhotos,
            offlineSignature = viewModel.offlineSignature
        )

        if (viewModel.offlinePhotos.isNotEmpty() || viewModel.offlineSignature.isNotEmpty()) {
            viewModel.updateTaskOnLocale(taskItem)
            context?.showDialog(title = "Notice", message = "Task has been saved offline") {
                navigateUp()
            }
            return
        }

        callback(taskItem)


    }

    private fun getCurrentLocation() {
        val somePermissionsNotGranted = permissions.any {
            ContextCompat.checkSelfPermission(
                requireContext(),
                it
            ) != PackageManager.PERMISSION_GRANTED
        }
        if (somePermissionsNotGranted) {
            context?.showDialog(
                "Permission required",
                message = "Please accept all permissions to continue",
                negativeTitle = "Cancel",
                positiveTitle = "Continue"
            ) {
                requestNeededPermissions()
            }
            return
        }

        if (!locationHelper.isGpsEnabled()) {
            locationHelper.onLocationSettingsSuccessListener { getLocation() }
                .addOnFailureListener { doLocationResolution(it) }
        } else {
            getLocation()
        }

    }

    private fun onPickImageClicked() {
        val granted = context?.isPermissionsGranted(*imagePermissions) == true
        if (!granted) {
            requestImagePermissions()
            return
        }

        pickImage()
    }

    private fun requestImagePermissions() {
        imageResultLauncher.launch(imagePermissions)
    }

    @RequiresPermission(allOf = [Permissions.ACCESS_COARSE_LOCATION, Permissions.ACCESS_BACKGROUND_LOCATION, Permissions.ACCESS_FINE_LOCATION])
    private fun getLocation() {
        Timber.d("getLocation()")
        binding.progressIndicationNo.visibleIf(true)
        binding.canAccessBuildingContainer.progressIndicationYes.visibleIf(true)

        locationHelper.requestLocationUpdate {

            locationHelper.getCurrentLocation { latLng, address ->

                Timber.d("Current location: $latLng $address")
                val locationAddress = "Location captured"  //address ?: "Lat: ${latLng?.lat}  Long: ${latLng?.long}"

                if (latLng != null) {
                    binding.canAccessBuildingContainer.yesGeoTaginput.setText(locationAddress)
                    binding.noGeoTaginput.setText(locationAddress)
                    val color = ContextCompat.getColor(requireContext(), R.color.colorDark)

                    binding.canAccessBuildingContainer.yesGeoTaglayout.setBackgroundColor(color)
                    binding.noGeoTaglayout.setBackgroundColor(color)

                    val whiteColor = ContextCompat.getColor(requireContext(), R.color.white)
                    binding.canAccessBuildingContainer.yesGeoTaginput.setTextColor(whiteColor)
                    binding.noGeoTaginput.setTextColor(whiteColor)

                    viewModel.taskAnswers = viewModel.taskAnswers.copy(latLong = latLng)
                } else {
                    binding.canAccessBuildingContainer.yesGeoTaginput.setText("Capture failed")
                    binding.noGeoTaginput.setText("Capture failed")
                }

                locationHelper.resetLocationCallback()
                // Remove location callback on getting location
                locationHelper.stopLocationUpdate()

                //Hide Loaders
                binding.progressIndicationNo.gone()
                binding.canAccessBuildingContainer.progressIndicationYes.gone()
            }
        }
    }

    private fun doLocationResolution(exception: Exception) {
        if (exception is ApiException) {
            when (exception.statusCode) {
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                    try {
                        val rae = exception as? ResolvableApiException
                        activity?.apply {
                            rae?.startResolutionForResult(this, LOCATION_RESOLUTION_RC)
                        }
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                        Timber.e("Location settings error $ex")
                    }
                }
            }
        }
    }

    private fun requestNeededPermissions() {
        locationPermissionRequest.launch(permissions)
    }

    private fun showSignatureDialog() {
        var dialog: MaterialDialog? = null
        val binding = SignatureLayoutBinding.inflate(layoutInflater)
        dialog = context?.inflateDialog(binding.root, cancelable = true)

        val layoutParams = dialog?.window?.attributes
        layoutParams?.height = 640
        layoutParams?.width = 720
        val paintView = PaintView(requireContext(), layoutParams)

        binding.frameLayout.addView(paintView)
        paintView.requestFocus()

        binding.saveBtn.setOnClickListener {
            dialog?.dismiss()
            val signatureBitmap = ThumbnailUtils.extractThumbnail(paintView.cachebBitmap, 360, 256)
            if (signatureBitmap != null) {
                val file = fileHelper.writeToFile(signatureBitmap, "image")
                Timber.d("File gotten: ${file?.absolutePath}")
                if (file != null) {
                    val requestBody = createMultipart(file)
                    uploadViewModel.uploadImage(requestBody, file, UploadViewState.Companion.UploadType.signatureUpload)
                }
            }
        }

        paintView.somethingDrawn.observe(viewLifecycleOwner) {
            binding.saveBtn.isEnabled = it
        }


        binding.clearBtn.setOnClickListener { paintView.clear() }
    }

    private fun showColorButtomSheet(callback: (color: TasksDomain.Color) -> Unit) {
        val binding = BottomSelectColorBinding.inflate(layoutInflater)
        binding.colorList.adapter = createColorsAdapter {
            callback(it)
            colorDialog?.dismiss()
        }.apply {
            submitList(viewModel.colors)
        }

        colorDialog = context?.inflateBottomSheet(binding.root, true)

    }

    private fun pickImage() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra("android.intent.extras.CAMERA_FACING", 1)
        pickImageLaucher.launch(intent)
    }

    private fun setObservers() {
        viewModel.taskItemState.observe(viewLifecycleOwner) {
            val taskItem = it.getContentIfNotHandled() ?: return@observe
            updateUI(taskItem)
        }

        viewModel.canYouLocateTheAddressState.observe(viewLifecycleOwner) {
            val state = it.getContentIfNotHandled() ?: return@observe
            binding.yesCanLocateAddress.setColor(state, R.color.colorDark, R.color.white)
            binding.noCantLocateAddress.setColor(!state, R.color.colorDark, R.color.white)
            binding.proceedToCaptureDetailsBtn.visibleIf(state && binding.canLocateAddressContainer.visibility == View.GONE)
            if (!state) {
                binding.canLocateAddressContainer.gone()
            }
            binding.cantLocateAddressContainer.visibleIf(!state)
            // When state changes, do reset
            doTaskReset()

        }

        viewModel.imagesPicked.observe(viewLifecycleOwner) {
            val imageList = it ?: return@observe
            updateImageList(imageList)
        }

        viewModel.taskRejectionState.observe(viewLifecycleOwner) {
            val state = it.getContentIfNotHandled() ?: return@observe
            when (state) {
                is ResultState.Loading -> progressLoader.show(message = "Loading...")
                is ResultState.Error -> {
                    progressLoader.hide()
                    context?.showDialog(message = state.error)
                }
                is ResultState.Success -> {
                    showMessage(state.data)
                }
            }
        }

        viewModel.doesBuildingHaveGate.observe(viewLifecycleOwner) {
            val hasGate = it.getContentIfNotHandled() ?: return@observe
            binding.canAccessBuildingContainer.gateColorLayout.visibleIf(hasGate)
            binding.canAccessBuildingContainer.hasGate.setColor(hasGate, R.color.colorDark, R.color.white)
            binding.canAccessBuildingContainer.noGate.setColor(!hasGate, R.color.colorDark, R.color.white)
            viewModel.taskAnswers = viewModel.taskAnswers.copy(hasGate = hasGate)
        }

        uploadViewModel.uploadState.observe(viewLifecycleOwner) {
            val state = it.getContentIfNotHandled() ?: return@observe
            when (state) {
                is UploadViewState.Loading -> {
                    progressLoader.show("Uploading image...")
                }

                is UploadViewState.Failure -> {
                    progressLoader.hide()
                    context?.showDialog(
                        "Upload error",
                        state.errorMessage,
                        positiveTitle = "Retry",
                        negativeTitle = "Continue offline",
                        negativeCallback = {
                            if (state.uploadType == UploadViewState.Companion.UploadType.imageUpload) {
                                viewModel.offlineSignature = state.file?.absolutePath.toString()
                                state.file?.let { viewModel.updateImagesPicked(state.file) }
                            } else {
                                viewModel.offlinePhotos.add(state.file?.absolutePath.toString())
                                binding.canAccessBuildingContainer.signature.loadImage(state.file?.absolutePath)
                                binding.signatureTwo.loadImage(state.file?.absolutePath)
                            }
                        }
                    ) {
                        val file = state.file ?: return@showDialog
                        val multipart = createMultipart(state.file)
                        uploadViewModel.uploadImage(multipart, file, state.uploadType)
                    }
                }

                is UploadViewState.Success -> {
                    progressLoader.hide()
                   processImageUpload(state)
                }
                else -> {}
            }
        }

        viewModel.startTaskState.observe(viewLifecycleOwner) {
            val state = it.getContentIfNotHandled() ?: return@observe
            when (state) {
                is ResultState.Loading -> progressLoader.show("Starting task. Please wait...")
                is ResultState.Error -> {
                    progressLoader.hide()
                    context?.showDialog(
                        "Error",
                        message = state.error,
                        positiveTitle = "Retry",
                        negativeTitle = "Cancel",
                        negativeCallback = { /*navigateUp()*/ }) {
                        viewModel.startTask(viewModel.currentTask?.id.toString())
                    }
                }

                is ResultState.Success -> {
                    progressLoader.hide()
                }
            }
        }

        viewModel.doesCandidateLiveAtAddress.observe(viewLifecycleOwner) {
            val livesHere = it.getContentIfNotHandled() ?: return@observe
            Timber.d("Lives here $livesHere")
            binding.canAccessBuildingContainer.candidateLiveHere.setColor(livesHere, R.color.colorDark, R.color.white)
            binding.canAccessBuildingContainer.candidateDontLiveHere.setColor(!livesHere, R.color.colorDark, R.color.white)
            viewModel.taskAnswers = viewModel.taskAnswers.copy(needsConfirmation = !livesHere)

            val livesHereText = if (livesHere) "Who confirmed that the candidate lives here?" else "Who confirmed that the candidate does'nt live here?"
            binding.canAccessBuildingContainer.whoConfirmedAddress.text = livesHereText

        }

        viewModel.updateAndSubmitTaskState.observe(viewLifecycleOwner) {
            val state = it.getContentIfNotHandled() ?: return@observe
            when(state) {
                is ResultState.Loading -> progressLoader.show("Submitting task...")

                is ResultState.Error -> {
                    progressLoader.hide()
                    context?.showDialog(message = "Unable to submit task. This task has been saved offline") {
                        navigateUp()
                    }
                }

                is ResultState.Success -> {
                    progressLoader.hide()
                    showMessage(state.data)
                }

            }
        }

        viewModel.canAccessBuildingState.observe(viewLifecycleOwner) {
            val canAccessBuilding = it.getContentIfNotHandled() ?: return@observe
            binding.yesCanAccessBuildingText.setColor(canAccessBuilding, R.color.colorDark, R.color.white)
            binding.noCantAccessBuidlingText.setColor(!canAccessBuilding, R.color.colorDark, R.color.white)
            binding.canAccessBuildingContainer.root.visibleIf(canAccessBuilding)
            binding.cantAccessBuildingContainer.root.visibleIf(!canAccessBuilding)

            binding.scrollView.scrollTo(
               0,
                binding.scrollView.bottom + 500
            )
        }

        viewModel.submitTaskState.observe(viewLifecycleOwner) {
            val state = it.getContentIfNotHandled() ?: return@observe
            when(state) {
                is ResultState.Loading -> progressLoader.show("Loading...")
                is ResultState.Error -> {
                    progressLoader.hide()
                    context?.showDialog(message = state.error)
                }
                is ResultState.Success -> {
                    progressLoader.hide()
                    showMessage("The business have been notified")
                }
            }
        }
    }

    private fun showMessage(message: String) {
        var dialog: MaterialDialog? = null
        val binding = LayoutSuccessBinding.inflate(layoutInflater)
        binding.message.text = message

        binding.process.setOnClickListener {
            dialog?.dismiss()
            navigateUp()
        }

        dialog = context?.inflateDialog(binding.root)

    }

    private fun navigateUp() {
        when (findNavController().graph.startDestinationId) {
            findNavController().currentDestination?.id -> {
                activity?.finish()
            }
            else -> {
                findNavController().navigateUp()
            }
        }
    }

    private fun updateUI(taskItem: TasksDomain.AgentTask) = with(binding) {
        verificationTypeText.text = taskItem.verificationType
        candidateAddress.text = taskItem.address
        candidateImage.loadImage(taskItem.candidate?.photo)
        candidateName.text = taskItem.candidate?.name

        //Only task is unasinged
        if (taskItem.status == TaskStatus.unasigned) {
            viewModel.startTask(taskItem.id)
        }

    }

    private fun updateImageList(imageList: ArrayList<File>) {
        imagesAdapter.submitList(imageList)
        imagesAdapter.notifyDataSetChanged()
    }



    private fun submit() {
        val dialogBuilder =
            AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog).create()
        val view = layoutInflater.inflate(R.layout.success_dialog, null)
        val okButton = view.findViewById<TextView>(R.id.text_ok)
        dialogBuilder.setView(view)

        okButton.setOnClickListener {
            dialogBuilder.dismiss()
            activity?.onBackPressed()
        }
        dialogBuilder.setCancelable(false)
        dialogBuilder.show()
        dialogBuilder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun showBottomBar() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_select_reason)

        val choice1 = dialog.findViewById<AppCompatRadioButton>(R.id.not_exist_btn)
        val choice2 = dialog.findViewById<AppCompatRadioButton>(R.id.incorrect_btn)


        choice1.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed({
                binding.cantLocateAddressReasonInput.setText(choice1.text.toString())
                dialog.dismiss()
            }, 500)
        }

        choice2.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed({
                binding.cantLocateAddressReasonInput.setText(choice2.text.toString())
                dialog.dismiss()
            }, 500)
        }

        dialog.show()
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setGravity(Gravity.BOTTOM)
        dialog.window?.attributes?.windowAnimations = R.style.BottomDialogAnimation
    }

    private fun processImageUpload(state: UploadViewState.Success) {
        val result = state.uploadResponse?.data?.firstOrNull()
        val file = state.file ?: return
        Timber.d("Upload ==> ${result?.location}")
        when(state.uploadType) {
            UploadViewState.Companion.UploadType.imageUpload -> {
                viewModel.uploadedImages.add(result?.location.toString())
                viewModel.updateImagesPicked(file)
            }

            UploadViewState.Companion.UploadType.signatureUpload -> {
                viewModel.taskAnswers = viewModel.taskAnswers.copy(signatureLink = result?.location.toString())
                binding.canAccessBuildingContainer.signature.loadImage(file.absolutePath)
                binding.signatureTwo.loadImage(file.absolutePath)
            }

        }
    }

    private fun showMessagesBottomSheet(title: String, reasons: List<String>, callback: (reason: String) -> Unit) {
        Timber.d("Messsage ==> $reasons")
        var dialog: MaterialDialog? = null
        val binding = SelectTypesLayoutBinding.inflate(layoutInflater)
        binding.selectReasonText.text = title
        binding.radioGroup.removeAllViews()
        for (item in reasons) {
            val radioButton = RadioButtonLayoutBinding.inflate(layoutInflater, binding.root, false)
            radioButton.radio.text = item
            radioButton.radio.id = item.hashCode()
            binding.radioGroup.addView(radioButton.root)
        }
        binding.radioGroup.setOnCheckedChangeListener { radioGroup, i ->
            val button = radioGroup?.findViewById<RadioButton>(i)
            if (button?.isChecked == true) {
                val reason = button.text?.toString() ?: ""
                dialog?.dismiss()
                callback(reason)
            }
        }

        dialog = context?.inflateBottomSheet(binding.root, cancelable = true)
    }

    @Deprecated("Deprecated in Java")
    @Suppress("deprecation")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOCATION_RESOLUTION_RC && resultCode != Activity.RESULT_OK) {
            context?.toast("Please accept all permissions")
        }
    }

    companion object {
        private const val LOCATION_RESOLUTION_RC = 209
        private val permissions =
            arrayOf(Permissions.ACCESS_COARSE_LOCATION, Permissions.ACCESS_FINE_LOCATION)
        private val imagePermissions = arrayOf(
            Permissions.CAMERA,
            Permissions.READ_EXTERNAL_STORAGE,
            Permissions.WRITE_EXTERNAL_STORAGE
        )
    }
}