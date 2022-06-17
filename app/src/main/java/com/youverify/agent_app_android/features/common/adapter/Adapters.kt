package com.youverify.agent_app_android.features.common.adapter

import androidx.core.content.ContextCompat
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.data.model.tasks.TasksDomain
import com.youverify.agent_app_android.features.common.adapter.viewholder.ColorViewHolder
import com.youverify.agent_app_android.features.common.adapter.viewholder.ImageViewHolder
import com.youverify.agent_app_android.util.extension.loadImage
import me.ibrahimyilmaz.kiel.adapterOf
import java.io.File


fun createColorsAdapter(onColorClick: (color: TasksDomain.Color) -> Unit) = adapterOf<TasksDomain.Color> {
    diff(
        areItemsTheSame = { old: TasksDomain.Color, new: TasksDomain.Color -> old.colorId == new.colorId },
        areContentsTheSame = { old: TasksDomain.Color, new: TasksDomain.Color -> old == new }
    )

    register(
        layoutResource = R.layout.image_color_item,
        viewHolder = ::ColorViewHolder,
        onBindViewHolder = { colorViewHolder: ColorViewHolder, position: Int, color: TasksDomain.Color ->
            val binding = colorViewHolder.binding
            val context = binding.root.context
            if (color.colorId != null) {
                binding.colorItem.backgroundTintList = ContextCompat.getColorStateList(context, color.colorId)
            } else {
                binding.colorItem.setImageResource(R.drawable.ic_none)
            }

            binding.colorItem.setOnClickListener { onColorClick(color) }
        }
    )
}

fun createImagesAdapter() = adapterOf<File> {
    diff(areItemsTheSame = { old: File, new: File -> old.absolutePath == new.absolutePath },
        areContentsTheSame = { old: File, new: File -> old == new }
    )

    register(
        layoutResource = R.layout.image_item,
        viewHolder = ::ImageViewHolder,
        onBindViewHolder = { imageViewHolder: ImageViewHolder, _: Int, file: File ->
            val binding = imageViewHolder.binding
            binding.image.loadImage(file.absolutePath)
        }
    )
}
