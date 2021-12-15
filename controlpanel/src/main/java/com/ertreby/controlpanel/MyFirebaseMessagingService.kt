package com.ertreby.controlpanel

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
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

        val notificationBuilder=NotificationCompat.Builder(this,"New Orders")
            .setContentTitle(notification.title)
            .setContentText(notification.body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)

        with(NotificationManagerCompat.from(this)){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                val channel=NotificationChannel(CHANNEL_ID, CHANNEL_ID,NotificationManager.IMPORTANCE_HIGH)
                channel.lockscreenVisibility=NotificationCompat.VISIBILITY_PUBLIC
                createNotificationChannel(channel)
            }
            notify(NOTIFICATION_ID,notificationBuilder.build())
        }

        playNotificationSound()
    }

    private fun playNotificationSound() {
        try {
            val ringtoneUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val ringtone=RingtoneManager.getRingtone(this,ringtoneUri)
            ringtone.play()
        }catch (e:Exception){
            e.printStackTrace()
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
        const val NOTIFICATION_ID=34556
        const val CHANNEL_ID = "restaurant message"
    }

}