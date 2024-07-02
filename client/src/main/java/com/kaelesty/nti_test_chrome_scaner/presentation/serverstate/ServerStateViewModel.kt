package com.kaelesty.nti_test_chrome_scaner.presentation.serverstate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaelesty.nti_test_chrome_scaner.domain.client.Client
import com.kaelesty.nti_test_chrome_scaner.domain.memoryusage.GetMemoryUsageUseCase
import com.kaelesty.shared.domain.MemoryUsage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServerStateViewModel @Inject constructor(
	private val getMemoryUsageUseCase: GetMemoryUsageUseCase,
	private val client: Client,
): ViewModel() {

	data class State(
		val memoryUsage: MemoryUsage = MemoryUsage(),
		val connectionState: Client.ConnectionState = Client.ConnectionState.DISCONNECT,
	)

	private val _stateFlow = MutableStateFlow(State())
	val stateFlow: StateFlow<State> get() = _stateFlow.asStateFlow()

	init {
		viewModelScope.launch(Dispatchers.IO) {
			getMemoryUsageUseCase().collect {
				_stateFlow.emit(
					_stateFlow.value.copy(
						memoryUsage = it
					)
				)
			}
		}
		viewModelScope.launch(Dispatchers.IO) {
			client.connectionStateFlow.collect {
				_stateFlow.emit(
					_stateFlow.value.copy(
						connectionState = it
					)
				)
			}
		}
	}
}