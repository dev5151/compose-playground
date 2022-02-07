package com.example.composeexperiment.services

import android.content.BroadcastReceiver
import android.content.Context
import android.os.Bundle
import android.telephony.SmsMessage
import java.lang.Exception
import android.os.Build
import android.provider.Telephony
import android.widget.Toast
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.net.Uri

import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.*
import com.example.composeexperiment.MainActivity
import com.example.composeexperiment.R
import com.example.composeexperiment.utils.sendNotification
import java.util.regex.Matcher
import java.util.regex.Pattern


class SmsService : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent!!.action.equals("android.provider.Telephony.SMS_RECEIVED")) {
            val smsMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            for (message in smsMessages) {
                // Do whatever you want to do with SMS.
                val sender: String = message.displayOriginatingAddress
                val messageBody: String = message.getMessageBody()
                val otp = messageBody.replace("[^0-9]".toRegex(), "") // here abcd contains otp
                Toast.makeText(context, messageBody.toString(), Toast.LENGTH_LONG).show()
/*
                val notificationManager = ContextCompat.getSystemService(
                    context!!,
                    NotificationManager::class.java
                ) as NotificationManager

                notificationManager.sendNotification(
                    otp, context
                )*/

                buildNotificationChannel(context = context!!)
                buildNotificationAnotherChannel(context = context)
                sendNotification(otp, context)
                sendGeneralNotification(body = messageBody, context = context)
            }

        } // bundle null
    }

    private fun buildNotificationChannel(context: Context?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                "otp_notification",
                "My Default Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            // Configure the notification channel.
            notificationChannel.description = "Default Channel"
            //notificationChannel.enableVibration(true)
            notificationChannel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
            val notificationManager = ContextCompat.getSystemService(
                context!!,
                NotificationManager::class.java
            ) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun buildNotificationAnotherChannel(context: Context?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                "general_notification",
                "General Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            // Configure the notification channel.
            notificationChannel.description = "General Channel"
            //notificationChannel.enableVibration(true)
            notificationChannel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
            val notificationManager = ContextCompat.getSystemService(
                context!!,
                NotificationManager::class.java
            ) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun sendNotification(body: String, context: Context?) {
        val notificationBuilder: NotificationCompat.Builder

        val intent = Intent(context!!, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder = NotificationCompat.Builder(context, "otp_notification")
            notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setStyle(NotificationCompat.BigTextStyle().bigText(body))
                .setContentTitle("Otp")
                .setColor(ContextCompat.getColor(context, R.color.black))
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentText(body)
                .setVibrate(longArrayOf(0L))
                //.setLargeIcon(image.get())
                //.setSound(soundUri)
                .setContentIntent(pendingIntent)
        } else {
            notificationBuilder = NotificationCompat.Builder(context, "otp_notification")
            notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Otp")
                .setContentText(body)
                .setColor(ContextCompat.getColor(context, R.color.black))
                .setVibrate(longArrayOf(0L))
                //.setSound(soundUri)
                //  .setLargeIcon(image.get())
                .setContentIntent(pendingIntent)
        }
        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager

        notificationManager.notify(/*notification id*/1, notificationBuilder.build());
    }

    private fun sendGeneralNotification(body: String, context: Context?) {
        val notificationBuilder: NotificationCompat.Builder

        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("https://www.google.com")


        val pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder = NotificationCompat.Builder(context!!, "general_notification")
            notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setStyle(NotificationCompat.BigTextStyle().bigText(body))
                .setContentTitle("General")
                .setColor(ContextCompat.getColor(context, R.color.black))
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentText(body)
                .setVibrate(longArrayOf(0L))
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        context.resources,
                        R.drawable.ic_android
                    )
                )
                //.setSound(soundUri)
                .setContentIntent(pendingIntent)
        } else {
            notificationBuilder = NotificationCompat.Builder(context!!, "general_notification")
            notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("General")
                .setContentText(body)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setColor(ContextCompat.getColor(context, R.color.black))
                .setVibrate(longArrayOf(0L))
                //.setSound(soundUri)
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        context.resources,
                        R.drawable.ic_android
                    )
                )
                .setContentIntent(pendingIntent)
        }
        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager

        notificationManager.notify(/*notification id*/1, notificationBuilder.build());
    }

    /*fun extractURL(
        str: String
    ): Uri {

        // Creating an empty ArrayList
        val list: MutableList<String> = ArrayList()
        var url: Uri = Uri.parse("")

        // Regular Expression to extract
        // URL from the string
        val regex = ("\\b((?:https?|ftp|file):"
                + "//[-a-zA-Z0-9+&@#/%?="
                + "~_|!:, .;]*[-a-zA-Z0-9+"
                + "&@#/%=~_|])")

        // Compile the Regular Expression
        val p: Pattern = Pattern.compile(
            regex,
            Pattern.CASE_INSENSITIVE
        )

        // Find the match between string
        // and the regular expression
        val m: Matcher = p.matcher(str)

        // Find the next subsequence of
        // the input subsequence that
        // find the pattern
        while (m.find()) {

            // Find the substring from the
            // first index of match result
            // to the last index of match
            // result and add in the list
            list.add(
                str.substring(
                    m.start(0), m.end(0)
                )
            )
        }

        // Print all the URLs stored
        url = Uri.parse(list[0])
        return url
    }*/


}