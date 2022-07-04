package com.youverify.agent_app_android.util.extension

import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.autoClearError() {
    this.editText?.addTextChangedListener {
            this.error = null
            this.isErrorEnabled = false
    }
}