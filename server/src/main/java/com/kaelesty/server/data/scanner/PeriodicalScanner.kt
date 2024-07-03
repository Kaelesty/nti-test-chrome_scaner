package com.kaelesty.server.data.scanner

import com.kaelesty.server.data.logs.LogsTool
import com.kaelesty.server.domain.connection.Server
import com.kaelesty.server.domain.scanner.ScannerRepo
import com.kaelesty.shared.domain.Scan
import com.kaelesty.shared.domain.ServerAction
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PeriodicalScanner @Inject constructor(
	private val scannerRepo: ScannerRepo,
) {

	private val _newScansFlow = MutableSharedFlow<Scan>()
	val newScansFlow: SharedFlow<Scan> get() = _newScansFlow

	private var delaySeconds: Int = 600

	private val scanningScope = CoroutineScope(
		Dispatchers.Default +
		CoroutineExceptionHandler { context, _ ->
			scope.launch {
				LogsTool.log("Exception when performing a scan")
			}
			context.cancelChildren()
			startScanning(delaySeconds)
		}
		+ SupervisorJob()
	)
	private val scope = CoroutineScope(Dispatchers.IO)

	fun startScanning(delaySeconds_: Int) {
		delaySeconds = delaySeconds_
		scope.launch {
			LogsTool.log("Starting scanning...")
		}
		scanningScope.launch {
			while (true) {
				LogsTool.log("Making scan...")
				val scan = scannerRepo.makeScan()
				LogsTool.log("Scan finished")
				scan?.let {
					_newScansFlow.emit(it)
				}
				delay(delaySeconds * 1000L)
			}
		}
	}

	fun stopScanning() {
		scanningScope.coroutineContext.cancelChildren()
		scope.launch {
			LogsTool.log("Scanning stopped")
		}
	}
}