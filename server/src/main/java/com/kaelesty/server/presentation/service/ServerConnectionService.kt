package com.kaelesty.server.presentation.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.kaelesty.server.R
import com.kaelesty.server.data.logs.LogsTool
import com.kaelesty.server.domain.connection.Server
import com.kaelesty.shared.domain.ClientAction
import com.kaelesty.shared.domain.MemoryUsage
import com.kaelesty.shared.domain.ServerAction
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@AndroidEntryPoint
class ServerConnectionService : Service() {

	private lateinit var engine: NettyApplicationEngine

	@Inject lateinit var server: Server

	private val scope = CoroutineScope(Dispatchers.IO)

	private fun getEngine() = embeddedServer(Netty, port = 8080) {
		install(WebSockets)

		routing {
			webSocket("/actions") {

				scope.launch {
					server.actionsToExecute.collect {
						Log.d("WebSocketConnection", "Action sent")
						send(
							Json.encodeToString(it)
						)
					}
				}

				incoming.consumeEach { frame ->
					if (frame is Frame.Text) {
						val action = Json.decodeFromString<ClientAction>(frame.readText())
						server.handleClientAction(action)
					}
				}
			}
		}
	}

	override fun onBind(intent: Intent): IBinder? {
		return null
	}

	override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
		scope.launch(Dispatchers.Default) {
			engine = getEngine()
			LogsTool.log("Server started")
			engine.start(wait = true)
		}
		startForeground(5000, getNotification("Chrome Scanner Server", "..."))
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
			context, ServerConnectionService::class.java
		)
	}
}