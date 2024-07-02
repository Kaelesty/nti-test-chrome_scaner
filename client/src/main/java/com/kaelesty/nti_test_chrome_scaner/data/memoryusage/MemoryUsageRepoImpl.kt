package com.kaelesty.nti_test_chrome_scaner.data.memoryusage

import android.util.Log
import com.kaelesty.nti_test_chrome_scaner.domain.memoryusage.MemoryUsageRepo
import com.kaelesty.shared.domain.MemoryUsage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MemoryUsageRepoImpl @Inject constructor(): MemoryUsageRepo {

	private val _memoryUsageFlow = MutableSharedFlow<MemoryUsage>()

	override fun getMemoryUsageFlow(): SharedFlow<MemoryUsage> {
		return _memoryUsageFlow.asSharedFlow()
	}

	suspend fun setMemoryUsage(memoryUsage: MemoryUsage) {
		Log.d("WebSocketClient", "Memory usage updated ${memoryUsage.toString()}")
		_memoryUsageFlow.emit(
			memoryUsage
		)
	}
}