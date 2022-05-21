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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.databinding.FragmentNotificationsBinding
import com.youverify.agent_app_android.data.model.NotificationItem
import com.youverify.agent_app_android.features.HomeActivity


class NotificationsFragment : Fragment(R.layout.fragment_notifications) {

    private lateinit var binding: FragmentNotificationsBinding
    private lateinit var notificationItemsAdapter: NotificationsAdapter
    private lateinit var homeActivity : HomeActivity

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

        setNotificationItems()
        return binding.root
    }

    private fun setNotificationItems() {
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

        //create the adapter and the swipeGesture
        // we want to use the swipeGesture abstract class we created to
        // determine what will happen when we swipe left or right.
        notificationItemsAdapter = NotificationsAdapter()
        notificationItemsAdapter.setData(notificationItems)

        val swipeGesture = object : SwipeGesture(requireContext()){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                when(direction){
                    ItemTouchHelper.LEFT -> {
                        notificationItemsAdapter.uploadItem(viewHolder.absoluteAdapterPosition)
                        showuploadSuccessDialog()
                    }
                    ItemTouchHelper.RIGHT -> {
                        notificationItemsAdapter.uploadItem(viewHolder.absoluteAdapterPosition)
                        showuploadSuccessDialog()
                    }
                }
            }
        }

        val touchHelper = ItemTouchHelper(swipeGesture)
        touchHelper.attachToRecyclerView(binding.recyclerView)
        binding.recyclerView.adapter = notificationItemsAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        homeActivity.removeNavBar()
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