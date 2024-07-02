package com.kaelesty.server.data.connection

import android.util.Log
import com.kaelesty.server.domain.connection.Server
import com.kaelesty.shared.domain.ClientAction
import com.kaelesty.shared.domain.ServerAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

object ServerImpl: Server {

	private val scope = CoroutineScope(Dispatchers.Default)

	private val _actionsToExecute = MutableSharedFlow<ServerAction>()
	override val actionsToExecute: SharedFlow<ServerAction>
		get() = _actionsToExecute.asSharedFlow()

	override fun handleClientAction(action: ClientAction) {

	}

	override fun executeAction(action: ServerAction) {
		scope.launch {
			Log.d("WebSocketConnection", "Action executing")
			_actionsToExecute.emit(action)
		}
	}
}