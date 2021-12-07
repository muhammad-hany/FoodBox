package com.ertreby.controlpanel

import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        if(remoteMessage.data.isNotEmpty()){
            remoteMessage.notification?.let {
                makeNotification(it)
            }
        }
    }

    private fun makeNotification(notification: RemoteMessage.Notification) {
        val intent=Intent(this,MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent=PendingIntent.getActivity(this,0,intent,0)
        val alarmSound: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder=NotificationCompat.Builder(this,"New Orders")
            .setContentTitle(notification.title)
            .setContentText(notification.body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setSound(alarmSound)

        with(NotificationManagerCompat.from(this)){
            notify(NOTIFICATION_ID,notificationBuilder.build())
        }
    }


    override fun onDeletedMessages() {
        super.onDeletedMessages()

    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        FirebaseService.saveTokenInServer(token)
    }

    companion object{
        const val NOTIFICATION_ID=345
    }

}