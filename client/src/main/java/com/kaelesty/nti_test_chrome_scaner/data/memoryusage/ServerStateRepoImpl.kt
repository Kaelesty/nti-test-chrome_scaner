package com.kaelesty.nti_test_chrome_scaner.data.memoryusage

import android.util.Log
import com.kaelesty.nti_test_chrome_scaner.domain.serverstate.ServerStateRepo
import com.kaelesty.shared.domain.MemoryUsage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow

object ServerStateRepoImpl: ServerStateRepo {

	private val _memoryUsageFlow = MutableSharedFlow<MemoryUsage>()
	private val _scanningStateFlow = MutableStateFlow(false)

	override fun getMemoryUsageFlow(): Flow<MemoryUsage> = flow {
		_memoryUsageFlow.collect{
			emit(it)
		}
	}

	override fun getScanningStateFlow() = _scanningStateFlow.asStateFlow()

	suspend fun setMemoryUsage(memoryUsage: MemoryUsage) {
		_memoryUsageFlow.emit(
			memoryUsage
		)
	}

	suspend fun setScanningStateFlow(state: Boolean) {
		_scanningStateFlow.emit(state)
	}
}