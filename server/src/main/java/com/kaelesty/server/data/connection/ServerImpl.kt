package com.kaelesty.server.data.connection

import android.util.Log
import com.kaelesty.server.domain.connection.Server
import com.kaelesty.shared.domain.ClientAction
import com.kaelesty.shared.domain.ServerAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

object ServerImpl: Server {

	private val scope = CoroutineScope(Dispatchers.Default)

	private val _serverStateFlow = MutableStateFlow(Server.ServerState.STOPPED)
	override val serverStateFlow: StateFlow<Server.ServerState>
		get() = _serverStateFlow.asStateFlow()

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

	override fun switchServer() {
		scope.launch {
			_serverStateFlow.emit(
				if (_serverStateFlow.value == Server.ServerState.STOPPED) {
					Server.ServerState.STARTING
				}
				else {
					Server.ServerState.STOPPED
				}
			)
		}
	}

	override fun startServer() {
		scope.launch {
			_serverStateFlow.emit(
				Server.ServerState.STARTED
			)
		}
	}
}