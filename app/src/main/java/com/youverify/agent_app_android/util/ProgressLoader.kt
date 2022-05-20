package com.youverify.agent_app_android.util

interface ProgressLoader {
    fun show(message: String? = null, cancellable: Boolean = false)
    fun hide()
}