package com.kaelesty.client.domain.client

import com.kaelesty.shared.domain.ClientAction
import com.kaelesty.shared.domain.ServerAction
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface Client {

	val actionsToExecute: SharedFlow<ClientAction>

	fun handleServerAction(action: ServerAction)

	fun executeAction(action: ClientAction)

	fun connect()

	fun reconnect()

	fun disconnect()

	val connectionStateFlow: StateFlow<ConnectionState>

	enum class ConnectionState(val message: String) {
		DISCONNECT("Disconnected"), CONNECTED("Connected"), LOADING("Loading...")
	}
}