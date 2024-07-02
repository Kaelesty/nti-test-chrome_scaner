package com.kaelesty.server.domain.connection

import com.kaelesty.shared.domain.ClientAction
import com.kaelesty.shared.domain.ServerAction
import kotlinx.coroutines.flow.SharedFlow

interface Server {

	val actionsToExecute: SharedFlow<ServerAction>

	fun handleClientAction(action: ClientAction)

	fun executeAction(action: ServerAction)
}