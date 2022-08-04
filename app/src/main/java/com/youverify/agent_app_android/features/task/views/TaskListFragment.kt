package com.youverify.agent_app_android.features.task.views

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.ncorti.slidetoact.SlideToActView
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.core.functional.ResultState
import com.youverify.agent_app_android.data.model.signup.State
import com.youverify.agent_app_android.data.model.tasks.TasksDomain
import com.youverify.agent_app_android.databinding.BottomFilterLayoutBinding
import com.youverify.agent_app_android.databinding.FragmentTaskBinding
import com.youverify.agent_app_android.databinding.TaskAssignedDialogBinding
import com.youverify.agent_app_android.features.dashboard.DashboardViewModel
import com.youverify.agent_app_android.features.task.TaskBundle
import com.youverify.agent_app_android.features.task.TaskViewModel
import com.youverify.agent_app_android.util.AgentSharePreference
import com.youverify.agent_app_android.util.AgentStatus
import com.youverify.agent_app_android.util.ProgressLoader
import com.youverify.agent_app_android.util.SharedPrefKeys
import com.youverify.agent_app_android.util.extension.*
import com.youverify.agent_app_android.util.helper.isLollipopPlus
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class TaskListFragment : Fragment(R.layout.fragment_task) {
    //code to get the view model object
    private lateinit var taskViewModel: TaskViewModel

    //adapter object
    private val adapter by lazy {
        TaskItemAdapter { selectedItem: TasksDomain.AgentTask ->
            listItemClicked(selectedItem)
        }
    }
    private val binding by viewBindings(FragmentTaskBinding::bind)

    @Inject
    lateinit var preference: AgentSharePreference

    @Inject
    lateinit var progressLoader: ProgressLoader

    private val viewModel by viewModels<TaskViewModel>()

    private val dashboardViewModel by activityViewModels<DashboardViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setObservers()
    }

    private fun setupUI() {
        binding.notificationIcon.setOnClickListener {
            findNavController().navigate(R.id.action_taskFragment_to_notificationsFragment)
        }

        binding.taskRecyclerView.adapter = adapter

        binding.filterBtn.setOnClickListener { viewModel.getFilterParams() }

        binding.dutySwitch.setOnCheckedChangeListener { compoundButton, b ->
            val currentStatus = binding.dutySwitch.tag as? String
            Timber.d("Tag ==> $currentStatus")
            currentStatus?.let {
                val status =
                    if (currentStatus == AgentStatus.ONINE) AgentStatus.OFFLINE else AgentStatus.ONINE
                dashboardViewModel.updateAgentStatus(status)
            }

        }
    }


    private fun setObservers() {
        viewModel.tasksState.observe(viewLifecycleOwner) {
            val state = it.getContentIfNotHandled() ?: return@observe
            when (state) {
                is ResultState.Loading -> {
                    progressLoader.show(message = "Please wait...", false)
                }
                is ResultState.Error -> {
                    progressLoader.hide()
                    context?.showDialog(message = state.error)
                }
                is ResultState.Success -> {
                    progressLoader.hide()

                    viewModel.offlineTasks.observe(viewLifecycleOwner) { offlineTasks ->
                        val allTasks = (offlineTasks ?: emptyList()) + state.data
                        adapter.setItemsList(allTasks)

                        if (allTasks.isEmpty()) {
                            binding.noTasksMessage.visibleIf(allTasks.isEmpty())
                            binding.gifImageView.visibleIf(allTasks.isEmpty())
                            binding.noTasksMessage.text = "Looks like there is no task for \n" +
                                    "you now.\n" +
                                    "\n" +
                                    "Enjoy the break ${AgentSharePreference(requireContext()).getString(SharedPrefKeys.FIRST_NAME)}"
                        }
                    }

                }
            }
        }

        viewModel.filterParamsState.observe(viewLifecycleOwner) {
            val state = it.getContentIfNotHandled() ?: return@observe
            when (state) {
                is ResultState.Loading -> progressLoader.show("Fetching filter params...")
                is ResultState.Error -> {
                    progressLoader.hide()
                    context?.toast(state.error)
                }
                is ResultState.Success -> {
                    progressLoader.hide()
                    showBottomBar(
                        state.data.first,
                        state.data.second.data ?: emptyList()
                    ) { selectedState, status ->
                        viewModel.fetchAgentTasks(selectedState, status)
                    }
                }
            }
        }

        dashboardViewModel.agentVisibiltyStatus.observe(viewLifecycleOwner) {
            val state = it ?: return@observe
            Timber.d("Status ==> $state")
            updateStatus(state)
        }
    }

    private fun updateStatus(state: String) {
        val check = state == AgentStatus.ONINE
        binding.dutySwitch.isChecked = check
        binding.dutySwitch.tag = state

        val showText: String
        if (check) {
            showText = "Online"
            binding.noTasksMessage.visibility = View.GONE
            binding.gifImageView.visibility = View.GONE
        } else {
            showText = "Offline"
            binding.noTasksMessage.visibility = View.VISIBLE
            binding.gifImageView.visibility = View.VISIBLE
        }
        binding.dutySwitch.text = showText

        binding.toolBar.setColor(check, R.color.colorPrimaryDark, R.color.off_duty)

        if (isLollipopPlus()) {
            val window = activity?.window
            window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            val color = if (check) R.color.colorPrimaryDark else R.color.off_duty
            window?.statusBarColor = ContextCompat.getColor(requireContext(), color)
        }

        val curvedBackground =
            if (check) R.drawable.curved_appbar else R.drawable.curved_appbar_inactive
        val notifyIcon =
            if (check) R.drawable.ic_bell_icon else R.drawable.ic_notify_off_duty
        val filterBkg =
            if (check) R.drawable.curved_button_outlined else R.drawable.curved_button_inactive
        val dayBkg =
            if (check) R.drawable.curved_button else R.drawable.curved_btn_inactive
        binding.toolbarDropped.setBackgroundResource(curvedBackground)
        binding.notificationIcon.setImageDrawable(ContextCompat.getDrawable(requireContext(), notifyIcon))
        binding.filterBtn.setBackgroundResource(filterBkg)
        binding.daySelector.setBackgroundResource(dayBkg)
        binding.taskRecyclerView.visibleIf(check)
    }


    private fun listItemClicked(taskItem: TasksDomain.AgentTask) {
        displayTaskDialog(taskItem)
    }

    private fun displayTaskDialog(taskItem: TasksDomain.AgentTask) {
        //we should use the taskItem passed in here to set the data on the dialog

        val dialogBuilder =
            AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog).create()
        val binding = TaskAssignedDialogBinding.inflate(layoutInflater)
        binding.addressText.text = taskItem.address
        binding.nameText.text = taskItem.candidate?.name

        val slideRightButton = binding.slideRightBtn
        val slideLeftButton = binding.slideLeftBtn
        val closeButton = binding.closeBtn

        dialogBuilder.setView(binding.root)

        slideRightButton.onSlideCompleteListener = object : SlideToActView.OnSlideCompleteListener {
            override fun onSlideComplete(view: SlideToActView) {
                dialogBuilder.dismiss()
                val taskBundle = TaskBundle(taskItem = taskItem).toJson()
                val taskIntent = Intent(requireContext(), TaskActivity::class.java).apply {
                    putExtra(TaskActivity.START_DESTINATION_KEY, R.id.taskDetailsFragment2)
                    putExtra(TaskActivity.BUNDLE_KEY, taskBundle)
                }
                startActivity(taskIntent)
            }
        }

        slideLeftButton.onSlideCompleteListener = object : SlideToActView.OnSlideCompleteListener {
            override fun onSlideComplete(view: SlideToActView) {
                dialogBuilder.dismiss()
            }
        }
        closeButton.setOnClickListener {
            dialogBuilder.dismiss()
        }

        dialogBuilder.setCancelable(false)
        dialogBuilder.show()
        dialogBuilder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun showBottomBar(
        state: List<State>,
        statuses: List<String>,
        callback: (state: String?, status: String?) -> Unit
    ) {
        var dialog: MaterialDialog? = null
        val binding = BottomFilterLayoutBinding.inflate(layoutInflater)

        binding.selectStateInput.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                state
            )
        )

        binding.selectStatusInput.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                statuses
            )
        )

        binding.buttonApply.setOnClickListener {
            val stateSeleted = binding.selectStateInput.text.toString()
            val status = binding.selectStatusInput.text.toString()
            dialog?.dismiss()
            callback(stateSeleted, status)
        }



        dialog = context?.inflateBottomSheet(binding.root, cancelable = true)


    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchAgentTasks()
    }
}