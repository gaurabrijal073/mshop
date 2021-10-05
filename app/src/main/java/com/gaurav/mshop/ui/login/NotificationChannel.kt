package com.gaurav.mshop.ui.login

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

class NotificationChannel(val context: Context) {
    val loginChannel: String = "Channel1"

    fun createNotificationChannels(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel1 = NotificationChannel(
                loginChannel, "Channel 1", NotificationManager.IMPORTANCE_HIGH
            )
            channel1.description = "This is channel one!!!!!!!"

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel1)
        }
    }
}