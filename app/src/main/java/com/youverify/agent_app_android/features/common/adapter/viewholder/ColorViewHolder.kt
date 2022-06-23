package com.youverify.agent_app_android.features.common.adapter.viewholder

import android.view.View
import com.youverify.agent_app_android.data.model.tasks.TasksDomain
import com.youverify.agent_app_android.databinding.ImageColorItemBinding
import me.ibrahimyilmaz.kiel.core.RecyclerViewHolder

class ColorViewHolder(view: View): RecyclerViewHolder<TasksDomain.Color>(view) {
    val binding = ImageColorItemBinding.bind(view)
}