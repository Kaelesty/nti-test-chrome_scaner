package com.kaelesty.server.data

import com.kaelesty.server.data.logs.LogsTool
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import java.net.InetSocketAddress
import java.nio.ByteBuffer

class CustomWebSocketServer(
	port: Int? = null
) : WebSocketServer(InetSocketAddress(port ?: PORT)) {

	private val scope = CoroutineScope(Dispatchers.IO)

	override fun onOpen(conn: WebSocket?, handshake: ClientHandshake?) = Unit

	override fun onClose(conn: WebSocket?, code: Int, reason: String?, remote: Boolean) = Unit

	override fun onMessage(conn: WebSocket?, message: String?)  {
		scope.launch {
			LogsTool.log(message ?: "nullmessage")
		}
	}


	override fun onStart() = Unit

	override fun onError(conn: WebSocket?, ex: Exception?) = Unit

	companion object {
		internal const val PORT = 50123
	}
}