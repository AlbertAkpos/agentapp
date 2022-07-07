package com.youverify.agent_app_android.util

import android.Manifest
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.data.model.tasks.TasksDomain
import com.youverify.agent_app_android.util.Constants.NO_ONE

object Constants {
    const val TAG = "cameraX"
    const val FILE_NAME_FORMAT = "yy-MM-dd-HH-mm-ss-SSS"
    const val REQUEST_CODE_PERMISSIONS = 123
    const val REQUEST_CODE_STORAGE_PERMISSION = 1
    const val REQUEST_CODE_SELECT_IMAGE = 2
    val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    const val REQUIRED_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE
    val buildTypes = getBuildingTyepes()
    val colors = getColorList()
    val whoConfirmedCandidateAddressList = positiveWhoConfirmedCandidateLivesHere()
    val whoConfirmedCandidateAddressNegative = negativeWhoConfirmedCandidateLiveHere()
    val cantLocateAddressReasons = cantLocateAddressReasons()
    const val NO_ONE = "No one"
}

object AgentStatus {
    const val ACTIVE = "ACTIVATED"
    const val ONINE = "ONLINE"
    const val OFFLINE = "OFFLINE"
}

object AgentTaskStatus {
    const val PENDING = "PENDING"
    const val REJECTED = "REJECTED"
}

object AgentTaskVerificationType {
    const val GUARANTOR = "GUARANTOR"
    const val INDIVIDUAL = "INDIVIDUAL"

}

object Permissions {
    const val ACCESS_COARSE_LOCATION = "android.permission.ACCESS_COARSE_LOCATION"
    const val ACCESS_BACKGROUND_LOCATION = "android.permission.ACCESS_BACKGROUND_LOCATION"
    const val ACCESS_FINE_LOCATION = "android.permission.ACCESS_FINE_LOCATION"
    const val CAMERA = "android.permission.CAMERA"
    const val READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE"
    const val WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE"
}

object SharedPrefKeys {
    const val TOKEN = "TOKEN"
    const val EMAIL = "EMAIL"
    const val PHONE = "PHONE"
    const val IMG_URL = "IMG_URL"
    const val AGENT_ID = "AGENT_ID"
    const val LAST_NAME = "LAST_NAME"
    const val IS_TRAINED = "IS_TRAINED"
    const val PREF_AREAS = "PREF_AREAS"
    const val FIRST_NAME = "FIRST_NAME"
    const val IS_VERIFIED = "IS_VERIFIED"
    const val AGENT_STATUS = "AGENT_STATUS"
    const val AGENT_VISIBLITY_STATE = "AGENT_VISIBLITY_STATE"
    const val REFRESH_TOKEN = "REFRESH_TOKEN"
    const val STATE_OF_RESIDENCE = "STATE_OF_RESIDENCE"
    const val ONBOARDING_FINISHED = "ONBOARDING_FINISHED"
}

object TaskKeys {
    const val TAKS_ID = "taskId"
    const val LAT = "lat"
    const val LONG = "lon"
    const val FLAT_NUMBER = "flatNumber"
    const val BUILDING_NAME = "buildingName"
    const val SUB_STREET = "subStreet"
    const val LGA = "lga"
    const val COUNTRY = "country"
    const val BUILDING_NUMBER  = "buildingNumber"
    const val LANDMARK = "landmark"
    const val STREET = "street"
    const val CITY = "city"
    const val STATE = "state"
    const val BUSINESS_NAME = "businessName"
    const val BUSINESS_REG_NUM = "businessRegNumber"
    const val STATUS = "status"
    const val VERIFICATION_TYPE = "verificationType"
    const val LAST_MODIFIED_AT = "lastModifiedAt"
}

private fun getBuildingTyepes() =
    arrayListOf(
        "Townhouse",
        "Terraced house",
        "Semi detached house",
        "Detached house",
        "Bungalow",
        "Duplex",
        "Mansion",
        "Penthouse",
        "Container house",
        "One room (face-me-I-face-you)"
    )

private fun getColorList(): List<TasksDomain.Color> {
    return listOf(
        TasksDomain.Color(colorId = null, name = "No color"),
        TasksDomain.Color(colorId = R.color.white, name = "White"),
        TasksDomain.Color(colorId = R.color.black, name = "Black"),
        TasksDomain.Color(colorId = R.color.yellow, name = "Yellow"),
        TasksDomain.Color(colorId = R.color.teal_green, name = "Teal green"),
        TasksDomain.Color(colorId = R.color.lemon_green, name = "Lemon green"),
        TasksDomain.Color(colorId = R.color.shady_green, name = "Shady green"),
        TasksDomain.Color(colorId = R.color.purple_blue, name = "Purple blue"),
        TasksDomain.Color(colorId = R.color.red_wine, name = "Red wine"),
        TasksDomain.Color(colorId = R.color.dark_red, name = "Dark red"),
        TasksDomain.Color(colorId = R.color.red, name = "Red"),
        TasksDomain.Color(colorId = R.color.faded_red, name = "Faded red"),
        TasksDomain.Color(colorId = R.color.dark_purple, name = "Dark purple"),
        TasksDomain.Color(colorId = R.color.light_purple, name = "Light purple"),
        TasksDomain.Color(colorId = R.color.purple, name = "Purple"),
        TasksDomain.Color(colorId = R.color.faded_purple, name = "Faded purple"),
        TasksDomain.Color(colorId = R.color.orange, name = "Orange"),
        TasksDomain.Color(colorId = R.color.brown, name = "Brown"),
        TasksDomain.Color(colorId = R.color.peach, name = "Peach"),
        TasksDomain.Color(colorId = R.color.off_white, name = "Off white"),
        TasksDomain.Color(colorId = R.color.same_dark_purple, name = "Sky blue"),
        TasksDomain.Color(colorId = R.color.grey, name = "Grey")
    )
}

private fun positiveWhoConfirmedCandidateLivesHere(): List<String> = arrayListOf(
    "Family Members", "Gatekeeper", "Neighbor", "Passer-by", "Near-by-Retailer", "Maid"," Landlord/Landlady", "Security Personnel", "Self"

)

private fun negativeWhoConfirmedCandidateLiveHere(): List<String> = arrayListOf(
    "Gatekeeper", "Nearby Retailer", "Landlord", "Security Personnel", "Neighbor", NO_ONE
)

private fun cantLocateAddressReasons() = arrayListOf("Address does not exist", "Incorrect address")