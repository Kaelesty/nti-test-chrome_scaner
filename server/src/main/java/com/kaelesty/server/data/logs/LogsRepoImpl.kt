package com.kaelesty.server.data.logs

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object LogsTool {

	private val stateFlow = MutableStateFlow(listOf<String>())

	suspend fun log(message: String) {
		stateFlow.emit(
			stateFlow.value.toMutableList().apply {
				add(message)
			}
		)
	}

	fun getLogs(): StateFlow<List<String>> {
		return stateFlow.asStateFlow()
	}
}