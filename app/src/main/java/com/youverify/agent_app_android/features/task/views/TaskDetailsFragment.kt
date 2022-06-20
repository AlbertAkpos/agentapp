package com.youverify.agent_app_android.features.task.views

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import com.youverify.agent_app_android.features.task.TaskViewModel
import com.youverify.agent_app_android.features.verification.id.UploadViewModel
import com.youverify.agent_app_android.features.verification.id.UploadViewState
import com.youverify.agent_app_android.util.Permissions
import com.youverify.agent_app_android.util.ProgressLoader
import com.youverify.agent_app_android.util.SingleEvent
import com.youverify.agent_app_android.util.extension.*
import com.youverify.agent_app_android.util.helper.FileHelper
import com.youverify.agent_app_android.util.helper.LocationHelper
import com.youverify.agent_app_android.util.helper.createMultipart
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.io.File
import java.lang.Exception
import javax.inject.Inject

@AndroidEntryPoint
class TaskDetailsFragment : Fragment(R.layout.fragment_task_details) {
    private lateinit var binding: FragmentTaskDetailsBinding

    /**
     * Steps:
     * 1. Start task - call api
     * 2. Fetch and set response reason endpoint
     */

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
        yesImageList.adapter = imagesAdapter
    }

    private fun setupClicks() = with(binding) {
        proceedToCaptureDetailsBtn.setOnClickListener {
            canLocateAddressContainer.show()
            it.gone()
        }

        yesBtn.setOnClickListener { viewModel.canYouLocateTheAddressState.postValue(SingleEvent(true)) }

        noBtn.setOnClickListener { viewModel.canYouLocateTheAddressState.postValue(SingleEvent(false)) }

        buildingTypeInput.setOnClickListener {
            openSelectTypeBottomSheet(viewModel.typesOfBuildings) { selectedBuilding ->
                viewModel.taskAnswers = viewModel.taskAnswers.copy(buildingType = selectedBuilding)
            }
        }

        buildingColorInput.setOnClickListener { showColorButtomSheet{
            binding.buildingColorInput.setText(it.name)
        }}

        gateColorInput.setOnClickListener { showColorButtomSheet {
            binding.gateColorInput.setText(it.name)
        } }

        noGeoTaginput.setOnClickListener { getCurrentLocation() }

        yesGeoTaginput.setOnClickListener { getCurrentLocation() }

        reasonInput.setOnClickListener {
            showMessagesBottomSheet(viewModel.rejectionMessages) {
                binding.reasonInput.setText(it)
                viewModel.taskAnswers = viewModel.taskAnswers.copy(rejectionReason = it)
            }
        }

        candidateNotImageUploadBtn.setOnClickListener { onPickImageClicked() }

        binding.accessLocBtn.setOnClickListener {
            binding.layoutAccessLoc.endIconDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_up2)
            binding.choiceLayout.show()
        }

        noSubmitButton.setOnClickListener { onNoSubmissionButtionClicked() }

        whoConfirmedAdressInput.setOnClickListener {
            openSelectTypeBottomSheet(viewModel.candidateAddressConfirmedBy) { selected ->
                viewModel.taskAnswers = viewModel.taskAnswers.copy(confirmedBy = selected)
            }
        }

        buildHasGate.setOnClickListener { viewModel.doesBuildingHaveGate.postValue(SingleEvent(true)) }
        buildDontHaveGate.setOnClickListener {
            viewModel.doesBuildingHaveGate.postValue(
                SingleEvent(
                    false
                )
            )
        }

        yesSubmitBtn.setOnClickListener {
            validateYesSubmission {
                //Submit
            }
        }

        candidateLiveHereBtn.setOnClickListener {
            viewModel.doesCandidateLiveAtAddress.postValue(
                SingleEvent(true)
            )
        }
        candidateDontLiveHereBtn.setOnClickListener {
            viewModel.doesCandidateLiveAtAddress.postValue(
                SingleEvent(false)
            )
        }

        toolbar.setNavigationOnClickListener { navigateUp() }

    }

    private fun validateYesSubmission(callback: () -> Unit) {
        val typeOfBuilding = binding.buildingTypeInput.text?.toString()
        val buildingColor = binding.buildingColorInput.text?.toString()
        val hasGate = viewModel.taskAnswers.hasGate
        if (hasGate) {
            val gateColor = binding.gateColorInput.text?.toString()
            if (gateColor.isNullOrEmpty()) {
                binding.gateColorLayout.error = "Please pick a gate color"
                return
            }
        }
    }

    private fun onNoSubmissionButtionClicked() {
        validateNoSubmition { request ->
            viewModel.rejectTask(request, viewModel.currentTask?.id.toString())
        }
    }

    private fun validateNoSubmition(callback: (submitRequest: TasksDto.SubmitTaskRequest) -> Unit) {
        val cantLocateAddressReason = binding.reasonInput.text?.toString()
        val noOfImages = viewModel.imagesPicked.value?.size ?: 0
        val nearestLandmark = binding.landmarkInput.text?.toString()
        val additionalInfo = binding.infoInput.text?.toString() ?: ""
        if (noOfImages < 1) {
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

        val rejectionAnswers = TasksDto.SubmitTaskRequest(
            message = cantLocateAddressReason
        )

        callback(rejectionAnswers)
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
        binding.progressIndicationYes.visibleIf(true)

        locationHelper.requestLocationUpdate {

            locationHelper.getCurrentLocation { latLng, address ->

                Timber.d("Current location: $latLng $address")
                binding.yesGeoTaginput.setText(
                    address ?: "Lat: ${latLng?.lat}  Long: ${latLng?.long}"
                )
                binding.noGeoTaginput.setText(
                    address ?: "Lat: ${latLng?.lat}  Long: ${latLng?.long}"
                )

                // Remove location callback on getting location
                locationHelper.stopLocationUpdate()

                //Hide Loaders
                binding.progressIndicationNo.gone()
                binding.progressIndicationYes.gone()
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

    private fun openSelectTypeBottomSheet(
        list: List<String>,
        callback: (selected: String) -> Unit
    ) {
        var dialog: MaterialDialog? = null
        val binding = SelectTypesLayoutBinding.inflate(layoutInflater)
        val radioButton = RadioButtonLayoutBinding.inflate(layoutInflater)
        for (item in list) {
            binding.radioGroup.removeAllViews()
            radioButton.radio.text = item
            binding.radioGroup.addView(radioButton.root)
        }
        binding.radioGroup.setOnCheckedChangeListener { radioGroup, i ->
            val button = radioGroup?.findViewById<RadioButton>(i)
            if (button?.isChecked == true) {
                val selectedBuilding = button.text?.toString() ?: ""
                dialog?.dismiss()
                callback(selectedBuilding)
            }
        }

        dialog = context?.inflateBottomSheet(binding.root, true)
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
            binding.gateColorLayout.visibleIf(hasGate)
            binding.hasGate.setColor(hasGate, R.color.colorDark, R.color.white)
            binding.noGate.setColor(!hasGate, R.color.colorDark, R.color.white)
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
                        negativeTitle = "Cancel"
                    ) {
                        val file = state.file ?: return@showDialog
                        val multipart = createMultipart(state.file)
                        uploadViewModel.uploadImage(multipart, file)
                    }
                }

                is UploadViewState.Success -> {
                    progressLoader.hide()
                    val result = state.uploadResponse?.data?.firstOrNull()
                    val file = state.file ?: return@observe
                    Timber.d("Upload ==> ${result?.location}")
                    viewModel.uploadedImages.add(result?.location.toString())
                    viewModel.updateImagesPicked(file)
                }
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
            binding.candidateLiveHere.setColor(livesHere, R.color.colorDark, R.color.white)
            binding.candidateLiveHere.setColor(!livesHere, R.color.colorDark, R.color.white)
            binding.whoConfirmedAddressLayout.visibleIf(livesHere)
            binding.whoConfirmedAddress.visibleIf(livesHere)
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

        viewModel.startTask(taskItem.id)

    }

    private fun updateImageList(imageList: ArrayList<File>) {
        imagesAdapter.submitList(imageList)
        imagesAdapter.notifyDataSetChanged()
    }

    //implement this function
    private fun configureUI() {


        binding.yesBtn.setOnClickListener {


            binding.selectReasonText.visibility = View.GONE
            binding.reasonLayout.visibility = View.GONE
            binding.imagesText.visibility = View.GONE
            binding.getTagText.visibility = View.GONE
            binding.landmarkText.visibility = View.GONE
            binding.landmarkLayout.visibility = View.GONE
            binding.infoText.visibility = View.GONE
            binding.infoLayout.visibility = View.GONE
        }

        binding.noBtn.setOnClickListener {

            binding.selectReasonText.visibility = View.VISIBLE
            binding.reasonLayout.visibility = View.VISIBLE
            binding.imagesText.visibility = View.VISIBLE
            binding.getTagText.visibility = View.VISIBLE
            binding.landmarkText.visibility = View.VISIBLE
            binding.landmarkLayout.visibility = View.VISIBLE
            binding.infoText.visibility = View.VISIBLE
            binding.infoLayout.visibility = View.VISIBLE
        }

        binding.reasonInput.setOnClickListener {
            showBottomBar()
        }



        binding.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

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
                binding.reasonInput.setText(choice1.text.toString())
                dialog.dismiss()
            }, 500)
        }

        choice2.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed({
                binding.reasonInput.setText(choice2.text.toString())
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

    private fun showMessagesBottomSheet(reasons: List<String>, callback: (reason: String) -> Unit) {
        Timber.d("Messsage ==> $reasons")
        var dialog: MaterialDialog? = null
        val binding = SelectTypesLayoutBinding.inflate(layoutInflater)
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