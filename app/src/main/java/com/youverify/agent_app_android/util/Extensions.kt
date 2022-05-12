package com.youverify.agent_app_android.util

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.youverify.agent_app_android.R
import org.w3c.dom.Text


fun BottomNavigationView.showNavBar() {
    this.visibility = View.VISIBLE
 }

fun Activity.createDialog(okButton: Button, navController: NavController,
                          successText: TextView, message: String, navView: BottomNavigationView){
    val dialogBuilder = AlertDialog.Builder(this, R.style.CustomAlertDialog).create()
    val view = layoutInflater.inflate(R.layout.reset_password_dialog, null)
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