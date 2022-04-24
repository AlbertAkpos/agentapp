package com.youverify.agent_app_android.view.fragment.home.notifications

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.databinding.NotificationItemsBinding
import com.youverify.agent_app_android.model.NotificationsItem


private const val ACCESS_GRANTED = "Address access granted"
private const val TASK_REJECTED = "Task rejected"
private const val OFFLINE_TASK = "Offline task"
private const val ACCESS_EXPIRED = "Access time expired"

class NotificationsAdapter (private var notificationItems : ArrayList<NotificationsItem>) : RecyclerView.Adapter<NotificationsAdapter.NotificationsItemViewHolder>(){


    //method to upload an item to the server when the recyclerview item is swiped
    fun uploadItem(position: Int){
        notificationItems.removeAt(position)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationsItemViewHolder {
        return NotificationsItemViewHolder(NotificationItemsBinding
            .inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: NotificationsItemViewHolder, position: Int) {
        holder.apply {
            bind(notificationItems[position])
        }

        //setting onclickListener for each item in recyclerview and pass in the currentItem clicked on
//        val currentItem = notificationItems[position]

//        holder.binding.cardView.setOnClickListener{
//          val action = NotificationsFragmentDirections.actionNotificationsFragmentToTaskDetailsFragment()
//          holder.itemView.findNavController().navigate(action)
//        }
    }

    override fun getItemCount() = notificationItems.size


//    fun setData(notifications: List<NotificationsItem>){
//        this.notificationItems = notifications
//        notifyDataSetChanged()
//    }

    inner class NotificationsItemViewHolder(val binding: NotificationItemsBinding) : RecyclerView.ViewHolder(binding.root){
        private val image = binding.imageView
        private val accessText = binding.accessText
        private val nameText = binding.nameText
        private val addressText = binding.addressText
        private val timeText = binding.timeText
        private val offlineView = binding.offlineView

        fun bind(notificationItem: NotificationsItem){

            when(notificationItem.accessText){
                ACCESS_GRANTED -> {
                    image.setImageResource(R.drawable.ic_access_granted)
                    accessText.setTextColor(Color.parseColor("#4C963F"))
                }
                ACCESS_EXPIRED -> {
                    image.setImageResource(R.drawable.ic_clock_icon)
                    accessText.setTextColor(Color.parseColor("#EC7A07"))
                }
                TASK_REJECTED -> {
                    image.setImageResource(R.drawable.ic_warning_icon)
                    accessText.setTextColor(Color.parseColor("#E5382B"))
                }
                OFFLINE_TASK -> {
                    image.setImageResource(R.drawable.ic_offline_task)
                    accessText.setTextColor(Color.parseColor("#6E7E82"))
                    offlineView.visibility = View.VISIBLE
                }
            }

            accessText.text = notificationItem.accessText
            nameText.text = notificationItem.nameText
            addressText.text = notificationItem.addressText
            timeText.text = notificationItem.timeText
        }
    }
}