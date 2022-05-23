package com.youverify.agent_app_android.util

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.navigation.NavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.youverify.agent_app_android.R


fun BottomNavigationView.showNavBar() {
    this.visibility = View.VISIBLE
 }

fun BottomNavigationView.removeNavBar() {
    this.visibility = View.GONE
}


fun Activity.isInternetAvailable(): Boolean {
    try {
        with(getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //Device is running on Marshmallow or later Android OS.
                with(getNetworkCapabilities(activeNetwork)) {
                    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
                    return this!!.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || hasTransport(
                        NetworkCapabilities.TRANSPORT_CELLULAR
                    )
                }
            } else {
                @Suppress("DEPRECATION")
                activeNetworkInfo?.let {
                    // connected to the internet
                    @Suppress("DEPRECATION")
                    return listOf(
                        ConnectivityManager.TYPE_WIFI,
                        ConnectivityManager.TYPE_MOBILE
                    ).contains(it.type)
                }
            }
        }
        return false
    } catch (exc: NullPointerException) {
        return false
    }
}

fun Activity.createDialog(okButton: Button, navController: NavController, layout: Int, style: Int,
                          successText: TextView, message: String, navView: BottomNavigationView){
    val dialogBuilder = AlertDialog.Builder(this, style).create()
    val view = layoutInflater.inflate(layout, null)
    dialogBuilder.setView(view)

    successText.text = message
    okButton.setOnClickListener{
        dialogBuilder.dismiss()
        navController.navigate(R.id.action_accessBuildingFragment_to_taskFragment)
        navView.showNavBar()
    }
    dialogBuilder.setCancelable(false)
    dialogBuilder.show()
    dialogBuilder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
}