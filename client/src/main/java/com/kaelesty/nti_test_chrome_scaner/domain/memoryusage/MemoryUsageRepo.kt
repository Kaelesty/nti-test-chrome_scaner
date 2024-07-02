package com.kaelesty.nti_test_chrome_scaner.domain.memoryusage

import com.kaelesty.shared.domain.MemoryUsage
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface MemoryUsageRepo {

	fun getMemoryUsageFlow(): SharedFlow<MemoryUsage>
}