package com.kaelesty.nti_test_chrome_scaner.domain.memoryusage

import javax.inject.Inject

class GetMemoryUsageUseCase @Inject constructor(
	private val repo: MemoryUsageRepo
) {

	operator fun invoke() = repo.getMemoryUsageFlow()
}