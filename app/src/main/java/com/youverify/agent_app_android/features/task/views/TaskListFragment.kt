package com.youverify.agent_app_android.features.task.views

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.ncorti.slidetoact.SlideToActView
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.core.functional.ResultState
import com.youverify.agent_app_android.data.model.signup.State
import com.youverify.agent_app_android.databinding.FragmentTaskBinding
import com.youverify.agent_app_android.data.model.tasks.TasksDomain
import com.youverify.agent_app_android.databinding.BottomFilterLayoutBinding
import com.youverify.agent_app_android.databinding.TaskAssignedDialogBinding
import com.youverify.agent_app_android.features.task.TaskBundle
import com.youverify.agent_app_android.features.task.TaskViewModel
import com.youverify.agent_app_android.util.AgentSharePreference
import com.youverify.agent_app_android.util.AgentTaskStatus
import com.youverify.agent_app_android.util.ProgressLoader
import com.youverify.agent_app_android.util.extension.*
import dagger.hilt.android.AndroidEntryPoint
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.fetchAgentTasks()
    }

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
                    binding.noTasksMessage.visibleIf(state.data.isNullOrEmpty())
                    adapter.setItemsList(state.data)
                }
            }
        }

        viewModel.filterParamsState.observe(viewLifecycleOwner) {
            val state = it.getContentIfNotHandled() ?: return@observe
            when(state) {
                is ResultState.Loading -> progressLoader.show("Fetching filter params...")
                is ResultState.Error -> {
                    progressLoader.hide()
                    context?.toast(state.error)
                }
                is ResultState.Success -> {
                    progressLoader.hide()
                    showBottomBar(state.data.first, state.data.second.data ?: emptyList()) { selectedState, status ->
                        viewModel.fetchAgentTasks(selectedState, status)
                    }
                }
            }
        }
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

    private fun showBottomBar(state: List<State>, statuses: List<String>, callback: (state: String?, status: String?) -> Unit) {
        var dialog: MaterialDialog? = null
        val binding = BottomFilterLayoutBinding.inflate(layoutInflater)

        binding.selectStateInput.setAdapter(ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, state))

        binding.selectStatusInput.setAdapter(ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, statuses))

        binding.buttonApply.setOnClickListener {
            val stateSeleted = binding.selectStateInput.text.toString()
            val status = binding.selectStatusInput.text.toString()
            dialog?.dismiss()
            callback(stateSeleted, status)
        }



        dialog = context?.inflateBottomSheet(binding.root, cancelable = true)


    }
}