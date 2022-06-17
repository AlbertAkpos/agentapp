package com.youverify.agent_app_android.util.helper

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.os.Environment
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import javax.inject.Inject

class FileHelper @Inject constructor(@ApplicationContext private val context: Context): ContextWrapper(context) {

    fun writeToFile(bitmap: Bitmap, filename: String): File? {
        val directoryName = "AgentApp"
        val directory = File(
            context.applicationContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
            directoryName
        )

        if (!directory.exists()) {
            directory.mkdir()
        }
        val imageFile = File(directory, filename + Date().time)

        return try {
            FileOutputStream(imageFile.absolutePath).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out) // bmp is your Bitmap instance
            }
            imageFile
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

}