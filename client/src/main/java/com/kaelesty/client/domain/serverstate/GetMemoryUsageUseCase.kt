package com.kaelesty.client.domain.serverstate

import javax.inject.Inject

class GetMemoryUsageUseCase @Inject constructor(
	private val repo: ServerStateRepo
) {

	operator fun invoke() = repo.getMemoryUsageFlow()
}