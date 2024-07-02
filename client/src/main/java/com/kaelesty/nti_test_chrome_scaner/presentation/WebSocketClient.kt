package com.kaelesty.nti_test_chrome_scaner.presentation

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.channels.consumeEach

class WebSocketClient(private val url: String) {
	private val client = HttpClient(CIO) {
		install(WebSockets)
	}

	private var webSocketSession: WebSocketSession? = null

	suspend fun connect() {
		webSocketSession = client.webSocketSession(url)
	}

	suspend fun send(message: String) {
		webSocketSession?.let {
			it.send(Frame.Text(message))
			Log.d("MainService", "sent finished")
		}
	}

	suspend fun disconnect() {
		webSocketSession?.close()
		client.close()
	}

	suspend fun listen(onMessageReceived: suspend (String) -> Unit) {
		webSocketSession?.incoming?.consumeEach { frame ->
			if (frame is Frame.Text) {
				onMessageReceived(frame.readText())
			}
		}
	}
}