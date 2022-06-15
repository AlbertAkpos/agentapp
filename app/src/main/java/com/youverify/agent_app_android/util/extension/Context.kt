package com.youverify.agent_app_android.util.extension

import android.content.Context
import android.content.res.Resources
import android.view.View
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.bottomsheets.setPeekHeight
import com.afollestad.materialdialogs.customview.customView

fun Context.showDialog(
    title: String = "Error",
    message: String,
    positiveTitle: String = "ok",
    negativeTitle: String? = "",
    positiveCallback: (() -> Unit)? = null
) {

    MaterialDialog(this).show {
        cornerRadius(5F)
        title(text = title)
        message(text = message)

        positiveButton(text = positiveTitle) { dialog ->
            positiveButton()
        }

        negativeButton(text = negativeTitle) {
            dismiss()
        }
    }
}

fun Context.inflateBottomSheet(views: View, cancelable: Boolean = false, cornerRadius: Float = 16F): MaterialDialog {
    return MaterialDialog(this, BottomSheet(LayoutMode.WRAP_CONTENT)).show {
        customView(view = views)
        cornerRadius(cornerRadius)
        cancelable(cancelable)
        setPeekHeight(Resources.getSystem().displayMetrics.heightPixels)
    }
}