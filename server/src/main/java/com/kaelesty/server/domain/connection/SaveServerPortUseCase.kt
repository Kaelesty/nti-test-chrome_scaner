package com.kaelesty.server.domain.connection

import javax.inject.Inject

class SaveServerPortUseCase @Inject constructor(
	private val repo: ConnectionRepo
) {

	suspend operator fun invoke(port: String) = repo.saveServerPort(port)
}