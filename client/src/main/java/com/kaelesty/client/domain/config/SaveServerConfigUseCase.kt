package com.kaelesty.client.domain.config

import javax.inject.Inject

class SaveServerConfigUseCase @Inject constructor(
	private val repo: ServerConfigRepo
) {

	suspend operator fun invoke(config: ServerConfig) = repo.saveServerConfig(config)
}