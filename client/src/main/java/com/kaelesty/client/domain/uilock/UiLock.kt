package com.kaelesty.client.domain.uilock

import kotlinx.coroutines.flow.StateFlow

interface UiLock {

	val stateFlow: StateFlow<Boolean>

	fun lockUi()
}