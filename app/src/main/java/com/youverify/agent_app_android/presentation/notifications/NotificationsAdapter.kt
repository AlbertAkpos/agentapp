package com.youverify.agent_app_android.presentation.notifications

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.databinding.NotificationItemBinding
import com.youverify.agent_app_android.data.model.NotificationItem


private const val ACCESS_GRANTED = "Address access granted"
private const val TASK_REJECTED = "Task rejected"
private const val OFFLINE_TASK = "Offline task"
private const val ACCESS_EXPIRED = "Access time expired"

class NotificationsAdapter ()
    : RecyclerView.Adapter<NotificationsItemViewHolder>(){

    private var notificationItems = ArrayList<NotificationItem>()

    //method to upload an item to the server when the recyclerview item is swiped
    fun uploadItem(position: Int){
        notificationItems.removeAt(position)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationsItemViewHolder {
        return NotificationsItemViewHolder(NotificationItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: NotificationsItemViewHolder, position: Int) {
        holder.apply {
            bind(notificationItems[position])
        }
    }

    override fun getItemCount() = notificationItems.size


    fun setData(notifications: ArrayList<NotificationItem>){
       notificationItems = notifications
    }
}

class NotificationsItemViewHolder(val binding: NotificationItemBinding) : RecyclerView.ViewHolder(binding.root){
    private val image = binding.imageView
    private val accessText = binding.accessText
    private val nameText = binding.nameText
    private val addressText = binding.addressText
    private val timeText = binding.timeText
    private val offlineView = binding.offlineView

    fun bind(notificationItem: NotificationItem){

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