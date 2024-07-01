package com.kaelesty.server.presentation.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.kaelesty.server.R
import com.kaelesty.server.data.KtorConnection
import com.kaelesty.server.data.logs.LogsTool
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ConnectionService : Service() {

	@Inject lateinit var connection: KtorConnection

	private val scope = CoroutineScope(Dispatchers.IO)

	override fun onBind(intent: Intent): IBinder? {
		return null
	}

	override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
		scope.launch {
			LogsTool.log("Connection opened!")
//			connection.getStateStream().collect {
//				Log.d("ConnectionService", it)
//			}
			for (i in 0..99) {
				LogsTool.log("Action $i was sent")
				connection.sendAction(i.toString())
			}
		}
		startForeground(5000, getNotification("Chrome Scanner Connection", "..."))
		return START_STICKY
	}

	private fun getNotification(title: String, text: String): Notification {
		val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
		val notificationChannel = NotificationChannel(
			"5001",
			"chromeScaner",
			NotificationManager.IMPORTANCE_DEFAULT
		)
		notificationManager.createNotificationChannel(notificationChannel)
		val notification: Notification = NotificationCompat.Builder(this, "5001")
			.setContentTitle(title)
			.setContentText(text)
			.setSmallIcon(R.drawable.ic_launcher_background)
			.build()
		return notification
	}

	companion object {
		fun newIntent(context: Context) = Intent(
			context, ConnectionService::class.java
		)
	}
}