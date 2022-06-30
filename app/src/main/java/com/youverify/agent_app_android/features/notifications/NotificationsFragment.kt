package com.youverify.agent_app_android.features.notifications

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.databinding.FragmentNotificationsBinding
import com.youverify.agent_app_android.data.model.NotificationItem
import com.youverify.agent_app_android.features.HomeActivity
import com.youverify.agent_app_android.features.task.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationsFragment : Fragment(R.layout.fragment_notifications) {

    private lateinit var binding: FragmentNotificationsBinding
    private  val notificationItemsAdapter: NotificationsAdapter by lazy { NotificationsAdapter() }
    private lateinit var homeActivity : HomeActivity

    private val viewModel by viewModels<TaskViewModel>()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationsBinding.inflate(layoutInflater)
        homeActivity = requireActivity() as HomeActivity

        binding.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setObservers()
    }

    private fun setupUI() {
        setupList()

    }

    private fun setObservers() {
        viewModel.notifications.observe(viewLifecycleOwner) {
            val tasks = it ?: return@observe
            // Set adapter
            notificationItemsAdapter.setData(tasks)
        }
    }

    private fun setupList() {
        //dummy data
        val notificationItems = arrayListOf(
                NotificationItem(
                    image = R.drawable.ic_access_granted,
                    accessText = "Address access granted",
                    nameText = "Janet Foly",
                    addressText = "78, Tony Cresent, Ajah, Off Lekki toll gate, Lagos",
                    timeText = "10 min. ago"
                ),
                NotificationItem(
                    image = R.drawable.ic_offline_task,
                    accessText = "Offline task",
                    nameText = "Janet Foly",
                    addressText = "78, Tony Cresent, Ajah, Off Lekki toll gate, Lagos",
                    timeText = "1 hr. ago"
                ),
                NotificationItem(
                    image = R.drawable.ic_access_granted,
                    accessText = "Access time expired",
                    nameText = "Thomas Peters",
                    addressText = "78, Tony Cresent, Ajah, Off Lekki toll gate, Lagos",
                    timeText = "1 hr. ago"
                ),
                NotificationItem(
                    image = R.drawable.ic_access_granted,
                    accessText = "Task rejected",
                    nameText = "Ada Nnenna",
                    addressText = "1901 Thornridge Cir. Shiloh, Hawaii 81063",
                    timeText = "2 hrs ago"
                )
            )

        val swipeGesture = object : SwipeGesture(requireContext()){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                when(direction){
                    ItemTouchHelper.LEFT, ItemTouchHelper.RIGHT -> {
                        notificationItemsAdapter.uploadItem(viewHolder.absoluteAdapterPosition)
                        val notificationItem = (viewHolder as? NotificationsItemViewHolder)?.notificationItem
                        notificationItem?.let { onItemSwiped(it) }
                    }
                }
            }

            override fun getSwipeDirs(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                if (viewHolder is NotificationsItemViewHolder) {
                    // Disables swipe on other views except for offline tasks
                   if (viewHolder.binding.offlineView.visibility != View.VISIBLE) return 0
                }
                return super.getSwipeDirs(recyclerView, viewHolder)
            }
        }

        val touchHelper = ItemTouchHelper(swipeGesture)
        touchHelper.attachToRecyclerView(binding.recyclerView)
        binding.recyclerView.adapter = notificationItemsAdapter
    }

    private fun onItemSwiped(notificationItem: NotificationItem) {
        val task = notificationItem.submitTask
        if (task != null) {
            viewModel.updateAndSubmitTask(task)
        }
    }


    override fun onStop() {
        super.onStop()
        homeActivity.showNavBar()
    }

    private fun showuploadSuccessDialog(){
//        val dialogBuilder =
//            AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog).create()
//        val view = layoutInflater.inflate(R.layout.upload_success_dialog, null)
//        dialogBuilder.setView(view)
//        dialogBuilder.setCancelable(true)
//        dialogBuilder.show()
//        dialogBuilder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//
//        Handler(Looper.getMainLooper()).postDelayed({
//            dialogBuilder.hide()
//        }, 3000)

        val toast = Toast(context)
        Handler(Looper.getMainLooper()).postDelayed({
            showToast(toast)
        }, 2000)
    }

    private fun showToast(toast: Toast) {
        val customView: View = LayoutInflater.from(requireContext())
            .inflate(R.layout.custom_toast, view?.findViewById<ConstraintLayout>(R.id.toast_wrapper))

        toast.apply {
            duration = Toast.LENGTH_SHORT
            view = customView
            setGravity(Gravity.TOP, 0, 0)
        }.show()
    }
}