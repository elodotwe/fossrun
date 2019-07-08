/*
Copyright 2019 Jacob Rau

This file is part of FOSSRun.

FOSSRun is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

FOSSRun is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with FOSSRun.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.jacobarau.fossrun

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import android.util.Log

class TripRecorder : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private val channelID = "1"
    private val TAG = "TripRecorder"

    private val ONGOING_NOTIFICATION_ID = 1
    override fun onCreate() {
        Log.d(TAG, "onCreate")

        val pendingIntent: PendingIntent =
            Intent(this, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent, 0)
            }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(channelID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }


        val notification = NotificationCompat.Builder(this, channelID)
            .setContentTitle(getText(R.string.notification_title))
            .setContentText(getText(R.string.notification_message))
            .setSmallIcon(R.drawable.ic_notification)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(ONGOING_NOTIFICATION_ID, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand")
        if (intent != null) {
            Log.d(TAG, "onStartCommand: $intent")
            when (intent.action) {
                "stop" -> stopRecording()
                "start" -> startRecording()
            }
        }

        return START_STICKY
    }

    private fun stopRecording() {
        getTripModel().stopTrip()
        stopSelf()
    }

    private fun startRecording() {
        getTripModel().startTrip()
    }
}