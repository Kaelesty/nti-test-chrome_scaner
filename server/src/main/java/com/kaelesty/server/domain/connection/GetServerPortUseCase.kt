package com.kaelesty.server.domain.connection

import javax.inject.Inject

class GetServerPortUseCase @Inject constructor(
	private val repo: ConnectionRepo
) {

	operator fun invoke() = repo.getServerPort()
}