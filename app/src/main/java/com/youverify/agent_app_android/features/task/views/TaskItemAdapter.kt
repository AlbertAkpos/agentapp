package com.youverify.agent_app_android.features.task.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.databinding.TaskItemBinding
import com.youverify.agent_app_android.data.model.TaskItem
import com.youverify.agent_app_android.data.model.tasks.TasksDomain

class TaskItemAdapter(
    private val clickListener: (TasksDomain.AgentTask) -> Unit)
    : RecyclerView.Adapter<TaskItemViewHolder>(){

    private val taskItemList = ArrayList<TasksDomain.AgentTask>()

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

    fun setItemsList(taskItems: List<TasksDomain.AgentTask>){
        taskItemList.clear()
        taskItemList.addAll(taskItems)
        notifyDataSetChanged()
    }
}

class TaskItemViewHolder(val binding : TaskItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(taskItem: TasksDomain.AgentTask, clickListener: (TasksDomain.AgentTask) -> Unit){
        binding.verificationTypeText.text = taskItem.verificationType
        binding.addressText.text = taskItem.address
        binding.timeText.text = taskItem.time

        binding.cardView.setOnClickListener {
            clickListener(taskItem)
        }
    }
}
