package com.youverify.agent_app_android.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.youverify.agent_app_android.data.model.response.ErrorMessage
import okhttp3.ResponseBody

fun handleErrorMessage(res: ResponseBody): ErrorMessage {

    var errorMessage = ErrorMessage()
    val gson = Gson()
    val type = object : TypeToken<ErrorMessage>() {}.type
    val errorResponse: ErrorMessage? = gson.fromJson(res.charStream(), type)
    if (errorResponse != null) {
        errorMessage = errorResponse
    }
    return errorMessage
}