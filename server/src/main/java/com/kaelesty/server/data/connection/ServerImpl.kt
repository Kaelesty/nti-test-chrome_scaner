package com.kaelesty.server.data.connection

import android.util.Log
import com.kaelesty.server.data.logs.LogsTool
import com.kaelesty.server.data.scanner.PeriodicalScanner
import com.kaelesty.server.data.scanner.ScannerRepoImpl
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
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServerImpl @Inject constructor(
	private val periodicalScanner: PeriodicalScanner,
	private val scannerRepo: ScannerRepoImpl,
): Server {

	private val scope = CoroutineScope(Dispatchers.Default)

	private val _serverStateFlow = MutableStateFlow(Server.ServerState.STOPPED)
	override val serverStateFlow: StateFlow<Server.ServerState>
		get() = _serverStateFlow.asStateFlow()

	private val _actionsToExecute = MutableSharedFlow<ServerAction>()
	override val actionsToExecute: SharedFlow<ServerAction>
		get() = _actionsToExecute.asSharedFlow()

	private var isScanningStarted = false

	init {
		scope.launch {
			periodicalScanner.newScansFlow.collect {
				executeAction(
					ServerAction.NewScan(it)
				)
			}
		}
	}

	override fun handleClientAction(action: ClientAction) {
		Log.d("ServerImpl", "Action Received: $action")
		when (action) {
			is ClientAction.StartScanning -> {
				isScanningStarted = true
				executeAction(ServerAction.SetScanningState(true))
				periodicalScanner.startScanning(action.delaySeconds)
			}

			is ClientAction.StopScanning -> {
				isScanningStarted = false
				executeAction(ServerAction.SetScanningState(false))
				periodicalScanner.stopScanning()
			}

			is ClientAction.RequestScanningState -> {
				executeAction(
					ServerAction.SetScanningState(isScanningStarted)
				)
			}

			is ClientAction.RequestScanList -> {
				scope.launch {
					executeAction(
						ServerAction.SetScanList(
							scannerRepo.getScansStatic()
						)
					)
				}
			}

			is ClientAction.RestoreScan -> {
				scope.launch {
					scannerRepo.restoreFileSystemByScan(
						action.scanId,
						onStart = { executeAction(ServerAction.RestoringStarted) },
						onFinish = { executeAction(ServerAction.RestoringFinished) }
					)
				}
			}
		}
	}

	override fun executeAction(action: ServerAction) {
		scope.launch {
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