package com.youverify.agent_app_android.util.helper

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.youverify.agent_app_android.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NotificationHelper @Inject constructor (@ApplicationContext context: Context) : ContextWrapper(context) {

    private val smallIcon = R.drawable.ic_launcher_background

    private val notificationManager by lazy { getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }

    init {
        createNotificationChannel(
            AGENT_APP_DEFAULT_NOTIFICATION_CHANNEL,
            AGENT_APP_DEFAULT_NOTIFICATION_CHANNEL
        )
    }


    fun createNotification(
        title: String?,
        body: String? = null,
        channelId: String,
        pendingIntent: PendingIntent? = null,
        silent: Boolean = false,
        group: String? = null,
        largeImage: Bitmap? = null
    ): NotificationCompat.Builder {
        val builder = NotificationCompat.Builder(applicationContext, channelId)
        //Support for lower api devices
        builder.setContentTitle(title)
        builder.setContentText(body)
        largeImage?.let { builder.setLargeIcon(largeImage) }

        if (largeImage == null) {
            val messageStyle = getMessageStyle(title, body)
            builder.setStyle(messageStyle)
        } else {
            val bigPicture = getBigPictureStyle(largeImage, title, body)
            builder.setStyle(bigPicture)
        }

        builder.setSmallIcon(smallIcon)
        builder.setAutoCancel(true)
        builder.color = ContextCompat.getColor(applicationContext, R.color.colorPrimary)
        pendingIntent?.let { builder.setContentIntent(pendingIntent) }
        group?.let { builder.setGroup(it) }
        builder.priority =
            if (silent) NotificationCompat.PRIORITY_LOW else NotificationCompat.PRIORITY_HIGH
        builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        return builder
    }

    private fun getBigPictureStyle(
        largeImage: Bitmap,
        title: String?,
        body: String?
    ): NotificationCompat.BigPictureStyle {
        val bigStyle = NotificationCompat.BigPictureStyle()
        bigStyle.setBigContentTitle(title)
        bigStyle.bigPicture(largeImage)
        bigStyle.bigLargeIcon(null)
        return bigStyle
    }

    private fun getMessageStyle(title: String?, body: String?): NotificationCompat.BigTextStyle {
        val bigStyle = NotificationCompat.BigTextStyle()
        bigStyle.setBigContentTitle(title)
        body?.let { bigStyle.bigText(it) }
        return bigStyle
    }

    private fun createNotificationChannel(channelId: String, channelName: String) {
        if (isOreoPlus()) {
            val channel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            channel.lightColor = ContextCompat.getColor(this, R.color.colorPrimary)
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun notify(notificationKey: Int, notification: Notification) {
        notificationManager.notify(notificationKey, notification)
    }

    companion object {
        const val AGENT_APP_DEFAULT_NOTIFICATION_CHANNEL = "VPD_DEFAULT_NOTIFICATION_CHANNEL"
        const val AGENT_APP_DEFAULT_NOTIFICATION_KEY = 9001
    }
}