package com.kaelesty.nti_test_chrome_scaner.presentation

import android.util.Log
import com.kaelesty.nti_test_chrome_scaner.domain.client.Client
import com.kaelesty.shared.domain.ServerAction
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.channels.consumeEach
import kotlinx.serialization.json.Json

class WebSocketClient(
	private val client: Client,
) {

	private val httpClient = HttpClient(CIO) {
		install(WebSockets)
	}

	private var webSocketSession: WebSocketSession? = null

	suspend fun connect(url: String) {
		Log.d("ClientConnection", "connecting to $url")
		webSocketSession = httpClient.webSocketSession(url).also {
			it.incoming.consumeEach {
				Log.d("WebSocketClient", "Server action received")
				client.handleServerAction(
					Json.decodeFromString<ServerAction>((it as Frame.Text).readText())
				)
			}
		}

	}

	suspend fun send(message: String) {
		webSocketSession?.let {
			it.send(Frame.Text(message))
			Log.d("MainService", "sent finished")
		}
	}

	suspend fun disconnect() {
		webSocketSession?.close()
		httpClient.close()
	}

	suspend fun listen(onMessageReceived: suspend (String) -> Unit) {
		webSocketSession?.incoming?.consumeEach { frame ->
			if (frame is Frame.Text) {
				onMessageReceived(frame.readText())
			}
		}
	}
}