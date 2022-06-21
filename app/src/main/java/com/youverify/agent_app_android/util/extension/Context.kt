package com.youverify.agent_app_android.util.extension

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.text.format.DateUtils
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.bottomsheets.setPeekHeight
import com.afollestad.materialdialogs.customview.customView
import java.util.*

fun Context.showDialog(
    title: String = "Error",
    message: String,
    positiveTitle: String = "ok",
    negativeTitle: String? = "",
    negativeCallback: (() -> Unit)? = null,
    positiveCallback: (() -> Unit)? = null
) {

    MaterialDialog(this).show {
        cornerRadius(5F)
        title(text = title)
        message(text = message)

        positiveButton(text = positiveTitle) { dialog ->
           positiveCallback?.invoke()
        }

        negativeButton(text = negativeTitle) {
            negativeCallback?.invoke()
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

fun Context.inflateDialog(views: View, cancelable: Boolean = false): MaterialDialog {
    return MaterialDialog(this).show {
        customView(view = views)
        cornerRadius(16F)
        cancelOnTouchOutside(cancelable)
        cancelable(cancelable)
    }
}

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.isPermissionsGranted(vararg permissions: String): Boolean {
    for (permission in permissions) {
        if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            return false
        }
    }
    return true
}

