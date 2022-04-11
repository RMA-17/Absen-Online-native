package com.pklproject.checkincheckout.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.pklproject.checkincheckout.MainActivity
import com.pklproject.checkincheckout.R

class NotificationWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {

        showNotification()

        Log.d("NotificationWorker", "Performing long running task in scheduled job")
        return Result.success()
    }

    private fun showNotification() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0)

        val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setContentTitle("Sudah absen?")
            .setContentText("Segera absen sekarang jika belum")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID)
            val channel = NotificationChannel(CHANNEL_ID, "Pengingat Absen", NotificationManager.IMPORTANCE_DEFAULT)
            channel.lightColor = Color.RED
            channel.description = "Pengingat absen untuk aplikasi Check In Check Out"
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        with (NotificationManagerCompat.from(applicationContext)) {
            notify(NOTIFICATION_ID, builder.build())
        }
    }

    companion object {
        const val NOTIFICATION_ID = 1
        const val CHANNEL_ID = "notificationId"
    }
}