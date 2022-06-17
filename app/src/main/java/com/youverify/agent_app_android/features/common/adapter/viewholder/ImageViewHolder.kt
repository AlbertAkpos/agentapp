package com.youverify.agent_app_android.features.common.adapter.viewholder

import android.view.View
import com.youverify.agent_app_android.databinding.ImageItemBinding
import me.ibrahimyilmaz.kiel.core.RecyclerViewHolder
import java.io.File

class ImageViewHolder(view: View): RecyclerViewHolder<File>(view) {
    val binding = ImageItemBinding.bind(view)
}