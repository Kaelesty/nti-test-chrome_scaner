package com.kaelesty.nti_test_chrome_scaner.presentation.main


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.kaelesty.nti_test_chrome_scaner.R
import com.kaelesty.nti_test_chrome_scaner.domain.client.Client
import com.kaelesty.nti_test_chrome_scaner.presentation.WebSocketClient
import com.kaelesty.shared.domain.ServerAction
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@AndroidEntryPoint
class ClientService : Service() {

	@Inject lateinit var webSocketClient: WebSocketClient
	@Inject lateinit var client: Client


	private val scope = CoroutineScope(Dispatchers.IO)

	override fun onBind(intent: Intent): IBinder? {
		return null
	}

	override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
		scope.launch {
			webSocketClient.connect()
		}
		scope.launch {
			webSocketClient.listen {
				Log.d("ClientService", "ServerAction received")
				client.handleServerAction(
					Json.decodeFromString<ServerAction>(it)
				)
			}
		}
		scope.launch {
			client.actionsToExecute.collect {
				webSocketClient.send(
					Json.encodeToString(it)
				)
			}
		}
		startForeground(5002, getNotification("Chrome Scanner Client", "..."))
		return START_STICKY
	}

	private fun getNotification(title: String, text: String): Notification {
		val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
		val notificationChannel = NotificationChannel(
			"5003",
			"chromeScanerClient",
			NotificationManager.IMPORTANCE_DEFAULT
		)
		notificationManager.createNotificationChannel(notificationChannel)
		val notification: Notification = NotificationCompat.Builder(this, "5003")
			.setContentTitle(title)
			.setContentText(text)
			.setSmallIcon(R.drawable.ic_launcher_background)
			.build()
		return notification
	}

	companion object {
		fun newIntent(context: Context) = Intent(
			context, ClientService::class.java
		)
	}
}