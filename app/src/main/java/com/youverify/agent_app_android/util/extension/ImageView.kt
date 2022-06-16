package com.youverify.agent_app_android.util.extension

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.youverify.agent_app_android.R

fun ImageView.loadImage(url: String?) {
    Glide.with(context)
        .load(url.toString())
        .error(R.drawable.background_profile)
        .into(this)
}