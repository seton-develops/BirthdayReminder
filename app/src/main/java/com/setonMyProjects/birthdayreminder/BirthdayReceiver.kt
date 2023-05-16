package com.setonMyProjects.birthdayreminder

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.setonMyProjects.birthdayreminder.R


class BirthdayReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val name = intent.getStringExtra("name")
        val age = intent.getIntExtra("age", -1)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        val bitmapImage = BitmapFactory.decodeResource(context.resources,
            R.drawable.cake_choco
        )

        val notification = NotificationCompat.Builder(context,
            BirthdayNotificationService.NOTIF_CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_balloon_icon)
            .setColor(context.resources.getColor(R.color.slate_pink))
            .setColorized(true)
            .setContentTitle("Birthday Alert!")
            .setContentText("REMINDER: Happy Birthday to $name ($age yrs old). " +
                    "Wish them well on their special day! ")
            .setStyle(NotificationCompat.BigTextStyle())
            .setStyle(NotificationCompat.BigPictureStyle().bigLargeIcon(bitmapImage))
            .build()

        notificationManager.notify(intent.getIntExtra("id", -1), notification)
    }

}