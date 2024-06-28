package com.kaelesty.nti_test_chrome_scaner.presentation.config

import android.net.InetAddresses
import android.os.Build
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaelesty.nti_test_chrome_scaner.domain.config.GetServerConfigUseCase
import com.kaelesty.nti_test_chrome_scaner.domain.config.SaveServerConfigUseCase
import com.kaelesty.nti_test_chrome_scaner.domain.config.ServerConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfigViewModel @Inject constructor(
	private val getServerConfigUseCase: GetServerConfigUseCase,
	private val saveServerConfigUseCase: SaveServerConfigUseCase,
) : ViewModel() {

	data class State(
		val editableServerConfig: ServerConfig,
		val currentServerConfig: ServerConfig,
		val isIpInvalid: Boolean,
		val isPortInvalid: Boolean
	)

	private val _state: MutableStateFlow<State> = MutableStateFlow(
		State(
			ServerConfig("", ""), ServerConfig("", ""), isIpInvalid = false, isPortInvalid = false
		)
	)
	val state: StateFlow<State> get() = _state.asStateFlow()

	init {
		viewModelScope.launch(Dispatchers.IO) {
			getServerConfigUseCase().collect {
				it?.let {
					val editableServerConfig = _state.value.editableServerConfig
					_state.emit(
						_state.value.copy(
							currentServerConfig = it,
							editableServerConfig = if (editableServerConfig == ServerConfig(
									"",
									""
								)
							) it
							else editableServerConfig
						)
					)
				}
			}
		}
	}

	fun changeIP(newValue: String) {
		viewModelScope.launch {
			val currentServerConfig = _state.value.editableServerConfig
			_state.emit(
				_state.value.copy(
					editableServerConfig = currentServerConfig.copy(
						ip = newValue,
					),
					isIpInvalid = false
				)
			)
		}
	}

	fun changePort(newValue: String) {
		viewModelScope.launch {
			val currentServerConfig = _state.value.editableServerConfig
			_state.emit(
				_state.value.copy(
					editableServerConfig = currentServerConfig.copy(
						port = newValue
					),
					isPortInvalid = false
				),
			)
		}
	}

	fun saveConfig() {
		viewModelScope.launch(Dispatchers.IO) {
			val isIpValid = validateIP(state.value.editableServerConfig.ip)
			val isPortValid = validatePort(state.value.editableServerConfig.port)

			if (isPortValid && isIpValid) {
				saveServerConfigUseCase(
					_state.value.editableServerConfig
				)
			} else {
				_state.emit(
					state.value.copy(
						isPortInvalid = ! isPortValid,
						isIpInvalid = ! isIpValid,
					)
				)
			}
		}
	}

	private fun validatePort(port: String) = try {
		port.toInt() in 0 .. 65535
	} catch (e: NumberFormatException) {
		false
	}

	private fun validateIP(ip: String): Boolean {
		return if (Build.VERSION.SDK_INT > 30) {
			InetAddresses.isNumericAddress(ip)
		} else {
			Patterns.IP_ADDRESS.matcher(ip).matches()
		}
	}
}