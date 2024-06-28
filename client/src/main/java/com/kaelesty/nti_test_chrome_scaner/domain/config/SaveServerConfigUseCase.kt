package com.kaelesty.nti_test_chrome_scaner.domain.config

import javax.inject.Inject

class SaveServerConfigUseCase @Inject constructor(
	private val repo: ServerConfigRepo
) {

	suspend operator fun invoke(config: ServerConfig) = repo.saveServerConfig(config)
}