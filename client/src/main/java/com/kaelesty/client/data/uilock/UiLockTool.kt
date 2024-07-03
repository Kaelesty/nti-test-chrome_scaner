package com.kaelesty.client.data.uilock

import com.kaelesty.client.domain.uilock.UiLock
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

object UiLockTool: UiLock {

	private val _stateFlow = MutableStateFlow(false)
	override val stateFlow: StateFlow<Boolean> get() = _stateFlow.asStateFlow()

	private val scope = CoroutineScope(Dispatchers.IO)

	fun setUiLockState(state: Boolean) {
		scope.launch {
			_stateFlow.emit(state)
		}
	}

	override fun lockUi() {
		setUiLockState(false)
	}
}