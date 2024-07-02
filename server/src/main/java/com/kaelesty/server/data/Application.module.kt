package com.kaelesty.server.data

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import io.ktor.websocket.send

fun Application.module() {

}

fun Application.configureSockets() {
	 install(WebSockets) {

	 }

	routing {
		webSocket("/actions") {
			send("Connection established")
			for (frame in incoming) {
				frame as Frame.Text
				val text = frame.readText()
				send("Received: $text")
			}
		}
	}
}