package com.kaelesty.nti_test_chrome_scaner.domain.client

import com.kaelesty.shared.domain.ClientAction
import com.kaelesty.shared.domain.ServerAction
import kotlinx.coroutines.flow.SharedFlow

interface Client {

	val actionsToExecute: SharedFlow<ClientAction>

	fun handleServerAction(action: ServerAction)

	fun executeAction(action: ClientAction)
}