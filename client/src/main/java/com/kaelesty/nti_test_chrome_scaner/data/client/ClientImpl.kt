package com.kaelesty.nti_test_chrome_scaner.data.client

import android.util.Log
import com.kaelesty.nti_test_chrome_scaner.data.memoryusage.MemoryUsageRepoImpl
import com.kaelesty.nti_test_chrome_scaner.domain.client.Client
import com.kaelesty.nti_test_chrome_scaner.domain.memoryusage.MemoryUsageRepo
import com.kaelesty.shared.domain.ClientAction
import com.kaelesty.shared.domain.ServerAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ClientImpl @Inject constructor(
	private val memoryUsageRepo: MemoryUsageRepoImpl
): Client {

	private val scope = CoroutineScope(Dispatchers.IO)

	private val _actionsToExecute = MutableSharedFlow<ClientAction>()
	override val actionsToExecute: SharedFlow<ClientAction>
		get() = _actionsToExecute.asSharedFlow()

	override fun handleServerAction(action: ServerAction) {
		when (action) {
			is ServerAction.UpdateMemoryUsage -> {
				scope.launch {
					memoryUsageRepo.setMemoryUsage(action.memoryUsage)
				}
			}
		}
	}

	override fun executeAction(action: ClientAction) {
		scope.launch {
			_actionsToExecute.emit(action)
		}
	}
}