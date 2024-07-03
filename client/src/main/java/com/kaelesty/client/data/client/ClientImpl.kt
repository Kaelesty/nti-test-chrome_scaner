package com.kaelesty.client.data.client

import android.util.Log
import com.kaelesty.client.data.memoryusage.ServerStateRepoImpl
import com.kaelesty.client.data.scanlist.ScanListRepoImpl
import com.kaelesty.client.data.uilock.UiLockTool
import com.kaelesty.client.domain.client.Client
import com.kaelesty.shared.domain.ClientAction
import com.kaelesty.shared.domain.ServerAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

object ClientImpl: Client {

	private val scope = CoroutineScope(Dispatchers.IO)

	private val _connectionStateFlow = MutableStateFlow(Client.ConnectionState.DISCONNECT)
	override val connectionStateFlow: StateFlow<Client.ConnectionState>
		get() = _connectionStateFlow.asStateFlow()

	private val _actionsToExecute = MutableSharedFlow<ClientAction>()
	override val actionsToExecute: SharedFlow<ClientAction>
		get() = _actionsToExecute.asSharedFlow()

	override fun handleServerAction(action: ServerAction) {
		when (action) {
			is ServerAction.UpdateMemoryUsage -> {
				scope.launch {
					ServerStateRepoImpl.setMemoryUsage(action.memoryUsage)
				}
			}

			is ServerAction.SetScanningState -> {
				scope.launch {
					ServerStateRepoImpl.setScanningStateFlow(action.isScanningStarted)
				}
			}

			is ServerAction.NewScan -> {
				ScanListRepoImpl.addScan(action.scan)
			}

			is ServerAction.SetScanList -> {
				ScanListRepoImpl.setScanList(action.scans)
			}

			is ServerAction.RestoringStarted -> {
				scope.launch {
					UiLockTool.setUiLockState(true)
				}
			}

			is ServerAction.RestoringFinished -> {
				scope.launch {
					UiLockTool.setUiLockState(false)
				}
			}
		}
	}

	override fun executeAction(action: ClientAction) {
		scope.launch {
			_actionsToExecute.emit(action)
		}
	}

	override fun reconnect() {
		Log.d("ClientConnection", "reconnecting")
		scope.launch {
			_connectionStateFlow.emit(Client.ConnectionState.LOADING)
			delay(3000)
			executeAction(
				ClientAction.RequestScanningState
			)
			executeAction(
				ClientAction.RequestScanList
			)
		}
	}

	override fun disconnect() {
		scope.launch {
			_connectionStateFlow.emit(Client.ConnectionState.DISCONNECT)
		}
	}

	override fun connect() {
		scope.launch {
			_connectionStateFlow.emit(Client.ConnectionState.CONNECTED)
		}
	}
}