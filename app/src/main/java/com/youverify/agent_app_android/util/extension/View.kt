package com.youverify.agent_app_android.util.extension

import android.view.View
import android.widget.ScrollView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun View.visibleIf(show: Boolean?) {
    visibility = if (show == true) View.VISIBLE
    else View.GONE
}

fun View.setColor(check: Boolean?, @ColorRes checkedColor: Int, @ColorRes uncheckedColor: Int) {
    if (check == true) {
        setBackgroundColor(ContextCompat.getColor(context, checkedColor))
    } else setBackgroundColor(ContextCompat.getColor(context, uncheckedColor))
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.scrollTo(scrollView: ScrollView) {
    scrollView.scrollTo(0, this.y.toInt())
}