package com.kaelesty.server.data

import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.http.ContentType.Application.Json
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class KtorConnection @Inject constructor(
	private val client: HttpClient
): Connection {

	private var session: WebSocketSession? = null

	override fun getStateStream(): Flow<String> {
		return flow {
			session = client.webSocketSession {
				url("wss://echo.websocket.org")
			}
			val messageStates = session!!
				.incoming
				.consumeAsFlow()
				.filterIsInstance<Frame.Text>()
				.mapNotNull {
					it.readText()
				}

			emitAll(messageStates)
		}
	}

	override suspend fun sendAction(action: String) {
		session?.outgoing?.send(
			Frame.Text(action)
		)
	}

	override suspend fun close() {
		session?.close()
		session = null
	}
}