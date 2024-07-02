package com.kaelesty.nti_test_chrome_scaner.data.memoryusage

import android.util.Log
import com.kaelesty.nti_test_chrome_scaner.domain.memoryusage.MemoryUsageRepo
import com.kaelesty.shared.domain.MemoryUsage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

object MemoryUsageRepoImpl: MemoryUsageRepo {

	private val _memoryUsageFlow = MutableSharedFlow<MemoryUsage>()

	override fun getMemoryUsageFlow(): Flow<MemoryUsage> = flow {
		Log.d("WebSocketClient", "Observing started")
		_memoryUsageFlow.collect{
			emit(it)
			Log.d("WebSocketClient", "Emitted to cold flow")
		}
	}

	suspend fun setMemoryUsage(memoryUsage: MemoryUsage) {
		Log.d("WebSocketClient", "Memory usage updated ${memoryUsage.toString()}")
		_memoryUsageFlow.emit(
			memoryUsage
		)
	}
}