package com.kaelesty.server.domain.connection

import com.kaelesty.shared.domain.ClientAction
import com.kaelesty.shared.domain.ServerAction
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface Server {

	val actionsToExecute: SharedFlow<ServerAction>

	val serverStateFlow: StateFlow<ServerState>

	fun handleClientAction(action: ClientAction)

	fun executeAction(action: ServerAction)

	fun switchServer()

	fun startServer()

	enum class ServerState {
		STARTED, STOPPED, STARTING
	}
}