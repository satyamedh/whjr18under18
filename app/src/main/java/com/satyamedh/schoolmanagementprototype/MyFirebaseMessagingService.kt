package com.satyamedh.schoolmanagementprototype

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.satyamedh.schoolmanagementprototype.MyFirebaseMessagingService
import android.content.Intent
import com.satyamedh.schoolmanagementprototype.MasterActivity
import android.app.PendingIntent
import android.media.RingtoneManager
import com.satyamedh.schoolmanagementprototype.R
import android.app.NotificationManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.ktx.Firebase

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        var notificationTitle: String? = null
        var notificationBody: String? = null

        // Check if message contains a notification payload
        if (remoteMessage.notification != null) {
            Log.d(
                TAG, "Message Notification Body: " + remoteMessage.notification!!
                    .body
            )
            notificationTitle = remoteMessage.notification!!.title
            notificationBody = remoteMessage.notification!!.body
        }

        // If you want to fire a local notification (that notification on the top of the phone screen)
        // you should fire it from here
        sendLocalNotification(notificationTitle, notificationBody)
    }

    private fun sendLocalNotification(notificationTitle: String?, notificationBody: String?) {
        val intent = Intent(this, MasterActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT
        )
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this)
            .setAutoCancel(true) //Automatically delete the notification
            .setSmallIcon(R.drawable.ic_launcher_foreground) //Notification icon
            .setContentIntent(pendingIntent)
            .setContentTitle(notificationTitle)
            .setContentText(notificationBody)
            .setSound(defaultSoundUri)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1234, notificationBuilder.build())
    }

    fun onTokenRefresh() {
        val refreshedToken = FirebaseInstanceId.getInstance().token
        Log.d(TAG, "Refreshed token: $refreshedToken")



        val db = Firebase.database
        val auth = Firebase.auth


        if (auth.currentUser == null){
            return
        }

        val ref = db.reference.child("Users").child(auth.currentUser.uid)
        ref.setValue(refreshedToken)

    }

    companion object {
        private const val TAG = "FirebaseMessagingServce"
    }
}