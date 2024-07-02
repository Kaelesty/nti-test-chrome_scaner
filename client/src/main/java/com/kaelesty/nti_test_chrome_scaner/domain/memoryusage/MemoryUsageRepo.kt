package com.kaelesty.nti_test_chrome_scaner.domain.memoryusage

import com.kaelesty.shared.domain.MemoryUsage
import kotlinx.coroutines.flow.Flow

interface MemoryUsageRepo {

	fun getMemoryUsageFlow(): Flow<MemoryUsage>
}