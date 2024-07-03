package com.kaelesty.client.domain.config

import javax.inject.Inject

class GetServerConfigUseCase @Inject constructor(
	private val repo: ServerConfigRepo
) {

	operator fun invoke() = repo.getServerConfig()
}