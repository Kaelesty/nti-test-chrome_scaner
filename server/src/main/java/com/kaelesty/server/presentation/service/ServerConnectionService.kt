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
import com.kaelesty.server.domain.connection.ConnectionRepo
import com.kaelesty.server.domain.connection.Server
import com.kaelesty.shared.domain.ClientAction
import com.kaelesty.shared.domain.MemoryUsage
import com.kaelesty.shared.domain.ServerAction
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.engine.stop
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@AndroidEntryPoint
class ServerConnectionService : Service() {

	@Inject lateinit var server: Server
	@Inject lateinit var connectionRepo: ConnectionRepo

	private lateinit var port: StateFlow<String?>

	private var engine: NettyApplicationEngine? = null

	private val scope = CoroutineScope(Dispatchers.IO)
	private val serverScope = CoroutineScope(
		Dispatchers.Default
				+ CoroutineExceptionHandler { _, throwable ->
					server.switchServer()
					Log.d("ServerConnectionService", throwable.message.toString())
				}
				+ SupervisorJob()
	)

	private fun getEngine(port: Int) = embeddedServer(Netty, port = port) {
		install(WebSockets)

		routing {
			webSocket("/actions") {

				scope.launch {
					server.actionsToExecute.collect {
						send(
							Json.encodeToString(it)
						)
					}
				}
				this.incoming.consumeEach { frame ->
					Log.d("ServerConnectionService", "New incoming")
					if (frame is Frame.Text) {
						val action = Json.decodeFromString<ClientAction>(frame.readText())
						server.handleClientAction(action)
					}
				}
			}
		}
	}

	private fun startServer() {
		val portValue = port.value
		if (portValue != null){
			serverScope.launch {
				engine = getEngine(portValue.toInt())
				LogsTool.log("Server started")
				engine?.start(wait = true)
			}
			server.startServer()
		}
		else {
			server.switchServer()
		}
	}

	private fun stopServer() {
		serverScope.coroutineContext.cancelChildren()
		engine?.stop()
		engine = null
		scope.launch {
			LogsTool.log("Server stopped")
		}
	}



	override fun onBind(intent: Intent): IBinder? {
		return null
	}

	override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

		port = connectionRepo.getServerPort()
			.stateIn(
				scope = scope,
				started = SharingStarted.Lazily,
				initialValue = null
			).also {
				scope.launch {
					it.collect {
						LogsTool.log("New port: $it")
					}
				}
			}

		scope.launch(Dispatchers.IO) {
			server.serverStateFlow.collect {
				when (it) {
					Server.ServerState.STARTED -> {

					}
					Server.ServerState.STOPPED -> {
						stopServer()
					}
					Server.ServerState.STARTING -> {
						startServer()
					}
				}
			}
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