package com.youverify.agent_app_android.util

import android.Manifest

object Constants {
    const val TAG = "cameraX"
    const val FILE_NAME_FORMAT = "yy-MM-dd-HH-mm-ss-SSS"
    const val REQUEST_CODE_PERMISSIONS = 123
    const val REQUEST_CODE_STORAGE_PERMISSION = 1
    const val REQUEST_CODE_SELECT_IMAGE = 2
    val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    const val REQUIRED_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE
    val buildTypes = getBuildingTyepes()
}


object AgentTaskStatus {
    const val PENDING = "PENDING"
    const val REJECTED = "REJECTED"
}

object AgentTaskVerificationType {
    const val GUARANTOR = "GUARANTOR"
    const val INDIVIDUAL = "INDIVIDUAL"

}

private fun getBuildingTyepes() =
    arrayListOf("Townhouse", "Terraced house", "Semi detached house", "Detached house", "Bungalow", "Duplex", "Mansion", "Penthouse", "Container house", "One room (face-me-I-face-you)")