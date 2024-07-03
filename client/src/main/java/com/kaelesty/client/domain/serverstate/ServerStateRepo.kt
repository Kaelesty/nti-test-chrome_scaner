package com.kaelesty.client.domain.serverstate

import com.kaelesty.shared.domain.MemoryUsage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface ServerStateRepo {

	fun getMemoryUsageFlow(): Flow<MemoryUsage>

	fun getScanningStateFlow(): StateFlow<Boolean>
}