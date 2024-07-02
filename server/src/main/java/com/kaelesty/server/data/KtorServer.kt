package com.kaelesty.server.data

import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import javax.inject.Inject

class KtorServer @Inject constructor() {

	fun run() {
		embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
			.start(wait = true)
	}
}