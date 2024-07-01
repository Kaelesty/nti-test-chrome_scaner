package com.kaelesty.server.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaelesty.server.data.logs.LogsTool
import com.kaelesty.server.domain.connection.ConnectionRepo
import com.kaelesty.server.domain.scanner.ScannerRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
	private val scannerRepo: ScannerRepo,
	private val connectionRepo: ConnectionRepo,
): ViewModel() {

	val MEMORY_USAGE_OBSERVATION_DELAY_MILLIS = 100L

	data class State(
		val port: String = "",
		val editablePort: String = "",
		val isPortInvalid: Boolean = false,
		val isServerStarted: Boolean = false,
		val memoryUsage: MemoryUsage = MemoryUsage(),
		val logs: List<String> = listOf(),
	) {
		data class MemoryUsage(
			val usedMemory: Float = 0f,
			val availableMemory: Float = 0f
		)
	}

	private val _state = MutableStateFlow(State())
	val state: StateFlow<State> get() = _state

	init {
		collectCurrentServerPort()
		observeMemoryUsage()
		observeLogs()
	}

	fun setPort(newValue: String) {
		viewModelScope.launch(Dispatchers.IO) {
			_state.emit(
				_state.value.copy(
					editablePort = newValue,
					isPortInvalid = false
				)
			)
		}
	}

	fun savePort() {
		viewModelScope.launch(Dispatchers.IO) {
			val port = _state.value.editablePort
			if (validatePort(port)) {
				connectionRepo.saveServerPort(port)
			}
			else {
				_state.emit(
					_state.value.copy(
						isPortInvalid = true
					)
				)
			}
		}
	}

	fun switchServer() {

	}

	private fun validatePort(port: String) = try {
		port.toInt() in 0 .. 65535
	} catch (e: NumberFormatException) {
		false
	}

	private fun collectCurrentServerPort() {
		viewModelScope.launch(Dispatchers.IO) {
			connectionRepo.getServerPort().collect {
				it?.let {
					_state.emit(
						_state.value.copy(
							port = it,
							editablePort = it
						)
					)
				}
			}
		}
	}

	private fun observeMemoryUsage() {
		viewModelScope.launch(Dispatchers.Default) {
			val runtime = Runtime.getRuntime()
			while (true) {
				_state.emit(
					_state.value.copy(
						memoryUsage = State.MemoryUsage(
							usedMemory = (runtime.totalMemory() - runtime.freeMemory())
								.bytesToMb()
							,
							availableMemory = runtime.totalMemory().bytesToMb()
						)
					)
				)
				delay(MEMORY_USAGE_OBSERVATION_DELAY_MILLIS)
			}
		}
	}

	private fun observeLogs() {
		viewModelScope.launch(Dispatchers.Default) {
			LogsTool.getLogs().collect {
				_state.emit(
					_state.value.copy(
						logs = it
					)
				)
			}
		}
	}
}

fun Long.bytesToMb(): Float {
	return this.toFloat() / 1024 / 1024
}