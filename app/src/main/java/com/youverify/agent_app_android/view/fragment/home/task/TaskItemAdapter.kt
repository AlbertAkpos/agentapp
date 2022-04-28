package com.youverify.agent_app_android.view.fragment.home.task

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.databinding.TaskItemBinding
import com.youverify.agent_app_android.model.TaskItem

class TaskItemAdapter(
    private val clickListener: (TaskItem) -> Unit)
    : RecyclerView.Adapter<TaskItemViewHolder>(){

    private val taskItemList = ArrayList<TaskItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding : TaskItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.task_item, parent, false)
        return TaskItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskItemViewHolder, position: Int) {
        holder.bind(taskItemList[position], clickListener)
    }

    override fun getItemCount(): Int {
        return taskItemList.size
    }

    fun setItemsList(taskItems: List<TaskItem>){
        taskItemList.clear()
        taskItemList.addAll(taskItems)
    }
}

class TaskItemViewHolder(val binding : TaskItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(taskItem: TaskItem, clickListener: (TaskItem) -> Unit){
        binding.verificationTypeText.text = taskItem.verificationType
        binding.addressText.text = taskItem.address
        binding.timeText.text = taskItem.time

        binding.cardView.setOnClickListener {
            clickListener(taskItem)
        }
    }
}
