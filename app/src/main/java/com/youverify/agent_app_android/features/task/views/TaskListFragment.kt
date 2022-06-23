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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ncorti.slidetoact.SlideToActView
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.core.functional.ResultState
import com.youverify.agent_app_android.databinding.FragmentTaskBinding
import com.youverify.agent_app_android.data.model.TaskItem
import com.youverify.agent_app_android.data.model.tasks.TasksDomain
import com.youverify.agent_app_android.features.HomeActivity
import com.youverify.agent_app_android.features.task.TaskBundle
import com.youverify.agent_app_android.features.task.TaskViewModel
import com.youverify.agent_app_android.util.AgentSharePreference
import com.youverify.agent_app_android.util.AgentTaskStatus
import com.youverify.agent_app_android.util.ProgressLoader
import com.youverify.agent_app_android.util.extension.showDialog
import com.youverify.agent_app_android.util.extension.toJson
import com.youverify.agent_app_android.util.extension.viewBindings
import com.youverify.agent_app_android.util.extension.visibleIf
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
        viewModel.fetchAgentTasks(AgentTaskStatus.PENDING)
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

        binding.filterBtn.setOnClickListener {
            showBottomBar()
        }
    }

    private fun setObservers() {
        viewModel.tasksState.observe(viewLifecycleOwner) {
            val state = it ?: return@observe
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
    }


    private fun listItemClicked(taskItem: TasksDomain.AgentTask) {
        displayTaskDialog(taskItem)
    }

    private fun displayTaskDialog(taskItem: TasksDomain.AgentTask) {
        //we should use the taskItem passed in here to set the data on the dialog

        val dialogBuilder =
            AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog).create()
        val view = layoutInflater.inflate(R.layout.task_assigned_dialog, null)
        view.findViewById<TextView>(R.id.address_text).text = taskItem.address

        val slideRightButton = view.findViewById<com.ncorti.slidetoact.SlideToActView>(R.id.slide_right_btn)
        val slideLeftButton = view.findViewById<SlideToActView>(R.id.slide_left_btn)
        val closeButton = view.findViewById<ImageView>(R.id.close_btn)

        dialogBuilder.setView(view)

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

    private fun showBottomBar() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_filter_layout)

        val applyButton = dialog.findViewById<Button>(R.id.button_apply)
        val drawerHandle = dialog.findViewById<View>(R.id.drawer_handle)

        applyButton.setOnClickListener {
            dialog.dismiss()
            Toast.makeText(requireContext(), "Dismissed dialog", Toast.LENGTH_SHORT).show()
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
}