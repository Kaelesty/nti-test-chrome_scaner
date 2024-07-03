package com.kaelesty.nti_test_chrome_scaner.domain.serverstate

import javax.inject.Inject

class GetMemoryUsageUseCase @Inject constructor(
	private val repo: ServerStateRepo
) {

	operator fun invoke() = repo.getMemoryUsageFlow()
}