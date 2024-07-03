package com.kaelesty.client.presentation

import android.util.Log
import com.kaelesty.client.domain.client.Client
import com.kaelesty.shared.domain.ServerAction
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class WebSocketClient(
	private val client: Client,
) {

	private val httpClient = HttpClient(CIO) {
		install(WebSockets)
	}
	private val _messageFlow = MutableSharedFlow<String>()

	private var webSocketSession: WebSocketSession? = null

	private val scope = CoroutineScope(Dispatchers.IO)

	suspend fun connect(url: String) {
		Log.d("WebSocketClient", "connecting to $url")
		webSocketSession = httpClient.webSocketSession(url).also {
			scope.launch {
				_messageFlow.collect { message ->
					it.send(Frame.Text(message))
					Log.d("WebSocketClient", "Sent to server: $message")
				}
			}
			it.incoming.consumeEach {
				client.handleServerAction(
					Json.decodeFromString<ServerAction>((it as Frame.Text).readText()).also {
						Log.d("WebSocketClient", "Server action received: $it")
					}
				)
			}
		}

	}

	suspend fun send(message: String) {
		_messageFlow.emit(message)
	}

	suspend fun disconnect() {
		webSocketSession?.close()
		//httpClient.close()
	}

	suspend fun listen(onMessageReceived: suspend (String) -> Unit) {
		webSocketSession?.incoming?.consumeEach { frame ->
			if (frame is Frame.Text) {
				onMessageReceived(frame.readText())
			}
		}
	}
}