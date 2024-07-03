package com.kaelesty.server.presentation.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaelesty.server.data.logs.LogsTool
import com.kaelesty.server.domain.connection.GetServerPortUseCase
import com.kaelesty.server.domain.connection.SaveServerPortUseCase
import com.kaelesty.server.domain.connection.Server
import com.kaelesty.shared.domain.MemoryUsage
import com.kaelesty.shared.domain.ServerAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
	private val getServerPortUseCase: GetServerPortUseCase,
	private val saveServerPortUseCase: SaveServerPortUseCase,
	private val server: Server,
): ViewModel() {

	val MEMORY_USAGE_OBSERVATION_DELAY_MILLIS = 100L

	data class State(
		val port: String = "",
		val editablePort: String = "",
		val isPortInvalid: Boolean = false,
		val isServerStarted: Boolean = false,
		val memoryUsage: MemoryUsage = MemoryUsage(),
		val logs: List<String> = listOf(),
	)

	private val _state = MutableStateFlow(State())
	val state: StateFlow<State> get() = _state

	init {
		collectCurrentServerPort()
		observeMemoryUsage()
		observeLogs()
		viewModelScope.launch(Dispatchers.IO) {
			server.serverStateFlow.collect {
				Log.d("MainViewModel", "New state: $it")
				_state.emit(
					_state.value.copy(
						isServerStarted = it == Server.ServerState.STARTED
					)
				)
			}
		}
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
				saveServerPortUseCase(port)
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
		server.switchServer()
	}

	private fun validatePort(port: String) = try {
		port.toInt() in 0 .. 65535
	} catch (e: NumberFormatException) {
		false
	}

	private fun collectCurrentServerPort() {
		viewModelScope.launch(Dispatchers.IO) {
			getServerPortUseCase().collect {
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
				val memoryUsage = MemoryUsage(
					usedMemory = (runtime.totalMemory() - runtime.freeMemory())
					,
					availableMemory = runtime.totalMemory()
				)

				server.executeAction(
					ServerAction.UpdateMemoryUsage(
						memoryUsage
					)
				)

				_state.emit(
					_state.value.copy(
						memoryUsage = memoryUsage
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